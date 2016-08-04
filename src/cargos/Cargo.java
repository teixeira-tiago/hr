/**
 * 
 */
package cargos;

import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import net.miginfocom.swing.MigLayout;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRResultSetDataSource;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import utiliter.SQL;
import utiliter.Util;
import utiliter.component.AComboBox;
import utiliter.component.TComboBoxEditor;
import utiliter.component.TComboBoxRenderer;
import utiliter.component.TDefaultTableModel;
import utiliter.component.TTable;

/**
 * @author Tiago A. Teixeira {@link tiago.a.teixeira@acm.org}
 *
 */
@SuppressWarnings("serial")
public class Cargo extends JInternalFrame {

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					Cargo window = new Cargo();
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
	public Cargo() {
		initialize();
		SQL sql = new SQL();
		tabela = "cargo";
		carregarTabela(sql.carregarTabela(tabela));
		levelTab = sql.nivelTabela(tabela);
		sql.close();
		index = total = dadosTable.getRowCount() - 1;
		carregarRegistro(index);
		editar(false);
	}

	public void setPosicao() {
		Dimension d = this.getDesktopPane().getSize();
		this.setLocation((d.width - this.getSize().width) / 2,
				(d.height - this.getSize().height) / 2);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	@SuppressWarnings("unchecked")
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
		setTitle("Cadastro de Cargos");
		setResizable(true);
		setMaximizable(true);
		setClosable(true);
		setBounds(1, 1, 550, 630);
		getContentPane()
				.setLayout(
						new MigLayout(
								"",
								"5[50px]0[]0[::30px]0[grow]0[50px]0[50px]0[120px:120px:120px]0[5px]0[30px]5",
								"5[60px:60px]0[6px:6px]0[24px]0[6px:6px]0[24px]0[6px:6px]0[24px]0[6px:6px]0[400px,grow]0[6px:6px]0[35px]5"));

		cNameToCod = new HashMap<String, Integer>();
		iNameToCod = new HashMap<String, Integer>();
		cCodToPos = new HashMap<Integer, Integer>();
		iCodToPos = new HashMap<Integer, Integer>();
		cPosToCod = new HashMap<Integer, Integer>();
		iPosToCod = new HashMap<Integer, Integer>();

		buscaPanel = new JPanel();
		buscaPanel.setLayout(null);
		buscaPanel.setPreferredSize(new Dimension(450, 75));
		getContentPane().add(buscaPanel, "cell 0 0 7 1,alignx left,growy");

		codLabel = new JLabel("Código:");
		codLabel.setBounds(5, 5, 57, 16);
		buscaPanel.add(codLabel);

		codTextField = new JTextField();
		codTextField.setEnabled(false);
		codTextField.setColumns(10);
		codTextField.setBounds(5, 26, 75, 24);
		codTextField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent evt) {
				codTextFieldKeyReleased(evt);
			}
		});
		buscaPanel.add(codTextField);

		findButton = new JButton("");
		findButton.setBounds(85, 22, 30, 30);
		findButton.setIcon(new ImageIcon(getClass().getResource(
				"/img/procurar.png")));
		findButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent evt) {
				findButtonActionPerformed(evt);
			}
		});
		buscaPanel.add(findButton);

		firstButton = new JButton("");
		firstButton.setBounds(120, 22, 30, 30);
		firstButton.setIcon(new ImageIcon(getClass().getResource(
				"/img/first.png")));
		firstButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent evt) {
				firstButtonActionPerformed(evt);
			}
		});
		buscaPanel.add(firstButton);

		beforeButton = new JButton("");
		beforeButton.setBounds(155, 22, 30, 30);
		beforeButton.setIcon(new ImageIcon(getClass().getResource(
				"/img/before.png")));
		beforeButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent evt) {
				beforeButtonActionPerformed(evt);
			}
		});
		buscaPanel.add(beforeButton);

		nextButton = new JButton("");
		nextButton.setBounds(190, 22, 30, 30);
		nextButton.setIcon(new ImageIcon(getClass()
				.getResource("/img/next.png")));
		nextButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent evt) {
				nextButtonActionPerformed(evt);
			}
		});
		buscaPanel.add(nextButton);

		lastButton = new JButton("");
		lastButton.setBounds(225, 22, 30, 30);
		lastButton.setIcon(new ImageIcon(getClass()
				.getResource("/img/last.png")));
		lastButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent evt) {
				lastButtonActionPerformed(evt);
			}
		});
		buscaPanel.add(lastButton);

		printButton = new JButton("");
		printButton.setBounds(260, 22, 30, 30);
		printButton.setIcon(new ImageIcon(getClass().getResource(
				"/img/print.png")));
		printButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent evt) {
				printButtonActionPerformed(evt);
			}
		});
		buscaPanel.add(printButton);

		subTextField = new JTextField();
		getContentPane().add(subTextField, "cell 8 2,grow");
		subTextField.setColumns(10);

		lblSalrioInicial = new JLabel("Salário Inicial:");
		getContentPane().add(lblSalrioInicial,
				"cell 0 6 2 1,alignx right,aligny center");

		salIniTextField = new JTextField();
		getContentPane().add(salIniTextField, "cell 2 6 2 1,growx");
		salIniTextField.setColumns(10);

		lblSalrioFinal = new JLabel("Salário Final:");
		getContentPane().add(lblSalrioFinal, "cell 4 6 2 1,alignx right");

		salFimTextField = new JTextField();
		getContentPane().add(salFimTextField, "cell 6 6 3 1,growx");
		salFimTextField.setColumns(10);

		actionPanel = new JPanel();
		getContentPane().add(actionPanel, "cell 0 10 9 1,grow");
		actionPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 0));

		newButton = new JButton("");
		newButton.setPreferredSize(new Dimension(30, 30));
		newButton
				.setIcon(new ImageIcon(getClass().getResource("/img/new.png")));
		newButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent evt) {
				newButtonActionPerformed(evt);
			}
		});
		actionPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 0));
		actionPanel.add(newButton);

		editButton = new JButton("");
		editButton.setPreferredSize(new Dimension(30, 30));
		editButton.setIcon(new ImageIcon(getClass()
				.getResource("/img/edit.png")));
		editButton.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				editButtonActionPerformed(evt);
			}
		});
		actionPanel.add(editButton);

		saveButton = new JButton("");
		saveButton.setPreferredSize(new Dimension(30, 30));
		saveButton.setIcon(new ImageIcon(getClass()
				.getResource("/img/save.png")));
		saveButton.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				saveButtonActionPerformed(evt);
			}
		});
		actionPanel.add(saveButton);

		deleteButton = new JButton("");
		deleteButton.setPreferredSize(new Dimension(30, 30));
		deleteButton.setIcon(new ImageIcon(getClass().getResource(
				"/img/delete.png")));
		deleteButton.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				deleteButtonActionPerformed(evt);
			}
		});
		actionPanel.add(deleteButton);

		lblCargo = new JLabel("Cargo:");
		lblCargo.setHorizontalAlignment(SwingConstants.RIGHT);
		getContentPane().add(lblCargo, "cell 0 2,alignx right,aligny center");

		lblSubordinadosImediatosN = new JLabel("Subordinados imediatos nº");
		lblSubordinadosImediatosN.setHorizontalAlignment(SwingConstants.RIGHT);
		getContentPane().add(lblSubordinadosImediatosN,
				"cell 5 2 2 1,growx,aligny center");

		lblSuperiorImediato = new JLabel("Superior imediato:");
		lblSuperiorImediato.setHorizontalAlignment(SwingConstants.RIGHT);
		getContentPane().add(lblSuperiorImediato, "cell 0 4 3 1,aligny center");

		lblCbo = new JLabel("C.B.O.:");
		getContentPane().add(lblCbo, "cell 4 4 2 1,alignx right,aligny center");

		cargoTextField = new JTextField();
		getContentPane().add(cargoTextField, "cell 1 2 4 1,grow");
		cargoTextField.setColumns(10);

		supTextField = new JTextField();
		getContentPane().add(supTextField, "cell 3 4 2 1,grow");
		supTextField.setColumns(10);

		cboComboBox = new AComboBox();
		cboComboBox.setModel(util.initComboBox("cbo", new String[] { "valor" },
				"idcbo"));
		cboComboBox.setSelectedIndex(-1);
		cboCombo = util.initCombo("cbo", "idcbo");
		getContentPane().add(cboComboBox, "cell 6 4,grow");

		cboButton = new JButton("");
		cboButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				cboButtonActionPerformed(arg0);
			}
		});
		cboButton
				.setIcon(new ImageIcon(getClass().getResource("/img/cbo.png")));
		getContentPane().add(cboButton, "cell 8 4,grow");

		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		getContentPane().add(tabbedPane, "cell 0 8 9 1,grow");

		dadosTable = new TTable();
		dadosTable.addMouseListener(new java.awt.event.MouseAdapter() {
			@Override
			public void mouseClicked(java.awt.event.MouseEvent evt) {
				dadosTableMouseClicked(evt);
			}
		});
		scrollPane_4 = new JScrollPane();
		tabbedPane.addTab("Cargos", null, scrollPane_4, null);
		scrollPane_4.setViewportView(dadosTable);

		splitPane = new JSplitPane();
		splitPane.setDividerLocation(100);
		splitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);

		scrollPane = new JScrollPane();
		splitPane.setLeftComponent(scrollPane);

		desSumTextArea = new JTextArea();
		scrollPane.setViewportView(desSumTextArea);

		lblDescri = new JLabel("Descrição sumária:");
		scrollPane.setColumnHeaderView(lblDescri);

		scrollPane_1 = new JScrollPane();
		splitPane.setRightComponent(scrollPane_1);
		tabbedPane.addTab("Descrições", null, splitPane, null);

		desComTextArea = new JTextArea();
		scrollPane_1.setViewportView(desComTextArea);

		lblDescrioDetalhada = new JLabel("Descrição detalhada:");
		scrollPane_1.setColumnHeaderView(lblDescrioDetalhada);

		splitPane_1 = new JSplitPane();
		splitPane_1.setDividerLocation(150);
		splitPane_1.setOrientation(JSplitPane.VERTICAL_SPLIT);
		// tabbedPane.addTab("Formação e Competências", null, splitPane_1,
		// null);
		tabbedPane.addTab("Formação", null, splitPane_1, null);

		scrollPane_2 = new JScrollPane();
		splitPane_1.setLeftComponent(scrollPane_2);

		lblFormao = new JLabel(
				"Formação e Experiência Profissional (necessária / desejável):");
		scrollPane_2.setColumnHeaderView(lblFormao);

		forTextArea = new JTextArea();
		scrollPane_2.setViewportView(forTextArea);

		scrollPane_3 = new JScrollPane();
		splitPane_1.setRightComponent(scrollPane_3);

		panel = new JPanel();
		scrollPane_3.setViewportView(panel);
		panel.setLayout(new MigLayout("", "5[24px]5[::445px,grow]5",
				"5[24px]5[24px]5[24px]5[::55px,grow]5"));

		panel_1 = new JPanel();
		panel.add(panel_1, "cell 0 0,grow");
		panel_1.setLayout(null);

		addCompButton = new JButton("");
		addCompButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				addCompButtonActionPerformed(e);
			}
		});
		addCompButton.setBounds(2, 2, 20, 20);
		addCompButton.setIcon(new ImageIcon(getClass().getResource(
				"/img/add.png")));
		panel_1.add(addCompButton);

		JPanel panel_2 = new JPanel();
		panel_2.setLayout(null);
		panel.add(panel_2, "cell 0 1,grow");

		subCompButton = new JButton("");
		subCompButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				subCompButtoAnctionPerformed(e);
			}
		});
		subCompButton.setBounds(2, 2, 20, 20);
		subCompButton.setIcon(new ImageIcon(getClass().getResource(
				"/img/sub.png")));
		panel_2.add(subCompButton);

		JPanel panel_3 = new JPanel();
		panel_3.setLayout(null);
		panel.add(panel_3, "cell 0 2,grow");

		newCompButton = new JButton("...");
		newCompButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				newCompButtonActionPerformed(e);
			}
		});
		newCompButton.setBounds(2, 2, 20, 20);
		panel_3.add(newCompButton);

		scrollPane_7 = new JScrollPane();
		panel.add(scrollPane_7, "cell 1 0 1 4,grow");

		TDefaultTableModel modeloComp = new TDefaultTableModel();
		compTable = new JTable(modeloComp);
		// compTable = new JTable();
		limparCompTable();

		scrollPane_7.setViewportView(compTable);

		splitPane_2 = new JSplitPane();
		splitPane_2.setOrientation(JSplitPane.VERTICAL_SPLIT);
		// tabbedPane.addTab("Indicadores e Observações", null, splitPane_2,
		// null);
		tabbedPane.addTab("Indicadores", null, splitPane_2, null);

		scrollPane_5 = new JScrollPane();
		splitPane_2.setLeftComponent(scrollPane_5);

		panel_4 = new JPanel();
		scrollPane_5.setViewportView(panel_4);
		panel_4.setLayout(new MigLayout("", "5[24px]5[::435px,grow]5",
				"5[24px]5[24px]5[24px]5[::50px,grow]5"));

		panel_5 = new JPanel();
		panel_5.setLayout(null);
		panel_4.add(panel_5, "cell 0 0,grow");

		addIndButton = new JButton("");
		addIndButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				addIndButtonActionPerformed(e);
			}
		});
		addIndButton.setBounds(2, 2, 20, 20);
		addIndButton.setIcon(new ImageIcon(getClass().getResource(
				"/img/add.png")));
		panel_5.add(addIndButton);

		JPanel panel_6 = new JPanel();
		panel_6.setLayout(null);
		panel_4.add(panel_6, "cell 0 1,grow");

		subIndButton = new JButton("");
		subIndButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				subIndButtoAnctionPerformed(e);
			}
		});
		subIndButton.setBounds(2, 2, 20, 20);
		subIndButton.setIcon(new ImageIcon(getClass().getResource(
				"/img/sub.png")));
		panel_6.add(subIndButton);

		JPanel panel_7 = new JPanel();
		panel_7.setLayout(null);
		panel_4.add(panel_7, "cell 0 2,grow");

		newIndButton = new JButton("...");
		newIndButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				newIndButtonActionPerformed(e);
			}
		});
		newIndButton.setBounds(2, 2, 20, 20);
		panel_7.add(newIndButton);

		scrollPane_8 = new JScrollPane();
		panel_4.add(scrollPane_8, "cell 1 0 1 4,grow");

		TDefaultTableModel modeloInd = new TDefaultTableModel();
		indTable = new JTable(modeloInd);
		scrollPane_8.setViewportView(indTable);
		limparIndTable();

		scrollPane_6 = new JScrollPane();
		splitPane_2.setRightComponent(scrollPane_6);

		lblObservaes = new JLabel("Observações:");
		scrollPane_6.setColumnHeaderView(lblObservaes);

		obsTextArea = new JTextArea();
		scrollPane_6.setViewportView(obsTextArea);
		splitPane_2.setDividerLocation(150);
	}

	private void dadosTableMouseClicked(MouseEvent evt) {
		index = dadosTable.getSelectedRow();
		carregarRegistro(index);
	}

	private void newButtonActionPerformed(ActionEvent evt) {
		editar(true);
		limpar();
	}

	private void editButtonActionPerformed(ActionEvent evt) {
		editar(true);
	}

	private void saveButtonActionPerformed(ActionEvent evt) {
		String sql = "", id, aux;
		SQL b = new SQL();
		ArrayList<Object> dt = dados();
		index = dadosTable.getSelectedRow();
		TDefaultTableModel tabelaCom = (TDefaultTableModel) compTable
				.getModel();
		TDefaultTableModel tabelaInd = (TDefaultTableModel) indTable.getModel();
		int value, ok = 0;
		if (!codTextField.getText().trim().isEmpty()) {
			sql = "Update `" + util.getNameBD() + "`.`" + tabela + "` Set `"
					+ tableCols.get(1) + "`='" + dt.get(0) + "', `"
					+ tableCols.get(2) + "`='" + dt.get(1) + "', `"
					+ tableCols.get(3) + "`='" + dt.get(2) + "', `"
					+ tableCols.get(4) + "`='" + dt.get(3) + "', `"
					+ tableCols.get(5) + "`='" + dt.get(4) + "', `"
					+ tableCols.get(6) + "`='" + dt.get(5) + "', `"
					+ tableCols.get(7) + "`='" + dt.get(6) + "', `"
					+ tableCols.get(8) + "`='" + dt.get(7) + "', `"
					+ tableCols.get(9) + "`='" + dt.get(8) + "', `"
					+ tableCols.get(10) + "`='" + dt.get(9) + "', `user`='"
					+ util.getUserSis() + "' Where `idcargo`='"
					+ codTextField.getText() + "';";
			if (b.executarSQL(sql) > 0) {
				for (int i = 0; i < tabelaCom.getRowCount(); ++i) {
					aux = String.valueOf(tabelaCom.getValueAt(i, 0));
					value = cNameToCod.containsKey(aux) ? codComp(1, aux)
							: !aux.trim().isEmpty() ? codComp(3, aux) : -1;
					id = String.valueOf(tabelaCom.getValueAt(i, 1));
					if (!id.trim().isEmpty() && util.isNumeric(id)) {
						ok += upDateCom(value, Integer.parseInt(id));
					} else {
						ok += addCom(value,
								Integer.parseInt(codTextField.getText()));
					}
				}
				for (int i = 0; i < tabelaInd.getRowCount(); ++i) {
					aux = String.valueOf(tabelaInd.getValueAt(i, 0));
					value = iNameToCod.containsKey(aux) ? codInd(1, aux) : !aux
							.trim().isEmpty() ? codInd(3, aux) : -1;
					id = String.valueOf(tabelaInd.getValueAt(i, 1));
					if (!id.trim().isEmpty() && util.isNumeric(id)) {
						ok += upDateInd(value, Integer.parseInt(id));
					} else {
						ok += addInd(value,
								Integer.parseInt(codTextField.getText()));
					}
				}
				if (ok > 0) {
					JOptionPane.showMessageDialog(null,
							"Dados atualizados com sucesso");
				}
			}
		} else {
			if (!cargoTextField.getText().trim().isEmpty()) {
				sql = "Insert Into `" + util.getNameBD() + "`.`" + tabela
						+ "` (`" + tableCols.get(1) + "`, `" + tableCols.get(2)
						+ "`, `" + tableCols.get(3) + "`, `" + tableCols.get(4)
						+ "`, `" + tableCols.get(5) + "`, `" + tableCols.get(6)
						+ "`, `" + tableCols.get(7) + "`, `" + tableCols.get(8)
						+ "`, `" + tableCols.get(9) + "`, `"
						+ tableCols.get(10) + "`, `user`) VALUES ('"
						+ dt.get(0) + "', '" + dt.get(1) + "', '" + dt.get(2)
						+ "', '" + dt.get(3) + "', '" + dt.get(4) + "', '"
						+ dt.get(5) + "', '" + dt.get(6) + "', '" + dt.get(7)
						+ "', '" + dt.get(8) + "', '" + dt.get(9) + "', '"
						+ util.getUserSis() + "');";
				if (b.executarSQL(sql) > 0) {
					ResultSet rs = new SQL()
							.query("Select idcargo id from cargo order by idcargo desc limit 1;");
					try {
						rs.next();
						for (int i = 0; i < tabelaCom.getRowCount(); ++i) {
							aux = String.valueOf(tabelaCom.getValueAt(i, 0));
							value = cNameToCod.containsKey(aux) ? codComp(1,
									aux) : !aux.trim().isEmpty() ? codComp(3,
									aux) : -1;
							ok += addCom(value, rs.getInt("id"));
						}
						for (int i = 0; i < tabelaInd.getRowCount(); ++i) {
							aux = String.valueOf(tabelaInd.getValueAt(i, 0));
							value = iNameToCod.containsKey(aux) ? codInd(1, aux)
									: !aux.trim().isEmpty() ? codInd(3, aux)
											: -1;
							ok += addInd(value, rs.getInt("id"));
						}
						if (ok > 0) {
							JOptionPane.showMessageDialog(null,
									"Dados inseridos com sucesso");
						}
					} catch (SQLException e1) {
						e1.printStackTrace();
					}
					JOptionPane.showMessageDialog(null,
							"Dados inseridos com sucesso");
				}
			}
		}
		carregarTabela(b.carregarTabela(tabela));
		if (index >= 0) {
			carregarRegistro(index);
		} else {
			total = dadosTable.getRowCount() - 1;
			carregarRegistro(total);
		}
		editar(false);
		b.close();

	}

	private int upDateCom(int value, int id) {
		String sql = "Update `" + util.getNameBD()
				+ "`.`cargo_com` Set `competencia`='" + value + "', `user`='"
				+ util.getUserSis() + "' where (`idccom`='" + id + "');";
		return new SQL().executarSQL(sql);
	}

	private int addCom(int value, int id) {
		String sql = "Insert Into `" + util.getNameBD()
				+ "`.`cargo_com` (`competencia`, `cargo`, `user`) values ('"
				+ value + "', '" + id + "', '" + util.getUserSis() + "');";
		return new SQL().executarSQL(sql);
	}

	private int upDateInd(int value, int id) {
		String sql = "Update `" + util.getNameBD()
				+ "`.`cargo_ind` Set `indicador`='" + value + "', `user`='"
				+ util.getUserSis() + "' where (`idcind`='" + id + "');";
		return new SQL().executarSQL(sql);
	}

	private int addInd(int value, int id) {
		String sql = "Insert Into `" + util.getNameBD()
				+ "`.`cargo_ind` (`indicador`, `cargo`, `user`) values ('"
				+ value + "', '" + id + "', '" + util.getUserSis() + "');";
		return new SQL().executarSQL(sql);
	}

	private void deleteButtonActionPerformed(ActionEvent evt) {
		boolean valido = false;
		Object[] opcoes = { "Não", "Sim", "Cancelar" };
		int contU = 0, contP = 0;
		SQL b = new SQL();
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
						rs = b.checarUsuario(user, pws);
						if (rs.next()) {
							if (rs.getInt("nivel") <= levelTab) {
								String sql = "Update `" + util.getNameBD()
										+ "`.`" + tabela + "` Set ";
								sql += "`active`='0', `user`='"
										+ rs.getInt("idusuario") + "' ";
								sql += "Where `" + tableCols.get(0) + "`='"
										+ codTextField.getText() + "';";
								b.executarSQL(sql);
								carregarTabela(b.carregarTabela(tabela));
								if (index < 1) {
									total = dadosTable.getRowCount() - 1;
									carregarRegistro(total);
								} else {
									--index;
									carregarRegistro(index);
								}
								editar(false);
								b.close();
								valido = true;
								JOptionPane.showMessageDialog(null,
										"Registro apagado com sucesso.");
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
					} catch (SQLException ex) {
						Logger.getLogger(this.getName()).log(Level.SEVERE,
								null, ex);
					}
				}
			}
		}
	}

	private void findButtonActionPerformed(ActionEvent evt) {
		editar(true);
		limpar();
		codTextField.setEnabled(true);
	}

	private void codTextFieldKeyReleased(KeyEvent evt) {
		if (codTextField.isEnabled()) {
			SQL b = new SQL();
			String col = "";
			for (String coluna : tableCols) {
				col += "`" + coluna + "`, ";
			}
			col = col.substring(0, col.lastIndexOf(", "));
			carregarTabela(b.query("Select " + col + " from `"
					+ util.getNameBD() + "`.`" + tabela + "` where `"
					+ tableCols.get(0) + "` like \"" + codTextField.getText()
					+ "%\""));
			b.close();
			index = 0;
			total = dadosTable.getRowCount() - 1;
		}
	}

	private void firstButtonActionPerformed(ActionEvent evt) {
		limpar();
		index = 0;
		carregarRegistro(index);
		editar(false);
	}

	private void lastButtonActionPerformed(ActionEvent evt) {
		limpar();
		total = dadosTable.getRowCount() - 1;
		index = total;
		carregarRegistro(index);
		editar(false);
	}

	private void beforeButtonActionPerformed(ActionEvent evt) {
		if ((total > 0) && (index > 0)) {
			limpar();
			carregarRegistro(--index);
		} else {
			firstButtonActionPerformed(evt);
		}
		editar(false);
	}

	private void nextButtonActionPerformed(ActionEvent evt) {
		if ((total > 0) && (index < total)) {
			limpar();
			carregarRegistro(++index);
		} else {
			lastButtonActionPerformed(evt);
		}
		editar(false);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void printButtonActionPerformed(ActionEvent evt) {
		try {
			// gerando o jasper design
			JasperDesign desenho = JRXmlLoader
					.load("relatorios/cargo/Cargo.jrxml");
			// compila o relatório
			JasperReport relatorio = JasperCompileManager
					.compileReport(desenho);
			// estabelece conexão
			SQL b = new SQL();
			String sql = "";
			if (dadosTable.getSelectedRow() > -1) {
				sql = "and (`idcargo`='" + codTextField.getText() + "')";
			}
			ResultSet rs = b.carregarRelatorioCargo(sql);
			// implementação da interface JRDataSource para DataSource ResultSet
			JRResultSetDataSource jrRS = new JRResultSetDataSource(rs);
			// executa o relatório
			Map parametros = new HashMap();
			parametros.put("logo", util.getConf("logo"));
			parametros.put("subCompetencia",
					"relatorios/cargo/Competencias.jasper");
			parametros.put("consCompetencia", new JRResultSetDataSource(
					new SQL().carregarRelatorioCompetencia(sql)));
			parametros.put("subIndicador",
					"relatorios/cargo/Indicadores.jasper");
			parametros.put(
					"consIndicador",
					new JRResultSetDataSource(new SQL()
							.carregarRelatorioIndicador(sql)));
			parametros.put("titulo", this.getTitle());
			JasperPrint impressao = JasperFillManager.fillReport(relatorio,
					parametros, jrRS);
			// exibe o resultado
			JasperExportManager.exportReportToPdfFile(impressao,
					"relatorios/Relatorio.pdf");
			File file = new File("relatorios/Relatorio.pdf");
			Desktop.getDesktop().open(file);
			editar(false);
			b.close();
		} catch (JRException | IOException ex) {
			Logger.getLogger(this.getName()).log(Level.SEVERE, null, ex);
		}
	}

	private void addCompButtonActionPerformed(ActionEvent evt) {
		TDefaultTableModel model = (TDefaultTableModel) compTable.getModel();
		model.addRow(new Object[] { "", "" });
		compTable.setModel(model);
		int idx = compTable.getRowCount() - 1;
		addCompCombo(0);
		compTable.setRowSelectionInterval(idx, idx);
	}

	private void subCompButtoAnctionPerformed(ActionEvent evt) {
		int idx = compTable.getSelectedRow();
		Object[] opcoes = { "Não", "Sim", "Cancelar" };
		SQL b = new SQL();
		int confirmar = JOptionPane.showOptionDialog(null,
				"Você realmente deseja apagar esse registro?", null, 1, 1,
				null, opcoes, 0);
		if (confirmar == 1) {
			String aux = String.valueOf(compTable.getValueAt(idx, 0));
			int cod = util.isNumeric(aux) ? codComp(3, aux) : codComp(1, aux);
			String sql = "Update `" + util.getNameBD() + "`.`cargo_com` Set ";
			sql += "`active`='0', `cargo`='0', `user`='" + util.getUserSis()
					+ "' ";
			sql += "Where (`competencia`='" + cod + "') and (`cargo`='"
					+ codTextField.getText() + "');";
			b.executarSQL(sql);
			carregarCompetencia(b.carregarTabela("cargo_com", "(`cargo`='"
					+ codTextField.getText() + "')"));
			b.close();
			JOptionPane
					.showMessageDialog(null, "Registro apagado com sucesso.");
		}
		editar(false);
	}

	private void newCompButtonActionPerformed(ActionEvent evt) {
		util.loadTelaCadastro("competencia", new JFrame(),
				"Cadastro de Competencias");
		SQL sql = new SQL();
		carregarCompetencia(sql.carregarTabela("cargo_com", "(`cargo`='"
				+ codTextField.getText() + "')"));
		sql.close();
	}

	private void addIndButtonActionPerformed(ActionEvent evt) {
		TDefaultTableModel model = (TDefaultTableModel) indTable.getModel();
		model.addRow(new Object[] { "" });
		indTable.setModel(model);
		int idx = indTable.getRowCount() - 1;
		addIndCombo(0);
		indTable.setRowSelectionInterval(idx, idx);
	}

	private void subIndButtoAnctionPerformed(ActionEvent evt) {
		int idx = indTable.getSelectedRow();
		Object[] opcoes = { "Não", "Sim", "Cancelar" };
		SQL b = new SQL();
		int confirmar = JOptionPane.showOptionDialog(null,
				"Você realmente deseja apagar esse registro?", null, 1, 1,
				null, opcoes, 0);
		if (confirmar == 1) {
			String aux = String.valueOf(indTable.getValueAt(idx, 0));
			int cod = util.isNumeric(aux) ? codComp(3, aux) : codComp(1, aux);
			String sql = "Update `" + util.getNameBD() + "`.`cargo_ind` Set ";
			sql += "`active`='0', `cargo`='0', `user`='" + util.getUserSis()
					+ "' ";
			sql += "Where (`indicador`='" + cod + "') and (`cargo`='"
					+ codTextField.getText() + "');";
			b.executarSQL(sql);
			carregarIndicador(b.carregarTabela("cargo_ind", "(`cargo`='"
					+ codTextField.getText() + "')"));
			b.close();
			JOptionPane
					.showMessageDialog(null, "Registro apagado com sucesso.");
		}
		editar(false);
	}

	private void newIndButtonActionPerformed(ActionEvent evt) {
		util.loadTelaCadastro("indicador", new JFrame(),
				"Cadastro de Indicadores");
		SQL sql = new SQL();
		carregarIndicador(sql.carregarTabela("cargo_ind", "(`cargo`='"
				+ codTextField.getText() + "')"));
		sql.close();
	}

	private void editar(boolean b) {
		cargoTextField.setEditable(b);
		supTextField.setEditable(b);
		subTextField.setEditable(b);
		salIniTextField.setEditable(b);
		salFimTextField.setEditable(b);
		cboComboBox.setEnabled(b);
		desComTextArea.setEditable(b);
		desSumTextArea.setEditable(b);
		forTextArea.setEditable(b);
		obsTextArea.setEditable(b);
		compTable.setEnabled(b);
		indTable.setEnabled(b);
		addCompButton.setEnabled(b);
		subCompButton.setEnabled(b);
		newCompButton.setEnabled(b);
		addIndButton.setEnabled(b);
		subIndButton.setEnabled(b);
		newIndButton.setEnabled(b);
	}

	private void limpar() {
		codTextField.setText("");
		cargoTextField.setText("");
		supTextField.setText("");
		subTextField.setText("");
		salIniTextField.setText("");
		salFimTextField.setText("");
		cboComboBox.setSelectedIndex(-1);
		desComTextArea.setText("");
		desSumTextArea.setText("");
		forTextArea.setText("");
		obsTextArea.setText("");
		limparCompTable();
		limparIndTable();
	}

	private void limparCompTable() {
		tableHeaders = new Vector<String>();
		tableHeaders.add("Competencias:");
		tableHeaders.add("Codigo");
		compTable.setModel(new TDefaultTableModel(null, tableHeaders));
		compTable.getColumnModel().getColumn(0).setPreferredWidth(300);
		util.ocultarColunas(compTable, 1, 1);
	}

	private void limparIndTable() {
		tableHeaders = new Vector<String>();
		tableHeaders.add("Indicadores:");
		tableHeaders.add("Codigo");
		indTable.setModel(new TDefaultTableModel(null, tableHeaders));
		indTable.getColumnModel().getColumn(0).setPreferredWidth(300);
		util.ocultarColunas(indTable, 1, 1);
	}

	private void carregarRegistro(int cod) {
		if (cod >= 0) {
			DefaultTableModel tabela = (DefaultTableModel) dadosTable
					.getModel();
			codTextField.setText(String.valueOf(tabela.getValueAt(cod, 0)));
			cargoTextField.setText((String) tabela.getValueAt(cod, 1));
			supTextField.setText((String) tabela.getValueAt(cod, 2));
			subTextField.setText(String.valueOf(tabela.getValueAt(cod, 3)));
			salIniTextField.setText(ptov(String.valueOf(tabela.getValueAt(cod,
					4))));
			salFimTextField.setText(ptov(String.valueOf(tabela.getValueAt(cod,
					5))));
			cboComboBox.setSelectedIndex((int) tabela.getValueAt(cod, 6) - 1);
			desSumTextArea.setText((String) tabela.getValueAt(cod, 7));
			desComTextArea.setText((String) tabela.getValueAt(cod, 8));
			forTextArea.setText((String) tabela.getValueAt(cod, 9));
			obsTextArea.setText((String) tabela.getValueAt(cod, 10));
			SQL sql = new SQL();
			carregarCompetencia(sql.carregarTabela("cargo_com", "(`cargo`='"
					+ codTextField.getText() + "')"));
			carregarIndicador(sql.carregarTabela("cargo_ind", "(`cargo`='"
					+ codTextField.getText() + "')"));
			sql.close();
		}
	}

	private String ptov(String text) {
		String format = text.contains(".") ? text.replace(".", ",") : text;
		return format;
	}

	private String vtop(String text) {
		String format = text.contains(",") ? text.replace(",", ".") : text;
		return format;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void carregarTabela(ResultSet rs) {
		try {
			tableHeaders = new Vector<String>();
			tableCols = new Vector<String>();
			tableData = new Vector();
			ResultSetMetaData meta = rs.getMetaData();
			String col = "", tmp;
			for (int i = 1; i < meta.getColumnCount() + 1; ++i) {
				col = meta.getColumnLabel(i);
				tableCols.add(col);
				tmp = col.substring(0, 1).toUpperCase() + col.substring(1);
				tmp = tmp.contains("Id") ? "Código" : tmp;
				tableHeaders.add(tmp);
			}
			while (rs.next()) {
				Vector<Object> oneRow = new Vector<Object>();
				for (Object coluna : tableCols) {
					oneRow.add(rs.getObject((String) coluna));
				}
				tableData.add(oneRow);
			}
			rs.close();
			dadosTable.setModel(new DefaultTableModel(tableData, tableHeaders));
			dadosTable.getColumnModel().getColumn(0).setPreferredWidth(50);
			dadosTable.getColumnModel().getColumn(1).setPreferredWidth(200);
			dadosTable.getColumnModel().getColumn(2).setPreferredWidth(150);
			dadosTable.getColumnModel().getColumn(3).setPreferredWidth(30);
			if (dadosTable.getColumnCount() > 4) {
				util.ocultarColunas(dadosTable, 4,
						dadosTable.getColumnCount() - 1);
			}
		} catch (SQLException ex) {
			Logger.getLogger(this.getName()).log(Level.SEVERE, null, ex);
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void carregarCompetencia(ResultSet rs) {
		try {
			tableHeaders = new Vector<String>();
			tableData = new Vector();
			tableHeaders.add("Competencias:");
			tableHeaders.add("Codigo");
			while (rs.next()) {
				Vector<Object> oneRow = new Vector<Object>();
				oneRow.add(codComp(2, String.valueOf(rs.getInt("competencia"))));
				oneRow.add(rs.getInt("idccom"));
				tableData.add(oneRow);
			}
			rs.close();
			compTable.setModel(new TDefaultTableModel(tableData, tableHeaders));
			addCompCombo(0);
			compTable.getColumnModel().getColumn(0).setPreferredWidth(300);
			util.ocultarColunas(compTable, 1, 1);
		} catch (SQLException ex) {
			Logger.getLogger(this.getName()).log(Level.SEVERE, null, ex);
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void carregarIndicador(ResultSet rs) {
		try {
			tableHeaders = new Vector<String>();
			tableData = new Vector();
			tableHeaders.add("Indicadores:");
			tableHeaders.add("Codigo");
			while (rs.next()) {
				Vector<Object> oneRow = new Vector<Object>();
				oneRow.add(codInd(2, String.valueOf(rs.getInt("indicador"))));
				oneRow.add(rs.getInt("idcind"));
				tableData.add(oneRow);
			}
			rs.close();
			indTable.setModel(new TDefaultTableModel(tableData, tableHeaders));
			addIndCombo(0);
			indTable.getColumnModel().getColumn(0).setPreferredWidth(300);
			util.ocultarColunas(indTable, 1, 1);
		} catch (SQLException ex) {
			Logger.getLogger(this.getName()).log(Level.SEVERE, null, ex);
		}
	}

	private ArrayList<Object> dados() {
		ArrayList<Object> obj = new ArrayList<>();
		obj.add(cargoTextField.getText());
		obj.add(supTextField.getText());
		obj.add(subTextField.getText());
		obj.add(vtop(salIniTextField.getText()));
		obj.add(vtop(salFimTextField.getText()));
		obj.add(cboCombo.get(cboComboBox.getSelectedIndex()));
		obj.add(desSumTextArea.getText());
		obj.add(desComTextArea.getText());
		obj.add(forTextArea.getText());
		obj.add(obsTextArea.getText());
		return obj;
	}

	private void cboButtonActionPerformed(ActionEvent evt) {
		try {
			File file = new File("CBOConsultas.jar");
			Desktop.getDesktop().open(file);
		} catch (IOException ex) {
			Logger.getLogger(this.getName()).log(Level.SEVERE, null, ex);
		}
	}

	private void addCompCombo(int column) {
		String tmp = "";
		SQL sql = new SQL();
		int cod;
		ResultSet rs = sql.query("Select `nome`, `idcomp` from `"
				+ util.getNameBD()
				+ "`.`competencia` where `active`='1' order by `nome`");
		try {
			int cont = 1;
			while (rs.next()) {
				tmp += rs.getString("nome") + "#";
				cod = rs.getInt("idcomp");
				cNameToCod.put(rs.getString("nome"), cod);
				cCodToPos.put(cod, cont);
				cPosToCod.put(cont, cod);
				++cont;
			}
			rs.close();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		sql.close();
		tmp.substring(0, tmp.lastIndexOf("#"));
		String[] values = tmp.split("#");
		TableColumn col = compTable.getColumnModel().getColumn(column);
		col.setCellEditor(new TComboBoxEditor(values));
		col.setCellRenderer(new TComboBoxRenderer(values));
	}

	/**
	 * @param type
	 *            1:codigo relacionado ao nome, 2:posicao no ComboBox a partir
	 *            do codigo, 3:codigo a partir da posicao no ComboBox
	 * @param info
	 *            nome, posicao no ComboBox ou codigo, informar info de acordo
	 *            com o tipo escolhido
	 * @return codigo requerido de acordo com o tipo fornecido
	 */
	private int codComp(int type, String info) {
		int cod = -1;
		switch (type) {
		case 1:
			cod = cNameToCod.get(info);
			break;
		case 2:
			cod = cCodToPos.get(Integer.parseInt(info));
			break;
		case 3:
			cod = cPosToCod.get(Integer.parseInt(info));
			break;
		default:
			break;
		}
		return cod;
	}

	private void addIndCombo(int column) {
		String tmp = "";
		int cod;
		SQL sql = new SQL();
		ResultSet rs = sql.query("Select `nome`, `idind` from `"
				+ util.getNameBD()
				+ "`.`indicador` where `active`='1' order by `nome`");
		try {
			int cont = 1;
			while (rs.next()) {
				tmp += rs.getString("nome") + "#";
				cod = rs.getInt("idind");
				iNameToCod.put(rs.getString("nome"), cod);
				iCodToPos.put(rs.getInt("idind"), cont);
				iPosToCod.put(cont, cod);
				++cont;
			}
			rs.close();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		sql.close();
		tmp.substring(0, tmp.lastIndexOf("#"));
		String[] values = tmp.split("#");
		TableColumn col = indTable.getColumnModel().getColumn(column);
		col.setCellEditor(new TComboBoxEditor(values));
		col.setCellRenderer(new TComboBoxRenderer(values));
	}

	/**
	 * @param type
	 *            1:codigo relacionado ao nome, 2:posicao no ComboBox a partir
	 *            do codigo, 3:codigo a partir da posicao no ComboBox
	 * @param info
	 *            nome, posicao no ComboBox ou codigo, informar info de acordo
	 *            com o tipo escolhido
	 * @return codigo requerido de acordo com o tipo fornecido
	 */
	private int codInd(int type, String info) {
		int cod = -1;
		switch (type) {
		case 1:
			cod = iNameToCod.get(info);
			break;
		case 2:
			cod = iCodToPos.get(Integer.parseInt(info));
			break;
		case 3:
			cod = iPosToCod.get(Integer.parseInt(info));
			break;
		default:
			break;
		}
		return cod;
	}

	private HashMap<String, Integer> cNameToCod;
	private HashMap<Integer, Integer> cCodToPos;
	private HashMap<Integer, Integer> cPosToCod;
	private HashMap<String, Integer> iNameToCod;
	private HashMap<Integer, Integer> iCodToPos;
	private HashMap<Integer, Integer> iPosToCod;
	private final Util util = new Util();
	private int levelTab = Integer.MIN_VALUE;
	private int index;
	private int total;
	private String tabela = "";
	private Vector<String> tableCols;
	private Vector<String> tableHeaders = new Vector<String>();
	@SuppressWarnings("rawtypes")
	private Vector tableData = new Vector();
	private JButton findButton;
	private JButton firstButton;
	private JButton beforeButton;
	private JButton nextButton;
	private JButton lastButton;
	private JButton printButton;
	private JButton newButton;
	private JButton editButton;
	private JButton saveButton;
	private JButton deleteButton;
	private JButton cboButton;
	private JButton addCompButton;
	private JButton subCompButton;
	private JButton newCompButton;
	private JButton addIndButton;
	private JButton subIndButton;
	private JButton newIndButton;
	private JLabel codLabel;
	private JPanel buscaPanel;
	private JPanel actionPanel;
	private JTextField codTextField;
	private JTextField cargoTextField;
	private JTextField subTextField;
	private JTextField supTextField;
	private AComboBox cboComboBox;
	private HashMap<Integer, Integer> cboCombo;
	private JTable dadosTable;
	private JSplitPane splitPane;
	private JSplitPane splitPane_1;
	private JScrollPane scrollPane_2;
	private JScrollPane scrollPane_1;
	private JScrollPane scrollPane;
	private JTextArea desComTextArea;
	private JTextArea desSumTextArea;
	private JLabel lblDescri;
	private JLabel lblDescrioDetalhada;
	private JScrollPane scrollPane_4;
	private JSplitPane splitPane_2;
	private JScrollPane scrollPane_3;
	private JScrollPane scrollPane_5;
	private JScrollPane scrollPane_6;
	private JLabel lblSubordinadosImediatosN;
	private JLabel lblSuperiorImediato;
	private JLabel lblCargo;
	private JLabel lblCbo;
	private JLabel lblObservaes;
	private JLabel lblFormao;
	private JTextArea forTextArea;
	private JTextArea obsTextArea;
	private JTabbedPane tabbedPane;
	private JLabel lblSalrioInicial;
	private JTextField salIniTextField;
	private JLabel lblSalrioFinal;
	private JTextField salFimTextField;
	private JPanel panel;
	private JPanel panel_1;
	private JScrollPane scrollPane_7;
	private JTable compTable;
	private JPanel panel_4;
	private JPanel panel_5;
	private JScrollPane scrollPane_8;
	private JTable indTable;
}
