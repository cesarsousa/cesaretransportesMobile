package br.com.cesaretransportesMobile.activity;

import br.com.cesaretransportesMobile.domain.Cliente;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

/**
 * <p>Tela admin</p>
 * <p>Tela de gerenciamento de erro na aplicação.</p>
 * 
 * @author cesar sousa
 *
 */
public class TelaErro extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tela_erro);	
		
		String mensagemErro = getIntent().getStringExtra("mensagemErro");
		final Cliente clienteAdm = (Cliente) getIntent().getSerializableExtra("clienteAdm");	
		
		TextView textoMensagemErro = (TextView) findViewById(R.id.telaErro_mensagemErro);
		textoMensagemErro.setText(mensagemErro + ". Possíveis causas desse erro. Problemas com o " +
				"servidor de email. Email de destino inválido ou inexistente. Erro no banco de dados.");
		
		Button btRetornar = (Button) findViewById(R.id.telaErro_btRetornar);
		btRetornar.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				finish();				
			}
		});
		
		Button btMenu = (Button) findViewById(R.id.telaErro_btMenu);
		btMenu.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				startActivity(new Intent(TelaErro.this, MenuAdmin.class).putExtra("clienteAdm", clienteAdm));							
			}
		});
		
		Button btLogin = (Button) findViewById(R.id.telaErro_btLogin);
		btLogin.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				startActivity(new Intent(TelaErro.this, Login.class));				
			}
		});		
	}
}