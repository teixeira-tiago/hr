package gui;

import java.awt.Color;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.ImageIcon;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JLayeredPane;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.WindowConstants;

import net.miginfocom.swing.MigLayout;
import pesquisa.ImportPesquisa;
import pesquisa.Pesquisa;
import pesquisa.Questionario;
import pesquisa.Reports;
import recrutamento.Curriculum;
import recrutamento.Selecao;
import utiliter.Util;
import cargos.Cargo;
import cargos.Salario;

/**
 * @author Tiago A. Teixeira {@link tiago.a.teixeira@acm.org}
 * 
 */
@SuppressWarnings("serial")
public class Main extends JFrame {

	/**
	 * Creates new form Main
	 */

	@SuppressWarnings("unused")
	private int user = Integer.MIN_VALUE;

	private int level = Integer.MIN_VALUE;

	private final Util util = new Util();

	public Main() {
		user = util.getUserSis();
		level = util.getLevelSis();
		initComponents();
		setLocationRelativeTo(null);

		switch (level) {
		case 1: // tudo liberado
			break;
		case 2: // bloquear alguns relatorios
			break;
		case 3: // bloquear alguns relatorios
			break;
		case 4: // bloquear alguns relatorios
			break;
		case 5: // bloquear alguns relatorios e indicadores
			break;
		case 6: // bloquear alguns relatorios e alguns indicadores
			break;
		case 7: // bloquear alguns relatorios e todos os indicadores
			break;
		case 8: // bloquear todos os relatorios e todos os indicadores
			break;
		default:
			menuBar.setVisible(false);
		}
	}

