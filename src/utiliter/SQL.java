/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utiliter;

import gui.Conection;

import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.mysql.jdbc.Connection;

/**
 * @author Tiago A. Teixeira {@link tiago.a.teixeira@acm.org}
 * 
 */
public class SQL {

	/**
	 * 
	 * @return
	 */
	private final Util util = new Util();

	@SuppressWarnings("unused")
	private int userSis = Integer.MIN_VALUE;
	private int levelSis = Integer.MIN_VALUE;
	private String nameBD = "";
	private Connection con;
	private Statement stm;

	public SQL() {
		try {
			userSis = util.getUserSis();
			levelSis = util.getLevelSis();
			nameBD = util.getNameBD();
			con = (Connection) DriverManager.getConnection(util.getUrlBD()
					+ util.getNameBD(), util.getUserBD(), util.getPwsBD());
			stm = con.createStatement();
		} catch (SQLException ex) {
			Logger.getLogger(SQL.class.getName()).log(Level.SEVERE, null, ex);
			new Conection().setVisible(true);
		}
	}

	public ResultSet carregarCadastroCidade() {
		try {
			return stm
					.executeQuery("Select cidade.idcidade as codigo, cidade.city as cidade, estado.state as estado, estado.uf, cidade.obs, cidade.user, cidade.level FROM "
							+ nameBD
							+ ".cidade, "
							+ nameBD
							+ ".estado where ((cidade.active =1) and (estado.active=1)) and (cidade.estado=estado.idestado) and (cidade.level >="
							+ levelSis + ") order by cidade.idcidade;");
		} catch (SQLException ex) {
			Logger.getLogger(SQL.class.getName()).log(Level.SEVERE, null, ex);
			return null;
		}
	}

	public ResultSet carregarCadastroCandidato() {
		try {
			return stm
					.executeQuery("Select `idcandidato`, `nome`, `endereco`, `cep`, `celular`, `fone`, `cpf`, `ctps`, `rg`, `foto`, `obscad`, `localnasc`, "
							+ "`motivo`, date_format(nascimento, '%d/%m/%Y') as nascimento, `passatempo`, `acidente`, `enfermidade`, `fortes`, `fracos`, `obsvoluntario`, "
							+ "`estado`, `cidade`, `sexo`, `estcivil`, `totalexp`, `area`, `escolaridade`, `esportes`, `iniciar`, `optenfermidade`, "
							+ "`optacidente`, `optesporte`, `outroemp`, `voluntario`, `residencia`, `turno`, `escala`, `level` From `"
							+ nameBD
							+ "`.`candidato` Where (active = 1) and"
							+ "(level >= "
							+ levelSis
							+ ") order by idcandidato;");
		} catch (SQLException ex) {
			Logger.getLogger(SQL.class.getName()).log(Level.SEVERE, null, ex);
			return null;
		}
	}

	public ResultSet carregarExperiencia(String candidato) {
		try {
			return stm
					.executeQuery("Select `empresa`, `gerente`, `cargo`, `salario`, `motivo`, date_format(entrada, '%d/%m/%Y') as entrada, date_format(saida, '%d/%m/%Y') "
							+ "as saida, `estado`, `cidade`, `atividade`, `idemprego` From `"
							+ nameBD
							+ "`.`emprego` where (active = 1) and (level >= "
							+ levelSis
							+ ") and (candidato = "
							+ candidato
							+ ") order " + "by idemprego;");
		} catch (SQLException ex) {
			Logger.getLogger(SQL.class.getName()).log(Level.SEVERE, null, ex);
			return null;
		}
	}

