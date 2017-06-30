/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.victorwads.equalsfiles.swing;

// <editor-fold defaultstate="collapsed" desc="Imports">
import br.com.victorwads.equalsfiles.controller.Perfils;
import br.com.victorwads.equalsfiles.model.Diretorio;
import br.com.victorwads.equalsfiles.swing.components.Card;
import br.com.victorwads.equalsfiles.swing.components.icones.GetIcone;
import static br.com.victorwads.equalsfiles.swing.components.icones.GetIcone.setResImage;
import br.com.victorwads.equalsfiles.swing.model.PerfisTableModel;
import br.com.victorwads.equalsfiles.swing.util.Dialogos;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.AbstractAction;
import javax.swing.InputMap;
import javax.swing.JLabel;
import javax.swing.KeyStroke;
import javax.swing.event.ListSelectionEvent;
import javax.swing.table.DefaultTableCellRenderer;
// </editor-fold>

/**
 *
 * @author victo
 */
public class CardPerfils extends Card {

	// <editor-fold defaultstate="collapsed" desc="Classes">
	public interface Load {

		/**
		 *
		 * @param diretorios
		 */
		public void cleanAndAddDiretorio(Diretorio[] diretorios);

		/**
		 *
		 */
		public void showCard();
	}
	// </editor-fold>

	private final CardPerfil addView;
	private final Load loadView;
	private boolean init = false;
	private DefaultTableCellRenderer centerRenderer;
	private DefaultTableCellRenderer centerHeaderRenderer;
	private PerfisTableModel tableModel;

	/**
	 * Creates new form cardPerfils
	 *
	 * @param loadView
	 * @param addView
	 */
	public CardPerfils(Load loadView, CardPerfil addView) {
		this.loadView = loadView;
		this.addView = addView;
	}

	// <editor-fold defaultstate="collapsed" desc="Methodos">
	private void init() {
		if (init) {
			return;
		}
		init = true;
		initComponents();
		setResImage(btnEditar, GetIcone.EDITAR);
		setResImage(btnAdicionar, GetIcone.ADICONAR);
		setResImage(btnRemover, GetIcone.LIMPAR);

		//Table Config
		centerRenderer = (DefaultTableCellRenderer) table.getDefaultRenderer(String.class);
		centerHeaderRenderer = (DefaultTableCellRenderer) table.getTableHeader().getDefaultRenderer();
		centerRenderer.setHorizontalAlignment(JLabel.CENTER);
		centerHeaderRenderer.setHorizontalAlignment(JLabel.CENTER);
		table.getSelectionModel().addListSelectionListener((ListSelectionEvent e) -> {
			if (table.getSelectedRow() == -1) {
				btnRemover.setEnabled(false);
				btnEditar.setEnabled(false);
			} else {
				btnRemover.setEnabled(true);
				btnEditar.setEnabled(true);
			}
		});
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getButton() == 1 && e.getClickCount() == 2) {
					abrir();
				}
			}
		});
		tableModel = new PerfisTableModel();
		tableModel.setTableModel(table);
		// <editor-fold defaultstate="collapsed" desc="Key Events">
		InputMap inMap = getInputMap(WHEN_IN_FOCUSED_WINDOW);
		inMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_E, InputEvent.CTRL_DOWN_MASK), "EDITAR_PERFIL");
		inMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_A, InputEvent.CTRL_DOWN_MASK), "ADICIONAR_PERFIL");
		inMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0), "DELETE_PERFIL");
		inMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, InputEvent.CTRL_DOWN_MASK), "LOAD");
		inMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_F5, 0), "RELOAD");
		getActionMap().put("RELOAD", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				listar();
			}
		});
		getActionMap().put("ADICIONAR_PERFIL", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				adicionar();
			}
		});
		getActionMap().put("EDITAR_PERFIL", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				editar();
			}
		});
		getActionMap().put("DELETE_PERFIL", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				remover();
			}
		});
		getActionMap().put("LOAD", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				abrir();
			}
		});
		// </editor-fold>
	}

	private void clean() {
		tableModel.clear();
		System.gc();
	}

	private void listar() {
		tableModel.addAll(Perfils.listar());
		table.requestFocus();
	}

	private void abrir() {
		int s = table.getSelectedRow();
		if (s != -1) {
			loadView.cleanAndAddDiretorio(tableModel.getRow(s).getDiretorios());
			loadView.showCard();
			setVisible(false);
		}
	}

	private void adicionar() {
		addView.newPerfil();
		addView.show(this);
	}

	private void editar() {
		int s = table.getSelectedRow();
		if (s != -1) {
			addView.setPerfil(tableModel.getRow(s));
			addView.show(this);
		}
	}

	private void remover() {
		int s = table.getSelectedRow();
		if (s != -1 && Dialogos.pergunta("Deseja realmente excluir o perfil selecionado?")) {
			Perfils.excluir(tableModel.getRow(s));
			tableModel.removeRow(s);
		}
	}
	// </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        btnAdicionar = new javax.swing.JButton();
        javax.swing.JScrollPane tableScroll = new javax.swing.JScrollPane();
        table = new javax.swing.JTable();
        btnRemover = new javax.swing.JButton();
        btnEditar = new javax.swing.JButton();

        btnAdicionar.setText("Adicionar");
        btnAdicionar.setFocusable(false);
        btnAdicionar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAdicionar(evt);
            }
        });

        tableScroll.setViewportView(table);

        btnRemover.setText("Remover");
        btnRemover.setEnabled(false);
        btnRemover.setFocusable(false);
        btnRemover.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRemover(evt);
            }
        });

        btnEditar.setText("Editar");
        btnEditar.setEnabled(false);
        btnEditar.setFocusable(false);
        btnEditar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditar(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(tableScroll, javax.swing.GroupLayout.DEFAULT_SIZE, 505, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btnAdicionar)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnEditar)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnRemover)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(40, 40, 40)
                .addComponent(tableScroll, javax.swing.GroupLayout.DEFAULT_SIZE, 287, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnRemover)
                    .addComponent(btnAdicionar)
                    .addComponent(btnEditar))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents
	// <editor-fold defaultstate="collapsed" desc="Eventos">
    private void btnAdicionar(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAdicionar
		adicionar();
    }//GEN-LAST:event_btnAdicionar

    private void btnRemover(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRemover
		remover();
    }//GEN-LAST:event_btnRemover

    private void btnEditar(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditar
		editar();
    }//GEN-LAST:event_btnEditar

	@Override
	public void cardHide(ComponentEvent evt) {
		if (init) {
			clean();
		}
	}

	@Override
	public void cardShow(ComponentEvent evt) {
		init();
		listar();
	}
	// </editor-fold>
	// <editor-fold defaultstate="collapsed" desc="Variáveis">
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAdicionar;
    private javax.swing.JButton btnEditar;
    private javax.swing.JButton btnRemover;
    private javax.swing.JTable table;
    // End of variables declaration//GEN-END:variables
	// </editor-fold>
}
