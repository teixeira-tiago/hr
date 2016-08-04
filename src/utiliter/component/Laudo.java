/**
 * 
 */
package utiliter.component;

import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import net.miginfocom.swing.MigLayout;
import utiliter.Util;

/**
 * @author Tiago A. Teixeira {@link tiago.a.teixeira@acm.org}
 *
 */
@SuppressWarnings("serial")
public class Laudo extends JPanel {

	/**
	 * Create the panel.
	 */
	public Laudo() {
		setBounds(0, 0, 750, 350);
		setLayout(new MigLayout(
				"",
				"5[60px:60px,grow]0[77px:77px,grow]0[5px:5px]0[41px,grow]0[210px,grow]0[153px,grow]0[59px]0[50px]0[5px:5px]0[50px]5",
				"5[25px]0[5px:5px]0[120px:120px]0[5px:5px]0[24px:24px]0[5px:5px]0[24px:24px,grow]5"));

		fotoPanel = new JPanel();
		fotoPanel.setLayout(null);
		add(fotoPanel, "cell 0 0 2 3,grow");

		fotoLabel = new JLabel("");
		fotoLabel.setBounds(0, 0, 137, 150);
		fotoPanel.add(fotoLabel);

		nomeLabel = new JLabel("Nome:");
		add(nomeLabel, "cell 3 0,alignx trailing");

		nomeTextField = new JTextField();
		add(nomeTextField, "cell 4 0 2 1,growx");
		nomeTextField.setColumns(10);

		indicadoLabel = new JLabel("Indicado?");
		add(indicadoLabel, "cell 6 0");

		indButtonGroup = new ButtonGroup();

		simRadioButton = new JRadioButton("Sim");
		indButtonGroup.add(simRadioButton);
		add(simRadioButton, "cell 7 0");

		naoRadioButton = new JRadioButton("Não");
		indButtonGroup.add(naoRadioButton);
		add(naoRadioButton, "cell 9 0");

		tableScrollPane = new JScrollPane();
		add(tableScrollPane, "cell 3 2 7 1,grow");

		table = new JTable();
		tableScrollPane.setViewportView(table);

		laudoLabel = new JLabel("Laudo:");
		add(laudoLabel, "cell 0 4");

		testePanel = new JPanel();
		add(testePanel, "cell 1 4 4 1,grow");
		testePanel.setLayout(null);

		sTbutton = new JButton("");
		sTbutton.setBounds(305, 2, 20, 20);
		sTbutton.setPreferredSize(new Dimension(20, 20));
		sTbutton.setIcon(new ImageIcon(getClass().getResource("/img/guard.png")));
		testePanel.add(sTbutton);

		nTbutton = new JButton("...");
		nTbutton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				nTbuttonActionPerformed(e);
			}
		});
		nTbutton.setPreferredSize(new Dimension(20, 20));
		nTbutton.setBounds(280, 2, 20, 20);
		testePanel.add(nTbutton);

		hTbutton = new JButton("");
		hTbutton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				hTbuttonActionPerformed(e);
			}
		});
		hTbutton.setPreferredSize(new Dimension(20, 20));
		hTbutton.setBounds(255, 2, 20, 20);
		hTbutton.setIcon(new ImageIcon(getClass().getResource("/img/help.png")));
		testePanel.add(hTbutton);

		testeComboBox = new AComboBox();
		testeComboBox.setBounds(65, 0, 185, 27);
		testePanel.add(testeComboBox);

		testeLabel = new JLabel("Teste:");
		testeLabel.setBounds(2, 4, 65, 16);
		testePanel.add(testeLabel);

		dinamicaPanel = new JPanel();
		add(dinamicaPanel, "cell 5 4 5 1,grow");
		dinamicaPanel.setLayout(null);

		dinamicaLabel = new JLabel("Dinamica:");
		dinamicaLabel.setBounds(0, 4, 65, 16);
		dinamicaPanel.add(dinamicaLabel);

		dinamicaComboBox = new AComboBox();
		dinamicaComboBox.setBounds(63, 0, 185, 27);
		dinamicaPanel.add(dinamicaComboBox);

		hDbutton = new JButton("");
		hDbutton.setPreferredSize(new Dimension(20, 20));
		hDbutton.setBounds(255, 2, 20, 20);
		hDbutton.setIcon(new ImageIcon(getClass().getResource("/img/help.png")));
		hDbutton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				hDbuttonActionPerformed(e);
			}
		});
		dinamicaPanel.add(hDbutton);

		nDbutton = new JButton("...");
		nDbutton.setPreferredSize(new Dimension(20, 20));
		nDbutton.setBounds(280, 2, 20, 20);
		nDbutton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				nDbuttonActionPerformed(e);
			}
		});
		dinamicaPanel.add(nDbutton);

		sDbutton = new JButton("");
		sDbutton.setPreferredSize(new Dimension(20, 20));
		sDbutton.setBounds(305, 2, 20, 20);
		sDbutton.setIcon(new ImageIcon(getClass().getResource("/img/guard.png")));
		dinamicaPanel.add(sDbutton);

		laudoScrollPane = new JScrollPane();
		add(laudoScrollPane, "cell 0 6 10 1,grow");

		laudoTextArea = new JTextArea();
		laudoScrollPane.setViewportView(laudoTextArea);
	}

	public JPanel getPanel() {
		return this;
	}

	/**
	 * @return the indButtonGroup
	 */
	public ButtonGroup getIndButtonGroup() {
		return indButtonGroup;
	}

	/**
	 * @param indButtonGroup
	 *            the indButtonGroup to set
	 */
	public void setIndButtonGroup(ButtonGroup indButtonGroup) {
		this.indButtonGroup = indButtonGroup;
	}

	/**
	 * @return the fotoPanel
	 */
	public JPanel getFotoPanel() {
		return fotoPanel;
	}

	/**
	 * @param fotoPanel
	 *            the fotoPanel to set
	 */
	public void setFotoPanel(JPanel fotoPanel) {
		this.fotoPanel = fotoPanel;
	}

	/**
	 * @return the nomeLabel
	 */
	public JLabel getNomeLabel() {
		return nomeLabel;
	}

	/**
	 * @param nomeLabel
	 *            the nomeLabel to set
	 */
	public void setNomeLabel(JLabel nomeLabel) {
		this.nomeLabel = nomeLabel;
	}

	/**
	 * @return the tableScrollPane
	 */
	public JScrollPane getTableScrollPane() {
		return tableScrollPane;
	}

	/**
	 * @param tableScrollPane
	 *            the tableScrollPane to set
	 */
	public void setTableScrollPane(JScrollPane tableScrollPane) {
		this.tableScrollPane = tableScrollPane;
	}

	/**
	 * @return the laudoLabel
	 */
	public JLabel getLaudoLabel() {
		return laudoLabel;
	}

	/**
	 * @param laudoLabel
	 *            the laudoLabel to set
	 */
	public void setLaudoLabel(JLabel laudoLabel) {
		this.laudoLabel = laudoLabel;
	}

	/**
	 * @return the testePanel
	 */
	public JPanel getTestePanel() {
		return testePanel;
	}

	/**
	 * @param testePanel
	 *            the testePanel to set
	 */
	public void setTestePanel(JPanel testePanel) {
		this.testePanel = testePanel;
	}

	/**
	 * @return the testeLabel
	 */
	public JLabel getTesteLabel() {
		return testeLabel;
	}

	/**
	 * @param testeLabel
	 *            the testeLabel to set
	 */
	public void setTesteLabel(JLabel testeLabel) {
		this.testeLabel = testeLabel;
	}

	/**
	 * @return the dinamicaPanel
	 */
	public JPanel getDinamicaPanel() {
		return dinamicaPanel;
	}

	/**
	 * @param dinamicaPanel
	 *            the dinamicaPanel to set
	 */
	public void setDinamicaPanel(JPanel dinamicaPanel) {
		this.dinamicaPanel = dinamicaPanel;
	}

	/**
	 * @return the dinamicaLabel
	 */
	public JLabel getDinamicaLabel() {
		return dinamicaLabel;
	}

	/**
	 * @param dinamicaLabel
	 *            the dinamicaLabel to set
	 */
	public void setDinamicaLabel(JLabel dinamicaLabel) {
		this.dinamicaLabel = dinamicaLabel;
	}

	/**
	 * @return the laudoScrollPane
	 */
	public JScrollPane getLaudoScrollPane() {
		return laudoScrollPane;
	}

	/**
	 * @param laudoScrollPane
	 *            the laudoScrollPane to set
	 */
	public void setLaudoScrollPane(JScrollPane laudoScrollPane) {
		this.laudoScrollPane = laudoScrollPane;
	}

	private void hTbuttonActionPerformed(ActionEvent e) {
		try {
			File file = new File("formularios/ManualDoColaborador.pdf");
			Desktop.getDesktop().open(file);
		} catch (IOException ex) {
			Logger.getLogger(this.getName()).log(Level.SEVERE, null, ex);
		}
	}

	private void hDbuttonActionPerformed(ActionEvent e) {
		try {
			File file = new File("formularios/ManualDoColaborador.pdf");
			Desktop.getDesktop().open(file);
		} catch (IOException ex) {
			Logger.getLogger(this.getName()).log(Level.SEVERE, null, ex);
		}
	}

	private void nTbuttonActionPerformed(ActionEvent e) {
		util.loadTelaCadastro("testes_ad", new JFrame(),
				"Cadastro de Testes Psicologicos");
	}

	private void nDbuttonActionPerformed(ActionEvent e) {
		util.loadTelaCadastro("dinamicas", new JFrame(),
				"Cadastro de Dinamicas");
	}

	/**
	 * @return the fotoLabel
	 */
	public JLabel getFotoLabel() {
		return fotoLabel;
	}

	/**
	 * @param fotoLabel
	 *            the fotoLabel to set
	 */
	public void setFotoLabel(JLabel fotoLabel) {
		this.fotoLabel = fotoLabel;
	}

	/**
	 * @return the indicadoLabel
	 */
	public JLabel getIndicadoLabel() {
		return indicadoLabel;
	}

	/**
	 * @param indicadoLabel
	 *            the indicadoLabel to set
	 */
	public void setIndicadoLabel(JLabel indicadoLabel) {
		this.indicadoLabel = indicadoLabel;
	}

	/**
	 * @return the simRadioButton
	 */
	public JRadioButton getSimRadioButton() {
		return simRadioButton;
	}

	/**
	 * @param simRadioButton
	 *            the simRadioButton to set
	 */
	public void setSimRadioButton(JRadioButton simRadioButton) {
		this.simRadioButton = simRadioButton;
	}

	/**
	 * @return the naoRadioButton
	 */
	public JRadioButton getNaoRadioButton() {
		return naoRadioButton;
	}

	/**
	 * @param naoRadioButton
	 *            the naoRadioButton to set
	 */
	public void setNaoRadioButton(JRadioButton naoRadioButton) {
		this.naoRadioButton = naoRadioButton;
	}

	/**
	 * @return the sTbutton
	 */
	public JButton getsTbutton() {
		return sTbutton;
	}

	/**
	 * @param sTbutton
	 *            the sTbutton to set
	 */
	public void setsTbutton(JButton sTbutton) {
		this.sTbutton = sTbutton;
	}

	/**
	 * @return the nTbutton
	 */
	public JButton getnTbutton() {
		return nTbutton;
	}

	/**
	 * @param nTbutton
	 *            the nTbutton to set
	 */
	public void setnTbutton(JButton nTbutton) {
		this.nTbutton = nTbutton;
	}

	/**
	 * @return the hTbutton
	 */
	public JButton gethTbutton() {
		return hTbutton;
	}

	/**
	 * @param hTbutton
	 *            the hTbutton to set
	 */
	public void sethTbutton(JButton hTbutton) {
		this.hTbutton = hTbutton;
	}

	/**
	 * @return the testeComboBox
	 */
	public AComboBox getTesteComboBox() {
		return testeComboBox;
	}

	/**
	 * @param testeComboBox
	 *            the testeComboBox to set
	 */
	public void setTesteComboBox(AComboBox testeComboBox) {
		this.testeComboBox = testeComboBox;
	}

	/**
	 * @return the dinamicaComboBox
	 */
	public AComboBox getDinamicaComboBox() {
		return dinamicaComboBox;
	}

	/**
	 * @param dinamicaComboBox
	 *            the dinamicaComboBox to set
	 */
	public void setDinamicaComboBox(AComboBox dinamicaComboBox) {
		this.dinamicaComboBox = dinamicaComboBox;
	}

	/**
	 * @return the hDbutton
	 */
	public JButton gethDbutton() {
		return hDbutton;
	}

	/**
	 * @param hDbutton
	 *            the hDbutton to set
	 */
	public void sethDbutton(JButton hDbutton) {
		this.hDbutton = hDbutton;
	}

	/**
	 * @return the nDbutton
	 */
	public JButton getnDbutton() {
		return nDbutton;
	}

	/**
	 * @param nDbutton
	 *            the nDbutton to set
	 */
	public void setnDbutton(JButton nDbutton) {
		this.nDbutton = nDbutton;
	}

	/**
	 * @return the sDbutton
	 */
	public JButton getsDbutton() {
		return sDbutton;
	}

	/**
	 * @param sDbutton
	 *            the sDbutton to set
	 */
	public void setsDbutton(JButton sDbutton) {
		this.sDbutton = sDbutton;
	}

	/**
	 * @return the laudoTextArea
	 */
	public JTextArea getLaudoTextArea() {
		return laudoTextArea;
	}

	/**
	 * @param laudoTextArea
	 *            the laudoTextArea to set
	 */
	public void setLaudoTextArea(JTextArea laudoTextArea) {
		this.laudoTextArea = laudoTextArea;
	}

	/**
	 * @return the table
	 */
	public JTable getTable() {
		return table;
	}

	/**
	 * @param table
	 *            the table to set
	 */
	public void setTable(JTable table) {
		this.table = table;
	}

	/**
	 * @return the nomeTextField
	 */
	public JTextField getNomeTextField() {
		return nomeTextField;
	}

	/**
	 * @param nomeTextField
	 *            the nomeTextField to set
	 */
	public void setNomeTextField(JTextField nomeTextField) {
		this.nomeTextField = nomeTextField;
	}

	private final Util util = new Util();
	private ButtonGroup indButtonGroup;
	private JPanel fotoPanel;
	private JLabel fotoLabel;
	private JLabel nomeLabel;
	private JLabel indicadoLabel;
	private JRadioButton simRadioButton;
	private JRadioButton naoRadioButton;
	private JScrollPane tableScrollPane;
	private JLabel laudoLabel;
	private JPanel testePanel;
	private JButton sTbutton;
	private JButton nTbutton;
	private JButton hTbutton;
	private AComboBox testeComboBox;
	private JLabel testeLabel;
	private JPanel dinamicaPanel;
	private JLabel dinamicaLabel;
	private AComboBox dinamicaComboBox;
	private JButton hDbutton;
	private JButton nDbutton;
	private JButton sDbutton;
	private JScrollPane laudoScrollPane;
	private JTextArea laudoTextArea;
	private JTable table;
	private JTextField nomeTextField;
}
