package br.com.cesaretransportesMobile.activity;

import java.io.IOException;
import java.util.Locale;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import br.com.cesaretransportesMobile.domain.Cliente;
import br.com.cesaretransportesMobile.domain.Empresa;
import br.com.cesaretransportesMobile.domain.Telefone;
import br.com.cesaretransportesMobile.http.Http;
import br.com.cesaretransportesMobile.http.HttpCliente;
import br.com.cesaretransportesMobile.http.HttpEmpresa;
import br.com.cesaretransportesMobile.util.Parametros;
import br.com.cesaretransportesMobile.util.Servlet;

public class ClienteContato extends Activity implements Runnable{
	
	private static final String URL_EMPRESA = Http.SERVIDOR + Servlet.androidEmpresaVisualizar;	
	private static final String ENVIAR_EMAIL_CONTATO = Http.SERVIDOR + Servlet.androidContatoCadastrar;
	
	private Handler handler = new Handler();
	private Cliente cliente;
	private ProgressDialog dialog;
	private EditText textoEmail;
	private String mensagem;
	private ImageButton btFone1;
	private ImageButton btFone2;
	private ImageButton btFone3;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);		
		setContentView(R.layout.cliente_contato);		
		
		
		try {
			final Empresa empresa = new HttpEmpresa().getEmpresa(URL_EMPRESA);
			
			cliente = (Cliente) getIntent().getSerializableExtra("cliente");	
			
			TextView textEnderecoEmpresa = (TextView) findViewById(R.id.clienteContato_textoEnderecoEmpresa);
			textEnderecoEmpresa.setText(empresa.getEndereco().toString());
			
			Button btMapaEndereco = (Button) findViewById(R.id.clienteContato_btMapaEndereco);
			btMapaEndereco.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					try {
						Geocoder geocoder = new Geocoder(ClienteContato.this, Locale.getDefault());
						Address address = new Mapa().getEndereco(empresa.getEndereco().toString(), geocoder);
						if (address == null){
							Toast.makeText(ClienteContato.this, "Não foi possível gerar mapa para: " + empresa.getEndereco(), Toast.LENGTH_LONG).show();
						}else{
							startActivity(new Intent(ClienteContato.this, Mapa.class).putExtra("endereco", address));
						}				
					} catch (IOException e) {
						Toast.makeText(ClienteContato.this, "Não foi possível gerar mapa para: " + empresa.getEndereco(), Toast.LENGTH_LONG).show();
					}
				}
			});
			
			Button btEnviarEmail = (Button) findViewById(R.id.clienteContato_btEnviarEmail);
			btEnviarEmail.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					textoEmail = (EditText) findViewById(R.id.clienteContato_textoEmail);
					mensagem = textoEmail.getText().toString();
					if("".equals(mensagem)){
						Toast.makeText(ClienteContato.this, "Insira o texto que deseja enviar por email.", Toast.LENGTH_SHORT).show();
						return;
					}
					
					dialog = ProgressDialog.show(ClienteContato.this, "Cesare Transportes", "Enviando email, por favor aguarde...", false, true);
					new Thread(ClienteContato.this).start();												
				}
			});
			
			TextView textoEmailEmpresa = (TextView) findViewById(R.id.clienteContato_textoEmailEmpresa);
			textoEmailEmpresa.setText(empresa.getEmail());
			
			TextView textMsnEmpresa = (TextView) findViewById(R.id.clienteContato_textoMsnEmpresa);
			textMsnEmpresa.setText(empresa.getMsn());
			
			int totalTelefones = empresa.getTelefones().size();
			if(totalTelefones == 0){
				desabilitarTelefones();
			}else if(totalTelefones == 1){
				configurarTelefone1(empresa.getTelefones().get(0));
				desabilitarTelefone2();
				desabilitarTelefone3();
			}else if(totalTelefones == 2){
				configurarTelefone1(empresa.getTelefones().get(0));
				configurarTelefone2(empresa.getTelefones().get(1));
				desabilitarTelefone3();
			}else if(totalTelefones == 3){
				configurarTelefone1(empresa.getTelefones().get(0));
				configurarTelefone2(empresa.getTelefones().get(1));
				configurarTelefone3(empresa.getTelefones().get(2));
			}		
			
			Button btMenu = (Button) findViewById(R.id.clienteContato_btMenu);
			btMenu.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					Intent intent = new Intent(ClienteContato.this, MenuCliente.class);
					intent.putExtra("cliente", cliente);				
					startActivity(intent);
				}
			});
			
		} catch (IOException e) {
			e.printStackTrace();			
			Toast.makeText(this, "Erro para carregar a página.", Toast.LENGTH_LONG).show();
			startActivity(new Intent(this, TelaErroCliente.class).putExtra("mensagemErro", e.getMessage()));
		}			
	}

	
	private void desabilitarTelefones() {		
		desabilitarTelefone1();
		desabilitarTelefone2();
		desabilitarTelefone3();
		setarMensagemPadrao();		
	}
	
	private void setarMensagemPadrao() {
		TextView textoSemTelefone = (TextView) findViewById(R.id.clienteContato_TextoSemTelefone);
		textoSemTelefone.setText("Todos os nossos telefones estão inoperantes no momento, Por favor Utilize nossos canais de email, Atenciosamente...");		
	}


	private void desabilitarTelefone1() {
		btFone1 = (ImageButton) findViewById(R.id.clienteContato_btFone1);
		btFone1.setVisibility(Button.INVISIBLE);		
	}
	
	private void desabilitarTelefone2() {		
		btFone2 = (ImageButton) findViewById(R.id.clienteContato_btFone2);
		btFone2.setVisibility(Button.INVISIBLE);		
	}
	
	private void desabilitarTelefone3() {		
		btFone3 = (ImageButton) findViewById(R.id.clienteContato_btFone3);
		btFone3.setVisibility(Button.INVISIBLE);
	}	

	private void configurarTelefone3(final Telefone telefone) {
		btFone3 = (ImageButton) findViewById(R.id.clienteContato_btFone3);
		if(telefone.formatado().equals("")){
			btFone3.setVisibility(Button.INVISIBLE);
		}else{
			TextView textoNumeroFone3 = (TextView) findViewById(R.id.clienteContato_numeroFone3);
			textoNumeroFone3.setText(telefone.formatado());
			btFone3.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					Uri uri = Uri.parse("tel:" + telefone.getUri());
					startActivity(new Intent(Intent.ACTION_CALL, uri));				
				}
			});
		}		
	}

	private void configurarTelefone2(final Telefone telefone) {
		btFone2 = (ImageButton) findViewById(R.id.clienteContato_btFone2);
		if(telefone.formatado().equals("")){
			btFone2.setVisibility(Button.INVISIBLE);
		}else{
			TextView textoNumeroFone2 = (TextView) findViewById(R.id.clienteContato_numeroFone2);
			textoNumeroFone2.setText(telefone.formatado());
			btFone2.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					Uri uri = Uri.parse("tel:" + telefone.getUri());
					startActivity(new Intent(Intent.ACTION_CALL, uri));				
				}
			});
		}	
	}

	private void configurarTelefone1(final Telefone telefone) {
		btFone1 = (ImageButton) findViewById(R.id.clienteContato_btFone1);
		if(telefone.formatado().equals("")){
			btFone1.setVisibility(Button.INVISIBLE);
		}else{
			TextView textoNumeroFone1 = (TextView) findViewById(R.id.clienteContato_numeroFone1);
			textoNumeroFone1.setText(telefone.formatado());
			btFone1.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					Uri uri = Uri.parse("tel:" + telefone.getUri());
					startActivity(new Intent(Intent.ACTION_CALL, uri));				
				}
			});
		}		
	}

	@Override
	public void run() {
		try {
			HttpCliente httpCliente = new HttpCliente();
			final String resultado = httpCliente.doPost(ENVIAR_EMAIL_CONTATO, 
					Parametros.getEmailParams(cliente.getIdCliente(), cliente.getNome(), cliente.getEmail(), mensagem));
			
			handler.post(new Runnable() {
				public void run() {
					if(resultado.startsWith("ERRO")){
						Intent intent = new Intent(ClienteContato.this, TelaErroCliente.class);
						intent.putExtra("mensagemErro", resultado);
						startActivity(intent);
					}else{
						textoEmail.setText("");
						Toast.makeText(ClienteContato.this, resultado, Toast.LENGTH_SHORT).show();
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