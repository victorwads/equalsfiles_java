/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.victorwads.equalsfiles.swing;

// <editor-fold defaultstate="collapsed" desc="Imports">
import java.awt.*;
import javax.swing.*;
import javax.swing.GroupLayout;
import javax.swing.LayoutStyle;
import static br.com.victorwads.equalsfiles.controller.CacheMD5.humamSize;
import br.com.victorwads.equalsfiles.controller.Processa;
import br.com.victorwads.equalsfiles.controller.ProcessaInterface;
import br.com.victorwads.equalsfiles.controller.Resultados;
import br.com.victorwads.equalsfiles.model.Diretorio;
import br.com.victorwads.equalsfiles.model.Resulted;
import br.com.victorwads.equalsfiles.swing.components.Card.CardName;
import br.com.victorwads.equalsfiles.swing.components.Janela;
import br.com.victorwads.equalsfiles.swing.util.DateTime;
import br.com.victorwads.equalsfiles.swing.util.Dialogos;
import java.awt.CardLayout;
import java.text.DecimalFormat;
import javax.swing.DefaultComboBoxModel;
// </editor-fold>

/**
 *
 * @author victo
 */
public class WindowProcesso extends Janela implements ProcessaInterface, CardResultado.View {

	private final CardName CARD_RESULT = new CardName();
	private final Resulted resulted;
	private final String TITLE_RUNNING = "Comparando arquivos de ";
	private final String TITLE_FINISH = "Comparação de arquivos de ";
	private final Processa processa;
	private final Thread processaThread;
	private CardResultado cardResultado;
	private float total;
	private boolean running = false;
	private String title;

	/**
	 * Creates new form EqualsFiles
	 *
	 * @param resulted
	 */
	public WindowProcesso(Resulted resulted) {
		setTitle(resulted.getDiretorios());
		setTitle(TITLE_RUNNING + title);
		initComponents();
		setVisible(true);

		this.resulted = resulted;
		if (resulted.getId() > 0) {
			processa = null;
			processaThread = null;
			setFilesAmount(resulted.getQuantidadeArquivos());
			setFilesSize(resulted.getTamanhoArquivos());
			setDuration(resulted.getDuracao());
			finish();
		} else {
			processa = new Processa(this, resulted);
			processaThread = new Thread(processa);
			processaThread.start();
			running = true;
		}
	}

	// <editor-fold defaultstate="collapsed" desc="Methodos">
	private void setTitle(Diretorio[] diretorios) {
		String[] diretoriosStrings = new String[diretorios.length];
		for (int i = 0; i < diretorios.length; i++) {
			diretoriosStrings[i] = diretorios[i].toString();
		}
		title = String.join(" , ", diretoriosStrings);
	}

	private void setStoped() {
		running = false;
		btnAcao.setVisible(false);
		btnAcao.setText("Salvar");
	}

	private boolean parar() {
		if (running) {
			if (Dialogos.pergunta("Você quer cancelar este processo?")) {
				processa.stop();
				try {
					processaThread.join();
				} catch (Exception e) {
				}
				setStoped();
			} else {
				return false;
			}
		}
		return true;
	}

	@Override
	public final void setDuration(int segundos) {
		txtDuracao.setText(DateTime.getDurationStringFromSeconds(segundos));
	}

	@Override
	public void clear() {
		lblProgresso.setText("");
		txtDuracao.setText("00:00:00");
		txtArquivos.setText("-");
		txtTamanho.setText("-");
		barProgresso.setString("");
		barProgresso.setValue(0);
	}

	@Override
	public void setLoadingTotal(int i) {
		barProgresso.setIndeterminate(false);
		barProgresso.setMaximum(i);
		total = (float) i;
	}

	@Override
	public void loading(int i) {
		barProgresso.setValue(i);
		barProgresso.setString(new DecimalFormat("#.##").format((float) i / total * 100f) + " %");
	}

	@Override
	public void loading(String path, int i) {
		lblProgresso.setText(path);
		loading(i);
	}

	@Override
	public void loading(String info, boolean infinita) {
		lblProgresso.setText(info);
		barProgresso.setIndeterminate(infinita);
	}

	@Override
	public void setDuplicates(long tamanho, int quantidade) {
		txtTamanhoDuplicatas.setText(humamSize(tamanho));
		txtDuplicatas.setText(Integer.toString(quantidade));
	}

	@Override
	public final void setFilesAmount(int amount) {
		txtArquivos.setText(Integer.toString(amount));
	}

	@Override
	public final void setFilesSize(long size) {
		txtTamanho.setText(humamSize(size));
	}

