/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.victorwads.equalsfiles.model;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author victo
 */
public class DateTime {

	private final static SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
	private final Date data;

	public DateTime(long data) {
		this.data = new Date(data);
	}

	public DateTime(Date data) {
		this.data = data;
	}

	public DateTime() {
		this.data = new Date();
	}

	public static String getDurationStringFromSeconds(int segundos) {
		return String.format("%02d:%02d:%02d", segundos / 3600, (segundos % 3600) / 60, segundos % 60);
	}

	@Override
	public String toString() {
		return format.format(data);
	}

}
