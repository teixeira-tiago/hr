/**
 * 
 */
package recrutamento;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;
import javax.swing.ButtonGroup;
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
import utiliter.component.TTable;

/**
 * @author Tiago A. Teixeira {@link tiago.a.teixeira@acm.org}
 * 
 */
@SuppressWarnings("serial")
public class Selecao extends JInternalFrame {

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					Selecao window = new Selecao();
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
	public Selecao() {
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
		setBounds(0, 0, 765, 650);

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
						"5[25px]0[5px:5px]0[120px:120px]0[5px:5px]0[24px:24px]0[5px:5px]0[24px:24px,grow]5"));

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

		lblLaudo = new JLabel("Laudo:");
		grupoPanel.add(lblLaudo, "cell 0 4,alignx left,aligny top");

		scrollPane_7 = new JScrollPane();
		grupoPanel.add(scrollPane_7, "cell 0 6 8 1,grow");

		laudoEtp1TextArea = new JTextArea();
		scrollPane_7.setViewportView(laudoEtp1TextArea);

		individualPanel = new JPanel();
		tabbedPane.addTab("Entrevista Individual", null, individualPanel, null);
		// tabbedPane.addTab("Individual", null, individualPanel, null);
		individualPanel
				.setLayout(new MigLayout(
						"",
						"5[137px:137px]0[5px:5px]0[41px]0[380px,grow]0[59px]0[50px]0[5px:5px]0[50px]5",
						"5[25px]0[5px:5px]0[120px:120px]0[5px:5px]0[24px:24px]0[5px:5px]0[24px:24px,grow]5"));

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

		label_9 = new JLabel("Laudo:");
		individualPanel.add(label_9, "cell 0 4,alignx left,aligny top");

		scrollPane_10 = new JScrollPane();
		individualPanel.add(scrollPane_10, "cell 0 6 8 1,grow");

		laudoEtp2TextArea = new JTextArea();
		scrollPane_10.setViewportView(laudoEtp2TextArea);

		elaboracaoPanel = new JPanel();
		tabbedPane.addTab("Elaboração do Parecer", null, elaboracaoPanel, null);
		// tabbedPane.addTab("Parecer", null, elaboracaoPanel, null);
		elaboracaoPanel
				.setLayout(new MigLayout(
						"",
						"5[137px:137px]0[5px:5px]0[41px]0[380px,grow]0[59px]0[50px]0[5px:5px]0[50px]5",
						"5[25px]0[5px:5px]0[120px:120px]0[5px:5px]0[24px:24px]0[5px:5px]0[24px:24px,grow]5"));

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

		label_15 = new JLabel("Laudo:");
		elaboracaoPanel.add(label_15, "cell 0 4,alignx left,aligny top");

		scrollPane_12 = new JScrollPane();
		elaboracaoPanel.add(scrollPane_12, "cell 0 6 8 1,grow");

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
						"5[25px]0[5px:5px]0[120px:120px]0[5px:5px]0[24px:24px]0[5px:5px]0[24px:24px,grow]5"));

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

		label_21 = new JLabel("Laudo:");
		encerramentoPanel.add(label_21, "cell 0 4,alignx left,aligny top");

		scrollPane_14 = new JScrollPane();
		encerramentoPanel.add(scrollPane_14, "cell 0 6 8 1,grow");

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

	private void buscaButtonActionPerformed(ActionEvent evt) {
		try {
			clearLaudo();
			SQL sql = new SQL();
			carregarTabela(sql.carregarSelecaoCandidato(select()));
			sql.close();
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
	}

	private void etp2TableMouseClicked(MouseEvent evt) {
		clearLaudo();
		carregarRegistro(etp2Table.getSelectedRow());
	}

	private void etp3TableMouseClicked(MouseEvent evt) {
		clearLaudo();
		carregarRegistro(etp3Table.getSelectedRow());
	}

	private void etp4TableMouseClicked(MouseEvent evt) {
		clearLaudo();
		carregarRegistro(etp4Table.getSelectedRow());
	}

	private void salvaButtonActionPerformed(ActionEvent evt) {
		int etapa = tabbedPane.getSelectedIndex(), id, ind;
		String sql = "", laudo, cod;
		SQL insert = new SQL();
		switch (etapa) {
		case 1:
			ind = simIndEtp1RadioButton.isSelected() ? 1
					: naoIndEtp1RadioButton.isSelected() ? 0 : -1;
			laudo = laudoEtp1TextArea.getText();
			cod = codEtp1;
			if (util.isNull(eIde.get(etapa))) {
				sql = "Insert Into `"
						+ util.getNameBD()
						+ "`.`selecao` (`etapa`, `laudo`, `indicado`, `candidato`, `user`) "
						+ "Values ('" + etapa + "', '" + laudo + "', '" + ind
						+ "', '" + cod + "', '" + util.getUserSis() + "');";
				if (insert.executarSQL(sql) > 0) {
					JOptionPane.showMessageDialog(null,
							"Dados salvos com sucesso");
					etp1TableMouseClicked(null);
				}
			} else {
				id = eIde.get(etapa);
				sql = "Update `" + util.getNameBD()
						+ "`.`selecao` Set `laudo`='" + laudo
						+ "', `indicado`='" + ind + "', `user`='"
						+ util.getUserSis() + "' where (`idselecao`='" + id
						+ "');";
				if (insert.executarSQL(sql) > 0) {
					JOptionPane.showMessageDialog(null,
							"Dados salvos com sucesso");
					etp1TableMouseClicked(null);
				}
			}
			break;
		case 2:
			ind = simIndEtp2RadioButton.isSelected() ? 1
					: naoIndEtp2RadioButton.isSelected() ? 0 : -1;
			laudo = laudoEtp2TextArea.getText();
			cod = codEtp2;
			if (util.isNull(eIde.get(etapa))) {
				sql = "Insert Into `"
						+ util.getNameBD()
						+ "`.`selecao` (`etapa`, `laudo`, `indicado`, `candidato`, `user`) "
						+ "Values ('" + etapa + "', '" + laudo + "', '" + ind
						+ "', '" + cod + "', '" + util.getUserSis() + "');";
				if (insert.executarSQL(sql) > 0) {
					JOptionPane.showMessageDialog(null,
							"Dados salvos com sucesso");
					etp2TableMouseClicked(null);
				}
			} else {
				id = eIde.get(etapa);
				sql = "Update `" + util.getNameBD()
						+ "`.`selecao` Set `laudo`='" + laudo
						+ "', `indicado`='" + ind + "', `user`='"
						+ util.getUserSis() + "' where (`idselecao`='" + id
						+ "');";
				if (insert.executarSQL(sql) > 0) {
					JOptionPane.showMessageDialog(null,
							"Dados salvos com sucesso");
					etp2TableMouseClicked(null);
				}
			}
			break;
		case 3:
			ind = simIndEtp3RadioButton.isSelected() ? 1
					: naoIndEtp3RadioButton.isSelected() ? 0 : -1;
			laudo = laudoEtp3TextArea.getText();
			cod = codEtp3;
			if (util.isNull(eIde.get(etapa))) {
				sql = "Insert Into `"
						+ util.getNameBD()
						+ "`.`selecao` (`etapa`, `laudo`, `indicado`, `candidato`, `user`) "
						+ "Values ('" + etapa + "', '" + laudo + "', '" + ind
						+ "', '" + cod + "', '" + util.getUserSis() + "');";
				if (insert.executarSQL(sql) > 0) {
					JOptionPane.showMessageDialog(null,
							"Dados salvos com sucesso");
					etp3TableMouseClicked(null);
				}
			} else {
				id = eIde.get(etapa);
				sql = "Update `" + util.getNameBD()
						+ "`.`selecao` Set `laudo`='" + laudo
						+ "', `indicado`='" + ind + "', `user`='"
						+ util.getUserSis() + "' where (`idselecao`='" + id
						+ "');";
				if (insert.executarSQL(sql) > 0) {
					JOptionPane.showMessageDialog(null,
							"Dados salvos com sucesso");
					etp3TableMouseClicked(null);
				}
			}
			break;
		case 4:
			ind = simIndEtp4RadioButton.isSelected() ? 1
					: naoIndEtp4RadioButton.isSelected() ? 0 : -1;
			laudo = laudoEtp4TextArea.getText();
			cod = codEtp4;
			if (util.isNull(eIde.get(etapa))) {
				sql = "Insert Into `"
						+ util.getNameBD()
						+ "`.`selecao` (`etapa`, `laudo`, `indicado`, `candidato`, `user`) "
						+ "Values ('" + etapa + "', '" + laudo + "', '" + ind
						+ "', '" + cod + "', '" + util.getUserSis() + "');";
				if (insert.executarSQL(sql) > 0) {
					JOptionPane.showMessageDialog(null,
							"Dados salvos com sucesso");
					etp4TableMouseClicked(null);
				}
			} else {
				id = eIde.get(etapa);
				sql = "Update `" + util.getNameBD()
						+ "`.`selecao` Set `laudo`='" + laudo
						+ "', `indicado`='" + ind + "', `user`='"
						+ util.getUserSis() + "' where (`idselecao`='" + id
						+ "');";
				if (insert.executarSQL(sql) > 0) {
					JOptionPane.showMessageDialog(null,
							"Dados salvos com sucesso");
					etp4TableMouseClicked(null);
				}
			}
			break;
		}
	}

	private void imprimeButtonActionPerformed(ActionEvent evt) {
		OptionLaudo opt = new OptionLaudo();
		switch (tabbedPane.getSelectedIndex()) {
		case 1:
			opt.setNomeCand(nomeEtp1TextField.getText());
			opt.setCodCand(codEtp1);
			opt.setEtp("1");
			break;
		case 2:
			opt.setNomeCand(nomeEtp2TextField.getText());
			opt.setCodCand(codEtp2);
			opt.setEtp("2");
			break;
		case 3:
			opt.setNomeCand(nomeEtp3TextField.getText());
			opt.setCodCand(codEtp3);
			opt.setEtp("3");
			break;
		case 4:
			opt.setNomeCand(nomeEtp4TextField.getText());
			opt.setCodCand(codEtp4);
			opt.setEtp("4");
			break;
		default:
			if (candTable.getSelectedRow() > -1) {
				opt.setNomeCand((String) candTable.getModel().getValueAt(
						candTable.getSelectedRow(), 0));
				opt.setCodCand(String.valueOf(candTable.getModel().getValueAt(
						candTable.getSelectedRow(), 10)));
			}
			break;
		}
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
						sql += "(`area`='" + area[i] + "') or ";
					}
					sql += "(`area`='" + area[area.length - 1] + "'))";
				} else {
					sql += "(`area`='" + area[0] + "'))";
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

	private void carregarRegistro(int cod) {
		DefaultTableModel tabela;
		SQL sql = new SQL();
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
				Logger.getLogger(this.getName()).log(Level.SEVERE, null, ex);
			}
			codEtp1 = String.valueOf(tabela.getValueAt(cod, 10));
			carregarLaudo(sql.carregarLaudo(codEtp1));
			etp1buttonGroup.clearSelection();
			op = util.isNull(eInd.get(1)) ? -1 : eInd.get(1);
			simIndEtp1RadioButton.setSelected(op > 0);
			naoIndEtp1RadioButton.setSelected(op == 0);
			laudoEtp1TextArea.setText(eLau.get(1));
			break;
		case 2:
			tabela = (DefaultTableModel) etp2Table.getModel();
			nomeEtp2TextField.setText((String) tabela.getValueAt(cod, 0));
			try {
				foto = (String) tabela.getValueAt(cod, 9);
				fotoEtp2Label.setIcon(new ImageIcon(ImageIO
						.read(new File(foto))));
			} catch (IOException ex) {
				Logger.getLogger(this.getName()).log(Level.SEVERE, null, ex);
			}
			codEtp2 = String.valueOf(tabela.getValueAt(cod, 10));
			carregarLaudo(sql.carregarLaudo(codEtp2));
			etp2buttonGroup.clearSelection();
			op = util.isNull(eInd.get(2)) ? -1 : eInd.get(2);
			simIndEtp2RadioButton.setSelected(op > 0);
			naoIndEtp2RadioButton.setSelected(op == 0);
			laudoEtp2TextArea.setText(eLau.get(2));
			break;
		case 3:
			tabela = (DefaultTableModel) etp3Table.getModel();
			nomeEtp3TextField.setText((String) tabela.getValueAt(cod, 0));
			try {
				foto = (String) tabela.getValueAt(cod, 9);
				fotoEtp3Label.setIcon(new ImageIcon(ImageIO
						.read(new File(foto))));
			} catch (IOException ex) {
				Logger.getLogger(this.getName()).log(Level.SEVERE, null, ex);
			}
			codEtp3 = String.valueOf(tabela.getValueAt(cod, 10));
			carregarLaudo(sql.carregarLaudo(codEtp3));
			etp3buttonGroup.clearSelection();
			op = util.isNull(eInd.get(3)) ? -1 : eInd.get(3);
			simIndEtp3RadioButton.setSelected(op > 0);
			naoIndEtp3RadioButton.setSelected(op == 0);
			laudoEtp3TextArea.setText(eLau.get(3));
			break;
		case 4:
			tabela = (DefaultTableModel) etp4Table.getModel();
			nomeEtp4TextField.setText((String) tabela.getValueAt(cod, 0));
			try {
				foto = (String) tabela.getValueAt(cod, 9);
				fotoEtp4Label.setIcon(new ImageIcon(ImageIO
						.read(new File(foto))));
			} catch (IOException ex) {
				Logger.getLogger(this.getName()).log(Level.SEVERE, null, ex);
			}
			codEtp4 = String.valueOf(tabela.getValueAt(cod, 10));
			carregarLaudo(sql.carregarLaudo(codEtp4));
			etp4buttonGroup.clearSelection();
			op = util.isNull(eInd.get(4)) ? -1 : eInd.get(4);
			simIndEtp4RadioButton.setSelected(op > 0);
			naoIndEtp4RadioButton.setSelected(op == 0);
			laudoEtp4TextArea.setText(eLau.get(4));
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
				Logger.getLogger(this.getName()).log(Level.SEVERE, null, ex);
			}
			break;
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void carregarTabela(ResultSet candidato) {
		try {
			Vector[] tableData = new Vector[] { new Vector(), new Vector(),
					new Vector(), new Vector(), new Vector() };
			Vector<String> tableHeaders = new Vector<String>();
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
				code = candidato.getInt("idcandidato");
				oneRow.add(code);
				tableData[0].add(oneRow);
				tableData[1].add(oneRow);
				carregarLaudo(sql.carregarLaudo(String.valueOf(code)));
				if (util.isNumeric(eInd.get(1)) && eInd.get(1) > 0) {
					tableData[2].add(oneRow);
				}
				if (util.isNumeric(eInd.get(2)) && eInd.get(2) > 0) {
					tableData[3].add(oneRow);
				}
				if (util.isNumeric(eInd.get(3)) && eInd.get(3) > 0) {
					tableData[4].add(oneRow);
				}
			}
			candTable
					.setModel(new DefaultTableModel(tableData[0], tableHeaders));
			setWidthTable(candTable);
			etp1Table
					.setModel(new DefaultTableModel(tableData[1], tableHeaders));
			setWidthTable(etp1Table);
			etp2Table
					.setModel(new DefaultTableModel(tableData[2], tableHeaders));
			setWidthTable(etp2Table);
			etp3Table
					.setModel(new DefaultTableModel(tableData[3], tableHeaders));
			setWidthTable(etp3Table);
			etp4Table
					.setModel(new DefaultTableModel(tableData[4], tableHeaders));
			setWidthTable(etp4Table);
			candidato.close();
		} catch (SQLException ex) {
			Logger.getLogger(this.getName()).log(Level.SEVERE, null, ex);
		}
	}

	private void carregarLaudo(ResultSet laudo) {
		eInd = new HashMap<Integer, Integer>();
		eIde = new HashMap<Integer, Integer>();
		eLau = new HashMap<Integer, String>();
		try {
			while (laudo.next()) {
				eInd.put(laudo.getInt("etapa"), laudo.getInt("indicado"));
				eIde.put(laudo.getInt("etapa"), laudo.getInt("id"));
				eLau.put(laudo.getInt("etapa"), laudo.getString("laudo"));
			}
		} catch (SQLException ex) {
			Logger.getLogger(this.getName()).log(Level.SEVERE, null, ex);
		}
	}

	private void setWidthTable(TTable table) {
		table.getColumnModel().getColumn(0).setPreferredWidth(200);
		table.getColumnModel().getColumn(1).setPreferredWidth(150);
		table.getColumnModel().getColumn(2).setPreferredWidth(150);
		table.getColumnModel().getColumn(3).setPreferredWidth(350);
		table.getColumnModel().getColumn(4).setPreferredWidth(100);
		util.ocultarColunas(table, 5, 10);
	}

	private void clearLaudo() {
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

	private HashMap<Integer, Integer> eInd;
	private HashMap<Integer, Integer> eIde;
	private HashMap<Integer, String> eLau;
	private final Util util = new Util();
	private String codEtp1 = "";
	private String codEtp2 = "";
	private String codEtp3 = "";
	private String codEtp4 = "";
	private ButtonGroup etp1buttonGroup;
	private ButtonGroup etp2buttonGroup;
	private ButtonGroup etp3buttonGroup;
	private ButtonGroup etp4buttonGroup;
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
}
