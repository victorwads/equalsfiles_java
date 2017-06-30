/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.victorwads.equalsfiles.controller;

// <editor-fold defaultstate="collapsed" desc="Imports">
import static br.com.victorwads.equalsfiles.dao.Conexao.BD_FILE;
import br.com.victorwads.equalsfiles.dao.Index;
import br.com.victorwads.equalsfiles.dao.IndexHistorico;
import br.com.victorwads.equalsfiles.model.ArquivoHistorico;
import br.com.victorwads.equalsfiles.model.ArquivoHistorico.Tipo;
import br.com.victorwads.equalsfiles.model.Diretorio;
import com.sun.nio.file.ExtendedWatchEventModifier;
import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_DELETE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY;
import static java.nio.file.StandardWatchEventKinds.OVERFLOW;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Date;
// </editor-fold>

/**
 *
 * @author victo
 */
public class Indexacao implements Runnable, Duracao.Listenner, ThreadListListenner {

	// <editor-fold defaultstate="collapsed" desc="Classes">
	private static class QueueAddIndex implements Runnable {

		private static int queue = 0, insert = 0;
		private static final ArrayList<Object> toInsert = new ArrayList<>();
		private final Object arquivo;

		public QueueAddIndex(ArquivoHistorico arquivo) {
			this.arquivo = arquivo;
			queue++;
		}

		@Override
		public void run() {
			toInsert.add(arquivo);
			insert++;
			if (insert == queue || insert > 5000) {
				queue = 0;
				insert = 0;
				ArquivoHistorico[] all = toInsert.toArray(new ArquivoHistorico[toInsert.size()]);
				new Index().inserir(all);
				new IndexHistorico().inserir(all);
				toInsert.clear();
			}
		}

	}

	private static class QueueUpdateIndex implements Runnable {

		private static int queue = 0, insert = 0;
		private static final ArrayList<Object> toInsert = new ArrayList<>();
		private final Object arquivo;

		public QueueUpdateIndex(ArquivoHistorico arquivo) {
			this.arquivo = arquivo;
			queue++;
		}

		@Override
		public void run() {
			toInsert.add(arquivo);
			insert++;
			if (insert == queue || insert > 5000) {
				queue = 0;
				insert = 0;
				ArquivoHistorico[] all = toInsert.toArray(new ArquivoHistorico[toInsert.size()]);
				new Index().atualizar(all);
				new IndexHistorico().inserir(all);
				toInsert.clear();
			}
		}

	}
	// </editor-fold>

	public static void addListenner(ThreadListListenner listenner) {
		threads.addListenner(listenner);
	}

	private final boolean ignoreFirstSearch;
	private final static ThreadList threads = new ThreadList();
	private final String FullFileName;
	private IndexacaoInterface view;
	private ArquivoHistorico[] bd;
	private Diretorio[] diretorios;
	private String state = "";
	private boolean stop = false;

	public Indexacao(IndexacaoInterface view, Diretorio[] diretorios) {
		this(view, diretorios, false);
	}

	public Indexacao(IndexacaoInterface view, Diretorio[] diretorios, boolean ignoreFirstSearch) {
		// <editor-fold defaultstate="collapsed" desc="No View">
		if (view == null) {
			view = new IndexacaoInterface() {
				@Override
				public void clear() {
				}

				@Override
				public void setLoadingTotal(int i) {
				}

				@Override
				public void loading(int i) {

				}

				@Override
				public void loading(String path, int i) {

				}

				@Override
				public void loading(String info, boolean infinita) {
				}

				@Override
				public void setDuration(int segundos) {
				}

				@Override
				public void finish() {
				}
			};
		}
		// </editor-fold>
		this.view = view;
		this.diretorios = diretorios;
		String f = null;
		try {
			f = new File(BD_FILE).getCanonicalPath();
		} catch (IOException ex) {
		}
		FullFileName = f;
		this.ignoreFirstSearch = ignoreFirstSearch;
	}

	@Override
	public void run() {
		if (!ignoreFirstSearch) {
			Duracao duracao = new Duracao(this);
			duracao.start();
			//Carregar BD
			view.loading(state = "Carregando banco de dados", true);
			bd = new Index().listar(diretorios);

			//Comparações
			long modificado;
			view.setLoadingTotal(bd.length);
			view.loading(state = "Verificando Mudanças", 0);
			//int i = 0;
			for (int i = 0; i < bd.length; i++) {
				if (stop) {
					break;
				}
				ArquivoHistorico a = bd[i];
				if (a.getTipo() == Tipo.EXCLUIDO) {
					continue;
				}
				File f = a.getFile();
				if (!f.exists()) {
					a.setTipo(Tipo.EXCLUIDO);
					a.setModificado(new Date().getTime());
					threads.addThread(new QueueUpdateIndex(a));
				} else if (a.getModificado() != (modificado = loadArquivoModificade(a))) {
					a.setTipo(Tipo.ALTERADO);
					a.setModificado(modificado);
					threads.addThread(new QueueUpdateIndex(a));
				}
				view.loading(i++);
			}

			//Carregar roots
			for (Diretorio diretorio : diretorios) {
				if (stop) {
					break;
				}
				view.loading(state = "Verificando Mudanças em " + diretorio, true);
				carregarPasta(diretorio, new Index().getRootId(diretorio.toString(), true));
			}

			view.loading(state = "Salvando", true);
			threads.addListenner(this);
			threads.join();
			duracao.stop();
		}

		view.loading(state = "Indexando em Background", false);
		Thread[] watchThreads = new Thread[diretorios.length];
		int i = 0;
		for (Diretorio diretorio : diretorios) {
			watchThreads[i++] = watch(new File(diretorio.toString()).toPath());
		}

		//CleanMemory
		bd = null;
		diretorios = null;
		System.gc();

		for (Thread t : watchThreads) {
			while (t.isAlive()) {
				try {
					Thread.sleep(100);
				} catch (Exception e) {
				}
				if (stop) {
					t.stop();
				}
			}
		}

		view.loading(state = "Finalizado", false);
		view.finish();
	}

