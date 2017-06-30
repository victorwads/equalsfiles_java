/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.victorwads.equalsfiles.model;

import java.util.HashMap;

/**
 *
 * @author victo
 */
public class MD5Cache extends HashMap<String, String> {

	public static class Join {

		public final String md5, caminho;

		public Join(String md5, String caminho) {
			this.md5 = md5;
			this.caminho = caminho;
		}

	}

}
