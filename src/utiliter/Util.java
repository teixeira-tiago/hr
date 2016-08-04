/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utiliter;

import java.awt.Font;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.Normalizer;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Currency;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JTable;
import javax.swing.UIManager;

import org.apache.pdfbox.exceptions.COSVisitorException;
import org.apache.pdfbox.util.PDFMergerUtility;

import cadastro.TelaCadastro;

/**
 * @author Tiago A. Teixeira {@link tiago.a.teixeira@acm.org}
 * 
 */
public final class Util {

	private int userSis = Integer.MIN_VALUE;
	private int levelSis = Integer.MIN_VALUE;
	private String userBD;
	private String nameBD;
	private String urlBD;
	private String pwsBD;
	private String drvBD;
	private String srvBD;
	private String prtBD;

	/**
	 * @return the userBD
	 */
	public String getUserBD() {
		return userBD;
	}

	/**
	 * @param userBD
	 *            the userBD to set
	 */
	public void setUserBD(String userBD) {
		this.userBD = userBD;
	}

	/**
	 * @return the nameBD
	 */
	public String getNameBD() {
		return nameBD;
	}

	/**
	 * @param nameBD
	 *            the nameBD to set
	 */
	public void setNameBD(String nameBD) {
		this.nameBD = nameBD;
	}

	/**
	 * @return the urlBD
	 */
	public String getUrlBD() {
		return urlBD;
	}

	/**
	 * @param urlBD
	 *            the urlBD to set
	 */
	public void setUrlBD(String urlBD) {
		this.urlBD = urlBD;
	}

	/**
	 * @return the pwsBD
	 */
	public String getPwsBD() {
		return pwsBD;
	}

	/**
	 * @param pwsBD
	 *            the pwsBD to set
	 */
	public void setPwsBD(String pwsBD) {
		this.pwsBD = pwsBD;
	}

	/**
	 * @return the drvBD
	 */
	public String getDrvBD() {
		return drvBD;
	}

	/**
	 * @param drvBD
	 *            the drvBD to set
	 */
	public void setDrvBD(String drvBD) {
		this.drvBD = drvBD;
	}

	/**
	 * @return the srvBD
	 */
	public String getSrvBD() {
		return srvBD;
	}

	/**
	 * @param srvBD
	 *            the srvBD to set
	 */
	public void setSrvBD(String srvBD) {
		this.srvBD = srvBD;
	}

	/**
	 * @return the prtBD
	 */
	public String getPrtBD() {
		return prtBD;
	}

	/**
	 * @param prtBD
	 *            the prtBD to set
	 */
	public void setPrtBD(String prtBD) {
		this.prtBD = prtBD;
	}

	public Util() {
		Properties prop = getProperties("config/ultimo_login.properties");
		if (!prop.isEmpty()) {
			userSis = Integer.parseInt(prop.getProperty("login.user"));
			levelSis = Integer.parseInt(prop.getProperty("login.level"));
		}
		prop = getProperties("config/bd.properties");
		userBD = prop.getProperty("bd.usr");
		nameBD = prop.getProperty("bd.name");
		urlBD = prop.getProperty("bd.url");
		pwsBD = prop.getProperty("bd.pws");
		drvBD = prop.getProperty("bd.drive");
		srvBD = prop.getProperty("bd.server");
		prtBD = prop.getProperty("bd.port");
	}

	public int getUserSis() {
		return userSis;
	}

	public int getLevelSis() {
		return levelSis;
	}

	public Font getFont() {
		Properties prop = getProperties("config/parametros.properties");
		Font fonte = new Font(prop.getProperty("conf.fonte"),
				Integer.parseInt(prop.getProperty("conf.fonte.espesura")),
				Integer.parseInt(prop.getProperty("conf.fonte.tamanho")));

		return fonte;
	}

	public String getConf(String conf) {
		Properties prop = getProperties("config/parametros.properties");
		return prop.getProperty("conf." + conf);
	}

	public void setUIFont(Font f) {
		Enumeration<Object> keys = UIManager.getDefaults().keys();
		while (keys.hasMoreElements()) {
			Object key = keys.nextElement();
			Object value = UIManager.get(key);
			if (value instanceof Font)
				UIManager.put(key, f);
		}
	}

