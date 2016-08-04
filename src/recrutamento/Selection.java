/**
 * 
 */
package recrutamento;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JEditorPane;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.MaskFormatter;

import net.miginfocom.swing.MigLayout;
import utiliter.SQL;
import utiliter.Util;
import utiliter.component.Laudo;
import utiliter.component.TTable;

/**
 * @author Tiago A. Teixeira {@link tiago.a.teixeira@acm.org}
 *
 */
@SuppressWarnings("serial")
public class Selection extends JInternalFrame {

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					Selection frame = new Selection();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Selection() {
		initialize();
		initLista();
	}

	public void setPosicao() {
		Dimension d = this.getDesktopPane().getSize();
		this.setLocation((d.width - this.getSize().width) / 2,
				(d.height - this.getSize().height) / 2);
	}

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
		setBounds(0, 0, 800, 600);
		getContentPane()
				.setLayout(
						new MigLayout(
								"",
								"5[120px,grow]0[5px:5px]0[120px,grow]0[5px:5px]0[120px,grow]0[5px:5px]0[120px,grow]0[5px:5px]0[120px,grow]0[5px:5px]0[125px,grow]5",
								"5[16px]0[5px:5px]0[140px]0[5px:5px]0[330px,grow]0[5px:5px]0[35px]5"));
		areaScrollPane = new JScrollPane();
		getContentPane().add(areaScrollPane, "cell 0 2,grow");

		areaList = new JList();
		areaScrollPane.setViewportView(areaList);

		escScrollPane = new JScrollPane();
		getContentPane().add(escScrollPane, "cell 2 2,grow");

		escList = new JList();
		escScrollPane.setViewportView(escList);

		expScrollPane = new JScrollPane();
		getContentPane().add(expScrollPane, "cell 4 2,grow");

		expList = new JList();
		expScrollPane.setViewportView(expList);

		dispScrollPane = new JScrollPane();
		getContentPane().add(dispScrollPane, "cell 6 2,grow");

		dispList = new JList();
		dispScrollPane.setViewportView(dispList);

		outroScrollPane = new JScrollPane();
		getContentPane().add(outroScrollPane, "cell 8 2,grow");

		outroList = new JList();
		outroScrollPane.setViewportView(outroList);

		editorScrollPane = new JScrollPane();
		getContentPane().add(editorScrollPane, "cell 10 2,grow");

		editorPane = new JEditorPane();
		editorScrollPane.setViewportView(editorPane);

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

		fotoPanel = new JPanel();
		fotoPanel.setLayout(null);
		selectPanel.add(fotoPanel, "cell 0 0 1 9,grow");

		fotoLabel = new JLabel("");
		fotoLabel.setBounds(0, 0, 137, 150);
		fotoPanel.add(fotoLabel);

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

		candScrollPane = new JScrollPane();
		selectPanel.add(candScrollPane, "cell 0 9 13 1,grow");