	public ResultSet carregarCurso(String candidato) {
		try {
			return stm
					.executeQuery("Select `curso`, `instituicao`, `carga`, `concluido`, date_format(fim, '%d/%m/%Y') as fim, `observacao`, date_format(inicio, '%d/%m/%Y') as inicio, "
							+ " `diploma`, `tipo`, `idcursos` From `"
							+ nameBD
							+ "`.`cursos` where (`active` = 1) and (`level` >= "
							+ levelSis
							+ ") and "
							+ "(candidato = "
							+ candidato
							+ ") order by idcursos;");
		} catch (SQLException ex) {
			Logger.getLogger(SQL.class.getName()).log(Level.SEVERE, null, ex);
			return null;
		}
	}

	public ResultSet carregarLaudo(int candidato, int etapa) {
		try {
			return stm
					.executeQuery("Select `laudo`, `teste`, `dinamica`, `idlaudo` as id From `"
							+ nameBD
							+ "`.`laudo` where (`active` = 1) and ( `level` >= "
							+ levelSis
							+ ") and (`candidato` = '"
							+ candidato
							+ "') and (`etapa` = '"
							+ etapa
							+ "') order by idlaudo;");
		} catch (SQLException ex) {
			Logger.getLogger(SQL.class.getName()).log(Level.SEVERE, null, ex);
			return null;
		}
	}

	public ResultSet carregarLaudo(String candidato) {
		try {
			return stm
					.executeQuery("Select `laudo`, `indicado`, `etapa`, `candidato`, `idselecao` as id From `"
							+ nameBD
							+ "`.`selecao` where (`active` = 1) and ( `level` >= "
							+ levelSis
							+ ") and (`candidato` = '"
							+ candidato
							+ "') order by idselecao;");
		} catch (SQLException ex) {
			Logger.getLogger(SQL.class.getName()).log(Level.SEVERE, null, ex);
			return null;
		}
	}

	public ResultSet carregarComboBox(String table, String[] fields,
			String order) {
		String field = "";
		for (int i = 0; i < fields.length - 1; i++) {
			field += "`" + table + "`.`" + fields[i] + "`, ";
		}
		field += "`" + table + "`.`" + fields[fields.length - 1] + "`";
		return carregarComboBox(table, field, order);
	}

	public ResultSet carregarComboBox(String table, String field, String order) {
		try {
			return stm.executeQuery("Select " + field + " from " + table
					+ " where (active=1 and level >=" + levelSis
					+ ") order by " + order + ";");
		} catch (SQLException ex) {
			Logger.getLogger(SQL.class.getName()).log(Level.SEVERE, null, ex);
			return null;
		}
	}

	public ResultSet carregarCombo(String table, String order) {
		try {
			return stm.executeQuery("Select " + order + " from " + table
					+ " where (active=1 and level >=" + levelSis
					+ ") order by " + order + ";");
		} catch (SQLException ex) {
			Logger.getLogger(SQL.class.getName()).log(Level.SEVERE, null, ex);
			return null;
		}
	}

	public ResultSet carregarRelatorioCurso(String cod) {
		try {
			return stm
					.executeQuery("Select `candidato`, `curso`, `instituicao`, `inicio`, `fim`, `carga`, `observacao`, `concluido`,"
							+ " `diploma`, `tipo_curso`.`tipo` as type  from `"
							+ nameBD
							+ "`.`cursos`, `"
							+ nameBD
							+ "`.`tipo_curso` where "
							+ "(`cursos`.`active`='1') and (`tipo_curso`.`active`='1') and (`cursos`.`tipo`=`tipo_curso`.`idtcurso`) and "
							+ "(`cursos`.`candidato`='"
							+ cod
							+ "') and (`cursos`.`level` >= '"
							+ levelSis
							+ "') order by `inicio` desc");
		} catch (SQLException ex) {
			Logger.getLogger(SQL.class.getName()).log(Level.SEVERE, null, ex);
			return null;
		}
	}

