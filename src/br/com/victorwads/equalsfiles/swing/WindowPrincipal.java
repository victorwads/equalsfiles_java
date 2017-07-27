/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.victorwads.equalsfiles.swing;

// <editor-fold defaultstate="collapsed" desc="Imports">
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import br.com.victorwads.equalsfiles.Build;
import br.com.victorwads.equalsfiles.Main;
import br.com.victorwads.equalsfiles.controller.CacheMD5;
import br.com.victorwads.equalsfiles.swing.components.Card.CardName;
import br.com.victorwads.equalsfiles.swing.components.Janela;
import br.com.victorwads.equalsfiles.swing.components.icones.GetIcone;
import static br.com.victorwads.equalsfiles.swing.components.icones.GetIcone.setResImage;
import br.com.victorwads.equalsfiles.swing.util.Dialogos;
import java.awt.CardLayout;
import java.io.File;
import java.net.URISyntaxException;
import javax.swing.JPanel;
import javax.swing.UIManager;
import mslinks.ShellLink;
// </editor-fold>

/**
 *
 * @author victo
 */
public final class WindowPrincipal extends Janela {

	private static WindowPrincipal ONE;
	private final CardName CARD_COMPARACAO = new CardName("Comparação de Arquivos");
	private final CardName CARD_PERFILS = new CardName("Perfis de Diretórios");
	private final CardName CARD_RESULTADOS = new CardName("Rsultados de Comparações");
	private final CardName CARD_INDEXACAO = new CardName("Indexação de Arquivos");
	private final CardName CARD_HISTORICO = new CardName("Histórico de Indexação");
	private final String linkPath = Main.USERHOME + "\\AppData\\Roaming\\Microsoft\\Windows\\Start Menu\\Programs\\Startup\\EqualsFiles.lnk";
	private WindowThreadViwer viewer;

	public WindowPrincipal() {
		if (ONE == null) {
			ONE = this;
			initComponents();
			CardPerfil cardPerfil = new CardPerfil();
			CardComparacao cardComparacao = new CardComparacao();
			addPanel(cardComparacao, CARD_COMPARACAO);
			addPanel(cardPerfil, null);
			addPanel(new CardPerfils(cardComparacao, cardPerfil), CARD_PERFILS);
			addPanel(new CardRelatorios(), CARD_RESULTADOS);
			addPanel(new CardIndexacao(), CARD_INDEXACAO);
			addPanel(new CardHistorico(), CARD_HISTORICO);
			showPanel(CARD_COMPARACAO);
			// <editor-fold defaultstate="collapsed" desc="Icones">
			//setResImage(menuArquivo, Icone.Arq);
			setResImage(menuIniciar, GetIcone.COMPARAR);
			setResImage(menuItemPerfils, GetIcone.PERFILS);
			setResImage(menuSair, GetIcone.SAIR);
			//setResImage(menuRelatorios, Icone.RESULTADOS);
			setResImage(menuResultados, GetIcone.RESULTADOS);
			setResImage(menuIndexar, GetIcone.INDEXACAO);
			setResImage(menuHistoricoIndexacao, GetIcone.HISTORICO);
			//setResImage(menuFerramentas, Icone.Fer);
			setResImage(menuAparencia, GetIcone.APARENCIA);
			setResImage(menuCache, GetIcone.CACHE);
			setResImage(menuCacheLimpar, GetIcone.LIMPAR);
			setResImage(menuCacheImport, GetIcone.IMPORTAR);
			setResImage(menuCacheExport, GetIcone.EXPORTAR);
			setResImage(menuItemThread, GetIcone.VIEWER);
			setResImage(menuConfiguracoes, GetIcone.CONFIG);
			//setResImage(menuAjuda, Icone.Fer);
			setResImage(menuAtalhos, GetIcone.ATALHOS);
			setResImage(menuSobre, GetIcone.SOBRE);
			// </editor-fold>
			menuConfigInit.setSelected(new File(linkPath).exists());

			pack();
			setVisible(true);
		} else {
			ONE.setVisible(true);
		}
	}

	// <editor-fold defaultstate="collapsed" desc="Metodos">
	private void addPanel(JPanel panel, CardName name) {
		if (name == null) {
			getContentPane().add(panel);
		} else {
			getContentPane().add(panel, name.NAME);
		}
	}

