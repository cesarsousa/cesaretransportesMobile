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
import android.widget.RadioButton;
import android.widget.Toast;
import br.com.cesaretransportesMobile.domain.Cliente;
import br.com.cesaretransportesMobile.domain.Endereco;
import br.com.cesaretransportesMobile.domain.Orcamento;
import br.com.cesaretransportesMobile.http.Http;
import br.com.cesaretransportesMobile.http.HttpCliente;
import br.com.cesaretransportesMobile.http.HttpEndereco;
import br.com.cesaretransportesMobile.http.HttpOrcamento;
import br.com.cesaretransportesMobile.util.Parametros;
import br.com.cesaretransportesMobile.util.Servlet;

/**
 * <p>Tela admin</p>
 * <p>Utilizada para exibir opções de visualização dos orçamentos.<p>
 * <p>Esta classe disponibiliza métodos para</p>
 * <ul>
 * <li>listar todos os orçamentos.
 * <li>buscar um orçamento pelo codigo.
 * <li>buscar orçamentos pelo id de um cliente.
 * <li>buscar orçamentos pelo id de um veículo.
 * <li>buscar orçamentos pelo origem e pelo destino.</li>
 * </ul>
 * 
 * @author cesar sousa
 *
 */
public class OpcaoVisualizarOrcamentos extends Activity {
	
	private static final String URL_LISTAR_ORCAMENTOS = Http.SERVIDOR + Servlet.androidOrcamentosPorCliente;
	private static final String URL_GET_CLIENTE = Http.SERVIDOR + Servlet.androidClienteVisualizar;
	private static final String URL_LISTAR_ENDERECOS_ORCAMENTO = Http.SERVIDOR + Servlet.androidEnderecosPorOrcamento;
	private static final String URL_VERIFICAR_CLIENTE = Http.SERVIDOR + Servlet.androidVerificarCliente;
	private static final String MENSAGEM = "Erro para efetuar a conexão com Cesare Transportes para listar orcamentos.";
	private ArrayList<Orcamento> orcamentos;
	
	private enum Filtro {
		CODIGO, NOME, EMAIL, ORIGEM, DESTINO;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.opcao_visualizar_orcamentos);
		
		final Cliente clienteAdm = (Cliente) getIntent().getSerializableExtra("clienteAdm");
		final HttpOrcamento httpOrcamento = new HttpOrcamento();
		final HttpCliente httpCliente = new HttpCliente();
		final HttpEndereco httpEndereco = new HttpEndereco();
		
		try {
			orcamentos = httpOrcamento.downloadOrcamentos(URL_LISTAR_ORCAMENTOS, Parametros.getIdParams(0));
			for (Orcamento orcamento : orcamentos) {
				ArrayList<Endereco> enderecos = httpEndereco.downloadEnderecos(URL_LISTAR_ENDERECOS_ORCAMENTO, 
						Parametros.getIdParams(orcamento.getIdOrcamento()));
				orcamento.setEnderecos(enderecos);
				final String resultado = httpCliente.doPost(URL_VERIFICAR_CLIENTE, Parametros.getIdParams((orcamento.getCliente().getIdCliente())));
				String[] params = resultado.split(";");
				Cliente c = httpCliente.getCliente(URL_GET_CLIENTE, Parametros.getLoginParams(params[0], params[1]));
				orcamento.setCliente(c);
			}
		} catch (IOException e) {
			Log.e(Http.LOGGER, e.getMessage(), e);
			Intent intent = new Intent(OpcaoVisualizarOrcamentos.this, TelaErro.class);
			intent.putExtra("mensagemErro", getClass().getSimpleName() + ": " + MENSAGEM);
			intent.putExtra("clienteAdm", clienteAdm);
			startActivity(intent);
		}
		