	public ResultSet carregarRelatorioExperiencia(String cod) {
		try {
			return stm
					.executeQuery("Select `empresa`, `gerente`, `cargo`, `salario`, `motivo`, `entrada`, `saida`, `estado`.`uf`, "
							+ "`cidade`.`city` as cidade from `"
							+ nameBD
							+ "`.`emprego`, `"
							+ nameBD
							+ "`.`cidade`, `"
							+ nameBD
							+ "`.`estado` where "
							+ "(`emprego`.`active`='1') and (`emprego`.`cidade`=`cidade`.`idcidade`) and (`emprego`.`estado`=`estado`.`idestado`) "
							+ "and (`emprego`.`level` >= '"
							+ levelSis
							+ "') and (`emprego`.`candidato`="
							+ cod
							+ ")  order by `entrada` desc limit 2;");
		} catch (SQLException ex) {
			Logger.getLogger(SQL.class.getName()).log(Level.SEVERE, null, ex);
			return null;
		}
	}

	public ResultSet carregarRelatorioCandidato(String cod) {
		try {
			return stm
					.executeQuery("Select `foto`, `nome`, year(curdate())-year(nascimento)-(right(curdate(),5)<right(date(nascimento),5)) "
							+ "as idade, `sexo`.`sexo`, `endereco`, `cidade`.`city`, `estado`.`uf`, `celular`,  `fone`, `cpf`, `rg`, `nascimento`, "
							+ "`localnasc`, `est_civil`.`civil`, `tipo_curso`.`tipo` as instr, `tempo_exper`.`tempo` as totalexp, `turno`, `ctps`,"
							+ "`departamento`.`depto` as area, `voluntario`, `obsvoluntario`, `esporte`.`esporte`, `optesporte`, `outroemp`, "
							+ "`ini_traba`.`iniciar`, `motivo`, `optacidente`, `acidente`, `optenfermidade`, `enfermidade`, `fortes`, `fracos`, "
							+ "`obscad`, `passatempo`, `residencia` From `"
							+ nameBD
							+ "`.`candidato`, `"
							+ nameBD
							+ "`.`sexo`, `"
							+ nameBD
							+ "`.`cidade`, `"
							+ nameBD
							+ "`.`estado`, `"
							+ nameBD
							+ "`.`est_civil`, `"
							+ nameBD
							+ "`.`tipo_curso`, `"
							+ nameBD
							+ "`.`tempo_exper`, `"
							+ nameBD
							+ "`.`departamento`, `"
							+ nameBD
							+ "`.`esporte`, `"
							+ nameBD
							+ "`.`ini_traba`Where (`candidato`.`active`=1) and (`sexo`."
							+ "`idsexo`=`candidato`.`sexo`)and (`cidade`.`idcidade`=`candidato`.`cidade`) and (`estado`.`idestado`=`candidato`."
							+ "`estado`)and (`candidato`.`estcivil`=`est_civil`.`idecivil`)and (`candidato`.`escolaridade`=`tipo_curso`.`idtcurso`) "
							+ "and (`candidato`.`totalexp`=`tempo_exper`.`idtexper`)and (`candidato`.`area`=`departamento`.`iddepto`)and "
							+ "(`candidato`.`esportes`=`esporte`.`idesporte`)and (`candidato`.`iniciar`=`ini_traba`.`iditraba`) and (`candidato`."
							+ "`idcandidato`='"
							+ cod
							+ "') and (`candidato`.`level`>=" + levelSis + ");");
		} catch (SQLException ex) {
			Logger.getLogger(SQL.class.getName()).log(Level.SEVERE, null, ex);
			return null;
		}
	}

