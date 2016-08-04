/**
 * 
 */
package pesquisa;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.filechooser.FileNameExtensionFilter;

import net.miginfocom.swing.MigLayout;
import utiliter.SQL;
import utiliter.TextFile;
import utiliter.Util;
import utiliter.component.AComboBox;
import utiliter.component.TDate;

/**
 * @author Tiago A. Teixeira {@link tiago.a.teixeira@acm.org}
 *
 */
@SuppressWarnings("serial")
public class ImportPesquisa extends JInternalFrame {

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					ImportPesquisa window = new ImportPesquisa(
							new JInternalFrame());
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

	private final JInternalFrame questionario;

	public ImportPesquisa(JInternalFrame questionario) {
		this.questionario = questionario;
		initialize();
		initCombo();
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
		setTitle("Importação de Pesquisa");
		setMaximizable(true);
		setResizable(true);
		setClosable(true);
		setBounds(1, 1, 400, 240);
		getContentPane()
				.setLayout(
						new MigLayout(
								"",
								"5[41px]0[5px]0[5px]0[5px]0[6px]0[5px]0[10px,grow]0[6px]0[21px]0[5px]0[90px]0[30px:30px:30px]5",
								"5[24px]0[6px:6px]0[24px]0[6px:6px]0[24px]0[6px:6px]0[24px]0[6px:6px]0[30px]5"));

		JLabel label = new JLabel("Titulo:");
		label.setHorizontalAlignment(SwingConstants.RIGHT);
		getContentPane().add(label, "cell 0 0,alignx right,aligny center");

		setorCombo = new HashMap<Integer, Integer>();
		questCombo = new HashMap<Integer, Integer>();

		nomeComboBox = new AComboBox(true);
		nomeComboBox.setSelectedIndex(-1);
		getContentPane().add(nomeComboBox, "cell 2 0 10 1,grow");

		JLabel label_1 = new JLabel("Data:");
		label_1.setHorizontalAlignment(SwingConstants.RIGHT);
		getContentPane().add(label_1, "cell 8 2,alignx left,aligny center");

		dataTimePicker = new TDate();
		dataTimePicker.getJFormattedTextField().setEditable(true);
		dataTimePicker.setTextEditable(true);
		getContentPane().add(dataTimePicker, "cell 10 2 2 1,grow");

		JLabel label_2 = new JLabel("Setor:");
		label_2.setHorizontalAlignment(SwingConstants.RIGHT);
		getContentPane().add(label_2, "cell 0 2,alignx right,aligny center");

		setorComboBox = new AComboBox();
		setorComboBox.setSelectedIndex(-1);
		getContentPane().add(setorComboBox, "cell 2 2 5 1,grow");

		JLabel label_3 = new JLabel("Questionario:");
		label_3.setHorizontalAlignment(SwingConstants.RIGHT);
		getContentPane()
				.add(label_3, "cell 0 4 5 1,alignx right,aligny center");

		questComboBox = new AComboBox();
		questComboBox.setSelectedIndex(-1);
		getContentPane().add(questComboBox, "cell 6 4 5 1,grow");

		JPanel panel_2 = new JPanel();
		panel_2.setLayout(null);
		getContentPane().add(panel_2, "cell 11 4,grow");

		JButton questButton = new JButton("...");
		questButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				questButtonActionPerformed(e);
			}
		});
		questButton.setBounds(4, 4, 20, 20);
		panel_2.add(questButton);

		JLabel lblArquivo = new JLabel("Arquivo:");
		getContentPane().add(lblArquivo,
				"cell 0 6 3 1,alignx right,aligny center");

		fileTextField = new JTextField();
		getContentPane().add(fileTextField, "cell 4 6 7 1,grow");
		fileTextField.setColumns(10);

		JPanel panel_1 = new JPanel();
		getContentPane().add(panel_1, "cell 11 6,grow");
		panel_1.setLayout(null);

		JButton btnOpenFile = new JButton("...");
		btnOpenFile.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				btnOpenFileActionPerformed(e);
			}
		});
		btnOpenFile.setBounds(4, 4, 20, 20);
		panel_1.add(btnOpenFile);

		JPanel panel = new JPanel();
		getContentPane().add(panel, "cell 0 8 12 1,grow");

		JButton btnImportar = new JButton("Importar");
		btnImportar.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				btnImportarActionPerformed(e);
			}
		});
		panel.add(btnImportar);
	}

	private void btnOpenFileActionPerformed(ActionEvent e) {
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setAcceptAllFileFilterUsed(false);
		fileChooser.addChoosableFileFilter(new FileNameExtensionFilter(
				"Arquivo .csv", "csv"));
		fileChooser.setCurrentDirectory(new File("."));
		int result = fileChooser.showOpenDialog(null);
		if (result == JFileChooser.APPROVE_OPTION) {
			File file = fileChooser.getSelectedFile();
			if (file != null) {
				fileTextField.setText(fileChooser.getSelectedFile()
						.getAbsolutePath());
			}
		}
	}

	private void questButtonActionPerformed(ActionEvent e) {
		questionario.setVisible(true);
		((Questionario) questionario).setPosicao();
	}

	private void btnImportarActionPerformed(ActionEvent e) {
		try {
			if (!fileTextField.getText().trim().isEmpty()) {
				TextFile arquivo = new TextFile(fileTextField.getText());
				HashMap<Integer, String> titulo = new HashMap<Integer, String>();
				TreeMap<String, String> line = new TreeMap<String, String>();
				Iterator<String> itFile = arquivo.conteudo().iterator();
				String dado = "", tmp;
				String aux = "";
				String[] vDados;
				while (itFile.hasNext()) {
					String Linha = itFile.next();
					if (Linha.contains("ja importado")) {
						JOptionPane
								.showMessageDialog(
										null,
										"Este arquivo já foi importando antes.\nFavor selecione outro arquivo para ser importado.");
						return;
					}
					vDados = Linha.split("[,]");
					if (Linha.contains("[")) {
						for (int i = 1; i < vDados.length; ++i) {
							tmp = vDados[i];
							if (tmp.contains("[")) {
								dado = tmp.substring(tmp.indexOf("[") + 1,
										tmp.indexOf("]"));
								titulo.put(i, dado);
							} else {
								titulo.put(i, vDados[i]);
							}
						}
					} else {
						for (int i = 1; i < vDados.length; ++i) {
							dado = vDados[i].replace("\"", "");
							aux = titulo.get(i);
							line.put(aux, (line.get(aux) == null ? aux + "#"
									: line.get(aux) + "&") + dado);

						}
					}
				}
				HashMap<String, Integer> ask = new HashMap<String, Integer>();
				HashMap<String, Integer> option = new HashMap<String, Integer>();
				SQL query = new SQL();
				ResultSet rs = query
						.query("Select `idask`, `nome` from `"
								+ util.getNameBD()
								+ "`.`pergunta` where `active`='1';");
				while (rs.next()) {
					ask.put(rs.getString("nome"), rs.getInt("idask"));
				}
				rs = query.query("Select `idoask`, `nome` from `"
						+ util.getNameBD() + "`.`opt_ask` where `active`='1';");
				while (rs.next()) {
					option.put(rs.getString("nome"), rs.getInt("idoask"));
				}
				rs.close();

				String sql;
				sql = "Insert Into `"
						+ util.getNameBD()
						+ "`.`pesquisa` (`nome`, `descricao`, `departamento`, `questionario`, `pergunta`, `opcoes`, `user`, `data`) values ";
				tmp = "";
				String desc;
				for (String ln : line.values()) {
					vDados = ln.split("#")[1].split("&");
					for (String dt : vDados) {
						desc = nomeComboBox.getSelectedItem()
								+ " em "
								+ dataTimePicker.getJFormattedTextField()
										.getText();
						tmp += "('"
								+ nomeComboBox.getSelectedItem()
								+ "', '"
								+ desc
								+ "', '"
								+ setorCombo.get(setorComboBox
										.getSelectedIndex())
								+ "', '"
								+ questCombo.get(questComboBox
										.getSelectedIndex())
								+ "', '"
								+ ask.get(ln.split("#")[0])
								+ "', '"
								+ option.get(dt)
								+ "', '"
								+ util.getUserSis()
								+ "', '"
								+ util.dateToSQLdate(util
										.strToDate(dataTimePicker
												.getJFormattedTextField()
												.getText())) + "'), ";
					}
				}
				sql += tmp.substring(0, tmp.lastIndexOf(", ")) + ";";
				if (query.executarSQL(sql) > 0) {
					JOptionPane.showMessageDialog(null,
							"Arquivo importado com sucesso.");
				}
				query.close();
				arquivo.add("ja importado", 0);
				arquivo.salvar(fileTextField.getText());
			} else {
				JOptionPane.showMessageDialog(null,
						"Favor informe o arquivo a ser importado.");
			}
		} catch (IOException | SQLException ex) {
			JOptionPane
					.showMessageDialog(
							null,
							"O arquivo informado é inválido, favor selecionar outr arquivo ou procurar o suporte.");
			ex.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	private void initCombo() {
		setorComboBox.setModel(util.initComboBox("departamento",
				new String[] { "depto" }, "iddepto"));
		setorCombo = util.initCombo("departamento", "iddepto");
		setorComboBox.setSelectedIndex(-1);
		questComboBox.setModel(util.initComboBox("questionario",
				new String[] { "nome" }, "idquest"));
		questCombo = util.initCombo("questionario", "idquest");
		questComboBox.setSelectedIndex(-1);
		SQL sql = new SQL();
		try {
			ResultSet rs = sql.query("Select distinct `nome` from `"
					+ util.getNameBD() + "`.`pesquisa` where (`active`='1');");
			while (rs.next()) {
				nomeComboBox.addItem(rs.getString("nome"));
			}
			nomeComboBox.setSelectedIndex(-1);
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		sql.close();
	}

	private HashMap<Integer, Integer> setorCombo;
	private HashMap<Integer, Integer> questCombo;
	private final Util util = new Util();
	private JTextField fileTextField;
	private AComboBox nomeComboBox;
	private AComboBox setorComboBox;
	private AComboBox questComboBox;
	private TDate dataTimePicker;
}