	@Override
	public void setCmbExtencoes(DefaultComboBoxModel<String> model) {
		cmbExtencoes.setModel(model);
		if (model.getSize() > 2) {
			cmbExtencoes.setSelectedIndex(2);
		} else if (model.getSize() == 2) {
			cmbExtencoes.setSelectedIndex(1);
		}
		cmbExtencoes.setEnabled(true);
	}

	@Override
	public void dispose() {
		if (parar()) {
			super.dispose(); //To change body of generated methods, choose Tools | Templates.
		}
	}

	/**
	 *
	 */
	@Override
	public final void finish() {
		setStoped();
		setTitle(TITLE_FINISH + title);
		cardResultado = new CardResultado(resulted, this);
		resultPane.add(cardResultado, CARD_RESULT.NAME);
		((CardLayout) resultPane.getLayout()).show(resultPane, CARD_RESULT.NAME);
		if (resulted.getId() == 0) {
			btnAcao.setVisible(true);
		}
		pack();
	}
	// </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
	// Generated using JFormDesigner Evaluation license - Victor Cleber Laureano
	private void initComponents() {
		JLabel lblA = new JLabel();
		txtArquivos = new JTextField();
		txtTamanho = new JTextField();
		txtDuracao = new JTextField();
		JLabel lblDuracao = new JLabel();
		JLabel lblT1 = new JLabel();
		txtTamanhoDuplicatas = new JTextField();
		txtDuplicatas = new JTextField();
		resultPane = new JPanel();
		jPanel1 = new JPanel();
		lblProgresso = new JLabel();
		barProgresso = new JProgressBar();
		lblExtensoes = new JLabel();
		cmbExtencoes = new JComboBox<>();
		btnAcao = new JButton();

		//======== this ========
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		Container contentPane = getContentPane();

		//---- lblA ----
		lblA.setText("Arquivos");

		//---- txtArquivos ----
		txtArquivos.setEditable(false);
		txtArquivos.setForeground(Color.red);
		txtArquivos.setHorizontalAlignment(SwingConstants.CENTER);
		txtArquivos.setText("-");

		//---- txtTamanho ----
		txtTamanho.setEditable(false);
		txtTamanho.setForeground(new Color(0, 204, 51));
		txtTamanho.setHorizontalAlignment(SwingConstants.CENTER);
		txtTamanho.setText("-");

		//---- txtDuracao ----
		txtDuracao.setEditable(false);
		txtDuracao.setForeground(new Color(0, 153, 255));
		txtDuracao.setHorizontalAlignment(SwingConstants.CENTER);
		txtDuracao.setText("00:00:00");

		//---- lblDuracao ----
		lblDuracao.setText("Dura\u00e7\u00e3o");

		//---- lblT1 ----
		lblT1.setText("Duplicatas");

		//---- txtTamanhoDuplicatas ----
		txtTamanhoDuplicatas.setEditable(false);
		txtTamanhoDuplicatas.setForeground(new Color(0, 204, 51));
		txtTamanhoDuplicatas.setHorizontalAlignment(SwingConstants.CENTER);
		txtTamanhoDuplicatas.setText("-");

		//---- txtDuplicatas ----
		txtDuplicatas.setEditable(false);
		txtDuplicatas.setForeground(new Color(0, 204, 51));
		txtDuplicatas.setHorizontalAlignment(SwingConstants.CENTER);
		txtDuplicatas.setText("-");

		//======== resultPane ========
		{

			resultPane.setLayout(new CardLayout());

			//======== jPanel1 ========
			{

				//---- lblProgresso ----
				lblProgresso.setFont(new Font("Tahoma", Font.PLAIN, 10));
				lblProgresso.setForeground(new Color(102, 0, 102));
				lblProgresso.setText("Progresso");
				lblProgresso.setFocusable(false);
				lblProgresso.setIconTextGap(0);

				//---- barProgresso ----
				barProgresso.setToolTipText("");
				barProgresso.setFocusable(false);
				barProgresso.setString("");
				barProgresso.setStringPainted(true);

				GroupLayout jPanel1Layout = new GroupLayout(jPanel1);
				jPanel1.setLayout(jPanel1Layout);
				jPanel1Layout.setHorizontalGroup(
					jPanel1Layout.createParallelGroup()
						.addGroup(jPanel1Layout.createParallelGroup()
							.addGroup(jPanel1Layout.createSequentialGroup()
								.addContainerGap()
								.addGroup(jPanel1Layout.createParallelGroup()
									.addComponent(barProgresso, GroupLayout.Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
									.addComponent(lblProgresso, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
								.addContainerGap()))
						.addGap(0, 850, Short.MAX_VALUE)
				);
				jPanel1Layout.setVerticalGroup(
					jPanel1Layout.createParallelGroup()
						.addGroup(jPanel1Layout.createParallelGroup()
							.addGroup(jPanel1Layout.createSequentialGroup()
								.addContainerGap()
								.addComponent(lblProgresso)
								.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
								.addComponent(barProgresso, GroupLayout.PREFERRED_SIZE, 20, GroupLayout.PREFERRED_SIZE)
								.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
						.addGap(0, 0, Short.MAX_VALUE)
				);
			}
			resultPane.add(jPanel1, "card3");
		}

		//---- lblExtensoes ----
		lblExtensoes.setText("Exten\u00e7\u00f5es");

		//---- cmbExtencoes ----
		cmbExtencoes.setModel(new DefaultComboBoxModel<>(new String[] {

		}));
		cmbExtencoes.setEnabled(false);
		cmbExtencoes.addActionListener(e -> cmbExtencoes(e));

		//---- btnAcao ----
		btnAcao.setText("Parar");
		btnAcao.addActionListener(e -> btnAcao(e));

		GroupLayout contentPaneLayout = new GroupLayout(contentPane);
		contentPane.setLayout(contentPaneLayout);
		contentPaneLayout.setHorizontalGroup(
			contentPaneLayout.createParallelGroup()
				.addGroup(contentPaneLayout.createSequentialGroup()
					.addContainerGap()
					.addComponent(lblA)
					.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
					.addComponent(txtArquivos, GroupLayout.PREFERRED_SIZE, 71, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
					.addComponent(txtTamanho, GroupLayout.PREFERRED_SIZE, 76, GroupLayout.PREFERRED_SIZE)
					.addGap(18, 18, 18)
					.addComponent(lblT1)
					.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
					.addComponent(txtTamanhoDuplicatas, GroupLayout.PREFERRED_SIZE, 76, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
					.addComponent(txtDuplicatas, GroupLayout.PREFERRED_SIZE, 76, GroupLayout.PREFERRED_SIZE)
					.addGap(18, 18, 18)
					.addComponent(lblExtensoes)
					.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
					.addComponent(cmbExtencoes, GroupLayout.PREFERRED_SIZE, 85, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
					.addComponent(btnAcao)
					.addGap(18, 18, 18)
					.addComponent(lblDuracao)
					.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
					.addComponent(txtDuracao, GroupLayout.PREFERRED_SIZE, 63, GroupLayout.PREFERRED_SIZE)
					.addContainerGap())
				.addComponent(resultPane, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
		);
		contentPaneLayout.setVerticalGroup(
			contentPaneLayout.createParallelGroup()
				.addGroup(GroupLayout.Alignment.TRAILING, contentPaneLayout.createSequentialGroup()
					.addContainerGap()
					.addGroup(contentPaneLayout.createParallelGroup()
						.addGroup(contentPaneLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
							.addComponent(txtDuracao, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
							.addComponent(lblDuracao)
							.addComponent(btnAcao))
						.addGroup(contentPaneLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
							.addComponent(lblA)
							.addComponent(txtArquivos, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
							.addComponent(txtTamanho, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
							.addComponent(lblT1)
							.addComponent(txtTamanhoDuplicatas, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
							.addComponent(txtDuplicatas, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
							.addComponent(cmbExtencoes, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
							.addComponent(lblExtensoes)))
					.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
					.addComponent(resultPane, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
		);
		pack();
		setLocationRelativeTo(getOwner());
    }// </editor-fold>//GEN-END:initComponents
	// <editor-fold defaultstate="collapsed" desc="Eventos">
    private void cmbExtencoes(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbExtencoes
		if (cmbExtencoes.getSelectedIndex() == 0) {
			cardResultado.setExtensao(null);
		} else {
			cardResultado.setExtensao((String) cmbExtencoes.getSelectedItem());
		}
    }//GEN-LAST:event_cmbExtencoes

    private void btnAcao(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAcao
		if (running) {
			parar();
		} else {
			if (Resultados.inserir(resulted)) {
				Dialogos.info("Resulted inserido em relatórios.");
				btnAcao.setEnabled(false);
			} else {
				Dialogos.erro("Erro na inserção.");
			}
		}
    }//GEN-LAST:event_btnAcao
// </editor-fold>
	// <editor-fold defaultstate="collapsed" desc="Variáveis">
    // Variables declaration - do not modify//GEN-BEGIN:variables
	// Generated using JFormDesigner Evaluation license - Victor Cleber Laureano
	private JTextField txtArquivos;
	private JTextField txtTamanho;
	private JTextField txtDuracao;
	private JTextField txtTamanhoDuplicatas;
	private JTextField txtDuplicatas;
	private JPanel resultPane;
	private JPanel jPanel1;
	private JLabel lblProgresso;
	private JProgressBar barProgresso;
	private JLabel lblExtensoes;
	private JComboBox<String> cmbExtencoes;
	private JButton btnAcao;
    // End of variables declaration//GEN-END:variables
	// </editor-fold>
}
