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
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
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
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.table.DefaultTableModel;

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
import utiliter.Moeda;
import utiliter.SQL;
import utiliter.Util;

/**
 * @author Tiago A. Teixeira {@link tiago.a.teixeira@acm.org}
 *
 */
@SuppressWarnings("serial")
public class Salario extends JInternalFrame {
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					Salario window = new Salario();
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
	public Salario() {
		initialize();
		SQL b = new SQL();
		carregarTabela(b.carregarTabela("cargo"));
		levelTab = b.nivelTabela("cargo");
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
		setTitle("Pesquisa Salarial");
		setBounds(1, 1, 560, 570);
		setMaximizable(true);
		setClosable(true);
		setResizable(true);

		dadosTable = new JTable();
		getContentPane().setLayout(
				new MigLayout("", "[66px]0[4px]0[475px,grow]",
						"[60px]0[6px]0[24px]0[6px]0[350px,grow]0[6px]0[35px]"));
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

		lblNome = new JLabel("Cargo:");
		lblNome.setHorizontalAlignment(SwingConstants.RIGHT);
		getContentPane().add(lblNome, "cell 0 2,alignx right,aligny center");

		nomeTextField = new JTextField();
		nomeTextField.setEditable(false);
		getContentPane().add(nomeTextField, "cell 2 2,grow");
		nomeTextField.setColumns(10);

		lblQuestes = new JLabel("Salários:");
		lblQuestes.setHorizontalAlignment(SwingConstants.RIGHT);
		getContentPane().add(lblQuestes, "cell 0 4,alignx right,aligny top");

		scrollPane_1 = new JScrollPane();
		getContentPane().add(scrollPane_1, "cell 2 4,grow");

		DefaultTableModel modelo = new DefaultTableModel();
		askTable = new JTable(modelo);
		scrollPane_1.setViewportView(askTable);
		limparAsk();

		panel = new JPanel();
		FlowLayout flowLayout = (FlowLayout) panel.getLayout();
		flowLayout.setVgap(0);
		getContentPane().add(panel, "cell 0 6 3 1,grow");

		addButton = new JButton("");
		addButton.setPreferredSize(new Dimension(30, 30));
		addButton
				.setIcon(new ImageIcon(getClass().getResource("/img/add.png")));
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
					+ util.getNameBD() + "`.`cargo` where `" + tableCols.get(0)
					+ "` like \"" + codTextField.getText() + "%\""));
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
			String sql = "Select `nome` , `cargo`, `fonte`, `valor`, `salarioini`, `salariofim` from `"
					+ util.getNameBD()
					+ "`.`salario` s, `"
					+ util.getNameBD()
					+ "`.`cargo` c where (s.active='1') and (s.cargo=c.idcargo) and (s.cargo='"
					+ codTextField.getText() + "');";
			BigDecimal salario = new BigDecimal("0.0");
			ArrayList<Moeda> calculus = new ArrayList<Moeda>();
			ResultSet calc = new SQL().query(sql);
			while (calc.next()) {
				calculus.add(new Moeda(calc.getBigDecimal("valor")));
				salario = new BigDecimal("0.0");
				salario = salario.add(calc.getBigDecimal("salarioini"));
				salario = salario.add(calc.getBigDecimal("salariofim"));
				salario = salario.divide(new BigDecimal("2.0"));
			}
			Moeda aux = new Moeda("0.0");
			BigDecimal total = new BigDecimal("0.0");
			BigDecimal despadrao = new BigDecimal("0.0");
			BigDecimal mediaplus2des = new BigDecimal("0.0");
			BigDecimal medialess2des = new BigDecimal("0.0");
			BigDecimal maxsmed = new BigDecimal("0.0");
			BigDecimal medsmin = new BigDecimal("0.0");
			BigDecimal maximo = new BigDecimal("0.0");
			BigDecimal quartil3 = new BigDecimal("0.0");
			BigDecimal quartil1 = new BigDecimal("0.0");
			BigDecimal mediana = new BigDecimal("0.0");
			BigDecimal media = new BigDecimal("0.0");
			BigDecimal minimo = new BigDecimal("999999999999.99");
			BigDecimal posmedia = new BigDecimal("0.0");
			BigDecimal posmediana = new BigDecimal("0.0");
			Iterator<Moeda> it = calculus.iterator();
			while (it.hasNext()) {
				aux = it.next();
				total = total.add(aux.getValor());
				if (aux.max(maximo)) {
					maximo = aux.getValor();
				}
				if (aux.les(minimo)) {
					minimo = aux.getValor();
				}
			}
			media = aux.media(calculus).getValor();
			despadrao = aux.desvio(calculus).getValor();
			mediana = aux.mediana(calculus).getValor();
			quartil1 = aux.quartil(calculus, 1).getValor();
			quartil3 = aux.quartil(calculus, 3).getValor();
			mediaplus2des = media
					.add(despadrao.multiply(new BigDecimal("2.0"))).setScale(2,
							RoundingMode.HALF_EVEN);
			medialess2des = media.subtract(
					despadrao.multiply(new BigDecimal("2.0"))).setScale(2,
					RoundingMode.HALF_EVEN);
			maxsmed = maximo.divide(media, 4, RoundingMode.HALF_EVEN)
					.subtract(new BigDecimal("1.0"))
					.multiply(new BigDecimal("100.0"))
					.setScale(2, RoundingMode.HALF_EVEN);
			medsmin = media.divide(minimo, 4, RoundingMode.HALF_EVEN)
					.subtract(new BigDecimal("1.0"))
					.multiply(new BigDecimal("100.0"))
					.setScale(2, RoundingMode.HALF_EVEN);
			posmedia = salario.divide(media, 4, RoundingMode.HALF_EVEN)
					.subtract(new BigDecimal("1.0"))
					.multiply(new BigDecimal("100.0"))
					.setScale(2, RoundingMode.HALF_EVEN);
			posmediana = salario.divide(mediana, 4, RoundingMode.HALF_EVEN)
					.subtract(new BigDecimal("1.0"))
					.multiply(new BigDecimal("100.0"))
					.setScale(2, RoundingMode.HALF_EVEN);
			NumberFormat f = NumberFormat.getCurrencyInstance();
			// gerando o jasper design
			JasperDesign desenho = JRXmlLoader
					.load("relatorios/cargo/Salario.jrxml");
			// compila o relatório
			JasperReport relatorio = JasperCompileManager
					.compileReport(desenho);
			// estabelece conexão

			SQL query = new SQL();
			ResultSet rs = query.query(sql);
			// implementação da interface JRDataSource para DataSource ResultSet
			JRResultSetDataSource jrRS = new JRResultSetDataSource(rs);

			// executa o relatório
			Map parametros = new HashMap();
			parametros.put("logo", util.getConf("logo"));
			parametros.put("frequencia", calculus.size() + "");
			parametros.put(
					"despadrao",
					f.format(
							despadrao.setScale(2, RoundingMode.HALF_EVEN)
									.doubleValue()).toString());
			parametros.put("mediaplus2des",
					f.format(mediaplus2des.doubleValue()).toString());
			parametros.put("medialess2des",
					f.format(medialess2des.doubleValue()).toString());
			parametros.put("maxsmed", maxsmed.toString() + "%");
			parametros.put("medsmin", medsmin.toString() + "%");
			parametros.put("maximo", f.format(maximo.doubleValue()).toString());
			parametros.put("quartil3", f.format(quartil3.doubleValue())
					.toString());
			parametros.put("quartil1", f.format(quartil1.doubleValue())
					.toString());
			parametros.put(
					"mediana",
					f.format(
							mediana.setScale(2, RoundingMode.HALF_EVEN)
									.doubleValue()).toString());
			parametros.put("media", f.format(media.doubleValue()).toString());
			parametros.put("minimo", f.format(minimo.doubleValue()).toString());
			parametros.put("posmedia", posmedia.toString() + "%");
			parametros.put("posmediana", posmediana.toString() + "%");
			parametros.put("salario", f.format(salario.doubleValue())
					.toString());
			JasperPrint impressao = JasperFillManager.fillReport(relatorio,
					parametros, jrRS);
			// exibe o resultado
			JasperExportManager.exportReportToPdfFile(impressao,
					"relatorios/Relatorio.pdf");
			File file = new File("relatorios/Relatorio.pdf");
			Desktop.getDesktop().open(file);
			editar(false);
			query.close();
		} catch (JRException | IOException | SQLException ex) {
			Logger.getLogger(this.getName()).log(Level.SEVERE, null, ex);
		}
	}

	private void addButtonActionPerformed(ActionEvent e) {
		DefaultTableModel model = (DefaultTableModel) askTable.getModel();
		model.addRow(new Object[] { "", "", "" });
		askTable.setModel(model);
		int idx = askTable.getRowCount() - 1;
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
			String sql = "Update `" + util.getNameBD() + "`.`salario` Set ";
			sql += "`active`='0', `user`='" + util.getUserSis() + "' ";
			sql += "Where `idsalario`='" + askTable.getValueAt(idx, 2) + "';";
			b.executarSQL(sql);
			carregarRegistro(Integer.parseInt(codTextField.getText()));
			b.close();
			JOptionPane
					.showMessageDialog(null, "Registro apagado com sucesso.");
		}
		editar(false);
	}

	private void editButtonActionPerformed(ActionEvent e) {
		editar(true);
	}

	private void saveButtonActionPerformed(ActionEvent e) {
		String id;
		SQL b = new SQL();
		int ok = 0;
		index = dadosTable.getSelectedRow();
		DefaultTableModel tabela = (DefaultTableModel) askTable.getModel();
		if (!codTextField.getText().trim().isEmpty()) {
			for (int i = 0; i < tabela.getRowCount(); ++i) {
				id = (String) tabela.getValueAt(i, 2);
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
				for (int i = 0; i < tabela.getRowCount(); ++i) {
					ok += addAsk(tabela, codTextField.getText(), i);
				}
				if (ok > 0) {
					JOptionPane.showMessageDialog(null,
							"Dados inseridos com sucesso");
				}

			}
		}
		carregarTabela(b.carregarTabela("cargo"));
		if (index >= 0) {
			carregarRegistro(index);
		} else {
			total = dadosTable.getRowCount() - 1;
			carregarRegistro(total);
		}
		editar(false);
		b.close();
	}

	private int upDateAsk(DefaultTableModel tabela, String id, int i) {
		String valor = (String) tabela.getValueAt(i, 1);
		valor = valor.contains(",") ? valor.replace(",", ".") : valor;
		String sql = "Update `" + util.getNameBD()
				+ "`.`salario` Set `fonte`='" + tabela.getValueAt(i, 0)
				+ "', `valor`='" + valor + "', `user`='" + util.getUserSis()
				+ "' where (`cargo`='" + codTextField.getText()
				+ "') and (`idsalario`='" + id + "');";
		return new SQL().executarSQL(sql);
	}

	private int addAsk(DefaultTableModel tabela, String id, int i) {
		String valor = (String) tabela.getValueAt(i, 1);
		valor = valor.contains(",") ? valor.replace(",", ".") : valor;
		String sql = "Insert Into `" + util.getNameBD()
				+ "`.`salario` (`fonte`, `valor`, `cargo`, `user`) values ('"
				+ tabela.getValueAt(i, 0) + "', '" + valor + "', '" + id
				+ "', '" + util.getUserSis() + "');";
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
										+ "`.`salario` Set `active`='0', `user`='"
										+ us + "' Where `cargo`='"
										+ codTextField.getText() + "';";
								b.executarSQL(sql);
								carregarTabela(b.carregarTabela("cargo"));
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
		limparAsk();
	}

	private void limparAsk() {
		tableHeaders = new Vector<String>();
		tableHeaders.add("Empresa");
		tableHeaders.add("Salário");
		askTable.setModel(new DefaultTableModel(null, tableHeaders));
		askTable.getColumnModel().getColumn(0).setPreferredWidth(300);
		askTable.getColumnModel().getColumn(1).setPreferredWidth(65);
	}

	private void editar(boolean b) {
		askTable.setEnabled(b);
	}

	private void carregarRegistro(int cod) {
		if (cod >= 0) {
			DefaultTableModel tabela = (DefaultTableModel) dadosTable
					.getModel();
			codTextField.setText(String.valueOf(tabela.getValueAt(cod, 0)));
			nomeTextField.setText((String) tabela.getValueAt(cod, 1));
			SQL sql = new SQL();
			carregarSalarios(sql.carregarTabela("salario", "(`cargo`='"
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
	private void carregarSalarios(ResultSet rs) {
		try {
			tableHeaders = new Vector<String>();
			tableData = new Vector();
			tableHeaders.add("Empresa");
			tableHeaders.add("Valor");
			tableHeaders.add("id");
			while (rs.next()) {
				Vector<Object> oneRow = new Vector<Object>();
				oneRow.add(rs.getString("fonte"));
				oneRow.add(rs.getDouble("valor"));
				oneRow.add(rs.getString("idsalario"));
				tableData.add(oneRow);
			}
			rs.close();
			askTable.setModel(new DefaultTableModel(tableData, tableHeaders));
			askTable.getColumnModel().getColumn(0).setPreferredWidth(300);
			askTable.getColumnModel().getColumn(1).setPreferredWidth(65);
			util.ocultarColunas(askTable, 2, 2);
		} catch (SQLException ex) {
			Logger.getLogger(this.getName()).log(Level.SEVERE, null, ex);
		}
	}

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
	private JLabel lblNome;
	private JLabel lblQuestes;
	private JScrollPane scrollPane_1;
	private JButton editButton;
	private JButton saveButton;
	private JButton deleteButton;
}
