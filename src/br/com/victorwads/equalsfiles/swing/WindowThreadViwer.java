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
import br.com.victorwads.equalsfiles.controller.CacheMD5;
import br.com.victorwads.equalsfiles.controller.ThreadListListenner;
import br.com.victorwads.equalsfiles.swing.components.Janela;
import br.com.victorwads.equalsfiles.swing.components.icones.GetIcone;
import static br.com.victorwads.equalsfiles.swing.components.icones.GetIcone.setResImage;
import java.text.DecimalFormat;
// </editor-fold>

/**
 *
 * @author victo
 */
public class WindowThreadViwer extends Janela implements ThreadListListenner {

	/**
	 * Creates new form ThreadViwer
	 */
	public WindowThreadViwer() {
		initComponents();
		setResImage(this, GetIcone.VIEWER);
		setVisible(true);
		CacheMD5.setListenner(this);
	}

	// <editor-fold defaultstate="collapsed" desc="Methodos">
	@Override
	public void changeState(int threadRunning, int threadQueueTotal, int threadTotal) {
		barProgresso.setMaximum(threadQueueTotal);
		barProgresso.setValue(threadRunning);
		barProgresso.setString(new DecimalFormat("#.##").format((float) threadRunning / (float) threadQueueTotal * 100f) + " %");
		txtEspera.setText("" + threadQueueTotal);
		txtTotal.setText("" + threadTotal);
		txtFila.setText("" + threadRunning);
	}
	// </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
	// Generated using JFormDesigner Evaluation license - Victor Cleber Laureano
	private void initComponents() {
		JLabel lbl1 = new JLabel();
		txtFila = new JTextField();
		barProgresso = new JProgressBar();
		JLabel lbl2 = new JLabel();
		txtEspera = new JTextField();
		JLabel lbl3 = new JLabel();
		txtTotal = new JTextField();

		//======== this ========
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setTitle("Thread Viwer");
		setResizable(false);
		Container contentPane = getContentPane();

		//---- lbl1 ----
		lbl1.setText("Posi\u00e7\u00e3o da Fila");

		//---- txtFila ----
		txtFila.setEditable(false);
		txtFila.setText("0");

		//---- barProgresso ----
		barProgresso.setStringPainted(true);

		//---- lbl2 ----
		lbl2.setText("Em Espera");

		//---- txtEspera ----
		txtEspera.setEditable(false);
		txtEspera.setText("0");

		//---- lbl3 ----
		lbl3.setText("Total");

		//---- txtTotal ----
		txtTotal.setEditable(false);
		txtTotal.setText("0");

		GroupLayout contentPaneLayout = new GroupLayout(contentPane);
		contentPane.setLayout(contentPaneLayout);
		contentPaneLayout.setHorizontalGroup(
			contentPaneLayout.createParallelGroup()
				.addGroup(contentPaneLayout.createSequentialGroup()
					.addContainerGap()
					.addGroup(contentPaneLayout.createParallelGroup()
						.addGroup(contentPaneLayout.createSequentialGroup()
							.addComponent(lbl1)
							.addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
							.addComponent(txtFila, GroupLayout.PREFERRED_SIZE, 73, GroupLayout.PREFERRED_SIZE)
							.addGap(18, 18, 18)
							.addComponent(lbl2)
							.addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
							.addComponent(txtEspera, GroupLayout.PREFERRED_SIZE, 73, GroupLayout.PREFERRED_SIZE)
							.addGap(18, 18, 18)
							.addComponent(lbl3)
							.addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
							.addComponent(txtTotal, GroupLayout.PREFERRED_SIZE, 73, GroupLayout.PREFERRED_SIZE)
							.addGap(0, 0, Short.MAX_VALUE))
						.addComponent(barProgresso, GroupLayout.Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
					.addContainerGap())
		);
		contentPaneLayout.setVerticalGroup(
			contentPaneLayout.createParallelGroup()
				.addGroup(contentPaneLayout.createSequentialGroup()
					.addContainerGap()
					.addGroup(contentPaneLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
						.addComponent(lbl1)
						.addComponent(txtFila, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(lbl2)
						.addComponent(txtEspera, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(lbl3)
						.addComponent(txtTotal, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
					.addComponent(barProgresso, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addContainerGap())
		);
		pack();
		setLocationRelativeTo(getOwner());
    }// </editor-fold>//GEN-END:initComponents
	// <editor-fold defaultstate="collapsed" desc="Variáveis">
    // Variables declaration - do not modify//GEN-BEGIN:variables
	// Generated using JFormDesigner Evaluation license - Victor Cleber Laureano
	private JTextField txtFila;
	private JProgressBar barProgresso;
	private JTextField txtEspera;
	private JTextField txtTotal;
    // End of variables declaration//GEN-END:variables
	// </editor-fold>
}
