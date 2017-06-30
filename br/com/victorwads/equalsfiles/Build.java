/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.victorwads.equalsfiles;

import java.util.ResourceBundle;

/**
 *
 * @author victo
 */
public final class Build {

	private static final String getProperty(String prop) {
		try {
			ResourceBundle rb = ResourceBundle.getBundle(Build.class.getCanonicalName());
			return rb.getString(prop);
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	public static final int getBuildNumber() {
		try {
			return Integer.parseInt(getProperty("BUILD"));
		} catch (Exception e) {
		}
		return 0;
	}

	public static final String getName() {
		return getProperty("NAME");
	}

	public static final String getVersion() {
		return getProperty("VERSION");
	}
}
