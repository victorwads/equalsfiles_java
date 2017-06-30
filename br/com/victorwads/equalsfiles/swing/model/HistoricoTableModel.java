/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.victorwads.equalsfiles.swing.model;

import br.com.victorwads.equalsfiles.model.ArquivoHistorico;
import br.com.victorwads.equalsfiles.swing.util.DateTime;
import javax.swing.JTable;

/**
 *
 * @author victo
 */
public final class HistoricoTableModel extends MyTableModel {

	public HistoricoTableModel() {
		super(new String[]{"Tipo", "Arquivo", "Data"});
	}

	public HistoricoTableModel(ArquivoHistorico[] arquivos) {
		this();
		addAll(arquivos);
	}

	public void addAll(ArquivoHistorico[] arquivos) {
		ROWS.clear();
		super.addAll(arquivos);
	}

	public void addRow(ArquivoHistorico a) {
		ROWS.add(a);
	}

	@Override
	public ArquivoHistorico removeRow(int index) {
		ArquivoHistorico a = (ArquivoHistorico) ROWS.remove(index);
		if (a != null) {
			tableDidChange();
		}
		return a;
	}

	/**
	 *
	 * @param index
	 * @return
	 */
	@Override
	public ArquivoHistorico getRow(int index) {
		return (ArquivoHistorico) ROWS.get(index);
	}

	/**
	 *
	 * @param table
	 */
	@Override
	public void setTableModel(JTable table) {
		super.setTableModel(table);
		for (int i = 0; i < COLUNAS.length; i++) {
			int min = 0, max = Integer.MAX_VALUE;
			switch (i) {
				case 0:
					min = 25;
					max = 35;
					break;
				case 2:
					min = 130;
					max = 140;
					break;
			}
			table.getColumnModel().getColumn(i).setMinWidth(min);
			table.getColumnModel().getColumn(i).setMaxWidth(max);
		}
	}

	@Override
	public Class getColumnClass(int columnIndex) {
		switch (columnIndex) {
			case 0:
				return ArquivoHistorico.Tipo.class;
			case 1:
				return ArquivoHistorico.class;
			default:
				return super.getColumnClass(columnIndex);
		}
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
		ArquivoHistorico a = (ArquivoHistorico) super.getValueAt(rowIndex, columnIndex);
		switch (columnIndex) {
			case 0:
				o = a.getTipo();
				break;
			case 1:
				o = a;
				break;
			case 2:
				o = new DateTime(a.getModificado());
				break;
		}
		return o;
	}

}
