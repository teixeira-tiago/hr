/**
 * 
 */
package recrutamento;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;
import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.MaskFormatter;

import net.miginfocom.swing.MigLayout;
import utiliter.SQL;
import utiliter.Util;
import utiliter.component.AComboBox;
import utiliter.component.TTable;

/**
 * @author Tiago A. Teixeira {@link tiago.a.teixeira@acm.org}
 * 
 */
@SuppressWarnings("serial")
public class SelecaoOld extends JInternalFrame {

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					SelecaoOld window = new SelecaoOld();
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
	public SelecaoOld() {
		initialize();
		initLista();
	}

	public void setPosicao() {
		Dimension d = this.getDesktopPane().getSize();
		this.setLocation((d.width - this.getSize().width) / 2,
				(d.height - this.getSize().height) / 2);
	}

	/**
	 * Initialize the contents of the
	 */
	@SuppressWarnings("rawtypes")
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
		setTitle("Seleção de Candidatos");
		setResizable(true);
		setMaximizable(true);
		setClosable(true);
		setBounds(0, 0, 765, 580);

		etp1buttonGroup = new ButtonGroup();
		etp2buttonGroup = new ButtonGroup();
		etp3buttonGroup = new ButtonGroup();
		etp4buttonGroup = new ButtonGroup();
		getContentPane()
				.setLayout(
						new MigLayout(
								"",
								"5[120px,grow]0[5px:5px]0[120px,grow]0[5px:5px]0[120px,grow]0[5px:5px]0[120px,grow]0[5px:5px]0[120px,grow]0[5px:5px]0[125px,grow]5",
								"5[16px]0[5px:5px]0[140px]0[5px:5px]0[330px,grow]0[5px:5px]0[35px]5"));

		scrollPane_1 = new JScrollPane();
		getContentPane().add(scrollPane_1, "cell 0 2,grow");

		areaList = new JList();
		scrollPane_1.setViewportView(areaList);

		scrollPane_2 = new JScrollPane();
		getContentPane().add(scrollPane_2, "cell 2 2,grow");

		escList = new JList();
		scrollPane_2.setViewportView(escList);

		scrollPane_3 = new JScrollPane();
		getContentPane().add(scrollPane_3, "cell 4 2,grow");

		expList = new JList();
		scrollPane_3.setViewportView(expList);

		scrollPane_4 = new JScrollPane();
		getContentPane().add(scrollPane_4, "cell 6 2,grow");

		dispList = new JList();
		scrollPane_4.setViewportView(dispList);

		scrollPane = new JScrollPane();
		getContentPane().add(scrollPane, "cell 8 2,grow");

		outroList = new JList();
		scrollPane.setViewportView(outroList);

		scrollPane_8 = new JScrollPane();
		getContentPane().add(scrollPane_8, "cell 10 2,grow");

		editorPane = new JEditorPane();
		scrollPane_8.setViewportView(editorPane);

		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		getContentPane().add(tabbedPane, "cell 0 4 11 1,grow");

		selectPanel = new JPanel();
		// tabbedPane.addTab("Seleção de Currículos", null, panel_2, null);
		tabbedPane.addTab("Seleção", null, selectPanel, null);
		selectPanel
				.setLayout(new MigLayout(
						"",
						"5[137px:137px]0[5px:5px]0[45px]0[16px]0[105px,grow]0[40px]0[60px,grow]0[50px]0[78px]0[22px]0[8px]0[14px]0[105px,grow]5",
						"5[24px:24px]0[6px:6px]0[24px:24px]0[6px:6px]0[24px:24px]0[6px:6px]0[24px:24px]0[30px:30px]0[5px:5px]0[140px,grow]5"));

		panel_7 = new JPanel();
		panel_7.setLayout(null);
		selectPanel.add(panel_7, "cell 0 0 1 9,grow");

		fotoLabel = new JLabel("");
		fotoLabel.setBounds(0, 0, 137, 150);
		panel_7.add(fotoLabel);

		lblNome = new JLabel("Nome:");
		lblNome.setHorizontalAlignment(SwingConstants.RIGHT);
		selectPanel.add(lblNome, "cell 2 0,aligny center");

		nomeTextField = new JTextField();
		nomeTextField.setEditable(false);
		selectPanel.add(nomeTextField, "cell 3 0 6 1,grow");
		nomeTextField.setColumns(10);

		lblSexo = new JLabel("Sexo:");
		lblSexo.setHorizontalAlignment(SwingConstants.RIGHT);
		selectPanel.add(lblSexo, "cell 9 0 2 1,aligny center");

		lblEndereo = new JLabel("Endereço:");
		lblEndereo.setHorizontalAlignment(SwingConstants.RIGHT);
		selectPanel.add(lblEndereo, "cell 2 2 2 1,aligny center");

		endTextField = new JTextField();
		endTextField.setEditable(false);
		selectPanel.add(endTextField, "cell 4 2 6 1,grow");
		endTextField.setColumns(10);

		lblCep = new JLabel("CEP:");
		lblCep.setHorizontalAlignment(SwingConstants.RIGHT);
		selectPanel.add(lblCep, "cell 10 2 2 1,aligny center");

		cepFormattedTextField = new JFormattedTextField();
		cepFormattedTextField.setEditable(false);
		try {
			cepFormattedTextField
					.setFormatterFactory(new DefaultFormatterFactory(
							new MaskFormatter("#####-###")));
		} catch (Exception e) {
			e.printStackTrace();
		}
		selectPanel.add(cepFormattedTextField, "cell 12 2,grow");

		lblEstado = new JLabel("Estado:");
		lblEstado.setHorizontalAlignment(SwingConstants.RIGHT);
		selectPanel.add(lblEstado, "cell 2 4,aligny center");

		lblCidade = new JLabel("Cidade:");
		lblCidade.setHorizontalAlignment(SwingConstants.RIGHT);
		selectPanel.add(lblCidade, "cell 7 4,alignx trailing,aligny center");

		cidTextField = new JTextField();
		cidTextField.setEditable(false);
		cidTextField.setColumns(10);
		selectPanel.add(cidTextField, "cell 8 4 5 1,grow");

		lblCelular = new JLabel("Celular:");
		lblCelular.setHorizontalAlignment(SwingConstants.RIGHT);
		selectPanel.add(lblCelular, "cell 2 6,aligny center");

		celFormattedTextField = new JFormattedTextField();
		celFormattedTextField.setEditable(false);
		try {
			celFormattedTextField
					.setFormatterFactory(new DefaultFormatterFactory(
							new MaskFormatter("(##)#####-###*")));
		} catch (Exception e) {
			e.printStackTrace();
		}
		selectPanel.add(celFormattedTextField, "cell 3 6 2 1,grow");

		lblOutro = new JLabel("Outro:");
		lblOutro.setHorizontalAlignment(SwingConstants.RIGHT);
		selectPanel.add(lblOutro, "cell 5 6,aligny center");

		foneFormattedTextField = new JFormattedTextField();
		foneFormattedTextField.setEditable(false);
		try {
			foneFormattedTextField
					.setFormatterFactory(new DefaultFormatterFactory(
							new MaskFormatter("(##)#####-###*")));
		} catch (Exception e) {
			e.printStackTrace();
		}
		selectPanel.add(foneFormattedTextField, "cell 6 6 2 1,grow");

		lblEstadoCivil = new JLabel("Estado Civil:");
		lblEstadoCivil.setHorizontalAlignment(SwingConstants.RIGHT);
		selectPanel.add(lblEstadoCivil,
				"cell 8 6,alignx trailing,aligny center");

		scrollPane_5 = new JScrollPane();
		selectPanel.add(scrollPane_5, "cell 0 9 13 1,grow");