		Button btListarTodos = (Button)findViewById(R.id.opcaoVisualOrcamentos_btListarTodos);
		btListarTodos.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if (orcamentos.size() == 0) {
					Toast.makeText(OpcaoVisualizarOrcamentos.this,
							"Não existem orçamento a ser visualizado",
							Toast.LENGTH_SHORT).show();
				} else {					
					Toast.makeText(OpcaoVisualizarOrcamentos.this, "Resultado: " + orcamentos.size() + " orçamento(s)", Toast.LENGTH_SHORT).show();
					Intent intent = new Intent(OpcaoVisualizarOrcamentos.this, VisualizarOrcamentos.class);
					intent.putExtra("orcamentos", orcamentos);
					intent.putExtra("clienteAdm", clienteAdm);
					startActivity(intent);
				}				
			}
		});
		
		Button btBuscar = (Button)findViewById(R.id.opcaoVisualOrcamentos_btBuscar);
		btBuscar.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				EditText campoBuscar = (EditText)findViewById(R.id.opcaoVisualOrcamentos_campoBuscar);
				String paramBusca = campoBuscar.getText().toString().trim();
				if(paramBusca.equals("")){
					Toast.makeText(OpcaoVisualizarOrcamentos.this, "Digite um parâmetro para a pesquisar", Toast.LENGTH_SHORT).show();
					return;
				}
				
				Filtro filtro = getFiltro();				
				ArrayList<Orcamento> orcamentosFiltrados = getAllBy(paramBusca, filtro);
				if(orcamentosFiltrados.size() == 0){
					Toast.makeText(OpcaoVisualizarOrcamentos.this, "0 resultados para a pesquisa", Toast.LENGTH_SHORT).show();
					return;
				}else{
					Toast.makeText(OpcaoVisualizarOrcamentos.this, orcamentosFiltrados.size() + " resultado(s) para a pesquisa", Toast.LENGTH_SHORT).show();
					Intent intent = new Intent(OpcaoVisualizarOrcamentos.this, VisualizarOrcamentos.class);
					intent.putExtra("orcamentos", orcamentosFiltrados);
					intent.putExtra("clienteAdm", clienteAdm);
					startActivity(intent);
				}			
			}		
		});
		
		Button btCancelar = (Button)findViewById(R.id.opcaoVisualOrcamentos_btCancelar);
		btCancelar.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				finish();				
			}
		});	
	}
	
	private Filtro getFiltro() {
		RadioButton radioCodigo = (RadioButton)findViewById(R.id.opcaoVisualOrcamentos_radioCodigo);		
		RadioButton radioEmail = (RadioButton)findViewById(R.id.opcaoVisualOrcamentos_radioEmail);
		RadioButton radioNome = (RadioButton)findViewById(R.id.opcaoVisualOrcamentos_radioNome);
		RadioButton radioOrigem = (RadioButton)findViewById(R.id.opcaoVisualOrcamentos_radioOrigem);
		if(radioCodigo.isChecked()){
			return Filtro.CODIGO;
		}else if(radioEmail.isChecked()){
			return Filtro.EMAIL;
		}else if(radioNome.isChecked()){
			return Filtro.NOME;
		}else if(radioOrigem.isChecked()){
			return Filtro.ORIGEM;
		}else{
			return Filtro.DESTINO;
		}
	}
	
	private ArrayList<Orcamento> getAllBy(String parametro, Filtro filtro) {
		ArrayList<Orcamento> orcamentosFiltrados = new ArrayList<Orcamento>();
		if(Filtro.CODIGO == filtro){
			for(Orcamento orcamento : orcamentos){
				if(String.valueOf(orcamento.getIdOrcamento()).equals(parametro)){
					orcamentosFiltrados.add(orcamento);
					return orcamentosFiltrados;
				}
			}
		}else if(Filtro.NOME == filtro){			
			for(Orcamento orcamento : orcamentos){
				if(orcamento.getCliente().getNome().contains(parametro)){
					orcamentosFiltrados.add(orcamento);
				}
			}
		}else if(Filtro.EMAIL == filtro){
			for(Orcamento orcamento : orcamentos){				
				if(orcamento.getCliente().getEmail().contains(parametro)){					
					orcamentosFiltrados.add(orcamento);					
				}
			}			
		}else if(Filtro.ORIGEM == filtro){
			for(Orcamento orcamento : orcamentos){				
				if(orcamento.getDetalheOrigem().contains(parametro)){					
					orcamentosFiltrados.add(orcamento);					
				}
			}
		}else{
			for(Orcamento orcamento : orcamentos){				
				if(orcamento.getDetalheDestino().contains(parametro)){					
					orcamentosFiltrados.add(orcamento);					
				}
			}
		}		
		
		return orcamentosFiltrados;
	}	
}
