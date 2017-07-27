/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.victorwads.equalsfiles.model;

import java.io.File;
import javax.swing.ImageIcon;

/**
 *
 * @author victo
 */
public class Arquivo {

	public final Diretorio diretorio;
	public final String nome, extencao;
	private String md5;
	private ImageIcon icone;
	private long size = 0;
	private boolean flag = false, toExclude = false;

	public Arquivo(Diretorio diretorio, String nome) {
		this.diretorio = diretorio;
		this.nome = nome;

		int p = this.nome.lastIndexOf(46);
		this.extencao = p == -1 ? "" : this.nome.substring(p + 1).toLowerCase();
	}

	public String getMD5() {
		return md5;
	}

	public void setMD5(String md5) {
		this.md5 = md5;
	}

	public boolean getFlag() {
		return flag;
	}

	public void setFlag() {
		this.flag = true;
	}

	public long getSize() {
		return size;
	}

	public void setSize(long size) {
		this.size = size;
	}

	public String getFullName() {
		return this.diretorio + this.nome;
	}

	public ImageIcon getIcone() {
		return icone;
	}

	public void setIcone(ImageIcon icone) {
		this.icone = icone;
	}

	public void setToExclude(boolean toExclude) {
		this.toExclude = toExclude;
	}

	public boolean isToExclude() {
		return toExclude;
	}

	public File getFile() {
		return new File(getFullName());
	}

	@Override
	public String toString() {
		return getFullName();
	}

}
