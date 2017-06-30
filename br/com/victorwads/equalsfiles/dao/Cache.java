/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.victorwads.equalsfiles.dao;

import br.com.victorwads.equalsfiles.model.MD5Cache;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 *
 * @author victo
 */
public class Cache extends Conexao {

	public boolean registrar(MD5Cache.Join cache) {
		return registrar(new MD5Cache.Join[]{cache});
	}

	public boolean registrar(MD5Cache.Join[] cache) {
		try {
			PreparedStatement ps = con.prepareStatement("INSERT INTO CacheMD5 (caminho, md5) values " + makeValues(2, cache.length));
			int i = 0;
			for (MD5Cache.Join c : cache) {
				ps.setString(++i, c.caminho);
				ps.setString(++i, c.md5);
			}
			return ps.executeUpdate() > 0;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			close();
		}
	}

	public boolean excluir(String caminho) {
		return excluir(new String[]{caminho});
	}

	public boolean excluir(String[] caminho) {
		try {
			PreparedStatement ps = con.prepareStatement("DELETE FROM CacheMD5 WHERE caminho IN " + makeValues(caminho.length, 1));
			for (int i = 0; i < caminho.length; i++) {
				ps.setString(i + 1, caminho[i]);
			}
			return ps.executeUpdate() > 0;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			close();
		}
	}

	public br.com.victorwads.equalsfiles.model.MD5Cache listar() {
		br.com.victorwads.equalsfiles.model.MD5Cache result = new br.com.victorwads.equalsfiles.model.MD5Cache();
		try {
			PreparedStatement ps = con.prepareStatement("SELECT * FROM CacheMD5");
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				result.put(rs.getString("caminho"), rs.getString("md5"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close();
		}
		return result;
	}

	public void limpar() {
		try {
			con.prepareStatement("DELETE FROM CacheMD5").executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close();
		}
	}
}
