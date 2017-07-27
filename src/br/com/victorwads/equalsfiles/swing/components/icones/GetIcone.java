/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.victorwads.equalsfiles.swing.components.icones;

import br.com.victorwads.equalsfiles.model.Arquivo;
import java.awt.Image;
import java.awt.Window;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.AbstractButton;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.filechooser.FileSystemView;

/**
 *
 * @author victo
 */
public class GetIcone {

	private static class Icone {

		private final String name;
		private ImageIcon icon = null;

		public Icone(String name) {
			this.name = name;
		}

		public ImageIcon getIcon() {
			if (icon == null) {
				icon = new ImageIcon(Icone.class.getResource(name + ".png"));
			}
			return icon;
		}

		@Override
		public String toString() {
			return name;
		}

	}

	public static final Icone ADICONAR = new Icone("adiconar"),
			ANTERIOR = new Icone("anterior"),
			APARENCIA = new Icone("aparencia"),
			ATALHOS = new Icone("atalhos"),
			CACHE = new Icone("cache"),
			COMPARAR = new Icone("comparar"),
			CONFIG = new Icone("config"),
			EDITAR = new Icone("editar"),
			EXPORTAR = new Icone("exportar"),
			HISTORICO = new Icone("historico"),
			ICONE = new Icone("Icone"),
			IMPORTAR = new Icone("importar"),
			FILE = new Icone("file"),
			INDEXACAO = new Icone("indexacao"),
			LIMPAR = new Icone("limpar"),
			MAIN = new Icone("main"),
			PERFILS = new Icone("perfils"),
			PARAR = new Icone("parar"),
			PLAY = new Icone("play"),
			PROXIMA = new Icone("proxima"),
			PRIMEIRA = new Icone("primeira"),
			RELOAD = new Icone("reload"),
			RESULTADOS = new Icone("resultados"),
			SAIR = new Icone("sair"),
			SALVAR = new Icone("salvar"),
			SOBRE = new Icone("sobre"),
			TRAY = new Icone("tray"),
			ULTIMA = new Icone("ultima"),
			VIEWER = new Icone("viewer");

	private static final HashMap<String, ImageIcon> ICON_EXTENISON_CACHE = new HashMap<>();
	private static final FileSystemView fileSystemView = FileSystemView.getFileSystemView();

	public static ImageIcon getIconByFile(File file) {
		return (ImageIcon) fileSystemView.getSystemIcon(file);
	}

	public static ImageIcon getIconByFile(String filename) {
		return (ImageIcon) fileSystemView.getSystemIcon(new File(filename));
	}

	private static ImageIcon getIconByExtension(String ext) {
		ImageIcon icon = ICON_EXTENISON_CACHE.get(ext);
		if (icon == null) {
			try {
				File f = File.createTempFile("icon", "." + ext);
				icon = getIconByFile(f.getAbsolutePath());
				f.delete();
			} catch (Exception e) {
				icon = getImageIcone(FILE);
			}
			ICON_EXTENISON_CACHE.put(ext, icon);
		}
		return icon;
	}

	public static ImageIcon getImageIcone(Icone icon) {
		return icon.getIcon();
	}

	public static ImageIcon getArquivoIcone(Arquivo arquivo) {
		ImageIcon icon = arquivo.getIcone();
		if (icon == null) {
			//icon = getIconByFile(arquivo.getFullName());
			if (icon == null) {
				icon = getIconByExtension(arquivo.extencao);
			}
			arquivo.setIcone(icon);
		}
		return icon;
	}

	/**
	 *
	 * @param withIcon
	 * @param icon
	 */
	public static void setResImage(JLabel withIcon, Icone icon) {
		withIcon.setIcon(getImageIcone(icon));
	}

	/**
	 *
	 * @param withIcon
	 * @param icon
	 */
	public static void setResImage(AbstractButton withIcon, Icone icon) {
		withIcon.setIcon(getImageIcone(icon));
	}

	/**
	 *
	 * @param withIcon
	 * @param icon
	 */
	public static void setResImage(Window withIcon, Icone icon) {
		ArrayList<Image> icons = new ArrayList<>();
		icons.add(getImageIcone(icon).getImage());
		((Window) withIcon).setIconImages(icons);
	}

}
