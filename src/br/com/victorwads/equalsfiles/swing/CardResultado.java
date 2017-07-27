/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.victorwads.equalsfiles.swing;

// <editor-fold defaultstate="collapsed" desc="Import">
import java.awt.*;
import javax.swing.*;
import javax.swing.GroupLayout;
import javax.swing.LayoutStyle;
import br.com.victorwads.equalsfiles.swing.components.*;
import static br.com.victorwads.equalsfiles.controller.CacheMD5.humamSize;
import br.com.victorwads.equalsfiles.model.Arquivo;
import br.com.victorwads.equalsfiles.model.Resulted;
import br.com.victorwads.equalsfiles.swing.components.Card;
import br.com.victorwads.equalsfiles.swing.components.CheckBoxTree;
import br.com.victorwads.equalsfiles.swing.components.CheckBoxTree.DefaultNode;
import br.com.victorwads.equalsfiles.swing.components.Janela;
import br.com.victorwads.equalsfiles.swing.components.icones.GetIcone;
import br.com.victorwads.equalsfiles.swing.util.Dialogos;
import com.sun.jna.platform.FileUtils;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ComponentEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JTree;
import javax.swing.WindowConstants;
import javax.swing.border.MatteBorder;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeCellRenderer;
import javax.swing.tree.TreeSelectionModel;
// </editor-fold>

/**
 *
 * @author victo
 */
public class CardResultado extends Card {

	// <editor-fold defaultstate="collapsed" desc="Interface e Class">
	/**
	 * Interface to displays duplicate files info to user view.
	 */
	public interface View {

		/**
		 * Set duplicate files info to the user view
		 *
		 * @param tamanho human readable total sum duplicates files sizes
		 * @param quantidade amount of duplicate files
		 */
		public void setDuplicates(long tamanho, int quantidade);

		public void setCmbExtencoes(DefaultComboBoxModel<String> model);
	}

	private class DuplicataNode extends CheckBoxTree.NoSelectionNode {

		private final String text, size, quantidade;

		public DuplicataNode(String text, long size, int quantidade) {
			super(null);
			this.text = text;
			this.size = humamSize(size);
			this.quantidade = humamSize(size * quantidade);
			setUserObject(this);
		}

		public String getText() {
			return text;
		}

		public String getSize() {
			return size;
		}

		public String getQuantidade() {
			return quantidade;
		}

	}

	private class ResultadoCellRender implements TreeCellRenderer {

		private final JComponent container = new JComponent() {
		};
		private final DefaultTreeCellRenderer renderer = new DefaultTreeCellRenderer();
		private final MatteBorder BORDER_FOCUS = BorderFactory.createMatteBorder(0, 0, 1, 0, Color.BLUE);
		private final MatteBorder EMPTY_BORDER = BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(0, 0, 0, 0));
		private final Color gray = new Color(119, 119, 119);
		private final JLabel label = new JLabel(), label_size = new JLabel(), label_quantidade = new JLabel();
		private JLabel label_text;

		public ResultadoCellRender() {
			super();
			container.setVisible(true);
			container.setLayout(new FlowLayout());
			container.setOpaque(false);
			Font f = label_size.getFont();
			f = new Font(f.getName(), 0, f.getSize() - 1);
			label_size.setFont(f);
			label_quantidade.setFont(f);
			label_quantidade.setForeground(new Color(221, 0, 0));
		}

