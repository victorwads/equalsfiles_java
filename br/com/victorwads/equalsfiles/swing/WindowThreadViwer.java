/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.victorwads.equalsfiles.swing;

// <editor-fold defaultstate="collapsed" desc="Imports">
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
    private void initComponents() {

        javax.swing.JLabel lbl1 = new javax.swing.JLabel();
        txtFila = new javax.swing.JTextField();
        barProgresso = new javax.swing.JProgressBar();
        javax.swing.JLabel lbl2 = new javax.swing.JLabel();
        txtEspera = new javax.swing.JTextField();
        javax.swing.JLabel lbl3 = new javax.swing.JLabel();
        txtTotal = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Thread Viwer");
        setResizable(false);

        lbl1.setText("Posição da Fila");

        txtFila.setEditable(false);
        txtFila.setText("0");

        barProgresso.setStringPainted(true);

        lbl2.setText("Em Espera");

        txtEspera.setEditable(false);
        txtEspera.setText("0");

        lbl3.setText("Total");

        txtTotal.setEditable(false);
        txtTotal.setText("0");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(lbl1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtFila, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(lbl2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtEspera, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(lbl3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(barProgresso, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbl1)
                    .addComponent(txtFila, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbl2)
                    .addComponent(txtEspera, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbl3)
                    .addComponent(txtTotal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(barProgresso, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
	// <editor-fold defaultstate="collapsed" desc="Variáveis">
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JProgressBar barProgresso;
    private javax.swing.JTextField txtEspera;
    private javax.swing.JTextField txtFila;
    private javax.swing.JTextField txtTotal;
    // End of variables declaration//GEN-END:variables
	// </editor-fold>
}
