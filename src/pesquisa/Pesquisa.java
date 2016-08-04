/**
 * 
 */
package pesquisa;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.Box;
import javax.swing.DefaultCellEditor;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

import net.miginfocom.swing.MigLayout;
import utiliter.SQL;
import utiliter.Util;
import utiliter.component.AComboBox;
import utiliter.component.TDate;

/**
 * @author Tiago A. Teixeira {@link tiago.a.teixeira@acm.org}
 * 
 */
@SuppressWarnings("serial")
public class Pesquisa extends JInternalFrame {

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					Pesquisa window = new Pesquisa();
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
	public Pesquisa() {
		initialize();
		initComboBoxes();
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
		setTitle("Pesquisa");
		setBounds(1, 1, 540, 530);
		setMaximizable(true);
		setClosable(true);
		setResizable(true);

		ask = new HashMap<Object, Integer>();
		getContentPane()
				.setLayout(
						new MigLayout(
								"",
								"5[41px]0[4px]0[23px]0[4px]0[113px,grow]0[5px]0[85px]0[5px]0[85px,grow]0[3px]0[33px]0[4px]0[125px]5",
								"5[24px]0[6px]0[24px]0[6px]0[340px,grow]0[6px]0[45px,grow]0[6px]0[35px]5"));
		lblTitulo = new JLabel("Titulo:");
		lblTitulo.setHorizontalAlignment(SwingConstants.RIGHT);
		getContentPane().add(lblTitulo, "cell 0 0,alignx left,aligny center");

		tituloComboBox = new AComboBox(true);
		tituloComboBox.setSelectedIndex(-1);
		getContentPane().add(tituloComboBox, "cell 2 0 7 1,grow");

		lblData = new JLabel("Data:");
		lblData.setHorizontalAlignment(SwingConstants.RIGHT);
		getContentPane().add(lblData, "cell 10 0,alignx left,aligny center");

		dataPesquisa = new TDate();
		dataPesquisa.getJButton().setLocation(97, 2);
		dataPesquisa.getJFormattedTextField().setSize(93, 24);
		dataPesquisa.getJFormattedTextField().setEditable(true);
		dataPesquisa.setTextEditable(true);
		getContentPane().add(dataPesquisa, "cell 12 0,grow");

		lblSetor = new JLabel("Setor:");
		lblSetor.setHorizontalAlignment(SwingConstants.RIGHT);
		getContentPane().add(lblSetor, "cell 0 2,alignx right,aligny center");

		setorComboBox = new AComboBox();
		getContentPane().add(setorComboBox, "cell 2 2 3 1,grow");

		lblQuestionario = new JLabel("Questionario:");
		lblQuestionario.setHorizontalAlignment(SwingConstants.RIGHT);
		getContentPane().add(lblQuestionario,
				"cell 6 2,alignx left,aligny center");

		quesComboBox = new AComboBox();
		quesComboBox.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				quesComboBoxItemStateChanged(e);
			}
		});
		getContentPane().add(quesComboBox, "cell 8 2 5 1,grow");

		scrollPane = new JScrollPane();
		getContentPane().add(scrollPane, "cell 2 4 11 1,grow");

		TDefaultTableModel modelo = new TDefaultTableModel();

		askTable = new Ttable(modelo);
		scrollPane.setViewportView(askTable);

		lblSugestes = new JLabel("Sugestões:");
		lblSugestes.setHorizontalAlignment(SwingConstants.RIGHT);
		getContentPane()
				.add(lblSugestes, "cell 0 6 3 1,alignx left,aligny top");

		scrollPane_1 = new JScrollPane();
		getContentPane().add(scrollPane_1, "cell 4 6 9 1,grow");

		obsTextArea = new JTextArea();
		scrollPane_1.setViewportView(obsTextArea);

		panel = new JPanel();
		FlowLayout flowLayout = (FlowLayout) panel.getLayout();
		flowLayout.setVgap(0);
		getContentPane().add(panel, "cell 0 8 13 1,grow");

		newButton = new JButton("");
		newButton
				.setIcon(new ImageIcon(getClass().getResource("/img/new.png")));
		newButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				newButtonActionPerformed(e);
			}
		});
		newButton.setPreferredSize(new Dimension(30, 30));
		panel.add(newButton);

		addButton = new JButton("");
		addButton.setIcon(new ImageIcon(Questionario.class
				.getResource("/img/add.png")));
		addButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				addButtonActionPerformed(e);
			}
		});
		addButton.setPreferredSize(new Dimension(30, 30));
		panel.add(addButton);

		saveButton = new JButton("");
		saveButton.setIcon(new ImageIcon(getClass()
				.getResource("/img/save.png")));
		saveButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				saveButtonActionPerformed(e);
			}
		});
		saveButton.setPreferredSize(new Dimension(30, 30));
		panel.add(saveButton);
	}

	private void quesComboBoxItemStateChanged(ItemEvent e) {
		if (quesComboBox.getSelectedIndex() > -1) {
			SQL sql = new SQL();
			carregarQuestoes(sql.carregarTabela("pergunta", "(`questionario`='"
					+ quesCombo.get(quesComboBox.getSelectedIndex()) + "')"));
			sql.close();
		}
	}

	private void newButtonActionPerformed(ActionEvent e) {
		tituloComboBox.setSelectedIndex(-1);
		dataPesquisa.getModel().setValue(null);
		setorComboBox.setSelectedIndex(-1);
		quesComboBox.setSelectedIndex(-1);
		obsTextArea.setText("");
		tableHeaders = new Vector<String>();
		tableHeaders.add("Pergunta");
		tableHeaders.add("Resposta");
		askTable.setModel(new TDefaultTableModel(null, tableHeaders));
		askTable.getColumnModel().getColumn(0).setPreferredWidth(365);
		askTable.getColumnModel().getColumn(1).setPreferredWidth(115);
	}

	private void addButtonActionPerformed(ActionEvent e) {
		quesComboBoxItemStateChanged(null);
		obsTextArea.setText("");
	}

	private void saveButtonActionPerformed(ActionEvent e) {
		TDefaultTableModel tabela = (TDefaultTableModel) askTable.getModel();
		ArrayList<Object> dt = new ArrayList<>();
		ArrayList<Object> datas = new ArrayList<>();
		SQL query = new SQL();
		String sql, data;
		boolean ok = false;
		dt.add(tituloComboBox.getSelectedItem());
		dt.add(obsTextArea.getText());
		dt.add(setorCombo.get(setorComboBox.getSelectedIndex()));
		dt.add(quesCombo.get(quesComboBox.getSelectedIndex()));
		data = util.dataToStr((java.util.Date) dataPesquisa.getModel()
				.getValue());
		datas.add(util.dateToSQLdate(dataPesquisa.getModel().getValue()));
		if (!data.trim().isEmpty()) {
			for (int i = 0; i < tabela.getRowCount(); ++i) {
				if ((boolean) tabela.getValueAt(i, 3)) {
					sql = "Insert Into `"
							+ util.getNameBD()
							+ "`.`pesquisa` (`nome`, `descricao`, `departamento`, `questionario`, `pergunta`, `opcoes`, `user`, `data`) values ('"
							+ dt.get(0) + "', '" + dt.get(1) + "', '"
							+ dt.get(2) + "', '" + dt.get(3) + "', '"
							+ tabela.getValueAt(i, 4) + "', '"
							+ ask.get(tabela.getValueAt(i, 1)) + "', '"
							+ util.getUserSis() + "', ?);";
					ok = query.executarSQL(sql, datas);
				}
			}
			if (ok) {
				JOptionPane.showMessageDialog(null,
						"Respostas inseridas com sucesso");
				addButtonActionPerformed(null);
			}
		} else {
			JOptionPane.showMessageDialog(null,
					"Favor insira uma data valida antes de prosseguir");
			newButtonActionPerformed(null);
		}
		query.close();
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void carregarQuestoes(ResultSet rs) {
		try {
			tableHeaders = new Vector<String>();
			tableData = new Vector();
			tableHeaders.add("Pergunta");
			tableHeaders.add("Resposta");
			tableHeaders.add("Tipo");
			tableHeaders.add("Ask");
			tableHeaders.add("id");
			boolean ask = false;
			while (rs.next()) {
				Vector<Object> oneRow = new Vector<Object>();
				oneRow.add(rs.getString("nome"));
				oneRow.add(null);
				oneRow.add(rs.getInt("tipo_opt"));
				ask = rs.getInt("ask") > 0;
				oneRow.add(ask);
				oneRow.add(rs.getString("idask"));
				tableData.add(oneRow);
			}
			rs.close();
			askTable.setModel(new TDefaultTableModel(tableData, tableHeaders));
			addCombo(1);
			askTable.getColumnModel().getColumn(0).setPreferredWidth(365);
			askTable.getColumnModel().getColumn(1).setPreferredWidth(115);
			util.ocultarColunas(askTable, 2, 4);
		} catch (SQLException ex) {
			Logger.getLogger(this.getName()).log(Level.SEVERE, null, ex);
		}
	}

	private void addCombo(int column) {
		TableColumn col = askTable.getColumnModel().getColumn(column);
		col.setCellEditor(new TCellEditor());
	}

	@SuppressWarnings({ "unchecked" })
	private void initComboBoxes() {
		setorComboBox.setModel(util.initComboBox("departamento",
				new String[] { "depto" }, "iddepto"));
		setorCombo = util.initCombo("departamento", "iddepto");
		setorComboBox.setSelectedIndex(-1);
		quesComboBox.setModel(util.initComboBox("questionario",
				new String[] { "nome" }, "idquest"));
		quesCombo = util.initCombo("questionario", "idquest");
		quesComboBox.setSelectedIndex(-1);
		SQL sql = new SQL();
		ResultSet rs = sql.query("Select `idoask`, `nome` from `"
				+ util.getNameBD() + "`.`opt_ask` where (`active`='1');");
		try {
			while (rs.next()) {
				ask.put(rs.getString("nome"), rs.getInt("idoask"));
			}
			rs = sql.query("Select distinct `nome` from `" + util.getNameBD()
					+ "`.`pesquisa` where (`active`='1');");
			while (rs.next()) {
				tituloComboBox.addItem(rs.getString("nome"));
			}
			tituloComboBox.setSelectedIndex(-1);
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		sql.close();
	}

	class TCellEditor extends DefaultCellEditor {
		@SuppressWarnings("rawtypes")
		public TCellEditor() {
			super(new JComboBox());
		}

		@SuppressWarnings("rawtypes")
		private JComboBox combo;

		@SuppressWarnings({ "rawtypes", "unchecked" })
		@Override
		public Component getTableCellEditorComponent(JTable table,
				Object value, boolean isSelected, int row, int column) {
			combo = (JComboBox) super.getTableCellEditorComponent(table, value,
					isSelected, row, column);
			combo.removeAllItems();
			Object[] items = getItemsForCell(table.getValueAt(row, 2));
			for (Object item : items) {
				combo.addItem(item);
			}
			return combo;
		}

		@Override
		public Object getCellEditorValue() {
			return combo.getSelectedItem();
		}

		private Object[] getItemsForCell(Object key) {
			String aux = "", tmp;
			try {
				HashMap<Object, Integer> tipo = new HashMap<Object, Integer>();
				SQL sql = new SQL();
				ResultSet rs = sql.query("Select `nome`, `tipo` From `"
						+ util.getNameBD()
						+ "`.`opt_ask` where (`active`='1');");
				while (rs.next()) {
					tipo.put(rs.getString("nome"), rs.getInt("tipo"));
				}
				if (util.isNumeric(String.valueOf(key))) {
					if (tipo.containsValue(key)) {
						tmp = String.valueOf(key);
					} else {
						tmp = String.valueOf(tipo.get(key));
					}
				} else {
					tmp = String.valueOf(tipo.get(key));
				}
				rs = sql.query("Select `nome` From `" + util.getNameBD()
						+ "`.`opt_ask` where (`active`='1') and (`tipo`='"
						+ tmp + "');");
				while (rs.next()) {
					aux += rs.getString("nome") + "#";
				}
				rs.close();
				sql.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			return aux.split("#");
		}
	}

	class TDefaultTableModel extends DefaultTableModel {
		@SuppressWarnings("rawtypes")
		public TDefaultTableModel(Vector data, Vector columnNames) {
			setDataVector(data, columnNames);
		}

		public TDefaultTableModel() {
			super(0, 0);
		}

		@Override
		public boolean isCellEditable(int row, int column) {
			return column != 0;
		}
	}

	class Ttable extends JTable {
		public Ttable(DefaultTableModel modelo) {
			super(modelo, null, null);
		}

		@Override
		public Component prepareRenderer(TableCellRenderer renderer, int row,
				int column) {
			Component c = super.prepareRenderer(renderer, row, column);
			if (Boolean.parseBoolean(String.valueOf(getValueAt(row, 3)))) {
				c.setBackground(Color.white);
			} else {
				c.setBackground(Color.lightGray);
			}
			if (isCellSelected(row, column)) {
				c.setBackground(Color.blue);
			}
			if ((column == 1)
					&& (Boolean
							.parseBoolean(String.valueOf(getValueAt(row, 3))))) {
				return c;
			} else if (column == 0) {
				return c;
			}
			return Box.createRigidArea(c.getPreferredSize());
		}

		@Override
		public Component prepareEditor(TableCellEditor editor, int row,
				int column) {
			Component c = super.prepareEditor(editor, row, column);
			if ((column == 1)
					&& (Boolean
							.parseBoolean(String.valueOf(getValueAt(row, 3))))) {
				return c;
			} else if (column == 0) {
				return c;
			}
			return Box.createRigidArea(c.getPreferredSize());
		}
	}

	private HashMap<Integer, Integer> setorCombo;
	private HashMap<Integer, Integer> quesCombo;
	private HashMap<Object, Integer> ask;
	private final Util util = new Util();
	private Vector<String> tableHeaders = new Vector<String>();
	@SuppressWarnings("rawtypes")
	private Vector tableData = new Vector();
	private AComboBox tituloComboBox;
	private JTable askTable;
	private TDate dataPesquisa;
	private JLabel lblTitulo;
	private JLabel lblData;
	private JLabel lblSetor;
	private JLabel lblQuestionario;
	private JLabel lblSugestes;
	private AComboBox setorComboBox;
	private AComboBox quesComboBox;
	private JScrollPane scrollPane;
	private JScrollPane scrollPane_1;
	private JTextArea obsTextArea;
	private JPanel panel;
	private JButton newButton;
	private JButton addButton;
	private JButton saveButton;
}
