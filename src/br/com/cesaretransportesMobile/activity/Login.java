package br.com.cesaretransportesMobile.activity;

import java.io.IOException;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import br.com.cesaretransportesMobile.domain.Cliente;
import br.com.cesaretransportesMobile.http.Http;
import br.com.cesaretransportesMobile.http.HttpCliente;
import br.com.cesaretransportesMobile.util.Parametros;
import br.com.cesaretransportesMobile.util.Servlet;

public class Login extends Activity implements Runnable{
	
	
	private static final String URL_LOGIN = Http.SERVIDOR + Servlet.androidLoginServlet;
	private static final String URL_GET_CLIENTE = Http.SERVIDOR + Servlet.androidClienteVisualizar;	
	private static final String MENSAGEM = "Erro para efetuar a conexão com Cesare Transportes.";
	
	private ProgressDialog dialog;
	private Handler handler = new Handler();	
	private String usuario;
	private String senha;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        Button btLogin = (Button)findViewById(R.id.main_btLogin);
        btLogin.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				EditText campoUsuario = (EditText)findViewById(R.id.main_campoUsuario);
				EditText campoSenha = (EditText)findViewById(R.id.main_campoSenha);
				usuario = campoUsuario.getText().toString().trim();
				senha = campoSenha.getText().toString().trim();	
				
				if("".equals(usuario) || "".equals(senha)){
					Toast.makeText(Login.this, "Por favor insira seu usuário e sua senha.", Toast.LENGTH_SHORT).show();
					return;
				}				
				
				// abre caixa de dialogo de download
				dialog = ProgressDialog.show(Login.this, "Cesare Transportes", "Efetuando conexão, por favor aguarde...", false, true);
				
				// delega para outra thread o acesso ao servidor.
				new Thread(Login.this).start();
			}			
        });	
        
        Button btRecSenha = (Button) findViewById(R.id.main_btRecSenha);
        btRecSenha.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				startActivity(new Intent(Login.this, RecuperarSenha.class));				
			}			
		});       
    }
    
    public void run() {    	
    	try {   			
			
			final HttpCliente httpCliente = new HttpCliente();
			
			final String resultado = httpCliente.doPost(URL_LOGIN, Parametros.getLoginParams(usuario, senha));
    		handler.post(new Runnable() {
				public void run() {				
					if(resultado == null){
						Toast.makeText(Login.this, "Erro. Verifique se o banco de dados esta ativo", Toast.LENGTH_LONG).show();
						finish();
					}
					if(resultado.startsWith("ERRO")){
						Intent intent = new Intent(Login.this, TelaErroCliente.class);
						intent.putExtra("mensagemErro", resultado);
						startActivity(intent);
					}else{
						int id = Integer.parseInt(resultado);
						if(id == 0){
							Toast.makeText(Login.this, "Usuário Inválido.", Toast.LENGTH_LONG).show();			
						}else{
							try {								
								Cliente cliente = httpCliente.getCliente(URL_GET_CLIENTE, Parametros.getLoginParams(usuario, senha));
															
								if(cliente.getTipoCliente().equals("A")){
									Intent intent = new Intent(Login.this, MenuAdmin.class);
									intent.putExtra("clienteAdm", cliente);
									startActivity(intent);
								}else{
									if(cliente.getDataExclusao() != null){
										Intent intent = new Intent(Login.this, RecadastrarCliente.class);
										intent.putExtra("cliente", cliente);
										startActivity(intent);										
									}else if(cliente.isStatusCliente()){
										Intent intent = new Intent(Login.this, MenuCliente.class);
										intent.putExtra("cliente", cliente);										
										startActivity(intent);
									}else{
										String Mensagem = "Olá " + cliente.getNome() + " seu cadastro ainda aguarda confirmação.";
										Toast.makeText(Login.this, Mensagem, Toast.LENGTH_LONG).show();					
									}
								}
							} catch (IOException e) {
								Toast.makeText(Login.this, MENSAGEM, Toast.LENGTH_LONG).show();
								Log.e(Http.LOGGER, e.getMessage(), e);
							}
						}						
					}										
				}
			});			
		} catch (Throwable e) {
			Log.e(Http.LOGGER, e.getMessage(), e);
			Toast.makeText(this, "Ocorreu um erro durante sua solicitação.", Toast.LENGTH_LONG).show();
			finish();
		} finally{
			dialog.dismiss();
		}    	
	}
}