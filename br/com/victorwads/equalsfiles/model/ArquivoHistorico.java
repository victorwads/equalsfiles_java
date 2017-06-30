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
public class ArquivoHistorico extends Arquivo {

	public enum Tipo {
		NOVO("Novo"), EXCLUIDO("Excluído"), ALTERADO("Alterado"), NAO_ALTERADO("");
		private String nome;

		private Tipo(String nome) {
			this.nome = nome;
		}

		public String getNome() {
			return nome;
		}

		/**
		 *
		 * @return
		 */
		@Override
		public String toString() {
			return "";
		}

	};

	private int id, idRoot;
	private long modificado;
	private Tipo tipo = Tipo.NAO_ALTERADO;

	public ArquivoHistorico(Diretorio diretorio, String nome) {
		super(diretorio, nome);
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getIdRoot() {
		return idRoot;
	}

	public void setIdRoot(int idRoot) {
		this.idRoot = idRoot;
	}

	public long getModificado() {
		return modificado;
	}

	public void setModificado(long modificado) {
		this.modificado = modificado;
	}

	public Tipo getTipo() {
		return tipo;
	}

	public void setTipo(Tipo tipo) {
		this.tipo = tipo;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		} else if (o == null || getClass() != o.getClass()) {
			return false;
		}
		final ArquivoHistorico other = (ArquivoHistorico) o;
		return getFullName().equals(other.getFullName()) && getTipo() == other.getTipo();
	}

}
