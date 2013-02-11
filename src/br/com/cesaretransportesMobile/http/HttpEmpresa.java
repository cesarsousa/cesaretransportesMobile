package br.com.cesaretransportesMobile.http;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import br.com.cesaretransportesMobile.domain.Empresa;

public class HttpEmpresa extends Http{

	/**
	 * Faz uma requisição post a servlet <code>AndroidEmpresaVisualizar</code> no servidor da aplicação.
	 * @param urlEmpresa
	 * @return
	 * @throws IOException 
	 */
	public Empresa getEmpresa(String urlEmpresa) throws IOException {
		
		URL url = new URL(urlEmpresa);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("POST");
		conn.setDoInput(true);
		conn.setDoOutput(false);
		conn.connect();
		
		InputStream in = conn.getInputStream();
		DataInputStream dataIn = new DataInputStream(in);
		
		Empresa empresa = new Empresa();
		empresa.deserialize(dataIn);
		
		dataIn.close();
		in.close();
		
		return empresa;
	}

}
