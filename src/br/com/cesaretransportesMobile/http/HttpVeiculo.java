package br.com.cesaretransportesMobile.http;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import br.com.cesaretransportesMobile.domain.Veiculo;

public class HttpVeiculo extends Http {

	/**
	 * Faz uma requisição post a servlet <code>AndroidVeiculoVisualizarTodos</code> no servidor da aplicação.
	 * @param url a url (servidor + servlet) para a requisição
	 * @return
	 * @throws IOException
	 */
	public List<Veiculo> downloadVeiculos(String url) throws IOException {

		URL u = new URL(url);
		HttpURLConnection connection = (HttpURLConnection) u.openConnection();
		connection.setRequestMethod("POST");
		connection.setDoInput(true);
		connection.setDoOutput(true);
		connection.connect();
		
		// escrita do id para efetuar a consulta no servidor
		OutputStream out = connection.getOutputStream();
		byte[] bytes = "".getBytes("UTF8");
		out.write(bytes);
		out.flush();
		out.close();

		InputStream in = connection.getInputStream();
		DataInputStream dataIn = new DataInputStream(in);

		// leitura do total de clientes lidos na aplicação web
		int totalVeiculos = dataIn.readInt();
		List<Veiculo> veiculos = new ArrayList<Veiculo>(totalVeiculos);
		for (int i = 0; i < totalVeiculos; i++) {
			Veiculo veiculo = new Veiculo();
			veiculo.deserialize(dataIn);
			veiculos.add(veiculo);
		}
		dataIn.close();
		in.close();
		return veiculos;
	}

}
