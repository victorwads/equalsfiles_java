/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.victorwads.equalsfiles.swing.model;

import static br.com.victorwads.equalsfiles.controller.CacheMD5.humamSize;
import br.com.victorwads.equalsfiles.model.Diretorio;
import br.com.victorwads.equalsfiles.model.Resultado;
import br.com.victorwads.equalsfiles.swing.util.DateTime;
import javax.swing.JTable;

/**
 *
 * @author victo
 */
public final class RelatoriosTableModel extends MyTableModel {

	public RelatoriosTableModel() {
		super(new String[]{"Data", "Diretorios", "Duração", "Total Arquivos", "Ver. Nomes"});
	}

	public RelatoriosTableModel(Resultado[] resultados) {
		this();
		addAll(resultados);
	}

	public void addAll(Resultado[] resultados) {
		ROWS.clear();
		super.addAll(resultados);
	}

	public void addRow(Resultado r) {
		ROWS.add(r);
	}

	@Override
	public Resultado removeRow(int index) {
		Resultado r = (Resultado) ROWS.remove(index);
		if (r != null) {
			tableDidChange();
		}
		return r;
	}

	/**
	 *
	 * @param index
	 * @return
	 */
	@Override
	public Resultado getRow(int index) {
		return (Resultado) ROWS.get(index);
	}

	/**
	 *
	 * @param table
	 */
	@Override
	public void setTableModel(JTable table) {
		table.setModel(this);
		for (int i = 0; i < COLUNAS.length; i++) {
			int min = 0, max = Integer.MAX_VALUE;
			switch (i) {
				case 0:
					min = 130;
					max = 140;
					break;
				case 1:
					break;
				case 3:
					min = 115;
					max = 125;
					break;
				default:
					min = 70;
					max = 80;
					break;
			}
			table.getColumnModel().getColumn(i).setMinWidth(min);
			table.getColumnModel().getColumn(i).setMaxWidth(max);
		}
	}

	@Override
	public Class getColumnClass(int columnIndex) {
		return columnIndex == 4 ? Boolean.class : String.class;
	}

	/**
	 *
	 * @param rowIndex
	 * @param columnIndex
	 * @return
	 */
	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		Object o = null;
		Resultado r = (Resultado) ROWS.get(rowIndex);
		switch (columnIndex) {
			case 0:
				o = new DateTime(r.getData());
				break;
			case 1:
				o = Diretorio.toString(r.getDiretorios());
				break;
			case 2:
				o = DateTime.getDurationStringFromSeconds(r.getDuracao());
				break;
			case 3:
				o = r.getQuantidadeArquivos() + " [" + humamSize(r.getTamanhoArquivos()) + "]";
				break;
			case 4:
				o = r.isNomeSensitive();
				break;
		}
		return o;
	}

}
