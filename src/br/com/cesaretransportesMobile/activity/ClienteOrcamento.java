package br.com.cesaretransportesMobile.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import br.com.cesaretransportesMobile.domain.Cliente;
import br.com.cesaretransportesMobile.http.Http;
import br.com.cesaretransportesMobile.http.HttpOrcamento;
import br.com.cesaretransportesMobile.util.Parametros;
import br.com.cesaretransportesMobile.util.Servlet;

public class ClienteOrcamento extends Activity implements Runnable{
	
	private static final String CADASTRAR_ORCAMENTO = Http.SERVIDOR + Servlet.androidOrcamentoCadastrar;
	
	private Handler handler = new Handler();
	private ProgressDialog dialog;
	private AutoCompleteTextView campoEstadoDestino;
	private AutoCompleteTextView campoEstadoOrigem;
	private Cliente cliente;

	protected String cidadeOrigem;
	protected String estadoOrigem;
	protected String enderecoOrigem;
	protected String cidadeDestino;
	protected String estadoDestino;
	protected String enderecoDestino;
	protected String peso;
	protected String dimensao;
	protected String mensagem;
			
	private static final String[] ESTADOS = new String[]{
		"AC", "AL", "AP", "AM", "BA", "CE", "DF", "ES", "GO", "MA", "MT", "MS",	"MG", "PA",
		"PB", "PR", "PE", "PI", "RN", "RS", "RJ", "RO", "RR", "SC", "SP", "SE", "TO"
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		cliente = (Cliente) getIntent().getSerializableExtra("cliente");		
		setContentView(R.layout.cliente_orcamento);
				
		TextView textoNome = (TextView)findViewById(R.id.clienteOrcamento_nome);
		textoNome.setText(cliente.getNome());
		
		TextView textoEmail = (TextView)findViewById(R.id.clienteOrcamento_email);
		textoEmail.setText(cliente.getEmail());
		
		TextView textoTelefone = (TextView)findViewById(R.id.clienteOrcamento_telefone);
		textoTelefone.setText(cliente.getTelefone().formatado());
		
		/*
		 * adaptador para mostrar o auto complete do estado
		 */
		ArrayAdapter<String> adaptador = 
			new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, ESTADOS);
		
		campoEstadoOrigem = (AutoCompleteTextView)findViewById(R.id.clienteOrcamento_estadoOrigem);
		campoEstadoOrigem.setAdapter(adaptador);
		
		campoEstadoDestino = (AutoCompleteTextView) findViewById(R.id.clienteOrcamento_estadoDestino);
		campoEstadoDestino.setAdapter(adaptador);
		
		Button btVoltar = (Button)findViewById(R.id.clienteOrcamento_btVoltar);
		btVoltar.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				finish();				
			}
		});
		
		Button btEnviar = (Button)findViewById(R.id.clienteOrcamento_btEnviar);
		btEnviar.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				EditText campoCidadeOrigem = (EditText)findViewById(R.id.clienteOrcamento_cidadeOrigem);
				cidadeOrigem = campoCidadeOrigem.getText().toString().trim();
				if ("".equals(cidadeOrigem)){
					alerta("cidade origem");
					return;
				}			
				
				estadoOrigem = campoEstadoDestino.getText().toString();
				if("".equals(estadoOrigem)){
					alerta("estado origem");
					return;
				}
				
				EditText campoEnderecoOrigem = (EditText) findViewById(R.id.clienteOrcamento_enderecoOrigem);
				enderecoOrigem = campoEnderecoOrigem.getText().toString().trim();
				if ("".equals(enderecoOrigem)){
					alerta("endereço origem");
					return;
				}
				
				EditText campoCidadeDestino = (EditText)findViewById(R.id.clienteOrcamento_cidadeDestino);
				cidadeDestino = campoCidadeDestino.getText().toString();
				if("".equals(cidadeDestino)){
					alerta("cidade destino");
					return;
				}
				
				estadoDestino = campoEstadoDestino.getText().toString();
				if("".equals(estadoDestino)){
					alerta("estado destino");
					return;
				}
				
				EditText campoEnderecoDestino = (EditText) findViewById(R.id.clienteOrcamento_enderecoDestino);
				enderecoDestino = campoEnderecoDestino.getText().toString().trim();
				if ("".equals(enderecoDestino)){
					alerta("endereço destino");
					return;
				}
				
				EditText campoPeso = (EditText)findViewById(R.id.clienteOrcamento_peso);
				peso = campoPeso.getText().toString();
				if("".equals(peso)){
					alerta("peso");
					return;
				}
				
				EditText campoDimensao = (EditText)findViewById(R.id.clienteOrcamento_dimensao);
				dimensao = campoDimensao.getText().toString();
				if("".equals(dimensao)){
					alerta("dimensao");
					return;
				}
				
				EditText campoMensagem = (EditText)findViewById(R.id.clienteOrcamento_mensagem);
				mensagem = campoMensagem.getText().toString();			
				
				dialog = ProgressDialog.show(ClienteOrcamento.this, "Cesare Transporte", "Enviando orçamento, por favor aguarde...", false, true);
				new Thread(ClienteOrcamento.this).start();							
			}		
		});		
	}
	
	private void alerta(String campo) {
		Toast.makeText(this, "O campo " + campo + " deve ser preenchido.", Toast.LENGTH_LONG).show();		
	}

	@Override
	public void run() {
		try {
			HttpOrcamento http = new HttpOrcamento();
			final String resultado = http.doPost(
					CADASTRAR_ORCAMENTO, Parametros.getCadastrarOrcamentosParams(
							cliente.getIdCliente(), 
							cidadeOrigem, estadoOrigem, enderecoOrigem,
							cidadeDestino, estadoDestino, enderecoDestino,
							peso, dimensao, mensagem));
			
			handler.post(new Runnable() {
				public void run() {
					if(resultado.startsWith("ERRO")){
						Intent intent = new Intent(ClienteOrcamento.this, TelaErroCliente.class);
						intent.putExtra("mensagemErro", resultado);
						startActivity(intent);
					}else{
						Toast.makeText(ClienteOrcamento.this, resultado, Toast.LENGTH_SHORT).show();
						Intent intent = new Intent(ClienteOrcamento.this, OpcaoOrcamento.class);
						intent.putExtra("cliente", cliente);
						startActivity(intent);
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