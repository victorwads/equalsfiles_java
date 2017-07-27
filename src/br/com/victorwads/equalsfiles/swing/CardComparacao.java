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
import br.com.victorwads.equalsfiles.model.Diretorio;
import br.com.victorwads.equalsfiles.model.Resulted;
import br.com.victorwads.equalsfiles.swing.components.Card;
import br.com.victorwads.equalsfiles.swing.components.icones.GetIcone;
import static br.com.victorwads.equalsfiles.swing.components.icones.GetIcone.setResImage;
import br.com.victorwads.equalsfiles.swing.util.GerenciaDiretorios;
import java.awt.event.ActionEvent;
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
public class CardComparacao extends Card implements CardPerfils.Load {

	private final GerenciaDiretorios gerenciador;
	private boolean verificarNomes = false;

	/**
	 * Creates new form DepoisDouNome
	 */
	public CardComparacao() {
		initComponents();
		setResImage(btnAdicionar, GetIcone.ADICONAR);
		setResImage(btnRemover, GetIcone.LIMPAR);
		setResImage(btnIniciar, GetIcone.PLAY);
		gerenciador = new GerenciaDiretorios(this, listDiretorios, btnRemover);
		checkNomes.setSelected(verificarNomes);

		// <editor-fold defaultstate="collapsed" desc="Eventos">
		InputMap inMap = getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
		inMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, InputEvent.CTRL_DOWN_MASK), "INICIAR");
		getActionMap().put("INICIAR", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				iniciar();
			}
		});
		// </editor-fold>
	}

	// <editor-fold defaultstate="collapsed" desc="Methodos">
	public void iniciar() {
		new WindowProcesso(new Resulted(gerenciador.getDiretorios(), verificarNomes));
	}

	@Override
	public void cleanAndAddDiretorio(Diretorio[] diretorios) {
		gerenciador.clear();
		for (Diretorio d : diretorios) {
			gerenciador.addDiretorio(d);
		}
	}

	@Override
	public void showCard() {
		setVisible(true);
	}
	// </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
	// Generated using JFormDesigner Evaluation license - Victor Cleber Laureano
	private void initComponents() {
		btnIniciar = new JButton();
		checkNomes = new JCheckBox();
		btnRemover = new JButton();
		JScrollPane listDiretoriosScroll = new JScrollPane();
		listDiretorios = new JList<>();
		btnAdicionar = new JButton();
		JLabel lbl1 = new JLabel();

		//======== this ========
		setMinimumSize(new Dimension(600, 200));
		setPreferredSize(new Dimension(600, 200));



		//---- btnIniciar ----
		btnIniciar.setText("Iniciar");
		btnIniciar.setFocusable(false);
		btnIniciar.addActionListener(e -> btnIniciar(e));

		//---- checkNomes ----
		checkNomes.setText("Checar Nomes");
		checkNomes.setFocusable(false);
		checkNomes.addActionListener(e -> checkNomes(e));

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

		GroupLayout layout = new GroupLayout(this);
		setLayout(layout);
		layout.setHorizontalGroup(
			layout.createParallelGroup()
				.addGroup(layout.createSequentialGroup()
					.addContainerGap()
					.addGroup(layout.createParallelGroup()
						.addGroup(layout.createSequentialGroup()
							.addComponent(lbl1)
							.addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
							.addComponent(btnAdicionar)
							.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
							.addComponent(btnRemover)
							.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 214, Short.MAX_VALUE)
							.addComponent(checkNomes)
							.addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
							.addComponent(btnIniciar))
						.addComponent(listDiretoriosScroll))
					.addContainerGap())
		);
		layout.setVerticalGroup(
			layout.createParallelGroup()
				.addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
					.addContainerGap()
					.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
						.addComponent(btnIniciar)
						.addComponent(btnRemover)
						.addComponent(btnAdicionar)
						.addComponent(lbl1)
						.addComponent(checkNomes))
					.addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
					.addComponent(listDiretoriosScroll, GroupLayout.DEFAULT_SIZE, 144, Short.MAX_VALUE)
					.addContainerGap())
		);
    }// </editor-fold>//GEN-END:initComponents
	// <editor-fold defaultstate="collapsed" desc="Eventos">
    private void btnIniciar(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnIniciar
		iniciar();
    }//GEN-LAST:event_btnIniciar

    private void checkNomes(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_checkNomes
		verificarNomes = checkNomes.isSelected();
    }//GEN-LAST:event_checkNomes

    private void btnRemover(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRemover
		gerenciador.removeDiretorio();
    }//GEN-LAST:event_btnRemover

    private void btnAdicionar(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAdicionar
		gerenciador.userAddDiretorio();
    }//GEN-LAST:event_btnAdicionar
	// </editor-fold>
	// <editor-fold defaultstate="collapsed" desc="Variáveis">
    // Variables declaration - do not modify//GEN-BEGIN:variables
	// Generated using JFormDesigner Evaluation license - Victor Cleber Laureano
	private JButton btnIniciar;
	private JCheckBox checkNomes;
	private JButton btnRemover;
	private JList<String> listDiretorios;
	private JButton btnAdicionar;
    // End of variables declaration//GEN-END:variables
	// </editor-fold>
}
