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
 * @param <T> Class of the object data that will be treated.
 * @author Victor Wads - victor.laureano@gmail.com
 */
public abstract class MyTableModel<T> implements TableModel {

	/**
	 * Class to be the object to store de data transaction on page change that needs new data.
	 * @param <T> Class of this data.
	 */
	@SuppressWarnings("WeakerAccess")
	private static class RequestBundle<T> {
		public Integer realTotal;
		public T[] dataRows;
	}

	/**
	 * Interface to watch page changes that needs new data.
	 * @param <T>
	 */
	public interface RequestDataListener<T> {

		void onRequestData(int offset, int maxItems, int page, RequestBundle<T> bundle);
	}

	private final ArrayList<TableModelListener> listenners = new ArrayList<>();
	private RequestDataListener<T> requestDataListener = null;
	private Integer itemsPerPage = null, page = 1;
	private Integer realTotal = null;
	@SuppressWarnings("WeakerAccess")
	protected final String[] COLUNAS;
	@SuppressWarnings("WeakerAccess")
	protected final ArrayList<T> ROWS = new ArrayList<>();

	/**
	 * @param columns name of all header columns on the table.
	 */
	public MyTableModel(String[] columns) {
		COLUNAS = columns;
	}

	/**
	 * make the table refresh the graphics.
	 */
	private void tableDidChange() {
		for (TableModelListener tl : listenners) {
			tl.tableChanged(new TableModelEvent(this));
		}
	}

	/**
	 * @param o make the table refresh the graphics of the row of the object passed by param. If this object dos'ent exists will do nothing.
	 */
	public void tableDidChange(T o) {
		int i = ROWS.indexOf(o);
		if (i != -1) {
			if (itemsPerPage != 0) {
				i -= (page - 1) * itemsPerPage;
			}
			for (TableModelListener tl : listenners) {
				tl.tableChanged(new TableModelEvent(this, i));
			}
		}
	}

	/**
	 * This action will make the table ask for a new row's data every page changes, this way java don't need to store much data on primary memory.
	 *
	 * @return listener that will watch the table model needs load data of a new page. If is not settled will be null.
	 */
	public RequestDataListener<T> getRequestDataListener() {
		return requestDataListener;
	}

	/**
	 * @param requestDataListener listener that will watch the table model needs load data of a new page.
	 */
	public void setRequestDataListener(RequestDataListener<T> requestDataListener) {
		this.requestDataListener = requestDataListener;
	}

	/**
	 * @return true if the table model will ask for new page data on page changes. otherwise it will be false.
	 */
	@SuppressWarnings("WeakerAccess")
	public boolean isAutoLoadEnabled() {
		return requestDataListener != null;
	}

	/**
	 *
	 */
	public final void clear() {
		ROWS.clear();
		tableDidChange();
	}

	/**
	 * append all objects in array pass by parameter.
	 *
	 * @param objects array os objects to be inserted on tablemodel.
	 */
	public final void addAll(T[] objects) {
		ROWS.addAll(Arrays.asList(objects));
		tableDidChange();
	}


	/**
	 * @param r object to be inserted
	 */
	public final void addRow(T r) {
		ROWS.add(r);
		tableDidChange();
	}

	/**
	 * @param index index of the row that will be removed.
	 * @return remove the row on the position pass by parameter.
	 */
	public final T removeRow(int index) {
		T o = ROWS.remove(index);
		if (o != null) {
			tableDidChange();
		}
		return o;
	}

	/**
	 * @param index index of the needed row
	 * @return the object in the index of current page.
	 */
	public final T getRow(int index) {
		return ROWS.get(index);
	}

	/**
	 * @param table table that will gets this model care.
	 */
	public void setTableModel(JTable table) {
		table.setModel(this);
		verifyPage();
		tableDidChange();
	}

	private void verifyPage() {
		if (page > getPagesAmount()) {
			page = getPagesAmount();
		}
		if (page < 1) {
			page = 1;
		}
	}

