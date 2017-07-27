/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.victorwads.equalsfiles.controller;

import br.com.victorwads.equalsfiles.dao.Relatorio;
import br.com.victorwads.equalsfiles.model.Resulted;

import java.util.Date;

/**
 *
 * @author victo
 */
public class Resultados {

	public static boolean inserir(Resulted resulted) {
		return new Relatorio().registrar(resulted);
	}

	public static Resulted[] listar(Date min, Date max) {
		return new Relatorio().listar(min, max);
	}

	public static void carregarArquivos(Resulted resulted) {
		new Relatorio().carregarArquivos(resulted);
	}

	public static boolean excluir(Resulted resulted) {
		return new Relatorio().excluir(resulted);
	}
}
