/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.victorwads.equalsfiles.controller;

// <editor-fold defaultstate="collapsed" desc="Imports">
import br.com.victorwads.equalsfiles.dao.Cache;
import br.com.victorwads.equalsfiles.model.MD5Cache;
import java.io.File;
import java.io.FileInputStream;
import java.security.MessageDigest;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Map;
// </editor-fold>

/**
 *
 * @author victo
 */
public class CacheMD5 {

	// <editor-fold defaultstate="collapsed" desc="Classes">
	private static class QueueDelSql extends Queue {

		private static final ArrayList<Object> itens = new ArrayList<>();

		public QueueDelSql(String caminho) {
			super(caminho, itens);
		}

		@Override
		public void action() {
			new Cache().excluir(itens.toArray(new String[itens.size()]));
		}

	}

	private static class QueueAddSql extends Queue {

		private static final ArrayList<Object> itens = new ArrayList<>();

		public QueueAddSql(MD5Cache.Join cache) {
			super(cache, itens);
		}

		@Override
		void action() {
			new Cache().registrar(itens.toArray(new MD5Cache.Join[itens.size()]));
		}

	}
	// </editor-fold>

	private static MD5Cache MD5BD = null;
	private static final ThreadList BD_TRHEADS = new ThreadList();

	private static String convertByteArrayToHexString(byte[] arrayBytes) {
		StringBuilder stringBuffer = new StringBuilder();
		for (int i = 0; i < arrayBytes.length; i++) {
			stringBuffer.append(Integer.toString((arrayBytes[i] & 0xff) + 0x100, 16)
					.substring(1));
		}
		return stringBuffer.toString();
	}

	private static String hashFile(String file, String algorithm) {
		try {
			FileInputStream inputStream = new FileInputStream(file);
			MessageDigest digest = MessageDigest.getInstance(algorithm);

			byte[] bytesBuffer = new byte[1024];
			int bytesRead;

			while ((bytesRead = inputStream.read(bytesBuffer)) != -1) {
				digest.update(bytesBuffer, 0, bytesRead);
			}
			inputStream.close();
			byte[] hashedBytes = digest.digest();

			return convertByteArrayToHexString(hashedBytes);
		} catch (Exception e) {
		}
		return null;
	}

	private static void validaBD() {
		new Thread(() -> {
			ArrayList<String> toDelete = new ArrayList<>();
			for (Map.Entry<String, String> entry : MD5BD.entrySet()) {
				boolean exists = false;
				try {
					exists = new File(entry.getKey()).exists();
				} catch (Exception e) {
				}
				if (!exists) {
					toDelete.add(entry.getKey());
				}
			}
			for (String caminho : toDelete) {
				BD_TRHEADS.addThread(new QueueDelSql(caminho));
				MD5BD.remove(caminho);
			}
		}).start();
	}

	private static void carregaBancoMD5() {
		MD5BD = new Cache().listar();
		validaBD();
	}

	public static void setListenner(ThreadListListenner listenner) {
		BD_TRHEADS.addListenner(listenner);
	}

	public static void addCache(MD5Cache.Join cache) {
		if (MD5BD.get(cache.caminho) == null) {
			MD5BD.put(cache.caminho, cache.md5);
			BD_TRHEADS.addThread(new QueueAddSql(cache));
		}
	}

	public static void limpaMD5() {
		new Cache().limpar();
		BD_TRHEADS.clear();
		MD5BD.clear();
	}

	public static String registerMD5(String arquivo) {
		if (MD5BD == null) {
			carregaBancoMD5();
		}

		String md5 = MD5BD.get(arquivo);
		if (md5 == null) {
			md5 = hashFile(arquivo, "MD5");
			if (md5 == null) {
				md5 = "";
			} else {
				addCache(new MD5Cache.Join(md5, arquivo));
			}
		}
		return md5;
	}

	public static String humamSize(long size) {
		if (size == 0) {
			return "0.00 B";
		}

		String[] s = {"B", "KB", "MB", "GB", "TB", "PB"};
		int e = (int) Math.floor(Math.log(size) / Math.log(1024));
		float f = (float) size / (float) Math.pow(1024, (double) e);

		return new DecimalFormat("#.##").format(f) + " " + s[e];
	}

	public static MD5Cache listar() {
		return MD5BD;
	}

}