		candTable = new TTable();
		candTable.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				candTableMouseClicked(e);
			}
		});
		scrollPane_5.setViewportView(candTable);

		estTextField = new JTextField();
		estTextField.setEditable(false);
		selectPanel.add(estTextField, "cell 3 4 4 1,grow");
		estTextField.setColumns(10);

		estCivilTextField = new JTextField();
		estCivilTextField.setEditable(false);
		selectPanel.add(estCivilTextField, "cell 9 6 4 1,grow");
		estCivilTextField.setColumns(10);

		sexoTextField = new JTextField();
		sexoTextField.setEditable(false);
		selectPanel.add(sexoTextField, "cell 11 0 2 1,grow");
		sexoTextField.setColumns(10);

		grupoPanel = new JPanel();
		// tabbedPane.addTab("Entrevista em Grupo", null, grupoPanel, null);
		tabbedPane.addTab("Grupo", null, grupoPanel, null);
		grupoPanel
				.setLayout(new MigLayout(
						"",
						"5[137px:137px]0[5px:5px]0[41px]0[380px,grow]0[59px]0[50px]0[5px:5px]0[50px]5",
						"5[25px]0[5px:5px]0[120px:120px]0[5px:5px]0[24px:24px]0[5px:5px]0[24px:24px]0[41px,grow]0[24px:24px]0[5px:5px]0[24px:24px]5"));

		panel_8 = new JPanel();
		panel_8.setLayout(null);
		grupoPanel.add(panel_8, "cell 0 0 1 3,grow");

		fotoEtp1Label = new JLabel("");
		fotoEtp1Label.setBounds(0, 0, 137, 150);
		panel_8.add(fotoEtp1Label);

		label_2 = new JLabel("Nome:");
		grupoPanel.add(label_2, "cell 2 0,aligny center");

		nomeEtp1TextField = new JTextField();
		nomeEtp1TextField.setEditable(false);
		nomeEtp1TextField.setColumns(10);
		grupoPanel.add(nomeEtp1TextField, "cell 3 0,grow");

		label_3 = new JLabel("Indicado?");
		grupoPanel.add(label_3, "cell 4 0,alignx center,aligny center");

		simIndEtp1RadioButton = new JRadioButton("Sim");
		etp1buttonGroup.add(simIndEtp1RadioButton);
		grupoPanel.add(simIndEtp1RadioButton, "cell 5 0,aligny center");

		naoIndEtp1RadioButton = new JRadioButton("Não");
		etp1buttonGroup.add(naoIndEtp1RadioButton);
		grupoPanel.add(naoIndEtp1RadioButton, "cell 7 0,aligny center");

		scrollPane_6 = new JScrollPane();
		grupoPanel.add(scrollPane_6, "cell 2 2 6 1,grow");

		etp1Table = new TTable();
		etp1Table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				etp1TableMouseClicked(e);
			}
		});
		scrollPane_6.setViewportView(etp1Table);

		panel_1 = new JPanel();
		grupoPanel.add(panel_1, "cell 0 4 1 3,grow");
		panel_1.setLayout(null);

		lblTeste = new JLabel("Teste:");
		lblTeste.setBounds(0, 4, 70, 16);
		panel_1.add(lblTeste);

		nPsiEtp1Button = new JButton("...");
		nPsiEtp1Button.setBounds(92, 2, 20, 20);
		panel_1.add(nPsiEtp1Button);
		nPsiEtp1Button.setPreferredSize(new Dimension(20, 20));

		gPsiEtp1Button = new JButton("");
		gPsiEtp1Button.setBounds(117, 2, 20, 20);
		panel_1.add(gPsiEtp1Button);
		gPsiEtp1Button.setPreferredSize(new Dimension(20, 20));
		gPsiEtp1Button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				gPsiEtp1ButtonActionPerformed(e);
			}
		});
		gPsiEtp1Button.setIcon(new ImageIcon(getClass().getResource(
				"/img/guard.png")));

		psiEtp1ComboBox = new AComboBox();
		psiEtp1ComboBox.setBounds(0, 29, 137, 24);
		panel_1.add(psiEtp1ComboBox);

		JButton hPsiEtp1Button = new JButton("");
		hPsiEtp1Button.setPreferredSize(new Dimension(20, 20));
		hPsiEtp1Button.setBounds(67, 2, 20, 20);
		hPsiEtp1Button.setIcon(new ImageIcon(getClass().getResource(
				"/img/help.png")));

		panel_1.add(hPsiEtp1Button);
		psiEtp1ComboBox.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				psiEtp1ComboBoxItemStateChanged(e);
			}
		});
		nPsiEtp1Button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				nPsiEtp1ButtonActionPerformed(e);
			}
		});

		panel_2 = new JPanel();
		grupoPanel.add(panel_2, "cell 0 8 1 3,grow");
		panel_2.setLayout(null);

		JLabel lblDinamica = new JLabel("Dinamica:");
		lblDinamica.setBounds(0, 4, 65, 16);
		panel_2.add(lblDinamica);

		nDimEtp1Button = new JButton("...");
		nDimEtp1Button.setBounds(92, 2, 20, 20);
		panel_2.add(nDimEtp1Button);

		gDimEtp1Button = new JButton("");
		gDimEtp1Button.setBounds(117, 2, 20, 20);
		panel_2.add(gDimEtp1Button);
		gDimEtp1Button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				gDimEtp1ButtonActionPerformed(e);
			}
		});
		gDimEtp1Button.setIcon(new ImageIcon(getClass().getResource(
				"/img/guard.png")));

		dimEtp1ComboBox = new AComboBox();
		dimEtp1ComboBox.setBounds(0, 29, 137, 24);
		panel_2.add(dimEtp1ComboBox);

		hDimEtp1Button = new JButton("");
		hDimEtp1Button.setPreferredSize(new Dimension(20, 20));
		hDimEtp1Button.setBounds(67, 2, 20, 20);
		hDimEtp1Button.setIcon(new ImageIcon(getClass().getResource(
				"/img/help.png")));
		panel_2.add(hDimEtp1Button);
		dimEtp1ComboBox.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				dimEtp1ComboBoxItemStateChanged(e);
			}
		});
		nDimEtp1Button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				nDimEtp1ButtonActionPerformed(e);
			}
		});

		lblLaudo = new JLabel("Laudo:");
		grupoPanel.add(lblLaudo, "cell 2 4 2 1,alignx left,aligny top");

		scrollPane_7 = new JScrollPane();
		grupoPanel.add(scrollPane_7, "cell 2 6 6 5,grow");

		laudoEtp1TextArea = new JTextArea();
		scrollPane_7.setViewportView(laudoEtp1TextArea);

		individualPanel = new JPanel();
		tabbedPane.addTab("Entrevista Individual", null, individualPanel, null);
		// tabbedPane.addTab("Individual", null, individualPanel, null);
		individualPanel
				.setLayout(new MigLayout(
						"",
						"5[137px:137px]0[5px:5px]0[41px]0[380px,grow]0[59px]0[50px]0[5px:5px]0[50px]5",
						"5[25px]0[5px:5px]0[120px:120px]0[5px:5px]0[24px:24px]0[5px:5px]0[24px:24px]0[41px,grow]0[24px:24px]0[5px:5px]0[24px:24px]5"));

		panel_4 = new JPanel();
		panel_4.setLayout(null);
		individualPanel.add(panel_4, "cell 0 0 1 3,grow");

		fotoEtp2Label = new JLabel("");
		fotoEtp2Label.setBounds(0, 0, 137, 150);
		panel_4.add(fotoEtp2Label);

		label_5 = new JLabel("Nome:");
		individualPanel.add(label_5, "cell 2 0,aligny center");

		nomeEtp2TextField = new JTextField();
		nomeEtp2TextField.setEditable(false);
		nomeEtp2TextField.setColumns(10);
		individualPanel.add(nomeEtp2TextField, "cell 3 0,grow");

		label_6 = new JLabel("Indicado?");
		individualPanel.add(label_6, "cell 4 0,alignx center,aligny center");

		simIndEtp2RadioButton = new JRadioButton("Sim");
		etp2buttonGroup.add(simIndEtp2RadioButton);
		individualPanel.add(simIndEtp2RadioButton, "cell 5 0,aligny center");

		naoIndEtp2RadioButton = new JRadioButton("Não");
		etp2buttonGroup.add(naoIndEtp2RadioButton);
		individualPanel.add(naoIndEtp2RadioButton, "cell 7 0,aligny center");

		scrollPane_9 = new JScrollPane();
		individualPanel.add(scrollPane_9, "cell 2 2 6 1,grow");

		etp2Table = new TTable();
		etp2Table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				etp2TableMouseClicked(e);
			}
		});
		scrollPane_9.setViewportView(etp2Table);

		panel_3 = new JPanel();
		individualPanel.add(panel_3, "cell 0 4 1 3,grow");
		panel_3.setLayout(null);

		nPsiEtp2Button = new JButton("...");
		nPsiEtp2Button.setBounds(92, 2, 20, 20);
		panel_3.add(nPsiEtp2Button);

		gPsiEtp2Button = new JButton("");
		gPsiEtp2Button.setBounds(117, 2, 20, 20);
		panel_3.add(gPsiEtp2Button);
		gPsiEtp2Button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				gPsiEtp2ButtonActionPerformed(e);
			}
		});
		gPsiEtp2Button.setIcon(new ImageIcon(getClass().getResource(
				"/img/guard.png")));

		JLabel lblTeste_1 = new JLabel("Teste:");
		lblTeste_1.setBounds(0, 4, 70, 16);
		panel_3.add(lblTeste_1);

		psiEtp2ComboBox = new AComboBox();
		psiEtp2ComboBox.setBounds(0, 29, 137, 24);
		panel_3.add(psiEtp2ComboBox);
		psiEtp2ComboBox.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				psiEtp2ComboBoxItemStateChanged(e);
			}
		});
		nPsiEtp2Button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				nPsiEtp2ButtonActionPerformed(e);
			}
		});

		panel_5 = new JPanel();
		individualPanel.add(panel_5, "cell 0 8 1 3,grow");
		panel_5.setLayout(null);

		nDimEtp2Button = new JButton("...");
		nDimEtp2Button.setBounds(92, 2, 20, 20);
		panel_5.add(nDimEtp2Button);

		gDimEtp2Button = new JButton("");
		gDimEtp2Button.setBounds(117, 2, 20, 20);
		panel_5.add(gDimEtp2Button);
		gDimEtp2Button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				gDimEtp2ButtonActionPerformed(e);
			}
		});
		gDimEtp2Button.setIcon(new ImageIcon(getClass().getResource(
				"/img/guard.png")));

		JLabel lblDinamica_1 = new JLabel("Dinamica:");
		lblDinamica_1.setBounds(0, 4, 80, 16);
		panel_5.add(lblDinamica_1);

		dimEtp2ComboBox = new AComboBox();
		dimEtp2ComboBox.setBounds(0, 29, 137, 24);
		panel_5.add(dimEtp2ComboBox);
		dimEtp2ComboBox.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				dimEtp2ComboBoxItemStateChanged(e);
			}
		});
		nDimEtp2Button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				nDimEtp2ButtonActionPerformed(e);
			}
		});

		label_9 = new JLabel("Laudo:");
		individualPanel.add(label_9, "cell 2 4 2 1,alignx left,aligny top");

		scrollPane_10 = new JScrollPane();
		individualPanel.add(scrollPane_10, "cell 2 6 6 5,grow");

		laudoEtp2TextArea = new JTextArea();
		scrollPane_10.setViewportView(laudoEtp2TextArea);

		elaboracaoPanel = new JPanel();
		tabbedPane.addTab("Elaboração do Parecer", null, elaboracaoPanel, null);
		// tabbedPane.addTab("Parecer", null, elaboracaoPanel, null);
		elaboracaoPanel
				.setLayout(new MigLayout(
						"",
						"5[137px:137px]0[5px:5px]0[41px]0[380px,grow]0[59px]0[50px]0[5px:5px]0[50px]5",
						"5[25px]0[5px:5px]0[120px:120px]0[5px:5px]0[24px:24px]0[5px:5px]0[24px:24px]0[41px,grow]0[24px:24px]0[5px:5px]0[24px:24px]5"));

		panel_9 = new JPanel();
		panel_9.setLayout(null);
		elaboracaoPanel.add(panel_9, "cell 0 0 1 3,grow");

		fotoEtp3Label = new JLabel("");
		fotoEtp3Label.setBounds(0, 0, 137, 150);
		panel_9.add(fotoEtp3Label);

		label_11 = new JLabel("Nome:");
		elaboracaoPanel.add(label_11, "cell 2 0,aligny center");

		nomeEtp3TextField = new JTextField();
		nomeEtp3TextField.setEditable(false);
		nomeEtp3TextField.setColumns(10);
		elaboracaoPanel.add(nomeEtp3TextField, "cell 3 0,grow");

		label_12 = new JLabel("Indicado?");
		elaboracaoPanel.add(label_12, "cell 4 0,alignx center,aligny center");

		simIndEtp3RadioButton = new JRadioButton("Sim");
		etp3buttonGroup.add(simIndEtp3RadioButton);
		elaboracaoPanel.add(simIndEtp3RadioButton, "cell 5 0,aligny center");

		naoIndEtp3RadioButton = new JRadioButton("Não");
		etp3buttonGroup.add(naoIndEtp3RadioButton);
		elaboracaoPanel.add(naoIndEtp3RadioButton, "cell 7 0,aligny center");

		scrollPane_11 = new JScrollPane();
		elaboracaoPanel.add(scrollPane_11, "cell 2 2 6 1,grow");

		etp3Table = new TTable();
		etp3Table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				etp3TableMouseClicked(e);
			}
		});
		scrollPane_11.setViewportView(etp3Table);

		panel_6 = new JPanel();
		elaboracaoPanel.add(panel_6, "cell 0 4 1 3,grow");
		panel_6.setLayout(null);

		nPsiEtp3Button = new JButton("...");
		nPsiEtp3Button.setBounds(92, 2, 20, 20);
		panel_6.add(nPsiEtp3Button);

		gPsiEtp3Button = new JButton("");
		gPsiEtp3Button.setBounds(117, 2, 20, 20);
		panel_6.add(gPsiEtp3Button);
		gPsiEtp3Button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				gPsiEtp3ButtonActionPerformed(e);
			}
		});
		gPsiEtp3Button.setIcon(new ImageIcon(getClass().getResource(
				"/img/guard.png")));

		lblTeste_2 = new JLabel("Teste:");
		lblTeste_2.setBounds(0, 4, 70, 16);
		panel_6.add(lblTeste_2);

		psiEtp3ComboBox = new AComboBox();
		psiEtp3ComboBox.setBounds(0, 29, 137, 24);
		panel_6.add(psiEtp3ComboBox);
		psiEtp3ComboBox.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				psiEtp3ComboBoxItemStateChanged(e);
			}
		});
		nPsiEtp3Button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				nPsiEtp3ButtonActionPerformed(e);
			}
		});

		panel_10 = new JPanel();
		elaboracaoPanel.add(panel_10, "cell 0 8 1 3,grow");
		panel_10.setLayout(null);

		nDimEtp3Button = new JButton("...");
		nDimEtp3Button.setBounds(92, 2, 20, 20);
		panel_10.add(nDimEtp3Button);

		gDimEtp3Button = new JButton("");
		gDimEtp3Button.setBounds(117, 2, 20, 20);
		panel_10.add(gDimEtp3Button);
		gDimEtp3Button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				gDimEtp3ButtonActionPerformed(e);
			}
		});
		gDimEtp3Button.setIcon(new ImageIcon(getClass().getResource(
				"/img/guard.png")));

		lblDinamica_2 = new JLabel("Dinamica:");
		lblDinamica_2.setBounds(0, 4, 80, 16);
		panel_10.add(lblDinamica_2);

		dimEtp3ComboBox = new AComboBox();
		dimEtp3ComboBox.setBounds(0, 29, 137, 24);
		panel_10.add(dimEtp3ComboBox);
		dimEtp3ComboBox.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				dimEtp3ComboBoxItemStateChanged(e);
			}
		});
		nDimEtp3Button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				nDimEtp3ButtonActionPerformed(e);
			}
		});

		label_15 = new JLabel("Laudo:");
		elaboracaoPanel.add(label_15, "cell 2 4 2 1,alignx left,aligny top");

		scrollPane_12 = new JScrollPane();
		elaboracaoPanel.add(scrollPane_12, "cell 2 6 6 5,grow");

		laudoEtp3TextArea = new JTextArea();
		scrollPane_12.setViewportView(laudoEtp3TextArea);

		encerramentoPanel = new JPanel();
		tabbedPane.addTab("Encerramento do Processo", null, encerramentoPanel,
				null);
		// tabbedPane.addTab("Encerramento", null, encerramentoPanel, null);
		encerramentoPanel
				.setLayout(new MigLayout(
						"",
						"5[137px:137px]0[5px:5px]0[41px]0[380px,grow]0[59px]0[50px]0[5px:5px]0[50px]5",
						"5[25px]0[5px:5px]0[120px:120px]0[5px:5px]0[24px:24px]0[5px:5px]0[24px:24px]0[41px,grow]0[24px:24px]0[5px:5px]0[24px:24px]5"));

		panel_11 = new JPanel();
		panel_11.setLayout(null);
		encerramentoPanel.add(panel_11, "cell 0 0 1 3,grow");

		fotoEtp4Label = new JLabel("");
		fotoEtp4Label.setBounds(0, 0, 137, 150);
		panel_11.add(fotoEtp4Label);

		label_17 = new JLabel("Nome:");
		encerramentoPanel.add(label_17, "cell 2 0,aligny center");

		nomeEtp4TextField = new JTextField();
		nomeEtp4TextField.setEditable(false);
		nomeEtp4TextField.setColumns(10);
		encerramentoPanel.add(nomeEtp4TextField, "cell 3 0,grow");

		label_18 = new JLabel("Indicado?");
		encerramentoPanel.add(label_18, "cell 4 0,alignx center,aligny center");

		simIndEtp4RadioButton = new JRadioButton("Sim");
		etp4buttonGroup.add(simIndEtp4RadioButton);
		encerramentoPanel.add(simIndEtp4RadioButton, "cell 5 0,aligny center");

		naoIndEtp4RadioButton = new JRadioButton("Não");
		etp4buttonGroup.add(naoIndEtp4RadioButton);
		encerramentoPanel.add(naoIndEtp4RadioButton, "cell 7 0,aligny center");

		scrollPane_13 = new JScrollPane();
		encerramentoPanel.add(scrollPane_13, "cell 2 2 6 1,grow");

		etp4Table = new TTable();
		etp4Table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				etp4TableMouseClicked(e);
			}
		});
		scrollPane_13.setViewportView(etp4Table);

		panel_12 = new JPanel();
		encerramentoPanel.add(panel_12, "cell 0 4 1 3,grow");
		panel_12.setLayout(null);

		nPsiEtp4Button = new JButton("...");
		nPsiEtp4Button.setBounds(92, 2, 20, 20);
		panel_12.add(nPsiEtp4Button);

		gPsiEtp4Button = new JButton("");
		gPsiEtp4Button.setBounds(117, 2, 20, 20);
		panel_12.add(gPsiEtp4Button);
		gPsiEtp4Button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				gPsiEtp4ButtonActionPerformed(e);
			}
		});
		gPsiEtp4Button.setIcon(new ImageIcon(getClass().getResource(
				"/img/guard.png")));

		lblTeste_3 = new JLabel("Teste:");
		lblTeste_3.setBounds(0, 4, 70, 16);
		panel_12.add(lblTeste_3);

		psiEtp4ComboBox = new AComboBox();
		psiEtp4ComboBox.setBounds(0, 29, 137, 24);
		panel_12.add(psiEtp4ComboBox);
		psiEtp4ComboBox.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				psiEtp4ComboBoxItemStateChanged(e);
			}
		});
		nPsiEtp4Button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				nPsiEtp4ButtonActionPerformed(e);
			}
		});

		panel_13 = new JPanel();
		encerramentoPanel.add(panel_13, "cell 0 8 1 3,grow");
		panel_13.setLayout(null);

		nDimEtp4Button = new JButton("...");
		nDimEtp4Button.setBounds(92, 2, 20, 20);
		panel_13.add(nDimEtp4Button);

		gDimEtp4Button = new JButton("");
		gDimEtp4Button.setBounds(117, 2, 20, 20);
		panel_13.add(gDimEtp4Button);
		gDimEtp4Button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				gDimEtp4ButtonActionPerformed(e);
			}
		});
		gDimEtp4Button.setIcon(new ImageIcon(getClass().getResource(
				"/img/guard.png")));

		lblDinamica_3 = new JLabel("Dinamica:");
		lblDinamica_3.setBounds(0, 4, 80, 16);
		panel_13.add(lblDinamica_3);

		dimEtp4ComboBox = new AComboBox();
		dimEtp4ComboBox.setBounds(0, 29, 137, 24);
		panel_13.add(dimEtp4ComboBox);
		dimEtp4ComboBox.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				dimEtp4ComboBoxItemStateChanged(e);
			}
		});
		nDimEtp4Button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				nDimEtp4ButtonActionPerformed(e);
			}
		});

		label_21 = new JLabel("Laudo:");
		encerramentoPanel.add(label_21, "cell 2 4 2 1,alignx left,aligny top");

		scrollPane_14 = new JScrollPane();
		encerramentoPanel.add(scrollPane_14, "cell 2 6 6 5,grow");

		laudoEtp4TextArea = new JTextArea();
		scrollPane_14.setViewportView(laudoEtp4TextArea);

		panel = new JPanel();
		getContentPane().add(panel, "cell 0 6 11 1,grow");

		buscaButton = new JButton("");
		buscaButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				buscaButtonActionPerformed(e);
			}
		});
		panel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 0));
		buscaButton.setIcon(new ImageIcon(getClass().getResource(
				"/img/find.png")));
		buscaButton.setPreferredSize(new Dimension(30, 30));
		panel.add(buscaButton);

		salvaButton = new JButton("");
		salvaButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				salvaButtonActionPerformed(e);
			}
		});
		salvaButton.setIcon(new ImageIcon(getClass().getResource(
				"/img/save.png")));
		salvaButton.setPreferredSize(new Dimension(30, 30));
		panel.add(salvaButton);

		imprimeButton = new JButton("");
		imprimeButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				imprimeButtonActionPerformed(e);
			}
		});
		imprimeButton.setIcon(new ImageIcon(getClass().getResource(
				"/img/print.png")));
		imprimeButton.setPreferredSize(new Dimension(30, 30));
		panel.add(imprimeButton);

		lblreas = new JLabel("Áreas:");
		getContentPane().add(lblreas, "cell 0 0,alignx left,aligny top");

		lblEscolaridade = new JLabel("Escolaridade:");
		getContentPane()
				.add(lblEscolaridade, "cell 2 0,alignx left,aligny top");

		lblExperincia = new JLabel("Experiência:");
		getContentPane().add(lblExperincia, "cell 4 0,alignx left,aligny top");

		lblDisponibilidade = new JLabel("Disponibilidade:");
		getContentPane().add(lblDisponibilidade,
				"cell 6 0,alignx left,aligny top");

		lblOutro_1 = new JLabel("Outro:");
		getContentPane().add(lblOutro_1, "cell 8 0,alignx left,aligny top");

		lblOutro_2 = new JLabel("Outro:");
		getContentPane().add(lblOutro_2, "cell 10 0,alignx left,aligny top");
	}

	private void psiEtp1ComboBoxItemStateChanged(ItemEvent evt) {
		if (tabbedPane.getSelectedIndex() == 1) {
			if (!codEtp1.trim().isEmpty()) {
				int index = psiEtp1ComboBox.getSelectedIndex();
				if (index >= 0) {
					String str = psiEtp1List.get(index);
					String laudo = getLaudo(str);
					laudoEtp1TextArea.setText(laudo);
					String txt = (String) psiEtp1ComboBox.getSelectedItem();
					if (txt.contains("<")) {
						while (txt.contains("<")) {
							txt = txt.contains(">") ? txt.replace(
									txt.substring(txt.indexOf("<"),
											txt.indexOf(">") + 1), "") : txt;
						}
						psiEtp1ComboBox.setSelectedItem(txt);
					}
				}
			}
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void gPsiEtp1ButtonActionPerformed(ActionEvent evt) {
		if (!codEtp1.trim().isEmpty()) {
			String laudo = laudoEtp1TextArea.getText();
			int psi = psiEtp1ComboBox.getSelectedIndex();
			if (psi >= 0) {
				DefaultComboBoxModel tmp = (DefaultComboBoxModel) psiEtp1ComboBox
						.getModel();
				String op = formatOP((String) tmp.getElementAt(psi));
				String aux = psiEtp1List.get(psi);
				if (aux.contains("#")) {
					if (laudo.equals(getLaudo(aux))) {
						psiEtp1List.set(psi, aux.split("#")[0] + "#" + laudo
								+ ";0");
					} else {
						psiEtp1List.set(psi, aux.split("#")[0] + "#" + laudo
								+ ";1");
					}
				} else {
					psiEtp1List.set(psi, "0:" + codEtp1 + ":" + psi + "#"
							+ laudo);
				}
				tmp.removeElementAt(psi);
				tmp.insertElementAt("<html><b>" + op + "</b></html>", psi);
				psiEtp1ComboBox.setModel(tmp);
			} else {
				JOptionPane
						.showMessageDialog(null,
								"Favor selecionar um teste para o qual o laudo será escrito");
			}
		} else {
			JOptionPane
					.showMessageDialog(null,
							"Favor selecionar um candidato para o qual o laudo será escrito");
		}
	}

	@SuppressWarnings("unchecked")
	private void nPsiEtp1ButtonActionPerformed(ActionEvent e) {
		util.loadTelaCadastro("testes_ad", new JFrame(),
				"Cadastro de Testes Psicológicos");
		psiEtp1ComboBox.setModel(util.initComboBox("testes_ad",
				new String[] { "teste" }, "idtestead"));
		psiEtp1ComboBox.setSelectedIndex(-1);
		etp1TableMouseClicked(null);
	}

	private void dimEtp1ComboBoxItemStateChanged(ItemEvent evt) {
		if (tabbedPane.getSelectedIndex() == 1) {
			if (!codEtp1.trim().isEmpty()) {
				int index = dimEtp1ComboBox.getSelectedIndex();
				if (index >= 0) {
					String str = dimEtp1List.get(index);
					String laudo = getLaudo(str);
					laudoEtp1TextArea.setText(laudo);
					String txt = (String) dimEtp1ComboBox.getSelectedItem();
					if (txt.contains("<")) {
						while (txt.contains("<")) {
							txt = txt.contains(">") ? txt.replace(
									txt.substring(txt.indexOf("<"),
											txt.indexOf(">") + 1), "") : txt;
						}
						dimEtp1ComboBox.setSelectedItem(txt);
					}
				}
			}
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void gDimEtp1ButtonActionPerformed(ActionEvent evt) {
		if (!codEtp1.trim().isEmpty()) {
			String laudo = laudoEtp1TextArea.getText();
			int dim = dimEtp1ComboBox.getSelectedIndex();
			if (dim >= 0) {
				DefaultComboBoxModel tmp = (DefaultComboBoxModel) dimEtp1ComboBox
						.getModel();
				String op = formatOP((String) tmp.getElementAt(dim));
				String aux = dimEtp1List.get(dim);
				if (aux.contains("#")) {
					if (laudo.equals(getLaudo(aux))) {
						dimEtp1List.set(dim, aux.split("#")[0] + "#" + laudo
								+ ";0");
					} else {
						dimEtp1List.set(dim, aux.split("#")[0] + "#" + laudo
								+ ";1");
					}
				} else {
					dimEtp1List.set(dim, "0:" + codEtp1 + ":" + dim + "#"
							+ laudo);
				}
				tmp.removeElementAt(dim);
				tmp.insertElementAt("<html><b>" + op + "</b></html>", dim);
				dimEtp1ComboBox.setModel(tmp);
			} else {
				JOptionPane
						.showMessageDialog(null,
								"Favor selecionar um teste para o qual o laudo será escrito");
			}
		} else {
			JOptionPane
					.showMessageDialog(null,
							"Favor selecionar um candidato para o qual o laudo será escrito");
		}
	}

	@SuppressWarnings("unchecked")
	private void nDimEtp1ButtonActionPerformed(ActionEvent e) {
		util.loadTelaCadastro("dinamicas", new JFrame(),
				"Cadastro de Dinâmicas");
		dimEtp1ComboBox.setModel(util.initComboBox("dinamicas",
				new String[] { "dinamica" }, "iddinam"));
		dimEtp1ComboBox.setSelectedIndex(-1);
		etp1TableMouseClicked(null);
	}

	private void psiEtp2ComboBoxItemStateChanged(ItemEvent evt) {
		if (tabbedPane.getSelectedIndex() == 2) {
			if (!codEtp2.trim().isEmpty()) {
				int index = psiEtp2ComboBox.getSelectedIndex();
				if (index >= 0) {
					String str = psiEtp2List.get(index);
					String laudo = getLaudo(str);
					laudoEtp2TextArea.setText(laudo);
					String txt = (String) psiEtp2ComboBox.getSelectedItem();
					if (txt.contains("<")) {
						while (txt.contains("<")) {
							txt = txt.contains(">") ? txt.replace(
									txt.substring(txt.indexOf("<"),
											txt.indexOf(">") + 1), "") : txt;
						}
						psiEtp2ComboBox.setSelectedItem(txt);
					}
				}
			}
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void gPsiEtp2ButtonActionPerformed(ActionEvent evt) {
		if (!codEtp2.trim().isEmpty()) {
			String laudo = laudoEtp2TextArea.getText();
			int psi = psiEtp2ComboBox.getSelectedIndex();
			if (psi >= 0) {
				DefaultComboBoxModel tmp = (DefaultComboBoxModel) psiEtp2ComboBox
						.getModel();
				String op = formatOP((String) tmp.getElementAt(psi));
				String aux = psiEtp2List.get(psi);
				if (aux.contains("#")) {
					if (laudo.equals(getLaudo(aux))) {
						psiEtp2List.set(psi, aux.split("#")[0] + "#" + laudo
								+ ";0");
					} else {
						psiEtp2List.set(psi, aux.split("#")[0] + "#" + laudo
								+ ";1");
					}
				} else {
					psiEtp2List.set(psi, "0:" + codEtp2 + ":" + psi + "#"
							+ laudo);
				}
				tmp.removeElementAt(psi);
				tmp.insertElementAt("<html><b>" + op + "</b></html>", psi);
				psiEtp2ComboBox.setModel(tmp);
			} else {
				JOptionPane
						.showMessageDialog(null,
								"Favor selecionar um teste para o qual o laudo será escrito");
			}
		} else {
			JOptionPane
					.showMessageDialog(null,
							"Favor selecionar um candidato para o qual o laudo será escrito");
		}
	}

	@SuppressWarnings("unchecked")
	private void nPsiEtp2ButtonActionPerformed(ActionEvent e) {
		util.loadTelaCadastro("testes_ad", new JFrame(),
				"Cadastro de Testes Psicológicos");
		psiEtp2ComboBox.setModel(util.initComboBox("testes_ad",
				new String[] { "teste" }, "idtestead"));
		psiEtp2ComboBox.setSelectedIndex(-1);
		etp2TableMouseClicked(null);
	}

	private void dimEtp2ComboBoxItemStateChanged(ItemEvent evt) {
		if (tabbedPane.getSelectedIndex() == 2) {
			if (!codEtp2.trim().isEmpty()) {
				int index = dimEtp2ComboBox.getSelectedIndex();
				if (index >= 0) {
					String str = dimEtp2List.get(index);
					String laudo = getLaudo(str);
					laudoEtp2TextArea.setText(laudo);
					String txt = (String) dimEtp2ComboBox.getSelectedItem();
					if (txt.contains("<")) {
						while (txt.contains("<")) {
							txt = txt.contains(">") ? txt.replace(
									txt.substring(txt.indexOf("<"),
											txt.indexOf(">") + 1), "") : txt;
						}
						dimEtp2ComboBox.setSelectedItem(txt);
					}
				}
			}
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void gDimEtp2ButtonActionPerformed(ActionEvent evt) {
		if (!codEtp2.trim().isEmpty()) {
			String laudo = laudoEtp2TextArea.getText();
			int dim = dimEtp2ComboBox.getSelectedIndex();
			if (dim >= 0) {
				DefaultComboBoxModel tmp = (DefaultComboBoxModel) dimEtp2ComboBox
						.getModel();
				String op = formatOP((String) tmp.getElementAt(dim));
				String aux = dimEtp2List.get(dim);
				if (aux.contains("#")) {
					if (laudo.equals(getLaudo(aux))) {
						dimEtp2List.set(dim, aux.split("#")[0] + "#" + laudo
								+ ";0");
					} else {
						dimEtp2List.set(dim, aux.split("#")[0] + "#" + laudo
								+ ";1");
					}
				} else {
					dimEtp2List.set(dim, "0:" + codEtp2 + ":" + dim + "#"
							+ laudo);
				}
				tmp.removeElementAt(dim);
				tmp.insertElementAt("<html><b>" + op + "</b></html>", dim);
				dimEtp2ComboBox.setModel(tmp);
			} else {
				JOptionPane
						.showMessageDialog(null,
								"Favor selecionar um teste para o qual o laudo será escrito");
			}
		} else {
			JOptionPane
					.showMessageDialog(null,
							"Favor selecionar um candidato para o qual o laudo será escrito");
		}
	}

	@SuppressWarnings("unchecked")
	private void nDimEtp2ButtonActionPerformed(ActionEvent e) {
		util.loadTelaCadastro("dinamicas", new JFrame(),
				"Cadastro de Dinâmicas");
		dimEtp2ComboBox.setModel(util.initComboBox("dinamicas",
				new String[] { "dinamica" }, "iddinam"));
		dimEtp2ComboBox.setSelectedIndex(-1);
		etp2TableMouseClicked(null);
	}

	private void psiEtp3ComboBoxItemStateChanged(ItemEvent evt) {
		if (tabbedPane.getSelectedIndex() == 3) {
			if (!codEtp3.trim().isEmpty()) {
				int index = psiEtp3ComboBox.getSelectedIndex();
				if (index >= 0) {
					String str = psiEtp3List.get(index);
					String laudo = getLaudo(str);
					laudoEtp3TextArea.setText(laudo);
					String txt = (String) psiEtp3ComboBox.getSelectedItem();
					if (txt.contains("<")) {
						while (txt.contains("<")) {
							txt = txt.contains(">") ? txt.replace(
									txt.substring(txt.indexOf("<"),
											txt.indexOf(">") + 1), "") : txt;
						}
						psiEtp3ComboBox.setSelectedItem(txt);
					}
				}
			}
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void gPsiEtp3ButtonActionPerformed(ActionEvent evt) {
		if (!codEtp3.trim().isEmpty()) {
			String laudo = laudoEtp3TextArea.getText();
			int psi = psiEtp3ComboBox.getSelectedIndex();
			if (psi >= 0) {
				DefaultComboBoxModel tmp = (DefaultComboBoxModel) psiEtp3ComboBox
						.getModel();
				String op = formatOP((String) tmp.getElementAt(psi));
				String aux = psiEtp3List.get(psi);
				if (aux.contains("#")) {
					if (laudo.equals(getLaudo(aux))) {
						psiEtp3List.set(psi, aux.split("#")[0] + "#" + laudo
								+ ";0");
					} else {
						psiEtp3List.set(psi, aux.split("#")[0] + "#" + laudo
								+ ";1");
					}
				} else {
					psiEtp3List.set(psi, "0:" + codEtp3 + ":" + psi + "#"
							+ laudo);
				}
				tmp.removeElementAt(psi);
				tmp.insertElementAt("<html><b>" + op + "</b></html>", psi);
				psiEtp3ComboBox.setModel(tmp);
			} else {
				JOptionPane
						.showMessageDialog(null,
								"Favor selecionar um teste para o qual o laudo será escrito");
			}
		} else {
			JOptionPane
					.showMessageDialog(null,
							"Favor selecionar um candidato para o qual o laudo será escrito");
		}
	}

	@SuppressWarnings("unchecked")
	private void nPsiEtp3ButtonActionPerformed(ActionEvent e) {
		util.loadTelaCadastro("testes_ad", new JFrame(),
				"Cadastro de Testes Psicológicos");
		psiEtp3ComboBox.setModel(util.initComboBox("testes_ad",
				new String[] { "teste" }, "idtestead"));
		psiEtp3ComboBox.setSelectedIndex(-1);
		etp3TableMouseClicked(null);
	}

	private void dimEtp3ComboBoxItemStateChanged(ItemEvent evt) {
		if (tabbedPane.getSelectedIndex() == 3) {
			if (!codEtp3.trim().isEmpty()) {
				int index = dimEtp3ComboBox.getSelectedIndex();
				if (index >= 0) {
					String str = dimEtp3List.get(index);
					String laudo = getLaudo(str);
					laudoEtp3TextArea.setText(laudo);
					String txt = (String) dimEtp3ComboBox.getSelectedItem();
					if (txt.contains("<")) {
						while (txt.contains("<")) {
							txt = txt.contains(">") ? txt.replace(
									txt.substring(txt.indexOf("<"),
											txt.indexOf(">") + 1), "") : txt;
						}
						dimEtp3ComboBox.setSelectedItem(txt);
					}
				}
			}
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void gDimEtp3ButtonActionPerformed(ActionEvent evt) {
		if (!codEtp3.trim().isEmpty()) {
			String laudo = laudoEtp3TextArea.getText();
			int dim = dimEtp3ComboBox.getSelectedIndex();
			if (dim >= 0) {
				DefaultComboBoxModel tmp = (DefaultComboBoxModel) dimEtp3ComboBox
						.getModel();
				String op = formatOP((String) tmp.getElementAt(dim));
				String aux = dimEtp3List.get(dim);
				if (aux.contains("#")) {
					if (laudo.equals(getLaudo(aux))) {
						dimEtp3List.set(dim, aux.split("#")[0] + "#" + laudo
								+ ";0");
					} else {
						dimEtp3List.set(dim, aux.split("#")[0] + "#" + laudo
								+ ";1");
					}
				} else {
					dimEtp3List.set(dim, "0:" + codEtp3 + ":" + dim + "#"
							+ laudo);
				}
				tmp.removeElementAt(dim);
				tmp.insertElementAt("<html><b>" + op + "</b></html>", dim);
				dimEtp3ComboBox.setModel(tmp);
			} else {
				JOptionPane
						.showMessageDialog(null,
								"Favor selecionar um teste para o qual o laudo será escrito");
			}
		} else {
			JOptionPane
					.showMessageDialog(null,
							"Favor selecionar um candidato para o qual o laudo será escrito");
		}
	}

	@SuppressWarnings("unchecked")
	private void nDimEtp3ButtonActionPerformed(ActionEvent e) {
		util.loadTelaCadastro("dinamicas", new JFrame(),
				"Cadastro de Dinâmicas");
		dimEtp3ComboBox.setModel(util.initComboBox("dinamicas",
				new String[] { "dinamica" }, "iddinam"));
		dimEtp3ComboBox.setSelectedIndex(-1);
		etp3TableMouseClicked(null);
	}

	private void psiEtp4ComboBoxItemStateChanged(ItemEvent evt) {
		if (tabbedPane.getSelectedIndex() == 4) {
			if (!codEtp4.trim().isEmpty()) {
				int index = psiEtp4ComboBox.getSelectedIndex();
				if (index >= 0) {
					String str = psiEtp4List.get(index);
					String laudo = getLaudo(str);
					laudoEtp4TextArea.setText(laudo);
					String txt = (String) psiEtp4ComboBox.getSelectedItem();
					if (txt.contains("<")) {
						while (txt.contains("<")) {
							txt = txt.contains(">") ? txt.replace(
									txt.substring(txt.indexOf("<"),
											txt.indexOf(">") + 1), "") : txt;
						}
						psiEtp4ComboBox.setSelectedItem(txt);
					}
				}
			}
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void gPsiEtp4ButtonActionPerformed(ActionEvent evt) {
		if (!codEtp4.trim().isEmpty()) {
			String laudo = laudoEtp4TextArea.getText();
			int psi = psiEtp4ComboBox.getSelectedIndex();
			if (psi >= 0) {
				DefaultComboBoxModel tmp = (DefaultComboBoxModel) psiEtp4ComboBox
						.getModel();
				String op = formatOP((String) tmp.getElementAt(psi));
				String aux = psiEtp4List.get(psi);
				if (aux.contains("#")) {
					if (laudo.equals(getLaudo(aux))) {
						psiEtp4List.set(psi, aux.split("#")[0] + "#" + laudo
								+ ";0");
					} else {
						psiEtp4List.set(psi, aux.split("#")[0] + "#" + laudo
								+ ";1");
					}
				} else {
					psiEtp4List.set(psi, "0:" + codEtp4 + ":" + psi + "#"
							+ laudo);
				}
				tmp.removeElementAt(psi);
				tmp.insertElementAt("<html><b>" + op + "</b></html>", psi);
				psiEtp4ComboBox.setModel(tmp);
			} else {
				JOptionPane
						.showMessageDialog(null,
								"Favor selecionar um teste para o qual o laudo será escrito");
			}
		} else {
			JOptionPane
					.showMessageDialog(null,
							"Favor selecionar um candidato para o qual o laudo será escrito");
		}
	}

	@SuppressWarnings("unchecked")
	private void nPsiEtp4ButtonActionPerformed(ActionEvent e) {
		util.loadTelaCadastro("testes_ad", new JFrame(),
				"Cadastro de Testes Psicológicos");
		psiEtp4ComboBox.setModel(util.initComboBox("testes_ad",
				new String[] { "teste" }, "idtestead"));
		psiEtp4ComboBox.setSelectedIndex(-1);
		etp4TableMouseClicked(null);
	}

	private void dimEtp4ComboBoxItemStateChanged(ItemEvent evt) {
		if (tabbedPane.getSelectedIndex() == 4) {
			if (!codEtp4.trim().isEmpty()) {
				int index = dimEtp4ComboBox.getSelectedIndex();
				if (index >= 0) {
					String str = dimEtp4List.get(index);
					String laudo = getLaudo(str);
					laudoEtp4TextArea.setText(laudo);
					String txt = (String) dimEtp4ComboBox.getSelectedItem();
					if (txt.contains("<")) {
						while (txt.contains("<")) {
							txt = txt.contains(">") ? txt.replace(
									txt.substring(txt.indexOf("<"),
											txt.indexOf(">") + 1), "") : txt;
						}
						dimEtp4ComboBox.setSelectedItem(txt);
					}
				}
			}
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void gDimEtp4ButtonActionPerformed(ActionEvent evt) {
		if (!codEtp4.trim().isEmpty()) {
			String laudo = laudoEtp4TextArea.getText();
			int dim = dimEtp4ComboBox.getSelectedIndex();
			if (dim >= 0) {
				DefaultComboBoxModel tmp = (DefaultComboBoxModel) dimEtp4ComboBox
						.getModel();
				String op = formatOP((String) tmp.getElementAt(dim));
				String aux = dimEtp4List.get(dim);
				if (aux.contains("#")) {
					if (laudo.equals(getLaudo(aux))) {
						dimEtp4List.set(dim, aux.split("#")[0] + "#" + laudo
								+ ";0");
					} else {
						dimEtp4List.set(dim, aux.split("#")[0] + "#" + laudo
								+ ";1");
					}
				} else {
					dimEtp4List.set(dim, "0:" + codEtp4 + ":" + dim + "#"
							+ laudo);
				}
				tmp.removeElementAt(dim);
				tmp.insertElementAt("<html><b>" + op + "</b></html>", dim);
				dimEtp4ComboBox.setModel(tmp);
			} else {
				JOptionPane
						.showMessageDialog(null,
								"Favor selecionar um teste para o qual o laudo será escrito");
			}
		} else {
			JOptionPane
					.showMessageDialog(null,
							"Favor selecionar um candidato para o qual o laudo será escrito");
		}
	}

	@SuppressWarnings("unchecked")
	private void nDimEtp4ButtonActionPerformed(ActionEvent e) {
		util.loadTelaCadastro("dinamicas", new JFrame(),
				"Cadastro de Dinâmicas");
		dimEtp4ComboBox.setModel(util.initComboBox("dinamicas",
				new String[] { "dinamica" }, "iddinam"));
		dimEtp4ComboBox.setSelectedIndex(-1);
		etp4TableMouseClicked(null);
	}

	private void buscaButtonActionPerformed(ActionEvent evt) {
		try {
			clearLaudo();
			SQL sql = new SQL();
			carregarTabela(sql.carregarSelecaoCandidato(select()));
		} catch (NullPointerException ex) {
			Logger.getLogger(this.getName()).log(Level.SEVERE, null, ex);
		}
	}

	private void candTableMouseClicked(MouseEvent evt) {
		carregarRegistro(candTable.getSelectedRow());
	}

	private void etp1TableMouseClicked(MouseEvent evt) {
		clearLaudo();
		carregarRegistro(etp1Table.getSelectedRow());
		carregarLaudo(Integer.parseInt(codEtp1), tabbedPane.getSelectedIndex());
	}

	private void etp2TableMouseClicked(MouseEvent evt) {
		clearLaudo();
		carregarRegistro(etp2Table.getSelectedRow());
		carregarLaudo(Integer.parseInt(codEtp2), tabbedPane.getSelectedIndex());
	}

	private void etp3TableMouseClicked(MouseEvent evt) {
		clearLaudo();
		carregarRegistro(etp3Table.getSelectedRow());
		carregarLaudo(Integer.parseInt(codEtp3), tabbedPane.getSelectedIndex());
	}

	private void etp4TableMouseClicked(MouseEvent evt) {
		clearLaudo();
		carregarRegistro(etp4Table.getSelectedRow());
		carregarLaudo(Integer.parseInt(codEtp4), tabbedPane.getSelectedIndex());
	}

	private void salvaButtonActionPerformed(ActionEvent evt) {
		String linha, sql = "", cand, tipo, laudo, id, indicado;
		SQL insert = new SQL();
		Iterator<String> it;
		String[] dt, op, up;
		int teste, cont;

		switch (tabbedPane.getSelectedIndex()) {
		case 1:
			cont = 0;
			it = psiEtp1List.iterator();
			while (it.hasNext()) {
				linha = it.next();
				if (linha.contains("#")) {
					dt = linha.split("#");
					op = dt[0].split(":");
					up = dt[1].split(";");
					laudo = up[0];
					tipo = op[0];
					cand = op[1];
					teste = Integer.parseInt(op[2]) + 1;
					if (tipo.equals("1")) {
						if (up[1].equals("1")) {
							id = op[3];
							sql = "Update `" + util.getNameBD()
									+ "`.`laudo` Set `laudo`='" + laudo
									+ "', `user`='" + util.getUserSis()
									+ "', `dinamica`='0' where (`candidato`='"
									+ cand + "') and (`idlaudo`='" + id
									+ "') and (`etapa`='1');";
							cont += insert.executarSQL(sql);
						}
					} else {
						sql = "Insert Into `"
								+ util.getNameBD()
								+ "`.`laudo` (`laudo`, `teste`, `dinamica`, `candidato`, `user`, `etapa`) "
								+ "Values ('" + laudo + "', '" + teste
								+ "', '0', '" + cand + "', '"
								+ util.getUserSis() + "', '1');";
						cont += insert.executarSQL(sql);
					}
				}
			}

			it = dimEtp1List.iterator();
			while (it.hasNext()) {
				linha = it.next();
				if (linha.contains("#")) {
					dt = linha.split("#");
					op = dt[0].split(":");
					up = dt[1].split(";");
					laudo = up[0];
					tipo = op[0];
					cand = op[1];
					teste = Integer.parseInt(op[2]) + 1;
					if (tipo.equals("1")) {
						if (up[1].equals("1")) {
							id = op[3];
							sql = "Update `" + util.getNameBD()
									+ "`.`laudo` Set `laudo`='" + laudo
									+ "', `user`='" + util.getUserSis()
									+ "', `teste`='0' where (`candidato`='"
									+ cand + "') and (`idlaudo`='" + id
									+ "') and (`etapa`='1');";
							cont += insert.executarSQL(sql);
						}
					} else {
						sql = "Insert Into `"
								+ util.getNameBD()
								+ "`.`laudo` (`laudo`, `dinamica`, `teste`, `candidato`, `user`, `etapa`) "
								+ "Values ('" + laudo + "', '" + teste
								+ "', '0', '" + cand + "', '"
								+ util.getUserSis() + "', '1');";
						cont += insert.executarSQL(sql);
					}
				}
			}

			try {
				indicado = etp1buttonGroup.getSelection().getActionCommand();
				sql = "Update `" + util.getNameBD()
						+ "`.`candidato` Set `optetapaum`='" + indicado
						+ "', `user`='" + util.getUserSis()
						+ "', `dataetapaum`=now() Where (`idcandidato`='"
						+ codEtp1 + "');";
				cont += insert.executarSQL(sql);
			} catch (NullPointerException ex) {
			}

			if (cont > 0) {
				JOptionPane.showMessageDialog(null, "Dados salvos com sucesso");
			}
			etp1TableMouseClicked(null);
			break;
		case 2:
			cont = 0;
			it = psiEtp2List.iterator();
			while (it.hasNext()) {
				linha = it.next();
				if (linha.contains("#")) {
					dt = linha.split("#");
					op = dt[0].split(":");
					up = dt[1].split(";");
					laudo = up[0];
					tipo = op[0];
					cand = op[1];
					teste = Integer.parseInt(op[2]) + 1;
					if (tipo.equals("1")) {
						if (up[1].equals("1")) {
							id = op[3];
							sql = "Update `" + util.getNameBD()
									+ "`.`laudo` Set `laudo`='" + laudo
									+ "', `user`='" + util.getUserSis()
									+ "', `dinamica`='0' where (`candidato`='"
									+ cand + "') and (`idlaudo`='" + id
									+ "') and (`etapa`='2');";
							cont += insert.executarSQL(sql);
						}
					} else {
						sql = "Insert Into `"
								+ util.getNameBD()
								+ "`.`laudo` (`laudo`, `teste`, `dinamica`, `candidato`, `user`, `etapa`) "
								+ "Values ('" + laudo + "', '" + teste
								+ "', '0', '" + cand + "', '"
								+ util.getUserSis() + "', '2');";
						cont += insert.executarSQL(sql);
					}
				}
			}

			it = dimEtp2List.iterator();
			while (it.hasNext()) {
				linha = it.next();
				if (linha.contains("#")) {
					dt = linha.split("#");
					op = dt[0].split(":");
					up = dt[1].split(";");
					laudo = up[0];
					tipo = op[0];
					cand = op[1];
					teste = Integer.parseInt(op[2]) + 1;
					if (tipo.equals("1")) {
						if (up[1].equals("1")) {
							id = op[3];
							sql = "Update `" + util.getNameBD()
									+ "`.`laudo` Set `laudo`='" + laudo
									+ "', `user`='" + util.getUserSis()
									+ "', `teste`='0' where (`candidato`='"
									+ cand + "') and (`idlaudo`='" + id
									+ "') and (`etapa`='2');";
							cont += insert.executarSQL(sql);
						}
					} else {
						sql = "Insert Into `"
								+ util.getNameBD()
								+ "`.`laudo` (`laudo`, `dinamica`, `teste`, `candidato`, `user`, `etapa`) "
								+ "Values ('" + laudo + "', '" + teste
								+ "', '0', '" + cand + "', '"
								+ util.getUserSis() + "', '2');";
						cont += insert.executarSQL(sql);
					}
				}
			}

			try {
				indicado = etp2buttonGroup.getSelection().getActionCommand();
				sql = "Update `" + util.getNameBD()
						+ "`.`candidato` Set `optetapadois`='" + indicado
						+ "', `user`='" + util.getUserSis()
						+ "', `dataetapaum`=now() Where (`idcandidato`='"
						+ codEtp2 + "');";
				cont += insert.executarSQL(sql);
			} catch (NullPointerException ex) {
			}

			if (cont > 0) {
				JOptionPane.showMessageDialog(null, "Dados salvos com sucesso");
			}
			etp2TableMouseClicked(null);
			break;
		case 3:
			cont = 0;
			it = psiEtp3List.iterator();
			while (it.hasNext()) {
				linha = it.next();
				if (linha.contains("#")) {
					dt = linha.split("#");
					op = dt[0].split(":");
					up = dt[1].split(";");
					laudo = up[0];
					tipo = op[0];
					cand = op[1];
					teste = Integer.parseInt(op[2]) + 1;
					if (tipo.equals("1")) {
						if (up[1].equals("1")) {
							id = op[3];
							sql = "Update `" + util.getNameBD()
									+ "`.`laudo` Set `laudo`='" + laudo
									+ "', `user`='" + util.getUserSis()
									+ "', `dinamica`='0' where (`candidato`='"
									+ cand + "') and (`idlaudo`='" + id
									+ "') and (`etapa`='3');";
							cont += insert.executarSQL(sql);
						}
					} else {
						sql = "Insert Into `"
								+ util.getNameBD()
								+ "`.`laudo` (`laudo`, `teste`, `dinamica`, `candidato`, `user`, `etapa`) "
								+ "Values ('" + laudo + "', '" + teste
								+ "', '0', '" + cand + "', '"
								+ util.getUserSis() + "', '3');";
						cont += insert.executarSQL(sql);
					}
				}
			}

			it = dimEtp3List.iterator();
			while (it.hasNext()) {
				linha = it.next();
				if (linha.contains("#")) {
					dt = linha.split("#");
					op = dt[0].split(":");
					up = dt[1].split(";");
					laudo = up[0];
					tipo = op[0];
					cand = op[1];
					teste = Integer.parseInt(op[2]) + 1;
					if (tipo.equals("1")) {
						if (up[1].equals("1")) {
							id = op[3];
							sql = "Update `" + util.getNameBD()
									+ "`.`laudo` Set `laudo`='" + laudo
									+ "', `user`='" + util.getUserSis()
									+ "', `teste`='0' where (`candidato`='"
									+ cand + "') and (`idlaudo`='" + id
									+ "') and (`etapa`='3');";
							cont += insert.executarSQL(sql);
						}
					} else {
						sql = "Insert Into `"
								+ util.getNameBD()
								+ "`.`laudo` (`laudo`, `dinamica`, `teste`, `candidato`, `user`, `etapa`) "
								+ "Values ('" + laudo + "', '" + teste
								+ "', '0', '" + cand + "', '"
								+ util.getUserSis() + "', '3');";
						cont += insert.executarSQL(sql);
					}
				}
			}

			try {
				indicado = etp3buttonGroup.getSelection().getActionCommand();
				sql = "Update `" + util.getNameBD()
						+ "`.`candidato` Set `optetapatres`='" + indicado
						+ "', `user`='" + util.getUserSis()
						+ "', `dataetapaum`=now() Where (`idcandidato`='"
						+ codEtp3 + "');";
				cont += insert.executarSQL(sql);
			} catch (NullPointerException ex) {
			}

			if (cont > 0) {
				JOptionPane.showMessageDialog(null, "Dados salvos com sucesso");
			}
			etp3TableMouseClicked(null);
			break;
		case 4:
			cont = 0;
			it = psiEtp4List.iterator();
			while (it.hasNext()) {
				linha = it.next();
				if (linha.contains("#")) {
					dt = linha.split("#");
					op = dt[0].split(":");
					up = dt[1].split(";");
					laudo = up[0];
					tipo = op[0];
					cand = op[1];
					teste = Integer.parseInt(op[2]) + 1;
					if (tipo.equals("1")) {
						if (up[1].equals("1")) {
							id = op[3];
							sql = "Update `" + util.getNameBD()
									+ "`.`laudo` Set `laudo`='" + laudo
									+ "', `user`='" + util.getUserSis()
									+ "', `dinamica`='0' where (`candidato`='"
									+ cand + "') and (`idlaudo`='" + id
									+ "') and (`etapa`='4');";
							cont += insert.executarSQL(sql);
						}
					} else {
						sql = "Insert Into `"
								+ util.getNameBD()
								+ "`.`laudo` (`laudo`, `teste`, `dinamica`, `candidato`, `user`, `etapa`) "
								+ "Values ('" + laudo + "', '" + teste
								+ "', '0', '" + cand + "', '"
								+ util.getUserSis() + "', '4');";
						cont += insert.executarSQL(sql);
					}
				}
			}

			it = dimEtp4List.iterator();
			while (it.hasNext()) {
				linha = it.next();
				if (linha.contains("#")) {
					dt = linha.split("#");
					op = dt[0].split(":");
					up = dt[1].split(";");
					laudo = up[0];
					tipo = op[0];
					cand = op[1];
					teste = Integer.parseInt(op[2]) + 1;
					if (tipo.equals("1")) {
						if (up[1].equals("1")) {
							id = op[3];
							sql = "Update `" + util.getNameBD()
									+ "`.`laudo` Set `laudo`='" + laudo
									+ "', `user`='" + util.getUserSis()
									+ "', `teste`='0' where (`candidato`='"
									+ cand + "') and (`idlaudo`='" + id
									+ "') and (`etapa`='4');";
							cont += insert.executarSQL(sql);
						}
					} else {
						sql = "Insert Into `"
								+ util.getNameBD()
								+ "`.`laudo` (`laudo`, `dinamica`, `teste`, `candidato`, `user`, `etapa`) "
								+ "Values ('" + laudo + "', '" + teste
								+ "', '0', '" + cand + "', '"
								+ util.getUserSis() + "', '4');";
						cont += insert.executarSQL(sql);
					}
				}
			}

			try {
				indicado = etp4buttonGroup.getSelection().getActionCommand();
				sql = "Update `" + util.getNameBD()
						+ "`.`candidato` Set `optetapaquatro`='" + indicado
						+ "', `user`='" + util.getUserSis()
						+ "', `dataetapaum`=now() Where (`idcandidato`='"
						+ codEtp4 + "');";
				cont += insert.executarSQL(sql);
			} catch (NullPointerException ex) {
			}

			if (cont > 0) {
				JOptionPane.showMessageDialog(null, "Dados salvos com sucesso");
			}
			etp4TableMouseClicked(null);
			break;
		}

	}

	private void imprimeButtonActionPerformed(ActionEvent evt) {
		OptionLaudo opt = new OptionLaudo();
		// switch (tabbedPane.getSelectedIndex()) {
		// case 1:
		// opt.setCand(codEtp1);
		// opt.setEtp("1");
		// break;
		// case 2:
		// opt.setCand(codEtp2);
		// opt.setEtp("2");
		// break;
		// case 3:
		// opt.setCand(codEtp3);
		// opt.setEtp("3");
		// break;
		// case 4:
		// opt.setCand(codEtp4);
		// opt.setEtp("4");
		// break;
		// default:
		// opt.setCand((String) candTable.getModel().getValueAt(
		// candTable.getSelectedRow(), 14));
		// break;
		// }
		opt.setVisible(true);
	}

	private String select() throws NullPointerException {
		String sql = "";
		if (!areaList.isSelectionEmpty()) {
			if (areaList.getSelectedIndex() > 0) {
				int[] area = areaList.getSelectedIndices();
				sql += " and (";
				if (area.length > 1) {
					for (int i = 0; i < area.length - 1; ++i) {
						sql += "(`areaum`='" + area[i] + "') or (`areadois`='"
								+ area[i] + "') or ";
					}
					sql += "(`areaum`='" + area[area.length - 1]
							+ "') or (`areadois`='" + area[area.length - 1]
							+ "'))";
				} else {
					sql += "(`areaum`='" + area[0] + "') or (`areadois`='"
							+ area[0] + "'))";
				}
			} else {
				sql += "#";
			}
		}
		if (!escList.isSelectionEmpty()) {
			if (escList.getSelectedIndex() > 0) {
				int[] esc = escList.getSelectedIndices();
				sql += " and (";
				if (esc.length > 1) {
					for (int i = 0; i < esc.length - 1; ++i) {
						sql += "(`escolaridade`='" + esc[i] + "') or ";
					}
					sql += "(`escolaridade`='" + esc[esc.length - 1] + "'))";
				} else {
					sql += "(`escolaridade`='" + esc[0] + "'))";
				}
			} else {
				sql += "#";
			}
		}
		if (!expList.isSelectionEmpty()) {
			if (expList.getSelectedIndex() > 0) {
				int[] exp = expList.getSelectedIndices();
				sql += " and (";
				if (exp.length > 1) {
					for (int i = 0; i < exp.length - 1; ++i) {
						sql += "(`totalexp`>='" + exp[i] + "') or ";
					}
					sql += "(`totalexp`>='" + exp[exp.length - 1] + "'))";
				} else {
					sql += "(`totalexp`>='" + exp[0] + "'))";
				}
			} else {
				sql += "#";
			}
		}
		if (!dispList.isSelectionEmpty()) {
			if (dispList.getSelectedIndex() > 0) {
				int[] disp = dispList.getSelectedIndices();
				sql += " and (";
				if (disp.length > 1) {
					for (int i = 0; i < disp.length - 1; ++i) {
						sql += "(`iniciar`<='" + disp[i] + "') or ";
					}
					sql += "(`iniciar`<='" + disp[disp.length - 1] + "'))";
				} else {
					sql += "(`iniciar`<='" + disp[0] + "'))";
				}
			} else {
				sql += "#";
			}
		}
		if (sql.trim().isEmpty()) {
			JOptionPane
					.showMessageDialog(null,
							"Favor selecione os criterios de pesquisa antes de continuar");
			throw new NullPointerException();
		} else {
			return sql.replaceAll("#", "");
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void carregarLaudo(int candidato, int etapa) {
		try {
			SQL sql = new SQL();
			ResultSet rs = sql.carregarLaudo(candidato, etapa);
			DefaultComboBoxModel psico = new DefaultComboBoxModel();
			DefaultComboBoxModel dinam = new DefaultComboBoxModel();
			int psi, din, id;
			String laudo, cand, op;
			switch (etapa) {
			case 1:
				psico = (DefaultComboBoxModel) psiEtp1ComboBox.getModel();
				dinam = (DefaultComboBoxModel) dimEtp1ComboBox.getModel();
				cand = codEtp1;
				while (rs.next()) {
					psi = rs.getInt("teste") - 1;
					din = rs.getInt("dinamica") - 1;
					id = rs.getInt("id");
					laudo = rs.getString("laudo");
					if (psi >= 0) {
						op = formatOP((String) psico.getElementAt(psi));
						psiEtp1List.set(psi, "1:" + cand + ":" + psi + ":" + id
								+ "#" + laudo + ";0");
						psico.removeElementAt(psi);
						psico.insertElementAt("<html><b>" + op + "</b></html>",
								psi);
					}
					if (din >= 0) {
						op = formatOP((String) dinam.getElementAt(din));
						dimEtp1List.set(din, "1:" + cand + ":" + din + ":" + id
								+ "#" + laudo + ";0");
						dinam.removeElementAt(din);
						dinam.insertElementAt("<html><b>" + op + "</b></html>",
								din);
					}
				}
				psiEtp1ComboBox.setModel(psico);
				dimEtp1ComboBox.setModel(dinam);
				break;
			case 2:
				psico = (DefaultComboBoxModel) psiEtp2ComboBox.getModel();
				dinam = (DefaultComboBoxModel) dimEtp2ComboBox.getModel();
				cand = codEtp2;
				while (rs.next()) {
					psi = rs.getInt("teste") - 1;
					din = rs.getInt("dinamica") - 1;
					id = rs.getInt("id");
					laudo = rs.getString("laudo");
					if (psi >= 0) {
						op = formatOP((String) psico.getElementAt(psi));
						psiEtp2List.set(psi, "1:" + cand + ":" + psi + ":" + id
								+ "#" + laudo + ";0");
						psico.removeElementAt(psi);
						psico.insertElementAt("<html><b>" + op + "</b></html>",
								psi);
					}
					if (din >= 0) {
						op = formatOP((String) dinam.getElementAt(din));
						dimEtp2List.set(din, "1:" + cand + ":" + din + ":" + id
								+ "#" + laudo + ";0");
						dinam.removeElementAt(din);
						dinam.insertElementAt("<html><b>" + op + "</b></html>",
								din);
					}
				}
				psiEtp2ComboBox.setModel(psico);
				dimEtp2ComboBox.setModel(dinam);
				break;
			case 3:
				psico = (DefaultComboBoxModel) psiEtp3ComboBox.getModel();
				dinam = (DefaultComboBoxModel) dimEtp3ComboBox.getModel();
				cand = codEtp3;
				while (rs.next()) {
					psi = rs.getInt("teste") - 1;
					din = rs.getInt("dinamica") - 1;
					id = rs.getInt("id");
					laudo = rs.getString("laudo");
					if (psi >= 0) {
						op = formatOP((String) psico.getElementAt(psi));
						psiEtp3List.set(psi, "1:" + cand + ":" + psi + ":" + id
								+ "#" + laudo + ";0");
						psico.removeElementAt(psi);
						psico.insertElementAt("<html><b>" + op + "</b></html>",
								psi);
					}
					if (din >= 0) {
						op = formatOP((String) dinam.getElementAt(din));
						dimEtp3List.set(din, "1:" + cand + ":" + din + ":" + id
								+ "#" + laudo + ";0");
						dinam.removeElementAt(din);
						dinam.insertElementAt("<html><b>" + op + "</b></html>",
								din);
					}
				}
				psiEtp3ComboBox.setModel(psico);
				dimEtp3ComboBox.setModel(dinam);
				break;
			case 4:
				psico = (DefaultComboBoxModel) psiEtp4ComboBox.getModel();
				dinam = (DefaultComboBoxModel) dimEtp4ComboBox.getModel();
				cand = codEtp4;
				while (rs.next()) {
					psi = rs.getInt("teste") - 1;
					din = rs.getInt("dinamica") - 1;
					laudo = rs.getString("laudo");
					id = rs.getInt("id");
					if (psi >= 0) {
						op = formatOP((String) psico.getElementAt(psi));
						psiEtp4List.set(psi, "1:" + cand + ":" + psi + ":" + id
								+ "#" + laudo + ";0");
						psico.removeElementAt(psi);
						psico.insertElementAt("<html><b>" + op + "</b></html>",
								psi);
					}
					if (din >= 0) {
						op = formatOP((String) dinam.getElementAt(din));
						dimEtp4List.set(din, "1:" + cand + ":" + din + ":" + id
								+ "#" + laudo + ";0");
						dinam.removeElementAt(din);
						dinam.insertElementAt("<html><b>" + op + "</b></html>",
								din);
					}
				}
				psiEtp4ComboBox.setModel(psico);
				dimEtp4ComboBox.setModel(dinam);
				break;
			}
			rs.close();
			sql.close();
		} catch (SQLException ex) {
			Logger.getLogger(this.getName()).log(Level.SEVERE, null, ex);
		}
	}

	private void carregarRegistro(int cod) {
		DefaultTableModel tabela;
		String foto;
		int op;
		switch (tabbedPane.getSelectedIndex()) {
		case 1:
			tabela = (DefaultTableModel) etp1Table.getModel();
			nomeEtp1TextField.setText((String) tabela.getValueAt(cod, 0));
			try {
				foto = (String) tabela.getValueAt(cod, 9);
				fotoEtp1Label.setIcon(new ImageIcon(ImageIO
						.read(new File(foto))));
			} catch (IOException ex) {

			}
			etp1buttonGroup.clearSelection();
			op = (int) tabela.getValueAt(cod, 10);
			simIndEtp1RadioButton.setSelected(op > 0);
			naoIndEtp1RadioButton.setSelected(op == 0);
			codEtp1 = (String) tabela.getValueAt(cod, 14);
			break;
		case 2:
			tabela = (DefaultTableModel) etp2Table.getModel();
			nomeEtp2TextField.setText((String) tabela.getValueAt(cod, 0));
			try {
				foto = (String) tabela.getValueAt(cod, 9);
				fotoEtp2Label.setIcon(new ImageIcon(ImageIO
						.read(new File(foto))));
			} catch (IOException ex) {

			}
			etp2buttonGroup.clearSelection();
			op = (int) tabela.getValueAt(cod, 11);
			simIndEtp2RadioButton.setSelected(op > 0);
			naoIndEtp2RadioButton.setSelected(op == 0);
			codEtp2 = (String) tabela.getValueAt(cod, 14);
			break;
		case 3:
			tabela = (DefaultTableModel) etp3Table.getModel();
			nomeEtp3TextField.setText((String) tabela.getValueAt(cod, 0));
			try {
				foto = (String) tabela.getValueAt(cod, 9);
				fotoEtp3Label.setIcon(new ImageIcon(ImageIO
						.read(new File(foto))));
			} catch (IOException ex) {

			}
			etp3buttonGroup.clearSelection();
			op = (int) tabela.getValueAt(cod, 12);
			simIndEtp3RadioButton.setSelected(op > 0);
			naoIndEtp3RadioButton.setSelected(op == 0);
			codEtp3 = (String) tabela.getValueAt(cod, 14);
			break;
		case 4:
			tabela = (DefaultTableModel) etp4Table.getModel();
			nomeEtp4TextField.setText((String) tabela.getValueAt(cod, 0));
			try {
				foto = (String) tabela.getValueAt(cod, 9);
				fotoEtp4Label.setIcon(new ImageIcon(ImageIO
						.read(new File(foto))));
			} catch (IOException ex) {

			}
			etp4buttonGroup.clearSelection();
			op = (int) tabela.getValueAt(cod, 13);
			simIndEtp4RadioButton.setSelected(op > 0);
			naoIndEtp4RadioButton.setSelected(op == 0);
			codEtp4 = (String) tabela.getValueAt(cod, 14);
			break;
		default:
			tabela = (DefaultTableModel) candTable.getModel();
			nomeTextField.setText((String) tabela.getValueAt(cod, 0));
			celFormattedTextField.setText((String) tabela.getValueAt(cod, 1));
			foneFormattedTextField.setText((String) tabela.getValueAt(cod, 2));
			endTextField.setText((String) tabela.getValueAt(cod, 3));
			cepFormattedTextField.setText((String) tabela.getValueAt(cod, 4));
			estTextField.setText((String) tabela.getValueAt(cod, 5));
			cidTextField.setText((String) tabela.getValueAt(cod, 6));

			estCivilTextField.setText((String) tabela.getValueAt(cod, 7));
			sexoTextField.setText((String) tabela.getValueAt(cod, 8));
			try {
				foto = (String) tabela.getValueAt(cod, 9);
				fotoLabel.setIcon(new ImageIcon(ImageIO.read(new File(foto))));
			} catch (IOException ex) {

			}
			break;
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void carregarTabela(ResultSet candidato) {
		try {
			Vector<String> tableHeaders = new Vector<String>();
			Vector[] tableData = new Vector[] { new Vector(), new Vector(),
					new Vector(), new Vector(), new Vector() };
			SQL sql = new SQL();
			int code = -1;
			ResultSet rs;
			tableHeaders.add("Nome");
			tableHeaders.add("Celular");
			tableHeaders.add("Telefone");
			tableHeaders.add("Endereço");
			tableHeaders.add("CEP");
			tableHeaders.add("Estado");
			tableHeaders.add("Cidade");
			tableHeaders.add("Civil");
			tableHeaders.add("Sexo");
			tableHeaders.add("Foto");
			tableHeaders.add("Etapa 1");
			tableHeaders.add("Etapa 2");
			tableHeaders.add("Etapa 3");
			tableHeaders.add("Etapa 4");
			tableHeaders.add("Codigo");
			while (candidato.next()) {
				Vector<Object> oneRow = new Vector<Object>();
				oneRow.add(candidato.getString("nome"));
				oneRow.add(candidato.getString("celular"));
				oneRow.add(candidato.getString("fone"));
				oneRow.add(candidato.getString("endereco"));
				oneRow.add(candidato.getString("cep"));
				code = candidato.getInt("estado");
				if (code > 0) {
					rs = sql.query("Select `state`, `uf` from `"
							+ util.getNameBD()
							+ "`.`estado` Where (`active`='1') and (`idestado`='"
							+ code + "');");
					rs.next();
					oneRow.add(rs.getString("uf") + " - "
							+ rs.getString("state"));
				} else {
					oneRow.add("");
				}
				code = candidato.getInt("cidade");
				if (code > 0) {
					rs = sql.query("Select `city` from `"
							+ util.getNameBD()
							+ "`.`cidade` Where (`active`='1') and (`idcidade`='"
							+ code + "');");
					rs.next();
					oneRow.add(rs.getString("city"));
				} else {
					oneRow.add("");
				}
				code = candidato.getInt("estcivil");
				if (code > 0) {
					rs = sql.query("Select `civil` from `"
							+ util.getNameBD()
							+ "`.`est_civil` Where (`active`='1') and (`idecivil`='"
							+ code + "');");
					rs.next();
					oneRow.add(rs.getString("civil"));
				} else {
					oneRow.add("");
				}
				code = candidato.getInt("sexo");
				if (code > 0) {
					rs = sql.query("Select `sexo` from `" + util.getNameBD()
							+ "`.`sexo` Where (`active`='1') and (`idsexo`='"
							+ code + "');");
					rs.next();
					oneRow.add(rs.getString("sexo"));
				} else {
					oneRow.add("");
				}
				oneRow.add(candidato.getString("foto"));
				oneRow.add(candidato.getInt("optetapaum"));
				oneRow.add(candidato.getInt("optetapadois"));
				oneRow.add(candidato.getInt("optetapatres"));
				oneRow.add(candidato.getInt("optetapaquatro"));
				oneRow.add(candidato.getString("idcandidato"));
				tableData[0].add(oneRow);
				tableData[1].add(oneRow);
				if (candidato.getInt("optetapaum") > 0) {
					tableData[2].add(oneRow);
				}
				if (candidato.getInt("optetapadois") > 0) {
					tableData[3].add(oneRow);
				}
				if (candidato.getInt("optetapatres") > 0) {
					tableData[4].add(oneRow);
				}
			}
			candTable
					.setModel(new DefaultTableModel(tableData[0], tableHeaders));
			etp1Table
					.setModel(new DefaultTableModel(tableData[1], tableHeaders));
			etp2Table
					.setModel(new DefaultTableModel(tableData[2], tableHeaders));
			etp3Table
					.setModel(new DefaultTableModel(tableData[3], tableHeaders));
			etp4Table
					.setModel(new DefaultTableModel(tableData[4], tableHeaders));
			candTable.getColumnModel().getColumn(0).setPreferredWidth(200);
			candTable.getColumnModel().getColumn(1).setPreferredWidth(150);
			candTable.getColumnModel().getColumn(2).setPreferredWidth(150);
			candTable.getColumnModel().getColumn(3).setPreferredWidth(350);
			candTable.getColumnModel().getColumn(4).setPreferredWidth(100);
			util.ocultarColunas(candTable, 5, 14);
			etp1Table.getColumnModel().getColumn(0).setPreferredWidth(200);
			etp1Table.getColumnModel().getColumn(1).setPreferredWidth(150);
			etp1Table.getColumnModel().getColumn(2).setPreferredWidth(150);
			etp1Table.getColumnModel().getColumn(3).setPreferredWidth(350);
			etp1Table.getColumnModel().getColumn(4).setPreferredWidth(100);
			util.ocultarColunas(etp1Table, 5, 14);
			etp2Table.getColumnModel().getColumn(0).setPreferredWidth(200);
			etp2Table.getColumnModel().getColumn(1).setPreferredWidth(150);
			etp2Table.getColumnModel().getColumn(2).setPreferredWidth(150);
			etp2Table.getColumnModel().getColumn(3).setPreferredWidth(350);
			etp2Table.getColumnModel().getColumn(4).setPreferredWidth(100);
			util.ocultarColunas(etp2Table, 5, 14);
			etp3Table.getColumnModel().getColumn(0).setPreferredWidth(200);
			etp3Table.getColumnModel().getColumn(1).setPreferredWidth(150);
			etp3Table.getColumnModel().getColumn(2).setPreferredWidth(150);
			etp3Table.getColumnModel().getColumn(3).setPreferredWidth(350);
			etp3Table.getColumnModel().getColumn(4).setPreferredWidth(100);
			util.ocultarColunas(etp3Table, 5, 14);
			etp4Table.getColumnModel().getColumn(0).setPreferredWidth(200);
			etp4Table.getColumnModel().getColumn(1).setPreferredWidth(150);
			etp4Table.getColumnModel().getColumn(2).setPreferredWidth(150);
			etp4Table.getColumnModel().getColumn(3).setPreferredWidth(350);
			etp4Table.getColumnModel().getColumn(4).setPreferredWidth(100);
			util.ocultarColunas(etp4Table, 5, 14);
			candidato.close();
		} catch (SQLException ex) {
			Logger.getLogger(this.getName()).log(Level.SEVERE, null, ex);
		}
	}

	private String formatOP(String op) {
		op = (op.contains("<html><b>") ? op.replaceAll("<html><b>", "") : op);
		op = (op.contains("</b></html>") ? op.replaceAll("</b></html>", "")
				: op);
		return op;
	}

	private String getLaudo(String str) {
		int start, end;
		start = str.contains("#") ? str.indexOf("#") + 1 : 0;
		end = str.contains(";") ? str.lastIndexOf(";") : str.length();
		String laudo = str.contains("#") ? str.substring(start, end) : str;
		return laudo;
	}

	@SuppressWarnings("unchecked")
	private void clearLaudo() {
		try {
			fotoEtp1Label.setIcon(new ImageIcon(getClass().getResource(
					"/img/nobody.png")));
			fotoEtp2Label.setIcon(new ImageIcon(getClass().getResource(
					"/img/nobody.png")));
			fotoEtp3Label.setIcon(new ImageIcon(getClass().getResource(
					"/img/nobody.png")));
			fotoEtp4Label.setIcon(new ImageIcon(getClass().getResource(
					"/img/nobody.png")));
			etp1buttonGroup.clearSelection();
			etp2buttonGroup.clearSelection();
			etp3buttonGroup.clearSelection();
			etp4buttonGroup.clearSelection();
			nomeEtp1TextField.setText("");
			nomeEtp2TextField.setText("");
			nomeEtp3TextField.setText("");
			nomeEtp4TextField.setText("");
			laudoEtp1TextArea.setText("");
			laudoEtp2TextArea.setText("");
			laudoEtp3TextArea.setText("");
			laudoEtp4TextArea.setText("");
			psiEtp1List.clear();
			psiEtp2List.clear();
			psiEtp3List.clear();
			psiEtp4List.clear();
			dimEtp1List.clear();
			dimEtp2List.clear();
			dimEtp3List.clear();
			dimEtp4List.clear();
			SQL sql = new SQL();
			String str = "";
			ResultSet rs = sql.carregarComboBox("testes_ad", "teste",
					"idtestead");
			while (rs.next()) {
				psiEtp1List.add(str);
				psiEtp2List.add(str);
				psiEtp3List.add(str);
				psiEtp4List.add(str);
			}
			psiEtp1ComboBox.setModel(util.initComboBox("testes_ad",
					new String[] { "teste" }, "idtestead"));
			psiEtp2ComboBox.setModel(util.initComboBox("testes_ad",
					new String[] { "teste" }, "idtestead"));
			psiEtp3ComboBox.setModel(util.initComboBox("testes_ad",
					new String[] { "teste" }, "idtestead"));
			psiEtp4ComboBox.setModel(util.initComboBox("testes_ad",
					new String[] { "teste" }, "idtestead"));
			psiEtp1ComboBox.setSelectedIndex(-1);
			psiEtp2ComboBox.setSelectedIndex(-1);
			psiEtp3ComboBox.setSelectedIndex(-1);
			psiEtp4ComboBox.setSelectedIndex(-1);

			rs = sql.carregarComboBox("dinamicas", "dinamica", "iddinam");
			while (rs.next()) {
				str = rs.getString("dinamica");
				dimEtp1List.add(str);
				dimEtp2List.add(str);
				dimEtp3List.add(str);
				dimEtp4List.add(str);
			}
			dimEtp1ComboBox.setModel(util.initComboBox("dinamicas",
					new String[] { "dinamica" }, "iddinam"));
			dimEtp2ComboBox.setModel(util.initComboBox("dinamicas",
					new String[] { "dinamica" }, "iddinam"));
			dimEtp3ComboBox.setModel(util.initComboBox("dinamicas",
					new String[] { "dinamica" }, "iddinam"));
			dimEtp4ComboBox.setModel(util.initComboBox("dinamicas",
					new String[] { "dinamica" }, "iddinam"));
			dimEtp1ComboBox.setSelectedIndex(-1);
			dimEtp2ComboBox.setSelectedIndex(-1);
			dimEtp3ComboBox.setSelectedIndex(-1);
			dimEtp4ComboBox.setSelectedIndex(-1);
			rs.close();
			sql.close();
		} catch (SQLException ex) {
			Logger.getLogger(this.getName()).log(Level.SEVERE, null, ex);
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void initLista() {
		try {
			SQL sql = new SQL();
			ResultSet rs = sql.carregarComboBox("tipo_curso", "tipo",
					"idtcurso");
			DefaultListModel lista = new DefaultListModel();
			lista.addElement("--TODOS--");
			while (rs.next()) {
				lista.addElement(rs.getString("tipo"));
			}
			escList.setModel(lista);
			escList.setSelectedIndex(-1);

			rs = sql.carregarComboBox("tempo_exper", "tempo", "idtexper");
			lista = new DefaultListModel();
			lista.addElement("--TODOS--");
			while (rs.next()) {
				lista.addElement(rs.getString("tempo"));
			}
			expList.setModel(lista);
			expList.setSelectedIndex(-1);

			rs = sql.carregarComboBox("departamento", "depto", "iddepto");
			lista = new DefaultListModel();
			lista.addElement("--TODOS--");
			while (rs.next()) {
				lista.addElement(rs.getString("depto"));
			}
			areaList.setModel(lista);
			areaList.setSelectedIndex(-1);

			rs = sql.carregarComboBox("ini_traba", "iniciar", "iditraba");
			lista = new DefaultListModel();
			lista.addElement("--TODOS--");
			while (rs.next()) {
				lista.addElement(rs.getString("iniciar"));
			}
			dispList.setModel(lista);
			dispList.setSelectedIndex(-1);

			clearLaudo();

			rs.close();
			sql.close();
		} catch (SQLException ex) {
			Logger.getLogger(this.getName()).log(Level.SEVERE, null, ex);
		}
	}

	private final java.util.ArrayList<String> psiEtp1List = new java.util.ArrayList<>();
	private final java.util.ArrayList<String> psiEtp2List = new java.util.ArrayList<>();
	private final java.util.ArrayList<String> psiEtp3List = new java.util.ArrayList<>();
	private final java.util.ArrayList<String> psiEtp4List = new java.util.ArrayList<>();
	private final java.util.ArrayList<String> dimEtp1List = new java.util.ArrayList<>();
	private final java.util.ArrayList<String> dimEtp2List = new java.util.ArrayList<>();
	private final java.util.ArrayList<String> dimEtp3List = new java.util.ArrayList<>();
	private final java.util.ArrayList<String> dimEtp4List = new java.util.ArrayList<>();
	private final Util util = new Util();
	private String codEtp1 = "";
	private String codEtp2 = "";
	private String codEtp3 = "";
	private String codEtp4 = "";
	private JButton gPsiEtp3Button;
	private JButton nPsiEtp3Button;
	private JButton nDimEtp3Button;
	private JButton gDimEtp3Button;
	private JButton gPsiEtp4Button;
	private JButton nPsiEtp4Button;
	private JButton nDimEtp4Button;
	private JButton gDimEtp4Button;
	private JButton gPsiEtp1Button;
	private JButton nPsiEtp1Button;
	private JButton nDimEtp1Button;
	private JButton gDimEtp1Button;
	private JButton gPsiEtp2Button;
	private JButton nPsiEtp2Button;
	private JButton nDimEtp2Button;
	private JButton gDimEtp2Button;
	private ButtonGroup etp1buttonGroup;
	private ButtonGroup etp2buttonGroup;
	private ButtonGroup etp3buttonGroup;
	private ButtonGroup etp4buttonGroup;
	private AComboBox psiEtp3ComboBox;
	private AComboBox dimEtp3ComboBox;
	private AComboBox psiEtp4ComboBox;
	private AComboBox dimEtp4ComboBox;
	private AComboBox psiEtp1ComboBox;
	private AComboBox dimEtp1ComboBox;
	private AComboBox psiEtp2ComboBox;
	private AComboBox dimEtp2ComboBox;
	private JEditorPane editorPane;
	private JFormattedTextField celFormattedTextField;
	private JFormattedTextField foneFormattedTextField;
	private JFormattedTextField cepFormattedTextField;
	private JLabel fotoLabel;
	private JLabel lblCidade;
	private JLabel lblOutro;
	private JLabel lblEstadoCivil;
	private JLabel lblEndereo;
	private JLabel fotoEtp1Label;
	private JLabel label_2;
	private JLabel label_3;
	private JLabel lblEscolaridade;
	private JLabel lblExperincia;
	private JLabel lblDisponibilidade;
	private JLabel lblOutro_1;
	private JLabel lblOutro_2;
	private JLabel lblNome;
	private JLabel fotoEtp3Label;
	private JLabel label_11;
	private JLabel label_12;
	private JLabel label_15;
	private JLabel lblSexo;
	private JLabel lblCep;
	private JLabel fotoEtp4Label;
	private JLabel label_17;
	private JLabel label_18;
	private JLabel lblEstado;
	private JLabel label_21;
	private JLabel lblCelular;
	private JLabel lblLaudo;
	private JLabel fotoEtp2Label;
	private JLabel label_5;
	private JLabel label_6;
	private JLabel label_9;
	private JLabel lblreas;
	@SuppressWarnings("rawtypes")
	private JList areaList;
	@SuppressWarnings("rawtypes")
	private JList escList;
	@SuppressWarnings("rawtypes")
	private JList expList;
	@SuppressWarnings("rawtypes")
	private JList dispList;
	@SuppressWarnings("rawtypes")
	private JList outroList;
	private JPanel panel;
	private JPanel selectPanel;
	private JPanel panel_7;
	private JPanel grupoPanel;
	private JPanel panel_8;
	private JPanel elaboracaoPanel;
	private JPanel panel_9;
	private JPanel encerramentoPanel;
	private JPanel panel_11;
	private JPanel individualPanel;
	private JPanel panel_4;
	private JRadioButton simIndEtp1RadioButton;
	private JRadioButton naoIndEtp1RadioButton;
	private JRadioButton simIndEtp3RadioButton;
	private JRadioButton naoIndEtp3RadioButton;
	private JRadioButton simIndEtp4RadioButton;
	private JRadioButton naoIndEtp4RadioButton;
	private JRadioButton simIndEtp2RadioButton;
	private JRadioButton naoIndEtp2RadioButton;
	private JScrollPane scrollPane;
	private JScrollPane scrollPane_1;
	private JScrollPane scrollPane_2;
	private JScrollPane scrollPane_3;
	private JScrollPane scrollPane_4;
	private JScrollPane scrollPane_5;
	private JScrollPane scrollPane_6;
	private JScrollPane scrollPane_7;
	private JScrollPane scrollPane_8;
	private JScrollPane scrollPane_9;
	private JScrollPane scrollPane_10;
	private JScrollPane scrollPane_11;
	private JScrollPane scrollPane_12;
	private JScrollPane scrollPane_13;
	private JScrollPane scrollPane_14;
	private JTabbedPane tabbedPane;
	private TTable candTable;
	private TTable etp1Table;
	private TTable etp2Table;
	private TTable etp3Table;
	private TTable etp4Table;
	private JTextArea laudoEtp2TextArea;
	private JTextArea laudoEtp3TextArea;
	private JTextArea laudoEtp4TextArea;
	private JTextArea laudoEtp1TextArea;
	private JTextField nomeTextField;
	private JTextField endTextField;
	private JTextField nomeEtp1TextField;
	private JTextField estTextField;
	private JTextField estCivilTextField;
	private JTextField sexoTextField;
	private JTextField nomeEtp2TextField;
	private JTextField nomeEtp3TextField;
	private JTextField nomeEtp4TextField;
	private JButton buscaButton;
	private JButton salvaButton;
	private JButton imprimeButton;
	private JTextField cidTextField;
	private JPanel panel_1;
	private JLabel lblTeste;
	private JPanel panel_2;
	private JPanel panel_3;
	private JPanel panel_5;
	private JPanel panel_6;
	private JPanel panel_10;
	private JLabel lblTeste_2;
	private JLabel lblDinamica_2;
	private JPanel panel_12;
	private JPanel panel_13;
	private JLabel lblTeste_3;
	private JLabel lblDinamica_3;
	private JButton hDimEtp1Button;
}
