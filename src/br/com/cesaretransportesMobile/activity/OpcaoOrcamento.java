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
import android.widget.Toast;
import br.com.cesaretransportesMobile.domain.Cliente;
import br.com.cesaretransportesMobile.domain.Endereco;
import br.com.cesaretransportesMobile.domain.Orcamento;
import br.com.cesaretransportesMobile.domain.Servico;
import br.com.cesaretransportesMobile.http.Http;
import br.com.cesaretransportesMobile.http.HttpEndereco;
import br.com.cesaretransportesMobile.http.HttpOrcamento;
import br.com.cesaretransportesMobile.http.HttpServico;
import br.com.cesaretransportesMobile.util.Parametros;
import br.com.cesaretransportesMobile.util.Servlet;

public class OpcaoOrcamento extends Activity {
	
	private static final String URL_LISTAR_ORCAMENTOS = Http.SERVIDOR + Servlet.androidOrcamentosPorCliente;
	private static final String URL_LISTAR_SERVICOS = Http.SERVIDOR + Servlet.androidServicosPorCliente;
	private static final String URL_LISTAR_ENDERECOS_ORCAMENTO = Http.SERVIDOR + Servlet.androidEnderecosPorOrcamento;
	private static final String MENSAGEM = "Erro para efetuar a conexão com Cesare Transportes para listar orçamentos do cliente.";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.opcao_orcamento);
		
		final Cliente cliente = (Cliente) getIntent().getSerializableExtra("cliente");
		final HttpOrcamento httpOrcamento = new HttpOrcamento();
		final HttpEndereco httpEndereco = new HttpEndereco();
		final HttpServico httpServico = new HttpServico();
		
		Button btNovo = (Button) findViewById(R.id.opcaoOrcamento_btNovo);
		btNovo.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(OpcaoOrcamento.this, ClienteOrcamento.class);
				intent.putExtra("cliente", cliente);
				startActivity(intent);				
			}
		});
		
		Button btListOrcamentos = (Button) findViewById(R.id.opcaoOrcamento_btListarOrcamentos);
		btListOrcamentos.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				try {					
					ArrayList<Orcamento> orcamentos = 
						httpOrcamento.downloadOrcamentos(URL_LISTAR_ORCAMENTOS, Parametros.getIdParams(cliente.getIdCliente()));
					
					if(orcamentos.size() == 0){
						Toast.makeText(OpcaoOrcamento.this, "Voce não possui orçamentos", Toast.LENGTH_SHORT).show();
					}else{
						for(Orcamento orcamento : orcamentos){
							ArrayList<Endereco> enderecos = 
								httpEndereco.downloadEnderecos(URL_LISTAR_ENDERECOS_ORCAMENTO, Parametros.getIdParams(orcamento.getIdOrcamento()));
							orcamento.setEnderecos(enderecos);
							orcamento.setCliente(cliente);
						}					
						
						Intent intent = new Intent(OpcaoOrcamento.this, ClienteVisualizarOrcamentos.class);
						intent.putExtra("cliente", cliente);
						intent.putExtra("orcamentos", orcamentos);
						startActivity(intent);					
					}
				} catch (IOException e) {
					Log.e(Http.LOGGER, e.getMessage(), e);
					Intent intent = new Intent(OpcaoOrcamento.this, TelaErro.class);
					intent.putExtra("mensagemErro", getClass().getSimpleName() + ": " + MENSAGEM);
					startActivity(intent);
				}				
			}
		});
		
		Button btListServicos = (Button) findViewById(R.id.opcaoOrcamento_btListarServicos);
		btListServicos.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				try {
					ArrayList<Servico> servicos = 
						httpServico.downloadServicosPorCliente(URL_LISTAR_SERVICOS, Parametros.getIdParams(cliente.getIdCliente()));
					
					if(servicos.size() == 0){
						Toast.makeText(OpcaoOrcamento.this, "Voce não possui serviços", Toast.LENGTH_SHORT).show();
					}else{
						for(Servico servico : servicos){
							ArrayList<Endereco> enderecos = 
								httpEndereco.downloadEnderecos(URL_LISTAR_ENDERECOS_ORCAMENTO, Parametros.getIdParams(servico.getOrcamento().getIdOrcamento()));
							servico.getOrcamento().setEnderecos(enderecos);
							servico.getOrcamento().setCliente(cliente);
						}
						
						Intent intent = new Intent(OpcaoOrcamento.this, ClienteVisualizarServicos.class);
						intent.putExtra("cliente", cliente);
						intent.putExtra("servicos", servicos);
						startActivity(intent);					
					}
				} catch (IOException e) {
					Log.e(Http.LOGGER, e.getMessage(), e);
					Intent intent = new Intent(OpcaoOrcamento.this, TelaErro.class);
					intent.putExtra("mensagemErro", getClass().getSimpleName() + ": " + MENSAGEM);
					startActivity(intent);
				}
				
			}
		});
		
		Button btMenu = (Button) findViewById(R.id.opcaoOrcamento_btMenu);
		btMenu.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				startActivity(new Intent(OpcaoOrcamento.this, MenuCliente.class).putExtra("cliente", cliente));				
			}
		});
	}
}
