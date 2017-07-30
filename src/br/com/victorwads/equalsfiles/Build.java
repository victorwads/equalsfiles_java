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

	public static final String BUILD_NUMBER = "131";
	public static final String NAME = "Equals Files";
	public static final String VERSION = "2.5";

	public static final int getBuildNumber() {
		try {
			return Integer.parseInt(BUILD_NUMBER);
		} catch (Exception e) {
		}
		return 0;
	}

	public static final String getName() {
		return NAME;
	}

	public static final String getVersion() {
		return VERSION;
	}
}
