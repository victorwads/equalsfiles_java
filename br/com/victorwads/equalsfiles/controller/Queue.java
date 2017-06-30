/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.victorwads.equalsfiles.controller;

import java.util.ArrayList;

/**
 *
 * @author victo
 */
public abstract class Queue implements Runnable {

	private static int total = 0, done = 0;
	private final ArrayList<Object> itens;
	private final Object object;

	public Queue(Object object, ArrayList<Object> itens) {
		this.object = object;
		this.itens = itens;
		total++;
	}

	abstract void action();

	/**
	 *
	 */
	@Override
	final public void run() {
		itens.add(object);
		done++;
		if (done == total) {
			total = 0;
			done = 0;
			action();
			itens.clear();
		}
	}

	public int getTotal() {
		return itens.size();
	}

}
