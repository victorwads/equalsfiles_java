/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.victorwads.equalsfiles.swing.components;

import java.awt.*;
import javax.swing.*;
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

/**
 *
 * @author victo
 */
public class tablePagerView extends javax.swing.JPanel {

	private MyTableModel tableModel;

	/**
	 * Creates new form tablePagerView
	 *
	 * @param tableModel MyTableModel interface to control de data on the table
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

	private void attView() {
		txtPagina.setText(Integer.toString(tableModel.getPage()));
		txtTotal.setText(Integer.toString(tableModel.getTotal()));
		txtTotalPaginas.setText(Integer.toString(tableModel.getPagesAmount()));
	}

	public void setKeyEvents(JTable jtable) {
		InputMap inMap = jtable.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
		inMap.put(getKeyStroke(KeyEvent.VK_RIGHT, 0), "PROXIMA_PAGINA");
		inMap.put(getKeyStroke(KeyEvent.VK_LEFT, 0), "ANTERIOR_PAGINA");
		jtable.getActionMap().put("PROXIMA_PAGINA", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				tableModel.nextPage();
			}
		});
		jtable.getActionMap().put("ANTERIOR_PAGINA", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				tableModel.previousPage();
			}
		});
	}

	public void setModel(MyTableModel tableModel) {
		this.tableModel = tableModel;
		if (tableModel != null) {
			tableModel.addTableModelListener((TableModelEvent tme) -> attView());
		}
	}

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
	// Generated using JFormDesigner Evaluation license - Victor Cleber Laureano
	private void initComponents() {
		brnPrimeira = new JButton();
		btnAnterior = new JButton();
		JLabel lblPagina = new JLabel();
		txtPagina = new JFormattedTextField();
		JLabel lblDe = new JLabel();
		txtTotalPaginas = new JFormattedTextField();
		JLabel lblTotal = new JLabel();
		txtTotal = new JFormattedTextField();
		btnProxima = new JButton();
		btnUltima = new JButton();

		//======== this ========
		setOpaque(false);


		setLayout(new FlowLayout());
		((FlowLayout)getLayout()).setAlignOnBaseline(true);

		//---- brnPrimeira ----
		brnPrimeira.addActionListener(this::primeira);
		add(brnPrimeira);

		//---- btnAnterior ----
		btnAnterior.addActionListener(this::anterior);
		add(btnAnterior);

		//---- lblPagina ----
		lblPagina.setText("Pagina");
		lblPagina.setFocusable(false);
		add(lblPagina);

		//---- txtPagina ----
		txtPagina.setHorizontalAlignment(SwingConstants.CENTER);
		txtPagina.setText("1");
		txtPagina.setMinimumSize(new Dimension(0, 0));
		txtPagina.setPreferredSize(new Dimension(35, 20));
		txtPagina.addActionListener(this::changePagina);
		add(txtPagina);

		//---- lblDe ----
		lblDe.setText("de");
		add(lblDe);

		//---- txtTotalPaginas ----
		txtTotalPaginas.setEditable(false);
		txtTotalPaginas.setHorizontalAlignment(SwingConstants.CENTER);
		txtTotalPaginas.setText("0");
		txtTotalPaginas.setEnabled(false);
		txtTotalPaginas.setFocusable(false);
		txtTotalPaginas.setMinimumSize(new Dimension(0, 0));
		txtTotalPaginas.setPreferredSize(new Dimension(35, 20));
		add(txtTotalPaginas);

		//---- lblTotal ----
		lblTotal.setText("Total");
		lblTotal.setFocusable(false);
		add(lblTotal);

		//---- txtTotal ----
		txtTotal.setEditable(false);
		txtTotal.setHorizontalAlignment(SwingConstants.CENTER);
		txtTotal.setText("0");
		txtTotal.setEnabled(false);
		txtTotal.setFocusable(false);
		txtTotal.setMinimumSize(new Dimension(0, 0));
		txtTotal.setPreferredSize(new Dimension(50, 20));
		add(txtTotal);

		//---- btnProxima ----
		btnProxima.addActionListener(this::proxima);
		add(btnProxima);

		//---- btnUltima ----
		btnUltima.addActionListener(this::ultima);
		add(btnUltima);
    }// </editor-fold>//GEN-END:initComponents
	// <editor-fold defaultstate="collapsed" desc="Eventos">
    private void anterior(@SuppressWarnings("unused") java.awt.event.ActionEvent evt) {//GEN-FIRST:event_anterior
		tableModel.previousPage();
    }//GEN-LAST:event_anterior

    private void primeira(@SuppressWarnings("unused") java.awt.event.ActionEvent evt) {//GEN-FIRST:event_primeira
		tableModel.setPage(1);
    }//GEN-LAST:event_primeira

    private void proxima(@SuppressWarnings("unused") java.awt.event.ActionEvent evt) {//GEN-FIRST:event_proxima
		tableModel.nextPage();
    }//GEN-LAST:event_proxima

    private void ultima(@SuppressWarnings("unused") java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ultima
		tableModel.setPage(Integer.MAX_VALUE);
    }//GEN-LAST:event_ultima

    private void changePagina(@SuppressWarnings("unused") java.awt.event.ActionEvent evt) {//GEN-FIRST:event_changePagina
		tableModel.setPage(Integer.parseInt(txtPagina.getText()));
    }//GEN-LAST:event_changePagina
	// </editor-fold>
	// <editor-fold defaultstate="collapsed" desc="Variáveis">
    // Variables declaration - do not modify//GEN-BEGIN:variables
	// Generated using JFormDesigner Evaluation license - Victor Cleber Laureano
	private JButton brnPrimeira;
	private JButton btnAnterior;
	private JFormattedTextField txtPagina;
	private JFormattedTextField txtTotalPaginas;
	private JFormattedTextField txtTotal;
	private JButton btnProxima;
	private JButton btnUltima;
    // End of variables declaration//GEN-END:variables
	// </editor-fold>
}
