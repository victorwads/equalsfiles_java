/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.victorwads.equalsfiles.swing;

// <editor-fold defaultstate="collapsed" desc="Imports">
import br.com.victorwads.equalsfiles.controller.Indexacao;
import br.com.victorwads.equalsfiles.controller.ThreadListListenner;
import br.com.victorwads.equalsfiles.model.ArquivoHistorico;
import br.com.victorwads.equalsfiles.model.ArquivoHistorico.Tipo;
import br.com.victorwads.equalsfiles.swing.components.Card;
import br.com.victorwads.equalsfiles.swing.components.icones.GetIcone;
import br.com.victorwads.equalsfiles.swing.model.HistoricoTableModel;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.AbstractAction;
import javax.swing.DefaultComboBoxModel;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.KeyStroke;
import javax.swing.ListCellRenderer;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableCellRenderer;
// </editor-fold>

/**
 *
 * @author victo
 */
public class CardHistorico extends Card implements ChangeListener, ThreadListListenner {

	// <editor-fold defaultstate="collapsed" desc="Classes">
	private class IconCellTableRenderer extends DefaultTableCellRenderer {

		@Override
		protected void setValue(Object o) {
			if (o instanceof ArquivoHistorico) {
				setIcon(GetIcone.getArquivoIcone((ArquivoHistorico) o));
				String patch = o.toString();
				if (pesquisa != null) {
					setText("<html>" + patch.replaceAll(pesquisa, "<b>" + pesquisa + "</b>") + "</html>");
				}
			} else if (o instanceof Tipo) {
				if (Tipo.NOVO == o) {
					GetIcone.setResImage(this, GetIcone.ADICONAR);
				} else if (Tipo.EXCLUIDO == o) {
					GetIcone.setResImage(this, GetIcone.LIMPAR);
				} else if (Tipo.ALTERADO == o) {
					GetIcone.setResImage(this, GetIcone.RELOAD);
				}
			}
			super.setValue(o);
		}
	}

	private class TipoListCellRenderer extends JLabel implements ListCellRenderer<Object> {

		@Override
		public Component getListCellRendererComponent(JList<?> jlist, Object o, int i, boolean bln, boolean bln1) {
			if (o instanceof Tipo) {
				if (Tipo.NOVO == o) {
					GetIcone.setResImage(this, GetIcone.ADICONAR);
					setText("Adicionados");
				} else if (Tipo.EXCLUIDO == o) {
					GetIcone.setResImage(this, GetIcone.LIMPAR);
					setText("Excluidos");
				} else if (Tipo.ALTERADO == o) {
					GetIcone.setResImage(this, GetIcone.RELOAD);
					setText("Alterados");
				}
			} else {
				setIcon(null);
				setText(o.toString());
			}
			return this;
		}
	}
	// </editor-fold>

	private final HistoricoTableModel tableModel = new HistoricoTableModel();
	private boolean init = false;
	private Thread loadingThread;
	private Timer timer = null;
	private Tipo tipo = null;
	private ArquivoHistorico[] data;
	private String pesquisa = "";

	/**
	 * Creates new form cardHistorico
	 */
	public CardHistorico() {
	}

