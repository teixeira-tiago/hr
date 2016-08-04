package recrutamento;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;
import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.MaskFormatter;
import javax.swing.text.NumberFormatter;

import net.miginfocom.swing.MigLayout;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRResultSetDataSource;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import utiliter.SQL;
import utiliter.Util;
import utiliter.component.AComboBox;
import utiliter.component.TDate;
import utiliter.component.TPButton;
import utiliter.component.TTable;

/**
 * @author Tiago A. Teixeira {@link tiago.a.teixeira@acm.org}
 * 
 */
@SuppressWarnings("serial")
public class Curriculum extends JInternalFrame {

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					Curriculum window = new Curriculum();
					window.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Curriculum() {
		setResizable(true);
		initialize();
		initComboBoxes();
		candidatosTable.setVisible(false);
		SQL sql = new SQL();
		carregarTabelaPrincipal(sql.carregarCadastroCandidato());
		sql.close();
		index = total = candidatosTable.getRowCount() - 1;
		if (index >= 0) {
			carregarRegistroPrincipal(index);
		}
		editar(false);
		candidatosTable.setVisible(false);
	}

	public void setPosicao() {
		Dimension d = this.getDesktopPane().getSize();
		this.setLocation((d.width - this.getSize().width) / 2,
				(d.height - this.getSize().height) / 2);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			util.setUIFont(util.getFont());
		} catch (UnsupportedLookAndFeelException | InstantiationException
				| IllegalAccessException | ClassNotFoundException ex) {
			Logger.getLogger(this.getName()).log(Level.SEVERE, null, ex);
		}
		setFrameIcon(new ImageIcon(getClass().getResource("/img/favicon.png")));
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setTitle("Cadastro de Curriculum");
		setMaximizable(true);
		setResizable(true);
		setClosable(true);

		buscaPanel = new JPanel();
		cadastroPanel = new JPanel();
		experienciaPanel = new JPanel();
		socialPanel = new JPanel();
		dadosTabbedPane = new JTabbedPane(SwingConstants.TOP);
		diplomabuttonGroup = new ButtonGroup();
		candidatosTable = new TTable();
		cursoConclbuttonGroup = new ButtonGroup();
		voluntariobuttonGroup = new ButtonGroup();
		esportesbuttonGroup = new ButtonGroup();
		residenciabuttonGroup = new ButtonGroup();
		turnobuttonGroup = new ButtonGroup();
		acidentebuttonGroup = new ButtonGroup();
		enfermidadebuttonGroup = new ButtonGroup();
		outroEmpregobuttonGroup = new ButtonGroup();
		cidadeCombo = new HashMap<Integer, Integer>();
		cidade2Combo = new HashMap<Integer, Integer>();

		setBounds(1, 1, 900, 580);
		getContentPane().setLayout(
				new MigLayout("", "5[755px,grow]5",
						"5[60px]0[435px,grow]0[40px]5"));

		buscaPanel.setPreferredSize(new Dimension(450, 75));
		getContentPane().add(buscaPanel, "cell 0 0,alignx left,growy");

		buscaPanel.setLayout(null);

		lblCpf = new JLabel("C.P.F.:");
		lblCpf.setBounds(10, 5, 39, 16);
		buscaPanel.add(lblCpf);
		lblCdigo = new JLabel("Código:");
		lblCdigo.setBounds(135, 5, 49, 16);
		buscaPanel.add(lblCdigo);
		cpfConsFormattedTextField = new JFormattedTextField();

		cpfConsFormattedTextField.setBounds(5, 23, 120, 24);
		cpfConsFormattedTextField.setPreferredSize(new Dimension(100, 30));
		try {
			cpfConsFormattedTextField
					.setFormatterFactory(new DefaultFormatterFactory(
							new MaskFormatter("###.###.###-##")));
		} catch (ParseException e4) {
			e4.printStackTrace();
		}
		cpfConsFormattedTextField
				.addKeyListener(new java.awt.event.KeyAdapter() {
					@Override
					public void keyReleased(java.awt.event.KeyEvent evt) {
						cpfConsFormattedTextFieldKeyReleased(evt);
					}
				});
		buscaPanel.add(cpfConsFormattedTextField);
		printButton = new JButton("");

