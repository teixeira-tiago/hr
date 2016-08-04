/**
 * 
 */
package utiliter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * @author Tiago A. Teixeira {@link tiago.a.teixeira@acm.org}
 *
 */
public class TextFile {

	private ArrayList<String> body;

	public TextFile() {

	}

	public TextFile(String arquivo) throws IOException {
		abrir(arquivo);
	}

	public void abrir(String arquivo) throws IOException {
		FileReader fileReader = new FileReader(new File(arquivo));
		BufferedReader reader = new BufferedReader(fileReader);
		String data = "";
		body = new ArrayList<>();
		while ((data = reader.readLine()) != null) {
			body.add(data);
		}
		fileReader.close();
		reader.close();
	}

	public void salvar(String arquivo) throws IOException {
		FileWriter fw = new FileWriter(new File(arquivo));
		Iterator<String> it = body.iterator();
		while (it.hasNext()) {
			fw.write(it.next() + "\r\n");
		}
		fw.flush();
		fw.close();
	}

	public void add(String texto, int linha) {
		body.add(linha, texto);
	}

	public void add(String texto) {
		body.add(texto);
	}

	public ArrayList<String> conteudo() {
		return body;
	}
}
