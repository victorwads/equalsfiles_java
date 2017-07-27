/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.victorwads.equalsfiles.controller;

/**
 *
 * @author victo
 */
public interface IndexacaoInterface {

	/**
	 *
	 */
	public void clear();

	/**
	 *
	 * @param i
	 */
	public void setLoadingTotal(int i);

	/**
	 *
	 * @param i
	 */
	public void loading(int i);

	/**
	 *
	 * @param path
	 * @param i
	 */
	public void loading(String path, int i);

	/**
	 *
	 * @param info
	 * @param infinita
	 */
	public void loading(String info, boolean infinita);

	/**
	 *
	 * @param segundos
	 */
	public void setDuration(int segundos);

	/**
	 *
	 */
	public void finish();

}
