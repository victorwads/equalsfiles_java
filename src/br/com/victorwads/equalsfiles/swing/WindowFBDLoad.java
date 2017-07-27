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
import br.com.victorwads.equalsfiles.controller.CacheMD5;
import br.com.victorwads.equalsfiles.model.MD5Cache;
import br.com.victorwads.equalsfiles.swing.components.Janela;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.util.Map;
import javax.swing.JFrame;
// </editor-fold>

/**
 *
 * @author victo
 */
public class WindowFBDLoad extends Janela {

	File file;
	Thread running;
	JFrame parent;

	/**
	 * Creates new form ConvertBDFile
	 *
	 * @param file
	 * @param parent
	 */
	public WindowFBDLoad(File file, JFrame parent) {
		initComponents();
		setVisible(true);

		this.parent = parent;
		this.parent.setVisible(false);
		this.file = file;

		running = new Thread(processo);
		running.start();
	}

	// <editor-fold defaultstate="collapsed" desc="Methodos">
	private void loading(int i) {
		barProgresso.setValue(i);
		barProgresso.setString(new DecimalFormat("#.##").format((float) i / (float) barProgresso.getMaximum() * 100f) + " %");
	}

	private boolean stop = false;
	final Runnable processo = new Runnable() {

		@Override
		public void run() {
			barProgresso.setString("Carregando e verificando arquivos.");
			barProgresso.setIndeterminate(true);
			MD5Cache m = new MD5Cache();
			try {
				FileInputStream stream = new FileInputStream(file);
				BufferedReader br = new BufferedReader(new InputStreamReader(stream));
				String strLine, caminho;
				while ((strLine = br.readLine()) != null && !stop) {
					caminho = strLine.substring(32);
					if (caminho != null) {
						m.put(caminho, strLine.substring(0, 32));
					}
				}
				br.close();
			} catch (Exception e) {
				e.printStackTrace();
			}

			barProgresso.setIndeterminate(false);
			barProgresso.setMaximum(m.size());
			loading(0);
			for (Map.Entry<String, String> e : m.entrySet()) {
				if (stop) {
					break;
				}
				CacheMD5.addCache(new MD5Cache.Join(e.getValue(), e.getKey()));
				loading(barProgresso.getValue() + 1);
			}
			loading(m.size());

			close();
		}
	};

	private void close() {
		parent.setVisible(true);
		setVisible(false);
		dispose();
	}
// </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
	// Generated using JFormDesigner Evaluation license - Victor Cleber Laureano
	private void initComponents() {
		barProgresso = new JProgressBar();

		//======== this ========
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setTitle("Carregando Arquivo");
		setResizable(false);
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				formWindowClosing(e);
			}
		});
		Container contentPane = getContentPane();

		GroupLayout contentPaneLayout = new GroupLayout(contentPane);
		contentPane.setLayout(contentPaneLayout);
		contentPaneLayout.setHorizontalGroup(
			contentPaneLayout.createParallelGroup()
				.addGroup(contentPaneLayout.createSequentialGroup()
					.addContainerGap()
					.addComponent(barProgresso, GroupLayout.DEFAULT_SIZE, 471, Short.MAX_VALUE)
					.addContainerGap())
		);
		contentPaneLayout.setVerticalGroup(
			contentPaneLayout.createParallelGroup()
				.addGroup(contentPaneLayout.createSequentialGroup()
					.addContainerGap()
					.addComponent(barProgresso, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
					.addContainerGap())
		);
		pack();
		setLocationRelativeTo(getOwner());
    }// </editor-fold>//GEN-END:initComponents
	// <editor-fold defaultstate="collapsed" desc="Eventos">
    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
		if (running.isAlive()) {
			stop = true;
		}
		close();
    }//GEN-LAST:event_formWindowClosing
	// </editor-fold>
	// <editor-fold defaultstate="collapsed" desc="Variáveis">
    // Variables declaration - do not modify//GEN-BEGIN:variables
	// Generated using JFormDesigner Evaluation license - Victor Cleber Laureano
	private JProgressBar barProgresso;
    // End of variables declaration//GEN-END:variables
	// </editor-fold>
}
