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
import br.com.cesaretransportesMobile.domain.Endereco;

public class HttpEndereco extends Http {

	/**
	 * Faz uma requisição post a servlet <code>AndroidEnderecosPorOrcamento</code> no servidor da aplicação.
	 * @param urlListarEnderecosOrcamento a url (servidor + servlet) para a requisição.
	 * @param idParams o id do orçamentos cujos endereços devem ser listados
	 * @return uma listagem dos endereços de um orçamento.
	 * @throws IOException 
	 */
	public ArrayList<Endereco> downloadEnderecos(String urlListarEnderecosOrcamento, Map<String, String> idParams) throws IOException {
		String queryParams = getQueryParams(idParams);
		ArrayList<Endereco> enderecos = new ArrayList<Endereco>();
		
		URL u = new URL(urlListarEnderecosOrcamento);
		Log.i(LOGGER, urlListarEnderecosOrcamento);
		
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
		
		int totalEnderecos = dataIn.readInt();
		
		for(int i = 0; i< totalEnderecos;i++){
			Endereco endereco = new Endereco();
			endereco.deserialize(dataIn);
			enderecos.add(endereco);
		}
		
		dataIn.close();
		in.close();
		
		return enderecos;
	}
	
	

}
