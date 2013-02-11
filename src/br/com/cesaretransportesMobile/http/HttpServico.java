package br.com.cesaretransportesMobile.http;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Map;

import android.util.Log;
import br.com.cesaretransportesMobile.domain.Servico;

public class HttpServico extends Http {

	/**
	 * Faz uma requisição post a servlet <code>AndroidServicosPorCliente</code>
	 * no servidor de aplicação.
	 * 
	 * @param urlListarServicos
	 *            a url (servidor + servlet) para a requisição.
	 * @param idParams
	 *            o id cliente a ser consultado
	 * @return todos os serviços ativos do cliente, ou seja,
	 *         serviços que não foram entregues.
	 * @throws IOException 
	 */
	public ArrayList<Servico> downloadServicosPorCliente(String urlListarServicos, Map<String, String> idParams) throws IOException {

		String queryParams = getQueryParams(idParams);
		ArrayList<Servico> servicos = new ArrayList<Servico>();
		
		URL u = new URL(urlListarServicos);
		Log.i(LOGGER, urlListarServicos);
		HttpURLConnection conexao = (HttpURLConnection) u.openConnection();
		conexao.setRequestMethod("POST");
		conexao.setDoInput(true);
		conexao.setDoOutput(true);
		conexao.connect();
		
		// escrita dos parâmetros da requisição
		OutputStream out = conexao.getOutputStream();
		byte[] bytes = queryParams.getBytes("UTF8");
		out.write(bytes);
		out.flush();
		out.close();
		
		InputStream in = conexao.getInputStream();
		DataInputStream dataIn = new DataInputStream(in);
		
		int totalServicos = dataIn.readInt();
		for (int i = 0; i < totalServicos; i++) {
			Servico servico = new Servico();
			servico.deserialize(dataIn);
			servicos.add(servico);
		}
		dataIn.close();
		in.close();
		
		return servicos;
	}

	/**
	 * Faz uma requisição post a servlet <code>AndroidOrcamentosPorVeiculo</code>
	 * no servidor de aplicação.
	 * 
	 * @param urlServicos
	 *            a url (servidor + servlet) para a requisição.
	 * @param params
	 *            o id veículo a ser consultado
	 * @return todos os serviços  ativos do veículo, ou seja,
	 *         serviços que não excluídos.
	 * @throws IOException 
	 */
	public ArrayList<Servico> downloadServicosPorVeiculo(String urlServicos, Map<String, String> params) throws IOException {
		String queryParams = getQueryParams(params);
		ArrayList<Servico> servicos = new ArrayList<Servico>();
		
		URL u = new URL(urlServicos);
		Log.i(LOGGER, urlServicos);
		HttpURLConnection conexao = (HttpURLConnection) u.openConnection();
		conexao.setRequestMethod("POST");
		conexao.setDoInput(true);
		conexao.setDoOutput(true);
		conexao.connect();
		
		// escrita dos parâmetros da requisição
		OutputStream out = conexao.getOutputStream();
		byte[] bytes = queryParams.getBytes("UTF8");
		out.write(bytes);
		out.flush();
		out.close();
		
		InputStream in = conexao.getInputStream();
		DataInputStream dataIn = new DataInputStream(in);
		
		int totalServicos = dataIn.readInt();
		for (int i = 0; i < totalServicos; i++) {
			Servico servico = new Servico();
			servico.deserialize(dataIn);
			servicos.add(servico);
		}
		dataIn.close();
		in.close();
		
		return servicos;
	}
}
