/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.victorwads.equalsfiles.controller;

import br.com.victorwads.equalsfiles.model.Perfil;

/**
 *
 * @author victo
 */
public class Perfils {

	public static Perfil[] listar() {
		return new br.com.victorwads.equalsfiles.dao.Perfils().listar();
	}

	public static boolean inserir(Perfil p) {
		return new br.com.victorwads.equalsfiles.dao.Perfils().inserir(p);
	}

	public static boolean excluir(Perfil p) {
		return new br.com.victorwads.equalsfiles.dao.Perfils().excluir(p);
	}

	public static boolean salvar(Perfil p) {
		return new br.com.victorwads.equalsfiles.dao.Perfils().salvar(p);
	}

}
