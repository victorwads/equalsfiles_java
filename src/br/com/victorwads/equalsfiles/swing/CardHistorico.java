/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.victorwads.equalsfiles.swing;

// <editor-fold defaultstate="collapsed" desc="Imports">
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.GroupLayout;
import javax.swing.LayoutStyle;
import br.com.victorwads.equalsfiles.controller.Indexacao;
import br.com.victorwads.equalsfiles.controller.ThreadListListenner;
import br.com.victorwads.equalsfiles.model.ArquivoHistorico;
import br.com.victorwads.equalsfiles.model.ArquivoHistorico.Tipo;
import br.com.victorwads.equalsfiles.swing.components.*;
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
	private ArquivoHistorico[] registros;
	private String pesquisa = "";
	private long lastUpdate = 0;

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

	private void cleanAll() {
		if (loadingThread != null && loadingThread.isAlive()) {
			loadingThread.stop();
		}
		cleanData();
	}

	private void cleanData() {
		if (timer != null) {
			timer.cancel();
			timer = null;
		}
		loadingThread = null;
		registros = null;
		tableModel.clear();
		System.gc();
	}

	private void loadData() {
		timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				loading.setVisible(true);
				table.setEnabled(false);
			}
		}, 500);
		loadingThread = new Thread(() -> {
			cleanData();
			registros = Indexacao.listarHistorico(txtData1.getDate(), txtData2.getDate(), tipo, txtPesquisa.getText());
			table.setEnabled(true);
			loading.setVisible(false);
			listar();
		});
		loadingThread.start();
	}

	private void listar() {
		tableModel.addAll(registros);
		table.setModel(tableModel);
	}
	// </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
	// Generated using JFormDesigner Evaluation license - Victor Cleber Laureano
	private void initComponents() {
		JLabel lbl1 = new JLabel();
		JLabel lbl2 = new JLabel();
		txtData1 = new DateTextField();
		txtData2 = new DateTextField();
		JScrollPane tableScroll = new JScrollPane();
		table = new JTable();
		JLabel lblTipos = new JLabel();
		cmbTipos = new JComboBox<>();
		JLabel lblPesquisa = new JLabel();
		txtPesquisa = new JTextField();
		tablePagerView = new tablePagerView();
		loading = new JProgressBar();

		//======== this ========



		//---- lbl1 ----
		lbl1.setText("De:");
		lbl1.setFocusable(false);

		//---- lbl2 ----
		lbl2.setText("At\u00e9:");
		lbl2.setFocusable(false);

		//---- txtData1 ----
		txtData1.addActionListener(e -> changeData(e));

		//---- txtData2 ----
		txtData2.addActionListener(e -> changeData(e));

		//======== tableScroll ========
		{
			tableScroll.setViewportView(table);
		}

		//---- lblTipos ----
		lblTipos.setText("Tipo");
		lblTipos.setFocusable(false);

		//---- cmbTipos ----
		cmbTipos.addActionListener(e -> cmbTipos(e));

		//---- lblPesquisa ----
		lblPesquisa.setText("Pesquisa");
		lblPesquisa.setFocusable(false);

		//---- txtPesquisa ----
		txtPesquisa.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				txtPesquisa(e);
			}
		});

		//---- loading ----
		loading.setIndeterminate(true);
		loading.setMinimumSize(new Dimension(146, 17));
		loading.setString("Carregando");
		loading.setStringPainted(true);

		GroupLayout layout = new GroupLayout(this);
		setLayout(layout);
		layout.setHorizontalGroup(
			layout.createParallelGroup()
				.addGroup(layout.createSequentialGroup()
					.addContainerGap()
					.addGroup(layout.createParallelGroup()
						.addComponent(tableScroll, GroupLayout.DEFAULT_SIZE, 711, Short.MAX_VALUE)
						.addGroup(layout.createSequentialGroup()
							.addComponent(lbl1)
							.addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
							.addComponent(txtData1, GroupLayout.PREFERRED_SIZE, 150, GroupLayout.PREFERRED_SIZE)
							.addGap(18, 18, 18)
							.addComponent(lbl2)
							.addGap(10, 10, 10)
							.addComponent(txtData2, GroupLayout.PREFERRED_SIZE, 150, GroupLayout.PREFERRED_SIZE)
							.addGap(18, 18, 18)
							.addComponent(lblTipos)
							.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
							.addComponent(cmbTipos, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
							.addGap(18, 18, 18)
							.addComponent(lblPesquisa)
							.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
							.addComponent(txtPesquisa))
						.addComponent(tablePagerView, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addComponent(loading, GroupLayout.Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
					.addContainerGap())
		);
		layout.setVerticalGroup(
			layout.createParallelGroup()
				.addGroup(layout.createSequentialGroup()
					.addContainerGap()
					.addGroup(layout.createParallelGroup()
						.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
							.addComponent(cmbTipos, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
							.addComponent(lblTipos))
						.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
							.addComponent(lbl1)
							.addComponent(lbl2)
							.addComponent(txtData1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
							.addComponent(txtData2, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
						.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
							.addComponent(txtPesquisa, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
							.addComponent(lblPesquisa)))
					.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
					.addComponent(loading, GroupLayout.PREFERRED_SIZE, 17, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
					.addComponent(tableScroll, GroupLayout.DEFAULT_SIZE, 250, Short.MAX_VALUE)
					.addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
					.addComponent(tablePagerView, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
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
		long now = new Date().getTime();
		if (threadQueueTotal == 0 && isVisible() && tableModel.getPage() == 1 && now > (lastUpdate + 2000)) {
			loadData();
			lastUpdate = now;
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
		cleanAll();
	}
	// </editor-fold>
	// <editor-fold defaultstate="collapsed" desc="Variáveis">
    // Variables declaration - do not modify//GEN-BEGIN:variables
	// Generated using JFormDesigner Evaluation license - Victor Cleber Laureano
	private DateTextField txtData1;
	private DateTextField txtData2;
	private JTable table;
	private JComboBox<String> cmbTipos;
	private JTextField txtPesquisa;
	private tablePagerView tablePagerView;
	private JProgressBar loading;
    // End of variables declaration//GEN-END:variables
	// </editor-fold>
}