	public String dataToStr(java.sql.Date data) {
		try {
			SimpleDateFormat formatador = new SimpleDateFormat("dd/MM/yyyy");
			return formatador.format(data);
		} catch (Exception e) {
			return "";
		}
	}

	public String dataToStr(java.util.Date data) {
		try {
			SimpleDateFormat formatador = new SimpleDateFormat("dd/MM/yyyy");
			return formatador.format(data);
		} catch (Exception e) {
			return "";
		}
	}

	public java.sql.Date strToDate(String str) {
		try {
			SimpleDateFormat formatador = new SimpleDateFormat("dd/MM/yyyy");
			java.util.Date data = formatador.parse(str);
			return new java.sql.Date(data.getTime());
		} catch (ParseException ex) {
			Logger.getLogger(Util.class.getName()).log(Level.SEVERE, null, ex);
			return null;
		}
	}

	public java.sql.Date dateToSQLdate(Object date) {
		return new java.sql.Date(((java.util.Date) date).getTime());
	}

	public String getAlfaNum(String str) {
		return Normalizer.normalize(str, Normalizer.Form.NFD)
				.replaceAll("[\\W]", "").replaceAll("_", "");
	}

	public String realToDolar(String real) {
		try {
			DecimalFormat formater = new DecimalFormat("#.00");
			formater.setCurrency(Currency.getInstance(new Locale("pt", "BR")));
			if (real.trim().isEmpty()) {
				real = "0";
			}
			return String.valueOf(formater.parse(real));
		} catch (ParseException ex) {
			Logger.getLogger(Util.class.getName()).log(Level.SEVERE, null, ex);
		}
		return "";
	}

	public String dolarToReal(String dolar) {
		DecimalFormat formater = new DecimalFormat("#.00");
		formater.setCurrency(Currency.getInstance(new Locale("pt", "BR")));
		if ((dolar == null) || (dolar.trim().isEmpty())) {
			dolar = "0";
		}
		return formater.format(Double.parseDouble(dolar));
	}

	public String nulo(String valor) {
		try {
			return valor;
		} catch (NullPointerException ex) {
			return "";
		}
	}