		printButton.setBounds(385, 18, 30, 30);
		printButton.setPreferredSize(new Dimension(35, 35));
		printButton.setIcon(new ImageIcon(getClass().getResource(
				"/img/print.png")));
		printButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent evt) {
				printButtonActionPerformed(evt);
			}
		});
		lastButton = new JButton("");

		lastButton.setBounds(350, 18, 30, 30);
		lastButton.setPreferredSize(new Dimension(35, 35));
		lastButton.setIcon(new ImageIcon(getClass()
				.getResource("/img/last.png")));
		lastButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent evt) {
				lastButtonActionPerformed(evt);
			}
		});
		nextButton = new JButton("");

		nextButton.setBounds(315, 18, 30, 30);
		nextButton.setPreferredSize(new Dimension(35, 35));
		nextButton.setIcon(new ImageIcon(getClass()
				.getResource("/img/next.png")));
		nextButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent evt) {
				nextButtonActionPerformed(evt);
			}
		});
		beforeButton = new JButton("");

		beforeButton.setBounds(280, 18, 30, 30);
		beforeButton.setPreferredSize(new Dimension(35, 35));
		beforeButton.setIcon(new ImageIcon(getClass().getResource(
				"/img/before.png")));
		beforeButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent evt) {
				beforeButtonActionPerformed(evt);
			}
		});
		firstButton = new JButton("");
		firstButton.setBounds(245, 18, 30, 30);
		firstButton.setPreferredSize(new Dimension(35, 35));
		firstButton.setIcon(new ImageIcon(getClass().getResource(
				"/img/first.png")));
		firstButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent evt) {
				firstButtonActionPerformed(evt);
			}
		});

		findButton = new JButton("");

		findButton.setBounds(210, 18, 30, 30);
		findButton.setPreferredSize(new java.awt.Dimension(35, 35));
		findButton.setIcon(new ImageIcon(getClass().getResource(
				"/img/procurar.png")));
		findButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent evt) {
				findButtonActionPerformed(evt);
			}
		});

		codTextField = new JTextField();
		codTextField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent evt) {
				codTextFieldKeyReleased(evt);
			}
		});
		codTextField.setBounds(130, 23, 75, 24);
		buscaPanel.add(codTextField);
		codTextField.setColumns(10);
		buscaPanel.add(findButton);
		buscaPanel.add(firstButton);
		buscaPanel.add(beforeButton);
		buscaPanel.add(nextButton);
		buscaPanel.add(lastButton);
		buscaPanel.add(printButton);

		getContentPane().add(dadosTabbedPane, "cell 0 1,grow");

		dadosTabbedPane.addTab("Cadastro", null, cadastroPanel, null);
		cadastroPanel
				.setLayout(new MigLayout(
						"",
						"5[100px:100px:100px]0[37px:37px:37px]0[5px]0[82px]0[5px]0[75px:75px:75px]0[25px:25px:25px]0[25px:25px:25px]0[5px]0[22px]0[5px]0[25px:25px]0[5px]0[47px,grow]0[5px]0[25px:25px:25px]0[5px]0[20px]0[1px]0[4px]0[4px]0[5px]0[100px,grow]0[:5px:5px]0[25px:25px:25px]5",
						"5[24px]0[6px]0[24px]0[6px]0[24px]0[6px]0[24px]0[6px]0[24px]0[6px]0[24px]0[6px]0[18px]0[6px]0[24px]0[16px,grow]5"));

		fotoLabel = new JLabel("");
		fotoLabel.setIcon(new ImageIcon(util.getConf("foto") + "nobody.png"));

		fotoPanel = new JPanel();
		fotoPanel.setBackground(Color.WHITE);
		fotoPanel.setLayout(new MigLayout("", "0[137px,grow]0",
				"0[150px,grow]0"));
		fotoPanel.add(fotoLabel, "cell 0 0,grow");
		cadastroPanel.add(fotoPanel, "cell 0 0 2 9,grow");

		lblNome = new JLabel("Nome:");
		lblNome.setHorizontalAlignment(SwingConstants.RIGHT);
		cadastroPanel.add(lblNome, "cell 3 0,alignx right,aligny center");

		nomeTextField = new JTextField();
		cadastroPanel.add(nomeTextField, "cell 5 0 14 1,grow");

		lblSexo = new JLabel("Sexo:");
		lblSexo.setHorizontalAlignment(SwingConstants.RIGHT);
		cadastroPanel.add(lblSexo, "cell 20 0,growx,aligny center");

		sexoComboBox = new AComboBox();
		cadastroPanel.add(sexoComboBox, "cell 22 0 3 1,grow");

		lblEndereo = new JLabel("Endereço:");
		lblEndereo.setHorizontalAlignment(SwingConstants.RIGHT);
		cadastroPanel.add(lblEndereo, "cell 3 2,alignx right,aligny center");

		enderecoTextField = new JTextField();
		cadastroPanel.add(enderecoTextField, "cell 5 2 14 1,grow");

		lblCep = new JLabel("C.E.P.:");
		lblCep.setHorizontalAlignment(SwingConstants.RIGHT);
		cadastroPanel.add(lblCep, "cell 20 2,growx,aligny center");

		cepFormattedTextField = new JFormattedTextField();
		try {
			cepFormattedTextField
					.setFormatterFactory(new DefaultFormatterFactory(
							new MaskFormatter("#####-###")));
		} catch (ParseException e3) {
			e3.printStackTrace();
		}
		cadastroPanel.add(cepFormattedTextField, "cell 22 2 3 1,grow");

		lblEstado = new JLabel("Fixo:");
		lblEstado.setHorizontalAlignment(SwingConstants.RIGHT);
		cadastroPanel.add(lblEstado, "cell 3 4,alignx right,aligny center");

		estadoComboBox = new AComboBox();
		estadoComboBox.addItemListener(new java.awt.event.ItemListener() {
			@Override
			public void itemStateChanged(java.awt.event.ItemEvent evt) {
				estadoComboBoxItemStateChanged(evt);
			}
		});
		cadastroPanel.add(estadoComboBox, "cell 11 6 3 1,grow");

		lblCidade = new JLabel("Cidade:");
		lblCidade.setHorizontalAlignment(SwingConstants.RIGHT);
		cadastroPanel.add(lblCidade, "cell 9 4,growx,aligny center");

		cidadeComboBox = new AComboBox();
		cadastroPanel.add(cidadeComboBox, "cell 11 4 12 1,grow");

		lblCelular = new JLabel("Celular:");
		lblCelular.setHorizontalAlignment(SwingConstants.RIGHT);
		cadastroPanel.add(lblCelular, "cell 3 6,alignx right,aligny center");

		celFormattedTextField = new JFormattedTextField();
		try {
			celFormattedTextField
					.setFormatterFactory(new DefaultFormatterFactory(
							new MaskFormatter("(##)#####-###*")));
		} catch (Exception e) {
			e.printStackTrace();
		}
		cadastroPanel.add(celFormattedTextField, "cell 5 6 3 1,grow");

		lblFixo = new JLabel("Estado:");
		lblFixo.setHorizontalAlignment(SwingConstants.RIGHT);
		cadastroPanel.add(lblFixo, "cell 9 6,alignx center,aligny center");

		foneFormattedTextField = new JFormattedTextField();
		foneFormattedTextField.setColumns(10);
		try {
			foneFormattedTextField
					.setFormatterFactory(new DefaultFormatterFactory(
							new MaskFormatter("(##)#####-###*")));
		} catch (Exception e) {
			e.printStackTrace();
		}
		cadastroPanel.add(foneFormattedTextField, "cell 5 4 3 1,grow");

		lblEstadoCivil = new JLabel("Est. Civil:");
		lblEstadoCivil.setHorizontalAlignment(SwingConstants.RIGHT);
		cadastroPanel.add(lblEstadoCivil, "cell 17 6 2 1,growx,aligny center");

		estCivilComboBox = new AComboBox();
		cadastroPanel.add(estCivilComboBox, "cell 20 6 5 1,grow");

		lblNascimento = new JLabel("Nascimento:");
		lblNascimento.setHorizontalAlignment(SwingConstants.RIGHT);
		cadastroPanel.add(lblNascimento, "cell 3 8,growx,aligny center");
		nascDatePicker = new TDate();
		nascDatePicker.getJButton().setLocation(97, 2);
		nascDatePicker.getJFormattedTextField().setSize(93, 24);
		cadastroPanel.add(nascDatePicker, "cell 5 8 3 1,grow");

		lblLocal = new JLabel("Local:");
		lblLocal.setHorizontalAlignment(SwingConstants.RIGHT);
		cadastroPanel.add(lblLocal, "cell 9 8,growx,aligny center");

		localTextField = new JTextField();
		cadastroPanel.add(localTextField, "cell 11 8 14 1,grow");
		localTextField.setColumns(20);

		fotoButton = new JButton("Carregar");
		fotoButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent evt) {
				fotoButtonActionPerformed(evt);
			}
		});
		cadastroPanel.add(fotoButton, "cell 0 10 2 1,alignx center,growy");

		lblCpf_1 = new JLabel("C.P.F.:");
		lblCpf_1.setHorizontalAlignment(SwingConstants.RIGHT);
		cadastroPanel.add(lblCpf_1, "cell 3 10,alignx right,aligny center");
		cpfFormattedTextField = new JFormattedTextField();
		cpfFormattedTextField.setColumns(15);
		try {
			cpfFormattedTextField
					.setFormatterFactory(new DefaultFormatterFactory(
							new MaskFormatter("###.###.###-##")));
		} catch (Exception e) {
			e.printStackTrace();
		}
		cadastroPanel.add(cpfFormattedTextField, "cell 5 10 3 1,grow");

		lblRg = new JLabel("R.G.:");
		lblRg.setHorizontalAlignment(SwingConstants.RIGHT);
		cadastroPanel.add(lblRg, "cell 9 10,alignx right,aligny center");

		rgTextField = new JTextField();
		cadastroPanel.add(rgTextField, "cell 11 10 5 1,grow");
		rgTextField.setColumns(10);

		lblCtps = new JLabel("C.T.P.S.:");
		lblCtps.setHorizontalAlignment(SwingConstants.RIGHT);
		cadastroPanel.add(lblCtps, "cell 17 10 2 1,alignx left,aligny center");

		ctpsTextField = new JTextField();
		cadastroPanel.add(ctpsTextField, "cell 20 10 5 1,grow");
		ctpsTextField.setColumns(10);

		lblGrauDeEscolaridade = new JLabel("Escolaridade:");
		lblGrauDeEscolaridade.setHorizontalAlignment(SwingConstants.RIGHT);
		cadastroPanel.add(lblGrauDeEscolaridade,
				"cell 0 12,growx,aligny center");

		grauEscComboBox = new AComboBox();
		cadastroPanel.add(grauEscComboBox, "cell 1 12 5 1,grow");

		lblTempoTotalDe = new JLabel("Experiência:");
		lblTempoTotalDe.setHorizontalAlignment(SwingConstants.RIGHT);
		cadastroPanel.add(lblTempoTotalDe, "cell 7 12 3 1,growx,aligny center");

		exptComboBox = new AComboBox();
		cadastroPanel.add(exptComboBox, "cell 11 12 4 1,grow");

		lblInteressePrimario = new JLabel("Area:");
		lblInteressePrimario.setHorizontalAlignment(SwingConstants.RIGHT);
		cadastroPanel.add(lblInteressePrimario,
				"cell 18 12,growx,aligny center");

		areaComboBox = new AComboBox();
		cadastroPanel.add(areaComboBox, "cell 20 12 3 1,grow");

		lblObservaes = new JLabel("Observações:");
		cadastroPanel.add(lblObservaes, "cell 0 14,alignx left,growy");

		scrollPane_2 = new JScrollPane();
		cadastroPanel.add(scrollPane_2, "cell 0 15 25 1,grow");

		obsCadTextArea = new JTextArea();
		scrollPane_2.setViewportView(obsCadTextArea);

		estButton = new TPButton("...");
		estButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				estButtonActionPerformed(e);
			}
		});
		cadastroPanel.add(estButton, "cell 15 6,grow");

		cidButton = new TPButton("...");
		cidButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				cidButtonActionPerformed(e);
			}
		});
		cadastroPanel.add(cidButton, "cell 24 4,grow");

		areaButton = new TPButton("...");
		areaButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				area1ButtonActionPerformed(e);
			}
		});
		cadastroPanel.add(areaButton, "cell 24 12,grow");

		escButton = new TPButton("...");
		escButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				escButtonActionPerformed(e);
			}
		});
		cadastroPanel.add(escButton, "cell 6 12,grow");

		exptButton = new TPButton("...");
		exptButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				exptButtonActionPerformed(e);
			}
		});
		cadastroPanel.add(exptButton, "cell 15 12,grow");

		dadosTabbedPane.addTab("Experiência", null, experienciaPanel, null);
		experienciaPanel
				.setLayout(new MigLayout(
						"",
						"5[46px]0[5px]0[34px]0[7px]0[18px]0[5px]0[82px,grow]0[5px]0[10px]0[2px]0[8px]0[5px]0[52px]0[195px,grow]0[5px]0[21px]0[4px]0[63px]0[5px]0[44px]0[4px]0[93px,grow]0[2px]0[23px]5",
						"5[24px]0[6px:6px]0[24px]0[6px:6px]0[24px]0[6px:6px]0[24px:24px]0[6px:6px]0[24px:24px]0[6px:6px]0[245px,grow]5"));

		lblEmpresa = new JLabel("Nome da Empresa:");
		lblEmpresa.setHorizontalAlignment(SwingConstants.RIGHT);
		experienciaPanel.add(lblEmpresa, "cell 0 0 5 1,growx,aligny center");

		lblGerente = new JLabel("Gerente:");
		lblGerente.setHorizontalAlignment(SwingConstants.RIGHT);
		experienciaPanel.add(lblGerente, "cell 17 0,growx,aligny center");

		gerenteTextField = new JTextField();
		experienciaPanel.add(gerenteTextField, "cell 19 0 5 1,grow");
		gerenteTextField.setColumns(10);

		lblEstado_1 = new JLabel("Estado:");
		lblEstado_1.setHorizontalAlignment(SwingConstants.RIGHT);
		experienciaPanel.add(lblEstado_1, "cell 0 2,growx,aligny center");

		estado2ComboBox = new AComboBox();
		estado2ComboBox.addItemListener(new java.awt.event.ItemListener() {
			@Override
			public void itemStateChanged(java.awt.event.ItemEvent evt) {
				estado2ComboBoxItemStateChanged(evt);
			}
		});
		experienciaPanel.add(estado2ComboBox, "cell 2 2 5 1,grow");

		lblCidade_1 = new JLabel("Cidade:");
		lblCidade_1.setHorizontalAlignment(SwingConstants.RIGHT);
		experienciaPanel.add(lblCidade_1, "cell 12 2,growx,aligny center");

		cidade2ComboBox = new AComboBox();
		experienciaPanel.add(cidade2ComboBox, "cell 13 2,grow");

		lblAtividade = new JLabel("Atividade:");
		lblAtividade.setHorizontalAlignment(SwingConstants.RIGHT);
		experienciaPanel.add(lblAtividade, "cell 17 2,growx,aligny center");

		atividadeComboBox = new AComboBox();
		experienciaPanel.add(atividadeComboBox, "cell 19 2 3 1,grow");

		lblUltimoCargo = new JLabel("Último cargo:");
		lblUltimoCargo.setHorizontalAlignment(SwingConstants.RIGHT);
		experienciaPanel
				.add(lblUltimoCargo, "cell 0 4 3 1,growx,aligny center");

		cargoTextField = new JTextField();
		experienciaPanel.add(cargoTextField, "cell 4 4 12 1,grow");
		cargoTextField.setColumns(10);

		lblltimoSalrio = new JLabel("Último salário: R$");
		lblltimoSalrio.setHorizontalAlignment(SwingConstants.RIGHT);
		experienciaPanel.add(lblltimoSalrio,
				"cell 17 4 3 1,growx,aligny center");

		salarioFormattedTextField = new JFormattedTextField();
		salarioFormattedTextField
				.setFormatterFactory(new DefaultFormatterFactory(
						new NumberFormatter(new DecimalFormat("#,##0.00"))));
		experienciaPanel.add(salarioFormattedTextField, "cell 21 4 3 1,grow");

		lblDataEntrada = new JLabel("Data Entrada:");
		lblDataEntrada.setHorizontalAlignment(SwingConstants.RIGHT);
		experienciaPanel
				.add(lblDataEntrada, "cell 0 6 3 1,growx,aligny center");

		entradaDatePicker = new TDate();
		entradaDatePicker.getJButton().setLocation(97, 2);
		entradaDatePicker.getJFormattedTextField().setSize(93, 24);
		experienciaPanel.add(entradaDatePicker, "cell 4 6 5 1,grow");

		lblMotivoDaSada = new JLabel("<html>Motivo da .<br>saída:</html>");
		lblMotivoDaSada.setHorizontalAlignment(SwingConstants.RIGHT);
		experienciaPanel.add(lblMotivoDaSada, "cell 10 6 3 3,growx,aligny top");

		lblDataSada = new JLabel("Data Saída:");
		lblDataSada.setHorizontalAlignment(SwingConstants.RIGHT);
		experienciaPanel.add(lblDataSada, "cell 0 8 3 1,growx,aligny center");

		saidaDatePicker = new TDate();
		saidaDatePicker.getJButton().setLocation(97, 2);
		saidaDatePicker.getJFormattedTextField().setSize(93, 24);
		experienciaPanel.add(saidaDatePicker, "cell 4 8 5 1,grow");

		scrollPane_3 = new JScrollPane();
		experienciaPanel.add(scrollPane_3, "cell 13 6 11 3,grow");

		motivoTextArea = new JTextArea();
		scrollPane_3.setViewportView(motivoTextArea);

		scrollPane_4 = new JScrollPane();
		experienciaPanel.add(scrollPane_4, "cell 0 10 24 1,grow");

		experienciaTable = new TTable();
		experienciaTable.addMouseListener(new java.awt.event.MouseAdapter() {
			@Override
			public void mouseClicked(java.awt.event.MouseEvent evt) {
				experienciaTableMouseClicked(evt);
			}
		});
		scrollPane_4.setViewportView(experienciaTable);

		empTextField = new JTextField();
		experienciaPanel.add(empTextField, "cell 6 0 10 1,grow");
		empTextField.setColumns(10);

		est2Button = new TPButton("...");
		est2Button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				est2ButtonActionPerformed(e);
			}
		});
		experienciaPanel.add(est2Button, "cell 8 2 3 1,grow");

		cid2Button = new TPButton("...");
		cid2Button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				cid2ButtonActionPerformed(e);
			}
		});
		experienciaPanel.add(cid2Button, "cell 15 2,grow");

		ativButton = new TPButton("...");
		ativButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ativButtonActionPerformed(e);
			}
		});
		experienciaPanel.add(ativButton, "cell 23 2,grow");
		cursoPanel = new JPanel();

		dadosTabbedPane.addTab("Cursos e Escolaridade", null, cursoPanel, null);
		cursoPanel
				.setLayout(new MigLayout(
						"",
						"5[41px]0[5px]0[19px]0[5px]0[50px]0[5px,grow]0[77px]0[5px]0[169px,grow]0[5px]0[19px]0[5px]0[8px]0[4px]0[63px]0[5px]0[57px,grow]0[5px]0[20px]0[5px]0[39px]0[4px]0[122px]5",
						"5[24px]0[6px]0[24px]0[6px]0[50px]0[5px]0[50px]0[225px,grow]5"));

		lblNewLabel = new JLabel("Curso:");
		lblNewLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		cursoPanel.add(lblNewLabel, "cell 0 0,growx,aligny center");

		cursoTextField = new JTextField();
		cursoPanel.add(cursoTextField, "cell 2 0 7 1,grow");
		cursoTextField.setColumns(10);

		lblTipo = new JLabel("Tipo:");
		lblTipo.setHorizontalAlignment(SwingConstants.RIGHT);
		cursoPanel.add(lblTipo, "cell 10 0 3 1,growx,aligny center");

		tipoCurComboBox = new AComboBox();
		cursoPanel.add(tipoCurComboBox, "cell 14 0 3 1,grow");

		lblInicio = new JLabel("Inicio:");
		lblInicio.setHorizontalAlignment(SwingConstants.RIGHT);
		cursoPanel.add(lblInicio, "cell 20 0,growx,aligny center");

		inicioCurDatePicker = new TDate();
		inicioCurDatePicker.getJButton().setLocation(97, 2);
		inicioCurDatePicker.getJFormattedTextField().setSize(93, 24);
		cursoPanel.add(inicioCurDatePicker, "cell 22 0,grow");

		lblInstituio = new JLabel("Instituição:");
		lblInstituio.setHorizontalAlignment(SwingConstants.RIGHT);
		cursoPanel.add(lblInstituio, "cell 0 2 3 1,growx,aligny center");

		instTextField = new JTextField();
		cursoPanel.add(instTextField, "cell 4 2 7 1,grow");
		instTextField.setColumns(10);

		lblCargaHoraria = new JLabel("Total horas:");
		lblCargaHoraria.setHorizontalAlignment(SwingConstants.RIGHT);
		cursoPanel.add(lblCargaHoraria, "cell 12 2 3 1,growx,aligny center");

		cargaHoTextField = new JTextField();
		cursoPanel.add(cargaHoTextField, "cell 16 2 3 1,grow");
		cargaHoTextField.setColumns(10);

		lblFim = new JLabel("Fim:");
		lblFim.setHorizontalAlignment(SwingConstants.RIGHT);
		cursoPanel.add(lblFim, "cell 20 2,alignx right,aligny center");

		fimCurDatePicker = new TDate();
		fimCurDatePicker.getJButton().setLocation(97, 2);
		fimCurDatePicker.getJFormattedTextField().setSize(93, 24);
		cursoPanel.add(fimCurDatePicker, "cell 22 2,grow");

		lblObservao = new JLabel("Observação:");
		lblObservao.setHorizontalAlignment(SwingConstants.RIGHT);
		cursoPanel.add(lblObservao, "cell 6 4,growx,aligny top");

		scrollPane_5 = new JScrollPane();
		cursoPanel.add(scrollPane_5, "cell 8 4 15 3,grow");

		obsCurTextArea = new JTextArea();
		scrollPane_5.setViewportView(obsCurTextArea);

		scrollPane_6 = new JScrollPane();
		cursoPanel.add(scrollPane_6, "cell 0 7 23 1,grow");

		cursosTable = new TTable();
		cursosTable.addMouseListener(new java.awt.event.MouseAdapter() {
			@Override
			public void mouseClicked(java.awt.event.MouseEvent evt) {
				cursosTableMouseClicked(evt);
			}
		});
		scrollPane_6.setViewportView(cursosTable);

		tipoButton = new TPButton("...");
		tipoButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				tipoButtonActionPerformed(e);
			}
		});
		cursoPanel.add(tipoButton, "cell 18 0,grow");

		panel12 = new JPanel();
		panel12.setBorder(new TitledBorder(null, "Possui diploma?",
				TitledBorder.LEADING, TitledBorder.TOP, null, null));
		cursoPanel.add(panel12, "cell 0 4 5 1,grow");
		panel12.setLayout(new MigLayout("", "0[50px]0[50px]0", "0[24px]0"));

		simDipRadioButton = new JRadioButton("Sim");
		panel12.add(simDipRadioButton, "cell 0 0,growx,aligny top");
		diplomabuttonGroup.add(simDipRadioButton);

		naoDipRadioButton = new JRadioButton("Não");
		panel12.add(naoDipRadioButton, "cell 1 0,growx,aligny top");
		diplomabuttonGroup.add(naoDipRadioButton);

		panel13 = new JPanel();
		panel13.setBorder(new TitledBorder(null, "Concluido?",
				TitledBorder.LEADING, TitledBorder.TOP, null, null));
		cursoPanel.add(panel13, "cell 0 6 5 1,grow");
		panel13.setLayout(new MigLayout("", "0[50px]0[50px]0", "0[24px]0"));

		simConRadioButton = new JRadioButton("Sim");
		panel13.add(simConRadioButton, "cell 0 0,growx,aligny top");
		cursoConclbuttonGroup.add(simConRadioButton);
		simConRadioButton.setActionCommand("1");

		naoConRadioButton = new JRadioButton("Não");
		panel13.add(naoConRadioButton, "cell 1 0,growx,aligny top");
		cursoConclbuttonGroup.add(naoConRadioButton);
		naoConRadioButton.setActionCommand("0");

		dadosTabbedPane.addTab("Social", null, socialPanel, null);
		dadosTabbedPane
				.addChangeListener(new javax.swing.event.ChangeListener() {
					@Override
					public void stateChanged(javax.swing.event.ChangeEvent evt) {
						dadosTabbedPaneStateChanged(evt);
					}
				});
		socialPanel
				.setLayout(new MigLayout(
						"",
						"5[200px]0[5px]0[75px,grow]0[5px]0[85px]0[5px]0[35px]0[5px]0[80px,grow]0[5px]0[40px]0[5px]0[25px]0[5px]0[50px]0[5px]0[115px]5",
						"5[50px]0[50px]0[50px]0[50px]0[23px]0[24px]0[120px,grow]5"));

		panel1 = new JPanel();
		panel1.setBorder(new TitledBorder(null,
				"Nas horas vagas faz algum trabalho volunt\u00E1rio?",
				TitledBorder.LEADING, TitledBorder.TOP, null, null));
		socialPanel.add(panel1, "cell 0 0 7 1,grow");
		panel1.setLayout(new MigLayout("", "0[50px]0[90px]0[250px,grow]0",
				"0[24px]0"));

		naoVolRadioButton = new JRadioButton("Não");
		panel1.add(naoVolRadioButton, "cell 0 0,growx,aligny bottom");
		voluntariobuttonGroup.add(naoVolRadioButton);
		naoVolRadioButton.setActionCommand("0");

		simVolRadioButton = new JRadioButton("Sim, qual?");
		panel1.add(simVolRadioButton, "cell 1 0,growx,aligny bottom");
		voluntariobuttonGroup.add(simVolRadioButton);
		simVolRadioButton.setActionCommand("1");

		voluntarioTextField = new JTextField();
		panel1.add(voluntarioTextField, "cell 2 0,grow");
		voluntarioTextField.setColumns(10);

		panel4 = new JPanel();
		panel4.setBorder(new TitledBorder(null,
				"Qual o seu passatempo preferido?", TitledBorder.LEADING,
				TitledBorder.TOP, null, null));
		socialPanel.add(panel4, "cell 2 1 7 1,grow");
		panel4.setLayout(new MigLayout("", "0[280px,grow]0", "0[24px]0"));

		pasTempTextField = new JTextField();
		panel4.add(pasTempTextField, "cell 0 0,grow");
		pasTempTextField.setColumns(10);

		lblNewLabel_1 = new JLabel(
				"Está aguardando ser chamado em algum outro emprego para qual tenha feito teste ou concurso?");
		socialPanel.add(lblNewLabel_1, "cell 0 4 13 1,growx,aligny center");

		naoAguRadioButton = new JRadioButton("Não");
		naoAguRadioButton.setActionCommand("0");
		outroEmpregobuttonGroup.add(naoAguRadioButton);
		socialPanel.add(naoAguRadioButton, "cell 14 4,growx,aligny top");

		simAguRadioButton = new JRadioButton("Sim");
		simAguRadioButton.setActionCommand("1");
		outroEmpregobuttonGroup.add(simAguRadioButton);
		socialPanel.add(simAguRadioButton, "cell 16 4,alignx left,aligny top");

		lblQualMotivoO = new JLabel(
				"Qual motivo o levou a procurar esta empresa?");
		socialPanel.add(lblQualMotivoO, "cell 0 5 3 1,growx,aligny center");

		motEsTextField = new JTextField();
		socialPanel.add(motEsTextField, "cell 4 5 13 1,grow");
		motEsTextField.setColumns(10);

		scrollPane_1 = new JScrollPane();

		fracoTextArea = new JTextArea();
		scrollPane_1.setViewportView(fracoTextArea);

		panel11 = new JPanel();
		panel11.setBorder(new TitledBorder(null,
				"Destaque 3 pontos fracos (pontos a melhorar):",
				TitledBorder.LEADING, TitledBorder.TOP, null, null));
		socialPanel.add(panel11, "cell 6 6 11 1,grow");
		panel11.setLayout(new MigLayout("", "0[360px,grow]0", "0[90px,grow]0"));
		panel11.add(scrollPane_1, "cell 0 0,grow");

		scrollPane = new JScrollPane();

		forteTextArea = new JTextArea();
		scrollPane.setViewportView(forteTextArea);

		panel2 = new JPanel();
		panel2.setBorder(new TitledBorder(null, "Pratica algum esporte?",
				TitledBorder.LEADING, TitledBorder.TOP, null, null));
		socialPanel.add(panel2, "cell 8 0 9 1,grow");
		panel2.setLayout(new MigLayout("",
				"0[50px]0[90px]0[140px,grow]0[20px]0", "0[24px]0"));

		naoEspRadioButton = new JRadioButton("Não");
		panel2.add(naoEspRadioButton, "cell 0 0,growx,aligny bottom");
		naoEspRadioButton.setActionCommand("0");
		esportesbuttonGroup.add(naoEspRadioButton);

		simEspRadioButton = new JRadioButton("Sim, qual?");
		panel2.add(simEspRadioButton, "cell 1 0,growx,aligny bottom");
		simEspRadioButton.setActionCommand("1");
		esportesbuttonGroup.add(simEspRadioButton);

		esporteComboBox = new AComboBox();
		panel2.add(esporteComboBox, "cell 2 0,grow");

		espButton = new TPButton("...");
		panel2.add(espButton, "cell 3 0,grow");
		panel3 = new JPanel();
		panel3.setBorder(new TitledBorder(null,
				"Qual o tipo da sua residência?", TitledBorder.LEADING,
				TitledBorder.TOP, null, null));
		socialPanel.add(panel3, "cell 0 1,grow");
		panel3.setLayout(new MigLayout("", "0[75px]0[80px]0", "0[24px]0"));

		resPropRadioButton = new JRadioButton("Própria");
		panel3.add(resPropRadioButton, "cell 0 0,growx,aligny top");
		resPropRadioButton.setActionCommand("0");
		residenciabuttonGroup.add(resPropRadioButton);

		resAlugRadioButton = new JRadioButton("Aluguel");
		panel3.add(resAlugRadioButton, "cell 1 0,alignx left,aligny top");
		resAlugRadioButton.setActionCommand("1");
		residenciabuttonGroup.add(resAlugRadioButton);
		panel5 = new JPanel();
		panel5.setBorder(new TitledBorder(null,
				"Pode trabalhar em qual turno?", TitledBorder.LEADING,
				TitledBorder.TOP, null, null));
		socialPanel.add(panel5, "cell 10 1 7 1,grow");
		panel5.setLayout(new MigLayout("", "0[75px]0[75px]0[75px]0", "0[24px]0"));

		turDiuRadioButton = new JRadioButton("Diruno");
		panel5.add(turDiuRadioButton, "cell 0 0,alignx left,aligny top");
		turDiuRadioButton.setActionCommand("0");
		turnobuttonGroup.add(turDiuRadioButton);

		turNotRadioButton = new JRadioButton("Noturno");
		panel5.add(turNotRadioButton, "cell 1 0,growx,aligny top");
		turNotRadioButton.setActionCommand("1");
		turnobuttonGroup.add(turNotRadioButton);

		turAmbRadioButton = new JRadioButton("Ambos");
		panel5.add(turAmbRadioButton, "cell 2 0,growx,aligny top");
		turAmbRadioButton.setActionCommand("2");
		turnobuttonGroup.add(turAmbRadioButton);

		panel6 = new JPanel();
		panel6.setBorder(new TitledBorder(null,
				"Pode trabalhar em qual escala?", TitledBorder.LEADING,
				TitledBorder.TOP, null, null));
		socialPanel.add(panel6, "cell 0 2 11 1,grow");
		panel6.setLayout(new MigLayout("",
				"0[80px]0[120px]0[80px]0[110px]0[120px]0", "0[24px]0"));

		escSemCheckBox = new JCheckBox("Semanal");
		panel6.add(escSemCheckBox, "cell 0 0,growx,aligny top");

		escFimCheckBox = new JCheckBox("Fim de semana");
		panel6.add(escFimCheckBox, "cell 1 0,growx,aligny top");

		esc1236CheckBox = new JCheckBox("12 por 36");
		panel6.add(esc1236CheckBox, "cell 2 0,growx,aligny top");

		escTarCheckBox = new JCheckBox("Somente tarde");
		panel6.add(escTarCheckBox, "cell 3 0,growx,aligny top");

		escManCheckBox = new JCheckBox("Somente manhã");
		panel6.add(escManCheckBox, "cell 4 0,growx,aligny top");
		panel7 = new JPanel();
		panel7.setBorder(new TitledBorder(null, "Pode iniciar em:",
				TitledBorder.LEADING, TitledBorder.TOP, null, null));
		socialPanel.add(panel7, "cell 12 2 5 1,grow");
		panel7.setLayout(new MigLayout("", "0[160px,grow]0[20px]0", "0[24px]0"));

		iniciarComboBox = new AComboBox();
		panel7.add(iniciarComboBox, "cell 0 0,grow");

		iniButton = new TPButton("...");
		panel7.add(iniButton, "cell 1 0,grow");

		panel8 = new JPanel();
		panel8.setBorder(new TitledBorder(null,
				"Já sofreu algum acidente grave?", TitledBorder.LEADING,
				TitledBorder.TOP, null, null));
		socialPanel.add(panel8, "cell 0 3 5 1,grow");
		panel8.setLayout(new MigLayout("", "0[50px]0[90px]0[210px,grow]0",
				"0[24px]0"));

		naoAciRadioButton = new JRadioButton("Não");
		panel8.add(naoAciRadioButton, "cell 0 0,growx,aligny bottom");
		naoAciRadioButton.setActionCommand("0");
		acidentebuttonGroup.add(naoAciRadioButton);

		simAciRadioButton = new JRadioButton("Sim, qual?");
		panel8.add(simAciRadioButton, "cell 1 0,growx,aligny bottom");
		simAciRadioButton.setActionCommand("1");
		acidentebuttonGroup.add(simAciRadioButton);

		acidenteTextField = new JTextField();
		panel8.add(acidenteTextField, "cell 2 0,grow");
		acidenteTextField.setColumns(10);

		panel9 = new JPanel();
		panel9.setBorder(new TitledBorder(null,
				"Já sofreu alguma enfermidade grave?", TitledBorder.LEADING,
				TitledBorder.TOP, null, null));
		socialPanel.add(panel9, "cell 6 3 11 1,grow");
		panel9.setLayout(new MigLayout("", "0[50px]0[90px]0[210px,grow]0",
				"0[24px]0"));

		naoEnfRadioButton = new JRadioButton("Não");
		panel9.add(naoEnfRadioButton, "cell 0 0,growx,aligny bottom");
		naoEnfRadioButton.setActionCommand("0");
		enfermidadebuttonGroup.add(naoEnfRadioButton);

		simEnfRadioButton = new JRadioButton("Sim, qual?");
		panel9.add(simEnfRadioButton, "cell 1 0,growx,aligny bottom");
		simEnfRadioButton.setActionCommand("1");
		enfermidadebuttonGroup.add(simEnfRadioButton);

		enfermTextField = new JTextField();
		panel9.add(enfermTextField, "cell 2 0,grow");
		enfermTextField.setColumns(10);

		panel10 = new JPanel();
		panel10.setBorder(new TitledBorder(null, "Destaque 3 pontos fortes:",
				TitledBorder.LEADING, TitledBorder.TOP, null, null));
		socialPanel.add(panel10, "cell 0 6 5 1,grow");
		panel10.setLayout(new MigLayout("", "0[360px,grow]0", "0[90px,grow]0"));
		panel10.add(scrollPane, "cell 0 0,grow");
		iniButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				iniButtonActionPerformed(e);
			}
		});
		espButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				espButtonActionPerformed(e);
			}
		});

		actionPanel = new JPanel();
		getContentPane().add(actionPanel, "cell 0 2,growx,aligny top");

		newButton = new JButton("");
		newButton
				.setIcon(new ImageIcon(getClass().getResource("/img/new.png")));
		newButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent evt) {
				newButtonActionPerformed(evt);
			}
		});
		actionPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		newButton.setPreferredSize(new Dimension(30, 30));
		actionPanel.add(newButton);

		editButton = new JButton("");
		editButton.setIcon(new ImageIcon(getClass()
				.getResource("/img/edit.png")));
		editButton.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				editButtonActionPerformed(evt);
			}
		});
		editButton.setPreferredSize(new Dimension(30, 30));
		actionPanel.add(editButton);

		saveButton = new JButton("");
		saveButton.setIcon(new ImageIcon(getClass()
				.getResource("/img/save.png")));
		saveButton.setPreferredSize(new Dimension(30, 30));
		saveButton.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				saveButtonActionPerformed(evt);
			}
		});
		actionPanel.add(saveButton);

		deleteButton = new JButton("");
		deleteButton.setIcon(new ImageIcon(getClass().getResource(
				"/img/delete.png")));
		deleteButton.setPreferredSize(new Dimension(30, 30));
		deleteButton.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				deleteButtonActionPerformed(evt);
			}
		});
		actionPanel.add(deleteButton);
	}

	@SuppressWarnings("unchecked")
	private void estButtonActionPerformed(ActionEvent e) {
		util.loadTela("cadastro.Estado", "Cadastro de Estado");
		estadoComboBox.setModel(util.initComboBox("estado", new String[] {
				"state", "uf" }, "idestado"));
		estadoCombo = util.initCombo("estado", "idestado");
		estadoComboBox.setSelectedIndex(-1);
	}

	@SuppressWarnings("unchecked")
	private void est2ButtonActionPerformed(ActionEvent e) {
		util.loadTela("cadastro.Estado", "Cadastro de Estado");
		estado2ComboBox.setModel(util.initComboBox("estado", new String[] {
				"state", "uf" }, "idestado"));
		estado2Combo = util.initCombo("estado", "idestado");
		estado2ComboBox.setSelectedIndex(-1);
	}

	@SuppressWarnings("unchecked")
	private void escButtonActionPerformed(ActionEvent e) {
		util.loadTelaCadastro("tipo_curso", new JFrame(),
				"Cadastro de Tipo de Curso");
		grauEscComboBox.setModel(util.initComboBox("tipo_curso",
				new String[] { "tipo" }, "idtcurso"));
		grauEscCombo = util.initCombo("tipo_curso", "idtcurso");
		grauEscComboBox.setSelectedIndex(-1);
	}

	@SuppressWarnings("unchecked")
	private void tipoButtonActionPerformed(ActionEvent e) {
		util.loadTelaCadastro("tipo_curso", new JFrame(),
				"Cadastro de Tipo de Curso");
		tipoCurComboBox.setModel(util.initComboBox("tipo_curso",
				new String[] { "tipo" }, "idtcurso"));
		tipoCurCombo = util.initCombo("tipo_curso", "idtcurso");
		tipoCurComboBox.setSelectedIndex(-1);
	}

	@SuppressWarnings("unchecked")
	private void cidButtonActionPerformed(ActionEvent e) {
		util.loadTela("cadastro.Cidade", "Cadastro de Cidade");
		cidadeComboBox.setModel(util.initComboBox("cidade",
				new String[] { "city" }, "idcidade"));
		cidadeCombo = util.initCombo("cidade", "idcidade");
		cidadeComboBox.setSelectedIndex(-1);
	}

	@SuppressWarnings("unchecked")
	private void cid2ButtonActionPerformed(ActionEvent e) {
		util.loadTela("cadastro.Cidade", "Cadastro de Cidade");
		cidade2ComboBox.setModel(util.initComboBox("cidade",
				new String[] { "city" }, "idcidade"));
		cidade2Combo = util.initCombo("cidade", "idcidade");
		cidade2ComboBox.setSelectedIndex(-1);
	}

	@SuppressWarnings("unchecked")
	private void area1ButtonActionPerformed(ActionEvent e) {
		util.loadTelaCadastro("departamento", new JFrame(),
				"Cadastro de Departamento");
		areaComboBox.setModel(util.initComboBox("departamento",
				new String[] { "depto" }, "iddepto"));
		areaCombo = util.initCombo("departamento", "iddepto");
		areaComboBox.setSelectedIndex(-1);
	}

	@SuppressWarnings("unchecked")
	private void exptButtonActionPerformed(ActionEvent e) {
		util.loadTelaCadastro("tempo_exper", new JFrame(),
				"Cadastro de Experiência");
		exptComboBox.setModel(util.initComboBox("tempo_exper",
				new String[] { "tempo" }, "idtexper"));
		exptCombo = util.initCombo("tempo_exper", "idtexper");
		exptComboBox.setSelectedIndex(-1);
	}

	@SuppressWarnings("unchecked")
	private void espButtonActionPerformed(ActionEvent e) {
		util.loadTelaCadastro("esporte", new JFrame(), "Cadastro de Esporte");
		esporteComboBox.setModel(util.initComboBox("esporte",
				new String[] { "esporte" }, "idesporte"));
		esporteCombo = util.initCombo("esporte", "idesporte");
		esporteComboBox.setSelectedIndex(-1);
	}

	@SuppressWarnings("unchecked")
	private void iniButtonActionPerformed(ActionEvent e) {
		util.loadTelaCadastro("ini_traba", new JFrame(),
				"Cadastro de Possibilidade de Inicio");
		iniciarComboBox.setModel(util.initComboBox("ini_traba",
				new String[] { "iniciar" }, "iditraba"));
		iniciarCombo = util.initCombo("ini_traba", "iditraba");
		iniciarComboBox.setSelectedIndex(-1);
	}

	@SuppressWarnings("unchecked")
	private void ativButtonActionPerformed(ActionEvent e) {
		util.loadTelaCadastro("atividade", new JFrame(),
				"Cadastro de Atividade");
		atividadeComboBox.setModel(util.initComboBox("atividade",
				new String[] { "atividade" }, "idatividade"));
		atividadeCombo = util.initCombo("atividade", "idatividade");
		atividadeComboBox.setSelectedIndex(-1);
	}

	private void newButtonActionPerformed(ActionEvent evt) {
		switch (dadosTabbedPane.getSelectedIndex()) {
		case 1:
			if (!codTextField.getText().isEmpty()) {
				editarExperiencia(true);
				limparExperiencia();
			} else {
				JOptionPane
						.showMessageDialog(null,
								"Favor cadastre primeiroo candidato antes de cadastrar a experiência");
			}
			break;
		case 2:
			if (!codTextField.getText().isEmpty()) {
				editarCurso(true);
				limparCurso();
			} else {
				JOptionPane
						.showMessageDialog(null,
								"Favor cadastre primeiroo candidato antes de cadastrar a experiência");
			}
			break;
		default:
			editarCandidato(true);
			limparCandidato();
			break;
		}
	}

	private void editButtonActionPerformed(ActionEvent evt) {
		switch (dadosTabbedPane.getSelectedIndex()) {
		case 1:
			upd = true;
			editarExperiencia(true);
			break;
		case 2:
			upd = true;
			editarCurso(true);
			break;
		default:
			editarCandidato(true);
			break;
		}
	}

	private void saveButtonActionPerformed(ActionEvent evt) {
		String sql = "";
		int id;
		SQL update = new SQL();
		ArrayList<Object> dt = new ArrayList<>();
		ArrayList<Object> datas = new ArrayList<>();
		switch (dadosTabbedPane.getSelectedIndex()) {
		case 1:
			dt = dadosExperiencia();
			datas.add(util.dateToSQLdate(dt.get(5)));
			datas.add(util.dateToSQLdate(dt.get(6)));
			if (upd) {
				id = (int) experienciaTable.getModel().getValueAt(
						experienciaTable.getSelectedRow(), 10);
				sql = "Update `" + util.getNameBD()
						+ "`.`emprego` Set `empresa`='" + dt.get(0)
						+ "', `gerente`='" + dt.get(1) + "', `cargo`='"
						+ dt.get(2) + "', `salario`='" + dt.get(3)
						+ "', `motivo`='" + dt.get(4) + "', `entrada`=?, "
						+ "`saida`=?, `estado`='" + dt.get(7) + "', `cidade`='"
						+ dt.get(8) + "', `atividade`='" + dt.get(9)
						+ "', `user`='" + util.getUserSis()
						+ "' Where (`idemprego`='" + id
						+ "') and (`candidato`='" + codTextField.getText()
						+ "');";
				if (update.executarSQL(sql, datas)) {
					JOptionPane.showMessageDialog(null,
							"Dados atualizados com sucesso");
				}
			} else {
				sql = "Insert Into `"
						+ util.getNameBD()
						+ "`.`emprego` (`empresa`, `gerente`, `cargo`, `salario`, `motivo`, `entrada`, "
						+ "`saida`, `estado`, `cidade`, `atividade`, `candidato`, `user`) Values ('"
						+ dt.get(0) + "', '" + dt.get(1) + "', '" + dt.get(2)
						+ "', '" + dt.get(3) + "', '" + dt.get(4)
						+ "', ?, ?, '" + dt.get(7) + "', '" + dt.get(8)
						+ "', '" + dt.get(9) + "', '" + codTextField.getText()
						+ "', '" + util.getUserSis() + "');";
				if (update.executarSQL(sql, datas)) {
					JOptionPane.showMessageDialog(null,
							"Dados inseridos com sucesso");
				}
			}
			carregarTabelaExperiencia(update.carregarExperiencia(codTextField
					.getText()));
			upd = false;
			break;
		case 2:
			dt = dadosEscolaridade();
			datas.add(util.dateToSQLdate(dt.get(3)));
			datas.add(util.dateToSQLdate(dt.get(5)));
			if (upd) {
				id = (int) cursosTable.getModel().getValueAt(
						cursosTable.getSelectedRow(), 9);
				sql = "Update `" + util.getNameBD()
						+ "`.`cursos` Set `curso`='" + dt.get(0)
						+ "', `tipo`='" + dt.get(6) + "', `instituicao`='"
						+ dt.get(1) + "', `carga`='" + dt.get(2)
						+ "', `concluido`='" + dt.get(7) + "', `fim`="
						+ "?, `observacao`='" + dt.get(4)
						+ "', `inicio`=?, `diploma`='" + dt.get(8)
						+ "', `user`='" + util.getUserSis()
						+ "' Where (`idcursos` = '" + id
						+ "') and (`candidato` = '" + codTextField.getText()
						+ "');";
				if (update.executarSQL(sql, datas)) {
					JOptionPane.showMessageDialog(null,
							"Dados atualizados com sucesso");
				}
			} else {
				sql = "Insert Into `"
						+ util.getNameBD()
						+ "`.`cursos` (`curso`, `tipo`, `instituicao`, `carga`, `concluido`, `fim`, "
						+ "`observacao`, `inicio`, `diploma`, `user`, `candidato`) Values ('"
						+ dt.get(0) + "', '" + dt.get(6) + "', '" + dt.get(1)
						+ "', '" + dt.get(2) + "', '" + dt.get(7) + "', ?, '"
						+ dt.get(4) + "', ?, '" + dt.get(8) + "', '"
						+ util.getUserSis() + "', '" + codTextField.getText()
						+ "');";
				if (update.executarSQL(sql, datas)) {
					JOptionPane.showMessageDialog(null,
							"Dados inseridos com sucesso");
				}
			}
			carregarTabelaEscolaridade(update.carregarCurso(codTextField
					.getText()));
			upd = false;
			break;
		default:
			index = candidatosTable.getSelectedRow();
			dt = dadosCandidato();
			datas.add(util.dateToSQLdate(dt.get(5)));
			if (!codTextField.getText().trim().isEmpty()) {
				sql = "Update `" + util.getNameBD()
						+ "`.`candidato` Set `nome`='" + dt.get(0)
						+ "', `endereco`='" + dt.get(1) + "', `cep`='"
						+ dt.get(2) + "', `celular`='" + dt.get(3)
						+ "', `fone`='" + dt.get(4) + "', `cpf`='" + dt.get(7)
						+ "', `ctps`='" + dt.get(9) + "', `rg`='" + dt.get(8)
						+ "', `foto`='" + dt.get(35) + "', `obscad`='"
						+ dt.get(10) + "', `localnasc`='" + dt.get(6)
						+ "', `motivo`='" + dt.get(15)
						+ "', `nascimento`=?, `passatempo`='" + dt.get(12)
						+ "', `acidente`='" + dt.get(13) + "', `enfermidade`='"
						+ dt.get(14) + "', `fortes`='" + dt.get(16)
						+ "', `fracos`='" + dt.get(17) + "', `obsvoluntario`='"
						+ dt.get(11) + "', `estado`='" + dt.get(19) + "', "
						+ "`cidade`='" + dt.get(20) + "', `sexo`='"
						+ dt.get(18) + "', `estcivil`='" + dt.get(21)
						+ "', `totalexp`='" + dt.get(23) + "', `area`='"
						+ dt.get(24) + "', `escolaridade`='" + dt.get(22)
						+ "', `esportes`='" + dt.get(25) + "', `iniciar`='"
						+ dt.get(26) + "', `optenfermidade`='" + dt.get(32)
						+ "', `optacidente`='" + dt.get(31)
						+ "', `optesporte`='" + dt.get(28) + "', `outroemp`='"
						+ dt.get(33) + "', `voluntario`='" + dt.get(27)
						+ "', `residencia`='" + dt.get(29) + "', `turno`='"
						+ dt.get(30) + "', `escala`='" + dt.get(34)
						+ "', `user`='" + util.getUserSis()
						+ "' Where `idcandidato`='" + codTextField.getText()
						+ "';";
				if (update.executarSQL(sql, datas)) {
					JOptionPane.showMessageDialog(null,
							"Dados atualizados com sucesso");
				}
			} else {
				sql = "Insert Into `"
						+ util.getNameBD()
						+ "`.`candidato` (`nome`, `endereco`, `cep`, `celular`, `fone`, `cpf`, `ctps`, "
						+ "`rg`, `foto`, `obscad`, `localnasc`, `motivo`, `nascimento`, `passatempo`, "
						+ "`acidente`, `enfermidade`, `fortes`, `fracos`, `obsvoluntario`, `estado`, "
						+ "`cidade`, `sexo`, `estcivil`, `totalexp`, `area`, `escolaridade`, `esportes`, "
						+ "`iniciar`, `optenfermidade`, `optacidente`, `optesporte`, `outroemp`, `voluntario`, "
						+ "`residencia`, `turno`, `escala`, `user`) Values ('"
						+ dt.get(0) + "', '" + dt.get(1) + "', '" + dt.get(2)
						+ "', '" + dt.get(3) + "', '" + dt.get(4) + "', '"
						+ dt.get(7) + "', '" + dt.get(9) + "', '" + dt.get(8)
						+ "', '" + dt.get(35) + "', '" + dt.get(10) + "', '"
						+ dt.get(6) + "', '" + dt.get(15) + "', ?, '"
						+ dt.get(12) + "', '" + dt.get(13) + "', '"
						+ dt.get(14) + "', '" + dt.get(16) + "', '"
						+ dt.get(17) + "', '" + dt.get(11) + "', '"
						+ dt.get(19) + "', '" + dt.get(20) + "', '"
						+ dt.get(18) + "', '" + dt.get(21) + "', '"
						+ dt.get(23) + "', '" + dt.get(24) + "', '"
						+ dt.get(22) + "', '" + dt.get(25) + "', '"
						+ dt.get(26) + "', '" + dt.get(32) + "', '"
						+ dt.get(31) + "', '" + dt.get(28) + "', '"
						+ dt.get(33) + "', '" + dt.get(27) + "', '"
						+ dt.get(29) + "', '" + dt.get(30) + "', '"
						+ dt.get(34) + "', '" + util.getUserSis() + "');";
				if (update.executarSQL(sql, datas)) {
					JOptionPane.showMessageDialog(null,
							"Dados inseridos com sucesso");
				}
			}
			carregarTabelaPrincipal(update.carregarCadastroCandidato());
			if (index >= 0) {
				carregarRegistroPrincipal(index);
			} else {
				total = candidatosTable.getRowCount() - 1;
				carregarRegistroPrincipal(total);
			}
			break;
		}
		editar(false);
		update.close();
	}

	private void deleteButtonActionPerformed(ActionEvent evt) {
		boolean valido = false;
		Object[] opcoes = { "Não", "Sim", "Cancelar" };
		int contU = 0, contP = 0;
		SQL update = new SQL();
		ResultSet rs;
		int confirmar = JOptionPane.showOptionDialog(null,
				"Você realmente deseja apagar esse registro?", null, 1, 1,
				null, opcoes, 0);
		if (confirmar == 1) {
			String user = "", pws = "";
			while (!valido) {
				if (user.trim().isEmpty()) {
					user = JOptionPane
							.showInputDialog("Qual o seu código de funcionário?");
					++contU;
					if (contU > 2) {
						if (JOptionPane.showConfirmDialog(null,
								"Deseja desistir de apagar o registro?") == 0) {
							valido = true;
						} else {
							contU = 0;
						}
					}
				} else {
					if (pws.trim().isEmpty()) {
						pws = JOptionPane.showInputDialog("Qual a sua senha?");
						++contP;
						if (contP > 2) {
							if (JOptionPane.showConfirmDialog(null,
									"Deseja desistir de apagar o registro?") == 0) {
								valido = true;
							} else {
								contP = 0;
							}
						}
					}
				}
				if (!user.trim().isEmpty() && !pws.trim().isEmpty()) {
					try {
						rs = update.checarUsuario(user, pws);
						if (rs.next()) {
							if (rs.getInt("nivel") <= levelTab) {
								String sql = "";
								switch (dadosTabbedPane.getSelectedIndex()) {
								case 1:
									if (experienciaTable.getRowCount() > 0) {
										int j = experienciaTable
												.getSelectedRow();
										if (j > -1) {
											int exp = (int) experienciaTable
													.getModel().getValueAt(j,
															10);
											sql = "Update `" + util.getNameBD()
													+ "`.`emprego` Set ";
											sql += "`active`='0', `user`='"
													+ rs.getInt("idusuario")
													+ "' ";
											sql += "Where (`candidato` = '"
													+ codTextField.getText()
													+ "') and (`idemprego`='"
													+ exp + "');";
											if (!sql.trim().isEmpty()) {
												update.executarSQL(sql);
											}
											carregarTabelaExperiencia(update
													.carregarExperiencia(codTextField
															.getText()));
											carregarRegistroExperiencia(((j - 1) > 0) ? (j - 1)
													: 0);
											editar(false);
											update.close();
											valido = true;
											JOptionPane
													.showMessageDialog(null,
															"Registro apagado com sucesso.");
										} else {
											JOptionPane
													.showMessageDialog(null,
															"Favor selecione um emprego a excluir");
										}
									} else {
										JOptionPane
												.showMessageDialog(null,
														"Não há experiências cadastradas para esse candidato a excluir");
									}
									break;
								case 2:
									if (cursosTable.getRowCount() > 0) {
										int j = cursosTable.getSelectedRow();
										if (j > -1) {
											int curso = (int) cursosTable
													.getModel()
													.getValueAt(j, 9);
											sql = "Update `" + util.getNameBD()
													+ "`.`cursos` Set ";
											sql += "`active`='0', `user`='"
													+ rs.getInt("idusuario")
													+ "' ";
											sql += "Where (`candidato`='"
													+ codTextField.getText()
													+ "') and (`idcursos`='"
													+ curso + "');";
											if (!sql.trim().isEmpty()) {
												update.executarSQL(sql);
											}
											carregarTabelaEscolaridade(update
													.carregarCurso(codTextField
															.getText()));
											carregarRegistroEscolaridade(((j - 1) > 0) ? (j - 1)
													: 0);
											editar(false);
											update.close();
											valido = true;
											JOptionPane
													.showMessageDialog(null,
															"Registro apagado com sucesso.");
										} else {
											JOptionPane
													.showMessageDialog(null,
															"Favor selecione um curso a excluir");
										}
									} else {
										JOptionPane
												.showMessageDialog(null,
														"Não há cursos cadastrados para esse candidato");
									}
									break;
								default:
									if (!codTextField.getText().trim()
											.isEmpty()) {
										sql = "Update `" + util.getNameBD()
												+ "`.`candidato` Set ";
										sql += "`active`='0', `user`='"
												+ rs.getInt("idusuario") + "' ";
										sql += "Where `idcandidato`='"
												+ codTextField.getText() + "';";
										if (!sql.trim().isEmpty()) {
											update.executarSQL(sql);
										}
										carregarTabelaPrincipal(update
												.carregarCadastroCandidato());
										if (index < 1) {
											total = candidatosTable
													.getRowCount() - 1;
											carregarRegistroPrincipal(total);
										} else {
											--index;
											carregarRegistroPrincipal(index);
										}
										editar(false);
										update.close();
										valido = true;
										JOptionPane
												.showMessageDialog(null,
														"Registro apagado com sucesso.");
									}
									break;
								}
							} else {
								user = "";
								pws = "";
								if (JOptionPane
										.showConfirmDialog(
												null,
												"O usuário informado não possui privilégios para deletar esse registro.\nDeseja desistir de apagar o registro?") == 0) {
									valido = true;
								} else {
									contP = 0;
									contU = 0;
								}
							}
						} else {
							user = "";
							pws = "";
							if (JOptionPane
									.showConfirmDialog(null,
											"Usuário e/ou senha inválido.\nDeseja desistir de apagar o registro?") == 0) {
								valido = true;
							} else {
								contP = 0;
								contU = 0;
							}
						}
						rs.close();
					} catch (SQLException ex) {
						Logger.getLogger(Curriculum.class.getName()).log(
								Level.SEVERE, null, ex);
					}
				}
			}
		}
	}

	private void codTextFieldKeyReleased(KeyEvent evt) {
		if (codTextField.isEnabled()) {
			SQL sql = new SQL();
			carregarTabelaPrincipal(sql
					.localizarCandidato("idcandidato like \""
							+ codTextField.getText() + "%\""));
			sql.close();
			index = 0;
			total = candidatosTable.getRowCount() - 1;
			carregarRegistroPrincipal(index);
		}
	}

	private void findButtonActionPerformed(ActionEvent evt) {
		editar(true);
		limpar();
		codTextField.setEnabled(true);
		cpfConsFormattedTextField.setEnabled(true);
	}

	private void firstButtonActionPerformed(ActionEvent evt) {
		limpar();
		index = 0;
		carregarRegistroPrincipal(index);
		SQL sql = new SQL();
		carregarTabelaExperiencia(sql.carregarExperiencia(codTextField
				.getText()));
		carregarTabelaEscolaridade(sql.carregarCurso(codTextField.getText()));
		sql.close();
		editar(false);
	}

	private void beforeButtonActionPerformed(ActionEvent evt) {
		if (index > 0) {
			limpar();
			carregarRegistroPrincipal(--index);
			SQL sql = new SQL();
			carregarTabelaExperiencia(sql.carregarExperiencia(codTextField
					.getText()));
			carregarTabelaEscolaridade(sql
					.carregarCurso(codTextField.getText()));
			sql.close();
		} else {
			firstButtonActionPerformed(evt);
		}
		editar(false);
	}

	private void nextButtonActionPerformed(ActionEvent evt) {
		if (index < total) {
			limpar();
			carregarRegistroPrincipal(++index);
			SQL sql = new SQL();
			carregarTabelaExperiencia(sql.carregarExperiencia(codTextField
					.getText()));
			carregarTabelaEscolaridade(sql
					.carregarCurso(codTextField.getText()));
			sql.close();
		} else {
			lastButtonActionPerformed(evt);
		}
		editar(false);
	}

	private void lastButtonActionPerformed(ActionEvent evt) {
		limpar();
		total = candidatosTable.getRowCount() - 1;
		index = total;
		carregarRegistroPrincipal(index);
		SQL sql = new SQL();
		carregarTabelaExperiencia(sql.carregarExperiencia(codTextField
				.getText()));
		carregarTabelaEscolaridade(sql.carregarCurso(codTextField.getText()));
		sql.close();
		editar(false);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void printButtonActionPerformed(ActionEvent evt) {
		try {
			// gerando o jasper design
			JasperDesign designPrincipal = JRXmlLoader
					.load("relatorios/selecao/Curriculum.jrxml");
			// compila o relatório
			JasperReport relatorio = JasperCompileManager
					.compileReport(designPrincipal);
			// estabelece conexão
			SQL query = new SQL();
			ResultSet rs = query.carregarRelatorioExperiencia(codTextField
					.getText());
			Map parametros = new HashMap();
			parametros.put("logo", util.getConf("logo"));
			parametros.put("subCurso", "relatorios/selecao/Cursos.jasper");
			parametros.put(
					"consCurso",
					new JRResultSetDataSource(new SQL()
							.carregarRelatorioCurso(codTextField.getText())));
			parametros.put("subLaudo", "relatorios/selecao/Laudo.jasper");
			parametros.put(
					"consLaudo",
					new JRResultSetDataSource(new SQL()
							.carregarRelatorioLaudo(" and (s.`candidato`='"
									+ codTextField.getText() + "')")));
			int cont = 1;
			while (rs.next()) {
				parametros.put("empresa" + cont, rs.getString("empresa"));
				parametros.put("gerente" + cont, rs.getString("gerente"));
				parametros.put("cargo" + cont, rs.getString("cargo"));
				parametros.put("salario" + cont, rs.getDouble("salario"));
				parametros.put("motivo" + cont, rs.getString("motivo"));
				parametros.put("entrada" + cont, rs.getTimestamp("entrada"));
				parametros.put("saida" + cont, rs.getTimestamp("saida"));
				parametros.put("uf" + cont, rs.getString("uf"));
				parametros.put("cidade" + cont, rs.getString("cidade"));
				++cont;
			}
			String[] linha;
			String escala = "";
			rs = query.query("Select `escala` From `" + util.getNameBD()
					+ "`.`candidato` Where (`active`='1') and (`idcandidato`='"
					+ codTextField.getText() + "');");
			while (rs.next()) {
				linha = rs.getString("escala").split(":");
				escala += linha[0].trim().isEmpty() ? "" : "Semanal, ";
				escala += linha[1].trim().isEmpty() ? "" : "Fim de semana, ";
				escala += linha[2].trim().isEmpty() ? "" : "12 por 36, ";
				escala += linha[3].trim().isEmpty() ? "" : "Somente tarde, ";
				escala += linha[4].trim().isEmpty() ? "" : "Somente manhã, ";
			}
			escala = escala.substring(0, escala.lastIndexOf(", ")) + ".";
			parametros.put("escala", escala);
			rs = query.carregarRelatorioCandidato(codTextField.getText());

			// implementação da interface JRDataSource para DataSource
			// ResultSet
			// JRResultSetDataSource jrRS = new JRResultSetDataSource(rs);
			JRBeanCollectionDataSource jrRS = new JRBeanCollectionDataSource(
					relatorio(), false);

			// executa o relatório
			JasperPrint impressao = JasperFillManager.fillReport(relatorio,
					parametros, jrRS);

			// exibe o resultado
			JasperExportManager.exportReportToPdfFile(impressao,
					"relatorios/Relatorio.pdf");
			Desktop.getDesktop().open(new File("relatorios/Relatorio.pdf"));
			editar(false);
			rs.close();
			query.close();
		} catch (JRException | IOException ex) {
			Logger.getLogger(Curriculum.class.getName()).log(Level.SEVERE,
					null, ex);
		} catch (SQLException ex) {
			Logger.getLogger(Curriculum.class.getName()).log(Level.SEVERE,
					null, ex);
		}
	}

	private ArrayList<?> relatorio() {
		ArrayList<Object> report = new ArrayList<>();
		CurriculoReport dados = new CurriculoReport();
		dados.setNome(nomeTextField.getText());
		dados.setEndereco(enderecoTextField.getText());
		dados.setCelular(celFormattedTextField.getText());
		dados.setFone(foneFormattedTextField.getText());
		dados.setNascimento(util.strToDate(nascDatePicker
				.getJFormattedTextField().getText()));
		dados.setLocalnasc(localTextField.getText());
		dados.setCpf(util.getAlfaNum(cpfFormattedTextField.getText()));
		dados.setRg(rgTextField.getText());
		dados.setCtps(ctpsTextField.getText());
		dados.setObscad(obsCadTextArea.getText());
		dados.setObsvoluntario(voluntarioTextField.getText());
		dados.setPassatempo(pasTempTextField.getText());
		dados.setAcidente(acidenteTextField.getText());
		dados.setEnfermidade(enfermTextField.getText());
		dados.setMotivo(motEsTextField.getText());
		dados.setFortes(forteTextArea.getText());
		dados.setFracos(fracoTextArea.getText());
		dados.setSexo((String) sexoComboBox.getSelectedItem());
		dados.setUf(((String) estadoComboBox.getSelectedItem()).substring(0, 2));
		dados.setCity((String) cidadeComboBox.getSelectedItem());
		dados.setCivil((String) estCivilComboBox.getSelectedItem());
		dados.setInstr((String) grauEscComboBox.getSelectedItem());
		dados.setTotalexp((String) exptComboBox.getSelectedItem());
		dados.setArea((String) areaComboBox.getSelectedItem());
		dados.setEsporte((String) esporteComboBox.getSelectedItem());
		dados.setIniciar((String) iniciarComboBox.getSelectedItem());
		try {
			dados.setVoluntario(Integer.parseInt(voluntariobuttonGroup
					.getSelection().getActionCommand()));
		} catch (NullPointerException ex) {
		}
		try {
			dados.setResidencia(Integer.parseInt(residenciabuttonGroup
					.getSelection().getActionCommand()));
		} catch (NullPointerException ex) {
		}
		try {
			dados.setTurno(Integer.parseInt(turnobuttonGroup.getSelection()
					.getActionCommand()));
		} catch (NullPointerException ex) {
		}
		try {
			dados.setAcidente(acidentebuttonGroup.getSelection()
					.getActionCommand());
		} catch (NullPointerException ex) {
		}
		try {
			dados.setOutroemp(Integer.parseInt(outroEmpregobuttonGroup
					.getSelection().getActionCommand()));
		} catch (NullPointerException ex) {
		}
		dados.setFoto(foto);
		dados.setIdade((long) util.getIdade(util.strToDate(nascDatePicker
				.getJFormattedTextField().getText())));
		report.add(dados);
		return report;
	}

	private void fotoButtonActionPerformed(ActionEvent evt) {
		String cpf = util.getAlfaNum(cpfFormattedTextField.getText());
		if (cpf.trim().isEmpty()) {
			JOptionPane.showMessageDialog(null,
					"Favor preencha o campo CPF antes de carregar a foto");
			cpfFormattedTextField.requestFocus();
		} else {
			foto = util.getConf("foto") + cpf;

			String ext;
			JFileChooser fileChooser = new JFileChooser();
			fileChooser.setAcceptAllFileFilterUsed(false);
			fileChooser.addChoosableFileFilter(new FileNameExtensionFilter(
					"arquivos de imagem", new String[] { "jpg", "jpge", "png",
							"gif" }));
			fileChooser.addChoosableFileFilter(new FileNameExtensionFilter(
					"arquivos .jpg", new String[] { "jpg", "jpge" }));
			fileChooser.addChoosableFileFilter(new FileNameExtensionFilter(
					"arquivos .png", "png"));
			fileChooser.addChoosableFileFilter(new FileNameExtensionFilter(
					"arquivos .gif", "gif"));

			fileChooser.setCurrentDirectory(new File("."));
			int result = fileChooser.showOpenDialog(null);
			if (result == JFileChooser.APPROVE_OPTION) {

				File file = fileChooser.getSelectedFile();

				if (file != null) {
					ext = fileChooser.getSelectedFile().getAbsolutePath();
					System.out.println(ext);
					ext = ext.substring(ext.lastIndexOf(".") + 1);
					foto += "." + ext;
					fotoRezise(file, ext, true);
				}
			}
		}
	}

	private void fotoRezise(File file, String ext, Boolean save) {
		try {
			BufferedImage tmp;
			tmp = ImageIO.read(file);
			BufferedImage newImage;
			int w = fotoLabel.getWidth();
			int h = fotoLabel.getHeight();
			newImage = new BufferedImage(w, h, tmp.getType());
			Graphics2D g2d = newImage.createGraphics();
			g2d.setComposite(AlphaComposite.Src);
			g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
					RenderingHints.VALUE_INTERPOLATION_BILINEAR);
			g2d.setRenderingHint(RenderingHints.KEY_RENDERING,
					RenderingHints.VALUE_RENDER_QUALITY);
			g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
					RenderingHints.VALUE_ANTIALIAS_ON);
			g2d.drawImage(tmp, 0, 0, w, h, null);
			g2d.dispose();
			if (save) {
				ImageIO.write(newImage, ext, new File(foto));
			}
			fotoLabel.setIcon(new ImageIcon(newImage));
		} catch (IOException ex) {
			Logger.getLogger(Curriculum.class.getName()).log(Level.SEVERE,
					null, ex);
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void estadoComboBoxItemStateChanged(ItemEvent evt) {
		try {
			if (estadoComboBox.getSelectedIndex() > -1) {
				int cont = 0;
				SQL sql = new SQL();
				ResultSet rs = sql.localizarCidade("idestado = \""
						+ estadoCombo.get(estadoComboBox.getSelectedIndex())
						+ "\"");
				DefaultComboBoxModel modelo = new DefaultComboBoxModel();
				while (rs.next()) {
					modelo.addElement(rs.getString("city"));
					cidadeCombo.put(cont++, rs.getInt("codigo"));
				}
				cidadeComboBox.setModel(modelo);
				cidadeComboBox.setSelectedIndex(-1);
				rs.close();
				sql.close();
			}
		} catch (SQLException ex) {
			Logger.getLogger(Curriculum.class.getName()).log(Level.SEVERE,
					null, ex);
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void estado2ComboBoxItemStateChanged(ItemEvent evt) {
		try {
			if (estado2ComboBox.getSelectedIndex() > -1) {
				int cont = 0;
				SQL sql = new SQL();
				ResultSet rs = sql.localizarCidade("idestado = \""
						+ estado2Combo.get(estadoComboBox.getSelectedIndex())
						+ "\"");
				DefaultComboBoxModel modelo = new DefaultComboBoxModel();
				while (rs.next()) {
					modelo.addElement(rs.getString("city"));
					cidade2Combo.put(cont++, rs.getInt("codigo"));
				}
				cidade2ComboBox.setModel(modelo);
				cidade2ComboBox.setSelectedIndex(-1);
				rs.close();
				sql.close();
			}
		} catch (SQLException ex) {
			Logger.getLogger(Curriculum.class.getName()).log(Level.SEVERE,
					null, ex);
		}
	}

	private void cpfConsFormattedTextFieldKeyReleased(KeyEvent evt) {
		if (cpfConsFormattedTextField.isEnabled()) {
			String cpf = util.getAlfaNum(cpfConsFormattedTextField.getText());
			SQL sql = new SQL();
			carregarTabelaPrincipal(sql.localizarCandidato("cpf like \"" + cpf
					+ "%\""));
			sql.close();
			index = 0;
			total = candidatosTable.getRowCount() - 1;
			carregarRegistroPrincipal(index);
		}
	}

	private void dadosTabbedPaneStateChanged(ChangeEvent evt) {
		switch (dadosTabbedPane.getSelectedIndex()) {
		case 1:
			if (codTextField.getText().trim().isEmpty()) {
				dadosTabbedPane.setSelectedIndex(0);
				JOptionPane.showMessageDialog(null,
						"Favor cadastrar primeiro um candidato");
			} else {
				SQL sql = new SQL();
				carregarTabelaExperiencia(sql.carregarExperiencia(codTextField
						.getText()));
				sql.close();
			}
			break;
		case 2:
			if (codTextField.getText().trim().isEmpty()) {
				dadosTabbedPane.setSelectedIndex(0);
				JOptionPane.showMessageDialog(null,
						"Favor cadastrar primeiro um candidato");
			} else {
				SQL sql = new SQL();
				carregarTabelaEscolaridade(sql.carregarCurso(codTextField
						.getText()));
				sql.close();
			}
			break;
		}
	}

	private void cursosTableMouseClicked(MouseEvent evt) {
		carregarRegistroEscolaridade(cursosTable.getSelectedRow());
	}

	private void experienciaTableMouseClicked(MouseEvent evt) {
		carregarRegistroExperiencia(experienciaTable.getSelectedRow());
	}

	private void editarCandidato(boolean b) {
		nomeTextField.setEditable(b);
		enderecoTextField.setEditable(b);
		cepFormattedTextField.setEditable(b);
		celFormattedTextField.setEditable(b);
		foneFormattedTextField.setEditable(b);
		nascDatePicker.getJFormattedTextField().setEditable(b);
		nascDatePicker.getJButton().setEnabled(b);
		localTextField.setEditable(b);
		cpfFormattedTextField.setEditable(b);
		rgTextField.setEditable(b);
		ctpsTextField.setEditable(b);
		obsCadTextArea.setEditable(b);
		voluntarioTextField.setEditable(b);
		pasTempTextField.setEditable(b);
		acidenteTextField.setEditable(b);
		enfermTextField.setEditable(b);
		motEsTextField.setEditable(b);
		forteTextArea.setEditable(b);
		fracoTextArea.setEditable(b);

		fotoButton.setEnabled(b);
		estButton.setEnabled(b);
		cidButton.setEnabled(b);
		escButton.setEnabled(b);
		exptButton.setEnabled(b);
		areaButton.setEnabled(b);
		espButton.setEnabled(b);
		iniButton.setEnabled(b);

		sexoComboBox.setEnabled(b);
		estadoComboBox.setEnabled(b);
		cidadeComboBox.setEnabled(b);
		estCivilComboBox.setEnabled(b);
		grauEscComboBox.setEnabled(b);
		exptComboBox.setEnabled(b);
		areaComboBox.setEnabled(b);
		esporteComboBox.setEnabled(b);
		iniciarComboBox.setEnabled(b);

		simVolRadioButton.setEnabled(b);
		naoVolRadioButton.setEnabled(b);
		simEspRadioButton.setEnabled(b);
		naoEspRadioButton.setEnabled(b);
		resPropRadioButton.setEnabled(b);
		resAlugRadioButton.setEnabled(b);
		turDiuRadioButton.setEnabled(b);
		turNotRadioButton.setEnabled(b);
		turAmbRadioButton.setEnabled(b);
		simAciRadioButton.setEnabled(b);
		naoAciRadioButton.setEnabled(b);
		simEnfRadioButton.setEnabled(b);
		naoEnfRadioButton.setEnabled(b);
		simAguRadioButton.setEnabled(b);
		naoAguRadioButton.setEnabled(b);

		escSemCheckBox.setEnabled(b);
		escFimCheckBox.setEnabled(b);
		esc1236CheckBox.setEnabled(b);
		escTarCheckBox.setEnabled(b);
		escManCheckBox.setEnabled(b);
	}

	private void editarExperiencia(boolean b) {
		empTextField.setEditable(b);
		gerenteTextField.setEditable(b);
		cargoTextField.setEditable(b);
		salarioFormattedTextField.setEditable(b);
		motivoTextArea.setEditable(b);
		entradaDatePicker.getJFormattedTextField().setEditable(b);
		entradaDatePicker.getJButton().setEnabled(b);
		saidaDatePicker.getJFormattedTextField().setEditable(b);
		saidaDatePicker.getJButton().setEnabled(b);

		estado2ComboBox.setEnabled(b);
		cidade2ComboBox.setEnabled(b);
		atividadeComboBox.setEnabled(b);
		est2Button.setEnabled(b);
		cid2Button.setEnabled(b);
		ativButton.setEnabled(b);
	}

	private void editarCurso(boolean b) {
		cursoTextField.setEditable(b);
		instTextField.setEditable(b);
		cargaHoTextField.setEditable(b);
		fimCurDatePicker.getJFormattedTextField().setEditable(b);
		fimCurDatePicker.getJButton().setEnabled(b);
		obsCurTextArea.setEditable(b);
		inicioCurDatePicker.getJFormattedTextField().setEditable(b);
		inicioCurDatePicker.getJButton().setEnabled(b);
		tipoCurComboBox.setEnabled(b);
		simConRadioButton.setEnabled(b);
		naoConRadioButton.setEnabled(b);
		simDipRadioButton.setEnabled(b);
		naoDipRadioButton.setEnabled(b);
		tipoButton.setEnabled(b);
	}

	private void editar(boolean b) {
		editarCandidato(b);
		editarExperiencia(b);
		editarCurso(b);
	}

	private void limparCandidato() {
		codTextField.setText("");
		nomeTextField.setText("");
		enderecoTextField.setText("");
		cepFormattedTextField.setText("");
		celFormattedTextField.setText("");
		foneFormattedTextField.setText("");
		nascDatePicker.getModel().setValue(null);
		localTextField.setText("");
		cpfFormattedTextField.setText("");
		rgTextField.setText("");
		ctpsTextField.setText("");
		obsCadTextArea.setText("");
		voluntarioTextField.setText("");
		pasTempTextField.setText("");
		acidenteTextField.setText("");
		enfermTextField.setText("");
		motEsTextField.setText("");
		forteTextArea.setText("");
		fracoTextArea.setText("");

		try {
			foto = util.getConf("foto") + "nobody.png";
			fotoLabel.setIcon(new ImageIcon(ImageIO.read(new File(foto))));
		} catch (IOException ex1) {
			Logger.getLogger(Curriculum.class.getName()).log(Level.SEVERE,
					null, ex1);
		}

		sexoComboBox.setSelectedIndex(-1);
		estadoComboBox.setSelectedIndex(-1);
		cidadeComboBox.setSelectedIndex(-1);
		estCivilComboBox.setSelectedIndex(-1);
		grauEscComboBox.setSelectedIndex(-1);
		exptComboBox.setSelectedIndex(-1);
		areaComboBox.setSelectedIndex(-1);
		esporteComboBox.setSelectedIndex(-1);
		iniciarComboBox.setSelectedIndex(-1);

		voluntariobuttonGroup.clearSelection();
		esportesbuttonGroup.clearSelection();
		residenciabuttonGroup.clearSelection();
		turnobuttonGroup.clearSelection();
		acidentebuttonGroup.clearSelection();
		enfermidadebuttonGroup.clearSelection();
		outroEmpregobuttonGroup.clearSelection();

		escSemCheckBox.setSelected(false);
		escFimCheckBox.setSelected(false);
		esc1236CheckBox.setSelected(false);
		escTarCheckBox.setSelected(false);
		escManCheckBox.setSelected(false);
	}

	private void limparExperiencia() {
		estado2ComboBox.setSelectedIndex(-1);
		cidade2ComboBox.setSelectedIndex(-1);
		atividadeComboBox.setSelectedIndex(-1);
		empTextField.setText("");
		gerenteTextField.setText("");
		cargoTextField.setText("");
		salarioFormattedTextField.setText("");
		motivoTextArea.setText("");
		entradaDatePicker.setText("");
		saidaDatePicker.setText("");
	}

	private void limparCurso() {
		cursoTextField.setText("");
		instTextField.setText("");
		cargaHoTextField.setText("");
		fimCurDatePicker.setText("");
		obsCurTextArea.setText("");
		inicioCurDatePicker.setText("");
		tipoCurComboBox.setSelectedIndex(-1);
		cursoConclbuttonGroup.clearSelection();
		diplomabuttonGroup.clearSelection();
	}

	private void limpar() {
		limparCandidato();
		limparExperiencia();
		limparCurso();
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void carregarTabelaPrincipal(ResultSet candidato) {
		try {
			Vector<String> tableHeaders = new Vector<String>();
			Vector tableData = new Vector();
			tableHeaders.add("idcandidato");
			tableHeaders.add("nome");
			tableHeaders.add("endereco");
			tableHeaders.add("cep");
			tableHeaders.add("celular");
			tableHeaders.add("fone");
			tableHeaders.add("nascimento");
			tableHeaders.add("localnasc");
			tableHeaders.add("cpf");
			tableHeaders.add("rg");
			tableHeaders.add("ctps");
			tableHeaders.add("obscad");
			tableHeaders.add("obsvoluntario");
			tableHeaders.add("passatempo");
			tableHeaders.add("acidente");
			tableHeaders.add("enfermidade");
			tableHeaders.add("motivo");
			tableHeaders.add("fortes");
			tableHeaders.add("fracos");
			tableHeaders.add("foto");

			tableHeaders.add("sexo");
			tableHeaders.add("estado");
			tableHeaders.add("cidade");
			tableHeaders.add("estcivil");
			tableHeaders.add("escolaridade");
			tableHeaders.add("totalexp");
			tableHeaders.add("area");
			tableHeaders.add("esportes");
			tableHeaders.add("iniciar");

			tableHeaders.add("voluntario");
			tableHeaders.add("optesporte");
			tableHeaders.add("residencia");
			tableHeaders.add("turno");
			tableHeaders.add("optacidente");
			tableHeaders.add("optenfermidade");
			tableHeaders.add("outroemp");

			tableHeaders.add("escala");
			while (candidato.next()) {
				Vector<Object> oneRow = new Vector<Object>();
				oneRow.add(candidato.getString("idcandidato"));
				oneRow.add(candidato.getString("nome"));
				oneRow.add(candidato.getString("endereco"));
				oneRow.add(candidato.getString("cep"));
				oneRow.add(candidato.getString("celular"));
				oneRow.add(candidato.getString("fone"));
				oneRow.add(candidato.getString("nascimento"));
				oneRow.add(candidato.getString("localnasc"));
				oneRow.add(candidato.getString("cpf"));
				oneRow.add(candidato.getString("rg"));
				oneRow.add(candidato.getString("ctps"));
				oneRow.add(candidato.getString("obscad"));
				oneRow.add(candidato.getString("obsvoluntario"));
				oneRow.add(candidato.getString("passatempo"));
				oneRow.add(candidato.getString("acidente"));
				oneRow.add(candidato.getString("enfermidade"));
				oneRow.add(candidato.getString("motivo"));
				oneRow.add(candidato.getString("fortes"));
				oneRow.add(candidato.getString("fracos"));
				oneRow.add(candidato.getString("foto"));

				oneRow.add(candidato.getInt("sexo"));
				oneRow.add(candidato.getInt("estado"));
				oneRow.add(candidato.getInt("cidade"));
				oneRow.add(candidato.getInt("estcivil"));
				oneRow.add(candidato.getInt("escolaridade"));
				oneRow.add(candidato.getInt("totalexp"));
				oneRow.add(candidato.getInt("area"));
				oneRow.add(candidato.getInt("esportes"));
				oneRow.add(candidato.getInt("iniciar"));

				oneRow.add(candidato.getInt("voluntario"));
				oneRow.add(candidato.getInt("optesporte"));
				oneRow.add(candidato.getInt("residencia"));
				oneRow.add(candidato.getInt("turno"));
				oneRow.add(candidato.getInt("optacidente"));
				oneRow.add(candidato.getInt("optenfermidade"));
				oneRow.add(candidato.getInt("outroemp"));

				oneRow.add(candidato.getString("escala"));
				levelTab = candidato.getInt("level");
				tableData.add(oneRow);
			}
			candidatosTable.setModel(new DefaultTableModel(tableData,
					tableHeaders));
			candidato.close();
		} catch (SQLException ex) {
			Logger.getLogger(Curriculum.class.getName()).log(Level.SEVERE,
					null, ex);
		}
	}

	private void carregarRegistroPrincipal(int cod) {
		if (cod >= 0) {
			limparCandidato();
			DefaultTableModel tabela = (DefaultTableModel) candidatosTable
					.getModel();
			codTextField.setText((String) tabela.getValueAt(cod, 0));
			nomeTextField.setText((String) tabela.getValueAt(cod, 1));
			enderecoTextField.setText((String) tabela.getValueAt(cod, 2));
			cepFormattedTextField.setText((String) tabela.getValueAt(cod, 3));
			celFormattedTextField.setText((String) tabela.getValueAt(cod, 4));
			foneFormattedTextField.setText((String) tabela.getValueAt(cod, 5));
			nascDatePicker.getJFormattedTextField().setText(
					(String) tabela.getValueAt(cod, 6));
			localTextField.setText((String) tabela.getValueAt(cod, 7));
			cpfFormattedTextField.setText((String) tabela.getValueAt(cod, 8));
			rgTextField.setText((String) tabela.getValueAt(cod, 9));
			ctpsTextField.setText((String) tabela.getValueAt(cod, 10));
			obsCadTextArea.setText((String) tabela.getValueAt(cod, 11));
			voluntarioTextField.setText((String) tabela.getValueAt(cod, 12));
			pasTempTextField.setText((String) tabela.getValueAt(cod, 13));
			acidenteTextField.setText((String) tabela.getValueAt(cod, 14));
			enfermTextField.setText((String) tabela.getValueAt(cod, 15));
			motEsTextField.setText((String) tabela.getValueAt(cod, 16));
			forteTextArea.setText((String) tabela.getValueAt(cod, 17));
			fracoTextArea.setText((String) tabela.getValueAt(cod, 18));
			try {
				foto = (String) tabela.getValueAt(cod, 19);
				fotoLabel.setIcon(new ImageIcon(ImageIO.read(new File(foto))));
			} catch (IOException ex) {
				Logger.getLogger(Curriculum.class.getName()).log(Level.SEVERE,
						null, ex);
				try {
					foto = util.getConf("foto") + "nobody.png";
					fotoLabel.setIcon(new ImageIcon(ImageIO
							.read(new File(foto))));
				} catch (IOException ex1) {
					Logger.getLogger(Curriculum.class.getName()).log(
							Level.SEVERE, null, ex1);
				}
			}
			sexoComboBox.setSelectedIndex((int) tabela.getValueAt(cod, 20) - 1);
			estadoComboBox
					.setSelectedIndex((int) tabela.getValueAt(cod, 21) - 1);
			try {
				ResultSet rs = new SQL().localizarCidade("idcidade='"
						+ (int) tabela.getValueAt(cod, 22) + "'");
				rs.next();
				cidadeComboBox.setSelectedItem(rs.getString("city"));
			} catch (SQLException ex) {
			}
			estCivilComboBox
					.setSelectedIndex((int) tabela.getValueAt(cod, 23) - 1);
			grauEscComboBox
					.setSelectedIndex((int) tabela.getValueAt(cod, 24) - 1);
			exptComboBox.setSelectedIndex((int) tabela.getValueAt(cod, 25) - 1);
			areaComboBox.setSelectedIndex((int) tabela.getValueAt(cod, 26) - 1);
			esporteComboBox
					.setSelectedIndex((int) tabela.getValueAt(cod, 27) - 1);
			iniciarComboBox
					.setSelectedIndex((int) tabela.getValueAt(cod, 28) - 1);

			int op = (int) tabela.getValueAt(cod, 29);
			simVolRadioButton.setSelected(op > 0);
			naoVolRadioButton.setSelected(op == 0);
			op = (int) tabela.getValueAt(cod, 30);
			simEspRadioButton.setSelected(op > 0);
			naoEspRadioButton.setSelected(op == 0);
			op = (int) tabela.getValueAt(cod, 31);
			resAlugRadioButton.setSelected(op > 0);
			resPropRadioButton.setSelected(op == 0);
			switch ((int) tabela.getValueAt(cod, 32)) {
			case 0:
				turDiuRadioButton.setSelected(true);
				break;
			case 1:
				turNotRadioButton.setSelected(true);
				break;
			case 2:
				turAmbRadioButton.setSelected(true);
				break;
			}
			op = (int) tabela.getValueAt(cod, 33);
			simAciRadioButton.setSelected(op > 0);
			naoAciRadioButton.setSelected(op == 0);
			op = (int) tabela.getValueAt(cod, 34);
			simEnfRadioButton.setSelected(op > 0);
			naoEnfRadioButton.setSelected(op == 0);
			op = (int) tabela.getValueAt(cod, 35);
			simAguRadioButton.setSelected(op > 0);
			naoAguRadioButton.setSelected(op == 0);

			String[] opt = ((String) tabela.getValueAt(cod, 36)).split(":");
			escSemCheckBox.setSelected(!opt[0].trim().isEmpty());
			escFimCheckBox.setSelected(!opt[1].trim().isEmpty());
			esc1236CheckBox.setSelected(!opt[2].trim().isEmpty());
			escTarCheckBox.setSelected(!opt[3].trim().isEmpty());
			escManCheckBox.setSelected(!opt[4].trim().isEmpty());
		} else {
			JOptionPane
					.showMessageDialog(
							null,
							"Você não possui o nível de acesso necessário para visualizar essa tabela.\nFavor verificar com a sua gerência.");
			setEnabled(false);
		}
	}

	private ArrayList<Object> dadosCandidato() {
		ArrayList<Object> obj = new ArrayList<>();
		obj.add(nomeTextField.getText());
		obj.add(enderecoTextField.getText());
		obj.add(cepFormattedTextField.getText());
		obj.add(celFormattedTextField.getText());
		obj.add(foneFormattedTextField.getText());
		obj.add(util.strToDate(nascDatePicker.getJFormattedTextField()
				.getText()));
		obj.add(localTextField.getText());
		obj.add(util.getAlfaNum(cpfFormattedTextField.getText()));
		obj.add(rgTextField.getText());
		obj.add(ctpsTextField.getText());
		obj.add(obsCadTextArea.getText());
		obj.add(voluntarioTextField.getText());
		obj.add(pasTempTextField.getText());
		obj.add(acidenteTextField.getText());
		obj.add(enfermTextField.getText());
		obj.add(motEsTextField.getText());
		obj.add(forteTextArea.getText());
		obj.add(fracoTextArea.getText());
		obj.add(sexoCombo.get(sexoComboBox.getSelectedIndex()));
		obj.add(estadoCombo.get(estadoComboBox.getSelectedIndex()));
		obj.add(cidadeCombo.get(cidadeComboBox.getSelectedIndex()));
		obj.add(estCivilCombo.get(estCivilComboBox.getSelectedIndex()));
		obj.add(grauEscCombo.get(grauEscComboBox.getSelectedIndex()));
		obj.add(exptCombo.get(exptComboBox.getSelectedIndex()));
		obj.add(areaCombo.get(areaComboBox.getSelectedIndex()));
		obj.add(esporteCombo.get(esporteComboBox.getSelectedIndex()));
		obj.add(iniciarCombo.get(iniciarComboBox.getSelectedIndex()));
		try {
			obj.add(voluntariobuttonGroup.getSelection().getActionCommand());
		} catch (NullPointerException ex) {
			obj.add(-1);
		}
		try {
			obj.add(esportesbuttonGroup.getSelection().getActionCommand());
		} catch (NullPointerException ex) {
			obj.add(-1);
		}
		try {
			obj.add(residenciabuttonGroup.getSelection().getActionCommand());
		} catch (NullPointerException ex) {
			obj.add(-1);
		}
		try {
			obj.add(turnobuttonGroup.getSelection().getActionCommand());
		} catch (NullPointerException ex) {
			obj.add(-1);
		}
		try {
			obj.add(acidentebuttonGroup.getSelection().getActionCommand());
		} catch (NullPointerException ex) {
			obj.add(-1);
		}
		try {
			obj.add(enfermidadebuttonGroup.getSelection().getActionCommand());
		} catch (NullPointerException ex) {
			obj.add(-1);
		}
		try {
			obj.add(outroEmpregobuttonGroup.getSelection().getActionCommand());
		} catch (NullPointerException ex) {
			obj.add(-1);
		}

		String escala = "";
		escala += escSemCheckBox.isSelected() ? "1:" : " :";
		escala += escFimCheckBox.isSelected() ? "1:" : " :";
		escala += esc1236CheckBox.isSelected() ? "1:" : " :";
		escala += escTarCheckBox.isSelected() ? "1:" : " :";
		escala += escManCheckBox.isSelected() ? "1:" : " :";
		obj.add(escala);
		obj.add(foto);
		return obj;
	}

	@SuppressWarnings({ "rawtypes", "unchecked", "unused" })
	private void carregarTabelaExperiencia(ResultSet experiencia) {
		try {
			int cont = 0;
			Vector<String> tableHeaders = new Vector<String>();
			Vector tableData = new Vector();
			tableHeaders.add("Empresa");
			tableHeaders.add("Gerente");
			tableHeaders.add("Cargo");
			tableHeaders.add("Salário");
			tableHeaders.add("motivo");
			tableHeaders.add("entrada");
			tableHeaders.add("saida");
			tableHeaders.add("estado");
			tableHeaders.add("cidade");
			tableHeaders.add("atividade");
			tableHeaders.add("id");
			while (experiencia.next()) {
				Vector<Object> oneRow = new Vector<Object>();
				oneRow.add(experiencia.getString("empresa"));
				oneRow.add(experiencia.getString("gerente"));
				oneRow.add(experiencia.getString("cargo"));
				oneRow.add(experiencia.getString("salario"));
				oneRow.add(experiencia.getString("motivo"));
				oneRow.add(experiencia.getString("entrada"));
				oneRow.add(experiencia.getString("saida"));
				oneRow.add(experiencia.getInt("estado"));
				oneRow.add(experiencia.getInt("cidade"));
				oneRow.add(experiencia.getInt("atividade"));
				oneRow.add(experiencia.getInt("idemprego"));
				tableData.add(oneRow);
			}
			experienciaTable.setModel(new DefaultTableModel(tableData,
					tableHeaders));
			experienciaTable.getColumnModel().getColumn(0)
					.setPreferredWidth(300);
			experienciaTable.getColumnModel().getColumn(1)
					.setPreferredWidth(250);
			experienciaTable.getColumnModel().getColumn(2)
					.setPreferredWidth(300);
			experienciaTable.getColumnModel().getColumn(3)
					.setPreferredWidth(100);
			util.ocultarColunas(experienciaTable, 4, 10);
			experiencia.close();
		} catch (SQLException ex) {
			Logger.getLogger(Curriculum.class.getName()).log(Level.SEVERE,
					null, ex);
		}
	}

	private void carregarRegistroExperiencia(int cod) {
		if (cod >= 0) {
			DefaultTableModel tabela = (DefaultTableModel) experienciaTable
					.getModel();
			limparExperiencia();
			empTextField.setText((String) tabela.getValueAt(cod, 0));
			gerenteTextField.setText((String) tabela.getValueAt(cod, 1));
			cargoTextField.setText((String) tabela.getValueAt(cod, 2));
			salarioFormattedTextField.setText(util.dolarToReal((String) tabela
					.getValueAt(cod, 3)));
			motivoTextArea.setText((String) tabela.getValueAt(cod, 4));
			entradaDatePicker.setText((String) tabela.getValueAt(cod, 5));
			saidaDatePicker.setText((String) tabela.getValueAt(cod, 6));
			estado2ComboBox
					.setSelectedIndex((int) tabela.getValueAt(cod, 7) - 1);
			try {
				ResultSet rs = new SQL().localizarCidade("idcidade='"
						+ (int) tabela.getValueAt(cod, 8) + "'");
				rs.next();
				cidade2ComboBox.setSelectedItem(rs.getString("city"));
			} catch (SQLException ex) {
			}
			atividadeComboBox
					.setSelectedIndex((int) tabela.getValueAt(cod, 9) - 1);
		}
	}

	private ArrayList<Object> dadosExperiencia() {
		ArrayList<Object> obj = new ArrayList<>();
		obj.add(empTextField.getText());
		obj.add(gerenteTextField.getText());
		obj.add(cargoTextField.getText());
		obj.add(util.realToDolar(salarioFormattedTextField.getText()
				.replaceAll("\\.", "")));
		obj.add(motivoTextArea.getText());
		obj.add(entradaDatePicker.getModel().getValue());
		obj.add(saidaDatePicker.getModel().getValue());
		obj.add(estado2Combo.get(estado2ComboBox.getSelectedIndex()));
		obj.add(cidade2Combo.get(cidade2ComboBox.getSelectedIndex()));
		obj.add(atividadeCombo.get(atividadeComboBox.getSelectedIndex()));
		return obj;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void carregarTabelaEscolaridade(ResultSet escolaridade) {
		try {
			Vector<String> tableHeaders = new Vector<String>();
			Vector tableData = new Vector();
			tableHeaders.add("Curso");
			tableHeaders.add("Tipo");
			tableHeaders.add("Instituição");
			tableHeaders.add("Carga");
			tableHeaders.add("Concluido");
			tableHeaders.add("Fim");
			tableHeaders.add("Observação");
			tableHeaders.add("Inicio");
			tableHeaders.add("Diploma");
			tableHeaders.add("id");
			int opt;
			while (escolaridade.next()) {
				Vector<Object> oneRow = new Vector<Object>();
				oneRow.add(escolaridade.getString("curso"));
				oneRow.add(tipoCurComboBox.getItemAt(escolaridade
						.getInt("tipo") - 1));
				oneRow.add(escolaridade.getString("instituicao"));
				oneRow.add(escolaridade.getString("carga"));
				opt = escolaridade.getInt("concluido");
				oneRow.add(opt > 0 ? "Sim" : opt == 0 ? "Não" : "");
				oneRow.add(escolaridade.getString("fim"));
				oneRow.add(escolaridade.getString("observacao"));
				oneRow.add(escolaridade.getString("inicio"));
				opt = escolaridade.getInt("diploma");
				oneRow.add(opt > 0 ? "Sim" : opt == 0 ? "Não" : "");
				oneRow.add(escolaridade.getInt("idcursos"));
				tableData.add(oneRow);
			}
			cursosTable
					.setModel(new DefaultTableModel(tableData, tableHeaders));
			cursosTable.getColumnModel().getColumn(0).setPreferredWidth(250);
			cursosTable.getColumnModel().getColumn(1).setPreferredWidth(100);
			cursosTable.getColumnModel().getColumn(2).setPreferredWidth(200);
			cursosTable.getColumnModel().getColumn(3).setPreferredWidth(40);
			cursosTable.getColumnModel().getColumn(4).setPreferredWidth(50);
			cursosTable.getColumnModel().getColumn(5).setPreferredWidth(75);
			util.ocultarColunas(cursosTable, 6, 9);
			escolaridade.close();
		} catch (SQLException ex) {
			Logger.getLogger(Curriculum.class.getName()).log(Level.SEVERE,
					null, ex);
		}
	}

	private void carregarRegistroEscolaridade(int cod) {
		if (cod >= 0) {
			DefaultTableModel tabela = (DefaultTableModel) cursosTable
					.getModel();
			limparCurso();
			cursoTextField.setText((String) tabela.getValueAt(cod, 0));
			tipoCurComboBox.setSelectedItem(tabela.getValueAt(cod, 1));
			instTextField.setText((String) tabela.getValueAt(cod, 2));
			cargaHoTextField.setText((String) tabela.getValueAt(cod, 3));
			String opt = (String) tabela.getValueAt(cod, 4);
			simConRadioButton.setSelected(opt.equals("Sim"));
			naoConRadioButton.setSelected(opt.equals("Não"));
			fimCurDatePicker.setText((String) tabela.getValueAt(cod, 5));
			obsCurTextArea.setText((String) tabela.getValueAt(cod, 6));
			inicioCurDatePicker.setText((String) tabela.getValueAt(cod, 7));
			opt = (String) tabela.getValueAt(cod, 8);
			simDipRadioButton.setSelected(opt.equals("Sim"));
			naoDipRadioButton.setSelected(opt.equals("Não"));
		}
	}

	private ArrayList<Object> dadosEscolaridade() {
		ArrayList<Object> obj = new ArrayList<>();
		obj.add(cursoTextField.getText());
		obj.add(instTextField.getText());
		obj.add(cargaHoTextField.getText());
		obj.add(fimCurDatePicker.getModel().getValue());
		obj.add(obsCurTextArea.getText());
		obj.add(inicioCurDatePicker.getModel().getValue());
		obj.add(tipoCurCombo.get(tipoCurComboBox.getSelectedIndex()));
		try {
			obj.add(cursoConclbuttonGroup.getSelection().getActionCommand());
		} catch (NullPointerException ex) {
			obj.add(-1);
		}
		try {
			obj.add(diplomabuttonGroup.getSelection().getActionCommand());
		} catch (NullPointerException ex) {
			obj.add(-1);
		}
		return obj;
	}

	@SuppressWarnings({ "unchecked" })
	private void initComboBoxes() {
		sexoComboBox.setModel(util.initComboBox("sexo",
				new String[] { "sexo" }, "idsexo"));
		sexoCombo = util.initCombo("sexo", "idsexo");
		sexoComboBox.setSelectedIndex(-1);

		estadoComboBox.setModel(util.initComboBox("estado", new String[] {
				"state", "uf" }, "idestado"));
		estadoCombo = util.initCombo("estado", "idestado");
		estadoComboBox.setSelectedIndex(-1);
		estado2ComboBox.setModel(util.initComboBox("estado", new String[] {
				"state", "uf" }, "idestado"));
		estado2Combo = util.initCombo("estado", "idestado");
		estado2ComboBox.setSelectedIndex(-1);

		estCivilComboBox.setModel(util.initComboBox("est_civil",
				new String[] { "civil" }, "idecivil"));
		estCivilCombo = util.initCombo("est_civil", "idecivil");
		estCivilComboBox.setSelectedIndex(-1);

		grauEscComboBox.setModel(util.initComboBox("tipo_curso",
				new String[] { "tipo" }, "idtcurso"));
		grauEscCombo = util.initCombo("tipo_curso", "idtcurso");
		grauEscComboBox.setSelectedIndex(-1);
		tipoCurComboBox.setModel(util.initComboBox("tipo_curso",
				new String[] { "tipo" }, "idtcurso"));
		tipoCurCombo = util.initCombo("tipo_curso", "idtcurso");
		tipoCurComboBox.setSelectedIndex(-1);

		exptComboBox.setModel(util.initComboBox("tempo_exper",
				new String[] { "tempo" }, "idtexper"));
		exptCombo = util.initCombo("tempo_exper", "idtexper");
		exptComboBox.setSelectedIndex(-1);

		areaComboBox.setModel(util.initComboBox("departamento",
				new String[] { "depto" }, "iddepto"));
		areaCombo = util.initCombo("departamento", "iddepto");
		areaComboBox.setSelectedIndex(-1);

		atividadeComboBox.setModel(util.initComboBox("atividade",
				new String[] { "atividade" }, "idatividade"));
		atividadeCombo = util.initCombo("atividade", "idatividade");
		atividadeComboBox.setSelectedIndex(-1);

		esporteComboBox.setModel(util.initComboBox("esporte",
				new String[] { "esporte" }, "idesporte"));
		esporteCombo = util.initCombo("esporte", "idesporte");
		esporteComboBox.setSelectedIndex(-1);

		iniciarComboBox.setModel(util.initComboBox("ini_traba",
				new String[] { "iniciar" }, "iditraba"));
		iniciarCombo = util.initCombo("ini_traba", "iditraba");
		iniciarComboBox.setSelectedIndex(-1);
	}

	private boolean upd = false;
	private int levelTab = Integer.MIN_VALUE;
	private int index;
	private int total;
	private String foto;
	private final Util util = new Util();
	private ButtonGroup diplomabuttonGroup;
	private ButtonGroup cursoConclbuttonGroup;
	private ButtonGroup voluntariobuttonGroup;
	private ButtonGroup esportesbuttonGroup;
	private ButtonGroup residenciabuttonGroup;
	private ButtonGroup turnobuttonGroup;
	private ButtonGroup acidentebuttonGroup;
	private ButtonGroup enfermidadebuttonGroup;
	private ButtonGroup outroEmpregobuttonGroup;
	private HashMap<Integer, Integer> sexoCombo;
	private HashMap<Integer, Integer> estadoCombo;
	private HashMap<Integer, Integer> estado2Combo;
	private HashMap<Integer, Integer> cidadeCombo;
	private HashMap<Integer, Integer> cidade2Combo;
	private HashMap<Integer, Integer> estCivilCombo;
	private HashMap<Integer, Integer> grauEscCombo;
	private HashMap<Integer, Integer> tipoCurCombo;
	private HashMap<Integer, Integer> exptCombo;
	private HashMap<Integer, Integer> areaCombo;
	private HashMap<Integer, Integer> atividadeCombo;
	private HashMap<Integer, Integer> esporteCombo;
	private HashMap<Integer, Integer> iniciarCombo;
	private JButton findButton;
	private JButton firstButton;
	private JButton beforeButton;
	private JButton nextButton;
	private JButton lastButton;
	private JButton printButton;
	private JButton fotoButton;
	private JButton newButton;
	private JButton editButton;
	private JButton saveButton;
	private JButton deleteButton;
	private TPButton est2Button;
	private TPButton cid2Button;
	private TPButton ativButton;
	private TPButton tipoButton;
	private TPButton espButton;
	private TPButton iniButton;
	private TPButton estButton;
	private TPButton cidButton;
	private TPButton areaButton;
	private TPButton escButton;
	private TPButton exptButton;
	private JCheckBox escSemCheckBox;
	private JCheckBox escFimCheckBox;
	private JCheckBox esc1236CheckBox;
	private JCheckBox escTarCheckBox;
	private JCheckBox escManCheckBox;
	private AComboBox sexoComboBox;
	private AComboBox estCivilComboBox;
	private AComboBox grauEscComboBox;
	private AComboBox exptComboBox;
	private AComboBox areaComboBox;
	private AComboBox cidadeComboBox;
	private AComboBox estadoComboBox;
	private AComboBox atividadeComboBox;
	private AComboBox estado2ComboBox;
	private AComboBox cidade2ComboBox;
	private AComboBox tipoCurComboBox;
	private AComboBox iniciarComboBox;
	private AComboBox esporteComboBox;
	private JFormattedTextField cpfConsFormattedTextField;
	private JFormattedTextField foneFormattedTextField;
	private JFormattedTextField celFormattedTextField;
	private JFormattedTextField cepFormattedTextField;
	private TDate nascDatePicker;
	private JFormattedTextField cpfFormattedTextField;
	private TDate entradaDatePicker;
	private TDate saidaDatePicker;
	private JFormattedTextField salarioFormattedTextField;
	private TDate inicioCurDatePicker;
	private TDate fimCurDatePicker;
	private JLabel lblNome;
	private JLabel lblEndereo;
	private JLabel lblEstado;
	private JLabel lblCelular;
	private JLabel lblNascimento;
	private JLabel lblCpf_1;
	private JLabel lblGrauDeEscolaridade;
	private JLabel lblInteressePrimario;
	private JLabel fotoLabel;
	private JLabel lblSexo;
	private JLabel lblCep;
	private JLabel lblFixo;
	private JLabel lblEstadoCivil;
	private JLabel lblRg;
	private JLabel lblCtps;
	private JLabel lblObservaes;
	private JLabel lblCpf;
	private JLabel lblCdigo;
	private JLabel lblTempoTotalDe;
	private JLabel lblCidade;
	private JLabel lblLocal;
	private JLabel lblEmpresa;
	private JLabel lblEstado_1;
	private JLabel lblUltimoCargo;
	private JLabel lblDataEntrada;
	private JLabel lblDataSada;
	private JLabel lblCidade_1;
	private JLabel lblAtividade;
	private JLabel lblGerente;
	private JLabel lblltimoSalrio;
	private JLabel lblMotivoDaSada;
	private JLabel lblNewLabel;
	private JLabel lblInstituio;
	private JLabel lblTipo;
	private JLabel lblCargaHoraria;
	private JLabel lblInicio;
	private JLabel lblFim;
	private JLabel lblObservao;
	private JLabel lblNewLabel_1;
	private JLabel lblQualMotivoO;
	private JPanel buscaPanel;
	private JPanel cadastroPanel;
	private JPanel experienciaPanel;
	private JPanel cursoPanel;
	private JPanel socialPanel;
	private JPanel fotoPanel;
	private JPanel actionPanel;
	private JPanel panel1;
	private JPanel panel2;
	private JPanel panel3;
	private JPanel panel4;
	private JPanel panel5;
	private JPanel panel6;
	private JPanel panel7;
	private JPanel panel8;
	private JPanel panel9;
	private JPanel panel10;
	private JPanel panel11;
	private JPanel panel12;
	private JPanel panel13;
	private JRadioButton simDipRadioButton;
	private JRadioButton naoDipRadioButton;
	private JRadioButton simConRadioButton;
	private JRadioButton naoConRadioButton;
	private JRadioButton naoVolRadioButton;
	private JRadioButton resPropRadioButton;
	private JRadioButton simVolRadioButton;
	private JRadioButton resAlugRadioButton;
	private JRadioButton naoAciRadioButton;
	private JRadioButton simAciRadioButton;
	private JRadioButton naoEspRadioButton;
	private JRadioButton simEspRadioButton;
	private JRadioButton turAmbRadioButton;
	private JRadioButton turNotRadioButton;
	private JRadioButton turDiuRadioButton;
	private JRadioButton naoEnfRadioButton;
	private JRadioButton simEnfRadioButton;
	private JRadioButton naoAguRadioButton;
	private JRadioButton simAguRadioButton;
	private JScrollPane scrollPane;
	private JScrollPane scrollPane_1;
	private JScrollPane scrollPane_2;
	private JScrollPane scrollPane_3;
	private JScrollPane scrollPane_4;
	private JScrollPane scrollPane_5;
	private JScrollPane scrollPane_6;
	private JTabbedPane dadosTabbedPane;
	private JTable experienciaTable;
	private JTable cursosTable;
	private JTable candidatosTable;
	private JTextArea motivoTextArea;
	private JTextArea obsCurTextArea;
	private JTextArea obsCadTextArea;
	private JTextArea forteTextArea;
	private JTextArea fracoTextArea;
	private JTextField codTextField;
	private JTextField enderecoTextField;
	private JTextField localTextField;
	private JTextField rgTextField;
	private JTextField ctpsTextField;
	private JTextField nomeTextField;
	private JTextField gerenteTextField;
	private JTextField empTextField;
	private JTextField cargoTextField;
	private JTextField cargaHoTextField;
	private JTextField cursoTextField;
	private JTextField instTextField;
	private JTextField voluntarioTextField;
	private JTextField pasTempTextField;
	private JTextField acidenteTextField;
	private JTextField enfermTextField;
	private JTextField motEsTextField;
}
