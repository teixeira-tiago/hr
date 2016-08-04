/**
 * 
 */
package pesquisa;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PiePlot3D;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.CategoryItemRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;

import utiliter.SQL;
import utiliter.Util;
import utiliter.component.Graficos;

/**
 * @author Tiago A. Teixeira {@link tiago.a.teixeira@acm.org}
 * 
 */
public class RelatorioPesquisa {

	private final Util util = new Util();

	public ArrayList<?> getPesquisa(String sql, int porcentagem) {
		ArrayList<DadosPesquisa> relatorio = new ArrayList<DadosPesquisa>();
		ResultSet rsPrin, rsSec;
		String query = "", temp;
		int ask, opt;
		// long min = System.currentTimeMillis();
		query = "Select `idoask` from `" + util.getNameBD()
				+ "`.`opt_ask` where (`active`='1');";
		SQL qrPri = new SQL();
		SQL qrSec = new SQL();
		rsPrin = qrPri.query(query);
		String[] grupo = new String[] { "", "", "", "", "", "" };
		Double[] coluna = new Double[6];
		Double total = 0.0;
		try {
			while (rsPrin.next()) {
				opt = rsPrin.getInt("idoask");
				switch ((opt % 5) + 1) {
				case 1:
					grupo[5] += "(p.`opcoes`='" + opt + "') or ";
					break;
				case 2:
					grupo[1] += "(p.`opcoes`='" + opt + "') or ";
					break;
				case 3:
					grupo[2] += "(p.`opcoes`='" + opt + "') or ";
					break;
				case 4:
					grupo[3] += "(p.`opcoes`='" + opt + "') or ";
					break;
				case 5:
					grupo[4] += "(p.`opcoes`='" + opt + "') or ";
					break;
				}
			}
			rsPrin.close();
			qrPri.close();
			for (int i = grupo.length - 1; i > 0; --i) {
				temp = grupo[i];
				temp = temp.substring(0, temp.lastIndexOf(" or "));
				temp = " and (" + temp + ")";
				grupo[i] = temp;
			}
		} catch (SQLException e1) {
			e1.printStackTrace();
		}

		query = "Select a.`nome`, a.`ask`, a.`idask` from `"
				+ util.getNameBD()
				+ "`.`pergunta` a, `"
				+ util.getNameBD()
				+ "`.`pesquisa` p, `"
				+ util.getNameBD()
				+ "`.`departamento` d, `"
				+ util.getNameBD()
				+ "`.`questionario` q where  (p.`active`='1') and (p.`departamento`=d.`iddepto`) and (p.`questionario`=q.`idquest`) and (a.`questionario`=q.`idquest`) "
				+ sql + " group by a.`nome` order by a.`idask`;";
		qrPri = new SQL();
		rsPrin = qrPri.query(query);
		try {
			while (rsPrin.next()) {
				for (int i = 1; i < coluna.length; ++i) {
					coluna[i] = 0.0;
				}
				int per = rsPrin.getInt("ask");
				if (per > 0) {
					ask = rsPrin.getInt("idask");
					query = "Select distinct count(*) col1 from `"
							+ util.getNameBD()
							+ "`.`pergunta` a, `"
							+ util.getNameBD()
							+ "`.`pesquisa` p, `"
							+ util.getNameBD()
							+ "`.`departamento` d, `"
							+ util.getNameBD()
							+ "`.`questionario` q where  (p.`active`='1') and (p.`departamento`=d.`iddepto`) and (p.`questionario`=q.`idquest`) and (a.`questionario`=q.`idquest`) "
							+ sql + grupo[1] + " and (p.`pergunta`='" + ask
							+ "') group by a.`nome` order by a.`idask`;";
					qrSec = new SQL();
					rsSec = qrSec.query(query);
					while (rsSec.next()) {
						coluna[1] = rsSec.getDouble("col1");
					}
					qrSec.close();
					rsSec.close();
					query = "Select distinct count(*) col2 from `"
							+ util.getNameBD()
							+ "`.`pergunta` a, `"
							+ util.getNameBD()
							+ "`.`pesquisa` p, `"
							+ util.getNameBD()
							+ "`.`departamento` d, `"
							+ util.getNameBD()
							+ "`.`questionario` q where  (p.`active`='1') and (p.`departamento`=d.`iddepto`) and (p.`questionario`=q.`idquest`) and (a.`questionario`=q.`idquest`) "
							+ sql + grupo[2] + " and (p.`pergunta`='" + ask
							+ "') group by a.`nome` order by a.`idask`;";
					qrSec = new SQL();
					rsSec = qrSec.query(query);
					while (rsSec.next()) {
						coluna[2] = rsSec.getDouble("col2");
					}
					qrSec.close();
					rsSec.close();
					query = "Select distinct count(*) col3 from `"
							+ util.getNameBD()
							+ "`.`pergunta` a, `"
							+ util.getNameBD()
							+ "`.`pesquisa` p, `"
							+ util.getNameBD()
							+ "`.`departamento` d, `"
							+ util.getNameBD()
							+ "`.`questionario` q where  (p.`active`='1') and (p.`departamento`=d.`iddepto`) and (p.`questionario`=q.`idquest`) and (a.`questionario`=q.`idquest`) "
							+ sql + grupo[3] + " and (p.`pergunta`='" + ask
							+ "') group by a.`nome` order by a.`idask`;";
					qrSec = new SQL();
					rsSec = qrSec.query(query);
					while (rsSec.next()) {
						coluna[3] = rsSec.getDouble("col3");
					}
					qrSec.close();
					rsSec.close();
					query = "Select distinct count(*) col4 from `"
							+ util.getNameBD()
							+ "`.`pergunta` a, `"
							+ util.getNameBD()
							+ "`.`pesquisa` p, `"
							+ util.getNameBD()
							+ "`.`departamento` d, `"
							+ util.getNameBD()
							+ "`.`questionario` q where  (p.`active`='1') and (p.`departamento`=d.`iddepto`) and (p.`questionario`=q.`idquest`) and (a.`questionario`=q.`idquest`) "
							+ sql + grupo[4] + " and (p.`pergunta`='" + ask
							+ "') group by a.`nome` order by a.`idask`;";
					qrSec = new SQL();
					rsSec = qrSec.query(query);
					while (rsSec.next()) {
						coluna[4] = rsSec.getDouble("col4");
					}
					qrSec.close();
					rsSec.close();
					query = "Select distinct count(*) col5 from `"
							+ util.getNameBD()
							+ "`.`pergunta` a, `"
							+ util.getNameBD()
							+ "`.`pesquisa` p, `"
							+ util.getNameBD()
							+ "`.`departamento` d, `"
							+ util.getNameBD()
							+ "`.`questionario` q where  (p.`active`='1') and (p.`departamento`=d.`iddepto`) and (p.`questionario`=q.`idquest`) and (a.`questionario`=q.`idquest`) "
							+ sql + grupo[5] + " and (p.`pergunta`='" + ask
							+ "') group by a.`nome` order by a.`idask`;";
					qrSec = new SQL();
					rsSec = qrSec.query(query);
					while (rsSec.next()) {
						coluna[5] = rsSec.getDouble("col5");
					}
					qrSec.close();
					rsSec.close();
					total = 0.0;
					for (int i = 1; i < coluna.length; ++i) {
						total += coluna[i];
					}
				}
				relatorio.add(per > 0 ? info(rsPrin.getString("nome"),
						rsPrin.getInt("ask"), coluna, porcentagem, total)
						: info(rsPrin.getString("nome"), rsPrin.getInt("ask")));
			}
			qrPri.close();
			rsPrin.close();
			// String tempoTotal = tempo(System.currentTimeMillis() - min);
			// System.out.println("Tempo total das querys " + tempoTotal);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return relatorio;
	}

	public ArrayList<?> getPie3D(String opt) {
		ArrayList<Graficos> graficos = new ArrayList<Graficos>();
		DefaultPieDataset dataset;
		SQL query = new SQL();
		try {
			ResultSet rs = query
					.query("Select a.`nome`, a.`idask` ask from `"
							+ util.getNameBD()
							+ "`.`pesquisa` p, `"
							+ util.getNameBD()
							+ "`.`pergunta` a, `"
							+ util.getNameBD()
							+ "`.`questionario` q, `"
							+ util.getNameBD()
							+ "`.`departamento` d where (a.`active`='1') and (p.`active`='1') and (q.`active`='1') and (p.`departamento`=d.`iddepto`) "
							+ opt
							+ " and (p.`pergunta`=a.`idask`) and (p.`questionario`=q.`idquest`) group by a.`nome` order by a.`idask`;");
			ArrayList<DadosGrafico> dados = new ArrayList<DadosGrafico>();

			while (rs.next()) {
				dados.add(dados(rs.getString("nome"), rs.getInt("ask")));
			}

			Iterator<DadosGrafico> iterator = dados.iterator();
			while (iterator.hasNext()) {
				DadosGrafico dadosGrafico = iterator.next();
				dataset = new DefaultPieDataset();
				rs = query
						.query("Select o.`nome`, count(*) parcial from `"
								+ util.getNameBD()
								+ "`.`pesquisa` p, `"
								+ util.getNameBD()
								+ "`.`opt_ask` o, `"
								+ util.getNameBD()
								+ "`.`questionario` q, `"
								+ util.getNameBD()
								+ "`.`departamento` d where (p.`active`='1') and (p.`questionario`=q.`idquest`) and (p.`pergunta`='"
								+ dadosGrafico.getId()
								+ "') and (p.`opcoes`=o.`idoask`) and (p.`departamento`=d.`iddepto`) "
								+ opt + " group by o.`nome`;");
				while (rs.next()) {
					dataset.setValue(rs.getString("nome"),
							rs.getDouble("parcial"));
				}

				JFreeChart chart = ChartFactory.createPieChart3D(
						dadosGrafico.getTitulo(), dataset, true, false, false);

				PiePlot3D plot = (PiePlot3D) chart.getPlot();
				plot.setForegroundAlpha(0.5f);
				plot.setBackgroundAlpha(0.0f);
				plot.setLabelGenerator(new StandardPieSectionLabelGenerator(
						"{0} ({2})", new DecimalFormat("#,##0"),
						new DecimalFormat("0.00%")));

				chart.setBackgroundPaint(Color.white);
				chart.setAntiAlias(true);
				chart.setBorderVisible(false);
				graficos.add(grafico(chart.createBufferedImage(555, 300)));
			}
			rs.close();
			query.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return graficos;
	}

	@SuppressWarnings("deprecation")
	public ArrayList<?> getBarChart3D(String opt) {
		ArrayList<Graficos> graficos = new ArrayList<Graficos>();
		DefaultCategoryDataset dataset;
		SQL query = new SQL();
		try {
			ResultSet rs = query
					.query("Select a.`nome`, a.`idask` ask, year(p.`data`) ano from `"
							+ util.getNameBD()
							+ "`.`pesquisa` p, `"
							+ util.getNameBD()
							+ "`.`pergunta` a, `"
							+ util.getNameBD()
							+ "`.`questionario` q, `"
							+ util.getNameBD()
							+ "`.`departamento` d where (a.`active`='1') and (p.`active`='1') and (q.`active`='1') and (p.`departamento`=d.`iddepto`) "
							+ opt
							+ " and (p.`pergunta`=a.`idask`) and (p.`questionario`=q.`idquest`) group by a.`nome` order by a.`idask`;");
			ArrayList<DadosGrafico> dados = new ArrayList<DadosGrafico>();
			ArrayList<Integer> anos = new ArrayList<Integer>();
			while (rs.next()) {
				dados.add(dados(rs.getString("nome"), rs.getInt("ask")));
			}
			rs = query
					.query("Select year(p.`data`) ano from `"
							+ util.getNameBD()
							+ "`.`pesquisa` p, `"
							+ util.getNameBD()
							+ "`.`questionario` q, `"
							+ util.getNameBD()
							+ "`.`departamento` d where (p.`questionario`=q.`idquest`) and (p.`departamento`=d.`iddepto`) "
							+ opt + " group by ano;");
			while (rs.next()) {
				anos.add(rs.getInt("ano"));
			}
			Iterator<DadosGrafico> iterator = dados.iterator();
			String years;
			Double value;
			while (iterator.hasNext()) {
				dataset = new DefaultCategoryDataset();
				DadosGrafico dadosGrafico = iterator.next();
				for (int i = 0; i < anos.size(); ++i) {
					int year = anos.get(i);
					rs = query
							.query("Select o.`nome`, count(*) parcial from `"
									+ util.getNameBD()
									+ "`.`pesquisa` p, `"
									+ util.getNameBD()
									+ "`.`opt_ask` o, `"
									+ util.getNameBD()
									+ "`.`questionario` q, `"
									+ util.getNameBD()
									+ "`.`departamento` d where (p.`active`='1') and (p.`questionario`=q.`idquest`) and (p.`pergunta`='"
									+ dadosGrafico.getId()
									+ "') and (year(p.`data`)='"
									+ year
									+ "') and (p.`opcoes`=o.`idoask`) and (p.`departamento`=d.`iddepto`) "
									+ opt + " group by o.`nome`;");
					while (rs.next()) {
						years = year + "";
						value = rs.getDouble("parcial");
						dataset.addValue(value, rs.getString("nome"), years);
					}
				}
				JFreeChart chart = ChartFactory.createBarChart3D(
						dadosGrafico.getTitulo(), "Ano", "Respostas", dataset,
						PlotOrientation.VERTICAL, true, true, false);
				chart.setBackgroundPaint(Color.white);
				chart.setAntiAlias(true);
				chart.setBorderVisible(false);
				CategoryPlot plot = chart.getCategoryPlot();
				plot.setForegroundAlpha(0.7f);
				plot.setBackgroundAlpha(0.3f);
				CategoryAxis axis = plot.getDomainAxis();
				axis.setCategoryLabelPositions(CategoryLabelPositions
						.createUpRotationLabelPositions(Math.PI / 8.0));
				CategoryItemRenderer renderer = plot.getRenderer();
				renderer.setItemLabelsVisible(true);
				BarRenderer r = (BarRenderer) renderer;
				r.setMaximumBarWidth(0.05);
				graficos.add(grafico(chart.createBufferedImage(555, 300)));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return graficos;
	}

	private Graficos grafico(BufferedImage graph) {
		Graficos grafico = new Graficos();
		grafico.setGrafico(graph);
		return grafico;
	}

	private DadosGrafico dados(String titulo, int id) {
		DadosGrafico dados = new DadosGrafico();
		dados.setTitulo(titulo);
		dados.setId(id);
		return dados;
	}

	private DadosPesquisa info(String nome, int ask) {
		DadosPesquisa dados = new DadosPesquisa();
		dados.setNome(nome);
		dados.setAsk(ask);
		return dados;
	}

	private DadosPesquisa info(String nome, int ask, Double[] colunas,
			int porcentage, Double total) {
		DadosPesquisa dados = new DadosPesquisa();
		dados.setNome(nome);
		dados.setAsk(ask);
		if (porcentage > 0) {
			dados.setCol1(String.format("%.2f", ((colunas[1]) / total) * 100)
					+ "%");
			dados.setCol2(String.format("%.2f", ((colunas[2]) / total) * 100)
					+ "%");
			dados.setCol3(String.format("%.2f", ((colunas[3]) / total) * 100)
					+ "%");
			dados.setCol4(String.format("%.2f", ((colunas[4]) / total) * 100)
					+ "%");
			dados.setCol5(String.format("%.2f", ((colunas[5]) / total) * 100)
					+ "%");
		} else {
			dados.setCol1(String.format("%.0f", colunas[1]));
			dados.setCol2(String.format("%.0f", colunas[2]));
			dados.setCol3(String.format("%.0f", colunas[3]));
			dados.setCol4(String.format("%.0f", colunas[4]));
			dados.setCol5(String.format("%.0f", colunas[5]));
		}
		return dados;
	}
	/*
	 * public String tempo(long start) { int secs = (int) start / 1000; int
	 * milesimal = (int) (start - (secs * 1000)); int[] ret = new int[3]; //
	 * calcula numero de horas, minutos, segundos e milesimos ret[0] = secs /
	 * 3600; secs = secs % 3600; ret[1] = secs / 60; secs = secs % 60; ret[2] =
	 * secs; String time = (ret[0] > 0) ? fill(ret[0], 2, 'a') + ":" : ""; time
	 * += fill(ret[1], 2, 'a') + ":" + fill(ret[2], 2, 'a') + "," +
	 * fill(milesimal, 3, 'a'); return time; }
	 * 
	 * public String fill(int num, int tam, char pos) { return
	 * fill(String.valueOf(num), "0", tam, pos); }
	 * 
	 * public String fill(String str, String ch, int tam, char pos) { String tmp
	 * = ""; int tamStr = str.length(); while (tamStr < tam) { tmp += ch;
	 * ++tamStr; } if (pos == 'a') { return tmp + str; } return str + tmp; }
	 */
}
