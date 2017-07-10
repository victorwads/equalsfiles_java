/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.victorwads.equalsfiles.swing.components;

// <editor-fold defaultstate="collapsed" desc="Imports">
import br.com.victorwads.equalsfiles.swing.components.icones.GetIcone;
import static br.com.victorwads.equalsfiles.swing.components.icones.GetIcone.setResImage;
import br.com.victorwads.equalsfiles.swing.model.MyTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import javax.swing.AbstractAction;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JTable;
import static javax.swing.KeyStroke.getKeyStroke;
import javax.swing.event.TableModelEvent;
// </editor-fold>

/**
 *
 * @author victo
 */
public class tablePagerView extends javax.swing.JPanel {

	private MyTableModel tableModel;

	/**
	 * Creates new form tablePagerView
	 *
	 * @param tableModel
	 */
	public tablePagerView(MyTableModel tableModel) {
		setModel(tableModel);
		initComponents();
		setResImage(brnPrimeira, GetIcone.PRIMEIRA);
		setResImage(btnAnterior, GetIcone.ANTERIOR);
		setResImage(btnProxima, GetIcone.PROXIMA);
		setResImage(btnUltima, GetIcone.ULTIMA);

	}

	public tablePagerView() {
		this(null);
	}

	public void attView() {
		txtPagina.setText(Integer.toString(tableModel.getPagina()));
		txtTotal.setText(Integer.toString(tableModel.getTotal()));
		txtTotalPaginas.setText(Integer.toString(tableModel.getTotalPaginas()));
	}

	public void setKeyEvents(JTable jtable) {
		InputMap inMap = jtable.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
		inMap.put(getKeyStroke(KeyEvent.VK_RIGHT, 0), "PROXIMA_PAGINA");
		inMap.put(getKeyStroke(KeyEvent.VK_LEFT, 0), "ANTERIOR_PAGINA");
		jtable.getActionMap().put("PROXIMA_PAGINA", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				tableModel.proximaPagina();
			}
		});
		jtable.getActionMap().put("ANTERIOR_PAGINA", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				tableModel.anteriorPagina();
			}
		});
	}

	public void setModel(MyTableModel tableModel) {
		this.tableModel = tableModel;
		if (tableModel != null) {
			tableModel.addTableModelListener((TableModelEvent tme) -> {
				attView();
			});
		}
	}

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        brnPrimeira = new javax.swing.JButton();
        btnAnterior = new javax.swing.JButton();
        javax.swing.JLabel lblPagina = new javax.swing.JLabel();
        txtPagina = new javax.swing.JFormattedTextField();
        javax.swing.JLabel lblDe = new javax.swing.JLabel();
        txtTotalPaginas = new javax.swing.JFormattedTextField();
        javax.swing.JLabel lblTotal = new javax.swing.JLabel();
        txtTotal = new javax.swing.JFormattedTextField();
        btnProxima = new javax.swing.JButton();
        btnUltima = new javax.swing.JButton();

        setOpaque(false);
        java.awt.FlowLayout flowLayout1 = new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 5, 0);
        flowLayout1.setAlignOnBaseline(true);
        setLayout(flowLayout1);

        brnPrimeira.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                primeira(evt);
            }
        });
        add(brnPrimeira);

        btnAnterior.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                anterior(evt);
            }
        });
        add(btnAnterior);

        lblPagina.setText("Pagina");
        lblPagina.setFocusable(false);
        add(lblPagina);

        txtPagina.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0"))));
        txtPagina.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtPagina.setText("1");
        txtPagina.setMinimumSize(new java.awt.Dimension(0, 0));
        txtPagina.setPreferredSize(new java.awt.Dimension(35, 20));
        txtPagina.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                changePagina(evt);
            }
        });
        add(txtPagina);

        lblDe.setText("de");
        add(lblDe);

        txtTotalPaginas.setEditable(false);
        txtTotalPaginas.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0"))));
        txtTotalPaginas.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtTotalPaginas.setText("0");
        txtTotalPaginas.setEnabled(false);
        txtTotalPaginas.setFocusable(false);
        txtTotalPaginas.setMinimumSize(new java.awt.Dimension(0, 0));
        txtTotalPaginas.setPreferredSize(new java.awt.Dimension(35, 20));
        add(txtTotalPaginas);

        lblTotal.setText("Total");
        lblTotal.setFocusable(false);
        add(lblTotal);

        txtTotal.setEditable(false);
        txtTotal.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0"))));
        txtTotal.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtTotal.setText("0");
        txtTotal.setEnabled(false);
        txtTotal.setFocusable(false);
        txtTotal.setMinimumSize(new java.awt.Dimension(0, 0));
        txtTotal.setPreferredSize(new java.awt.Dimension(50, 20));
        add(txtTotal);

        btnProxima.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                proxima(evt);
            }
        });
        add(btnProxima);

        btnUltima.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ultima(evt);
            }
        });
        add(btnUltima);
    }// </editor-fold>//GEN-END:initComponents
	// <editor-fold defaultstate="collapsed" desc="Eventos">
    private void anterior(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_anterior
		tableModel.anteriorPagina();
    }//GEN-LAST:event_anterior

    private void primeira(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_primeira
		tableModel.setPagina(1);
    }//GEN-LAST:event_primeira

    private void proxima(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_proxima
		tableModel.proximaPagina();
    }//GEN-LAST:event_proxima

    private void ultima(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ultima
		tableModel.setPagina(Integer.MAX_VALUE);
    }//GEN-LAST:event_ultima

    private void changePagina(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_changePagina
		tableModel.setPagina(Integer.parseInt(txtPagina.getText()));
    }//GEN-LAST:event_changePagina
	// </editor-fold>
	// <editor-fold defaultstate="collapsed" desc="Variáveis">
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton brnPrimeira;
    private javax.swing.JButton btnAnterior;
    private javax.swing.JButton btnProxima;
    private javax.swing.JButton btnUltima;
    private javax.swing.JFormattedTextField txtPagina;
    private javax.swing.JFormattedTextField txtTotal;
    private javax.swing.JFormattedTextField txtTotalPaginas;
    // End of variables declaration//GEN-END:variables
	// </editor-fold>
}
