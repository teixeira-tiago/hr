package cadastro;

import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
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
import javax.swing.table.JTableHeader;

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
import utiliter.component.TTable;

/**
 * @author Tiago A. Teixeira {@link tiago.a.teixeira@acm.org}
 * 
 */
@SuppressWarnings("serial")
public class TelaCadastro extends JDialog {

	/**
	 * @param frame
	 */
	public TelaCadastro(JFrame frame) {
		super(frame);
	}

	public TelaCadastro() {
		init();
	}

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					TelaCadastro dialog = new TelaCadastro(new JFrame());
					dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
					dialog.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the dialog.
	 */

	public void setTabela(String tabela) {
		this.tabela = tabela;
	}

	public void init() {
		initComponents();
		initTableClick();
		SQL b = new SQL();
		carregarTabela(b.carregarTabela(tabela));
		levelTab = b.nivelTabela(tabela);
		b.close();
		index = total = dadosTable.getRowCount() - 1;
		carregarRegistro(index);
		editar(false);
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
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setBounds(0, 0, 410, 390);
		getContentPane().setLayout(null);
		setModal(true);
		// setAlwaysOnTop(true);
		setLocationRelativeTo(null);
		setResizable(false);
		buscaPanel = new JPanel();
		buscaPanel.setBounds(5, 5, 395, 60);
		buscaPanel.setLayout(null);
		buscaPanel.setPreferredSize(new Dimension(450, 75));
		getContentPane().add(buscaPanel);

		codLabel = new JLabel("Código:");
		codLabel.setBounds(5, 5, 75, 16);
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

		actionPanel = new JPanel();
		actionPanel.setBounds(5, 325, 395, 35);
		getContentPane().add(actionPanel);

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

		bodyPanel = new JPanel();
		bodyPanel.setBounds(5, 70, 400, 250);
		getContentPane().add(bodyPanel);
		bodyPanel.setLayout(null);

		scrollPane = new JScrollPane();
		scrollPane.setBounds(5, 85, 390, 160);
		bodyPanel.add(scrollPane);

		dadosTable = new TTable();
		dadosTable.addMouseListener(new java.awt.event.MouseAdapter() {
			@Override
			public void mouseClicked(java.awt.event.MouseEvent evt) {
				dadosTableMouseClicked(evt);
			}
		});
		scrollPane.setViewportView(dadosTable);

		// Não repete nos outros modulos
		lblNome = new JLabel("Nome:");
		lblNome.setHorizontalAlignment(SwingConstants.RIGHT);
		lblNome.setBounds(5, 11, 41, 16);
		bodyPanel.add(lblNome);

		nomeTextField = new JTextField();
		nomeTextField.setBounds(51, 5, 344, 24);
		bodyPanel.add(nomeTextField);
		nomeTextField.setColumns(10);

		scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(53, 34, 340, 45);
		bodyPanel.add(scrollPane_1);

		obsTextArea = new JTextArea();
		scrollPane_1.setViewportView(obsTextArea);

		lblObs = new JLabel("Obs.:");
		lblObs.setHorizontalAlignment(SwingConstants.RIGHT);
		lblObs.setBounds(5, 40, 40, 16);
		bodyPanel.add(lblObs);
	}