	public ResultSet carregarRelatorioLaudos(String sql) {
		try {
			return stm
					.executeQuery("Select `nome`, `etapa`, `optetapaum`, `optetapadois`, `optetapatres`, `optetapaquatro`, `laudo`, "
							+ "`testes_ad`.`teste`, `dinamicas`.`dinamica`, `laudo`.`dinamica` as din, `laudo`.`teste` as tes From `"
							+ nameBD
							+ "`.`candidato`, `"
							+ nameBD
							+ "`.`laudo`, `"
							+ nameBD
							+ "`.`dinamicas`, `"
							+ nameBD
							+ "`.`testes_ad` Where (`candidato`."
							+ "`active`='1') and (`candidato`.`idcandidato`=`laudo`.`candidato`) and ((`laudo`.`teste`=`testes_ad`.`idtestead`) "
							+ "or (`laudo`.`dinamica`=`dinamicas`.`iddinam`)) and (`laudo`<>'') "
							+ sql
							+ " and (`candidato`.`level`>="
							+ levelSis
							+ ") "
							+ "group by `laudo`.`dinamica`, `laudo`.`teste` order by `candidato`.`idcandidato`, `etapa`, `laudo`.`dinamica`;");
		} catch (SQLException ex) {
			Logger.getLogger(SQL.class.getName()).log(Level.SEVERE, null, ex);
			return null;
		}
	}

	public ResultSet carregarRelatorioLaudo(String sql) {
		try {
			return stm
					.executeQuery("Select c.`nome` ncand, s.`laudo`, e.`idetapa` idetp, e.`nome` netapa, DATE_FORMAT( s.`data_i` , '%d/%c/%Y as %H:%i' ) AS `inicio`, "
							+ "DATE_FORMAT( s.`data_f` , '%d/%c/%Y as %H:%i' ) AS `fim`,  Case When s.`indicado` = '-1' THEN 'Em avaliação' "
							+ "WHEN s.`indicado` = '0' THEN 'Não' WHEN s.`indicado` = '1' THEN 'Sim' END AS situacao From `"
							+ nameBD
							+ "`.`selecao` s, `"
							+ nameBD
							+ "`.`candidato` c, `"
							+ nameBD
							+ "`.`etapa` e Where (s.`active`='1') and (s.`candidato`=c.`idcandidato`) and (s.`etapa`=e.`idetapa`) "
							+ sql + " Order by s.`candidato`, s.`etapa`");
		} catch (SQLException ex) {
			Logger.getLogger(SQL.class.getName()).log(Level.SEVERE, null, ex);
			return null;
		}
	}

	public ResultSet carregarRelatorioCargo(String sql) {
		try {
			return stm
					.executeQuery("Select c.`nome` cargo, o.`valor` cbo, `superior`, `subordinado`, `desc_sumaria`, `desc_completa`, `formacao`, obs From `"
							+ nameBD
							+ "`.`cargo` c, `"
							+ nameBD
							+ "`.`cbo` o where (c.`active`='1') and (`cbo`=`idcbo`) "
							+ sql + ";");
		} catch (SQLException ex) {
			Logger.getLogger(SQL.class.getName()).log(Level.SEVERE, null, ex);
			return null;
		}
	}

	public ResultSet carregarRelatorioCompetencia(String sql) {
		try {
			return stm
					.executeQuery("Select co.`nome` From "
							+ nameBD
							+ ".`cargo_com` cc, "
							+ nameBD
							+ ".`cargo`, "
							+ nameBD
							+ ".`competencia` co Where (cc.`active`='1') and (`idcargo`=cc.`cargo`) and (co.`idcomp`=cc.`competencia`) "
							+ sql + " ;");
		} catch (SQLException ex) {
			Logger.getLogger(SQL.class.getName()).log(Level.SEVERE, null, ex);
			return null;
		}
	}

	public ResultSet carregarRelatorioIndicador(String sql) {
		try {
			return stm
					.executeQuery("Select i.`nome` From "
							+ nameBD
							+ ".`cargo_ind` ci, "
							+ nameBD
							+ ".`cargo`, "
							+ nameBD
							+ ".`indicador` i Where (ci.`active`='1') and (`idcargo`=ci.`cargo`) and (i.`idind`=ci.`indicador`)"
							+ sql + ";");
		} catch (SQLException ex) {
			Logger.getLogger(SQL.class.getName()).log(Level.SEVERE, null, ex);
			return null;
		}
	}

