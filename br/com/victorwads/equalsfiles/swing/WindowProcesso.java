/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.victorwads.equalsfiles.swing;

// <editor-fold defaultstate="collapsed" desc="Imports">
import static br.com.victorwads.equalsfiles.controller.CacheMD5.humamSize;
import br.com.victorwads.equalsfiles.controller.Processa;
import br.com.victorwads.equalsfiles.controller.ProcessaInterface;
import br.com.victorwads.equalsfiles.controller.Resultados;
import br.com.victorwads.equalsfiles.model.Diretorio;
import br.com.victorwads.equalsfiles.model.Resultado;
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
	private final Resultado resultado;
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
	 * @param resultado
	 */
	public WindowProcesso(Resultado resultado) {
		setTitle(resultado.getDiretorios());
		setTitle(TITLE_RUNNING + title);
		initComponents();
		setVisible(true);

		this.resultado = resultado;
		if (resultado.getId() > 0) {
			processa = null;
			processaThread = null;
			setFilesAmount(resultado.getQuantidadeArquivos());
			setFilesSize(resultado.getTamanhoArquivos());
			setDuration(resultado.getDuracao());
			finish();
		} else {
			processa = new Processa(this, resultado);
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
		cardResultado = new CardResultado(resultado, this);
		resultPane.add(cardResultado, CARD_RESULT.NAME);
		((CardLayout) resultPane.getLayout()).show(resultPane, CARD_RESULT.NAME);
		if (resultado.getId() == 0) {
			btnAcao.setVisible(true);
		}
		pack();
	}
	// </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        javax.swing.JLabel lblA = new javax.swing.JLabel();
        txtArquivos = new javax.swing.JTextField();
        txtTamanho = new javax.swing.JTextField();
        txtDuracao = new javax.swing.JTextField();
        javax.swing.JLabel lblDuracao = new javax.swing.JLabel();
        javax.swing.JLabel lblT1 = new javax.swing.JLabel();
        txtTamanhoDuplicatas = new javax.swing.JTextField();
        txtDuplicatas = new javax.swing.JTextField();
        resultPane = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        lblProgresso = new javax.swing.JLabel();
        barProgresso = new javax.swing.JProgressBar();
        lblExtensoes = new javax.swing.JLabel();
        cmbExtencoes = new javax.swing.JComboBox<>();
        btnAcao = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        lblA.setText("Arquivos");

        txtArquivos.setEditable(false);
        txtArquivos.setForeground(new java.awt.Color(255, 0, 0));
        txtArquivos.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtArquivos.setText("-");

        txtTamanho.setEditable(false);
        txtTamanho.setForeground(new java.awt.Color(0, 204, 51));
        txtTamanho.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtTamanho.setText("-");

        txtDuracao.setEditable(false);
        txtDuracao.setForeground(new java.awt.Color(0, 153, 255));
        txtDuracao.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtDuracao.setText("00:00:00");

        lblDuracao.setText("Duração");

        lblT1.setText("Duplicatas");

        txtTamanhoDuplicatas.setEditable(false);
        txtTamanhoDuplicatas.setForeground(new java.awt.Color(0, 204, 51));
        txtTamanhoDuplicatas.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtTamanhoDuplicatas.setText("-");

        txtDuplicatas.setEditable(false);
        txtDuplicatas.setForeground(new java.awt.Color(0, 204, 51));
        txtDuplicatas.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtDuplicatas.setText("-");

        resultPane.setLayout(new java.awt.CardLayout());

        lblProgresso.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        lblProgresso.setForeground(new java.awt.Color(102, 0, 102));
        lblProgresso.setText("Progresso");
        lblProgresso.setFocusable(false);
        lblProgresso.setIconTextGap(0);

        barProgresso.setToolTipText("");
        barProgresso.setFocusable(false);
        barProgresso.setString("");
        barProgresso.setStringPainted(true);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 850, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(barProgresso, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(lblProgresso, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addContainerGap()))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(lblProgresso)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(barProgresso, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );

        resultPane.add(jPanel1, "card3");

        lblExtensoes.setText("Extenções");

        cmbExtencoes.setEnabled(false);
        cmbExtencoes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbExtencoes(evt);
            }
        });

        btnAcao.setText("Parar");
        btnAcao.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAcao(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblA)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtArquivos, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtTamanho, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(lblT1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtTamanhoDuplicatas, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtDuplicatas, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(lblExtensoes)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cmbExtencoes, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnAcao)
                .addGap(18, 18, 18)
                .addComponent(lblDuracao)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtDuracao, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
            .addComponent(resultPane, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtDuracao, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lblDuracao)
                        .addComponent(btnAcao))
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(lblA)
                        .addComponent(txtArquivos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txtTamanho, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lblT1)
                        .addComponent(txtTamanhoDuplicatas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txtDuplicatas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(cmbExtencoes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lblExtensoes)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(resultPane, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
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
			if (Resultados.inserir(resultado)) {
				Dialogos.info("Resultado inserido em relatórios.");
				btnAcao.setEnabled(false);
			} else {
				Dialogos.erro("Erro na inserção.");
			}
		}
    }//GEN-LAST:event_btnAcao
// </editor-fold>
	// <editor-fold defaultstate="collapsed" desc="Variáveis">
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JProgressBar barProgresso;
    private javax.swing.JButton btnAcao;
    private javax.swing.JComboBox<String> cmbExtencoes;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JLabel lblExtensoes;
    private javax.swing.JLabel lblProgresso;
    private javax.swing.JPanel resultPane;
    private javax.swing.JTextField txtArquivos;
    private javax.swing.JTextField txtDuplicatas;
    private javax.swing.JTextField txtDuracao;
    private javax.swing.JTextField txtTamanho;
    private javax.swing.JTextField txtTamanhoDuplicatas;
    // End of variables declaration//GEN-END:variables
	// </editor-fold>
}
