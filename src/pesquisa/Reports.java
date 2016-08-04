/**
 * 
 */
package pesquisa;

import java.awt.Desktop;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.JSeparator;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.MaskFormatter;

import net.miginfocom.swing.MigLayout;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import utiliter.Util;
import utiliter.component.AComboBox;

/**
 * @author Tiago A. Teixeira {@link tiago.a.teixeira@acm.org}
 * 
 */
@SuppressWarnings("serial")
public class Reports extends JDialog {

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					Reports window = new Reports();
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
	public Reports() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	@SuppressWarnings({ "unchecked" })
	private void initialize() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			util.setUIFont(util.getFont());
		} catch (UnsupportedLookAndFeelException | InstantiationException
				| IllegalAccessException | ClassNotFoundException ex) {
			Logger.getLogger(this.getName()).log(Level.SEVERE, null, ex);
		}
		setIconImage(new ImageIcon(getClass().getResource("/img/favicon.png"))
				.getImage());
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setTitle("Gerador de Relatórios");
		setBounds(0, 0, 300, 250);
		setModal(true);
		setAlwaysOnTop(true);
		setLocationRelativeTo(null);
		setResizable(false);

		anoButtonGroup = new ButtonGroup();
		setorButtonGroup = new ButtonGroup();
		graphButtonGroup = new ButtonGroup();
		percButtonGroup = new ButtonGroup();

		rdbtnTodosOsSetores = new JRadioButton("Todos os setores");
		rdbtnTodosOsSetores.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				rdbtnTodosOsSetoresAtionPerformed(e);
			}
		});
		getContentPane()
				.setLayout(
						new MigLayout(
								"",
								"5[68px]0[13px]0[5px]0[35px]0[40px,grow]5",
								"5[24px]0[24px]0[6px]0[24px]0[6px]0[30px]0[6px]0[24px]0[6px]0[24px]0[6px]0[30px]5"));
		rdbtnTodosOsSetores.setActionCommand("0");
		rdbtnTodosOsSetores.setSelected(true);
		setorButtonGroup.add(rdbtnTodosOsSetores);
		getContentPane().add(rdbtnTodosOsSetores, "cell 0 0 4 1,grow");

		rdbtnSetor = new JRadioButton("Setor:");
		rdbtnSetor.setActionCommand("1");
		setorButtonGroup.add(rdbtnSetor);
		getContentPane().add(rdbtnSetor, "cell 0 1,grow");

		setorComboBox = new AComboBox();
		setorComboBox.setModel(util.initComboBox("departamento",
				new String[] { "depto" }, "iddepto"));
		setorCombo = util.initCombo("departamento", "iddepto");
		setorComboBox.setSelectedIndex(-1);
		getContentPane().add(setorComboBox, "cell 1 1 4 1,growx,aligny center");

		rdbtnTodosOsPeriodos = new JRadioButton("Todos os periodos");
		rdbtnTodosOsPeriodos.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				rdbtnTodosOsPeriodosAtionPerformed(e);
			}
		});
		rdbtnTodosOsPeriodos.setActionCommand("0");
		rdbtnTodosOsPeriodos.setSelected(true);
		anoButtonGroup.add(rdbtnTodosOsPeriodos);
		getContentPane().add(rdbtnTodosOsPeriodos, "flowx,cell 0 3 3 1,grow");

		separator = new JSeparator();
		getContentPane().add(separator, "cell 0 2 5 1,growx,aligny top");

		rdbtnAno = new JRadioButton("Ano:");
		rdbtnAno.setActionCommand("1");
		anoButtonGroup.add(rdbtnAno);
		getContentPane().add(rdbtnAno, "cell 3 3,grow");

		anoFormattedTextField = new JFormattedTextField();
		try {
			anoFormattedTextField
					.setFormatterFactory(new DefaultFormatterFactory(
							new MaskFormatter("####")));
		} catch (ParseException e4) {
			e4.printStackTrace();
		}
		getContentPane().add(anoFormattedTextField,
				"cell 4 3,growx,aligny center");

		separator_1 = new JSeparator();
		getContentPane().add(separator_1, "cell 0 4 5 1,growx,aligny top");

		btnVisualizar = new JButton("Visualizar");
		btnVisualizar.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				btnVisualizarActionPerformed(e);
			}
		});
		getContentPane().add(btnVisualizar,
				"cell 0 11 5 1,alignx center,aligny center");

		lblExibir = new JLabel("Inserir grafico?");
		getContentPane().add(lblExibir, "cell 0 9 2 1,grow");

		rdbtnGraphNao = new JRadioButton("Não");
		rdbtnGraphNao.setActionCommand("0");
		rdbtnGraphNao.setSelected(true);
		graphButtonGroup.add(rdbtnGraphNao);
		getContentPane().add(rdbtnGraphNao, "cell 2 9,alignx right,growy");

		rdbtnGraphSim = new JRadioButton("Sim");
		rdbtnGraphSim.setActionCommand("1");
		graphButtonGroup.add(rdbtnGraphSim);
		getContentPane().add(rdbtnGraphSim, "cell 3 9,alignx right,growy");

		lblQuestionario = new JLabel("Questionario:");
		getContentPane().add(lblQuestionario,
				"cell 0 5 2 1,alignx center,aligny center");

		questComboBox = new AComboBox();
		questComboBox.setModel(util.initComboBox("questionario",
				new String[] { "nome" }, "idquest"));
		questCombo = util.initCombo("questionario", "idquest");
		questComboBox.setSelectedIndex(-1);
		getContentPane().add(questComboBox, "cell 2 5 3 1,growx,aligny center");

		separator_2 = new JSeparator();
		getContentPane().add(separator_2, "cell 0 6 5 1,growx,aligny top");

		lblExibirValores = new JLabel("Exibir valores:");
		getContentPane().add(lblExibirValores, "cell 0 7 2 1,grow");

		rdbtnPercentuais = new JRadioButton("Percentuais");
		rdbtnPercentuais.setActionCommand("1");
		rdbtnPercentuais.setSelected(true);
		percButtonGroup.add(rdbtnPercentuais);
		getContentPane().add(rdbtnPercentuais, "cell 2 7 2 1,grow");

		rdbtnTotais = new JRadioButton("Totais");
		rdbtnTotais.setActionCommand("0");
		percButtonGroup.add(rdbtnTotais);
		getContentPane().add(rdbtnTotais, "cell 4 7,alignx left,growy");

		separator_3 = new JSeparator();
		getContentPane().add(separator_3, "cell 0 8 5 1,growx,aligny top");

		JSeparator separator_4 = new JSeparator();
		getContentPane().add(separator_4, "cell 0 10 5 1,growx,aligny top");
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void btnVisualizarActionPerformed(ActionEvent e) {
		setAlwaysOnTop(false);
		String opt = "", subTitulo = "";
		boolean ano = false;
		if (rdbtnSetor.isSelected()) {
			if (setorComboBox.getSelectedIndex() > -1) {
				opt += "and (d.`iddepto`='"
						+ setorCombo.get(setorComboBox.getSelectedIndex())
						+ "') ";
				subTitulo += "Setor: " + setorComboBox.getSelectedItem() + " ";
			} else {
				JOptionPane.showMessageDialog(null, "Favor selecione um setor");
				return;
			}
		}
		if (rdbtnAno.isSelected()) {
			if (anoFormattedTextField.getText().trim().isEmpty()) {
				JOptionPane.showMessageDialog(null,
						"Favor indique um ano valido");
				return;
			} else {
				opt += "and (year(p.`data`)='"
						+ anoFormattedTextField.getText() + "') ";
				ano = true;
				subTitulo += "Ano: " + anoFormattedTextField.getText() + " ";
			}
		}
		if (questComboBox.getSelectedIndex() < 0) {
			JOptionPane.showMessageDialog(null,
					"Favor selecione um questionario");
			return;
		}
		try {
			JasperDesign desenho = JRXmlLoader
					.load("relatorios/pesquisa/Pesquisa.jrxml");
			opt += "and (p.`questionario`='"
					+ questCombo.get(questComboBox.getSelectedIndex()) + "') ";
			JasperReport relatorio = JasperCompileManager
					.compileReport(desenho);
			RelatorioPesquisa pesquisa = new RelatorioPesquisa();
			JRBeanCollectionDataSource jrRS = new JRBeanCollectionDataSource(
					pesquisa.getPesquisa(opt, Integer.parseInt(percButtonGroup
							.getSelection().getActionCommand())), false);
			Map parametros = new HashMap();
			parametros.put("logo", util.getConf("logo"));
			parametros.put("subTitulo", subTitulo);

			if (graphButtonGroup.getSelection().getActionCommand().equals("1")) {
				parametros.put("subRelatorio",
						"relatorios/pesquisa/Grafico.jasper");
				if (ano) {
					parametros.put("Consulta", new JRBeanCollectionDataSource(
							pesquisa.getPie3D(opt)));
				} else {
					parametros.put("Consulta", new JRBeanCollectionDataSource(
							pesquisa.getBarChart3D(opt)));
				}
			}/*
			 * JasperViewer jrviewer = new JasperViewer(impressao, false);
			 * jrviewer.setVisible(true); jrviewer.toFront();
			 */

			JasperPrint impressao = JasperFillManager.fillReport(relatorio,
					parametros, jrRS);
			JasperExportManager.exportReportToPdfFile(impressao,
					"relatorios/Relatorio.pdf");
			File file = new File("relatorios/Relatorio.pdf");
			Desktop.getDesktop().open(file);
		} catch (JRException | IOException ex) {
			Logger.getLogger(this.getName()).log(Level.SEVERE, null, ex);
		}
	}

	private void rdbtnTodosOsSetoresAtionPerformed(ActionEvent e) {
		if (rdbtnTodosOsSetores.isSelected()) {
			setorComboBox.setSelectedIndex(-1);
		}
	}

	private void rdbtnTodosOsPeriodosAtionPerformed(ActionEvent e) {
		if (rdbtnTodosOsPeriodos.isSelected()) {
			anoFormattedTextField.setText("");
		}
	}

	private final Util util = new Util();
	private HashMap<Integer, Integer> setorCombo;
	private HashMap<Integer, Integer> questCombo;
	private JRadioButton rdbtnTodosOsSetores;
	private JRadioButton rdbtnTodosOsPeriodos;
	private JRadioButton rdbtnSetor;
	private JRadioButton rdbtnAno;
	private JRadioButton rdbtnGraphNao;
	private JRadioButton rdbtnGraphSim;
	private JRadioButton rdbtnPercentuais;
	private JRadioButton rdbtnTotais;
	private ButtonGroup setorButtonGroup;
	private ButtonGroup graphButtonGroup;
	private ButtonGroup anoButtonGroup;
	private ButtonGroup percButtonGroup;
	private AComboBox setorComboBox;
	private AComboBox questComboBox;
	private JSeparator separator;
	private JSeparator separator_1;
	private JSeparator separator_2;
	private JSeparator separator_3;
	private JFormattedTextField anoFormattedTextField;
	private JButton btnVisualizar;
	private JLabel lblExibir;
	private JLabel lblQuestionario;
	private JLabel lblExibirValores;
}
