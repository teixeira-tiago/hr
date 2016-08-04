/**
 * 
 */
package utiliter;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

/**
 * @author Tiago A. Teixeira {@link tiago.a.teixeira@acm.org}
 *
 */
public class Moeda {

	private final BigDecimal valor;

	public Moeda() {
		valor = new BigDecimal("0.0");
	}

	public Moeda(Double valor) {
		this.valor = new BigDecimal(String.valueOf(valor));
	}

	public Moeda(String valor) {
		this.valor = new BigDecimal(String.valueOf(Double.parseDouble(valor)));
	}

	public Moeda(int valor) {
		this.valor = new BigDecimal(String.valueOf(valor));
	}

	public Moeda(BigDecimal valor) {
		this.valor = valor;
	}

	public Moeda add(Moeda add) {
		return new Moeda(valor.add(add.getValor()));
	}

	public Moeda sub(Moeda sub) {
		return new Moeda(valor.subtract(sub.getValor()));
	}

	public Moeda mult(Moeda mult) {
		return new Moeda(valor.multiply(mult.getValor()));
	}

	public Moeda div(Moeda div) {
		return new Moeda(valor.divide(div.getValor(), 2, RoundingMode.UP));
	}

	public Moeda pot(int pow) {
		return new Moeda(valor.pow(pow));
	}

	public BigDecimal getValor() {
		return valor;
	}

	public Boolean max(BigDecimal max) {
		return valor.compareTo(max) > 0;
	}

	public Boolean les(BigDecimal les) {
		return valor.compareTo(les) < 0;
	}

	public Boolean eql(BigDecimal eq) {
		return valor.compareTo(eq) == 0;
	}

	public Moeda desvio(ArrayList<Moeda> array) {
		Moeda aux, pow;
		BigDecimal total = new BigDecimal("0.0");
		BigDecimal left = new BigDecimal("1.0");
		BigDecimal right = new BigDecimal(array.size() - 1);
		Moeda avg = media(array);
		Iterator<Moeda> it = array.iterator();
		while (it.hasNext()) {
			aux = it.next();
			pow = aux.sub(avg);
			total = total.add(pow.pot(2).getValor());
		}
		left = left.divide(right);
		right = left.multiply(total);
		return new Moeda(sqrt(right));
	}

	public Moeda media(ArrayList<Moeda> array) {
		Moeda aux;
		BigDecimal total = new BigDecimal("0.0");
		Iterator<Moeda> it = array.iterator();
		while (it.hasNext()) {
			aux = it.next();
			total = total.add(aux.getValor());
		}
		aux = new Moeda(total);
		return aux.div(new Moeda(array.size()));
	}

	public Moeda mediana(ArrayList<Moeda> array) {
		ArrayList<Moeda> ordem = order(array);
		Moeda mediana;
		if (ordem.size() % 2 != 0) {
			int posicaoMediana = (ordem.size() + 1) / 2;
			posicaoMediana--; // nosso ArrayList começa do zero
			mediana = ordem.get(posicaoMediana);
		} else {
			int posicao1 = ordem.size() / 2;
			posicao1--; // nosso ArrayList começa do zero
			int posicao2 = posicao1 + 1;
			mediana = ordem.get(posicao1).add(ordem.get(posicao2))
					.div(new Moeda("2.0"));
		}
		return mediana;
	}

	public Moeda quartil(ArrayList<Moeda> array, int each) {
		ArrayList<Moeda> order = order(array);
		ArrayList<Moeda> portion = new ArrayList<Moeda>();
		Moeda quartil = new Moeda("0.0");
		int half = (int) Math.round(array.size() * 0.5);
		switch (each) {
		case 1:
			for (int i = 0; i < half; ++i) {
				portion.add(order.get(i));
			}
			quartil = mediana(portion);
			break;
		case 2:
			quartil = mediana(order);
			break;
		case 3:
			for (int i = (half - (array.size() % 2)); i < order.size(); ++i) {
				portion.add(order.get(i));
			}
			quartil = mediana(portion);
			break;
		}
		return quartil;
	}

	private BigDecimal sqrt(BigDecimal value) {
		BigDecimal x = new BigDecimal(Math.sqrt(value.doubleValue()));
		return x.add(new BigDecimal(value.subtract(x.multiply(x)).doubleValue()
				/ (x.doubleValue() * 2.0)));
	}

	private ArrayList<Moeda> order(ArrayList<Moeda> array) {
		ArrayList<String> temp = new ArrayList<String>();
		ArrayList<Moeda> ordem = new ArrayList<Moeda>();
		for (Moeda line : array) {
			temp.add(line.getValor().toString());
		}
		Collections.sort(temp);
		for (String line : temp) {
			ordem.add(new Moeda(line));
		}
		return ordem;
	}
}
