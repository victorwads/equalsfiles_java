/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.victorwads.equalsfiles.dao;

// <editor-fold defaultstate="collapsed" desc="Imports">
import br.com.victorwads.equalsfiles.Main;
import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
// </editor-fold>

/**
 *
 * @author victo
 */
public class Conexao {

	protected Connection con;
	private boolean autoClose = true;
	final public static String BD_FILE = Main.USERHOME + File.separator + "EqualsFiles.cache";
	final private static String TABLE_CACHE_MD5 = "CREATE TABLE CacheMD5 (caminho VARCHAR PRIMARY KEY NOT NULL,md5 CHAR (32) NOT NULL)";
	final private static String TABLE_INDEXACAO_ROOTS = "CREATE TABLE IndexacaoRoots (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, patch VARCHAR NOT NULL UNIQUE)";
	final private static String TABLE_INDEXACAO = "CREATE TABLE Indexacao (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, idRoot INTEGER REFERENCES IndexacaoRoots (id)  NOT NULL, diretorio VARCHAR NOT NULL, nome VARCHAR NOT NULL, type TINYINT NOT NULL, modificado DATETIME NOT NULL, UNIQUE(diretorio, nome))";
	final private static String TABLE_INDEXACAO_HISTORICO = "CREATE TABLE IndexacaoHistorico (idCaminho INTEGER  NOT NULL REFERENCES Indexacao (id), type TINYINT NOT NULL, modificado DATETIME NOT NULL)";
	final private static String TABLE_RELATORIO = "CREATE TABLE Relatorios (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,nomeSensitive BOOLEAN NOT NULL,arquivos INT NOT NULL,arquivosTamanho BIGINT NOT NULL,duracao INT NOT NULL,data DATETIME NOT NULL)";
	final private static String TABLE_RESULTADO_DIRETORIOS = "CREATE TABLE ResultadosDiretorios (idResultado INTEGER CONSTRAINT resultadoDiretorio REFERENCES Relatorios (id) ON DELETE CASCADE ON UPDATE CASCADE NOT NULL, caminho VARCHAR NOT NULL)";
	final private static String TABLE_RESULTADO_ARQUIVOS = "CREATE TABLE ResultadosArquivos (idResultado INTEGER CONSTRAINT resultadoArquivo REFERENCES Relatorios (id) ON DELETE CASCADE ON UPDATE CASCADE NOT NULL, diretorio VARCHAR NOT NULL, nome VARCHAR NOT NULL, tamanho BIGINTd NOT NULL, md5 CHAR (32) NOT NULL)";
	final private static String TABLE_PERFILS = "CREATE TABLE Perfils (id INTEGER PRIMARY KEY NOT NULL,nome VARCHAR (50) NOT NULL)";
	final private static String TABLE_PERFILS_DIRETORIOS = "CREATE TABLE PerfilsDiretorios (idPerfil INTEGER CONSTRAINT resultadoArquivo REFERENCES Perfils (id) ON DELETE CASCADE ON UPDATE CASCADE NOT NULL,caminho VARCHAR NOT NULL)";
	private static boolean innited = false;

	public Conexao() {
		try {
			final String URL = "jdbc:sqlite:" + BD_FILE;
			con = DriverManager.getConnection(URL, "teste", "teste");
			PreparedStatement ps = con.prepareStatement("PRAGMA foreign_keys = ON");
			ps.execute();
		} catch (Exception e) {
			e.printStackTrace();
		}
		Init();
	}

	public void setAutoClose(boolean autoClose) {
		this.autoClose = autoClose;
	}

	public void closeConnection() {
		try {
			con.close();
		} catch (Exception e) {
		}
	}

	protected void close() {
		if (autoClose) {
			closeConnection();
		}
	}

	protected java.sql.Date toDate(java.util.Date data) {
		return new java.sql.Date(data.getTime());
	}

	protected java.util.Date toDate(java.sql.Date data) {
		return new java.util.Date(data.getTime());
	}

	protected String makeValues(int collumns, int rows) {
		String all = "";
		String modelToRepeat = "";
		for (int i = 0; i < collumns; i++) {
			modelToRepeat += ",?";
		}
		modelToRepeat = ",(" + modelToRepeat.substring(1) + ")";
		for (int i = 0; i < rows; i++) {
			all += modelToRepeat;
		}
		return all.substring(1);
	}

	private void Init() {
		if (innited) {
			return;
		}
		innited = true;
		tryTable("CacheMD5", "caminho, md5", TABLE_CACHE_MD5);
		tryTable("Relatorios", "id, nomeSensitive, arquivos, arquivosTamanho, duracao, data", TABLE_RELATORIO);
		tryTable("ResultadosDiretorios", "idResultado, caminho", TABLE_RESULTADO_DIRETORIOS);
		tryTable("ResultadosArquivos", "idResultado, diretorio, nome, tamanho, md5", TABLE_RESULTADO_ARQUIVOS);
		tryTable("Perfils", "id, nome", TABLE_PERFILS);
		tryTable("PerfilsDiretorios", "idPerfil, caminho", TABLE_PERFILS_DIRETORIOS);
		tryTable("IndexacaoRoots", "id, patch", TABLE_INDEXACAO_ROOTS);
		tryTable("Indexacao", "id, idRoot, diretorio, nome, type, modificado", TABLE_INDEXACAO);
		tryTable("IndexacaoHistorico", "idCaminho, type, modificado", TABLE_INDEXACAO_HISTORICO);
	}

	private void tryTable(String table, String fields, String DDL) {
		try {
			PreparedStatement ps = con.prepareStatement("SELECT " + fields + " FROM " + table + " LIMIT 1");
			ps.execute();
			ps.close();
		} catch (Exception e) {
			execute("DROP TABLE IF EXISTS " + table);
			execute(DDL);
		}
	}

	private boolean execute(String sql) {
		try {
			PreparedStatement ps = con.prepareStatement(sql);
			ps.execute();
			ps.close();
			return true;
		} catch (Exception e) {
			return false;
		}
	}

}
