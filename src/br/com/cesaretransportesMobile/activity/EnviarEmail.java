package br.com.cesaretransportesMobile.activity;

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
import android.widget.TextView;
import android.widget.Toast;
import br.com.cesaretransportesMobile.http.Http;
import br.com.cesaretransportesMobile.http.HttpCliente;
import br.com.cesaretransportesMobile.util.Parametros;
import br.com.cesaretransportesMobile.util.Servlet;

/**
 * <p>Tela admin</p>
 * <p>Classe utilizada pelas activities DetalheCliente para enviar um email.</p>
 */
public class EnviarEmail extends Activity implements Runnable{
	
	private static final String ENVIAR_EMAIL = Http.SERVIDOR + Servlet.androidEnviarEmail;
	private Handler handler = new Handler();
	private ProgressDialog dialog;
	
	private String email;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.enviar_email);	
			
		String nome = getIntent().getStringExtra("nome");
		email = getIntent().getStringExtra("email");
		
		TextView textoNome = (TextView) findViewById(R.id.enviarEmail_nome);
		textoNome.setText(nome);
		TextView textoEmail = (TextView) findViewById(R.id.enviarEmail_email);
		textoEmail.setText(email);
		
		Button btCancelar = (Button) findViewById(R.id.enviarEmail_btCancelar);
		btCancelar.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				finish();				
			}
		});
		
		Button btEnviar = (Button) findViewById(R.id.enviarEmail_btEnviar);
		btEnviar.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				EditText campoMensagem = (EditText) findViewById(R.id.enviarEmail_mensagem);
				String mensagem = campoMensagem.getText().toString().trim();
				
				EditText campoAssunto = (EditText) findViewById(R.id.enviarEmail_assunto);
				String assunto = campoAssunto.getText().toString().trim();
				
				if(assunto.equals("")){
					Toast.makeText(EnviarEmail.this, "Por favor digite o assunto.", Toast.LENGTH_SHORT).show();
					return;
				}
				
				if(mensagem.equals("")){
					Toast.makeText(EnviarEmail.this, "Por favor digite a mensagem.", Toast.LENGTH_SHORT).show();
					return;
				}
				
				// abre a tela de progresso e roda a solicitação em outra tela
				dialog = ProgressDialog.show(EnviarEmail.this, "Cesare Transportes", "Enviando solicitação, por favor aguarde...", false, true);
				new Thread(EnviarEmail.this).start();			
			}
		});
	}

	@Override
	public void run() {
		try {
			EditText campoMensagem = (EditText) findViewById(R.id.enviarEmail_mensagem);
			String mensagem = campoMensagem.getText().toString().trim();
			EditText campoAssunto = (EditText) findViewById(R.id.enviarEmail_assunto);
			String assunto = campoAssunto.getText().toString().trim();
			HttpCliente httpCliente = new HttpCliente();
			final String resposta = httpCliente.doPost(ENVIAR_EMAIL, 
					Parametros.getEnviarEmailParams(email, assunto, mensagem));
			
			handler.post(new Runnable() {
				public void run() {
					if(resposta.startsWith("ERRO")){						
						String mensagemErro = resposta.substring(5);
						Intent intent = new Intent(EnviarEmail.this, TelaErro.class);						
						intent.putExtra("mensagemErro", mensagemErro);
						startActivity(intent);					
					}else{
						Toast.makeText(EnviarEmail.this, "Email enviado com sucesso.", Toast.LENGTH_LONG).show();
						finish();
					}					
				}
			});
			
		} catch (Throwable e) {
			Log.e(Http.LOGGER, e.getMessage(), e);
			Toast.makeText(this, "Ocorreu um erro durante sua solicitação.", Toast.LENGTH_LONG).show();
			finish();
		} finally {
			dialog.dismiss();
		}		
	}
}