	/**
	 * @return number of items per page.
	 */
	public int getItemsPerPage() {
		return itemsPerPage;
	}

	/**
	 * @param itemsPerPage number of the items per page.
	 */
	public void setItemsPerPage(Integer itemsPerPage) {
		this.itemsPerPage = itemsPerPage < 1 ? 0 : itemsPerPage;
		verifyPage();
		tableDidChange();
	}

	/**
	 * @return total of rows in current page;
	 */
	public int getTotal() {
		return ROWS.size();
	}

	/**
	 * @return total of pages.
	 */
	public int getPagesAmount() {
		if (ROWS.isEmpty() || itemsPerPage == null) {
			return 1;
		}
		int total = isAutoLoadEnabled()? realTotal : ROWS.size();
		return (int) Math.ceil(total / itemsPerPage);
	}

	/**
	 * @return current page number.
	 */
	public int getPage() {
		return page;
	}

	/**
	 * Changes current page to the number page pass by parameter.
	 *
	 * @param page the new page number.
	 */
	public void setPage(int page) {
		if (this.page == page) return;
		if (requestDataListener != null) {
			//Request page data
			int offset = ((page - 1) * itemsPerPage);
			RequestBundle<T> bundle = new RequestBundle<>();
			requestDataListener.onRequestData(offset, itemsPerPage, page, bundle);
			//Set bundle data
			if(bundle.dataRows == null || bundle.realTotal == null){
				throw new MyTableModelException(bundle.getClass().getName()+" has no data or realTotal passed in onRequestData() transaction.");
			}
			realTotal = bundle.realTotal;
			clear();
			addAll(bundle.dataRows);
		}
		this.page = page;
		verifyPage();
		tableDidChange();
	}

	/**
	 * Change to the next page.
	 */
	public void nextPage() {
		setPage(page + 1);
	}

	/**
	 * Change to de previous page.
	 */
	public void previousPage() {
		setPage(page - 1);
	}

	/**
	 * @param rowIndex    index of the row's column.
	 * @param columnIndex index of the need column.
	 * @return is the needed column is editable - aways false;
	 */
	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return false;
	}

	/**
	 * @return amount of columns.
	 */
	@Override
	public int getColumnCount() {
		return COLUNAS.length;
	}

	/**
	 * @param i index of the needed column name.
	 * @return name of this column.
	 */
	@Override
	public String getColumnName(int i) {
		return COLUNAS[i];
	}

	/**
	 * @param columnIndex index of the needed column class.
	 * @return class to this column data.
	 */
	@Override
	public Class getColumnClass(int columnIndex) {
		return String.class;
	}

	/**
	 * @return number of rows in current page.
	 */
	@Override
	public int getRowCount() {
		if (itemsPerPage == null || itemsPerPage > ROWS.size()) {
			return ROWS.size();
		} else {
			return itemsPerPage;
		}
	}

	/**
	 * '
	 *
	 * @param rowIndex index of the needed row.
	 * @return row object on the current page position pass by param.
	 */
	@SuppressWarnings("WeakerAccess")
	protected final T getRowAt(int rowIndex) {
		if (itemsPerPage != null) {
			rowIndex = ((page - 1) * itemsPerPage) + rowIndex;
		}
		return ROWS.get(rowIndex);
	}

	/**
	 * @param o           don't use.
	 * @param rowIndex    don't use.
	 * @param columnIndex don't use.
	 */
	@Override
	public void setValueAt(Object o, int rowIndex, int columnIndex) {
		throw new UnsupportedOperationException("Not supported.");
	}

	/**
	 * @param tl Listener to see the model data changes.
	 */
	@Override
	public final void addTableModelListener(TableModelListener tl) {
		listenners.add(tl);
	}

	/**
	 * @param tl Listener to see the model data changes.
	 */
	@Override
	public final void removeTableModelListener(TableModelListener tl) {
		listenners.remove(tl);
	}

}
