/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.victorwads.equalsfiles.controller;

import java.util.Timer;
import java.util.TimerTask;

/**
 *
 * @author victo
 */
public class Duracao extends TimerTask {

	public interface Listenner {

		public void update(int duracao);
	}

	private final Timer interval;
	private final Listenner view;
	private final long start;
	private int duracao;

	public Duracao(Listenner view) {
		this.view = view;
		start = System.currentTimeMillis();
		interval = new Timer();
	}

	@Override
	public void run() {
		duracao = (int) ((System.currentTimeMillis() - start) / 1000);
		view.update(duracao);
	}

	public void start() {
		interval.schedule(this, 0l, 1000l);
	}

	public void stop() {
		interval.cancel();
	}

	public int getDuracao() {
		return duracao;
	}
}
