/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.victorwads.equalsfiles.controller;

// <editor-fold defaultstate="collapsed" desc="Imports">
import br.com.victorwads.equalsfiles.model.Arquivo;
import br.com.victorwads.equalsfiles.model.Diretorio;
import br.com.victorwads.equalsfiles.model.Resulted;

import java.io.File;
import java.util.ArrayList;
// </editor-fold>

/**
 *
 * @author victo
 */
public class Processa implements Runnable, Duracao.Listenner {

	private final Duracao duracao;
	private final ProcessaInterface view;
	private final Resulted resulted;
	private ArrayList<Arquivo> arquivos;
	private int duplicatas = 0;
	private long totalDuplicatas = 0;
	private boolean stop = false;

	public Processa(ProcessaInterface view, Resulted resulted) {
		this.view = view;
		this.resulted = resulted;

		duracao = new Duracao(this);
		duracao.start();
	}

	// <editor-fold defaultstate="collapsed" desc="Methodos">
	public void stop() {
		stop = true;
	}

	@Override
	public void run() {
		arquivos = new ArrayList<>();

		view.clear();

		for (Diretorio diretorio : resulted.getDiretorios()) {
			view.loading("Carregando diretório " + diretorio, true);
			carregarPasta(diretorio);
		}

		view.setFilesAmount(arquivos.size());
		view.setLoadingTotal(arquivos.size());
		view.loading("Carregando Tamanhos", 0);
		long totalArquivosTamanho = 0;
		for (int i = 0; i < arquivos.size() && !stop; i++) {
			totalArquivosTamanho += getArquivoSize(arquivos.get(i));
			view.loading(i);
		}
		view.setFilesSize(totalArquivosTamanho);

		view.setLoadingTotal(arquivos.size());
		for (int c = 0; c < arquivos.size() && !stop; c++) {
			Arquivo arquivo = arquivos.get(c);
			if (arquivo.getFlag() || arquivo.getSize() == 0) {
				continue;
			}
			view.loading(arquivo.getFullName(), c);
			for (int i = c + 1; i < arquivos.size() && !stop; i++) {
				compare(arquivo, arquivos.get(i));
			}
		}

		duracao.stop();
		resulted.setDuracao(duracao.getDuracao());
		resulted.setQuantidadeArquivos(arquivos.size());
		resulted.setTamanhoArquivos(totalArquivosTamanho);

		view.loading("Finalizado", arquivos.size());
		view.finish();
	}

	private void carregarPasta(Diretorio diretorio) {
		if (stop) {
			return;
		}
		try {
			File scan = new File(diretorio.toString());
			File[] listagem = scan.listFiles();
			for (File arquivo : listagem) {
				if (arquivo.isDirectory()) {
					carregarPasta(new Diretorio(arquivo.getAbsolutePath()));
				} else if (arquivo.isFile()) {
					arquivos.add(new Arquivo(diretorio, arquivo.getName()));
				}
			}
		} catch (Exception e) {
		}
	}

	private void addArquivo(Arquivo a) {
		if (a.getFlag()) {
			return;
		}
		resulted.addFile(a);
		a.setFlag();
	}

	private void compare(Arquivo a1, Arquivo a2) {
		//if(checkNomes.isEnabled())
		if ((!a1.getFlag()) && (!resulted.isNomeSensitive() || a1.nome.equals(a2.nome)) && (a1.getSize() == a2.getSize()) && getAquivoMD5(a1).equals(getAquivoMD5(a2))) {
			addArquivo(a1);
			addArquivo(a2);
			duplicatas++;
			totalDuplicatas += a1.getSize();
			view.setDuplicates(totalDuplicatas, duplicatas);
		}
	}

	private String getAquivoMD5(Arquivo a) {
		if (a.getMD5() == null) {
			a.setMD5(CacheMD5.registerMD5(a.getFullName()));
		}
		return a.getMD5();
	}

	private long getArquivoSize(Arquivo a) {
		try {
			a.setSize(new File(a.getFullName()).length());
		} catch (Exception e) {
		}
		return a.getSize();
	}

	@Override
	public void update(int duracao) {
		view.setDuration(duracao);
	}
	// </editor-fold>

}
