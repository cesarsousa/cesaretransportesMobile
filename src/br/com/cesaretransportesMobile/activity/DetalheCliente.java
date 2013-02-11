package br.com.cesaretransportesMobile.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import br.com.cesaretransportesMobile.domain.Cliente;
import br.com.cesaretransportesMobile.domain.Cliente.TipoDoDocumento;
import br.com.cesaretransportesMobile.http.Http;
import br.com.cesaretransportesMobile.http.HttpCliente;
import br.com.cesaretransportesMobile.util.CesareUtil;
import br.com.cesaretransportesMobile.util.Parametros;
import br.com.cesaretransportesMobile.util.Servlet;

/**
 * <p>Tela admin.</p>
 * <p>Utilizada para exibir um cliente no detalhe.</p>
 * <p>Esta Classe disponibiliza métodos para.</p>
 * <ul>
 * <li>Confirma um status de cliente como liberado para fazer login na aplicação.
 * <li>Fazer uma ligação telefônica para o cliente.
 * <li>Enviar um torpedo SMS para o cliente.
 * <li>Enviar e-mail via JavaMail Api para o cliente.
 * <li>Fazer a exclusão (lógica) do cliente do sistema.
 * <li>Retornar ao Menu Principal.</li>
 * </ul>
 * 
 * @author Miguel
 *
 */
public class DetalheCliente extends Activity implements Runnable {
	
	private static final String URL_DELETAR = Http.SERVIDOR + Servlet.androidClienteDeletar;
	private static final String URL_CONFIRMAR = Http.SERVIDOR + Servlet.androidClienteConfirmar;
	
