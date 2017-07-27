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
import br.com.victorwads.equalsfiles.controller.Perfils;
import br.com.victorwads.equalsfiles.model.Diretorio;
import br.com.victorwads.equalsfiles.model.Perfil;
import br.com.victorwads.equalsfiles.swing.components.Card;
import br.com.victorwads.equalsfiles.swing.components.icones.GetIcone;
import static br.com.victorwads.equalsfiles.swing.components.icones.GetIcone.setResImage;
import br.com.victorwads.equalsfiles.swing.util.Dialogos;
import br.com.victorwads.equalsfiles.swing.util.GerenciaDiretorios;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import javax.swing.AbstractAction;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.KeyStroke;
// </editor-fold>

/**
 *
 * @author victo
 */
public class CardPerfil extends Card {

	private GerenciaDiretorios gerenciador;
	private Perfil perfil;
	private JComponent backComponent = null;
	private boolean init = false;

	/**
	 * Creates new form DepoisDouNome
	 */
	public CardPerfil() {
	}

	// <editor-fold defaultstate="collapsed" desc="Methodos">
	private void init() {
		if (init) {
			return;
		}
		init = true;
		initComponents();
		setResImage(btnSalvar, GetIcone.SALVAR);
		setResImage(btnAdicionar, GetIcone.ADICONAR);
		setResImage(btnRemover, GetIcone.LIMPAR);
		gerenciador = new GerenciaDiretorios(this, listDiretorios, btnRemover);

		// <editor-fold defaultstate="collapsed" desc="Eventos">
		InputMap inMap = getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
		inMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, InputEvent.CTRL_DOWN_MASK), "SALVAR");
		getActionMap().put("SALVAR", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				salvar();
			}
		});
		// </editor-fold>
	}

	private void clear() {
		perfil = null;
		txtNome.setText("");
		gerenciador.clear();
		System.gc();
	}

	private void salvar() {
		perfil.setNome(txtNome.getText().trim());
		perfil.setDiretorios(gerenciador.getDiretorios());
		if (perfil.getDiretorios().length == 0) {
			Dialogos.erro("Você deve selecionar ao menos uma pasta!");
			return;
		}
		if (perfil.getId() == 0) {
			Perfils.inserir(perfil);
		} else {
			Perfils.salvar(perfil);
		}
		setVisible(false);
		backComponent.setVisible(true);
	}

	public void newPerfil() {
		setPerfil(null);
	}

	public void setPerfil(Perfil p) {
		init();
		if (p == null) {
			p = new Perfil();
		}
		perfil = p;
		txtNome.setText(p.getNome());
		gerenciador.clear();
		for (Diretorio d : p.getDiretorios()) {
			gerenciador.addDiretorio(d);
		}
	}

	public void show(JComponent back) {
		backComponent = back;
		setVisible(true);
		back.setVisible(false);
	}
	// </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
	// Generated using JFormDesigner Evaluation license - Victor Cleber Laureano
	private void initComponents() {
		btnRemover = new JButton();
		JScrollPane listDiretoriosScroll = new JScrollPane();
		listDiretorios = new JList<>();
		btnAdicionar = new JButton();
		JLabel lbl1 = new JLabel();
		JLabel lbl2 = new JLabel();
		txtNome = new JTextField();
		btnSalvar = new JButton();

		//======== this ========
		setMinimumSize(new Dimension(600, 200));
		setPreferredSize(new Dimension(600, 200));



		//---- btnRemover ----
		btnRemover.setText("Remover");
		btnRemover.setEnabled(false);
		btnRemover.setFocusable(false);
		btnRemover.addActionListener(e -> btnRemover(e));

		//======== listDiretoriosScroll ========
		{

			//---- listDiretorios ----
			listDiretorios.setModel(new AbstractListModel<String>() {
				String[] values = {

				};
				@Override
				public int getSize() { return values.length; }
				@Override
				public String getElementAt(int i) { return values[i]; }
			});
			listDiretoriosScroll.setViewportView(listDiretorios);
		}

		//---- btnAdicionar ----
		btnAdicionar.setText("Adicionar");
		btnAdicionar.setFocusable(false);
		btnAdicionar.addActionListener(e -> btnAdicionar(e));

		//---- lbl1 ----
		lbl1.setText("Pastas:");

		//---- lbl2 ----
		lbl2.setText("Nome:");

		//---- txtNome ----
		txtNome.setColumns(25);

		//---- btnSalvar ----
		btnSalvar.setText("Salvar");
		btnSalvar.addActionListener(e -> btnSalvar(e));

		GroupLayout layout = new GroupLayout(this);
		setLayout(layout);
		layout.setHorizontalGroup(
			layout.createParallelGroup()
				.addGroup(layout.createSequentialGroup()
					.addContainerGap()
					.addGroup(layout.createParallelGroup()
						.addComponent(listDiretoriosScroll, GroupLayout.DEFAULT_SIZE, 682, Short.MAX_VALUE)
						.addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
							.addComponent(lbl2)
							.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
							.addComponent(txtNome, GroupLayout.PREFERRED_SIZE, 180, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
							.addComponent(btnSalvar)
							.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
							.addComponent(lbl1)
							.addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
							.addComponent(btnAdicionar)
							.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
							.addComponent(btnRemover)))
					.addContainerGap())
		);
		layout.setVerticalGroup(
			layout.createParallelGroup()
				.addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
					.addContainerGap()
					.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
						.addComponent(btnRemover)
						.addComponent(btnAdicionar)
						.addComponent(lbl1)
						.addComponent(lbl2)
						.addComponent(txtNome, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(btnSalvar))
					.addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
					.addComponent(listDiretoriosScroll, GroupLayout.DEFAULT_SIZE, 144, Short.MAX_VALUE)
					.addContainerGap())
		);
    }// </editor-fold>//GEN-END:initComponents
	// <editor-fold defaultstate="collapsed" desc="Eventos">
    private void btnAdicionar(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAdicionar
		gerenciador.userAddDiretorio();
    }//GEN-LAST:event_btnAdicionar

    private void btnRemover(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRemover
		gerenciador.removeDiretorio();
    }//GEN-LAST:event_btnRemover

    private void btnSalvar(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalvar
		salvar();
    }//GEN-LAST:event_btnSalvar

	@Override
	public void cardShow(ComponentEvent evt) {
		init();
	}

	@Override
	public void cardHide(ComponentEvent evt) {
		if (init) {
			salvar();
			clear();
		}
	}
	// </editor-fold>
	// <editor-fold defaultstate="collapsed" desc="Variáveis">
    // Variables declaration - do not modify//GEN-BEGIN:variables
	// Generated using JFormDesigner Evaluation license - Victor Cleber Laureano
	private JButton btnRemover;
	private JList<String> listDiretorios;
	private JButton btnAdicionar;
	private JTextField txtNome;
	private JButton btnSalvar;
    // End of variables declaration//GEN-END:variables
	// </editor-fold>
}