	public ResultSet carregarGraficoPesquisa() {
		try {
			return stm
					.executeQuery("Select  count(*) parcial, a.`nome` pergunta, o.`nome` opcao, a.`idask` ask, (Select count(*) from `"
							+ nameBD
							+ "`.`pesquisa` pes where (a.`idask`=pes.`pergunta`) and (pes.`active`='1')) total from `"
							+ nameBD
							+ "`.`pesquisa` p, `"
							+ nameBD
							+ "`.`pergunta` a, `"
							+ nameBD
							+ "`.`opt_ask` o where (p.`active`='1') and (a.`active`='1') and (a.`idask`=p.`pergunta`) and (o.`idoask`=p.`opcoes`) group by a.`nome`;");
		} catch (SQLException ex) {
			Logger.getLogger(SQL.class.getName()).log(Level.SEVERE, null, ex);
			return null;
		}
	}

	public ResultSet localizarCidade(String sql) {
		try {
			return stm
					.executeQuery("Select cidade.idcidade codigo, cidade.city, estado.state, estado.uf, cidade.obs, cidade.user, cidade.level FROM "
							+ nameBD
							+ ".cidade, "
							+ nameBD
							+ ".estado where ((cidade.active =1) and (estado.active=1)) and (cidade.estado=estado.idestado) and (cidade.level >="
							+ levelSis
							+ ") and ("
							+ sql
							+ ") order by cidade.idcidade;");
		} catch (SQLException ex) {
			Logger.getLogger(SQL.class.getName()).log(Level.SEVERE, null, ex);
			return null;
		}
	}

	public ResultSet localizarCandidato(String sql) {
		try {
			return stm
					.executeQuery("Select `idcandidato`, `nome`, `endereco`, `cep`, `celular`, `fone`, `cpf`, `ctps`, `rg`, `foto`, `obscad`, `localnasc`, "
							+ "`motivo`, `nascimento`, `passatempo`, `acidente`, `enfermidade`, `fortes`, `fracos`, `obsvoluntario`, `estado`, `cidade`, `sexo`, "
							+ "`estcivil`, `totalexp`, `area`, `escolaridade`, `esportes`, `iniciar`, `optenfermidade`, `optacidente`, "
							+ "`optesporte`, `outroemp`, `voluntario`, `residencia`, `turno`, `escala`, `level` From "
							+ nameBD
							+ ".`candidato` Where (active = 1) and"
							+ "(level >= "
							+ levelSis
							+ ") and ("
							+ sql
							+ ") order by idcandidato;");
		} catch (SQLException ex) {
			Logger.getLogger(SQL.class.getName()).log(Level.SEVERE, null, ex);
			return null;
		}
	}

	public ResultSet query(String sql) {
		try {
			return stm.executeQuery(sql);
		} catch (SQLException ex) {
			Logger.getLogger(SQL.class.getName()).log(Level.SEVERE, null, ex);
			return null;
		}
	}

	public ResultSet checarUsuario(String user, String pws) {
		try {
			return stm.executeQuery("Select * from Usuario where (nome=\""
					+ user + "\") and (senha=\""
					+ util.encriptar(pws, "SHA-256") + "\") limit 1;");
		} catch (SQLException ex) {
			Logger.getLogger(SQL.class.getName()).log(Level.SEVERE, null, ex);
			return null;
		}
	}

	public ResultSet checarExperienciaCandidato(String candidato) {
		try {
			return stm
					.executeQuery("Select idemprego from Emprego where (candidato=\""
							+ candidato
							+ "\") and (active = 1) and (level>=\""
							+ levelSis + "\");");
		} catch (SQLException ex) {
			Logger.getLogger(SQL.class.getName()).log(Level.SEVERE, null, ex);
			return null;
		}
	}

