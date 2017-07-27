/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.victorwads.equalsfiles.swing.components;

// <editor-fold defaultstate="collapsed" desc="Imports">
import java.awt.Component;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 *
 * @author victo
 */
public class Card extends JPanel {

	/**
	 *
	 */
	public static class CardName {

		private static int COUNT_Cards = 0;
		public final String NAME;
		public final String TITLE;

		/**
		 *
		 */
		public CardName() {
			NAME = Integer.toString(COUNT_Cards);
			TITLE = "Card sem título";
			COUNT_Cards++;
		}

		public CardName(String titulo) {
			NAME = Integer.toString(COUNT_Cards);
			TITLE = titulo;
			COUNT_Cards++;
		}

	}

	/**
	 *
	 */
	public Card() {
		setFocusable(true);
		addComponentListener(new ComponentAdapter() {
			@Override
			public void componentShown(ComponentEvent e) {
				cardShow(e);
			}

			@Override
			public void componentHidden(ComponentEvent e) {
				cardHide(e);
			}
		});
	}

	public JFrame getParentJFrame() {
		Component p = getParent();
		while (p != null && !(p instanceof JFrame)) {
			p = p.getParent();
		}
		return (JFrame) p;
	}

	/**
	 *
	 * @param evt
	 */
	public void cardShow(ComponentEvent evt) {
	}

	/**
	 *
	 * @param evt
	 */
	public void cardHide(ComponentEvent evt) {
	}

}
