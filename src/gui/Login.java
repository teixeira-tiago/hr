package gui;

import static java.awt.GraphicsDevice.WindowTranslucency.TRANSLUCENT;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.ResultSet;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.WindowConstants;

import utiliter.Mover;
import utiliter.SQL;
import utiliter.Util;

/**
 * @author Tiago A. Teixeira {@link tiago.a.teixeira@acm.org}
 * 
 */
@SuppressWarnings("serial")
public class Login extends JFrame {

	private final Util util = new Util();

	/**
	 * Creates new form NewJFrame
	 */
	public Login() {
		setUndecorated(true);
		setBackground(new Color(0f, 0f, 0f, 0f));
		initComponents();
		setLocationRelativeTo(null);
		Mover.getInstance().registerComponent(this);
	}

	private void initComponents() {
		setIconImage(new ImageIcon(getClass().getResource("/img/favicon.png"))
				.getImage());
		GridBagConstraints gridBagConstraints;
		util.setUIFont(util.getFont());
		jPanel1 = new JPanel();
		jPanel2 = new JPanel();
		jPanel3 = new JPanel();
		jPanel2.setBounds(284, 336, 242, 119);
		jPanel3.setBounds(5, 79, 232, 40);
		userLabel = new JLabel();
		userLabel.setBounds(5, 12, 35, 16);
		userTextField = new JTextField();
		userTextField.setBounds(50, 6, 185, 28);
		pwsLabel = new JLabel();
		pwsLabel.setBounds(5, 52, 37, 16);
		pwsPasswordField = new JPasswordField();
		pwsPasswordField.setBounds(50, 46, 185, 28);
		logarButton = new JButton();
		logarButton.setBounds(53, 80, 79, 29);
		logarButton
				.setIcon(new ImageIcon(getClass().getResource("/img/ok.png")));
		cancelarButton = new JButton();
		cancelarButton.setBounds(138, 80, 98, 29);
		cancelarButton.setIcon(new ImageIcon(getClass().getResource(
				"/img/cancel.png")));
		logoLabel = new JLabel();
		getRootPane().setDefaultButton(logarButton);

		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setTitle("Login Rh");
		getContentPane().setLayout(new GridBagLayout());

		jPanel1.setOpaque(false);

		userLabel.setText("Login");

		pwsLabel.setText("Senha");

		logarButton.setText("Logar");
		logarButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent evt) {
				logarButtonActionPerformed(evt);
			}
		});

		cancelarButton.setText("Cancelar");
		cancelarButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent evt) {
				cancelarButtonActionPerformed(evt);
			}
		});
		userTextField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent evt) {
				userTextFieldKeyPressed(evt);
			}
		});
		jPanel1.setLayout(null);

		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.fill = GridBagConstraints.BOTH;
		getContentPane().add(jPanel1, gridBagConstraints);
		logoLabel.setIcon(new ImageIcon("./" + util.getConf("splash")));
		gbc_logoLabel = new GridBagConstraints();
		gbc_logoLabel.gridx = 1;
		gbc_logoLabel.gridy = 1;
		gbc_logoLabel.fill = GridBagConstraints.BOTH;
		getContentPane().add(logoLabel, gbc_logoLabel);

		jPanel2.setBackground(new Color(0f, 0f, 0f, 0f));
		jPanel3.setBackground(new Color(0f, 0f, 0f, 0f));
		jPanel1.add(jPanel2);
		jPanel2.setLayout(null);
		jPanel2.add(userLabel);
		jPanel2.add(userTextField);
		jPanel2.add(pwsLabel);
		jPanel3.add(logarButton);
		jPanel3.add(cancelarButton);
		jPanel2.add(jPanel3);
		jPanel2.add(pwsPasswordField);
		pack();
	}

	private void logarButtonActionPerformed(ActionEvent evt) {
		String user = userTextField.getText();
		String pws = new String(pwsPasswordField.getPassword());
		if (user.trim().isEmpty()) {
			user = JOptionPane.showInputDialog("Favor digite o seu login?");
		} else {
			if (pws.trim().isEmpty()) {
				pws = JOptionPane.showInputDialog("Favor digite a sua senha?");
			}
		}
		try {
			if (!user.trim().isEmpty() && !pws.trim().isEmpty()) {

				ResultSet result = new SQL().checarUsuario(user, pws);
				if (result.next()) {

					if (result.getInt("active") > 0) {
						String prop = "login.user&"
								+ result.getString("idusuario");
						prop += "#login.level&" + result.getString("nivel");
						util.setProperties("config/ultimo_login.properties",
								prop);
						new Main().setVisible(true);
						setVisible(false);
					} else {
						JOptionPane
								.showMessageDialog(null,
										"O usuário informado está inativo.\nFavor digitar um usuário ativo");
						userTextField.setText("");
						pwsPasswordField.setText("");
					}
				} else {
					user = "";
					pws = "";
					JOptionPane.showMessageDialog(null,
							"Usuário e/ou senha inválido");
					userTextField.setText("");
					pwsPasswordField.setText("");
					userTextField.requestFocus();
				}
				result.close();
			}
		} catch (Exception ex) {
			Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	private void cancelarButtonActionPerformed(ActionEvent evt) {
		System.exit(0);
	}

	private void userTextFieldKeyPressed(KeyEvent evt) {
		if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
			pwsPasswordField.requestFocus();
		}
	}

	/**
	 * @param args
	 *            the command line arguments
	 */
	public static void main(String args[]) {
		/* Set the Nimbus look and feel */
		// <editor-fold defaultstate="collapsed"
		// desc=" Look and feel setting code (optional) ">
		/*
		 * If Nimbus (introduced in Java SE 6) is not available, stay with the
		 * default look and feel. For details see
		 * http://download.oracle.com/javase
		 * /tutorial/uiswing/lookandfeel/plaf.html
		 */

		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (UnsupportedLookAndFeelException | InstantiationException
				| IllegalAccessException | ClassNotFoundException ex) {
			java.util.logging.Logger.getLogger(Login.class.getName()).log(
					java.util.logging.Level.SEVERE, null, ex);
		}

		GraphicsEnvironment ge = GraphicsEnvironment
				.getLocalGraphicsEnvironment();
		GraphicsDevice gd = ge.getDefaultScreenDevice();

		if (!gd.isWindowTranslucencySupported(TRANSLUCENT)) {
			try {
				for (UIManager.LookAndFeelInfo info : UIManager
						.getInstalledLookAndFeels()) {
					if ("Nimbus".equals(info.getName())) {
						UIManager.setLookAndFeel(info.getClassName());
						break;
					}
				}
			} catch (ClassNotFoundException | InstantiationException
					| IllegalAccessException | UnsupportedLookAndFeelException ex) {
				java.util.logging.Logger.getLogger(Login.class.getName()).log(
						java.util.logging.Level.SEVERE, null, ex);
			}
			/* Create and display the form */
			EventQueue.invokeLater(new Runnable() {
				@Override
				public void run() {
					new Login().setVisible(true);
				}
			});

		} else {
			// Create the GUI on the event-dispatching thread
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					Login tw = new Login();

					// Set the window to 55% opaque (45% translucent).
					// tw.setOpacity(0.55f);

					// Display the window.
					// --->>> DESCOMENTAR ESSA LINHA E APAGAR A ABAIXO
					tw.setVisible(true);
					//new Util().setProperties("config/ultimo_login.properties", "login.user&1#login.level&1");
					//new Main().setVisible(true);
				}
			});
		}
	}

	private JButton logarButton;
	private JButton cancelarButton;
	private JLabel logoLabel;
	private JLabel userLabel;
	private JLabel pwsLabel;
	private JPanel jPanel1;
	private JPanel jPanel2;
	private JPanel jPanel3;
	private JPasswordField pwsPasswordField;
	private JTextField userTextField;
	private GridBagConstraints gbc_logoLabel;
}