	public ResultSet carregarSelecaoCandidato(String where) {
		try {
			return stm
					.executeQuery("Select `nome`, `celular`, `fone`, `endereco`, `cep`, `estado`, `cidade`, `estcivil`, `sexo`, `foto`, `idcandidato` From "
							+ nameBD
							+ ".`candidato` Where (active = 1) and (level >= "
							+ levelSis + ")" + where + " order by idcandidato;");
		} catch (SQLException ex) {
			Logger.getLogger(SQL.class.getName()).log(Level.SEVERE, null, ex);
			return null;
		}
	}

	public int executarSQL(String sql) {
		try {
			return stm.executeUpdate(sql);
		} catch (SQLException ex) {
			Logger.getLogger(SQL.class.getName()).log(Level.SEVERE, null, ex);
			return -1;
		}
	}

	public boolean executarSQL(String sql, ArrayList<Object> dt) {
		try {
			PreparedStatement ps = con.prepareStatement(sql);
			for (int i = 0; i < dt.size(); ++i) {
				if (dt.get(i) != null) {
					ps.setDate(i + 1, (Date) dt.get(i));
				} else {
					ps.setNull(i + 1, Types.DATE);
				}
			}
			return ps.execute();
		} catch (SQLException ex) {
			Logger.getLogger(SQL.class.getName()).log(Level.SEVERE, null, ex);
			return false;
		}
	}

	private String chave(String table) {
		String chave = "";
		String sql = "SELECT information_schema.KEY_COLUMN_USAGE.COLUMN_NAME as chave FROM information_schema.KEY_COLUMN_USAGE "
				+ "WHERE information_schema.KEY_COLUMN_USAGE.CONSTRAINT_NAME LIKE 'PRIMARY' AND "
				+ "information_schema.KEY_COLUMN_USAGE.TABLE_SCHEMA LIKE '"
				+ nameBD
				+ "' AND information_schema.KEY_COLUMN_USAGE.TABLE_NAME LIKE '"
				+ table + "';";
		try {
			ResultSet rs = query(sql);
			rs.next();
			chave = rs.getString("chave");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return chave;
	}

	private String colunas(String table) {
		String colunas = "";
		String sql = "SHOW columns FROM `"
				+ nameBD
				+ "`.`"
				+ table
				+ "` where (field not like 'user') and (field not like 'level') and (field not like 'active');";
		try {
			ResultSet rs = query(sql);
			while (rs.next()) {
				colunas += "`" + rs.getString("field") + "`, ";
			}
			colunas = colunas.substring(0, colunas.lastIndexOf(", "));
		} catch (SQLException ex) {
			Logger.getLogger(SQL.class.getName()).log(Level.SEVERE, null, ex);
		}
		return colunas;
	}

	public ResultSet carregarTabela(String table) {
		try {
			return stm
					.executeQuery("Select " + colunas(table) + " from `"
							+ nameBD + "`.`" + table
							+ "` where (`active`='1') order by `"
							+ chave(table) + "`;");
		} catch (SQLException ex) {
			Logger.getLogger(SQL.class.getName()).log(Level.SEVERE, null, ex);
			return null;
		}
	}

	public ResultSet carregarTabela(String table, String where) {
		try {
			return stm.executeQuery("Select " + colunas(table) + " from `"
					+ nameBD + "`.`" + table + "` where (`active`='1') and "
					+ where + " order by `" + chave(table) + "`;");
		} catch (SQLException ex) {
			Logger.getLogger(SQL.class.getName()).log(Level.SEVERE, null, ex);
			return null;
		}
	}

	public int nivelTabela(String table) {
		try {
			ResultSet rs = stm.executeQuery("Select `level` from `" + nameBD
					+ "`.`" + table + "` where (`active`='1') order by `"
					+ chave(table) + "` limit 1;");
			rs.next();
			return rs.getInt("level");
		} catch (SQLException ex) {
			Logger.getLogger(SQL.class.getName()).log(Level.SEVERE, null, ex);
			return 0;
		}
	}

	public void close() {
		try {
			stm.close();
			con.close();
		} catch (SQLException ex) {
			Logger.getLogger(SQL.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
}
