package br.com.cesaretransportesMobile.activity;

import java.io.IOException;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import br.com.cesaretransportesMobile.domain.Cliente;
import br.com.cesaretransportesMobile.http.Http;
import br.com.cesaretransportesMobile.http.HttpCliente;
import br.com.cesaretransportesMobile.util.Parametros;
import br.com.cesaretransportesMobile.util.Servlet;

/**
 * <p>Tela de admin</p>
 * <p>Utilizada para exibir opções de visualização dos cliente.<p>
 * <p>Esta classe disponibiliza métodos para</p>
 * <ul>
 * <li>listar todos os clientes.
 * <li>buscar um cliente por id.
 * <li>buscar clientes por nome.
 * <li>buscar clientes por documento.</li>
 * </ul>
 * 
 * @author cesar sousa
 *
 */
public class OpcaoVisualizarClientes extends Activity {
	
	private static final String URL_GET_CLIENTE = Http.SERVIDOR + Servlet.androidClienteVisualizar;	
	private static final String URL_GET_CLIENTES = Http.SERVIDOR + Servlet.androidClienteVisualizarTodos;	
	private static final String URL_VERIFICAR_CLIENTE = Http.SERVIDOR + Servlet.androidVerificarCliente;
	private static final String MENSAGEM = "Erro para efetuar a conexão com Cesare Transportes para listar clientes.";
	private ArrayList<Cliente> clientes;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.opcao_visualizar_clientes);
		
		final Cliente clienteAdm = (Cliente) getIntent().getSerializableExtra("clienteAdm");		
		final HttpCliente httpCliente = new HttpCliente();
		
		Button btListarTodos = (Button)findViewById(R.id.opcaoVisualClientes_btListarTodos);
		btListarTodos.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent;
				try {
					clientes = (ArrayList<Cliente>) httpCliente.downloadClientes(URL_GET_CLIENTES, Parametros.getVisualizarClientesPendentesParams());			
				} catch (IOException e) {
					Log.e(Http.LOGGER, e.getMessage(), e);
					intent = new Intent(OpcaoVisualizarClientes.this, TelaErro.class);
					intent.putExtra("mensagemErro", getClass().getSimpleName() + ": " + MENSAGEM);
					startActivity(intent);
				}
				if(clientes.size() == 0){
					Toast.makeText(OpcaoVisualizarClientes.this, "Não existe cliente a confirmar", Toast.LENGTH_SHORT).show();
				}else{
					intent = new Intent(OpcaoVisualizarClientes.this, VisualizarClientes.class);
					intent.putExtra("clientes", clientes);
					intent.putExtra("clienteAdm", clienteAdm);
					startActivity(intent);	
				}
			}
		});
		
		Button btBuscaId = (Button)findViewById(R.id.opcaoVisualClientes_btBuscaId);
		btBuscaId.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {				
				EditText campoId = (EditText)findViewById(R.id.opcaoVisualClientes_campoId);
				String idCliente = campoId.getText().toString().trim();
				try {
					Integer.parseInt(idCliente);
				} catch (NumberFormatException e) {
					Toast.makeText(OpcaoVisualizarClientes.this, "Insira um número de id para a pesquisa", Toast.LENGTH_LONG).show();
					return;
				}
				
				if (idCliente.equals("")){
					Toast.makeText(OpcaoVisualizarClientes.this, "Insira um número de id para a pesquisa", Toast.LENGTH_LONG).show();
					return;
				}
				
				final String resultado = httpCliente.doPost(URL_VERIFICAR_CLIENTE, Parametros.getIdParams(Integer.parseInt(idCliente)));
				
				if("0".equals(resultado)){
					Toast.makeText(OpcaoVisualizarClientes.this, "Não existe cliente com o id '" + idCliente + "'.", Toast.LENGTH_LONG).show();
				}else{
					String[] params = resultado.split(";");
					try {
						Cliente c = httpCliente.getCliente(URL_GET_CLIENTE, Parametros.getLoginParams(params[0], params[1]));
						Intent intent = new Intent(OpcaoVisualizarClientes.this, DetalheCliente.class);
						intent.putExtra("cliente", c);
						intent.putExtra("clienteAdm", clienteAdm);
						startActivity(intent);
					} catch (IOException e) {						
						e.printStackTrace();
						Toast.makeText(OpcaoVisualizarClientes.this, "Não foi possível verificar o cliente", Toast.LENGTH_SHORT).show();
					}
				}			
			}		
		});
		
		Button btBuscaNome = (Button)findViewById(R.id.opcaoVisualClientes_btBuscaNome);
		btBuscaNome.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				EditText campoNome = (EditText)findViewById(R.id.opcaoVisualClientes_campoNome);
				String nome = campoNome.getText().toString().trim();
				if(nome.equals("")){
					Toast.makeText(OpcaoVisualizarClientes.this, "Insira o nome para a pesquisa", Toast.LENGTH_LONG).show();
					return;
				}
				
				Intent intent;
				try {
					clientes = (ArrayList<Cliente>) httpCliente.downloadClientes(URL_GET_CLIENTES, Parametros.getVisualizarClientes("nome", nome));
				} catch (IOException e) {
					Log.e(Http.LOGGER, e.getMessage(), e);
					intent = new Intent(OpcaoVisualizarClientes.this, TelaErro.class);
					intent.putExtra("mensagemErro", getClass().getSimpleName() + ": " + MENSAGEM);
					startActivity(intent);
				}
								
				if (clientes.size() == 0){
					Toast.makeText(OpcaoVisualizarClientes.this, "Sem ocorrências para '" + nome + "'", Toast.LENGTH_LONG).show();
				}else{
					Toast.makeText(OpcaoVisualizarClientes.this, clientes.size() + " ocorrência(s) para '" + nome + "'", Toast.LENGTH_SHORT).show();
					intent = new Intent(OpcaoVisualizarClientes.this, VisualizarClientes.class);
					intent.putExtra("clientes", clientes);
					intent.putExtra("clienteAdm", clienteAdm);
					startActivity(intent);
				}				
			}		
		});
		
		Button btBuscaDocumento = (Button)findViewById(R.id.opcaoVisualClientes_btBuscaDocumento);
		btBuscaDocumento.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				EditText campoDocumento = (EditText)findViewById(R.id.opcaoVisualClientes_campoDocumento);
				String documento = campoDocumento.getText().toString().trim();
				if(documento.equals("")){
					Toast.makeText(OpcaoVisualizarClientes.this, "Insira o número do documento para a pesquisa", Toast.LENGTH_LONG).show();
					return;
				}
				
				Intent intent;
				try {
					clientes = (ArrayList<Cliente>) httpCliente.downloadClientes(URL_GET_CLIENTES, Parametros.getVisualizarClientes("numDoc", documento));
				} catch (IOException e) {
					Log.e(Http.LOGGER, e.getMessage(), e);
					intent = new Intent(OpcaoVisualizarClientes.this, TelaErro.class);
					intent.putExtra("mensagemErro", getClass().getSimpleName() + ": " + MENSAGEM);
					startActivity(intent);
				}
				if (clientes.size() == 0){
					Toast.makeText(OpcaoVisualizarClientes.this, "Sem ocorrências para '" + documento + "'", Toast.LENGTH_LONG).show();
				}else{
					Toast.makeText(OpcaoVisualizarClientes.this, clientes.size() + " ocorrência(s) para '" + documento + "'", Toast.LENGTH_SHORT).show();
					intent = new Intent(OpcaoVisualizarClientes.this, VisualizarClientes.class);
					intent.putExtra("clientes", clientes);
					intent.putExtra("clienteAdm", clienteAdm);
					startActivity(intent);
				}
			}
		});		
		
		Button btCancelar = (Button)findViewById(R.id.opcaoVisualClientes_btCancelar);
		btCancelar.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				finish();				
			}
		});		
	}
}
