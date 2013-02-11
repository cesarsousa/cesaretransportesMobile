package br.com.cesaretransportesMobile.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import br.com.cesaretransportesMobile.http.Http;
import br.com.cesaretransportesMobile.http.HttpCliente;
import br.com.cesaretransportesMobile.util.Parametros;
import br.com.cesaretransportesMobile.util.Servlet;

public class TelaErroCliente extends Activity implements Runnable {
	
	private static final String ENVIAR_EMAIL = Http.SERVIDOR + Servlet.androidEnviarEmail;
	private ProgressDialog dialog;
	private String mensagemErro;
	private Handler handler = new Handler();
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tela_erro_cliente);
		
			
		mensagemErro = getIntent().getStringExtra("mensagemErro");
		
		String texto = "Ajude-nos a resolver este problema relatando-nos deste " +
				"erro. Agradecemos sua compreensão e desculpe-nos pelo transtorno.";
		
		TextView textoMensagem = (TextView) findViewById(R.id.telaErroCliente_mensagem);
		textoMensagem.setText(texto);
		
		Button btOk = (Button) findViewById(R.id.telaErroCliente_btOK);
		btOk.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				finish();				
			}
		});
		
		Button btRelatarErro = (Button) findViewById(R.id.telaErroCliente_btRelatarErro);
		btRelatarErro.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				dialog = ProgressDialog.show(TelaErroCliente.this, "Cesare Transportes", "Enviando notificação de erro, por favor aguarde...", false, true);
				new Thread(TelaErroCliente.this).start();				
			}
		});
	}

	@Override
	public void run() {
		try {
			HttpCliente httpCliente = new HttpCliente();
			httpCliente.doPost(ENVIAR_EMAIL, Parametros.getEnviarEmailParams(
					"cesarsonline@gmail.com", "Cetrans - Erro de execucao", mensagemErro));
			
			handler.post(new Runnable() {
				public void run() {
					Toast.makeText(TelaErroCliente.this, "O portal Cesare Transportes agradeçe sua " +
							"atenção. Sua notificação foi recebida com sucesso.", Toast.LENGTH_LONG).show();
					finish();
					
				}
			});
		}catch (Throwable e) {
			Log.e(Http.LOGGER, e.getMessage(), e);
			Toast.makeText(this, "Ocorreu um erro durante sua solicitação.", Toast.LENGTH_LONG).show();
			finish();
		} finally {
			dialog.dismiss();
		}
	}

}
