/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.victorwads.equalsfiles.swing;

// <editor-fold defaultstate="collapsed" desc="Import">
import static br.com.victorwads.equalsfiles.controller.CacheMD5.humamSize;
import br.com.victorwads.equalsfiles.model.Arquivo;
import br.com.victorwads.equalsfiles.model.Resultado;
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
	private final Resultado resultado;
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
	 * @param resultado
	 * @param view
	 */
	public CardResultado(Resultado resultado, View view) {
		initComponents();
		setVisible(true);
		btnRemover.setVisible(false);
		ordem = resultado.getSortedHashs();
		this.resultado = resultado;
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
			total += resultado.get(key).size();
		}
		barLoading.setString("Carregando ícones");
		barLoading.setMaximum(total);

		//Filtrando Dados;
		ArrayList<String> exts = new ArrayList<>();
		long max = 0, maxInt = 0;
		for (String key : ordem) {
			ArrayList<Arquivo> arquivos = resultado.get(key);
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
			ArrayList<Arquivo> arquivos = resultado.get(key);
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
				resultado.removeFile(toDelete[i]);
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
    private void initComponents() {

        digRemover = new javax.swing.JDialog();
        javax.swing.JLabel lbl1 = new javax.swing.JLabel();
        lblItens = new javax.swing.JLabel();
        javax.swing.JLabel lbl2 = new javax.swing.JLabel();
        lblItensSize = new javax.swing.JLabel();
        javax.swing.JLabel lbl3 = new javax.swing.JLabel();
        btnPermitir = new javax.swing.JButton();
        btnCancelar = new javax.swing.JButton();
        javax.swing.JButton btnListar = new javax.swing.JButton();
        panelChecksRemover = new javax.swing.JPanel();
        radioLixeira = new javax.swing.JRadioButton();
        radioPermanente = new javax.swing.JRadioButton();
        remocaoBar = new javax.swing.JProgressBar();
        removerRadioGroup = new javax.swing.ButtonGroup();
        lblRangeVal = new javax.swing.JLabel();
        lblRangeExt = new javax.swing.JLabel();
        rngTamanho = new br.com.victorwads.equalsfiles.swing.components.RangeSlider();
        arvoreScroll = new javax.swing.JScrollPane();
        arvore = new br.com.victorwads.equalsfiles.swing.components.CheckBoxTree();
        barLoading = new javax.swing.JProgressBar();
        btnRemover = new javax.swing.JButton();

        digRemover.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        digRemover.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        digRemover.setModal(true);
        digRemover.setResizable(false);
        digRemover.setSize(new java.awt.Dimension(0, 0));
        digRemover.setType(java.awt.Window.Type.POPUP);

        lbl1.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lbl1.setText("Deseja realmente remover");

        lblItens.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblItens.setForeground(new java.awt.Color(255, 0, 0));
        lblItens.setText("x");

        lbl2.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lbl2.setText("itens? Serão liberados");

        lblItensSize.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblItensSize.setForeground(new java.awt.Color(0, 153, 0));
        lblItensSize.setText("xB");

        lbl3.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lbl3.setText("de espaço em disco.");

        btnPermitir.setText("Permitir");
        btnPermitir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPermitirActionPerformed(evt);
            }
        });

        btnCancelar.setText("Cancelar");
        btnCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelarActionPerformed(evt);
            }
        });

        btnListar.setText("Visualizar Lista");
        btnListar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnListarActionPerformed(evt);
            }
        });

        removerRadioGroup.add(radioLixeira);
        radioLixeira.setText("Mover Para Lixeira");
        panelChecksRemover.add(radioLixeira);

        removerRadioGroup.add(radioPermanente);
        radioPermanente.setForeground(new java.awt.Color(255, 0, 0));
        radioPermanente.setText("Remover Definitivamente");
        panelChecksRemover.add(radioPermanente);

        javax.swing.GroupLayout digRemoverLayout = new javax.swing.GroupLayout(digRemover.getContentPane());
        digRemover.getContentPane().setLayout(digRemoverLayout);
        digRemoverLayout.setHorizontalGroup(
            digRemoverLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(digRemoverLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(digRemoverLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(remocaoBar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, digRemoverLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btnPermitir)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnListar)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnCancelar))
                    .addComponent(panelChecksRemover, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(digRemoverLayout.createSequentialGroup()
                        .addComponent(lbl1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblItens)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lbl2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblItensSize)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lbl3)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        digRemoverLayout.setVerticalGroup(
            digRemoverLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(digRemoverLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(digRemoverLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbl1)
                    .addComponent(lblItens)
                    .addComponent(lbl2)
                    .addComponent(lblItensSize)
                    .addComponent(lbl3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panelChecksRemover, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(remocaoBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(digRemoverLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnListar)
                    .addComponent(btnPermitir)
                    .addComponent(btnCancelar))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        setMinimumSize(new java.awt.Dimension(800, 400));
        setPreferredSize(new java.awt.Dimension(800, 400));

        lblRangeVal.setText("0 B");

        lblRangeExt.setText("MAX B");

        rngTamanho.setMaximum(1);
        rngTamanho.setValue(0);
        rngTamanho.setEnabled(false);
        rngTamanho.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                rngTamanhoStateChanged(evt);
            }
        });

        arvore.setMinimumSize(new java.awt.Dimension(0, 100));
        arvore.setRootVisible(false);
        arvoreScroll.setViewportView(arvore);

        barLoading.setString("");
        barLoading.setStringPainted(true);

        btnRemover.setText("Remover Selecionados");
        btnRemover.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRemoverActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(barLoading, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(arvoreScroll, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(lblRangeVal)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(rngTamanho, javax.swing.GroupLayout.DEFAULT_SIZE, 788, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblRangeExt))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btnRemover)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(barLoading, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnRemover)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblRangeExt)
                    .addComponent(rngTamanho, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblRangeVal))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(arvoreScroll, javax.swing.GroupLayout.DEFAULT_SIZE, 298, Short.MAX_VALUE)
                .addContainerGap())
        );
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
    private br.com.victorwads.equalsfiles.swing.components.CheckBoxTree arvore;
    private javax.swing.JScrollPane arvoreScroll;
    private javax.swing.JProgressBar barLoading;
    private javax.swing.JButton btnCancelar;
    private javax.swing.JButton btnPermitir;
    private javax.swing.JButton btnRemover;
    private javax.swing.JDialog digRemover;
    private javax.swing.JLabel lblItens;
    private javax.swing.JLabel lblItensSize;
    private javax.swing.JLabel lblRangeExt;
    private javax.swing.JLabel lblRangeVal;
    private javax.swing.JPanel panelChecksRemover;
    private javax.swing.JRadioButton radioLixeira;
    private javax.swing.JRadioButton radioPermanente;
    private javax.swing.JProgressBar remocaoBar;
    private javax.swing.ButtonGroup removerRadioGroup;
    private br.com.victorwads.equalsfiles.swing.components.RangeSlider rngTamanho;
    // End of variables declaration//GEN-END:variables
	// </editor-fold>
}
