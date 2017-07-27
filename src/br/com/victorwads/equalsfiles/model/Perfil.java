/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.victorwads.equalsfiles.model;

/**
 *
 * @author victo
 */
public class Perfil {

	private int id;
	private String nome;
	private Diretorio[] diretorios;

	public Perfil() {
		this.diretorios = new Diretorio[0];
	}

	public Perfil(String nome, Diretorio[] diretorios) {
		this.nome = nome;
		this.diretorios = diretorios;
	}

	public Perfil(int id, String nome) {
		this.id = id;
		this.nome = nome;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public Diretorio[] getDiretorios() {
		return diretorios;
	}

	public void setDiretorios(Diretorio[] diretorios) {
		this.diretorios = diretorios;
	}
}