	private void initComponents() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			util.setUIFont(util.getFont());
		} catch (UnsupportedLookAndFeelException | InstantiationException
				| IllegalAccessException | ClassNotFoundException ex) {
			Logger.getLogger(this.getName()).log(Level.SEVERE, null, ex);
		}
		setIconImage(new ImageIcon(getClass().getResource("/img/favicon.png"))
				.getImage());
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setTitle("Sistema de Gestão de Pessoas");
		setBounds(0, 0, 1024, 768);
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent evt) {
				formWindowClosing(evt);
			}

			@Override
			public void windowClosed(WindowEvent evt) {
				formWindowClosed(evt);
			}
		});
		try {
			img = javax.imageio.ImageIO.read(new File("./"
					+ util.getConf("screen")));

		} catch (Exception e) {
		}

		desktopPane = new JDesktopPane() {
			@Override
			public void paintComponent(Graphics g) {
				super.paintComponent(g);
				if (img != null) {
					g.drawImage(img, 0, 0, this.getWidth(), this.getHeight(),
							this);
					// g.drawString("Imagem carregada", this.getWidth() / 2,
					// this.getHeight() / 2);
				} else
					g.drawString("Image not found", 50, 50);
			}

		};
		desktopPane.setBackground(new Color(65, 105, 170));
		setExtendedState(6);
		getContentPane().setLayout(
				new MigLayout("", "0[1152px,grow]0",
						"0[22px:22px]0[746px,grow]0"));

		menuBar = new JMenuBar();
		menuBar.setPreferredSize(new Dimension(22, 1152));
		getContentPane().add(menuBar, "cell 0 0,grow");

		mnFechar = new JMenu("Fechar");
		menuBar.add(mnFechar);

		mntmSair = new JMenuItem("Sair");
		mntmSair.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				mnFecharActionPerformed(e);
			}
		});
		mntmSair.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q,
				InputEvent.CTRL_MASK));
		mnFechar.add(mntmSair);

		mnFormulario = new JMenu("Formulários");
		menuBar.add(mnFormulario);

		mntmAvaliaoDeDesempenho = new JMenuItem("Avaliação de Desempenho");
		mntmAvaliaoDeDesempenho.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				mntmAvaliaoDeDesempenhoActionPerformed(e);
			}
		});
		mnFormulario.add(mntmAvaliaoDeDesempenho);

		mntmRequisioDePessoal = new JMenuItem("Requisição de Pessoal");
		mnFormulario.add(mntmRequisioDePessoal);

		mntmModeloDeCurrculo = new JMenuItem("Ficha de Solicitação de Emprego");
		mnFormulario.add(mntmModeloDeCurrculo);
		mntmModeloDeCurrculo.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				mntmModeloDeCurrculoActionPerformed(e);
			}
		});
		mntmRequisioDePessoal.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				mntmRequisioDePessoalActionPerformed(e);
			}
		});

		mnRecrutamentoESeleo = new JMenu("Recrutamento & Seleção");
		menuBar.add(mnRecrutamentoESeleo);

		mntmCbo = new JMenuItem("C.B.O.");
		mntmCbo.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				mntmCboActionPerformed(e);
			}
		});
		mnRecrutamentoESeleo.add(mntmCbo);

		mntmEntrevistaDeSeleo = new JMenuItem("Seleção");
		mntmEntrevistaDeSeleo.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				mntmEntrevistaDeSeleoActionPerformed(e);
			}
		});
		mnRecrutamentoESeleo.add(mntmEntrevistaDeSeleo);

		mntmBancoDeCurrculos = new JMenuItem("Cadastro de Candidato");
		mntmBancoDeCurrculos.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				mntmBancoDeCurrculosActionPerformed(e);
			}
		});
		mnRecrutamentoESeleo.add(mntmBancoDeCurrculos);

		mntmRemuneraoEBenefcios = new JMenu("Remuneração & Benefícios");
		menuBar.add(mntmRemuneraoEBenefcios);

		JMenuItem mntmCadastroDeCargos = new JMenuItem("Descrição de Cargos");
		mntmCadastroDeCargos.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				mntmCadastroDeCargosActionPerformed(arg0);
			}
		});
		mntmRemuneraoEBenefcios.add(mntmCadastroDeCargos);

		JMenuItem mntmPesquisaSalarial = new JMenuItem("Pesquisa Salarial");
		mntmPesquisaSalarial.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				mntmPesquisaSalarialActionPerformed(e);
			}
		});
		mntmRemuneraoEBenefcios.add(mntmPesquisaSalarial);

		mnPolticaEAdministrao = new JMenu("Política e Administração de RH");
		menuBar.add(mnPolticaEAdministrao);

		mntmManualDoColaborador = new JMenuItem("Manual do Colaborador");
		mntmManualDoColaborador.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				mntmManualDoColaboradorActionPerformed(e);
			}
		});
		mnPolticaEAdministrao.add(mntmManualDoColaborador);

		mntmRegimentoInterno = new JMenuItem("Regimento Interno");
		mnPolticaEAdministrao.add(mntmRegimentoInterno);

		mntmDiagnsticoDeClima = new JMenuItem(
				"Diagnóstico de Clima Organizacional");
		mnPolticaEAdministrao.add(mntmDiagnsticoDeClima);

		mnPesquisa = new JMenu("Pesquisa");
		mnPolticaEAdministrao.add(mnPesquisa);

		mntmCriarQuestionario = new JMenuItem("Criar questionario");
		mnPesquisa.add(mntmCriarQuestionario);

		mntmPesquisa = new JMenuItem("Pesquisa");
		mnPesquisa.add(mntmPesquisa);

		mntmRelatorios = new JMenuItem("Relatorios");
		mntmRelatorios.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				mntmRelatoriosActionPerformed(e);
			}
		});
		mnPesquisa.add(mntmRelatorios);

		JMenuItem mntmImportarPesquisa = new JMenuItem("Importar Pesquisa");
		mntmImportarPesquisa.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				mntmImportarPesquisaActionPerformed(e);
			}
		});
		mnPesquisa.add(mntmImportarPesquisa);
		mntmPesquisa.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				mntmPesquisaActionPerformed(e);
			}
		});
		mntmCriarQuestionario.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				mntmCriarQuestionarioActionPerformed(e);
			}
		});

		mnTreinamentoEDesenvolvimento = new JMenu(
				"Treinamento e Desenvolvimento");
		menuBar.add(mnTreinamentoEDesenvolvimento);
		getContentPane().add(desktopPane, "cell 0 1,grow");
		// pack();
		// 223, 310 are the aspect values for a card image, width, height
		// these need to be maintained as the GUI size changes
		// this.setExtendedState(this.getExtendedState() |
		// JFrame.MAXIMIZED_BOTH);
	}

	private void formWindowClosed(WindowEvent evt) {
		util.limparArquivo("config/ultimo_login.properties");
	}

	private void formWindowClosing(WindowEvent evt) {
		util.limparArquivo("config/ultimo_login.properties");
	}

	private void mnFecharActionPerformed(ActionEvent e) {
		System.exit(0);
	}

	private void mntmManualDoColaboradorActionPerformed(ActionEvent e) {
		try {
			File file = new File("formularios/ManualDoColaborador.pdf");
			Desktop.getDesktop().open(file);
		} catch (IOException ex) {
			Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	private void mntmAvaliaoDeDesempenhoActionPerformed(ActionEvent e) {
		try {
			File file = new File("formularios/AvaliacaoDesempenho.pdf");
			Desktop.getDesktop().open(file);
		} catch (IOException ex) {
			Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	private void mntmRequisioDePessoalActionPerformed(ActionEvent e) {
		try {
			File file = new File("formularios/RequisicaoDePessoal.pdf");
			Desktop.getDesktop().open(file);
		} catch (IOException ex) {
			Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	private void mntmModeloDeCurrculoActionPerformed(ActionEvent e) {
		try {
			File file = new File("formularios/SolicitacaoDeEmprego.pdf");
			Desktop.getDesktop().open(file);
		} catch (IOException ex) {
			Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	private void mntmBancoDeCurrculosActionPerformed(ActionEvent e) {
		Curriculum curriculum = new Curriculum();
		desktopPane.add(curriculum, JLayeredPane.DEFAULT_LAYER);
		curriculum.setVisible(true);
		curriculum.setPosicao();
	}

	private void mntmEntrevistaDeSeleoActionPerformed(ActionEvent e) {
		Selecao selecao = new Selecao();
		desktopPane.add(selecao, JLayeredPane.DEFAULT_LAYER);
		selecao.setVisible(true);
		selecao.setPosicao();
	}

	private void mntmCboActionPerformed(ActionEvent e) {
		try {
			File file = new File("CBOConsultas.jar");
			Desktop.getDesktop().open(file);
		} catch (IOException ex) {
			Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	private void mntmCriarQuestionarioActionPerformed(ActionEvent e) {
		Questionario questionario = new Questionario();
		desktopPane.add(questionario, JLayeredPane.DEFAULT_LAYER);
		questionario.setVisible(true);
		questionario.setPosicao();
	}

	private void mntmPesquisaActionPerformed(ActionEvent e) {
		Pesquisa pesquisa = new Pesquisa();
		desktopPane.add(pesquisa, JLayeredPane.DEFAULT_LAYER);
		pesquisa.setVisible(true);
		pesquisa.setPosicao();
	}

	private void mntmImportarPesquisaActionPerformed(ActionEvent e) {
		Questionario questionario = new Questionario();
		desktopPane.add(questionario, JLayeredPane.DEFAULT_LAYER);

		ImportPesquisa importPes = new ImportPesquisa(questionario);
		desktopPane.add(importPes, JLayeredPane.DEFAULT_LAYER);
		importPes.setVisible(true);
		importPes.setPosicao();
	}

	private void mntmRelatoriosActionPerformed(ActionEvent e) {
		new Reports().setVisible(true);
	}

	private void mntmCadastroDeCargosActionPerformed(ActionEvent e) {
		Cargo cargo = new Cargo();
		desktopPane.add(cargo, JLayeredPane.DEFAULT_LAYER);
		cargo.setVisible(true);
		cargo.setPosicao();
	}

	private void mntmPesquisaSalarialActionPerformed(ActionEvent e) {
		Salario salario = new Salario();
		desktopPane.add(salario, JLayeredPane.DEFAULT_LAYER);
		salario.setVisible(true);
		salario.setPosicao();
	}

	/**
	 * @param args
	 *            the command line arguments
	 */
	public static void main(String args[]) {
		/* Create and display the form */
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				new Main().setVisible(true);
			}
		});
	}

	private JMenuBar menuBar;
	private JMenu mnFechar;
	private JMenu mnFormulario;
	private JMenu mnRecrutamentoESeleo;
	private JMenu mnPesquisa;
	private JMenu mnPolticaEAdministrao;
	private JMenu mnTreinamentoEDesenvolvimento;
	private JMenu mntmRemuneraoEBenefcios;
	private JMenuItem mntmManualDoColaborador;
	private JMenuItem mntmRegimentoInterno;
	private JMenuItem mntmDiagnsticoDeClima;
	private JMenuItem mntmSair;
	private JMenuItem mntmAvaliaoDeDesempenho;
	private JMenuItem mntmRequisioDePessoal;
	private JMenuItem mntmEntrevistaDeSeleo;
	private JMenuItem mntmModeloDeCurrculo;
	private JMenuItem mntmBancoDeCurrculos;
	private JMenuItem mntmRelatorios;
	private JMenuItem mntmCbo;
	private JMenuItem mntmCriarQuestionario;
	private JMenuItem mntmPesquisa;
	private JDesktopPane desktopPane;
	private Image img;
}
