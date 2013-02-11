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
import android.widget.TextView;
import android.widget.Toast;
import br.com.cesaretransportesMobile.domain.Cliente;
import br.com.cesaretransportesMobile.http.Http;
import br.com.cesaretransportesMobile.http.HttpCliente;
import br.com.cesaretransportesMobile.util.CesareUtil;
import br.com.cesaretransportesMobile.util.Parametros;
import br.com.cesaretransportesMobile.util.Servlet;

public class RecadastrarCliente extends Activity implements Runnable {
	
	private static final String URL_REATIVAR_CONTA = Http.SERVIDOR + Servlet.androidRecadastrarCliente;
	
	private Cliente cliente;
	private ProgressDialog dialog;
	private Handler handler = new Handler();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.recadastrar_cliente);
		
		cliente = (Cliente) getIntent().getSerializableExtra("cliente");
		
		TextView textoNome = (TextView) findViewById(R.id.recadastrarCliente_nome);
		textoNome.setText("Nome ou Empresa: " + cliente.getNome());
		
		TextView textoEmail = (TextView) findViewById(R.id.recadastrarCliente_email);
		textoEmail.setText("Email: " + cliente.getEmail());
		
		TextView textoDocumento = (TextView) findViewById(R.id.recadastrarCliente_documento);
		textoDocumento.setText("Documento: " + cliente.getTipoDoDocumento().toString().toUpperCase() + " - " + CesareUtil.formatarDocumento(cliente.getTipoDoDocumento(), cliente.getNumeroDoDocumento()));
		
		TextView textoTelefone = (TextView) findViewById(R.id.recadastrarCliente_telefone);
		textoTelefone.setText("Telefone: " + cliente.getTelefone().formatado());
		
		Button btCancelar = (Button) findViewById(R.id.recadastrarCliente_btCancelar);
		btCancelar.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				startActivity(new Intent(RecadastrarCliente.this, Login.class));				
			}
		});
		
		Button btReativar = (Button) findViewById(R.id.recadastrarCliente_btReativar);
		btReativar.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {				
				// abre a caixa de dialogo da solicitação
				dialog = ProgressDialog.show(RecadastrarCliente.this, "Cesare Transportes", "Enviando solicitação, por favor aguarde...", false, true);
				
				// delega para outra Thread o acesso ao servidor
				new Thread(RecadastrarCliente.this).start();
			}
		});	
	}

	@Override
	public void run() {
		try {
			final HttpCliente httpCliente = new HttpCliente();
			final String resultado = httpCliente.doPost(URL_REATIVAR_CONTA, Parametros.getEmailParams(cliente.getEmail()));
			handler.post(new Runnable() {
				public void run() {
					if(resultado == null){
						Toast.makeText(RecadastrarCliente.this, "Erro. Verifique se o banco de dados esta ativo", Toast.LENGTH_LONG).show();
						finish();
					}
					
					if(resultado.startsWith("ERRO")){
						Intent intent = new Intent(RecadastrarCliente.this, TelaErroCliente.class);
						intent.putExtra("mensagemErro", resultado);
						startActivity(intent);
					}else{
						String mensagem = "Sua conta foi reativada com sucesso. Aguarde que em breve enviaremos um email de confirmação a sua solicitação.";
						startActivity(new Intent(RecadastrarCliente.this, RespostaRecuperarSenha.class).putExtra("mensagem", mensagem));
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