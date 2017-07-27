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
public class ThreadList {

	private boolean running = false;

	private class ThreadListItem implements Runnable {

		private Runnable toRun;

		public ThreadListItem(Runnable runnable) {
			toRun = runnable;
		}

		@Override
		public void run() {
			toRun.run();
			nextThread();
		}

	}

	private final ArrayList<ThreadListListenner> listenners = new ArrayList<>();
	private final ArrayList<Thread> list = new ArrayList<>();
	private int threadRunning = 0, threadTotal = 0;
	private boolean stop = false;

	public void addListenner(ThreadListListenner listenner) {
		listenners.add(listenner);
		dispatchListenner();
	}

	public void addThread(Runnable runnable) {
		threadTotal++;
		running = true;
		list.add(new Thread(new ThreadListItem(runnable)));
		checkThread();
	}

	public void stop() {
		stop = true;
		clear();
	}

	public void join() {
		try {
			while (running && !stop) {
				Thread.sleep(100);
			}
		} catch (InterruptedException ex) {
		}
	}

	public void clear() {
		threadRunning = 0;
		list.clear();
		dispatchListenner();
	}

	public boolean isRunning() {
		return running;
	}

	private void nextThread() {
		threadRunning++;
		checkThread();
	}

	private void dispatchListenner() {
		for (ThreadListListenner evt : listenners) {
			evt.changeState(threadRunning, list.size(), threadTotal);
		}
	}

	private void checkThread() {
		if (stop) {
			return;
		}
		if (list.size() > threadRunning) {
			if (list.get(threadRunning) != null && !list.get(threadRunning).isAlive()) {
				try {
					list.get(threadRunning).start();
				} catch (Exception e) {
				}
			}
			dispatchListenner();
		} else {
			running = false;
			clear();
		}
	}
}
