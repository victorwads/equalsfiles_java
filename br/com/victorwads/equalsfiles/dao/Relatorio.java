/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.victorwads.equalsfiles.dao;

import br.com.victorwads.equalsfiles.model.Arquivo;
import br.com.victorwads.equalsfiles.model.Diretorio;
import br.com.victorwads.equalsfiles.model.Resultado;
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

	public boolean registrar(Resultado resultado) {
		try {
			//Salva relatorio
			PreparedStatement ps = con.prepareStatement("INSERT INTO Relatorios (nomeSensitive, arquivos, arquivosTamanho, duracao, data) values (?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
			ps.setBoolean(1, resultado.isNomeSensitive());
			ps.setInt(2, resultado.getQuantidadeArquivos());
			ps.setLong(3, resultado.getTamanhoArquivos());
			ps.setInt(4, resultado.getDuracao());
			ps.setDate(5, toDate(resultado.getData()));
			int result = ps.executeUpdate();

			//Pega id da inserção
			ResultSet rs = ps.getGeneratedKeys();
			rs.next();
			resultado.setId(rs.getInt(1));

			//Salva Diretorios
			int i = 0;
			ps = con.prepareStatement("INSERT INTO ResultadosDiretorios (idResultado, caminho) VALUES " + makeValues(2, resultado.getDiretorios().length));
			for (Diretorio d : resultado.getDiretorios()) {
				ps.setInt(++i, resultado.getId());
				ps.setString(++i, d.toString());
			}
			result = result > 0 ? ps.executeUpdate() : 0;

			//Salvar Aquivos
			i = 0;
			for (Map.Entry<String, ArrayList<Arquivo>> entry : resultado.entrySet()) {
				i += entry.getValue().size();
			}
			ps = con.prepareStatement("INSERT INTO ResultadosArquivos (idResultado, diretorio, nome, tamanho, md5) VALUES " + makeValues(5, i));
			i = 0;
			for (Map.Entry<String, ArrayList<Arquivo>> entry : resultado.entrySet()) {
				for (Arquivo a : entry.getValue()) {
					ps.setInt(++i, resultado.getId());
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

	public boolean excluir(Resultado resultado) {
		if (resultado.getId() == 0) {
			return false;
		}
		try {
			PreparedStatement ps = con.prepareStatement("DELETE FROM Relatorios WHERE id = ?");
			ps.setInt(1, resultado.getId());
			return ps.executeUpdate() > 0;
		} catch (Exception e) {
			return false;
		} finally {
			close();
		}
	}

	public Resultado[] listar(Date min, Date max) {
		ArrayList<Resultado> resultados = new ArrayList<>();
		Resultado[] rt = null;
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
				resultados.add(new Resultado(
						rs.getBoolean(2),
						rs.getInt(3),
						rs.getLong(4),
						toDate(rs.getDate(6)),
						rs.getInt(5),
						rs.getInt(1)
				));
			}
			rt = new Resultado[resultados.size()];
			int i = 0;
			for (Resultado r : resultados) {
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

	private boolean carregarDiretorios(Resultado resultado) {
		if (resultado.getId() == 0) {
			return false;
		}
		try {
			PreparedStatement ps = con.prepareStatement("SELECT caminho FROM ResultadosDiretorios WHERE idResultado = ?");
			ps.setInt(1, resultado.getId());
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
			resultado.setDiretorios(ds);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean carregarArquivos(Resultado resultado) {
		if (resultado.getId() == 0) {
			return false;
		}
		try {
			PreparedStatement ps = con.prepareStatement("SELECT diretorio, nome, tamanho, md5 FROM ResultadosArquivos WHERE idResultado = ?");
			ps.setInt(1, resultado.getId());
			ResultSet rs = ps.executeQuery();
			resultado.clear();
			Arquivo a;
			while (rs.next()) {
				a = new Arquivo(new Diretorio(rs.getString(1)), rs.getString(2));
				a.setSize(rs.getLong(3));
				a.setMD5(rs.getString(4));
				a.setFlag();
				resultado.addFile(a);
			}
			return true;
		} catch (Exception e) {
			return false;
		} finally {
			close();
		}
	}
}
