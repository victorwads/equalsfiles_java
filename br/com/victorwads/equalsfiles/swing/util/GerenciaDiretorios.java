/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.victorwads.equalsfiles.swing.util;

// <editor-fold defaultstate="collapsed" desc="Imports">
import br.com.victorwads.equalsfiles.model.Diretorio;
import br.com.victorwads.equalsfiles.swing.components.Card;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import javax.swing.AbstractAction;
import javax.swing.DefaultListModel;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.KeyStroke;
import javax.swing.event.ListSelectionEvent;
// </editor-fold>

/**
 *
 * @author victo
 */
public class GerenciaDiretorios {

	// <editor-fold defaultstate="collapsed" desc="Classes">
	private class DiretorioFilho extends Diretorio {

		public DiretorioFilho(String patch) {
			super(patch);
		}

		public DiretorioFilho(Diretorio d) {
			super(d.toString());
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj) {
				return true;
			} else if (obj == null || getClass() != obj.getClass()) {
				return false;
			}
			final DiretorioFilho other = (DiretorioFilho) obj;
			return toString().indexOf(other.toString()) == 0 || other.toString().indexOf(this.toString()) == 0;
		}

		@Override
		public int hashCode() {
			int hash = 3;
			return hash;
		}
	}
	// </editor-fold>

	private final Card card;
	private final JList listDiretorios;
	private final JButton btnRemover;
	private final DefaultListModel DIRETORIOS = new DefaultListModel();

	/**
	 *
	 * @param card
	 * @param diretorios
	 * @param remover
	 */
	public GerenciaDiretorios(Card card, JList diretorios, JButton remover) {
		this.card = card;
		this.listDiretorios = diretorios;
		this.btnRemover = remover;
		listDiretorios.setModel(DIRETORIOS);
		// <editor-fold defaultstate="collapsed" desc="Eventos">
		listDiretorios.addListSelectionListener((ListSelectionEvent lse) -> {
			if (((JList) lse.getSource()).getSelectedIndices().length > 0) {
				btnRemover.setEnabled(true);
			}
		});
		InputMap inMap = card.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
		inMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0), "GDClass_REMOVER");
		inMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_A, InputEvent.CTRL_DOWN_MASK), "GDClass_ADICIONAR");
		card.getActionMap().put("GDClass_REMOVER", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				removeDiretorio();
			}
		});
		card.getActionMap().put("GDClass_ADICIONAR", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				userAddDiretorio();
			}
		});
		// </editor-fold>
	}

	// <editor-fold defaultstate="collapsed" desc="Methodos">
	public void userAddDiretorio() {
		Diretorio a = Dialogos.selecionarPasta(card);
		if (a != null) {
			addDiretorio(new Diretorio(a.toString()));
		}
	}

	public boolean addDiretorio(Diretorio diretorio) {
		DiretorioFilho caminho = new DiretorioFilho(diretorio);
		if (DIRETORIOS.indexOf(caminho) != -1) {
			Dialogos.erro("Caminho já inserido!");
			return false;
		}
		DIRETORIOS.addElement(caminho);
		listDiretorios.ensureIndexIsVisible(0);
		return true;
	}

	public void removeDiretorio() {
		if (listDiretorios.getSelectedIndices().length > 0) {
			DiretorioFilho[] selecteds = new DiretorioFilho[listDiretorios.getSelectedIndices().length];
			int c = 0;
			for (int i : listDiretorios.getSelectedIndices()) {
				selecteds[c] = (DiretorioFilho) DIRETORIOS.getElementAt(i);
				c++;
			}
			for (DiretorioFilho d : selecteds) {
				DIRETORIOS.removeElement(d);
			}
			listDiretorios.ensureIndexIsVisible(0);
			btnRemover.setEnabled(false);
		}
	}

	public void clear() {
		DIRETORIOS.removeAllElements();
		listDiretorios.ensureIndexIsVisible(0);
	}

	public Diretorio[] getDiretorios() {
		Diretorio[] diretorios = new Diretorio[DIRETORIOS.size()];
		for (int i = 0; i < DIRETORIOS.size(); i++) {
			diretorios[i] = (Diretorio) DIRETORIOS.getElementAt(i);
		}
		return diretorios;
	}
	// </editor-fold>

}