	private Handler handler = new Handler();
	private ProgressDialog dialog;
	private Cliente cliente;
	private Cliente clienteAdm;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.detalhe_cliente);
		
		clienteAdm = (Cliente) getIntent().getSerializableExtra("clienteAdm");		
		cliente = (Cliente) getIntent().getSerializableExtra("cliente");
		
		if(cliente == null){
			Toast.makeText(this, "Erro Cliente inválido.", Toast.LENGTH_SHORT).show();
			finish();
		}
		
		TextView textNome = (TextView)findViewById(R.id.detalheCliente_nome);
		textNome.setText("Cód: " + String.valueOf(cliente.getIdCliente()) + " - " + cliente.getNome());
		
		TextView textEndereco = (TextView) findViewById(R.id.detalheCliente_endereco);
		textEndereco.setText(cliente.getEndereco().toString());
		
		TextView textDataCadastro = (TextView)findViewById(R.id.detalheCliente_dataCadastro);
		textDataCadastro.setText("Cadastro realizado no dia " + CesareUtil.formatarData(cliente.getDataCadastro(), "dd/MM/yyyy"));
		
		Button btLiberar = (Button)findViewById(R.id.detalheCliente_btLiberarStatus);
		btLiberar.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				
				// abre caixa de dialogo de download
				dialog = ProgressDialog.show(DetalheCliente.this, "Cesare Transportes", "Confirmando cliente, por favor aguarde...", false, true);
				
				// delega para outra thread o acesso ao servidor.
				new Thread(DetalheCliente.this).start();														
			}
		});
		
		TextView textStatus = (TextView)findViewById(R.id.detalheCliente_statusCliente);
		if(cliente.isStatusCliente()){
			textStatus.setText("Confirmado");
			textStatus.setTextColor(Color.GREEN);
			btLiberar.setEnabled(false);			
		}else{
			textStatus.setText("Pendente");
			textStatus.setTextColor(Color.RED);
			btLiberar.setEnabled(true);
		}
		
		TextView textDocumento = (TextView)findViewById(R.id.detalheCliente_documento);
		if(TipoDoDocumento.CPF == cliente.getTipoDoDocumento()){
			textDocumento.setText("CPF " + CesareUtil.formatarDocumento(cliente.getTipoDoDocumento(), cliente.getNumeroDoDocumento()));
		}else{
			textDocumento.setText("CNPJ " + CesareUtil.formatarDocumento(cliente.getTipoDoDocumento(), cliente.getNumeroDoDocumento()));
		}
		
		TextView textTelefone = (TextView)findViewById(R.id.detalheCliente_telefone);
		textTelefone.setText(cliente.getTelefone().formatado());
		
		Button btLigar = (Button)findViewById(R.id.detalheCliente_btLigar);
		btLigar.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				Uri uri = Uri.parse("tel:" + cliente.getTelefone().getUri());
				startActivity(new Intent(Intent.ACTION_CALL, uri));
			}
		});
		
		Button btSms = (Button)findViewById(R.id.detalheCliente_btSms);
		btSms.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(DetalheCliente.this, EscreverMensagem.class);
				intent.putExtra("telefone", cliente.getTelefone().getUri());
				intent.putExtra("nome", cliente.getNome());
				/*
				 * Após a execução a activity é destruída, não sendo necessário passar o clienteAdm como parâmetro.
				 */
				startActivity(intent);				
			}
		});
		
		TextView textEmail = (TextView)findViewById(R.id.detalheCliente_email);
		textEmail.setText("Email: " + cliente.getEmail());
		
		Button btEmail = (Button)findViewById(R.id.detalheCliente_btEmail);
		btEmail.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(DetalheCliente.this, EnviarEmail.class);
				intent.putExtra("nome", cliente.getNome());
				intent.putExtra("email", cliente.getEmail());
				/*
				 * Após a execução a activity é destruída, não sendo necessário passar o clienteAdm como parâmetro.
				 */
				startActivity(intent);
			}
		});
		
		Button btDeletar = (Button) findViewById(R.id.detalheCliente_btDeletar);
		btDeletar.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				if(cliente.getTipoCliente().equals("admin")){
					Toast.makeText(DetalheCliente.this, "Excluir o administrador não é uma opção válida !", Toast.LENGTH_LONG).show();
					return;
				}
				
				// Alert dialog com sim ou não
				AlertDialog.Builder alerta = new AlertDialog.Builder(DetalheCliente.this);
				alerta.setIcon(R.drawable.icon_aguia);
				alerta.setTitle("Excluir Cliente");
				alerta.setMessage("Tem certeza que deseja excluir " + cliente.getNome() + "?");
				
				// configurações dos botões.
				alerta.setPositiveButton("Sim", new DialogInterface.OnClickListener() {					
					public void onClick(DialogInterface dialog, int which) {
						HttpCliente httpCliente = new HttpCliente();
						String resultado = httpCliente.doPost(URL_DELETAR, Parametros.getIdParams(cliente.getIdCliente()));						
						
						if(resultado.startsWith("ERRO")){
							Intent intent = new Intent(DetalheCliente.this, TelaErroCliente.class);
							intent.putExtra("mensagemErro", resultado);
							startActivity(intent);							
						}else{						
							Toast.makeText(DetalheCliente.this, "Cliente Excluido com sucesso", Toast.LENGTH_SHORT).show();						
							startActivity(new Intent(DetalheCliente.this, MenuAdmin.class).putExtra("clienteAdm", clienteAdm));
						}
					}
				});
				
				alerta.setNegativeButton("Não", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {}
				});
				alerta.show();
			}
		});
		
		Button btMenu = (Button) findViewById(R.id.detalheCliente_btMenu);
		btMenu.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				startActivity(new Intent(DetalheCliente.this, MenuAdmin.class).putExtra("clienteAdm", clienteAdm));			
			}
		});
		
	}

	@Override
	public void run() {
		try {
			HttpCliente httpCliente = new HttpCliente();
			final String resultado = httpCliente.doPost(URL_CONFIRMAR, Parametros.getIdParams(cliente.getIdCliente()));
			
			handler.post(new Runnable() {
				public void run() {
					if(resultado.startsWith("ERRO")){
						Intent intent = new Intent(DetalheCliente.this, TelaErro.class);
						intent.putExtra("mensagemErro", resultado);
						intent.putExtra("clienteAdm", clienteAdm);
						startActivity(intent);
					}else{
						Toast.makeText(DetalheCliente.this, "Cliente confirmado com sucesso", Toast.LENGTH_SHORT).show();					
						startActivity(new Intent(DetalheCliente.this, MenuAdmin.class).putExtra("clienteAdm", clienteAdm));
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