	// <editor-fold defaultstate="collapsed" desc="Methodos">
	private void init() {
		if (init) {
			return;
		}
		init = true;
		initComponents();
		txtData1.setDateTime(true);
		txtData2.setDateTime(true);

		//Table Config
		((DefaultTableCellRenderer) table.getDefaultRenderer(String.class)).setHorizontalAlignment(JLabel.CENTER);
		((DefaultTableCellRenderer) table.getTableHeader().getDefaultRenderer()).setHorizontalAlignment(JLabel.CENTER);
		IconCellTableRenderer renderer = new IconCellTableRenderer();
		renderer.setHorizontalAlignment(JLabel.CENTER);
		table.setDefaultRenderer(Tipo.class, renderer);
		table.setDefaultRenderer(ArquivoHistorico.class, new IconCellTableRenderer());
		table.setRowHeight(22);
		tableModel.setTableModel(table);
		tablePagerView.setModel(tableModel);
		tablePagerView.setKeyEvents(table);

		//Data Fields
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		c.add(Calendar.DAY_OF_MONTH, -1);
		txtData1.setDate(c.getTime());
		txtData1.addChangeListenner(this);

		//Combos Tipos
		DefaultComboBoxModel tiposModel = new DefaultComboBoxModel();
		tiposModel.addElement("Todos");
		tiposModel.addElement(Tipo.NOVO);
		tiposModel.addElement(Tipo.ALTERADO);
		tiposModel.addElement(Tipo.EXCLUIDO);
		cmbTipos.setRenderer(new TipoListCellRenderer());
		cmbTipos.setModel(tiposModel);

		Indexacao.addListenner(this);

		// <editor-fold defaultstate="collapsed" desc="Key Events">
		InputMap inMap = getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
		inMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_F5, 0), "RELOAD");
		getActionMap().put("RELOAD", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				loadData();
			}
		});
		// </editor-fold>
	}

	private void clean() {
		if (timer != null) {
			timer.cancel();
		}
		timer = null;
		if (loadingThread != null && loadingThread.isAlive()) {
			loadingThread.stop();
		}
		loadingThread = null;
		data = null;
		tableModel.clear();
		System.gc();
	}

	private void loadData() {
		clean();
		timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				loading.setVisible(true);
				table.setEnabled(false);
			}
		}, 500);
		loadingThread = new Thread(() -> {
			data = Indexacao.listarHistorico(txtData1.getDate(), txtData2.getDate(), tipo, txtPesquisa.getText());
			if (timer != null) {
				timer.cancel();
			}
			timer = null;
			table.setEnabled(true);
			loading.setVisible(false);
			listar();
		});
		loadingThread.start();
	}

	private void listar() {
		tableModel.addAll(data);
		table.setModel(tableModel);
	}
	// </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        javax.swing.JLabel lbl1 = new javax.swing.JLabel();
        javax.swing.JLabel lbl2 = new javax.swing.JLabel();
        txtData1 = new br.com.victorwads.equalsfiles.swing.components.DateTextField();
        txtData2 = new br.com.victorwads.equalsfiles.swing.components.DateTextField();
        javax.swing.JScrollPane tableScroll = new javax.swing.JScrollPane();
        table = new javax.swing.JTable();
        javax.swing.JLabel lblTipos = new javax.swing.JLabel();
        cmbTipos = new javax.swing.JComboBox<>();
        javax.swing.JLabel lblPesquisa = new javax.swing.JLabel();
        txtPesquisa = new javax.swing.JTextField();
        tablePagerView = new br.com.victorwads.equalsfiles.swing.components.tablePagerView();
        loading = new javax.swing.JProgressBar();

        lbl1.setText("De:");
        lbl1.setFocusable(false);

        lbl2.setText("Até:");
        lbl2.setFocusable(false);

        txtData1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                changeData(evt);
            }
        });

        txtData2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                changeData(evt);
            }
        });

        tableScroll.setViewportView(table);

        lblTipos.setText("Tipo");
        lblTipos.setFocusable(false);

        cmbTipos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbTipos(evt);
            }
        });

        lblPesquisa.setText("Pesquisa");
        lblPesquisa.setFocusable(false);

        txtPesquisa.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtPesquisa(evt);
            }
        });

        loading.setIndeterminate(true);
        loading.setMinimumSize(new java.awt.Dimension(146, 17));
        loading.setString("Carregando");
        loading.setStringPainted(true);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(tableScroll, javax.swing.GroupLayout.DEFAULT_SIZE, 711, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(lbl1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtData1, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(lbl2)
                        .addGap(10, 10, 10)
                        .addComponent(txtData2, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(lblTipos)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cmbTipos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(lblPesquisa)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtPesquisa))
                    .addComponent(tablePagerView, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(loading, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(cmbTipos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lblTipos))
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(lbl1)
                        .addComponent(lbl2)
                        .addComponent(txtData1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txtData2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtPesquisa, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lblPesquisa)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(loading, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tableScroll, javax.swing.GroupLayout.DEFAULT_SIZE, 250, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(tablePagerView, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents
	// <editor-fold defaultstate="collapsed" desc="Eventos">
    private void changeData(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_changeData
		loadData();
    }//GEN-LAST:event_changeData

    private void cmbTipos(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbTipos
		if (cmbTipos.getSelectedIndex() == 0) {
			tipo = null;
		} else {
			tipo = (Tipo) cmbTipos.getSelectedItem();
		}
		loadData();
    }//GEN-LAST:event_cmbTipos

    private void txtPesquisa(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPesquisa
		String s = txtPesquisa.getText();
		if ((pesquisa != null && !pesquisa.equals(s)) || (pesquisa == null && s.equals(""))) {
			if (s.equals("")) {
				pesquisa = null;
			} else {
				pesquisa = txtPesquisa.getText();
			}
		}
		loadData();
    }//GEN-LAST:event_txtPesquisa

	@Override
	public void changeState(int threadRunning, int threadQueueTotal, int threadTotal) {
		if (threadQueueTotal == 0 && isVisible() && tableModel.getPagina() == 1) {
			loadData();
		}
	}

	@Override
	public void stateChanged(ChangeEvent ce) {
		loadData();
	}

	@Override
	public void cardShow(ComponentEvent evt) {
		init();
		loadData();
	}

	@Override
	public void cardHide(ComponentEvent evt) {
		clean();
	}
	// </editor-fold>
	// <editor-fold defaultstate="collapsed" desc="Variáveis">
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> cmbTipos;
    private javax.swing.JProgressBar loading;
    private javax.swing.JTable table;
    private br.com.victorwads.equalsfiles.swing.components.tablePagerView tablePagerView;
    private br.com.victorwads.equalsfiles.swing.components.DateTextField txtData1;
    private br.com.victorwads.equalsfiles.swing.components.DateTextField txtData2;
    private javax.swing.JTextField txtPesquisa;
    // End of variables declaration//GEN-END:variables
	// </editor-fold>
}
