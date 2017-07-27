/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.victorwads.equalsfiles.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author victo
 */
public class Resulted extends HashMap<String, ArrayList<Arquivo>> {

	private Diretorio[] diretorios;
	private boolean nomeSensitive = false;
	private Date data;
	private long tamanhoArquivos;
	private int id, quantidadeArquivos, duracao;

	public Resulted() {
		this.data = new Date();
	}

	public Resulted(Diretorio[] diretorios, boolean nomeSensitive) {
		this();
		this.diretorios = diretorios;
		this.nomeSensitive = nomeSensitive;
	}

	public Resulted(boolean nomeSensitive, int quantidadeArquivos, long tamanhoArquivos, Date data, int duracao, int id) {
		this.nomeSensitive = nomeSensitive;
		this.quantidadeArquivos = quantidadeArquivos;
		this.tamanhoArquivos = tamanhoArquivos;
		this.data = data;
		this.duracao = duracao;
		this.id = id;
	}

	public String[] getSortedHashs() {
		Resulted disordenedTemp = (Resulted) this.clone();
		Resulted ordenedTemp = new Resulted();
		String[] order = new String[size()];

		int count = 0;
		while (!disordenedTemp.isEmpty()) {
			long max = 0, temp;
			String key = null;
			for (Map.Entry<String, ArrayList<Arquivo>> entry : disordenedTemp.entrySet()) {
				temp = entry.getValue().get(0).getSize();
				if (temp >= max) {
					max = temp;
					key = entry.getKey();
				}
			}
			if (key != null) {
				order[count] = key;
				ordenedTemp.put(key, disordenedTemp.remove(key));
			}
			count++;
		}
		return order;
	}

	public void addFile(Arquivo a) {
		ArrayList<Arquivo> p = this.get(a.getMD5());
		if (p == null) {
			p = new ArrayList<>();
			this.put(a.getMD5(), p);
		}
		p.add(a);
	}

	public void removeFile(Arquivo a) {
		ArrayList<Arquivo> p = this.get(a.getMD5());
		if (p == null) {
			return;
		}
		p.remove(a);
		if (p.size() <= 1) {
			this.remove(a.getMD5());
		}
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public boolean isNomeSensitive() {
		return nomeSensitive;
	}

	public void setNomeSensitive(boolean verificaNomes) {
		this.nomeSensitive = verificaNomes;
	}

	public int getQuantidadeArquivos() {
		return quantidadeArquivos;
	}

	public void setQuantidadeArquivos(int quantidadeArquivos) {
		this.quantidadeArquivos = quantidadeArquivos;
	}

	public int getDuracao() {
		return duracao;
	}

	public void setDuracao(int duracao) {
		this.duracao = duracao;
	}

	public long getTamanhoArquivos() {
		return tamanhoArquivos;
	}

	public void setTamanhoArquivos(long tamanhoArquivos) {
		this.tamanhoArquivos = tamanhoArquivos;
	}

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public Diretorio[] getDiretorios() {
		return diretorios;
	}

	public void setDiretorios(Diretorio[] diretorios) {
		this.diretorios = diretorios;
	}

}