	private void initTableClick() {
		// pego o header da JTable
		final JTableHeader h = dadosTable.getTableHeader();
		// adiciono o listener não a um botão, mas ao header
		h.addMouseListener(new MouseAdapter() {
			@Override
			@SuppressWarnings({ "unchecked", "rawtypes" })
			public void mouseClicked(MouseEvent e) {
				final int colIndex = h.columnAtPoint(new Point(e.getX(), e
						.getY()));
				if (asc) {
					Collections.sort(tableData, new Comparator() {
						@Override
						public int compare(Object o1, Object o2) {
							Vector v1 = (Vector) o1;
							Vector v2 = (Vector) o2;
							Comparable i1 = (Comparable) v1.get(colIndex);
							Comparable i2 = (Comparable) v2.get(colIndex);
							return i1.compareTo(i2);
						}
					});
					asc = false;
				} else {
					Collections.sort(tableData, new Comparator() {
						@Override
						public int compare(Object o1, Object o2) {
							Vector v1 = (Vector) o1;
							Vector v2 = (Vector) o2;
							Comparable i1 = (Comparable) v1.get(colIndex);
							Comparable i2 = (Comparable) v2.get(colIndex);
							return i2.compareTo(i1);
						}
					});
					asc = true;
				}
				dadosTable.repaint();
			}
		});
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
		String sql = "", aux;
		SQL b = new SQL();
		index = dadosTable.getSelectedRow();
		if (!codTextField.getText().trim().isEmpty()) {
			if (tableCols.size() > 2) {
				aux = tableCols.get(1) + "`='" + nomeTextField.getText()
						+ "', `" + tableCols.get(2) + "`='"
						+ obsTextArea.getText() + "', ";
			} else {
				aux = tableCols.get(1) + "`='" + nomeTextField.getText()
						+ "', ";
			}
			sql = "Update `" + util.getNameBD() + "`.`" + tabela + "` Set `"
					+ aux + "`user`='" + util.getUserSis() + "' " + "Where `"
					+ tableCols.get(0) + "`='" + codTextField.getText() + "';";
			if (b.executarSQL(sql) > 0) {
				JOptionPane.showMessageDialog(null,
						"Dados atualizados com sucesso");
			}
		} else {
			if (!nomeTextField.getText().trim().isEmpty()) {
				if (tableCols.size() > 2) {
					aux = tableCols.get(1) + "`, `" + tableCols.get(2)
							+ "`, `user`) VALUES (" + "'"
							+ nomeTextField.getText() + "', '"
							+ obsTextArea.getText() + "', '";
				} else {
					aux = tableCols.get(1) + "`, `user`) VALUES (" + "'"
							+ nomeTextField.getText() + "', '";
				}
				sql = "Insert Into `" + util.getNameBD() + "`.`" + tabela
						+ "` (`" + aux + util.getUserSis() + "');";
				if (b.executarSQL(sql) > 0) {
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
					.load("relatorios/cadastro/Cadastro.jrxml");
			// compila o relatório
			JasperReport relatorio = JasperCompileManager
					.compileReport(desenho);
			// estabelece conexão
			SQL b = new SQL();
			ResultSet rs = b.query("Select `" + tableCols.get(0)
					+ "` as codigo, `" + tableCols.get(1) + "` as nome From `"
					+ util.getNameBD() + "`.`" + tabela
					+ "` Where (`active`='1') order by " + tableCols.get(0)
					+ ";");
			// implementação da interface JRDataSource para DataSource ResultSet
			JRResultSetDataSource jrRS = new JRResultSetDataSource(rs);
			// executa o relatório
			Map parametros = new HashMap();
			parametros.put("logo", util.getConf("logo"));
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

	private void editar(boolean b) {
		nomeTextField.setEditable(b);
		obsTextArea.setEditable(b);
	}

	private void limpar() {
		codTextField.setText("");
		nomeTextField.setText("");
		obsTextArea.setText("");
	}

	private void carregarRegistro(int cod) {
		if (cod >= 0) {
			DefaultTableModel tabela = (DefaultTableModel) dadosTable
					.getModel();
			codTextField.setText(String.valueOf(tabela.getValueAt(cod, 0)));
			nomeTextField.setText((String) tabela.getValueAt(cod, 1));
			if (tabela.getColumnCount() > 2) {
				obsTextArea.setText((String) tabela.getValueAt(cod, 2));
			}
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
			int count = 0;
			for (int i = 1; i < meta.getColumnCount() + 1; ++i) {
				col = meta.getColumnLabel(i);
				tableCols.add(col);
				tmp = col.substring(0, 1).toUpperCase() + col.substring(1);
				tmp = tmp.contains("Id") ? "Código" : tmp;
				tableHeaders.add(tmp);
				++count;
			}
			while (rs.next()) {
				Vector<Object> oneRow = new Vector<Object>();
				for (Object coluna : tableCols) {
					oneRow.add(rs.getObject((String) coluna));
				}
				tableData.add(oneRow);
			}
			if (count < 3) {
				obsTextArea.setVisible(false);
				lblObs.setVisible(false);
				scrollPane.setBounds(5, 34, 390, 210);
			}
			rs.close();
			dadosTable.setModel(new DefaultTableModel(tableData, tableHeaders));
			dadosTable.getColumnModel().getColumn(0).setPreferredWidth(50);
			dadosTable.getColumnModel().getColumn(1).setPreferredWidth(350);
			if (dadosTable.getColumnCount() > 2) {
				util.ocultarColunas(dadosTable, 2,
						dadosTable.getColumnCount() - 1);
			}
		} catch (SQLException ex) {
			Logger.getLogger(this.getName()).log(Level.SEVERE, null, ex);
		}
	}

	private final Util util = new Util();
	private int levelTab = Integer.MIN_VALUE;
	private Vector<String> tableCols;
	private String tabela = "";
	private int index;
	private int total;
	private Vector<String> tableHeaders = new Vector<String>();
	@SuppressWarnings("rawtypes")
	private Vector tableData = new Vector();
	private boolean asc = true;
	private JPanel buscaPanel;
	private JPanel actionPanel;
	private JPanel bodyPanel;
	private JLabel codLabel;
	private JLabel lblObs;
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
	private JTable dadosTable;
	private JScrollPane scrollPane;
	private JScrollPane scrollPane_1;
	private JTextField codTextField;
	private JTextArea obsTextArea;

	private JLabel lblNome;
	private JTextField nomeTextField;
}
