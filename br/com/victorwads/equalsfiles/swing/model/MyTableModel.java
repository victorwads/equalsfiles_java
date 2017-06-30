/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.victorwads.equalsfiles.swing.model;

import java.util.ArrayList;
import java.util.Arrays;
import javax.swing.JTable;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

/**
 *
 * @author victo
 */
public abstract class MyTableModel implements TableModel {

	protected ArrayList<TableModelListener> listenners = new ArrayList<>();
	protected final String[] COLUNAS;
	protected final ArrayList<Object> ROWS = new ArrayList<>();
	protected int itensPorPagina = 0, pagina = 1;

	/**
	 *
	 * @param colunas
	 */
	public MyTableModel(String[] colunas) {
		COLUNAS = colunas;
	}

	/**
	 *
	 * @param objects
	 */
	protected void addAll(Object[] objects) {
		ROWS.addAll(Arrays.asList(objects));
		tableDidChange();
	}

	public void clear() {
		ROWS.clear();
		tableDidChange();
	}

	/**
	 *
	 * @param r
	 */
	public void addRow(Object r) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	/**
	 *
	 * @param index
	 * @return
	 */
	public Object removeRow(int index) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	/**
	 *
	 * @param index
	 * @return
	 */
	public Object getRow(int index) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	/**
	 *
	 * @param table
	 */
	public void setTableModel(JTable table) {
		table.setModel(this);
		verificaPagina();
		tableDidChange();
	}

	private void verificaPagina() {
		if (pagina > getTotalPaginas()) {
			pagina = getTotalPaginas();
		}
		if (pagina < 1) {
			pagina = 1;
		}
	}

	public int getItensPorPagina() {
		return itensPorPagina;
	}

	public void setItensPorPagina(int itensPorPagina) {
		if (itensPorPagina < 0) {
			itensPorPagina *= -1;
		}
		this.itensPorPagina = itensPorPagina;
		verificaPagina();
		tableDidChange();
	}

	public int getTotal() {
		return ROWS.size();
	}

	public int getTotalPaginas() {
		if (ROWS.isEmpty()) {
			return 0;
		}
		return (int) Math.ceil(ROWS.size() / itensPorPagina);
	}

	public int getPagina() {
		return pagina;
	}

	public void setPagina(int pagina) {
		this.pagina = pagina;
		verificaPagina();
		tableDidChange();
	}

	public void proximaPagina() {
		setPagina(pagina + 1);
	}

	public void anteriorPagina() {
		setPagina(pagina - 1);
	}

	/**
	 *
	 * @param rowIndex
	 * @param columnIndex
	 * @return
	 */
	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return false;
	}

	/**
	 *
	 * @return
	 */
	@Override
	public int getColumnCount() {
		return COLUNAS.length;
	}

	/**
	 *
	 * @param i
	 * @return
	 */
	@Override
	public String getColumnName(int i) {
		return COLUNAS[i];
	}

	/**
	 *
	 * @param columnIndex
	 * @return
	 */
	@Override
	public Class getColumnClass(int columnIndex) {
		return String.class;
	}

	/**
	 *
	 * @return
	 */
	@Override
	public int getRowCount() {
		if (itensPorPagina == 0 || itensPorPagina > ROWS.size()) {
			return ROWS.size();
		} else {
			return itensPorPagina;
		}
	}

	/**
	 * '
	 *
	 * @param rowIndex
	 * @param columnIndex
	 * @return
	 */
	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		if (itensPorPagina == 0) {
		} else {
			rowIndex = ((pagina - 1) * itensPorPagina) + rowIndex;
		}
		return ROWS.get(rowIndex);
	}

	/**
	 *
	 * @param o
	 * @param i
	 * @param i1
	 */
	@Override
	public void setValueAt(Object o, int i, int i1) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	/**
	 *
	 * @param tl
	 */
	@Override
	public void addTableModelListener(TableModelListener tl) {
		listenners.add(tl);
	}

	/**
	 *
	 * @param tl
	 */
	@Override
	public void removeTableModelListener(TableModelListener tl) {
		listenners.remove(tl);
	}

	/**
	 *
	 */
	public void tableDidChange() {
		for (TableModelListener tl : listenners) {
			tl.tableChanged(new TableModelEvent(this));
		}
	}

	/**
	 *
	 * @param o
	 */
	public void tableDidChange(Object o) {
		int i;
		if ((i = ROWS.indexOf(o)) != -1) {
			if (itensPorPagina != 0) {
				i -= (pagina - 1) * itensPorPagina;
			}
			for (TableModelListener tl : listenners) {
				tl.tableChanged(new TableModelEvent(this, i));
			}
		}
	}

}
