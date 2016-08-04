/**
 * 
 */
package recrutamento;

import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JRadioButton;
import javax.swing.JSeparator;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRResultSetDataSource;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import utiliter.SQL;
import utiliter.Util;

/**
 * @author Tiago A. Teixeira {@link tiago.a.teixeira@acm.org}
 * 
 */
@SuppressWarnings("serial")
public class OptionLaudo extends JDialog {

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			OptionLaudo dialog = new OptionLaudo();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public OptionLaudo() {
		initComponents();
	}

	private void initComponents() {
		Util util = new Util();
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			util.setUIFont(util.getFont());
		} catch (UnsupportedLookAndFeelException | InstantiationException
				| IllegalAccessException | ClassNotFoundException ex) {
			Logger.getLogger(this.getName()).log(Level.SEVERE, null, ex);
		}
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setBounds(0, 0, 250, 210);
		setModal(true);
		setAlwaysOnTop(true);
		setLocationRelativeTo(null);
		setResizable(false);
		getContentPane().setLayout(null);

		candButtonGroup = new ButtonGroup();
		etpButtonGroup = new ButtonGroup();

		allCandRadioButton = new JRadioButton("Todos os Candidatos");
		allCandRadioButton.setSelected(true);
		allCandRadioButton.setActionCommand("0");
		allCandRadioButton.setBounds(5, 35, 165, 23);
		getContentPane().add(allCandRadioButton);
		candButtonGroup.add(allCandRadioButton);

		oneCandRadioButton = new JRadioButton("Somente o Candidato selecionado");
		oneCandRadioButton.setActionCommand("1");
		oneCandRadioButton.setBounds(5, 5, 245, 23);
		getContentPane().add(oneCandRadioButton);
		candButtonGroup.add(oneCandRadioButton);

		oneEtpRadioButton = new JRadioButton("Somente a Etapa selecionada");
		oneEtpRadioButton.setBounds(5, 75, 213, 23);
		oneEtpRadioButton.setActionCommand("1");
		getContentPane().add(oneEtpRadioButton);
		etpButtonGroup.add(oneEtpRadioButton);

		allEtpRadioButton = new JRadioButton("Todas as Etapas");
		allEtpRadioButton.setSelected(true);
		allEtpRadioButton.setBounds(5, 105, 141, 23);
		allEtpRadioButton.setActionCommand("0");
		getContentPane().add(allEtpRadioButton);
		etpButtonGroup.add(allEtpRadioButton);

		separator = new JSeparator();
		separator.setBounds(5, 65, 235, 12);
		getContentPane().add(separator);

		separator_1 = new JSeparator();
		separator_1.setBounds(5, 135, 235, 12);
		getContentPane().add(separator_1);

		btnVisualizar = new JButton("Visualizar");
		btnVisualizar.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				btnVisualizarActionPerformed(e);
			}
		});
		btnVisualizar.setBounds(72, 145, 100, 30);
		getContentPane().add(btnVisualizar);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void btnVisualizarActionPerformed(ActionEvent e) {
		String cand = candButtonGroup.getSelection().getActionCommand();
		String etp = etpButtonGroup.getSelection().getActionCommand();
		String sql = "";
		if (cand.contains("1")) {
			sql += "and (s.`candidato`='" + codCand + "') ";
		}
		if (etp.contains("1")) {
			sql += "and (s.`etapa`='" + codEtp + "') ";
		}
		try {
			// compila o relatório
			JasperReport relatorio = JasperCompileManager
					.compileReport(JRXmlLoader
							.load("relatorios/selecao/Laudo.jrxml"));

			// estabelece conexão
			SQL query = new SQL();
			ResultSet rs = query.carregarRelatorioLaudo(sql);

			// executa o relatório
			Map parametros = new HashMap();
			parametros.put("titulo", "Processo Seletivo");
			parametros.put("nome", nomeCand);
			parametros.put("logo", util.getConf("logo"));
			JasperPrint impressao = JasperFillManager.fillReport(relatorio,
					parametros, new JRResultSetDataSource(rs));

			// exibe o resultado
			JasperExportManager.exportReportToPdfFile(impressao,
					"relatorios/Relatorio.pdf");
			Desktop.getDesktop().open(new File("relatorios/Relatorio.pdf"));
			rs.close();
			query.close();
			dispose();
		} catch (JRException | IOException ex) {
			Logger.getLogger(this.getName()).log(Level.SEVERE, null, ex);
		} catch (SQLException ex) {
			Logger.getLogger(this.getName()).log(Level.SEVERE, null, ex);
		}
	}

	public void setNomeCand(String cand) {
		nomeCand = cand;
	}

	public void setCodCand(String cand) {
		codCand = cand;
	}

	public void setEtp(String etapa) {
		codEtp = etapa;
	}

	private final Util util = new Util();
	private String nomeCand = "";
	private String codCand = "";
	private String codEtp = "";
	private JButton btnVisualizar;
	private ButtonGroup candButtonGroup;
	private ButtonGroup etpButtonGroup;
	private JRadioButton allCandRadioButton;
	private JRadioButton oneCandRadioButton;
	private JRadioButton oneEtpRadioButton;
	private JRadioButton allEtpRadioButton;
	private JSeparator separator;
	private JSeparator separator_1;
}