		@Override
		public Component getTreeCellRendererComponent(JTree jtree, Object o, boolean selecionado, boolean expandido, boolean folha, int row, boolean hasFocus) {
			Object userObject;
			if (o instanceof DefaultMutableTreeNode && (userObject = ((DefaultMutableTreeNode) o).getUserObject()) instanceof Arquivo) {
				Arquivo a = (Arquivo) userObject;
				label.setText(a.toString());
				label.setIcon(a.getIcone());
				label.setBorder(hasFocus ? BORDER_FOCUS : EMPTY_BORDER);
				label.setForeground(selecionado ? new Color(120, 0, 0) : new Color(0, 70, 0));
				a.setToExclude(selecionado);
				return label;
			} else if (o instanceof DuplicataNode) {
				DuplicataNode n = (DuplicataNode) o;
				label_text = (JLabel) renderer.getTreeCellRendererComponent(jtree, n.getText(), false, true, false, 0, false);
				label_text.setForeground(gray);
				label_size.setText(n.getSize());
				label_quantidade.setText(n.getQuantidade());
				container.add(label_text);
				container.add(label_size);
				container.add(label_quantidade);
				return container;
			}
			return renderer.getTreeCellRendererComponent(jtree, o, selecionado, expandido, folha, row, hasFocus);
		}
	}
	// </editor-fold>

	private final DefaultComboBoxModel<String> extensoes = new DefaultComboBoxModel<>();
	private final Resulted resulted;
	private final String[] ordem;
	private final Thread loadThread;
	private final FileUtils fileutils;
	private Arquivo[] toDelete;
	private View view;
	private String extensao = null;
	private boolean init = false;
	long minSize, maxSize;

	/**
	 * Creates new panel witch shows de EqualFiles process result.
	 *
	 * @param resulted
	 * @param view
	 */
	public CardResultado(Resulted resulted, View view) {
		initComponents();
		setVisible(true);
		btnRemover.setVisible(false);
		ordem = resulted.getSortedHashs();
		this.resulted = resulted;
		this.view = view;

		//Regras da arvore
		arvore.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		arvore.setCellRenderer(new ResultadoCellRender());
		arvore.setModel(new DefaultTreeModel(new DefaultMutableTreeNode("")));
		arvoreScroll.setVisible(false);

		loadThread = new Thread(CardResultado.this::loadDados);
		loadThread.start();

		fileutils = FileUtils.getInstance();
		panelChecksRemover.setVisible(fileutils.hasTrash());
	}

	// <editor-fold defaultstate="collapsed" desc="Methodos">
	public void setExtensao(String extensao) {
		this.extensao = extensao;
		listar();
	}

	public Arquivo[] getSelectedFiles() {
		DefaultNode[] nodes = arvore.getSelectedCheckBoxNodes();
		Arquivo[] arquivos = new Arquivo[nodes.length];
		int i = 0;
		for (DefaultNode node : nodes) {
			arquivos[i] = (Arquivo) node.getUserObject();
			i++;
		}
		return arquivos;
	}

	private void loadDados() {

		//Getting Total
		int total = 0, i = 0;
		for (String key : ordem) {
			total += resulted.get(key).size();
		}
		barLoading.setString("Carregando ícones");
		barLoading.setMaximum(total);

		//Filtrando Dados;
		ArrayList<String> exts = new ArrayList<>();
		long max = 0, maxInt = 0;
		for (String key : ordem) {
			ArrayList<Arquivo> arquivos = resulted.get(key);
			for (Arquivo a : arquivos) {
				GetIcone.getArquivoIcone(a);
				a.setToExclude(true);
				i++;
				barLoading.setValue(i);
			}
			Arquivo a = arquivos.get(0);
			a.setToExclude(false);
			if (max < a.getSize()) {
				maxInt = (int) (a.getSize() / 1024);
				max = a.getSize();
			}
			if (exts.indexOf(a.extencao) == -1) {
				exts.add(a.extencao);
			}
		}

		//Lista de Extenções
		extensoes.addElement("Todas");
		Collections.sort(exts);
		for (String ext : exts) {
			extensoes.addElement(ext);
		}

		//Atualizando view
		view.setCmbExtencoes(extensoes);
		rngTamanho.setMaximum((int) maxInt);
		rngTamanho.setExtent(rngTamanho.getMaximum());
		rngTamanho.setEnabled(true);
		init = true;
		listar();
	}

	private void listar() {
		DefaultMutableTreeNode root = new DefaultMutableTreeNode("Root");

		long totalToFree = 0;
		int quantidade = 0;
		for (String key : ordem) {
			ArrayList<Arquivo> arquivos = resulted.get(key);
			if (arquivos == null) {
				continue;
			}
			Arquivo first = arquivos.get(0);
			if ((extensao != null && !first.extencao.equals(extensao)) || first.getSize() < minSize || first.getSize() > maxSize) {
				continue;
			}
			quantidade++;
			totalToFree += first.getSize() * (arquivos.size() - 1);

			DefaultMutableTreeNode md5Node = new DuplicataNode(arquivos.get(0).nome, first.getSize(), arquivos.size());
			for (Arquivo arquivo : arquivos) {
				md5Node.add(new DefaultNode(arquivo, arquivo.isToExclude()));
			}
			root.add(md5Node);
		}

		DefaultTreeModel t = new DefaultTreeModel(root);
		arvore.setModel(t);
		arvore.expandAll();
		arvoreScroll.setVisible(true);
		barLoading.setVisible(false);
		btnRemover.setVisible(true);

		if (view != null) {
			view.setDuplicates(totalToFree, quantidade);
		}
	}

	private void attRange() {
		minSize = (long) rngTamanho.getValue() * 1024l;
		maxSize = ((long) rngTamanho.getExtent() + (long) rngTamanho.getValue()) * 1024l;
		lblRangeVal.setText(humamSize(minSize));
		lblRangeExt.setText(humamSize(maxSize));
	}

	private void removerSelecionados() {
		boolean toSpace = !fileutils.hasTrash() || radioPermanente.isSelected();
		boolean toTrash = radioLixeira.isSelected();
		if (!toSpace && !toTrash) {
			Dialogos.erro("Selecione o tipo de exclusão!");
			return;
		}
		if (!toTrash && !Dialogos.pergunta("Realmente deseja mandar estes itens Para o Espaço Sideral!?")) {
			return;
		}
		btnPermitir.setEnabled(false);
		btnCancelar.setEnabled(false);
		digRemover.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		new Thread(() -> {
			remocaoBar.setMaximum(toDelete.length);
			for (int i = 0; i < toDelete.length; i++) {
				if (toTrash) {
					try {
						fileutils.moveToTrash(new File[]{new File(toDelete[i].getFullName())});
					} catch (Exception e) {
					}
				} else if (toSpace) {
					new File(toDelete[i].getFullName()).delete();
				}
				remocaoBar.setValue(i);
				resulted.removeFile(toDelete[i]);
			}
			digRemover.dispose();
			listar();
		}).start();
	}

	private void listarSelecionados() {
		Dialogos.erro("Em breve");
	}

	private void setRemocao() {
		DefaultNode[] nodes = arvore.getSelectedCheckBoxNodes();
		toDelete = new Arquivo[nodes.length];
		long size = 0;
		for (int i = 0; i < nodes.length; i++) {
			toDelete[i] = (Arquivo) nodes[i].getUserObject();
			size += toDelete[i].getSize();
		}
		remocaoBar.setValue(0);
		removerRadioGroup.clearSelection();
		btnPermitir.setEnabled(true);
		btnCancelar.setEnabled(true);
		lblItens.setText(Integer.toString(nodes.length));
		lblItensSize.setText(humamSize(size));
		digRemover.pack();
		Janela.center(digRemover);
		digRemover.setVisible(true);
		digRemover.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
	}

	// </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
	// Generated using JFormDesigner Evaluation license - Victor Cleber Laureano
	private void initComponents() {
		lblRangeVal = new JLabel();
		lblRangeExt = new JLabel();
		rngTamanho = new RangeSlider();
		arvoreScroll = new JScrollPane();
		arvore = new CheckBoxTree();
		barLoading = new JProgressBar();
		btnRemover = new JButton();
		digRemover = new JDialog();
		JLabel lbl1 = new JLabel();
		lblItens = new JLabel();
		JLabel lbl2 = new JLabel();
		lblItensSize = new JLabel();
		JLabel lbl3 = new JLabel();
		btnPermitir = new JButton();
		btnCancelar = new JButton();
		JButton btnListar = new JButton();
		panelChecksRemover = new JPanel();
		radioLixeira = new JRadioButton();
		radioPermanente = new JRadioButton();
		remocaoBar = new JProgressBar();
		removerRadioGroup = new ButtonGroup();

		//======== this ========
		setMinimumSize(new Dimension(800, 400));
		setPreferredSize(new Dimension(800, 400));



		//---- lblRangeVal ----
		lblRangeVal.setText("0 B");

		//---- lblRangeExt ----
		lblRangeExt.setText("MAX B");

		//---- rngTamanho ----
		rngTamanho.setMaximum(1);
		rngTamanho.setValue(0);
		rngTamanho.setEnabled(false);
		rngTamanho.addChangeListener(e -> rngTamanhoStateChanged(e));

		//======== arvoreScroll ========
		{

			//---- arvore ----
			arvore.setMinimumSize(new Dimension(0, 100));
			arvore.setRootVisible(false);
			arvoreScroll.setViewportView(arvore);
		}

		//---- barLoading ----
		barLoading.setString("");
		barLoading.setStringPainted(true);

		//---- btnRemover ----
		btnRemover.setText("Remover Selecionados");
		btnRemover.addActionListener(e -> btnRemoverActionPerformed(e));

		GroupLayout layout = new GroupLayout(this);
		setLayout(layout);
		layout.setHorizontalGroup(
			layout.createParallelGroup()
				.addGroup(layout.createSequentialGroup()
					.addContainerGap()
					.addGroup(layout.createParallelGroup()
						.addComponent(barLoading, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addComponent(arvoreScroll, GroupLayout.Alignment.TRAILING)
						.addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
							.addComponent(lblRangeVal)
							.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
							.addComponent(rngTamanho, GroupLayout.DEFAULT_SIZE, 324, Short.MAX_VALUE)
							.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
							.addComponent(lblRangeExt))
						.addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
							.addGap(0, 0, Short.MAX_VALUE)
							.addComponent(btnRemover)))
					.addContainerGap())
		);
		layout.setVerticalGroup(
			layout.createParallelGroup()
				.addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
					.addContainerGap()
					.addComponent(barLoading, GroupLayout.PREFERRED_SIZE, 23, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
					.addComponent(btnRemover)
					.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
					.addGroup(layout.createParallelGroup()
						.addComponent(lblRangeExt)
						.addComponent(rngTamanho, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblRangeVal))
					.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
					.addComponent(arvoreScroll, GroupLayout.DEFAULT_SIZE, 199, Short.MAX_VALUE)
					.addContainerGap())
		);

		//======== digRemover ========
		{
			digRemover.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
			digRemover.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			digRemover.setModal(true);
			digRemover.setResizable(false);
			digRemover.setSize(new Dimension(0, 0));
			Container digRemoverContentPane = digRemover.getContentPane();

			//---- lbl1 ----
			lbl1.setFont(new Font("Tahoma", Font.PLAIN, 12));
			lbl1.setText("Deseja realmente remover");

			//---- lblItens ----
			lblItens.setFont(new Font("Tahoma", Font.PLAIN, 12));
			lblItens.setForeground(Color.red);
			lblItens.setText("x");

			//---- lbl2 ----
			lbl2.setFont(new Font("Tahoma", Font.PLAIN, 12));
			lbl2.setText("itens? Ser\u00e3o liberados");

			//---- lblItensSize ----
			lblItensSize.setFont(new Font("Tahoma", Font.PLAIN, 12));
			lblItensSize.setForeground(new Color(0, 153, 0));
			lblItensSize.setText("xB");

			//---- lbl3 ----
			lbl3.setFont(new Font("Tahoma", Font.PLAIN, 12));
			lbl3.setText("de espa\u00e7o em disco.");

			//---- btnPermitir ----
			btnPermitir.setText("Permitir");
			btnPermitir.addActionListener(e -> btnPermitirActionPerformed(e));

			//---- btnCancelar ----
			btnCancelar.setText("Cancelar");
			btnCancelar.addActionListener(e -> btnCancelarActionPerformed(e));

			//---- btnListar ----
			btnListar.setText("Visualizar Lista");
			btnListar.addActionListener(e -> btnListarActionPerformed(e));

			//======== panelChecksRemover ========
			{

				// JFormDesigner evaluation mark
				panelChecksRemover.setBorder(new javax.swing.border.CompoundBorder(
					new javax.swing.border.TitledBorder(new javax.swing.border.EmptyBorder(0, 0, 0, 0),
						"JFormDesigner Evaluation", javax.swing.border.TitledBorder.CENTER,
						javax.swing.border.TitledBorder.BOTTOM, new java.awt.Font("Dialog", java.awt.Font.BOLD, 12),
						java.awt.Color.red), panelChecksRemover.getBorder())); panelChecksRemover.addPropertyChangeListener(new java.beans.PropertyChangeListener(){public void propertyChange(java.beans.PropertyChangeEvent e){if("border".equals(e.getPropertyName()))throw new RuntimeException();}});

				panelChecksRemover.setLayout(new FlowLayout());

				//---- radioLixeira ----
				radioLixeira.setText("Mover Para Lixeira");
				panelChecksRemover.add(radioLixeira);

				//---- radioPermanente ----
				radioPermanente.setForeground(Color.red);
				radioPermanente.setText("Remover Definitivamente");
				panelChecksRemover.add(radioPermanente);
			}

			GroupLayout digRemoverContentPaneLayout = new GroupLayout(digRemoverContentPane);
			digRemoverContentPane.setLayout(digRemoverContentPaneLayout);
			digRemoverContentPaneLayout.setHorizontalGroup(
				digRemoverContentPaneLayout.createParallelGroup()
					.addGroup(digRemoverContentPaneLayout.createSequentialGroup()
						.addContainerGap()
						.addGroup(digRemoverContentPaneLayout.createParallelGroup()
							.addComponent(remocaoBar, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
							.addGroup(GroupLayout.Alignment.TRAILING, digRemoverContentPaneLayout.createSequentialGroup()
								.addGap(0, 0, Short.MAX_VALUE)
								.addComponent(btnPermitir)
								.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
								.addComponent(btnListar)
								.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
								.addComponent(btnCancelar))
							.addComponent(panelChecksRemover, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
							.addGroup(digRemoverContentPaneLayout.createSequentialGroup()
								.addComponent(lbl1)
								.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
								.addComponent(lblItens)
								.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
								.addComponent(lbl2)
								.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
								.addComponent(lblItensSize)
								.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
								.addComponent(lbl3)
								.addGap(0, 0, Short.MAX_VALUE)))
						.addContainerGap())
			);
			digRemoverContentPaneLayout.setVerticalGroup(
				digRemoverContentPaneLayout.createParallelGroup()
					.addGroup(digRemoverContentPaneLayout.createSequentialGroup()
						.addContainerGap()
						.addGroup(digRemoverContentPaneLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
							.addComponent(lbl1)
							.addComponent(lblItens)
							.addComponent(lbl2)
							.addComponent(lblItensSize)
							.addComponent(lbl3))
						.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
						.addComponent(panelChecksRemover, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
						.addComponent(remocaoBar, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
						.addGroup(digRemoverContentPaneLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
							.addComponent(btnListar)
							.addComponent(btnCancelar)
							.addComponent(btnPermitir))
						.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
			);
		}

		//---- removerRadioGroup ----
		removerRadioGroup.add(radioLixeira);
		removerRadioGroup.add(radioPermanente);
    }// </editor-fold>//GEN-END:initComponents
	// <editor-fold defaultstate="collapsed" desc="Eventos">
    private void rngTamanhoStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_rngTamanhoStateChanged
		attRange();
		if (init) {
			listar();
		}
    }//GEN-LAST:event_rngTamanhoStateChanged

    private void btnCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarActionPerformed
		digRemover.dispose();
    }//GEN-LAST:event_btnCancelarActionPerformed

    private void btnListarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnListarActionPerformed
		listarSelecionados();
    }//GEN-LAST:event_btnListarActionPerformed

    private void btnPermitirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPermitirActionPerformed
		removerSelecionados();
    }//GEN-LAST:event_btnPermitirActionPerformed

    private void btnRemoverActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRemoverActionPerformed
		setRemocao();
    }//GEN-LAST:event_btnRemoverActionPerformed

	@Override
	public void cardHide(ComponentEvent evt) {
		if (!getParentJFrame().isVisible()) {
			try {
				loadThread.join();
			} catch (Exception e) {
			}
			view = null;
			getParentJFrame().dispose();
		}
	}
	//</editor-fold	>
	// <editor-fold defaultstate="collapsed" desc="Variáveis">
    // Variables declaration - do not modify//GEN-BEGIN:variables
	// Generated using JFormDesigner Evaluation license - Victor Cleber Laureano
	private JLabel lblRangeVal;
	private JLabel lblRangeExt;
	private RangeSlider rngTamanho;
	private JScrollPane arvoreScroll;
	private CheckBoxTree arvore;
	private JProgressBar barLoading;
	private JButton btnRemover;
	private JDialog digRemover;
	private JLabel lblItens;
	private JLabel lblItensSize;
	private JButton btnPermitir;
	private JButton btnCancelar;
	private JPanel panelChecksRemover;
	private JRadioButton radioLixeira;
	private JRadioButton radioPermanente;
	private JProgressBar remocaoBar;
	private ButtonGroup removerRadioGroup;
    // End of variables declaration//GEN-END:variables
	// </editor-fold>
}
