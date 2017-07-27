/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.victorwads.equalsfiles.swing.util;

import br.com.victorwads.equalsfiles.model.Diretorio;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

/**
 *
 * @author victo
 */
public class Dialogos {

	private static String titulo = "Sistema Victor - Aula JP";

	public static void info(String message) {
		JOptionPane.showMessageDialog(null, message, titulo, JOptionPane.INFORMATION_MESSAGE);
	}

	public static void erro(String message) {
		JOptionPane.showMessageDialog(null, message, titulo, JOptionPane.ERROR_MESSAGE);
	}

	public static boolean pergunta(String message) {
		return JOptionPane.showConfirmDialog(null, message, titulo, JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION;
	}

	private static class prepareFileChooser implements ActionListener {

		private boolean eventFired = false;
		private JFileChooser dialogo = new JFileChooser(File.separator);

		public prepareFileChooser(Component janela, String titulo, String textoBotao, int dialogType, int selectionMode) {
			dialogo.addActionListener(this);
			dialogo.setFileSelectionMode(selectionMode);
			dialogo.setDialogType(dialogType);
			dialogo.setDialogTitle(titulo);
			dialogo.showDialog(janela, textoBotao);
		}

		@Override
		public void actionPerformed(ActionEvent evt) {
			if (!evt.getActionCommand().equals("CancelSelection")) {
				eventFired = true;
			}
		}

		public boolean isEventFired() {
			return eventFired;
		}

		public File getSelectedFile() {
			return dialogo.getSelectedFile();
		}
	}

	public static File abrirArquivo(Component janela) {
		prepareFileChooser chooser = new prepareFileChooser(janela, "Selecionar Arquivo", "Selecionar", JFileChooser.OPEN_DIALOG, JFileChooser.FILES_ONLY);
		File selected = chooser.getSelectedFile();
		if (chooser.isEventFired()) {
			try {
				if (selected.isFile()) {
					return selected;
				} else {
					erro("Arquivo inválido!");
				}
			} catch (Exception e) {
			}
		}
		return null;
	}

	public static File salvarArquivo(Component janela) {
		prepareFileChooser chooser = new prepareFileChooser(janela, "Salvar Arquivo", "Salvar", JFileChooser.SAVE_DIALOG, JFileChooser.FILES_ONLY);
		File selected = chooser.getSelectedFile();
		if (chooser.isEventFired()) {
			try {
				if (selected.isFile()) {
					if (pergunta("Esste arquivo já existe!\nDeseja substituir?")) {
						return selected;
					} else {
						return salvarArquivo(janela);
					}
				} else {
					if (!selected.getParentFile().isDirectory()) {
						erro("Caminho/Nome inválido!");
					} else {
						return selected;
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	public static Diretorio selecionarPasta(Component janela) {
		prepareFileChooser chooser = new prepareFileChooser(janela, "Selecionar Pasta", "Selecionar", JFileChooser.OPEN_DIALOG, JFileChooser.DIRECTORIES_ONLY);
		File selected = chooser.getSelectedFile();
		if (chooser.isEventFired()) {
			try {
				if (selected.isDirectory()) {
					return new Diretorio(selected.getCanonicalPath());
				} else {
					erro("Diretório inválido!");
				}
			} catch (Exception e) {
			}
		}
		return null;
	}
}
