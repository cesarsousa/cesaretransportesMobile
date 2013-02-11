package br.com.cesaretransportesMobile.activity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import br.com.cesaretransportesMobile.domain.Cliente;
import br.com.cesaretransportesMobile.domain.Endereco;
import br.com.cesaretransportesMobile.domain.Servico;
import br.com.cesaretransportesMobile.domain.Veiculo;
import br.com.cesaretransportesMobile.http.Http;
import br.com.cesaretransportesMobile.http.HttpCliente;
import br.com.cesaretransportesMobile.http.HttpEndereco;
import br.com.cesaretransportesMobile.http.HttpOrcamento;
import br.com.cesaretransportesMobile.http.HttpServico;
import br.com.cesaretransportesMobile.http.HttpVeiculo;
import br.com.cesaretransportesMobile.util.Parametros;
import br.com.cesaretransportesMobile.util.Servlet;

/**
 * <p>Tela admin</p>
 * 
 * <p>Utilizada para exibir um veículo no detalhe.</p>
 * <p>Esta Classe disponibiliza métodos para.</p>
 * <ul>
 * <li>Editar a localização atual de um veículo.
 * <li>listar todos os orçamentos de serviço referente a um veículo.
 * <li>confirmar todas as entregas de um determinado veículo.
 * <li>gerar mapas da localização do veículo.</li>
 * </ul>
 * @author cesar sousa
 *
 */
public class DetalheVeiculo extends Activity {
	private static final String MENSAGEM = "Erro de comunicação com Cesare Transporte durante solicitação " +
			"de serviços por veiculo";
	
