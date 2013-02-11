package br.com.cesaretransportesMobile.http;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;
import java.util.Map;

import android.util.Log;

public abstract class Http {	
	//public static final String SERVIDOR = "http://www.cesaretransportes.com.br/";
	public static final String SERVIDOR = "http://10.0.2.2:8080//cesaretransportes/";
	public static final String LOGGER = "cetrans";

	/**
	 * Realiza uma requisição doPost com o servidor da aplicação.
	 * 
	 * @param url url para realizar a requisição
	 * @param params parâmetros para reescrita da url
	 * @return
	 */
	public String doPost(String url, Map<String, String> params) {
		String queryParams = getQueryParams(params);
		String texto = doPost(url, queryParams);
		return texto;		
	}

	private String doPost(String url, String params) {		
		try {
			
			URL u = new URL(url);
			Log.i(LOGGER, "Http#doPost " + url);
			HttpURLConnection conn = (HttpURLConnection) u.openConnection();
			conn.setRequestMethod("POST");
			conn.setDoInput(true);
			conn.setDoOutput(true);			
			conn.connect();			
			
			OutputStream out = conn.getOutputStream();
			byte[] bytes = params.getBytes("UTF8");
			out.write(bytes);
			out.flush();
			out.close();
			
			InputStream in = conn.getInputStream();
			String texto = readString(in);
			conn.disconnect();
			return texto;
			
		} catch (MalformedURLException e) {
			Log.e(LOGGER, e.getMessage(), e);
			e.printStackTrace();
		} catch (IOException e) {
			Log.e(LOGGER, e.getMessage(), e);
			e.printStackTrace();
		}
		
		return null;
	}

	private String readString(InputStream in) throws IOException {
		/*
		 * faz a leitura do texto da input stream retornada
		 */
		byte[] bytes = readBytes(in);
		String texto = new String(bytes);
		return texto;
	}

	private byte[] readBytes(InputStream in) throws IOException {
		/*
		 * faz a leitura do array de bytes da input stream retornada.
		 */
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		try{
			byte[] buffer = new byte[1024];
			int len;
			while((len = in.read(buffer)) > 0){
				bos.write(buffer, 0, len);
			}
			byte[] bytes = bos.toByteArray();
			return bytes;
		}finally{
			bos.close();
			in.close();
		}
	}

	protected String getQueryParams(Map<String, String> params) {
		if (params == null || params.size() == 0){
			return null;
		}
		
		/*
		 * escreve a query de parametros ex. chave=valor&chave=valor...
		 */
		String urlParams = null;
		Iterator<String> iterator = params.keySet().iterator();
		while(iterator.hasNext()){
			String chave = iterator.next();
			String valor = params.get(chave).toString();
			urlParams = urlParams == null ? "" : urlParams + "&";
			urlParams += chave + "=" + valor;
		}
		
		return urlParams;
	}
	
	/*public void doPost(String url) {
		doPost(url, "");		
	}*/
}
