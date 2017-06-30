/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.victorwads.equalsfiles.swing;

// <editor-fold defaultstate="collapsed" desc="Imports">
import br.com.victorwads.equalsfiles.controller.Resultados;
import br.com.victorwads.equalsfiles.swing.components.Card;
import br.com.victorwads.equalsfiles.swing.components.icones.GetIcone;
import static br.com.victorwads.equalsfiles.swing.components.icones.GetIcone.setResImage;
import br.com.victorwads.equalsfiles.swing.model.RelatoriosTableModel;
import br.com.victorwads.equalsfiles.swing.util.Dialogos;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.util.Calendar;
import java.util.Date;
import javax.swing.AbstractAction;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.KeyStroke;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.table.DefaultTableCellRenderer;

/**
 *
 * @author victo
 */
public class CardRelatorios extends Card implements ChangeListener {

	private DefaultTableCellRenderer centerRenderer;
	private DefaultTableCellRenderer centerHeaderRenderer;
	private RelatoriosTableModel tableModel;
	private boolean init = false;

	/**
	 * Creates new form cardResultado
	 */
	public CardRelatorios() {
	}

	// <editor-fold defaultstate="collapsed" desc="Methodos">
	public void init() {
		if (init) {
			return;
		}
		init = true;
		initComponents();
		setResImage(btnRemover, GetIcone.LIMPAR);

		//Table Config
		centerRenderer = (DefaultTableCellRenderer) table.getDefaultRenderer(String.class);
		centerHeaderRenderer = (DefaultTableCellRenderer) table.getTableHeader().getDefaultRenderer();
		centerRenderer.setHorizontalAlignment(JLabel.CENTER);
		centerHeaderRenderer.setHorizontalAlignment(JLabel.CENTER);
		table.getSelectionModel().addListSelectionListener((ListSelectionEvent e) -> {
			if (table.getSelectedRow() == -1) {
				btnRemover.setEnabled(false);
			} else {
				btnRemover.setEnabled(true);
			}
		});
		tableModel = new RelatoriosTableModel();
		tableModel.setTableModel(table);

		//Data Fields
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		txtData2.setDate(c.getTime());
		txtData2.addChangeListenner(this);
		c.add(Calendar.MONTH, -1);
		txtData1.setDate(c.getTime());
		txtData1.addChangeListenner(this);

		// <editor-fold defaultstate="collapsed" desc="Key Events">
		InputMap inMap = getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
		inMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0), "REMOVER");
		inMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_F5, 0), "RELOAD");
		getActionMap().put("REMOVER", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				remover();
			}
		});
		getActionMap().put("RELOAD", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				listar();
			}
		});
		// </editor-fold>
	}

	public void clean() {
		if (init) {
			tableModel.clear();
		}
		System.gc();
	}

	public void listar() {
		tableModel.addAll(Resultados.listar(txtData1.getDate(), txtData2.getDate()));
	}

	public void remover() {
		int selectedRow = table.getSelectedRow();
		if (selectedRow != -1 && Dialogos.pergunta("Deseja mesmo excluir o resultado selecionado?")) {
			Resultados.excluir(tableModel.removeRow(selectedRow));
		}
	}
	// </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        javax.swing.JLabel lbl1 = new javax.swing.JLabel();
        lbl2 = new javax.swing.JLabel();
        txtData1 = new br.com.victorwads.equalsfiles.swing.components.DateTextField();
        txtData2 = new br.com.victorwads.equalsfiles.swing.components.DateTextField();
        btnRemover = new javax.swing.JButton();
        javax.swing.JScrollPane tableScroll = new javax.swing.JScrollPane();
        table = new javax.swing.JTable();

        lbl1.setText("De:");

        lbl2.setText("Até:");

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

        btnRemover.setText("Remover");
        btnRemover.setEnabled(false);
        btnRemover.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRemover(evt);
            }
        });

        table.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tableClicked(evt);
            }
        });
        tableScroll.setViewportView(table);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(lbl1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtData1, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(lbl2)
                        .addGap(10, 10, 10)
                        .addComponent(txtData2, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnRemover))
                    .addComponent(tableScroll, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbl1)
                    .addComponent(lbl2)
                    .addComponent(txtData1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtData2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnRemover))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tableScroll, javax.swing.GroupLayout.DEFAULT_SIZE, 171, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents
	// <editor-fold defaultstate="collapsed" desc="Eventos">
    private void changeData(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_changeData
		listar();
    }//GEN-LAST:event_changeData

    private void btnRemover(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRemover
		remover();
    }//GEN-LAST:event_btnRemover

    private void tableClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableClicked
		int selectedRow = table.getSelectedRow();
		if (selectedRow != -1 && evt.getClickCount() == 2 && evt.getButton() == 1) {
			Resultados.carregarArquivos(tableModel.getRow(selectedRow));
			new WindowProcesso(tableModel.getRow(selectedRow));
		}
    }//GEN-LAST:event_tableClicked

	@Override
	public void cardShow(ComponentEvent evt) {
		init();
		listar();
		table.requestFocus();
	}

	@Override
	public void cardHide(ComponentEvent evt) {
		clean();
	}

	@Override
	public void stateChanged(ChangeEvent ce) {
		listar();
	}
	// </editor-fold>
	// <editor-fold defaultstate="collapsed" desc="Variáveis">
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnRemover;
    private javax.swing.JLabel lbl2;
    private javax.swing.JTable table;
    private br.com.victorwads.equalsfiles.swing.components.DateTextField txtData1;
    private br.com.victorwads.equalsfiles.swing.components.DateTextField txtData2;
    // End of variables declaration//GEN-END:variables
	// </editor-fold>
}