	public String encriptar(String senha, String type) {
		try {
			byte[] bytes;
			MessageDigest md = MessageDigest.getInstance(type);
			md.update(senha.getBytes("UTF-8"));
			bytes = md.digest();

			StringBuilder s = new StringBuilder();
			for (int i = 0; i < bytes.length; i++) {
				int parteAlta = ((bytes[i] >> 4) & 0xf) << 4;
				int parteBaixa = bytes[i] & 0xf;
				if (parteAlta == 0) {
					s.append('0');
				}
				s.append(Integer.toHexString(parteAlta | parteBaixa));
			}
			return s.toString();
		} catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
			return "";
		}
	}

	public void setProperties(String arquivo, String body) {
		FileInputStream fileInput = null;
		FileOutputStream fileOutput = null;
		File file = new File(arquivo);
		Properties props = new Properties();
		try {
			fileInput = new FileInputStream(file);
			props.load(fileInput);
			fileInput.close();
		} catch (IOException e) {
			limparArquivo(arquivo);
		}
		String[] linha = body.split("#");
		String[] conteudo;
		int i;
		for (i = 0; i < linha.length; ++i) {
			conteudo = linha[i].split("&");
			props.setProperty(conteudo[0], conteudo[1]);
		}
		try {
			fileOutput = new FileOutputStream(file);
			props.store(fileOutput,
					"Arquivo para uso de comunicacao entre processos");
			fileOutput.close();
		} catch (IOException e) {
		}
	}

	public Properties getProperties(String arquivo) {
		File file = new File(arquivo);
		Properties props = new Properties();
		try {
			try (FileInputStream fileInput = new FileInputStream(file)) {
				props.load(fileInput);
			}
		} catch (IOException e) {
		}
		return props;
	}

	public void limparArquivo(String arquivo) {
		try {
			try (FileWriter writer = new FileWriter(new File(arquivo), false);
					PrintWriter arq = new PrintWriter(writer)) {
				arq.println();
				arq.flush();
			}
		} catch (Exception e) {
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public DefaultComboBoxModel initComboBox(String table, String[] fields,
			String order) {
		DefaultComboBoxModel modelo = new DefaultComboBoxModel();
		SQL sql = new SQL();
		ResultSet rs = sql.carregarComboBox(table, fields, order);
		String aux = "";
		try {
			while (rs.next()) {
				aux = rs.getString(fields[0]);
				if (fields.length > 1) {
					for (int i = 1; i < fields.length; ++i) {
						aux = rs.getString(fields[i]) + " - " + aux;
					}
				}
				modelo.addElement(aux);
			}
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		sql.close();
		return modelo;
	}

	public HashMap<Integer, Integer> initCombo(String table, String order) {
		HashMap<Integer, Integer> modelo = new HashMap<Integer, Integer>();
		SQL sql = new SQL();
		ResultSet rs = sql.carregarCombo(table, order);
		int aux = 0;
		try {
			while (rs.next()) {
				modelo.put(aux++, rs.getInt(order));
			}
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		sql.close();
		return modelo;
	}

	public void ocultarColunas(JTable tab, int start, int end) {
		for (int i = start; i <= end; ++i) {
			tab.getColumnModel().getColumn(i).setMaxWidth(0);
			tab.getColumnModel().getColumn(i).setMinWidth(0);
			tab.getColumnModel().getColumn(i).setPreferredWidth(0);
		}
	}

	public void loadTela(String nome, String titulo) {
		JDialog tela = null;
		Class<?> telaClass;
		try {
			telaClass = Class.forName(nome);
			tela = (JDialog) telaClass.newInstance();
			tela.setTitle(titulo);
			tela.setVisible(true);
			tela.dispose();
		} catch (ClassNotFoundException ex) {
			ex.printStackTrace();
		} catch (InstantiationException ex) {
			ex.printStackTrace();
		} catch (IllegalAccessException ex) {
			ex.printStackTrace();
		}
	}

	public void loadTelaCadastro(String tabela, JFrame frame, String titulo) {
		TelaCadastro tela = new TelaCadastro(frame);
		tela.setTabela(tabela);
		tela.init();
		tela.setTitle(titulo);
		tela.setVisible(true);
		tela.dispose();
	}

	public boolean isNumeric(String s) {
		try {
			Long.parseLong(s);
			return true;
		} catch (NumberFormatException ex) {
			return false;
		}
	}

	public boolean isNumeric(Object o) {
		try {
			Long.parseLong(String.valueOf(o));
			return true;
		} catch (NumberFormatException ex) {
			return false;
		}
	}

	public boolean isNullOrBlank(String s) {
		return (s == null || s.trim().equals(""));
	}

	public boolean isNull(Object o) {
		return (o == null);
	}

	public int getIdade(Date data) {
		Calendar cData = Calendar.getInstance();
		Calendar cHoje = Calendar.getInstance();
		cData.setTime(data);
		cData.set(Calendar.YEAR, cHoje.get(Calendar.YEAR));
		int idade = cData.after(cHoje) ? -1 : 0;
		cData.setTime(data);
		idade += cHoje.get(Calendar.YEAR) - cData.get(Calendar.YEAR);
		return idade;
	}

	public void mergePDF() {
		try {
			// Get the byte streams from any source (maintain order)
			List<InputStream> sourcePDFs = new ArrayList<>();
			sourcePDFs.add(new FileInputStream(new File("pdf1.pdf")));
			sourcePDFs.add(new FileInputStream(new File("pdf2.pdf")));
			// sourcePDFs.add(new FileInputStream(new File("pdf3.pdf")));

			// initialize the Merger utility and add pdfs to be merged
			PDFMergerUtility mergerUtility = new PDFMergerUtility();
			mergerUtility.addSources(sourcePDFs);
			// set the destination pdf name and merge input pdfs
			mergerUtility.setDestinationFileName("merged.pdf");
			mergerUtility.mergeDocuments();
		} catch (FileNotFoundException ex) {
			Logger.getLogger(Util.class.getName()).log(Level.SEVERE, null, ex);
		} catch (IOException | COSVisitorException ex) {
			Logger.getLogger(Util.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
}
