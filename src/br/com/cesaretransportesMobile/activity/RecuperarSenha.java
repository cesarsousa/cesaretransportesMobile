package br.com.cesaretransportesMobile.activity;

import br.com.cesaretransportesMobile.http.Http;
import br.com.cesaretransportesMobile.http.HttpLogin;
import br.com.cesaretransportesMobile.util.Parametros;
import br.com.cesaretransportesMobile.util.Servlet;
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

public class RecuperarSenha extends Activity implements OnClickListener, Runnable{
	
	private static final String RECUPERAR_SENHA = Http.SERVIDOR + Servlet.androidRecuperarSenha;
	private Handler handler = new Handler();
	private ProgressDialog dialog;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.recuperar_senha);
		
		TextView texto = (TextView) findViewById(R.id.recuperarSenha_texto);
		texto.setText(getTextoDaTela());
		
		Button btEnviar = (Button) findViewById(R.id.recuperarSenha_btEnviar);
		btEnviar.setOnClickListener(this);
		
	}

	@Override
	public void onClick(View v) {
		EditText campoEmail = (EditText) findViewById(R.id.recuperarSenha_campoEmail);
		String email = campoEmail.getText().toString().trim();
		if("".equals(email)){
			Toast.makeText(this, "Por favor digite a sua mensagem.", Toast.LENGTH_SHORT).show();
			return;
		}
		
		// abre a tela de progresso e roda solicitação em outra thread
		dialog = ProgressDialog.show(this, "Cesare Transportes", "Enviando solicitação, por favor aguarde...", false, true);
		new Thread(this).start();		
	}

	private String getTextoDaTela() {
		return "Para efetuar a recuperação da sua senha, digite o email utilizado no seu cadastro.";
	}

	@Override
	public void run() {
		try {
			EditText campoEmail = (EditText) findViewById(R.id.recuperarSenha_campoEmail);
			String email = campoEmail.getText().toString().trim();
			HttpLogin httpLogin = new HttpLogin();
			final String resposta = httpLogin.doPost(RECUPERAR_SENHA, Parametros.getEmailParams(email));			
			
			handler.post(new Runnable() {
				public void run() {
					if(resposta.startsWith("EMAILINVALIDO")){
						Toast.makeText(RecuperarSenha.this, resposta.substring(5), Toast.LENGTH_LONG).show();						
					} else if(resposta.startsWith("ERRO")){
						Intent intent = new Intent(RecuperarSenha.this, TelaErroCliente.class);
						intent.putExtra("mensagemErro", resposta);
						startActivity(intent);
					} else{
						String mensagem = "Uma mensagem foi enviada ao seu email, por favor verifique sua caixa de entrada ou sua caixa de spam para recuperar sua senha.";
						startActivity(new Intent(RecuperarSenha.this, RespostaRecuperarSenha.class).putExtra("mensagem", mensagem));
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