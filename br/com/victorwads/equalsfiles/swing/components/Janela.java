/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.victorwads.equalsfiles.swing.components;

// <editor-fold defaultstate="collapsed" desc="Imports">
import br.com.victorwads.equalsfiles.swing.WindowPrincipal;
import br.com.victorwads.equalsfiles.swing.components.icones.GetIcone;
import static br.com.victorwads.equalsfiles.swing.components.icones.GetIcone.setResImage;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
// </editor-fold>

/**
 *
 * @author victo
 */
public class Janela extends JFrame {

	private final static ArrayList<Janela> JANELAS = new ArrayList<>();
	protected String title = null;

	public Janela() {
		this(null);
	}

	public Janela(Janela parent) {
		this(parent, null);
	}

	public Janela(Janela parent, String titulo) {
		title = titulo;
		setTitle(title);
		setResImage(this, GetIcone.MAIN);
		getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_P, InputEvent.CTRL_DOWN_MASK | InputEvent.ALT_DOWN_MASK), "REOPEN_MAIN");
		getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "CLOSE_ON_ESC");
		getRootPane().getActionMap().put("CLOSE_ON_ESC", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				switch (getDefaultCloseOperation()) {
					case WindowConstants.DISPOSE_ON_CLOSE:
						dispose();
						break;
					case WindowConstants.HIDE_ON_CLOSE:
						setVisible(false);
						break;
					case WindowConstants.EXIT_ON_CLOSE:
						System.exit(0);
						break;
				}
			}
		});
		getRootPane().getActionMap().put("REOPEN_MAIN", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new WindowPrincipal();
			}
		});
	}

	public static void center(Window window) {
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		window.setLocation(dim.width / 2 - window.getSize().width / 2, dim.height / 2 - window.getSize().height / 2);
	}

	public void center() {
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		setLocation(dim.width / 2 - this.getSize().width / 2, dim.height / 2 - this.getSize().height / 2);
	}

	protected void realodUI() {
		JANELAS.forEach(SwingUtilities::updateComponentTreeUI);
	}

	@Override
	public void setTitle(String string) {
		if (title == null) {
			title = string;
		}
		super.setTitle(string);
	}

	@Override
	public void dispose() {
		JANELAS.remove(this);
		super.dispose();
	}

	@Override
	public void setVisible(boolean bln) {
		if (bln && JANELAS.indexOf(this) == -1) {
			JANELAS.add(this);
		}
		super.setVisible(bln);
	}

	@Override
	public void pack() {
		super.pack();
		center();
	}

}
