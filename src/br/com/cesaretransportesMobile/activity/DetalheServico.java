package br.com.cesaretransportesMobile.activity;

import java.util.Calendar;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import br.com.cesaretransportesMobile.domain.Cliente;
import br.com.cesaretransportesMobile.domain.Servico;
import br.com.cesaretransportesMobile.http.Http;
import br.com.cesaretransportesMobile.http.HttpOrcamento;
import br.com.cesaretransportesMobile.util.CesareUtil;
import br.com.cesaretransportesMobile.util.Parametros;
import br.com.cesaretransportesMobile.util.Servlet;

public class DetalheServico extends Activity {
	
	private static final String URL_CONFIRMAR_ENTREGA = Http.SERVIDOR + Servlet.androidConfirmarOrcamento;
	private static final String URL_DELETAR_SERVICO = Http.SERVIDOR + Servlet.androidExcluirServico;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.detalhe_orcamento);
		
		final String tipoDoLayout = getIntent().getStringExtra("tipoDoLayout");		
		
		// recupera o serviço.
		final Servico servico = (Servico) getIntent().getSerializableExtra("servico");
		// recupera o cliente da seção
		final Cliente cliente = (Cliente) getIntent().getSerializableExtra("cliente");
		// recupera o administrador da seção
		final Cliente clienteAdm = (Cliente) getIntent().getSerializableExtra("clienteAdm");
		
		TextView textoHeader = (TextView) findViewById(R.id.detalheOrcamento_header);
		textoHeader.setText("Informação do Serviço");
		
		TextView textoStatusCliente = (TextView)findViewById(R.id.detalheOrcamento_statusCliente);
		textoStatusCliente.setText("Dados do Cliente");
		textoStatusCliente.setTextColor(Color.WHITE);
		
		LinearLayout linearLayout = (LinearLayout) findViewById(R.id.detalheOrcamento_layoutFotoVan);		
		Button btFotoVan = (Button) findViewById(R.id.detalheOrcamento_btFotoVan);
		
		if(servico.getDataEntrega() != null){
			linearLayout.setBackgroundResource(R.drawable.van_servico_entregue_30_30);
			btFotoVan.setVisibility(Button.INVISIBLE);
		}else{			
			linearLayout.setBackgroundResource(R.drawable.van_servico_30_30);
			btFotoVan.setBackgroundResource(R.drawable.van_servico_30_30);
			
			TextView textoDetalheVeiculo = (TextView) findViewById(R.id.detalheOrcamento_detalheVeiculo);
			textoDetalheVeiculo.setText(tipoDoLayout.equals("admin") ? "veículo em trânsito. Cód. " + servico.getVeiculo().getIdVeiculo() : "veículo em trânsito");
			
			if(tipoDoLayout.equals("admin")){			
				btFotoVan.setOnClickListener(new OnClickListener() {
					public void onClick(View v) {
						// Alert dialog com sim ou não
						AlertDialog.Builder alerta = new AlertDialog.Builder(DetalheServico.this);
						alerta.setIcon(R.drawable.icon_aguia);
						alerta.setTitle("Confirmar Serviço");						
						alerta.setMessage("Confirmar este serviço como entregue na data " + CesareUtil.formatarData(Calendar.getInstance(), "dd/MM/yyyy") + "?");
						
						// configurações dos botões.
						alerta.setPositiveButton("Sim", new DialogInterface.OnClickListener() {					
							public void onClick(DialogInterface dialog, int which) {								
								HttpOrcamento httpOrcamento = new HttpOrcamento();
								// http serviço 
								String resultado = httpOrcamento.doPost(URL_CONFIRMAR_ENTREGA, Parametros.getConfirmarOrcamentoParams("entrega", servico.getIdServico()));						
								
								if(resultado.startsWith("ERRO")){
									Intent intent = new Intent(DetalheServico.this, TelaErro.class);
									intent.putExtra("mensagemErro", resultado);
									intent.putExtra("clienteAdm", clienteAdm);
									startActivity(intent);
								}else{
									Toast.makeText(DetalheServico.this, "Serviço confirmado com sucesso", Toast.LENGTH_SHORT).show();						
									startActivity(new Intent(DetalheServico.this, MenuAdmin.class).putExtra("clienteAdm", clienteAdm));
								}												
							}
						});

						alerta.setNegativeButton("Não", new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int which) {}
						});
						alerta.show();
					}
				});
			}
		}		
		
		TextView textoData = (TextView)findViewById(R.id.detalheOrcamento_data);
		textoData.setText("Orçamento enviado em " + CesareUtil.formatarData(servico.getOrcamento().getDataCadastro(), "dd/MM/yyyy"));
		
		TextView textoDataPrevEntrega = (TextView) findViewById(R.id.detalheOrcamento_dataPrevEntrega);
		String dataPrevEntrega = servico.getDataPrevEntrega() == null ? "Não Disponível" : CesareUtil.formatarData(servico.getDataPrevEntrega(), "dd/MM/yyyy");
		textoDataPrevEntrega.setText("Data prevista para entrega - " + dataPrevEntrega);
		
		TextView textoDataEntrega = (TextView) findViewById(R.id.detalheOrcamento_dataEntrega);
		String dataEntrega = servico.getDataEntrega() == null ? "Não Disponível" : CesareUtil.formatarData(servico.getDataEntrega(), "dd/MM/yyyy");
		textoDataEntrega.setText("Data da entrega - " + dataEntrega);
		
		TextView textoNome = (TextView)findViewById(R.id.detalheOrcamento_nome);
		textoNome.setText(servico.getOrcamento().getCliente().getNome());
		
		TextView textoEmail = (TextView)findViewById(R.id.detalheOrcamento_email);
		textoEmail.setText(servico.getOrcamento().getCliente().getEmail());
		
		TextView textoTelefone = (TextView)findViewById(R.id.detalheOrcamento_telefone);
		textoTelefone.setText(servico.getOrcamento().getCliente().getTelefone().formatado());
		
		Button btLigar = (Button)findViewById(R.id.detalheOrcamento_btLigar);
		btLigar.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				Uri uri = Uri.parse("tel:" + servico.getOrcamento().getCliente().getTelefone().getUri());
				startActivity(new Intent(Intent.ACTION_CALL, uri));				
			}
		});		
		if(tipoDoLayout.equals("cliente")){
			btLigar.setVisibility(Button.INVISIBLE);
		}
		
		Button btSms = (Button)findViewById(R.id.detalheOrcamento_btSms);
		btSms.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(DetalheServico.this, EscreverMensagem.class);
				intent.putExtra("telefone", servico.getOrcamento().getCliente().getTelefone().getUri());
				intent.putExtra("nome", servico.getOrcamento().getCliente().getNome());
				startActivity(intent);				
			}
		});
		if(tipoDoLayout.equals("cliente")){
			btSms.setVisibility(Button.INVISIBLE);
		}
		
		TextView textoCodigo = (TextView)findViewById(R.id.detalheOrcamento_codigo);
		textoCodigo.setText("Cód. Serv " + String.valueOf(servico.getIdServico()));
		
		TextView textoStatusOrcamento = (TextView)findViewById(R.id.detalheOrcamento_statusOrcamento);
		if(servico.getOrcamento().isOrcamentoLido()){
			textoStatusOrcamento.setText("Lido");
			textoStatusOrcamento.setTextColor(Color.GREEN);
		}else{
			textoStatusOrcamento.setText("Não lido");
			textoStatusOrcamento.setTextColor(Color.RED);
		}
		TextView textoStatusOrcamentoResposta = (TextView)findViewById(R.id.detalheOrcamento_statusOrcamentoResposta);
		if(servico.getOrcamento().isOrcamentoRespondido()){
			textoStatusOrcamentoResposta.setText("Respondido");
			textoStatusOrcamentoResposta.setTextColor(Color.GREEN);
		}else{
			textoStatusOrcamentoResposta.setText("Não Respondido");
			textoStatusOrcamentoResposta.setTextColor(Color.RED);
		}
		
		TextView textoOrigem = (TextView)findViewById(R.id.detalheOrcamento_origem);		
		textoOrigem.setText(servico.getOrcamento().getDetalheOrigem());		
		
		TextView textoDestino = (TextView)findViewById(R.id.detalheOrcamento_destino);
		textoDestino.setText(servico.getOrcamento().getDetalheDestino());		
		
		TextView textoPeso = (TextView)findViewById(R.id.detalheOrcamento_peso);
		textoPeso.setText(servico.getOrcamento().getPeso());
		
		TextView textoDimensao = (TextView)findViewById(R.id.detalheOrcamento_dimensao);
		textoDimensao.setText(servico.getOrcamento().getDimensao());
		
		TextView textoValor = (TextView)findViewById(R.id.detalheOrcamento_valor);
		textoValor.setText("R$ " + String.valueOf(servico.getValor()));
		
		TextView textoMensagem = (TextView)findViewById(R.id.detalheOrcamento_mensagem);
		textoMensagem.setText(servico.getOrcamento().getMensagem());
		
		Button btResposta = (Button)findViewById(R.id.detalheOrcamento_btResposta);		
		btResposta.setOnClickListener(new OnClickListener() {			
			public void onClick(View v) {
				//Intent intent = new Intent(DetalheServico.this, ResponderOrcamento.class);				
				//intent.putExtra("servico", servico);
				//intent.putExtra("clienteAdm", clienteAdm);
				//startActivity(intent);
			}
		});
		if(servico.getOrcamento().isOrcamentoRespondido()){
			btResposta.setEnabled(false);
		}
		if(tipoDoLayout.equals("cliente")){
			btResposta.setVisibility(Button.INVISIBLE);
		}
		
		Button btExcluir = (Button)findViewById(R.id.detalheOrcamento_btExcluir);
		btExcluir.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				// Alert dialog com sim ou não
				AlertDialog.Builder alerta = new AlertDialog.Builder(DetalheServico.this);
				alerta.setIcon(R.drawable.icon_aguia);
				alerta.setTitle("Excluir Orçamento");
				alerta.setMessage("Tem certeza que deseja excluir este orçamento?");
				
				// configurações dos botões.
				alerta.setPositiveButton("Sim", new DialogInterface.OnClickListener() {					
					public void onClick(DialogInterface dialog, int which) {
						HttpOrcamento httpOrcamento = new HttpOrcamento();
						String resultado = httpOrcamento.doPost(URL_DELETAR_SERVICO, Parametros.getIdParams(servico.getIdServico()));						
						
						if(resultado.startsWith("ERRO")){
							Intent intent = new Intent(DetalheServico.this, TelaErro.class);
							intent.putExtra("mensagemErro", resultado);
							intent.putExtra("clienteAdm", clienteAdm);
							startActivity(intent);
						}else{
							Toast.makeText(DetalheServico.this, "Orçamento excluído com sucesso.", Toast.LENGTH_SHORT).show();						
							if(tipoDoLayout.equals("cliente")){
								startActivity(new Intent(DetalheServico.this, MenuCliente.class).putExtra("cliente", cliente));
							}else{
								startActivity(new Intent(DetalheServico.this, MenuAdmin.class).putExtra("clienteAdm", clienteAdm));
							}							
						}												
					}
				});
				
				alerta.setNegativeButton("Não", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {}
				});
				alerta.show();
				
			}
		});
		if(tipoDoLayout.equals("cliente") || servico.getDataEntrega() == null){
			btExcluir.setVisibility(Button.INVISIBLE);
		}
		
		Button btEmail = (Button)findViewById(R.id.detalheOrcamento_btEmail);
		btEmail.setOnClickListener(new OnClickListener() {			
			public void onClick(View v) {
				Intent intent = new Intent(DetalheServico.this, EnviarEmail.class);
				intent.putExtra("nome", servico.getOrcamento().getCliente().getNome());
				intent.putExtra("email", servico.getOrcamento().getCliente().getEmail());
				startActivity(intent);
			}
		});
		if(tipoDoLayout.equals("cliente")){
			btEmail.setVisibility(Button.INVISIBLE);
		}
		
		Button btMenu = (Button) findViewById(R.id.detalheOrcamento_btMenu);
		btMenu.setOnClickListener(new OnClickListener() {			
			public void onClick(View v) {
				if(tipoDoLayout.equals("cliente")){
					startActivity(new Intent(DetalheServico.this, MenuCliente.class).putExtra("cliente", cliente));
				}else{
					startActivity(new Intent(DetalheServico.this, MenuAdmin.class).putExtra("clienteAdm", clienteAdm));
				}						
			}
		});
	}
}