/**
 * 
 */
package pesquisa;

import java.awt.Color;
import java.awt.Component;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
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
import utiliter.component.TComboBoxEditor;
import utiliter.component.TComboBoxRenderer;

/**
 * @author Tiago A. Teixeira {@link tiago.a.teixeira@acm.org}
 * 
 */
@SuppressWarnings("serial")
public class Questionario extends JInternalFrame {

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					Questionario window = new Questionario();
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
	public Questionario() {
		initialize();
		SQL b = new SQL();
		carregarTabela(b.carregarTabela("questionario"));
		levelTab = b.nivelTabela("questionario");
		b.close();
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
		setTitle("Manutenção de Questionarios");
		setBounds(1, 1, 560, 570);
		setMaximizable(true);
		setClosable(true);
		setResizable(true);

		opt_ask = new HashMap<String, Integer>();
		dadosTable = new JTable();
		getContentPane()
				.setLayout(
						new MigLayout("", "5[66px]0[4px]0[475px,grow]5",
								"5[60px]0[6px]0[24px]0[6px]0[38px]0[6px]0[350px,grow]0[6px]0[35px]5"));
		buscaPanel = new JPanel();
		buscaPanel.setLayout(null);
		buscaPanel.setPreferredSize(new Dimension(450, 75));
		getContentPane().add(buscaPanel, "cell 0 0 3 1,alignx left,growy");

		codLabel = new JLabel("Código:");
		codLabel.setBounds(5, 5, 75, 16);
		buscaPanel.add(codLabel);

		codTextField = new JTextField();
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

		lblNome = new JLabel("Nome:");
		lblNome.setHorizontalAlignment(SwingConstants.RIGHT);
		getContentPane().add(lblNome, "cell 0 2,alignx right,aligny center");

		nomeTextField = new JTextField();
		getContentPane().add(nomeTextField, "cell 2 2,grow");
		nomeTextField.setColumns(10);

		lblDescrio = new JLabel("Descrição:");
		lblDescrio.setHorizontalAlignment(SwingConstants.RIGHT);
		getContentPane().add(lblDescrio, "cell 0 4,alignx left,aligny center");

		scrollPane = new JScrollPane();
		getContentPane().add(scrollPane, "cell 2 4,grow");

		descTextArea = new JTextArea();
		scrollPane.setViewportView(descTextArea);

		lblQuestes = new JLabel("Questões:");
		lblQuestes.setHorizontalAlignment(SwingConstants.RIGHT);
		getContentPane().add(lblQuestes, "cell 0 6,alignx right,aligny top");

		scrollPane_1 = new JScrollPane();
		getContentPane().add(scrollPane_1, "cell 2 6,grow");

		TDefaultTableModel modelo = new TDefaultTableModel();

		askTable = new JTable(modelo) {

			@Override
			public Component prepareRenderer(TableCellRenderer renderer,
					int row, int column) {
				Component c = super.prepareRenderer(renderer, row, column);
				if (Boolean.parseBoolean(String.valueOf(getValueAt(row, 1)))) {
					c.setBackground(Color.white);
				} else {
					c.setBackground(Color.lightGray);
				}
				if (isCellSelected(row, column)) {
					c.setBackground(Color.blue);
				}
				if ((column == 2 || column == 3)
						&& (Boolean.parseBoolean(String.valueOf(getValueAt(row,
								1))))) {
					return c;
				} else if (column == 0 || column == 1) {
					return c;
				}
				return Box.createRigidArea(c.getPreferredSize());
			}

			@Override
			public Component prepareEditor(TableCellEditor editor, int row,
					int column) {
				Component c = super.prepareEditor(editor, row, column);
				if ((column == 2 || column == 3)
						&& (Boolean.parseBoolean(String.valueOf(getValueAt(row,
								1))))) {
					return c;
				} else if (column == 0 || column == 1) {
					return c;
				}
				return Box.createRigidArea(c.getPreferredSize());
			}
		};
		scrollPane_1.setViewportView(askTable);
		limparAsk();

		panel = new JPanel();
		FlowLayout flowLayout = (FlowLayout) panel.getLayout();
		flowLayout.setVgap(0);
		getContentPane().add(panel, "cell 0 8 3 1,grow");

		addButton = new JButton("");
		addButton.setPreferredSize(new Dimension(30, 30));
		addButton.setIcon(new ImageIcon(Questionario.class
				.getResource("/img/add.png")));
		addButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				addButtonActionPerformed(e);
			}
		});
		panel.add(addButton);

		subButton = new JButton("");
		subButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				subButtonActionPerformed(e);
			}
		});
		subButton.setPreferredSize(new Dimension(30, 30));
		subButton
				.setIcon(new ImageIcon(getClass().getResource("/img/sub.png")));
		panel.add(subButton);

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
		panel.add(newButton);

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
		panel.add(editButton);

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
		panel.add(saveButton);

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
		panel.add(deleteButton);
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
					+ util.getNameBD() + "`.`questionario` where `"
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
		if (index > 0) {
			limpar();
			carregarRegistro(--index);
		} else {
			firstButtonActionPerformed(evt);
		}
		editar(false);
	}

	private void nextButtonActionPerformed(ActionEvent evt) {
		if (index < total) {
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
					.load("relatorios/pesquisa/Questionario.jrxml");
			// compila o relatório
			JasperReport relatorio = JasperCompileManager
					.compileReport(desenho);
			// estabelece conexão
			String sql = "Select `nome`, `ask`, `tipo_opt` from `"
					+ util.getNameBD()
					+ "`.`pergunta` where (`active`='1') and (`questionario`='"
					+ codTextField.getText() + "');";
			SQL query = new SQL();
			ResultSet rs = query.query(sql);
			// implementação da interface JRDataSource para DataSource ResultSet
			JRResultSetDataSource jrRS = new JRResultSetDataSource(rs);
			// executa o relatório
			Map parametros = new HashMap();
			parametros.put("logo", util.getConf("logo"));
			JasperPrint impressao = JasperFillManager.fillReport(relatorio,
					parametros, jrRS);
			// exibe o resultado
			JasperExportManager.exportReportToPdfFile(impressao,
					"relatorios/Relatorio.pdf");
			File file = new File("relatorios/Relatorio.pdf");
			Desktop.getDesktop().open(file);
			editar(false);
			query.close();
		} catch (JRException | IOException ex) {
			Logger.getLogger(this.getName()).log(Level.SEVERE, null, ex);
		}
	}

	private void addButtonActionPerformed(ActionEvent e) {
		TDefaultTableModel model = (TDefaultTableModel) askTable.getModel();
		model.addRow(new Object[] { "", Boolean.TRUE, "-1", "" });
		askTable.setModel(model);
		int idx = askTable.getRowCount() - 1;
		addCombo(2);
		askTable.setRowSelectionInterval(idx, idx);
	}

	private void subButtonActionPerformed(ActionEvent e) {
		int idx = askTable.getSelectedRow();
		Object[] opcoes = { "Não", "Sim", "Cancelar" };
		SQL b = new SQL();
		int confirmar = JOptionPane.showOptionDialog(null,
				"Você realmente deseja apagar esse registro?", null, 1, 1,
				null, opcoes, 0);
		if (confirmar == 1) {
			String sql = "Update `" + util.getNameBD() + "`.`pergunta` Set ";
			sql += "`active`='0', `questionario`='0', `user`='"
					+ util.getUserSis() + "' ";
			sql += "Where `idask`='" + askTable.getValueAt(idx, 3) + "';";
			b.executarSQL(sql);
			carregarRegistro(Integer.parseInt(codTextField.getText()));
			b.close();
			JOptionPane
					.showMessageDialog(null, "Registro apagado com sucesso.");
		}
		editar(false);
	}

	private void newButtonActionPerformed(ActionEvent e) {
		editar(true);
		limpar();
	}

	private void editButtonActionPerformed(ActionEvent e) {
		editar(true);
	}

	private void saveButtonActionPerformed(ActionEvent e) {
		String sql = "", id;
		SQL b = new SQL();
		int ok = 0;
		index = dadosTable.getSelectedRow();
		TDefaultTableModel tabela = (TDefaultTableModel) askTable.getModel();
		if (!codTextField.getText().trim().isEmpty()) {
			sql = "Update `" + util.getNameBD()
					+ "`.`questionario` Set `nome`='" + nomeTextField.getText()
					+ "', `descricao`='" + descTextArea.getText()
					+ "', `user`='" + util.getUserSis()
					+ "' where (`idquest`='" + codTextField.getText() + "');";
			ok += b.executarSQL(sql);
			for (int i = 0; i < tabela.getRowCount(); ++i) {
				id = (String) tabela.getValueAt(i, 3);
				if (!id.trim().isEmpty()) {
					ok += upDateAsk(tabela, id, i);
				} else {
					ok += addAsk(tabela, codTextField.getText(), i);
				}
			}
			if (ok > 0) {
				JOptionPane.showMessageDialog(null,
						"Dados atualizados com sucesso");
			}
		} else {
			if (!nomeTextField.getText().trim().isEmpty()) {
				sql = "Insert Into `"
						+ util.getNameBD()
						+ "`.`questionario` (`nome`, `descricao`, `user`) Values ('"
						+ nomeTextField.getText() + "', '"
						+ descTextArea.getText() + "', '" + util.getUserSis()
						+ "');";
				if (b.executarSQL(sql) > 0) {
					ResultSet rs = new SQL()
							.query("Select idquest id from questionario order by idquest desc limit 1;");
					try {
						rs.next();
						id = rs.getString("id");
						for (int i = 0; i < tabela.getRowCount(); ++i) {
							ok += addAsk(tabela, id, i);
						}
						if (ok > 0) {
							JOptionPane.showMessageDialog(null,
									"Dados inseridos com sucesso");
						}
					} catch (SQLException e1) {
						e1.printStackTrace();
					}
				}
			}
		}
		carregarTabela(b.carregarTabela("questionario"));
		if (index >= 0) {
			carregarRegistro(index);
		} else {
			total = dadosTable.getRowCount() - 1;
			carregarRegistro(total);
		}
		editar(false);
		b.close();
	}

	private int upDateAsk(TDefaultTableModel tabela, String id, int i) {
		int ask = (boolean) tabela.getValueAt(i, 1) ? 1 : 0;
		String sql = "Update `" + util.getNameBD()
				+ "`.`pergunta` Set `nome`='" + tabela.getValueAt(i, 0)
				+ "', `tipo_opt`='" + tabela.getValueAt(i, 2) + "', `ask`='"
				+ ask + "', `user`='" + util.getUserSis()
				+ "' where (`questionario`='" + codTextField.getText()
				+ "') and (`idask`='" + id + "');";
		return new SQL().executarSQL(sql);
	}

	private int addAsk(TDefaultTableModel tabela, String id, int i) {
		int ask = (boolean) tabela.getValueAt(i, 1) ? 1 : 0;
		String key = (String) tabela.getValueAt(i, 2);
		int opt = opt_ask.containsKey(key) ? opt_ask.get(key) : -1;
		String sql = "Insert Into `"
				+ util.getNameBD()
				+ "`.`pergunta` (`nome`, `questionario`, `tipo_opt`, `ask`, `user`) values ('"
				+ tabela.getValueAt(i, 0) + "', '" + id + "', '" + opt + "', '"
				+ ask + "', '" + util.getUserSis() + "');";
		return new SQL().executarSQL(sql);
	}

	private void deleteButtonActionPerformed(ActionEvent e) {
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
							int us = rs.getInt("idusuario");
							if (rs.getInt("nivel") <= levelTab) {
								String sql = "Update `"
										+ util.getNameBD()
										+ "`.`questionario` Set `active`='0', `user`='"
										+ us + "' Where `" + tableCols.get(0)
										+ "`='" + codTextField.getText() + "';";
								b.executarSQL(sql);
								carregarTabela(b.carregarTabela("questionario"));
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

	private void limpar() {
		nomeTextField.setText("");
		descTextArea.setText("");
		codTextField.setText("");
		limparAsk();
	}

	private void limparAsk() {
		tableHeaders = new Vector<String>();
		tableHeaders.add("Titulo");
		tableHeaders.add("Pergunta?");
		tableHeaders.add("Tipo");
		askTable.setModel(new TDefaultTableModel(null, tableHeaders));
		askTable.getColumnModel().getColumn(0).setPreferredWidth(300);
		askTable.getColumnModel().getColumn(1).setPreferredWidth(65);
		askTable.getColumnModel().getColumn(2).setPreferredWidth(115);
	}

	private void editar(boolean b) {
		nomeTextField.setEnabled(b);
		descTextArea.setEnabled(b);
		askTable.setEnabled(b);
	}

	private void carregarRegistro(int cod) {
		if (cod >= 0) {
			DefaultTableModel tabela = (DefaultTableModel) dadosTable
					.getModel();
			codTextField.setText(String.valueOf(tabela.getValueAt(cod, 0)));
			nomeTextField.setText((String) tabela.getValueAt(cod, 1));
			descTextArea.setText((String) tabela.getValueAt(cod, 2));
			SQL sql = new SQL();
			carregarQuestoes(sql.carregarTabela("pergunta", "(`questionario`='"
					+ codTextField.getText() + "')"));
			sql.close();
		} else {
			JOptionPane
					.showMessageDialog(
							null,
							"Você não possui o nível de acesso necessário para visualizar essa tabela.\nFavor verificar com a sua gerência.");
			setEnabled(false);
		}
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
		} catch (SQLException ex) {
			Logger.getLogger(this.getName()).log(Level.SEVERE, null, ex);
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void carregarQuestoes(ResultSet rs) {
		try {
			tableHeaders = new Vector<String>();
			tableData = new Vector();
			tableHeaders.add("Titulo");
			tableHeaders.add("Pergunta?");
			tableHeaders.add("Tipo");
			tableHeaders.add("id");
			boolean ask = false;
			while (rs.next()) {
				Vector<Object> oneRow = new Vector<Object>();
				oneRow.add(rs.getString("nome"));
				ask = rs.getInt("ask") > 0;
				oneRow.add(ask);
				oneRow.add(rs.getInt("tipo_opt"));
				oneRow.add(rs.getString("idask"));
				tableData.add(oneRow);
			}
			rs.close();
			askTable.setModel(new TDefaultTableModel(tableData, tableHeaders));
			addCombo(2);
			askTable.getColumnModel().getColumn(0).setPreferredWidth(300);
			askTable.getColumnModel().getColumn(1).setPreferredWidth(65);
			askTable.getColumnModel().getColumn(2).setPreferredWidth(115);
			util.ocultarColunas(askTable, 3, 3);
		} catch (SQLException ex) {
			Logger.getLogger(this.getName()).log(Level.SEVERE, null, ex);
		}
	}

	private void addCombo(int column) {
		String tmp = "";
		SQL sql = new SQL();
		ResultSet rs = sql.query("Select `nome`, `idtopt` from `"
				+ util.getNameBD() + "`.`tipo_opt` where `active`='1'");
		try {
			while (rs.next()) {
				tmp += rs.getString("nome") + "#";
				opt_ask.put(rs.getString("nome"), rs.getInt("idtopt"));
			}
			rs.close();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		sql.close();
		tmp.substring(0, tmp.lastIndexOf("#"));
		String[] values = tmp.split("#");
		TableColumn col = askTable.getColumnModel().getColumn(column);
		col.setCellEditor(new TComboBoxEditor(values));
		col.setCellRenderer(new TComboBoxRenderer(values));
	}

	class TDefaultTableModel extends DefaultTableModel {

		@SuppressWarnings("rawtypes")
		public TDefaultTableModel(Vector data, Vector columnNames) {
			setDataVector(data, columnNames);
		}

		public TDefaultTableModel() {
			super(0, 0);
		}

		@SuppressWarnings({ "unchecked", "rawtypes" })
		@Override
		public Class getColumnClass(int columnIndex) {
			switch (columnIndex) {
			case 0:
				return String.class;
			case 1:
				return Boolean.class;
			case 2:
				return String.class;
			case 3:
				return String.class;
			}
			return null;
		}
	}

	private HashMap<String, Integer> opt_ask;
	private final Util util = new Util();
	private int levelTab = Integer.MIN_VALUE;
	private Vector<String> tableCols;
	private int index;
	private int total;
	private Vector<String> tableHeaders = new Vector<String>();
	@SuppressWarnings("rawtypes")
	private Vector tableData = new Vector();
	private JPanel panel;
	private JPanel buscaPanel;
	private JTextField nomeTextField;
	private JTable askTable;
	private JTable dadosTable;
	private JTextField codTextField;
	private JLabel codLabel;
	private JButton addButton;
	private JButton subButton;
	private JButton findButton;
	private JButton firstButton;
	private JButton beforeButton;
	private JButton nextButton;
	private JButton lastButton;
	private JButton printButton;
	private JButton newButton;
	private JLabel lblNome;
	private JLabel lblDescrio;
	private JLabel lblQuestes;
	private JScrollPane scrollPane;
	private JScrollPane scrollPane_1;
	private JTextArea descTextArea;
	private JButton editButton;
	private JButton saveButton;
	private JButton deleteButton;
}
