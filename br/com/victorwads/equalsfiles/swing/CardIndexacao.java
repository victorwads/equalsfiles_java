/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.victorwads.equalsfiles.swing;

// <editor-fold defaultstate="collapsed" desc="Imports">
import static br.com.victorwads.equalsfiles.Main.objectIndexacao;
import static br.com.victorwads.equalsfiles.Main.showTrayIcon;
import static br.com.victorwads.equalsfiles.Main.suportTray;
import static br.com.victorwads.equalsfiles.Main.threadIndexacao;
import br.com.victorwads.equalsfiles.controller.Indexacao;
import br.com.victorwads.equalsfiles.controller.IndexacaoInterface;
import br.com.victorwads.equalsfiles.dao.Index;
import br.com.victorwads.equalsfiles.model.Diretorio;
import br.com.victorwads.equalsfiles.swing.components.Card;
import br.com.victorwads.equalsfiles.swing.components.IconCheckBox;
import br.com.victorwads.equalsfiles.swing.components.WrapLayout;
import br.com.victorwads.equalsfiles.swing.components.icones.GetIcone;
import static br.com.victorwads.equalsfiles.swing.components.icones.GetIcone.setResImage;
import br.com.victorwads.equalsfiles.swing.util.DateTime;
import br.com.victorwads.equalsfiles.swing.util.Dialogos;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.text.DecimalFormat;
import javax.swing.AbstractAction;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.KeyStroke;
import javax.swing.WindowConstants;
// </editor-fold>

/**
 *
 * @author victo
 */
public class CardIndexacao extends Card implements IndexacaoInterface {

	private float total;
	private boolean init = false;
	private IconCheckBox[] checks;
	private JFrame parentWindow = null;
	private int parentWindowDefaultCloseOperation;
	private final WindowAdapter closingEvent = new WindowAdapter() {
		@Override
		public void windowClosing(WindowEvent we) {
			if (suportTray) {
				showTrayIcon();
			} else if (Dialogos.pergunta("Existe uma indexação em andamento, deseja finaliza-la?")) {
				objectIndexacao.stop();
				((JFrame) we.getSource()).dispose();
			} else {
				new Thread(() -> {
					((JFrame) we.getSource()).setVisible(true);
				}).start();
			}
		}
	};

	/**
	 * Creates new form CardIndexacao
	 */
	public CardIndexacao() {
		initComponents();
	}

	// <editor-fold defaultstate="collapsed" desc="Methodos">
	private void loadRoots() {
		File[] roots = File.listRoots();
		checks = new IconCheckBox[roots.length];
		panelRoots.removeAll();
		for (int i = 0; i < roots.length; i++) {
			checks[i] = new IconCheckBox(roots[i].getAbsolutePath());
			checks[i].setIcon(GetIcone.getIconByFile(roots[i]));
			checks[i].setSelected(new Index().getRootId(roots[i].getAbsolutePath(), false) != 0);
			panelRoots.add(checks[i]);
		}
		panelRoots.revalidate();
		panelRoots.repaint();
	}

	private boolean isRunning() {
		return threadIndexacao != null && threadIndexacao.isAlive();
	}

	private void setParent() {
		if (parentWindow == null && isRunning()) {
			parentWindow = getParentJFrame();
			parentWindowDefaultCloseOperation = parentWindow.getDefaultCloseOperation();
			parentWindow.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
			parentWindow.addWindowListener(closingEvent);
		}
	}