	private static final String URL_DELETAR = Http.SERVIDOR + Servlet.androidVeiculoDeletar;
	private static final String URL_EDITAR = Http.SERVIDOR + Servlet.androidVeiculoEditar;
	private static final String URL_SERVICOS = Http.SERVIDOR + Servlet.androidOrcamentosPorVeiculo;
	private static final String URL_LISTAR_ENDERECOS_ORCAMENTO = Http.SERVIDOR + Servlet.androidEnderecosPorOrcamento;
	private static final String URL_VERIFICAR_CLIENTE = Http.SERVIDOR + Servlet.androidVerificarCliente;
	private static final String URL_GET_CLIENTE = Http.SERVIDOR + Servlet.androidClienteVisualizar;	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.detalhe_veiculo);
		
		final String tipoDoLayout = getIntent().getStringExtra("tipoDoLayout");
		final Cliente clienteAdm = (Cliente) getIntent().getSerializableExtra("clienteAdm");
		
		final Veiculo veiculo = (Veiculo) getIntent().getSerializableExtra("veiculo");
		if(veiculo == null){
			Toast.makeText(this, "Erro veículo inválido.", Toast.LENGTH_SHORT).show();
			finish();
		}
		
		TextView textViewPlaca = (TextView)findViewById(R.id.detalheVeiculo_placa);
		textViewPlaca.setText(tipoDoLayout.equals("admin") ? veiculo.getInfoPlaca() + " - Cód. " + veiculo.getIdVeiculo() : veiculo.getInfoPlaca());
		
		TextView textViewMarca = (TextView)findViewById(R.id.detalheVeiculo_marca);
		textViewMarca.setText(veiculo.getMarca().toUpperCase());
		
		TextView textViewCor = (TextView)findViewById(R.id.detalheVeiculo_cor);
		textViewCor.setText(veiculo.getCor());
		
		TextView textViewTotalOrcamentos = (TextView)findViewById(R.id.detalheVeiculo_textoTotalOrcamentos);
		String totalOrcamentos = veiculo.getTotalOrcamento() == 0 ? "Este Veículo não possui entregas" : "Este veículo possui " + veiculo.getTotalOrcamento() + " entrega(s)";
		textViewTotalOrcamentos.setText(totalOrcamentos);
		
		final TextView textViewLocalizacao = (TextView)findViewById(R.id.detalheVeiculo_localizacao);
		textViewLocalizacao.setText(veiculo.getInfoLocalizacao());
		
		Button btEditarLocalizacao = (Button)findViewById(R.id.detalheVeiculo_btEditarLocalizacao);
		btEditarLocalizacao.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				EditText campoEditarLocalizacao = (EditText)findViewById(R.id.detalheVeiculo_campoEditarLocalizacao);
				String novaLocalizacao = campoEditarLocalizacao.getText().toString().trim();
				if(novaLocalizacao.equals("")){
					Toast.makeText(DetalheVeiculo.this, "Digite a nova localização", Toast.LENGTH_SHORT).show();
					return;
				}
				
				HttpVeiculo httpVeiculo = new HttpVeiculo();
				Map<String, String> parametro = Parametros.getIdParams(veiculo.getIdVeiculo());
				parametro.put("localizacao", novaLocalizacao);
				String resultado = httpVeiculo.doPost(URL_EDITAR, parametro);
				
				if(resultado.startsWith("ERRO")){
					Intent intent = new Intent(DetalheVeiculo.this, TelaErro.class);
					intent.putExtra("mensagemErro", resultado);
					intent.putExtra("clienteAdm", clienteAdm);
					startActivity(intent);					
				}else{
					Toast.makeText(DetalheVeiculo.this, "Nova localização editada com sucesso", Toast.LENGTH_LONG).show();
					Intent intent = new Intent(DetalheVeiculo.this, DetalheVeiculo.class);
					veiculo.setLocalizacao(novaLocalizacao);
					intent.putExtra("veiculo", veiculo);
					intent.putExtra("tipoDoLayout", tipoDoLayout);
					intent.putExtra("clienteAdm", clienteAdm);
					startActivity(intent);
				}				
			}
		});
		
		Button btListarOrcamentos = (Button) findViewById(R.id.detalheVeiculo_btListarOrcamentos);		
		btListarOrcamentos.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				HttpServico httpServico = new HttpServico();
				HttpEndereco httpEndereco = new HttpEndereco();
				HttpCliente httpCliente = new HttpCliente();
				
				Map<String, String> parametros = Parametros.getIdParams(veiculo.getIdVeiculo());				
				try {
					ArrayList<Servico> servicos = httpServico.downloadServicosPorVeiculo(URL_SERVICOS, parametros);
					
					if (servicos.size() == 0){
						Toast.makeText(DetalheVeiculo.this, "Veículo não possui entregas", Toast.LENGTH_SHORT).show();
					}else{
						for(Servico servico : servicos){
							ArrayList<Endereco> enderecos = 
								httpEndereco.downloadEnderecos(URL_LISTAR_ENDERECOS_ORCAMENTO, Parametros.getIdParams(servico.getOrcamento().getIdOrcamento()));
							servico.getOrcamento().setEnderecos(enderecos);
							final String resultado = httpCliente.doPost(URL_VERIFICAR_CLIENTE, Parametros.getIdParams(servico.getOrcamento().getCliente().getIdCliente()));
							String[] params = resultado.split(";");
							Cliente c = httpCliente.getCliente(URL_GET_CLIENTE, Parametros.getLoginParams(params[0], params[1]));
							servico.getOrcamento().setCliente(c);
						}
					}
					
					Intent intent = new Intent(DetalheVeiculo.this, VisualizarServicos.class);
					intent.putExtra("servicos", servicos);
					intent.putExtra("clienteAdm", clienteAdm);
					startActivity(intent);					
				} catch (IOException e) {
					Toast.makeText(DetalheVeiculo.this, MENSAGEM, Toast.LENGTH_LONG).show();
					Log.e(Http.LOGGER, e.getMessage(), e);
				}
				
			}
		});
		
		Button btConfirmarEntregas = (Button) findViewById(R.id.detalheVeiculo_btConfirmarEntregas);
		btConfirmarEntregas.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				HttpOrcamento httpOrcamento = new HttpOrcamento();
				Map<String, String> parametros = Parametros.getIdParams(veiculo.getIdVeiculo());
				parametros.put("finalizarRota", "true");
				String resultado = httpOrcamento.doPost(URL_EDITAR, parametros);
				
				if(resultado.startsWith("ERRO")){
					Intent intent = new Intent(DetalheVeiculo.this, TelaErro.class);
					intent.putExtra("mensagemErro", resultado);
					intent.putExtra("clienteAdm", clienteAdm);
					startActivity(intent);
				}else{
					Toast.makeText(DetalheVeiculo.this, "Entregas finalizadas", Toast.LENGTH_SHORT).show();
					startActivity(new Intent(DetalheVeiculo.this, MenuAdmin.class).putExtra("clienteAdm", clienteAdm));
				}				
			}
		});
		
		if(veiculo.getTotalOrcamento() == 0){
			btListarOrcamentos.setEnabled(false);
			btConfirmarEntregas.setEnabled(false);
		}
		
		Button btMapaLocalizacao = (Button) findViewById(R.id.detalheVeiculo_btMapaLocalizacao);
		btMapaLocalizacao.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(DetalheVeiculo.this, MenuOpcaoMapa.class);
				intent.putExtra("endereco", veiculo.getLocalizacao());
				startActivity(intent);
				
				//startActivity(new Intent(DetalheVeiculo.this, Mapa.class).putExtra("endereco", veiculo.getLocalizacao()));				
			}
		});
		
		if(veiculo.getInfoLocalizacao().equals("nao disponivel")){
			btMapaLocalizacao.setEnabled(false);
		}
		
		Button btExcluir = (Button)findViewById(R.id.detalheVeiculo_btExcluir);
		btExcluir.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				// Alert dialog com sim ou não
				AlertDialog.Builder alerta = new AlertDialog.Builder(DetalheVeiculo.this);
				alerta.setIcon(R.drawable.icon_aguia);
				alerta.setTitle("Excluir Veículo");
				alerta.setMessage("Tem certeza que deseja excluir este veículo?");
				
				// configurações dos botões.
				alerta.setPositiveButton("Sim", new DialogInterface.OnClickListener() {					
					public void onClick(DialogInterface dialog, int which) {
						HttpVeiculo httpVeiculo = new HttpVeiculo();
						String resultado = httpVeiculo.doPost(URL_DELETAR, Parametros.getIdParams(veiculo.getIdVeiculo()));						
												
						if(resultado.startsWith("ERRO")){
							Intent intent = new Intent(DetalheVeiculo.this, TelaErro.class);
							intent.putExtra("mensagemErro", resultado);
							startActivity(intent);
						}else{
							Toast.makeText(DetalheVeiculo.this, resultado, Toast.LENGTH_SHORT).show();						
							startActivity(new Intent(DetalheVeiculo.this, MenuAdmin.class).putExtra("clienteAdm", clienteAdm));
						}
					}
				});
				
				alerta.setNegativeButton("Não", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {}
				});
				alerta.show();
			}
		});	
		
		if(veiculo.getTotalOrcamento() != 0){
			btExcluir.setEnabled(false);			
		}
		
		Button btMenu = (Button)findViewById(R.id.detalheVeiculo_btMenu);
		btMenu.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if(tipoDoLayout.equals("admin")){
					startActivity(new Intent(DetalheVeiculo.this, MenuAdmin.class).putExtra("clienteAdm", clienteAdm));
				}
			}
		});	
	}
}