/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.victorwads.equalsfiles.dao;

import br.com.victorwads.equalsfiles.model.Arquivo;
import br.com.victorwads.equalsfiles.model.Diretorio;
import br.com.victorwads.equalsfiles.model.Resulted;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

/**
 *
 * @author victo
 */
public class Relatorio extends Conexao {

	public boolean registrar(Resulted resulted) {
		try {
			//Salva relatorio
			PreparedStatement ps = con.prepareStatement("INSERT INTO Relatorios (nomeSensitive, arquivos, arquivosTamanho, duracao, data) values (?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
			ps.setBoolean(1, resulted.isNomeSensitive());
			ps.setInt(2, resulted.getQuantidadeArquivos());
			ps.setLong(3, resulted.getTamanhoArquivos());
			ps.setInt(4, resulted.getDuracao());
			ps.setDate(5, toDate(resulted.getData()));
			int result = ps.executeUpdate();

			//Pega id da inserção
			ResultSet rs = ps.getGeneratedKeys();
			rs.next();
			resulted.setId(rs.getInt(1));

			//Salva Diretorios
			int i = 0;
			ps = con.prepareStatement("INSERT INTO ResultadosDiretorios (idResultado, caminho) VALUES " + makeValues(2, resulted.getDiretorios().length));
			for (Diretorio d : resulted.getDiretorios()) {
				ps.setInt(++i, resulted.getId());
				ps.setString(++i, d.toString());
			}
			result = result > 0 ? ps.executeUpdate() : 0;

			//Salvar Aquivos
			i = 0;
			for (Map.Entry<String, ArrayList<Arquivo>> entry : resulted.entrySet()) {
				i += entry.getValue().size();
			}
			ps = con.prepareStatement("INSERT INTO ResultadosArquivos (idResultado, diretorio, nome, tamanho, md5) VALUES " + makeValues(5, i));
			i = 0;
			for (Map.Entry<String, ArrayList<Arquivo>> entry : resulted.entrySet()) {
				for (Arquivo a : entry.getValue()) {
					ps.setInt(++i, resulted.getId());
					ps.setString(++i, a.diretorio.toString());
					ps.setString(++i, a.nome);
					ps.setLong(++i, a.getSize());
					ps.setString(++i, a.getMD5());
				}
			}
			result = result > 0 ? ps.executeUpdate() : 0;

			return result > 0;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			close();
		}
	}

	public boolean excluir(Resulted resulted) {
		if (resulted.getId() == 0) {
			return false;
		}
		try {
			PreparedStatement ps = con.prepareStatement("DELETE FROM Relatorios WHERE id = ?");
			ps.setInt(1, resulted.getId());
			return ps.executeUpdate() > 0;
		} catch (Exception e) {
			return false;
		} finally {
			close();
		}
	}

	public Resulted[] listar(Date min, Date max) {
		ArrayList<Resulted> resulteds = new ArrayList<>();
		Resulted[] rt = null;
		try {
			String query = "SELECT id, nomeSensitive, arquivos, arquivosTamanho, duracao, data FROM Relatorios";
			PreparedStatement ps;
			if (min == null && max == null) {
				ps = con.prepareStatement(query);
			} else if (min != null && max != null) {
				ps = con.prepareStatement(query + " WHERE data BETWEEN ? and ?");
				ps.setDate(1, toDate(min));
				ps.setDate(2, toDate(max));
			} else if (max != null) {
				ps = con.prepareStatement(query + " WHERE data <= ?");
				ps.setDate(1, toDate(max));
			} else {
				ps = con.prepareStatement(query + " WHERE data >= ?");
				ps.setDate(1, toDate(min));
			}
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				resulteds.add(new Resulted(
						rs.getBoolean(2),
						rs.getInt(3),
						rs.getLong(4),
						toDate(rs.getDate(6)),
						rs.getInt(5),
						rs.getInt(1)
				));
			}
			rt = new Resulted[resulteds.size()];
			int i = 0;
			for (Resulted r : resulteds) {
				carregarDiretorios(r);
				rt[i] = r;
				i++;
			}
		} catch (Exception e) {
		} finally {
			close();
		}
		return rt;
	}

	private boolean carregarDiretorios(Resulted resulted) {
		if (resulted.getId() == 0) {
			return false;
		}
		try {
			PreparedStatement ps = con.prepareStatement("SELECT caminho FROM ResultadosDiretorios WHERE idResultado = ?");
			ps.setInt(1, resulted.getId());
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
			resulted.setDiretorios(ds);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean carregarArquivos(Resulted resulted) {
		if (resulted.getId() == 0) {
			return false;
		}
		try {
			PreparedStatement ps = con.prepareStatement("SELECT diretorio, nome, tamanho, md5 FROM ResultadosArquivos WHERE idResultado = ?");
			ps.setInt(1, resulted.getId());
			ResultSet rs = ps.executeQuery();
			resulted.clear();
			Arquivo a;
			while (rs.next()) {
				a = new Arquivo(new Diretorio(rs.getString(1)), rs.getString(2));
				a.setSize(rs.getLong(3));
				a.setMD5(rs.getString(4));
				a.setFlag();
				resulted.addFile(a);
			}
			return true;
		} catch (Exception e) {
			return false;
		} finally {
			close();
		}
	}
}
