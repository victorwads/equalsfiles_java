/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.victorwads.equalsfiles.dao;

// <editor-fold defaultstate="collapsed" desc="Imports">
import br.com.victorwads.equalsfiles.model.ArquivoHistorico;
import br.com.victorwads.equalsfiles.model.Diretorio;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
// </editor-fold>

/**
 *
 * @author victo
 */
public class Index extends Conexao {

	private final static HashMap<String, Integer> ROOTS = new HashMap<>();

	public boolean inserir(ArquivoHistorico a) {
		return inserir(new ArquivoHistorico[]{a});
	}

	public boolean inserir(ArquivoHistorico[] arquivos) {
		String query = "INSERT INTO Indexacao (idRoot, diretorio, nome, type, modificado) VALUES (?,?,?,?,?)";
		try {
			con.createStatement().execute("BEGIN TRANSACTION");
			PreparedStatement ps;
			int i;
			for (ArquivoHistorico a : arquivos) {
				try {
					ps = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
					i = 0;
					ps.setInt(++i, a.getIdRoot());
					ps.setString(++i, a.diretorio.toString());
					ps.setString(++i, a.nome);
					ps.setInt(++i, a.getTipo().ordinal());
					ps.setDate(++i, new Date(a.getModificado()));
					ps.execute();

					ResultSet rs = ps.getGeneratedKeys();
					if (rs.next()) {
						a.setId(rs.getInt(1));
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			return con.createStatement().executeUpdate("COMMIT") > 0;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			close();
		}
	}

	public boolean atualizar(ArquivoHistorico[] arquivos) {
		try {
			con.createStatement().execute("BEGIN TRANSACTION");
			for (ArquivoHistorico a : arquivos) {
				PreparedStatement ps = con.prepareStatement("UPDATE Indexacao SET modificado = ?, type = ? WHERE id = ? ");
				int i = 0;
				ps.setDate(++i, new Date(a.getModificado()));
				ps.setInt(++i, a.getTipo().ordinal());
				ps.setInt(++i, a.getId());
				ps.execute();
			}
			return con.createStatement().executeUpdate("COMMIT") > 0;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			close();
		}
	}

	public boolean getOrInsertByPath(ArquivoHistorico a) {
		String query = "SELECT id FROM Indexacao WHERE diretorio = ? and nome = ?";
		setAutoClose(false);
		try {
			PreparedStatement ps = con.prepareStatement(query);
			ps.setString(1, a.diretorio.toString());
			ps.setString(2, a.nome);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				a.setId(rs.getInt(1));
			} else {
				return inserir(a);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			setAutoClose(true);
			close();
		}

		return true;
	}

	public ArquivoHistorico[] listar(Diretorio[] roots) {
		ArquivoHistorico[] arquivos = null;
		try {
			PreparedStatement ps = con.prepareStatement("SELECT id, idRoot, diretorio, nome, modificado FROM Indexacao WHERE idRoot in " + makeValues(roots.length, 1));
			for (int i = 0; i < roots.length; i++) {
				ps.setInt(i + 1, getRootId(roots[i].toString(), false, true));
			}
			ResultSet rs = ps.executeQuery();
			ArrayList<ArquivoHistorico> all = new ArrayList<>();
			while (rs.next()) {
				ArquivoHistorico a = new ArquivoHistorico(new Diretorio(rs.getString(3)), rs.getString(4));
				a.setId(rs.getInt(1));
				a.setIdRoot(rs.getInt(2));
				a.setModificado(rs.getDate(5).getTime());
				all.add(a);
			}
			arquivos = all.toArray(new ArquivoHistorico[all.size()]);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close();
		}
		return arquivos;
	}

	public int getRootId(String path, boolean createIfNotExists) {
		return getRootId(path, true, createIfNotExists);
	}

	private int getRootId(String path, boolean close, boolean createIfNotExists) {
		Integer id = ROOTS.get(path);
		if (id != null) {
			return id;
		}
		try {
			PreparedStatement ps = con.prepareStatement("SELECT id FROM IndexacaoRoots WHERE patch = ?");
			ps.setString(1, path);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				id = rs.getInt(1);
				ROOTS.put(path, id);
				return id;
			} else if (createIfNotExists) {
				return insertRoot(path);
			} else {
				return 0;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		} finally {
			if (close) {
				close();
			}
		}
	}

	private int insertRoot(String path) {
		try {
			PreparedStatement ps = con.prepareStatement("INSERT INTO IndexacaoRoots (patch) VALUES (?)", Statement.RETURN_GENERATED_KEYS);
			ps.setString(1, path);
			ps.executeUpdate();
			ResultSet rs = ps.getGeneratedKeys();
			rs.next();
			return rs.getInt(1);
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}
}