	// <editor-fold defaultstate="collapsed" desc="Methodos">
	public void setView(IndexacaoInterface view) {
		if (view == null) {
			return;
		}
		view.loading(state, false);
		this.view = view;
	}

	public void verificaArquivo(int idRoot, Diretorio diretorio, String filename) {
		ArquivoHistorico a = null;
		for (int i = 0; i < bd.length && a == null; i++) {
			if (bd[i].nome.equals(filename) && bd[i].diretorio.equals(diretorio)) {
				a = bd[i];
				break;
			}
		}

		if (a == null) {
			a = new ArquivoHistorico(diretorio, filename);
			a.setIdRoot(idRoot);
			a.setModificado(loadArquivoModificade(a));
			a.setTipo(Tipo.NOVO);
			threads.addThread(new QueueAddIndex(a));
		} else if (a.getTipo() == Tipo.EXCLUIDO) {
			a.setTipo(Tipo.NOVO);
			threads.addThread(new QueueUpdateIndex(a));
		}
	}

	public long loadArquivoModificade(ArquivoHistorico a) {
		try {
			BasicFileAttributes attr = Files.readAttributes(a.getFile().toPath(), BasicFileAttributes.class);
			return attr.lastModifiedTime().toMillis();
		} catch (Exception e) {
			return 0;
		}
	}

	private void carregarPasta(Diretorio diretorio, int idRoot) {
		if (stop) {
			return;
		}
		try {
			File scan = new File(diretorio.toString());
			File[] listagem = scan.listFiles();
			for (File arquivo : listagem) {
				if (stop) {
					break;
				}
				if (arquivo.isDirectory()) {
					carregarPasta(new Diretorio(arquivo.getAbsolutePath()), idRoot);
				} else if (arquivo.isFile()) {
					verificaArquivo(idRoot, diretorio, arquivo.getName());
				}
			}
		} catch (Exception e) {
		}
	}

	@Override
	public void changeState(int threadRunning, int threadQueueTotal, int threadTotal) {
		view.setLoadingTotal(threadQueueTotal);
		view.loading(threadRunning);
	}

	@Override
	public void update(int duracao) {
		view.setDuration(duracao);
	}

	public void stop() {
		stop = true;
		threads.stop();
	}

	public static ArquivoHistorico[] listarHistorico(Date min, Date max, Tipo tipo, String pesquisa) {
		return new IndexHistorico().listar(min, max, tipo, pesquisa);
	}

	// </editor-fold>
	// <editor-fold defaultstate="collapsed" desc="Watch">
	public void verificaWatchArquivo(int idRoot, Diretorio diretorio, String filename, WatchEvent.Kind kind) {
		ArquivoHistorico a = new ArquivoHistorico(diretorio, filename);
		a.setIdRoot(idRoot);
		a.setModificado(new Date().getTime());
		new Index().getOrInsertByPath(a);

		if (kind == ENTRY_CREATE) {
			a.setTipo(Tipo.NOVO);
		} else if (kind == ENTRY_DELETE) {
			a.setTipo(Tipo.EXCLUIDO);
		} else if (kind == ENTRY_MODIFY) {
			a.setTipo(Tipo.ALTERADO);
		}
		//System.out.println(kind + " ID: " + a.getId() + " " + a.getTipo() + " " + a);
		threads.addThread(new QueueUpdateIndex(a));
	}

	public Thread watch(Path path) {
		int idRoot = new Index().getRootId(path.toString(), true);
		Thread t = new Thread(() -> {
			WatchService watchService;
			WatchKey key;
			try {
				watchService = FileSystems.getDefault().newWatchService();
				path.register(watchService, new WatchEvent.Kind<?>[]{ENTRY_MODIFY, ENTRY_CREATE, ENTRY_DELETE}, ExtendedWatchEventModifier.FILE_TREE);
			} catch (Exception e) {
				e.printStackTrace();
				return;
			}
			while (true) {
				if (stop) {
					break;
				}
				try {
					key = watchService.take();
				} catch (Exception e) {
					return;
				}

				for (WatchEvent<?> event : key.pollEvents()) {
					if (stop) {
						break;
					}
					if (event.kind() == OVERFLOW) {
						continue;
					}
					try {
						Path name = (Path) event.context();
						File filename = path.resolve(name).toFile();
						if ((filename.getAbsolutePath()).indexOf(FullFileName) == 0) {
							continue;
						}
						//System.err.println(path.toString() + " " + event.kind() + " " + filename.getAbsolutePath());
						if (!filename.isDirectory()) {
							verificaWatchArquivo(idRoot, new Diretorio(filename.getParent()), filename.getName(), event.kind());
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				if (!key.reset()) {
					break;
				}
			}
		});
		t.start();
		return t;
	}
	// </editor-fold>
}