	private void initGraphics() {
		if (init) {
			return;
		}
		init = true;
		panelRoots.setLayout(new WrapLayout());
		setResImage(btnReload, GetIcone.RELOAD);
		// <editor-fold defaultstate="collapsed" desc="Eventos">
		InputMap inMap = getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
		inMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_F5, 0), "RELOAD");
		inMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, InputEvent.CTRL_DOWN_MASK), "INICIAR");
		getActionMap().put("INICIAR", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				iniciar();
			}
		});
		getActionMap().put("RELOAD", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				loadRoots();
			}
		});
		// </editor-fold>
		if (isRunning()) {
			init();
		} else {
			setResImage(btnIniciar, GetIcone.PLAY);
			barProgresso.setVisible(false);
		}
	}

	private void init() {
		setResImage(btnIniciar, GetIcone.PARAR);
		btnIniciar.setText("Parar");
		barProgresso.setVisible(true);
		if (objectIndexacao != null) {
			objectIndexacao.setView(this);
		}
	}

	public void iniciar() {
		if (isRunning()) {
			objectIndexacao.stop();
			btnIniciar.setEnabled(false);
			return;
		}
		int count = 0;
		for (IconCheckBox c : checks) {
			if (c.isSelected()) {
				count++;
			}
		}
		if (count == 0) {
			Dialogos.erro("Você deve selecionar ao menos uma unidade para indexar.");
			return;
		}
		init();
		Diretorio[] diretorios = new Diretorio[count];
		int i = 0;
		for (IconCheckBox c : checks) {
			if (c.isSelected()) {
				diretorios[i] = new Diretorio(c.getText());
				i++;
			}
		}
		objectIndexacao = new Indexacao(this, diretorios);
		threadIndexacao = new Thread(objectIndexacao);
		threadIndexacao.start();
		setParent();
	}

	@Override
	public final void setDuration(int segundos) {
		txtDuracao.setText(DateTime.getDurationStringFromSeconds(segundos));
	}

	@Override
	public void clear() {
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
		clear();
		lblProgresso.setText(info);
		barProgresso.setIndeterminate(infinita);
	}

	@Override
	public void finish() {
		clear();
		setResImage(btnIniciar, GetIcone.PLAY);
		btnIniciar.setText("Iniciar");
		btnIniciar.setEnabled(true);
		barProgresso.setVisible(false);
		parentWindow.setDefaultCloseOperation(parentWindowDefaultCloseOperation);
		parentWindow.removeWindowListener(closingEvent);
		if (!parentWindow.isVisible()) {
			parentWindow.dispose();
		}
		parentWindow = null;
	}
	// </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        btnIniciar = new javax.swing.JButton();
        lblProgresso = new javax.swing.JLabel();
        panelRoots = new javax.swing.JPanel();
        btnReload = new javax.swing.JButton();
        javax.swing.JLabel lbl1 = new javax.swing.JLabel();
        txtDuracao = new javax.swing.JTextField();
        javax.swing.JLabel lblDuracao = new javax.swing.JLabel();
        barProgresso = new javax.swing.JProgressBar();

        btnIniciar.setText("Iniciar");
        btnIniciar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnIniciar(evt);
            }
        });

        lblProgresso.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        panelRoots.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        panelRoots.setPreferredSize(new java.awt.Dimension(0, 0));

        javax.swing.GroupLayout panelRootsLayout = new javax.swing.GroupLayout(panelRoots);
        panelRoots.setLayout(panelRootsLayout);
        panelRootsLayout.setHorizontalGroup(
            panelRootsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 548, Short.MAX_VALUE)
        );
        panelRootsLayout.setVerticalGroup(
            panelRootsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 279, Short.MAX_VALUE)
        );

        btnReload.setText("Recarregar");
        btnReload.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnReload(evt);
            }
        });

        lbl1.setText("Unidades:");

        txtDuracao.setEditable(false);
        txtDuracao.setForeground(new java.awt.Color(0, 153, 255));
        txtDuracao.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtDuracao.setText("00:00:00");

        lblDuracao.setText("Duração");

        barProgresso.setStringPainted(true);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(panelRoots, javax.swing.GroupLayout.DEFAULT_SIZE, 552, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(lblDuracao)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtDuracao, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblProgresso, javax.swing.GroupLayout.DEFAULT_SIZE, 376, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnIniciar))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(lbl1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnReload))
                    .addComponent(barProgresso, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnReload)
                    .addComponent(lbl1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panelRoots, javax.swing.GroupLayout.DEFAULT_SIZE, 283, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(barProgresso, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtDuracao, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lblDuracao)
                        .addComponent(lblProgresso, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(btnIniciar))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents
	// <editor-fold defaultstate="collapsed" desc="Eventos">
    private void btnIniciar(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnIniciar
		iniciar();
    }//GEN-LAST:event_btnIniciar

    private void btnReload(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnReload
		loadRoots();
    }//GEN-LAST:event_btnReload

	@Override
	public void cardHide(ComponentEvent evt) {
		setParent();
	}

	@Override
	public void cardShow(ComponentEvent evt) {
		initGraphics();
		loadRoots();
	}

	// </editor-fold>
	// <editor-fold defaultstate="collapsed" desc="Variáveis">
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JProgressBar barProgresso;
    private javax.swing.JButton btnIniciar;
    private javax.swing.JButton btnReload;
    private javax.swing.JLabel lblProgresso;
    private javax.swing.JPanel panelRoots;
    private javax.swing.JTextField txtDuracao;
    // End of variables declaration//GEN-END:variables
	// </editor-fold>
}
