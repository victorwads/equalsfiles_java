/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.victorwads.equalsfiles.swing;

// <editor-fold defaultstate="collapsed" desc="Imports">
import br.com.victorwads.equalsfiles.model.Diretorio;
import br.com.victorwads.equalsfiles.model.Resultado;
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
		new WindowProcesso(new Resultado(gerenciador.getDiretorios(), verificarNomes));
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
    private void initComponents() {

        btnIniciar = new javax.swing.JButton();
        checkNomes = new javax.swing.JCheckBox();
        btnRemover = new javax.swing.JButton();
        javax.swing.JScrollPane listDiretoriosScroll = new javax.swing.JScrollPane();
        listDiretorios = new javax.swing.JList<>();
        btnAdicionar = new javax.swing.JButton();
        javax.swing.JLabel lbl1 = new javax.swing.JLabel();

        setMinimumSize(new java.awt.Dimension(600, 200));
        setPreferredSize(new java.awt.Dimension(600, 200));

        btnIniciar.setText("Iniciar");
        btnIniciar.setFocusable(false);
        btnIniciar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnIniciar(evt);
            }
        });

        checkNomes.setText("Checar Nomes");
        checkNomes.setFocusable(false);
        checkNomes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                checkNomes(evt);
            }
        });

        btnRemover.setText("Remover");
        btnRemover.setEnabled(false);
        btnRemover.setFocusable(false);
        btnRemover.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRemover(evt);
            }
        });

        listDiretoriosScroll.setViewportView(listDiretorios);

        btnAdicionar.setText("Adicionar");
        btnAdicionar.setFocusable(false);
        btnAdicionar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAdicionar(evt);
            }
        });

        lbl1.setText("Pastas:");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(lbl1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnAdicionar)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnRemover)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 214, Short.MAX_VALUE)
                        .addComponent(checkNomes)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnIniciar))
                    .addComponent(listDiretoriosScroll))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnIniciar)
                    .addComponent(btnRemover)
                    .addComponent(btnAdicionar)
                    .addComponent(lbl1)
                    .addComponent(checkNomes))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(listDiretoriosScroll, javax.swing.GroupLayout.DEFAULT_SIZE, 144, Short.MAX_VALUE)
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
    private javax.swing.JButton btnAdicionar;
    private javax.swing.JButton btnIniciar;
    private javax.swing.JButton btnRemover;
    private javax.swing.JCheckBox checkNomes;
    private javax.swing.JList<String> listDiretorios;
    // End of variables declaration//GEN-END:variables
	// </editor-fold>
}
