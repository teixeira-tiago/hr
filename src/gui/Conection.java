/**
 * 
 */
package gui;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import utiliter.Util;

/**
 * @author Tiago A. Teixeira {@link tiago.a.teixeira@acm.org}
 *
 */
@SuppressWarnings("serial")
public class Conection extends JDialog {

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					Conection dialog = new Conection();
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
	public Conection() {
		initComponents();
		load();
	}

	private void initComponents() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			util.setUIFont(util.getFont());
		} catch (UnsupportedLookAndFeelException | InstantiationException
				| IllegalAccessException | ClassNotFoundException ex) {
			Logger.getLogger(this.getName()).log(Level.SEVERE, null, ex);
		}
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setBounds(0, 0, 370, 200);
		getContentPane().setLayout(null);
		setModal(true);
		setAlwaysOnTop(true);
		setLocationRelativeTo(null);
		setResizable(false);
		setTitle("Parametros de Conec\u00E7\u00E3o");

		lblInformeAString = new JLabel("Informe a string de conexão do banco:");
		lblInformeAString.setBounds(5, 11, 300, 16);
		getContentPane().add(lblInformeAString);

		lblServidor = new JLabel("Servidor:");
		lblServidor.setHorizontalAlignment(SwingConstants.RIGHT);
		lblServidor.setBounds(35, 41, 60, 16);
		getContentPane().add(lblServidor);

		servTextField = new JTextField();
		servTextField.setBounds(100, 35, 120, 24);
		getContentPane().add(servTextField);
		servTextField.setColumns(10);

		lblPorta = new JLabel("Porta:");
		lblPorta.setHorizontalAlignment(SwingConstants.RIGHT);
		lblPorta.setBounds(235, 41, 40, 16);
		getContentPane().add(lblPorta);

		baseTextField = new JTextField();
		baseTextField.setBounds(100, 65, 120, 24);
		getContentPane().add(baseTextField);
		baseTextField.setColumns(10);

		lblBaseDeDados = new JLabel("Base de dados:");
		lblBaseDeDados.setHorizontalAlignment(SwingConstants.RIGHT);
		lblBaseDeDados.setBounds(5, 71, 90, 16);
		getContentPane().add(lblBaseDeDados);

		portaTextField = new JTextField();
		portaTextField.setBounds(280, 36, 80, 24);
		getContentPane().add(portaTextField);
		portaTextField.setColumns(10);

		lblUsurio = new JLabel("Usu\u00E1rio:");
		lblUsurio.setHorizontalAlignment(SwingConstants.RIGHT);
		lblUsurio.setBounds(220, 71, 55, 16);
		getContentPane().add(lblUsurio);

		userTextField = new JTextField();
		userTextField.setBounds(280, 65, 80, 24);
		getContentPane().add(userTextField);
		userTextField.setColumns(10);

		lblSenha = new JLabel("Senha:");
		lblSenha.setHorizontalAlignment(SwingConstants.RIGHT);
		lblSenha.setBounds(225, 101, 50, 16);
		getContentPane().add(lblSenha);

		pwsPasswordField = new JPasswordField();
		pwsPasswordField.setBounds(280, 95, 80, 24);
		getContentPane().add(pwsPasswordField);

		panel = new JPanel();
		panel.setBounds(5, 125, 355, 40);
		getContentPane().add(panel);

		btnSalvar = new JButton("");
		btnSalvar.setPreferredSize(new java.awt.Dimension(30, 30));
		panel.add(btnSalvar);
		btnSalvar.setIcon(new ImageIcon(getClass().getResource("/img/ok.png")));

		btnCancelar = new JButton("");
		btnCancelar.setPreferredSize(new java.awt.Dimension(30, 30));
		panel.add(btnCancelar);
		btnCancelar.setIcon(new ImageIcon(getClass().getResource(
				"/img/cancel.png")));

		lblNomeConex = new JLabel("Nome Conexão:");
		lblNomeConex.setHorizontalAlignment(SwingConstants.RIGHT);
		lblNomeConex.setBounds(5, 101, 92, 16);
		getContentPane().add(lblNomeConex);

		textField = new JTextField();
		textField.setText((String) null);
		textField.setColumns(10);
		textField.setBounds(100, 95, 120, 24);
		getContentPane().add(textField);
		btnCancelar.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		btnSalvar.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				btnSalvarActionPerformed(e);
			}
		});
	}

	private void btnSalvarActionPerformed(ActionEvent e) {
		String file = "config/bd.properties";
		util.setDrvBD("jdbc:mysql://");
		util.setNameBD(baseTextField.getText());
		util.setPrtBD(portaTextField.getText());
		if (pwsPasswordField.getPassword().length > 0) {
			util.setPwsBD(new String(pwsPasswordField.getPassword()));
		}
		util.setSrvBD(servTextField.getText());
		util.setUserBD(userTextField.getText());
		util.setUrlBD(util.getDrvBD() + util.getSrvBD() + ":" + util.getPrtBD()
				+ "/");
		String prop = "bd.url&" + util.getUrlBD();
		prop += "#bd.drive&" + util.getDrvBD();
		prop += "#bd.server&" + util.getSrvBD();
		prop += "#bd.port&" + util.getPrtBD();
		prop += "#bd.name&" + util.getNameBD();
		prop += "#bd.usr&" + util.getUserBD();
		prop += "#bd.pws&" + util.getPwsBD();
		util.limparArquivo(file);
		util.setProperties(file, prop);
		dispose();
	}

	private void load() {
		servTextField.setText(util.getSrvBD());
		baseTextField.setText(util.getNameBD());
		portaTextField.setText(util.getPrtBD());
		userTextField.setText(util.getUserBD());
	}

	private final Util util = new Util();
	private JLabel lblInformeAString;
	private JLabel lblServidor;
	private JLabel lblPorta;
	private JLabel lblUsurio;
	private JLabel lblBaseDeDados;
	private JLabel lblSenha;
	private JLabel lblNomeConex;
	private JPasswordField pwsPasswordField;
	private JTextField servTextField;
	private JTextField baseTextField;
	private JTextField portaTextField;
	private JTextField userTextField;
	private JTextField textField;
	private JPanel panel;
	private JButton btnSalvar;
	private JButton btnCancelar;
}
