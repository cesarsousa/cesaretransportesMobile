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
import br.com.cesaretransportesMobile.domain.Cliente;

public class HttpCliente extends Http{

	/**
	 * Faz uma requisição post a servlet <code>AndroidClienteVisualizar</code> no servidor da aplicação.
	 * 
 	 * @param urlCliente a url (servidor + servlet) para a requisição
	 * @param params mapa dos parâmetros da requisição
	 * @return o cliente referente ao id parametrizado.
	 * @throws IOException 
	 */
	public Cliente getCliente(String urlCliente, Map<String, String> params) throws IOException {
		String queryParams = getQueryParams(params);	
		
		Cliente cliente = new Cliente();
		
		URL url = new URL(urlCliente);
		Log.i(LOGGER, urlCliente);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("POST");
		conn.setDoInput(true);
		conn.setDoOutput(true);
		conn.connect();

		OutputStream out = conn.getOutputStream();
		byte[] bytes = queryParams.getBytes("UTF8");
		out.write(bytes);
		out.flush();
		out.close();

		InputStream in = conn.getInputStream();
		DataInputStream dataIn = new DataInputStream(in);

		cliente.deserialize(dataIn);

		dataIn.close();
		in.close();		
		
		return cliente;
	}
	
	/**
	 * Faz uma requisição post a servlet <code>AndroidClienteVisualizarTodos</code> no servidor da aplicação.
	 * @param url urlCliente a url (servidor + servlet) para a requisição
	 * @return a listagem com todos os clientes cadastrados e pendente confirmação na base de dados.
	 * @throws IOException 
	 */
	public List<Cliente> downloadClientes(String url, Map<String, String> params) throws IOException {
			String queryParams = getQueryParams(params);
		
			URL u = new URL(url);
			HttpURLConnection conn = (HttpURLConnection) u.openConnection();
			conn.setRequestMethod("POST");
			conn.setDoInput(true);
			conn.setDoOutput(true);
			conn.connect();
			
			OutputStream out = conn.getOutputStream();
			byte[] bytes = queryParams.getBytes("UTF8");
			out.write(bytes);
			out.flush();
			out.close();
			
			InputStream in = conn.getInputStream();
			DataInputStream dataIn = new DataInputStream(in);
			
			// Leitura do total de clientes lidos na aplicação web
			int totalClientes = dataIn.readInt();
			List<Cliente> clientes = new ArrayList<Cliente>(totalClientes);
			for (int i = 0; i < totalClientes; i++) {
				Cliente cliente = new Cliente();				
				cliente.deserialize(dataIn);				
				clientes.add(cliente);
			}
			dataIn.close();
			in.close();
			
			return clientes;		
	}
}