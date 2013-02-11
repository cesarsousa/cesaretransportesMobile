package br.com.cesaretransportesMobile.activity;

import java.io.IOException;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;
import br.com.cesaretransportesMobile.domain.Cliente;
import br.com.cesaretransportesMobile.domain.Cliente.TipoDoDocumento;
import br.com.cesaretransportesMobile.http.Http;
import br.com.cesaretransportesMobile.http.HttpCliente;
import br.com.cesaretransportesMobile.util.Parametros;
import br.com.cesaretransportesMobile.util.Servlet;

public class ClienteAlterarDados extends Activity{
	
	private static final String URL_ALTERAR_DADOS = Http.SERVIDOR + Servlet.androidClienteAlterarDados;
	private static final String URL_GET_CLIENTE = Http.SERVIDOR + Servlet.androidClienteVisualizar;
	private static final String MENSAGEM = "Erro para efetuar a conexão com Cesare Transportes.";
	private Cliente cliente;
	
	private AutoCompleteTextView enderecoEstado;
	private static final String[] ESTADOS = new String[]{
		"AC", "AL", "AP", "AM", "BA", "CE", "DF", "ES", "GO", "MA", "MT", "MS",	"MG", "PA",
		"PB", "PR", "PE", "PI", "RN", "RS", "RJ", "RO", "RR", "SC", "SP", "SE", "TO"
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);		
		setContentView(R.layout.cliente_alterar_dados);
		
		cliente = (Cliente) getIntent().getSerializableExtra("cliente");		
		
		TextView textoNome = (TextView) findViewById(R.id.clienteAlterarDados_nome);
		textoNome.setText(cliente.getNome());
		
		TextView textoEmail = (TextView) findViewById(R.id.clienteAlterarDados_email);
		textoEmail.setText(cliente.getEmail());
		
		final EditText campoDDD = (EditText) findViewById(R.id.clienteAlterarDados_dddTelefone);
		campoDDD.setText(cliente.getTelefone().getDdd());
		
		final EditText campoTelefone = (EditText) findViewById(R.id.clienteAlterarDados_numTelefone);
		campoTelefone.setText(cliente.getTelefone().getNumero());
		
		final EditText campoTelefoneComplemento = (EditText) findViewById(R.id.clienteAlterarDados_telefoneComplemento);
		campoTelefoneComplemento.setText(cliente.getTelefone().getComplemento() != null ? cliente.getTelefone().getComplemento() : "");
		
		 
		final EditText campoNumDocumento = (EditText) findViewById(R.id.clienteAlterarDados_numDocumento);
		campoNumDocumento.setText(cliente.getNumeroDoDocumento());
		
		final EditText campoEnderecoLocalizacao = (EditText) findViewById(R.id.clienteAlterarDados_enderecoLocalizacao);
		campoEnderecoLocalizacao.setText(cliente.getEndereco().getLocalizacao());
		
		final EditText campoEnderecoCidade = (EditText) findViewById(R.id.clienteAlterarDados_enderecoCidade);
		campoEnderecoCidade.setText(cliente.getEndereco().getCidade());
		
		/*
		 * adaptador para mostrar o auto complete do estado
		 */
		ArrayAdapter<String> adaptador = 
			new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, ESTADOS);
		enderecoEstado = (AutoCompleteTextView) findViewById(R.id.clienteAlterarDados_enderecoEstado);
		enderecoEstado.setAdapter(adaptador);
		enderecoEstado.setText(cliente.getEndereco().getEstado());
		
		RadioGroup grupoDoc = (RadioGroup) findViewById(R.id.clienteAlterarDados_radiogroup);
		final RadioButton radioCpf = (RadioButton) findViewById(R.id.clienteAlterarDados_radioCpf);
		final RadioButton radioCnpj = (RadioButton) findViewById(R.id.clienteAlterarDados_radioCnpj);
		if (TipoDoDocumento.CNPJ == cliente.getTipoDoDocumento()){
			radioCnpj.setChecked(true);			
		}else{
			radioCpf.setChecked(true);
		}
		
		grupoDoc.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				boolean cpf = R.id.clienteAlterarDados_radioCpf == checkedId;
				boolean cnpj = R.id.clienteAlterarDados_radioCnpj == checkedId;
				if(cpf){
					radioCpf.setChecked(true);
				}
				if(cnpj){
					radioCnpj.setChecked(true);
				}				
			}
		});
		
		Button btVoltar = (Button) findViewById(R.id.clienteAlterarDados_btVoltar);
		btVoltar.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				finish();
			}
		});
		
		Button btMenu = (Button) findViewById(R.id.clienteAlterarDados_btMenu);
		btMenu.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(ClienteAlterarDados.this, MenuCliente.class);
				intent.putExtra("cliente", cliente);
				startActivity(intent);
			}
		});
		
		Button btConfirmar = (Button) findViewById(R.id.clienteAlterarDados_btConfirmar);
		btConfirmar.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if("".equals(campoDDD.getText().toString())){
					alerta("Informe o DDD do telefone.");
					return;
				}
				
				if("".equals(campoTelefone.getText().toString())){
					alerta("Informe o numero do telefone.");
					return;
				}
				
				if("".equals(campoNumDocumento.getText().toString())){
					alerta("Informe o numero do documento.");
					return;
				}
				
				if(radioCpf.isChecked() && campoNumDocumento.getText().toString().length() != 11){
					alerta("O CPF deve conter exatamente 11 dígitos.");
					return;
				}
				
				if(radioCnpj.isChecked() && campoNumDocumento.getText().toString().length() != 14){
					alerta("O CNPJ deve conter exatamente 14 dígitos.");
					return;
				}
				
				if ("".equals(campoEnderecoLocalizacao.getText().toString())){
					alerta("Informe o seu endereço");
					return;
				}
				
				if("".equals(campoEnderecoCidade.getText().toString())){
					alerta("Informa a sua cidade");
					return;
				}
				
								
				HttpCliente httpCliente = new HttpCliente();				
				String resultado = httpCliente.doPost(URL_ALTERAR_DADOS, 
						Parametros.getAlterarDadosParams(
								cliente.getIdCliente(), 
								campoDDD.getText().toString(),
								campoTelefone.getText().toString(),
								campoTelefoneComplemento.getText().toString(),
								radioCpf.isChecked() ? "cpf" : "cnpj", 
								campoNumDocumento.getText().toString(),
								campoEnderecoLocalizacao.getText().toString(),
								campoEnderecoCidade.getText().toString(),
								enderecoEstado.getText().toString()));
				
				// verifica se a requisição ao servidor não retornou erro
				if(resultado.startsWith("ERRO")){
					Intent intent = new Intent(ClienteAlterarDados.this, TelaErroCliente.class);
					intent.putExtra("mensagemErro", resultado);
					startActivity(intent);
				}else{
					try {
						cliente = httpCliente.getCliente(URL_GET_CLIENTE, Parametros.getIdParams(cliente.getIdCliente()));
						Intent intent = new Intent(ClienteAlterarDados.this, ClientePerfil.class);
						intent.putExtra("cliente", cliente);
						
						alerta("Seus dados foram alterados com sucesso!");
						
						startActivity(intent);
					} catch (IOException e) {
						Toast.makeText(ClienteAlterarDados.this, MENSAGEM, Toast.LENGTH_LONG).show();
						Log.e(Http.LOGGER, e.getMessage(), e);
					}
				}								
			}		
		});		
	}
	
	private void alerta(String mensagem) {
		Toast.makeText(this, mensagem, Toast.LENGTH_LONG).show();		
	}
}
