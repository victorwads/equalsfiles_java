/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.victorwads.equalsfiles.model;

// <editor-fold defaultstate="collapsed" desc="Imports">
import java.io.File;
import java.util.Arrays;
import java.util.Objects;
// </editor-fold>

/**
 *
 * @author victo
 */
public class Diretorio {

	private final String patch;

	public Diretorio(String patch) {
		this.patch = patch + (patch.charAt(patch.length() - 1) == File.separatorChar ? "" : File.separator);
	}

	@Override
	public String toString() {
		return this.patch;
	}

	@Override
	public int hashCode() {
		int hash = 3;
		hash = 43 * hash + Objects.hashCode(this.patch);
		return hash;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null || getClass() != obj.getClass()) {
			return false;
		}
		final Diretorio other = (Diretorio) obj;
		return Objects.equals(this.patch, other.patch);
	}

	public static String toString(Diretorio[] d) {
		return Arrays.toString(d).replaceAll("[\\[\\]]", "");
	}

}
