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
public interface ThreadListListenner {
    public void changeState(int threadRunning, int threadQueueTotal, int threadTotal);
}
