package br.com.cesaretransportesMobile.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import br.com.cesaretransportesMobile.util.CesareUtil;
import br.com.cesaretransportesMobile.util.Sms;

/**
 * <p>Tela Admin</p>
 * <p>Classe utilizada pela activities DetalheCliente e DetalheOrcamento para
 * enviar um torpedo Sms para um cliente.</p>
 * 
 * @author cesar sousa
 */
public class EscreverMensagem extends Activity {
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.escrever_mesagem);
		
		final String telefone = getIntent().getStringExtra("telefone");
		final String nome = getIntent().getStringExtra("nome");		
		
		TextView textoNome = (TextView) findViewById(R.id.escreverMensagem_nome);
		textoNome.setText(nome);
		TextView textoTelefone = (TextView) findViewById(R.id.escreverMensagem_telefone);
		textoTelefone.setText(CesareUtil.formatarTelefone(telefone));
		
		Button btCancelar = (Button)findViewById(R.id.escreverMensagem_btCancelar);
		btCancelar.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				finish();				
			}
		});
		
		Button btEnviarSms = (Button)findViewById(R.id.escreverMensagem_btSms);
		btEnviarSms.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				Log.i("cetrans", "Enviando msg para o fone: " + telefone);
				EditText texto = (EditText)findViewById(R.id.escreverMensagem_texto);
				String mensagem = texto.getText().toString();
				if("".equals(mensagem)){
					Toast.makeText(
							EscreverMensagem.this, "Por favor digite sua mensagem !", Toast.LENGTH_SHORT).show();
				}else{
					Sms sms = new Sms();
					sms.enviarSms(EscreverMensagem.this, telefone, mensagem);
					Toast.makeText(EscreverMensagem.this, "Mensagem enviada com sucesso !", Toast.LENGTH_LONG).show();
					finish();
				}				
			}
		});		
	}
}