		candTable = new TTable();
		candTable.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				candTableMouseClicked(e);
			}
		});
		candScrollPane.setViewportView(candTable);

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

		etp = new Laudo[4];
		for (int i = 0; i < etp.length; ++i) {
			etp[i] = new Laudo();
		}

		Laudo laudo = new Laudo();
		Laudo laudo1 = new Laudo();
		Laudo laudo2 = new Laudo();
		Laudo laudo3 = new Laudo();
		tabbedPane.addTab("Entrevista em Grupo", null, laudo, null);
		tabbedPane.addTab("Entrevista Individual", null, laudo1, null);
		tabbedPane.addTab("Elaboração do Parecer", null, laudo2, null);
		tabbedPane.addTab("Enceramento do Processo", null, laudo3, null);
		// tabbedPane.addTab("Grupo", null, etp[0], null);
		// tabbedPane.addTab("Individual", null, etp[1], null);
		// tabbedPane.addTab("Parecer", null, etp[2], null);
		// tabbedPane.addTab("Enceramento", null, etp[3], null);
	}

	private void candTableMouseClicked(MouseEvent evt) {
		carregarRegistro(candTable.getSelectedRow());
	}

	private void carregarRegistro(int cod) {/*
											 * DefaultTableModel tabela; String
											 * foto; int op; switch
											 * (tabbedPane.getSelectedIndex()) {
											 * case 1: tabela =
											 * (DefaultTableModel)
											 * etp1Table.getModel();
											 * nomeEtp1TextField
											 * .setText((String)
											 * tabela.getValueAt(cod, 0)); try {
											 * foto = (String)
											 * tabela.getValueAt(cod, 9);
											 * fotoEtp1Label.setIcon(new
											 * ImageIcon(ImageIO .read(new
											 * File(foto)))); } catch
											 * (IOException ex) {
											 * 
											 * }
											 * etp1buttonGroup.clearSelection();
											 * op = (int) tabela.getValueAt(cod,
											 * 10);
											 * simIndEtp1RadioButton.setSelected
											 * (op > 0);
											 * naoIndEtp1RadioButton.setSelected
											 * (op == 0); codEtp1 = (String)
											 * tabela.getValueAt(cod, 14);
											 * break; case 2: tabela =
											 * (DefaultTableModel)
											 * etp2Table.getModel();
											 * nomeEtp2TextField
											 * .setText((String)
											 * tabela.getValueAt(cod, 0)); try {
											 * foto = (String)
											 * tabela.getValueAt(cod, 9);
											 * fotoEtp2Label.setIcon(new
											 * ImageIcon(ImageIO .read(new
											 * File(foto)))); } catch
											 * (IOException ex) {
											 * 
											 * }
											 * etp2buttonGroup.clearSelection();
											 * op = (int) tabela.getValueAt(cod,
											 * 11);
											 * simIndEtp2RadioButton.setSelected
											 * (op > 0);
											 * naoIndEtp2RadioButton.setSelected
											 * (op == 0); codEtp2 = (String)
											 * tabela.getValueAt(cod, 14);
											 * break; case 3: tabela =
											 * (DefaultTableModel)
											 * etp3Table.getModel();
											 * nomeEtp3TextField
											 * .setText((String)
											 * tabela.getValueAt(cod, 0)); try {
											 * foto = (String)
											 * tabela.getValueAt(cod, 9);
											 * fotoEtp3Label.setIcon(new
											 * ImageIcon(ImageIO .read(new
											 * File(foto)))); } catch
											 * (IOException ex) {
											 * 
											 * }
											 * etp3buttonGroup.clearSelection();
											 * op = (int) tabela.getValueAt(cod,
											 * 12);
											 * simIndEtp3RadioButton.setSelected
											 * (op > 0);
											 * naoIndEtp3RadioButton.setSelected
											 * (op == 0); codEtp3 = (String)
											 * tabela.getValueAt(cod, 14);
											 * break; case 4: tabela =
											 * (DefaultTableModel)
											 * etp4Table.getModel();
											 * nomeEtp4TextField
											 * .setText((String)
											 * tabela.getValueAt(cod, 0)); try {
											 * foto = (String)
											 * tabela.getValueAt(cod, 9);
											 * fotoEtp4Label.setIcon(new
											 * ImageIcon(ImageIO .read(new
											 * File(foto)))); } catch
											 * (IOException ex) {
											 * 
											 * }
											 * etp4buttonGroup.clearSelection();
											 * op = (int) tabela.getValueAt(cod,
											 * 13);
											 * simIndEtp4RadioButton.setSelected
											 * (op > 0);
											 * naoIndEtp4RadioButton.setSelected
											 * (op == 0); codEtp4 = (String)
											 * tabela.getValueAt(cod, 14);
											 * break; default: tabela =
											 * (DefaultTableModel)
											 * candTable.getModel();
											 * nomeTextField.setText((String)
											 * tabela.getValueAt(cod, 0));
											 * celFormattedTextField
											 * .setText((String)
											 * tabela.getValueAt(cod, 1));
											 * foneFormattedTextField
											 * .setText((String)
											 * tabela.getValueAt(cod, 2));
											 * endTextField.setText((String)
											 * tabela.getValueAt(cod, 3));
											 * cepFormattedTextField
											 * .setText((String)
											 * tabela.getValueAt(cod, 4));
											 * estTextField.setText((String)
											 * tabela.getValueAt(cod, 5));
											 * cidTextField.setText((String)
											 * tabela.getValueAt(cod, 6));
											 * 
											 * estCivilTextField.setText((String)
											 * tabela.getValueAt(cod, 7));
											 * sexoTextField.setText((String)
											 * tabela.getValueAt(cod, 8)); try {
											 * foto = (String)
											 * tabela.getValueAt(cod, 9);
											 * fotoLabel.setIcon(new
											 * ImageIcon(ImageIO.read(new
											 * File(foto)))); } catch
											 * (IOException ex) {
											 * 
											 * } break; }
											 */
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void carregarTabela(ResultSet candidato) {/*
													 * try { Vector<String>
													 * tableHeaders = new
													 * Vector<String>();
													 * Vector[] tableData = new
													 * Vector[] { new Vector(),
													 * new Vector(), new
													 * Vector(), new Vector(),
													 * new Vector() }; SQL sql =
													 * new SQL(); int code = -1;
													 * ResultSet rs;
													 * tableHeaders.add("Nome");
													 * tableHeaders
													 * .add("Celular");
													 * tableHeaders
													 * .add("Telefone");
													 * tableHeaders
													 * .add("Endereço");
													 * tableHeaders.add("CEP");
													 * tableHeaders
													 * .add("Estado");
													 * tableHeaders
													 * .add("Cidade");
													 * tableHeaders
													 * .add("Civil");
													 * tableHeaders.add("Sexo");
													 * tableHeaders.add("Foto");
													 * tableHeaders
													 * .add("Etapa 1");
													 * tableHeaders
													 * .add("Etapa 2");
													 * tableHeaders
													 * .add("Etapa 3");
													 * tableHeaders
													 * .add("Etapa 4");
													 * tableHeaders
													 * .add("Codigo"); while
													 * (candidato.next()) {
													 * Vector<Object> oneRow =
													 * new Vector<Object>();
													 * oneRow
													 * .add(candidato.getString
													 * ("nome"));
													 * oneRow.add(candidato
													 * .getString("celular"));
													 * oneRow
													 * .add(candidato.getString
													 * ("fone"));
													 * oneRow.add(candidato
													 * .getString("endereco"));
													 * oneRow
													 * .add(candidato.getString
													 * ("cep")); code =
													 * candidato
													 * .getInt("estado"); if
													 * (code > 0) { rs =
													 * sql.query(
													 * "Select `state`, `uf` from `"
													 * + util.getNameBD() +
													 * "`.`estado` Where (`active`='1') and (`idestado`='"
													 * + code + "');");
													 * rs.next();
													 * oneRow.add(rs.getString
													 * ("uf") + " - " +
													 * rs.getString("state")); }
													 * else { oneRow.add(""); }
													 * code =
													 * candidato.getInt("cidade"
													 * ); if (code > 0) { rs =
													 * sql
													 * .query("Select `city` from `"
													 * + util.getNameBD() +
													 * "`.`cidade` Where (`active`='1') and (`idcidade`='"
													 * + code + "');");
													 * rs.next();
													 * oneRow.add(rs.getString
													 * ("city")); } else {
													 * oneRow.add(""); } code =
													 * candidato
													 * .getInt("estcivil"); if
													 * (code > 0) { rs =
													 * sql.query
													 * ("Select `civil` from `"
													 * + util.getNameBD() +
													 * "`.`est_civil` Where (`active`='1') and (`idecivil`='"
													 * + code + "');");
													 * rs.next();
													 * oneRow.add(rs.getString
													 * ("civil")); } else {
													 * oneRow.add(""); } code =
													 * candidato.getInt("sexo");
													 * if (code > 0) { rs =
													 * sql.query
													 * ("Select `sexo` from `" +
													 * util.getNameBD() +
													 * "`.`sexo` Where (`active`='1') and (`idsexo`='"
													 * + code + "');");
													 * rs.next();
													 * oneRow.add(rs.getString
													 * ("sexo")); } else {
													 * oneRow.add(""); }
													 * oneRow.add
													 * (candidato.getString
													 * ("foto"));
													 * oneRow.add(candidato
													 * .getInt("optetapaum"));
													 * oneRow
													 * .add(candidato.getInt
													 * ("optetapadois"));
													 * oneRow.
													 * add(candidato.getInt
													 * ("optetapatres"));
													 * oneRow.
													 * add(candidato.getInt
													 * ("optetapaquatro"));
													 * oneRow
													 * .add(candidato.getString
													 * ("idcandidato"));
													 * tableData[0].add(oneRow);
													 * tableData[1].add(oneRow);
													 * if (candidato.getInt(
													 * "optetapaum") > 0) {
													 * tableData[2].add(oneRow);
													 * } if (candidato.getInt(
													 * "optetapadois") > 0) {
													 * tableData[3].add(oneRow);
													 * } if (candidato.getInt(
													 * "optetapatres") > 0) {
													 * tableData[4].add(oneRow);
													 * } } candTable
													 * .setModel(new
													 * DefaultTableModel
													 * (tableData[0],
													 * tableHeaders)); etp1Table
													 * .setModel(new
													 * DefaultTableModel
													 * (tableData[1],
													 * tableHeaders)); etp2Table
													 * .setModel(new
													 * DefaultTableModel
													 * (tableData[2],
													 * tableHeaders)); etp3Table
													 * .setModel(new
													 * DefaultTableModel
													 * (tableData[3],
													 * tableHeaders)); etp4Table
													 * .setModel(new
													 * DefaultTableModel
													 * (tableData[4],
													 * tableHeaders));
													 * candTable.
													 * getColumnModel()
													 * .getColumn
													 * (0).setPreferredWidth
													 * (200);
													 * candTable.getColumnModel
													 * ().getColumn(1).
													 * setPreferredWidth(150);
													 * candTable
													 * .getColumnModel()
													 * .getColumn
													 * (2).setPreferredWidth
													 * (150);
													 * candTable.getColumnModel
													 * ().getColumn(3).
													 * setPreferredWidth(350);
													 * candTable
													 * .getColumnModel()
													 * .getColumn
													 * (4).setPreferredWidth
													 * (100);
													 * util.ocultarColunas
													 * (candTable, 5, 14);
													 * etp1Table
													 * .getColumnModel()
													 * .getColumn
													 * (0).setPreferredWidth
													 * (200);
													 * etp1Table.getColumnModel
													 * ().getColumn(1).
													 * setPreferredWidth(150);
													 * etp1Table
													 * .getColumnModel()
													 * .getColumn
													 * (2).setPreferredWidth
													 * (150);
													 * etp1Table.getColumnModel
													 * ().getColumn(3).
													 * setPreferredWidth(350);
													 * etp1Table
													 * .getColumnModel()
													 * .getColumn
													 * (4).setPreferredWidth
													 * (100);
													 * util.ocultarColunas
													 * (etp1Table, 5, 14);
													 * etp2Table
													 * .getColumnModel()
													 * .getColumn
													 * (0).setPreferredWidth
													 * (200);
													 * etp2Table.getColumnModel
													 * ().getColumn(1).
													 * setPreferredWidth(150);
													 * etp2Table
													 * .getColumnModel()
													 * .getColumn
													 * (2).setPreferredWidth
													 * (150);
													 * etp2Table.getColumnModel
													 * ().getColumn(3).
													 * setPreferredWidth(350);
													 * etp2Table
													 * .getColumnModel()
													 * .getColumn
													 * (4).setPreferredWidth
													 * (100);
													 * util.ocultarColunas
													 * (etp2Table, 5, 14);
													 * etp3Table
													 * .getColumnModel()
													 * .getColumn
													 * (0).setPreferredWidth
													 * (200);
													 * etp3Table.getColumnModel
													 * ().getColumn(1).
													 * setPreferredWidth(150);
													 * etp3Table
													 * .getColumnModel()
													 * .getColumn
													 * (2).setPreferredWidth
													 * (150);
													 * etp3Table.getColumnModel
													 * ().getColumn(3).
													 * setPreferredWidth(350);
													 * etp3Table
													 * .getColumnModel()
													 * .getColumn
													 * (4).setPreferredWidth
													 * (100);
													 * util.ocultarColunas
													 * (etp3Table, 5, 14);
													 * etp4Table
													 * .getColumnModel()
													 * .getColumn
													 * (0).setPreferredWidth
													 * (200);
													 * etp4Table.getColumnModel
													 * ().getColumn(1).
													 * setPreferredWidth(150);
													 * etp4Table
													 * .getColumnModel()
													 * .getColumn
													 * (2).setPreferredWidth
													 * (150);
													 * etp4Table.getColumnModel
													 * ().getColumn(3).
													 * setPreferredWidth(350);
													 * etp4Table
													 * .getColumnModel()
													 * .getColumn
													 * (4).setPreferredWidth
													 * (100);
													 * util.ocultarColunas
													 * (etp4Table, 5, 14);
													 * candidato.close(); }
													 * catch (SQLException ex) {
													 * Logger
													 * .getLogger(this.getName
													 * ()).log(Level.SEVERE,
													 * null, ex); }
													 */
	}

	@SuppressWarnings("unchecked")
	private void clearLaudo() {/*
								 * try { fotoEtp1Label.setIcon(new
								 * ImageIcon(getClass().getResource(
								 * "/img/nobody.png")));
								 * fotoEtp2Label.setIcon(new
								 * ImageIcon(getClass().getResource(
								 * "/img/nobody.png")));
								 * fotoEtp3Label.setIcon(new
								 * ImageIcon(getClass().getResource(
								 * "/img/nobody.png")));
								 * fotoEtp4Label.setIcon(new
								 * ImageIcon(getClass().getResource(
								 * "/img/nobody.png")));
								 * etp1buttonGroup.clearSelection();
								 * etp2buttonGroup.clearSelection();
								 * etp3buttonGroup.clearSelection();
								 * etp4buttonGroup.clearSelection();
								 * nomeEtp1TextField.setText("");
								 * nomeEtp2TextField.setText("");
								 * nomeEtp3TextField.setText("");
								 * nomeEtp4TextField.setText("");
								 * laudoEtp1TextArea.setText("");
								 * laudoEtp2TextArea.setText("");
								 * laudoEtp3TextArea.setText("");
								 * laudoEtp4TextArea.setText("");
								 * psiEtp1List.clear(); psiEtp2List.clear();
								 * psiEtp3List.clear(); psiEtp4List.clear();
								 * dimEtp1List.clear(); dimEtp2List.clear();
								 * dimEtp3List.clear(); dimEtp4List.clear(); SQL
								 * sql = new SQL(); String str = ""; ResultSet
								 * rs = sql.carregarComboBox("testes_ad",
								 * "teste", "idtestead"); while (rs.next()) {
								 * psiEtp1List.add(str); psiEtp2List.add(str);
								 * psiEtp3List.add(str); psiEtp4List.add(str); }
								 * psiEtp1ComboBox
								 * .setModel(util.initComboBox("testes_ad", new
								 * String[] { "teste" }, "idtestead"));
								 * psiEtp2ComboBox
								 * .setModel(util.initComboBox("testes_ad", new
								 * String[] { "teste" }, "idtestead"));
								 * psiEtp3ComboBox
								 * .setModel(util.initComboBox("testes_ad", new
								 * String[] { "teste" }, "idtestead"));
								 * psiEtp4ComboBox
								 * .setModel(util.initComboBox("testes_ad", new
								 * String[] { "teste" }, "idtestead"));
								 * psiEtp1ComboBox.setSelectedIndex(-1);
								 * psiEtp2ComboBox.setSelectedIndex(-1);
								 * psiEtp3ComboBox.setSelectedIndex(-1);
								 * psiEtp4ComboBox.setSelectedIndex(-1);
								 * 
								 * rs = sql.carregarComboBox("dinamicas",
								 * "dinamica", "iddinam"); while (rs.next()) {
								 * str = rs.getString("dinamica");
								 * dimEtp1List.add(str); dimEtp2List.add(str);
								 * dimEtp3List.add(str); dimEtp4List.add(str); }
								 * dimEtp1ComboBox
								 * .setModel(util.initComboBox("dinamicas", new
								 * String[] { "dinamica" }, "iddinam"));
								 * dimEtp2ComboBox
								 * .setModel(util.initComboBox("dinamicas", new
								 * String[] { "dinamica" }, "iddinam"));
								 * dimEtp3ComboBox
								 * .setModel(util.initComboBox("dinamicas", new
								 * String[] { "dinamica" }, "iddinam"));
								 * dimEtp4ComboBox
								 * .setModel(util.initComboBox("dinamicas", new
								 * String[] { "dinamica" }, "iddinam"));
								 * dimEtp1ComboBox.setSelectedIndex(-1);
								 * dimEtp2ComboBox.setSelectedIndex(-1);
								 * dimEtp3ComboBox.setSelectedIndex(-1);
								 * dimEtp4ComboBox.setSelectedIndex(-1);
								 * rs.close(); sql.close(); } catch
								 * (SQLException ex) {
								 * Logger.getLogger(this.getName
								 * ()).log(Level.SEVERE, null, ex); }
								 */
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

	private final Util util = new Util();
	private Laudo[] etp;
	private JScrollPane areaScrollPane;
	private JScrollPane escScrollPane;
	private JScrollPane expScrollPane;
	private JScrollPane dispScrollPane;
	private JScrollPane outroScrollPane;
	private JScrollPane editorScrollPane;
	private JEditorPane editorPane;
	private JTabbedPane tabbedPane;
	private JLabel lblreas;
	private JLabel lblEscolaridade;
	private JLabel lblExperincia;
	private JLabel lblDisponibilidade;
	private JLabel lblOutro_1;
	private JLabel lblOutro_2;
	private JPanel selectPanel;
	private JPanel fotoPanel;
	private JLabel fotoLabel;
	private JLabel lblNome;
	private JTextField nomeTextField;
	private JLabel lblSexo;
	private JLabel lblEndereo;
	private JTextField endTextField;
	private JLabel lblCep;
	private JFormattedTextField cepFormattedTextField;
	private JLabel lblEstado;
	private JLabel lblCidade;
	private JTextField cidTextField;
	private JLabel lblCelular;
	private JFormattedTextField celFormattedTextField;
	private JLabel lblOutro;
	private JFormattedTextField foneFormattedTextField;
	private JLabel lblEstadoCivil;
	private JScrollPane candScrollPane;
	private TTable candTable;
	private JTextField estTextField;
	private JTextField estCivilTextField;
	private JTextField sexoTextField;
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
}
