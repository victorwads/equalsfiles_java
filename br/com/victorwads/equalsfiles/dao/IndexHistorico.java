/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.victorwads.equalsfiles.dao;

import br.com.victorwads.equalsfiles.model.ArquivoHistorico;
import br.com.victorwads.equalsfiles.model.Diretorio;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;

/**
 *
 * @author victo
 */
public class IndexHistorico extends Conexao {

	public boolean inserir(ArquivoHistorico a) {
		return inserir(new ArquivoHistorico[]{a});
	}

	public boolean inserir(ArquivoHistorico[] arquivos) {
		try {
			con.createStatement().executeUpdate("BEGIN TRANSACTION");
			PreparedStatement ps;
			int i;
			for (ArquivoHistorico a : arquivos) {
				try {
					i = 0;
					ps = con.prepareStatement("INSERT INTO IndexacaoHistorico (idCaminho, type, modificado) VALUES (?,?,?)");
					ps.setInt(++i, a.getId());
					ps.setInt(++i, a.getTipo().ordinal());
					ps.setDate(++i, new java.sql.Date(a.getModificado()));
					ps.execute();
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

	public ArquivoHistorico[] listar(Date min, Date max, ArquivoHistorico.Tipo tipo, String pesquisa) {
		ArquivoHistorico[] arquivos = null;
		try {
			PreparedStatement ps;
			String query = "SELECT i.diretorio, i.nome, h.type, h.modificado, i.id FROM IndexacaoHistorico h LEFT JOIN Indexacao i ON h.idCaminho = i.id";
			String order = "ORDER BY h.modificado DESC";
			String where = "";
			int i = 0;
			if (tipo != null) {
				where += " and h.type = ?";
			}
			if (pesquisa != null) {
				where += " and (i.diretorio || i.nome LIKE ?)";
			}
			if (min == null && max == null) {
				ps = con.prepareStatement(query + " WHERE (1=1)" + where + order);
			} else if (min != null && max != null) {
				ps = con.prepareStatement(query + " WHERE h.modificado BETWEEN ? and ?" + where + order);
				ps.setDate(++i, toDate(min));
				ps.setDate(++i, toDate(max));
			} else if (max != null) {
				ps = con.prepareStatement(query + " WHERE h.modificado <= ?" + where + order);
				ps.setDate(++i, toDate(max));
			} else {
				ps = con.prepareStatement(query + " WHERE h.modificado >= ?" + where + order);
				ps.setDate(++i, toDate(min));
			}
			if (tipo != null) {
				ps.setInt(++i, tipo.ordinal());
			}
			if (pesquisa != null) {
				ps.setString(++i, "%" + pesquisa + "%");
			}
			ResultSet rs = ps.executeQuery();
			ArrayList<ArquivoHistorico> all = new ArrayList<>();
			while (rs.next()) {
				ArquivoHistorico a = new ArquivoHistorico(new Diretorio(rs.getString(1)), rs.getString(2));
				a.setTipo(ArquivoHistorico.Tipo.values()[rs.getInt(3)]);
				a.setModificado(rs.getDate(4).getTime());
				a.setId(rs.getInt(5));
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
}