	private void showPanel(CardName name) {
		((CardLayout) getContentPane().getLayout()).show(getContentPane(), name.NAME);
		setTitle(title + " - " + name.TITLE);
	}

	private void setLookAndFeel(String look) {
		try {
			UIManager.setLookAndFeel(look);
		} catch (Exception e) {
		}
		realodUI();
	}
	// </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
	// Generated using JFormDesigner Evaluation license - Victor Cleber Laureano
	private void initComponents() {
		JMenuBar menuBar = new JMenuBar();
		menuArquivo = new JMenu();
		menuItemPerfils = new JMenuItem();
		menuIniciar = new JMenuItem();
		menuIndexar = new JMenuItem();
		menuSair = new JMenuItem();
		menuRelatorios = new JMenu();
		menuResultados = new JMenuItem();
		menuHistoricoIndexacao = new JMenuItem();
		menuFerramentas = new JMenu();
		menuAparencia = new JMenu();
		JMenuItem menuItemMotif = new JMenuItem();
		JMenuItem menuItemWin = new JMenuItem();
		JMenuItem menuItemWinClassic = new JMenuItem();
		JMenu menuItemSwing = new JMenu();
		menuItemMetal = new JMenuItem();
		menuItemNimbus = new JMenuItem();
		menuCache = new JMenu();
		menuCacheLimpar = new JMenuItem();
		menuCacheImport = new JMenuItem();
		menuCacheExport = new JMenuItem();
		menuItemThread = new JMenuItem();
		menuConfiguracoes = new JMenu();
		menuConfigInit = new JCheckBoxMenuItem();
		JMenu menuAjuda = new JMenu();
		menuAtalhos = new JMenuItem();
		menuSobre = new JMenuItem();

		//======== this ========
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setTitle("Equals Files");
		setMinimumSize(new Dimension(630, 300));
		setSize(new Dimension(502, 221));
		Container contentPane = getContentPane();
		contentPane.setLayout(new CardLayout());

		//======== menuBar ========
		{

			//======== menuArquivo ========
			{
				menuArquivo.setText("Arquivo");

				//---- menuItemPerfils ----
				menuItemPerfils.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P, KeyEvent.ALT_MASK));
				menuItemPerfils.setText("Perfils");
				menuItemPerfils.addActionListener(e -> menuPerfils(e));
				menuArquivo.add(menuItemPerfils);

				//---- menuIniciar ----
				menuIniciar.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, KeyEvent.ALT_MASK));
				menuIniciar.setText("Compara\u00e7\u00e3o");
				menuIniciar.addActionListener(e -> menuIniciar(e));
				menuArquivo.add(menuIniciar);

				//---- menuIndexar ----
				menuIndexar.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_I, KeyEvent.ALT_MASK));
				menuIndexar.setText("Indexa\u00e7\u00e3o");
				menuIndexar.addActionListener(e -> menuIndexarActionPerformed(e));
				menuArquivo.add(menuIndexar);

				//---- menuSair ----
				menuSair.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0));
				menuSair.setText("Sair");
				menuSair.addActionListener(e -> menuSair(e));
				menuArquivo.add(menuSair);
			}
			menuBar.add(menuArquivo);

			//======== menuRelatorios ========
			{
				menuRelatorios.setText("Relat\u00f3rios");

				//---- menuResultados ----
				menuResultados.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, KeyEvent.ALT_MASK));
				menuResultados.setText("Resultados de Compara\u00e7\u00f5es");
				menuResultados.addActionListener(e -> menuResultadosActionPerformed(e));
				menuRelatorios.add(menuResultados);

				//---- menuHistoricoIndexacao ----
				menuHistoricoIndexacao.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_H, KeyEvent.ALT_MASK));
				menuHistoricoIndexacao.setText("Hist\u00f3rico Indexa\u00e7\u00e3o");
				menuHistoricoIndexacao.addActionListener(e -> menuHistoricoIndexacaoActionPerformed(e));
				menuRelatorios.add(menuHistoricoIndexacao);
			}
			menuBar.add(menuRelatorios);

			//======== menuFerramentas ========
			{
				menuFerramentas.setText("Ferramentas");

				//======== menuAparencia ========
				{
					menuAparencia.setText("Aparencia");

					//---- menuItemMotif ----
					menuItemMotif.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_M, KeyEvent.CTRL_MASK|KeyEvent.SHIFT_MASK));
					menuItemMotif.setText("Motif");
					menuItemMotif.addActionListener(e -> menuApMotif(e));
					menuAparencia.add(menuItemMotif);

					//---- menuItemWin ----
					menuItemWin.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_W, KeyEvent.CTRL_MASK|KeyEvent.SHIFT_MASK));
					menuItemWin.setText("Windows");
					menuItemWin.addActionListener(e -> menuApWin(e));
					menuAparencia.add(menuItemWin);

					//---- menuItemWinClassic ----
					menuItemWinClassic.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_W, KeyEvent.CTRL_MASK|KeyEvent.ALT_MASK|KeyEvent.SHIFT_MASK));
					menuItemWinClassic.setText("Windows Classic");
					menuItemWinClassic.addActionListener(e -> menuApWinClassic(e));
					menuAparencia.add(menuItemWinClassic);

					//======== menuItemSwing ========
					{
						menuItemSwing.setText("Swing");

						//---- menuItemMetal ----
						menuItemMetal.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.CTRL_MASK|KeyEvent.SHIFT_MASK));
						menuItemMetal.setText("Metal");
						menuItemMetal.addActionListener(e -> menuApMetal(e));
						menuItemSwing.add(menuItemMetal);

						//---- menuItemNimbus ----
						menuItemNimbus.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, KeyEvent.CTRL_MASK|KeyEvent.SHIFT_MASK));
						menuItemNimbus.setText("Nimbus");
						menuItemNimbus.addActionListener(e -> menuApNimbus(e));
						menuItemSwing.add(menuItemNimbus);
					}
					menuAparencia.add(menuItemSwing);
				}
				menuFerramentas.add(menuAparencia);

				//======== menuCache ========
				{
					menuCache.setText("Cache");

					//---- menuCacheLimpar ----
					menuCacheLimpar.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_T, KeyEvent.ALT_MASK));
					menuCacheLimpar.setText("Limpar");
					menuCacheLimpar.addActionListener(e -> menuLimparCache(e));
					menuCache.add(menuCacheLimpar);

					//---- menuCacheImport ----
					menuCacheImport.setText("Carregar de arquivo");
					menuCacheImport.addActionListener(e -> menuCarregarCache(e));
					menuCache.add(menuCacheImport);

					//---- menuCacheExport ----
					menuCacheExport.setText("Exportar para arquivo");
					menuCacheExport.addActionListener(e -> menuExportarCache(e));
					menuCache.add(menuCacheExport);
				}
				menuFerramentas.add(menuCache);

				//---- menuItemThread ----
				menuItemThread.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, KeyEvent.ALT_MASK));
				menuItemThread.setText("Thred Status");
				menuItemThread.addActionListener(e -> menuThread(e));
				menuFerramentas.add(menuItemThread);

				//======== menuConfiguracoes ========
				{
					menuConfiguracoes.setText("Configura\u00e7\u00f5es");

					//---- menuConfigInit ----
					menuConfigInit.setSelected(true);
					menuConfigInit.setText("Iniciar com o sistema");
					menuConfigInit.addActionListener(e -> menuConfigInitActionPerformed(e));
					menuConfiguracoes.add(menuConfigInit);
				}
				menuFerramentas.add(menuConfiguracoes);
			}
			menuBar.add(menuFerramentas);

			//======== menuAjuda ========
			{
				menuAjuda.setText("Ajuda");

				//---- menuAtalhos ----
				menuAtalhos.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, KeyEvent.ALT_MASK));
				menuAtalhos.setText("Atalhos");
				menuAtalhos.addActionListener(e -> menuAtalhos(e));
				menuAjuda.add(menuAtalhos);

				//---- menuSobre ----
				menuSobre.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.ALT_MASK));
				menuSobre.setText("Sobre");
				menuSobre.addActionListener(e -> menuSobre(e));
				menuAjuda.add(menuSobre);
			}
			menuBar.add(menuAjuda);
		}
		setJMenuBar(menuBar);
		pack();
		setLocationRelativeTo(getOwner());
    }// </editor-fold>//GEN-END:initComponents
    // <editor-fold defaultstate="collapsed" desc="Eventos">
    private void menuCarregarCache(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuCarregarCache
		File a = Dialogos.abrirArquivo(this);
		if (a != null) {
			new WindowFBDLoad(a, this);
		}
    }//GEN-LAST:event_menuCarregarCache

    private void menuLimparCache(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuLimparCache
		if (Dialogos.pergunta("Deseja realmente zerar todo o cache MD5?")) {
			CacheMD5.limpaMD5();
		}
    }//GEN-LAST:event_menuLimparCache

    private void menuExportarCache(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuExportarCache
		File a = Dialogos.salvarArquivo(this);
		if (a != null) {
			new WindowFBDSave(a, this);
		}
    }//GEN-LAST:event_menuExportarCache

    private void menuThread(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuThread
		if (viewer == null) {
			viewer = new WindowThreadViwer();
		}
		viewer.setVisible(true);
    }//GEN-LAST:event_menuThread

    private void menuApMotif(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuApMotif
		setLookAndFeel("com.sun.java.swing.plaf.motif.MotifLookAndFeel");
    }//GEN-LAST:event_menuApMotif

    private void menuApMetal(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuApMetal
		setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
    }//GEN-LAST:event_menuApMetal

    private void menuApNimbus(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuApNimbus
		setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
    }//GEN-LAST:event_menuApNimbus

    private void menuApWin(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuApWin
		setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
    }//GEN-LAST:event_menuApWin

    private void menuApWinClassic(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuApWinClassic
		setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsClassicLookAndFeel");
    }//GEN-LAST:event_menuApWinClassic

    private void menuSair(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuSair
		System.exit(0);
    }//GEN-LAST:event_menuSair

    private void menuIniciar(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuIniciar
		showPanel(CARD_COMPARACAO);
    }//GEN-LAST:event_menuIniciar

    private void menuPerfils(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuPerfils
		showPanel(CARD_PERFILS);
    }//GEN-LAST:event_menuPerfils

    private void menuResultadosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuResultadosActionPerformed
		showPanel(CARD_RESULTADOS);
    }//GEN-LAST:event_menuResultadosActionPerformed

    private void menuHistoricoIndexacaoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuHistoricoIndexacaoActionPerformed
		showPanel(CARD_HISTORICO);
    }//GEN-LAST:event_menuHistoricoIndexacaoActionPerformed

    private void menuSobre(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuSobre
		Dialogos.info("<html>"
				+ "<center><b>" + Build.getName() + "</b></center>"
				+ "<center>2017</center><br />"
				+ "Autor: Victor Cleber Laureano<br />"
				+ "Version: " + Build.getVersion() + "." + Build.getBuildNumber()
				+ "<br /><br />"
				+ "<b>Java Native Access</b> - <a href=https://github.com/java-native-access/jna>github.com/java-native-access/jna</a><br />"
				+ "Esta biblioteca é licenciada sob a LGPL, versão 2.1 or superior<br />"
				+ "<br />"
				+ "<b>ShellLink</b> - <a href=https://github.com/BlackOverlord666/mslinks>github.com/BlackOverlord666/mslinks</a><br />"
				+ "Licenciada sob a WTFPL<br />"
				+ "<br />"
				+ "<b>Equals Files</b> - <a href=https://github.com/victorwads/equalsfiles>github.com/victorwads/equalsfiles</a><br />"
				+ "Licenciada sob a LGPL, versão 3.0 or superior"
				+ "</html>"
		);
    }//GEN-LAST:event_menuSobre

    private void menuAtalhos(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuAtalhos
		Dialogos.info("<html>"
				+ "<b><font color=#333355 size=4>Atalhos Gerais</font></b>"
				+ "<dl color=#555555 size=1>"
				+ "<dd><b>ESC</b> - Fecha uma Janela.</dd>"
				+ "<dd><b>CTRL+ALT+P</b> - Reabre a janela principal.</dd>"
				+ "</dl>"
				+ "<b><font color=#333355 size=2>(Quando disponível)</font></b>"
				+ "<dl color=#555555 size=1>"
				+ "<dd><b>F5</b> - Recarregar conteúdo.</dd>"
				+ "<dd><b>DEL</b> - Remover item.</dd>"
				+ "<dd><b>CTRL+A</b> - Adicionar item.</dd>"
				+ "<dd><b>CTRL+E</b> - Editar item.</dd>"
				+ "<dd><b>CTRL+ENTER</b> - Iniciar.</dd>"
				+ "</dl>"
				+ "<b><font color=#333355 size=4>Menus</font></b>"
				+ "<dl color=#555555 size=1>"
				+ "<dd><b>ALT+V</b> - Abre a ferramente de visualização de Threads.</dd>"
				+ "<dd><b>ALT+C</b> - Abre a área de Comparação.</dd>"
				+ "<dd><b>ALT+P</b> - Abre a área de Perfils.</dd>"
				+ "<dd><b>ALT+R</b> - Abre a área de Relatórios.</dd>"
				+ "<dd><b>ALT+I</b> - Abre a área de Indexação.</dd>"
				+ "<dd><b>ALT+H</b> - Abre a área de Histórico de indexação.</dd>"
				+ "<dd><b>ALT+T</b> - Limpar Cache.</dd>"
				+ "<dd><b>ALT+A</b> - Atalhos.</dd>"
				+ "<dd><b>ALT+S</b> - Sobre.</dd>"
				+ "</dl>"
				+ "<b><font color=#333355 size=4>Mudar Aparencia</font></b>"
				+ "<dl color=#555555 size=1>"
				+ "<dd><b>CTRL+SHIFT+M</b> - Motif</dd>"
				+ "<dd><b>CTRL+SHIFT+W</b> - Windows</dd>"
				+ "<dd><b>CTRL+SHIFT+ALT+W</b> - Windows Classic</dd>"
				+ "<dd><b>CTRL+SHIFT+S</b> - Swing Metal</dd>"
				+ "<dd><b>CTRL+SHIFT+N</b> - Swing Nimbus</dd>"
				+ "</dl>"
				+ "</html>");
    }//GEN-LAST:event_menuAtalhos

    private void menuIndexarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuIndexarActionPerformed
		showPanel(CARD_INDEXACAO);
    }//GEN-LAST:event_menuIndexarActionPerformed

    private void menuConfigInitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuConfigInitActionPerformed
		if (menuConfigInit.isSelected()) {
			//Teste WinStartUp
			String linkTarget;
			try {
				linkTarget = new File(Main.class.getProtectionDomain().getCodeSource().getLocation().toURI()).toString();
			} catch (URISyntaxException ex) {
				return;
			}

			ShellLink newLink = new ShellLink();
			newLink.setTarget(linkTarget);
			newLink.setCMDArgs(" starttray");
			try {
				newLink.saveTo(linkPath);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		} else {
			new File(linkPath).delete();
		}
		//
    }//GEN-LAST:event_menuConfigInitActionPerformed

	// </editor-fold>
	// <editor-fold defaultstate="collapsed" desc="Variáveis">
    // Variables declaration - do not modify//GEN-BEGIN:variables
	// Generated using JFormDesigner Evaluation license - Victor Cleber Laureano
	private JMenu menuArquivo;
	private JMenuItem menuItemPerfils;
	private JMenuItem menuIniciar;
	private JMenuItem menuIndexar;
	private JMenuItem menuSair;
	private JMenu menuRelatorios;
	private JMenuItem menuResultados;
	private JMenuItem menuHistoricoIndexacao;
	private JMenu menuFerramentas;
	private JMenu menuAparencia;
	private JMenuItem menuItemMetal;
	private JMenuItem menuItemNimbus;
	private JMenu menuCache;
	private JMenuItem menuCacheLimpar;
	private JMenuItem menuCacheImport;
	private JMenuItem menuCacheExport;
	private JMenuItem menuItemThread;
	private JMenu menuConfiguracoes;
	private JCheckBoxMenuItem menuConfigInit;
	private JMenuItem menuAtalhos;
	private JMenuItem menuSobre;
    // End of variables declaration//GEN-END:variables
	// </editor-fold>

}
