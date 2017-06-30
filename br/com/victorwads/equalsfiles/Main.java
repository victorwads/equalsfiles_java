/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.victorwads.equalsfiles;

// <editor-fold defaultstate="collapsed" desc="Imports">
import br.com.victorwads.equalsfiles.controller.Indexacao;
import br.com.victorwads.equalsfiles.dao.Index;
import br.com.victorwads.equalsfiles.model.Diretorio;
import br.com.victorwads.equalsfiles.swing.WindowPrincipal;
import br.com.victorwads.equalsfiles.swing.components.icones.GetIcone;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.io.File;
import javax.swing.AbstractAction;
import javax.swing.UIManager;
// </editor-fold>

/**
 *
 * @author victo
 */
public class Main {

	private final static SystemTray tray = SystemTray.getSystemTray();
	private final static TrayIcon trayIcon = new TrayIcon(GetIcone.getImageIcone(GetIcone.TRAY).getImage(), "Equals Files");
	public final static String USERHOME = System.getProperty("user.home");
	public final static boolean suportTray = SystemTray.isSupported();
	public static Thread threadIndexacao = null;
	public static Indexacao objectIndexacao = null;

	public static boolean showTrayIcon() {
		if (suportTray) {
			if (trayIcon.getPopupMenu() == null) {
				//IconTray
				PopupMenu m = new PopupMenu("Equals Files");
				MenuItem abrir = new MenuItem("Abrir"), sair = new MenuItem("Ferchar Forçado");
				m.add(abrir);
				m.add(sair);
				sair.addActionListener(new AbstractAction() {
					@Override
					public void actionPerformed(ActionEvent ae) {
						System.exit(0);
					}
				});
				AbstractAction actionAbrir = new AbstractAction() {
					@Override
					public void actionPerformed(ActionEvent ae) {
						new WindowPrincipal();
						removeTrayIcon();
					}
				};
				abrir.addActionListener(actionAbrir);
				trayIcon.addActionListener(actionAbrir);
				trayIcon.setPopupMenu(m);
			}
			try {
				tray.add(trayIcon);
			} catch (Exception e) {
			}
		}
		return suportTray;
	}

	public static void removeTrayIcon() {
		if (suportTray) {
			try {
				tray.remove(trayIcon);
			} catch (Exception e) {
			}
		}
	}

	/**
	 * @param args the command line arguments
	 */
	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception ex) {
		}

//		args = new String[]{"starttray"};
		if (args.length > 0 && args[0].indexOf("starttray") != -1) {
			showTrayIcon();
			File[] roots = File.listRoots();
			int c = 0;
			for (int i = 0; i < roots.length; i++) {
				if (new Index().getRootId(roots[i].getAbsolutePath(), false) != 0) {
					c++;
				}
			}
			if (c == 0) {
				return;
			}
			Diretorio[] dirs = new Diretorio[c];
			c = 0;
			for (int i = 0; i < roots.length; i++) {
				if (new Index().getRootId(roots[i].getAbsolutePath(), false) != 0) {
					dirs[c++] = new Diretorio(roots[i].getAbsolutePath());
				}
			}
			objectIndexacao = new Indexacao(null, dirs, true);
			threadIndexacao = new Thread(objectIndexacao);
			threadIndexacao.start();
			dirs = null;
			roots = null;
		} else {
			new WindowPrincipal();
		}
		System.gc();
	}

}
