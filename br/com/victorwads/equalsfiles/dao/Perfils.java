/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.victorwads.equalsfiles.dao;

import br.com.victorwads.equalsfiles.model.Diretorio;
import br.com.victorwads.equalsfiles.model.Perfil;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

/**
 *
 * @author victo
 */
public class Perfils extends Conexao {

	public boolean inserir(Perfil p) {
		try {
			boolean rb;
			PreparedStatement ps = con.prepareStatement("INSERT INTO Perfils (nome) VALUES (?)", Statement.RETURN_GENERATED_KEYS);
			ps.setString(1, p.getNome());
			rb = ps.executeUpdate() > 0;
			ResultSet rs = ps.getGeneratedKeys();
			rs.next();
			p.setId(rs.getInt(1));
			return rb && inserirDiretorios(p);
		} catch (Exception e) {
			return false;
		} finally {
			close();
		}
	}

	public boolean salvar(Perfil p) {
		if (p.getId() == 0) {
			return false;
		}
		try {
			PreparedStatement ps = con.prepareStatement("UPDATE Perfils SET nome = ? WHERE id = ?");
			ps.setString(2, p.getNome());
			ps.setInt(2, p.getId());
			excluirDiretorios(p);
			inserirDiretorios(p);
			return ps.executeUpdate() > 0;
		} catch (Exception e) {
			return false;
		} finally {
			close();
		}
	}

	public boolean excluir(Perfil p) {
		try {
			PreparedStatement ps = con.prepareStatement("DELETE FROM Perfils WHERE id = ?");
			ps.setInt(1, p.getId());
			return ps.executeUpdate() > 0;
		} catch (Exception e) {
			return false;
		} finally {
			close();
		}
	}

	public Perfil[] listar() {
		ArrayList<Perfil> perfis = new ArrayList<>();
		Perfil[] rt = null;
		try {
			PreparedStatement ps = con.prepareStatement("SELECT id, nome FROM Perfils");
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				perfis.add(new Perfil(rs.getInt(1), rs.getString(2)));
			}
			rt = new Perfil[perfis.size()];
			int i = 0;
			for (Perfil r : perfis) {
				carregarDiretorios(r);
				rt[i] = r;
				i++;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close();
		}
		return rt;
	}

	private boolean excluirDiretorios(Perfil p) {
		if (p.getDiretorios().length == 0) {
			return true;
		}
		try {
			PreparedStatement ps = con.prepareStatement("DELETE FROM PerfilsDiretorios WHERE idPerfil = ?");
			ps.setInt(1, p.getId());
			return ps.executeUpdate() > 0;
		} catch (Exception e) {
			return false;
		}
	}

	private boolean inserirDiretorios(Perfil p) {
		if (p.getDiretorios().length == 0) {
			return true;
		}
		try {
			PreparedStatement ps = con.prepareStatement("INSERT INTO PerfilsDiretorios (idPerfil, caminho) VALUES " + makeValues(2, p.getDiretorios().length));
			int i = 0;
			for (Diretorio d : p.getDiretorios()) {
				ps.setInt(i + 1, p.getId());
				ps.setString(i + 2, d.toString());
				i += 2;
			}
			return ps.executeUpdate() > 0;
		} catch (Exception e) {
			return false;
		}
	}

	private boolean carregarDiretorios(Perfil p) {
		if (p.getId() == 0) {
			return false;
		}
		try {
			PreparedStatement ps = con.prepareStatement("SELECT caminho FROM PerfilsDiretorios WHERE idPerfil = ?");
			ps.setInt(1, p.getId());
			ResultSet rs = ps.executeQuery();
			ArrayList<Diretorio> diretorios = new ArrayList<>();
			while (rs.next()) {
				diretorios.add(new Diretorio(rs.getString(1)));
			}
			Diretorio[] ds = new Diretorio[diretorios.size()];
			int i = 0;
			for (Diretorio d : diretorios) {
				ds[i] = d;
				i++;
			}
			p.setDiretorios(ds);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
}
