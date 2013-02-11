package br.com.cesaretransportesMobile.http;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.util.Log;
import br.com.cesaretransportesMobile.domain.Orcamento;

public class HttpOrcamento extends Http {

	/**
	 * Faz uma requisição post a servlet <code>AndroidOrcamentoVisualizarTodos</code> no servidor da aplicação.
	 * @param url a url (servidor + servlet) para a requisição.
	 * @return listagem com todos os orçamentos cadastrados na base de dados.
	 * @throws IOException
	 */
	public List<Orcamento> downloadOrcamentos(String url) throws IOException {
		
		URL u = new URL(url);
		HttpURLConnection conexao = (HttpURLConnection) u.openConnection();
		conexao.setRequestMethod("POST");
		conexao.setDoInput(true);
		conexao.setDoOutput(false);
		conexao.connect();
		
		InputStream in = conexao.getInputStream();
		DataInputStream dataIn = new DataInputStream(in);
		
		// leitura do total de orçamentos na aplicação web
		int totalOrcamentos = dataIn.readInt();
		List<Orcamento> orcamentos = new ArrayList<Orcamento>(totalOrcamentos);
		for (int i = 0; i < totalOrcamentos; i++) {
			Orcamento orcamento = new Orcamento();
			orcamento.deserialize(dataIn);
			orcamentos.add(orcamento);
		}
		
		dataIn.close();
		in.close();
		
		return orcamentos;
	}

	/**
	 * Faz uma requisição post a servlet <code>AndroidOrcamentosPorCliente</code> no servidor da aplicação.
	 * @param urlListarOrcamentos a url (servidor + servlet) para a requisição.
	 * @param idParams o id cliente a ser listado
	 * @return uma lista contendo os orçamentos referente ao cliente parametrizado.
	 * @throws IOException 
	 */
	public ArrayList<Orcamento> downloadOrcamentos(String urlListarOrcamentos, Map<String, String> idParams) throws IOException {
		
		String queryParams = getQueryParams(idParams);
		ArrayList<Orcamento> orcamentos = new ArrayList<Orcamento>();
		
		URL u = new URL(urlListarOrcamentos);
		Log.i(LOGGER, urlListarOrcamentos);
		HttpURLConnection conexao = (HttpURLConnection) u.openConnection();
		conexao.setRequestMethod("POST");
		conexao.setDoInput(true);
		conexao.setDoOutput(true);
		conexao.connect();
		
		// escrita do id para efetuar a consulta no servidor
		OutputStream out = conexao.getOutputStream();
		byte[] bytes = queryParams.getBytes("UTF8");
		out.write(bytes);
		out.flush();
		out.close();
		
		InputStream in = conexao.getInputStream();
		DataInputStream dataIn = new DataInputStream(in);
		
		// total de orçamentos lidos no servidor.
		int totalOrcamentos = dataIn.readInt();		
		for (int i = 0; i < totalOrcamentos; i++) {
			Orcamento orcamento = new Orcamento();
			orcamento.deserialize(dataIn);
			orcamentos.add(orcamento);
		}		
		dataIn.close();
		in.close();
		
		return orcamentos;	
	}

}
