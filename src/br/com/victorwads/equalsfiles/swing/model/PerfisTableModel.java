/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.victorwads.equalsfiles.swing.model;

// <editor-fold defaultstate="collapsed" desc="Imports">
import br.com.victorwads.equalsfiles.model.Diretorio;
import br.com.victorwads.equalsfiles.model.Perfil;
import javax.swing.JTable;
// </editor-fold>

/**
 *
 * @author victo
 */
public final class PerfisTableModel extends MyTableModel<Perfil> {

	public PerfisTableModel() {
		super(new String[]{"Nome", "Diretorios"});
	}

	public PerfisTableModel(Perfil[] perfis) {
		this();
		addAll(perfis);
	}

	@Override
	public void setTableModel(JTable table) {
		table.setModel(this);
		for (int i = 0; i < COLUNAS.length; i++) {
			int min = 0, max = Integer.MAX_VALUE;
			if (i == 0) {
				max = 70;
			}
			table.getColumnModel().getColumn(i).setMinWidth(min);
			table.getColumnModel().getColumn(i).setMaxWidth(max);
		}
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		Object o = null;
		Perfil p = getRowAt(rowIndex);
		switch (columnIndex) {
			case 0:
				o = p.getNome();
				break;
			case 1:
				o = Diretorio.toString(p.getDiretorios());
				break;
		}
		return o;
	}

}
