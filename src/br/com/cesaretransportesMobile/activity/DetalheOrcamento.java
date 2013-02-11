package br.com.cesaretransportesMobile.activity;

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
import br.com.cesaretransportesMobile.domain.Orcamento;
import br.com.cesaretransportesMobile.http.Http;
import br.com.cesaretransportesMobile.http.HttpOrcamento;
import br.com.cesaretransportesMobile.util.CesareUtil;
import br.com.cesaretransportesMobile.util.Parametros;
import br.com.cesaretransportesMobile.util.Servlet;

/**
 * <p>Tela admin</p>
 * <p>Tela cliente</p>
 * <p>Utilizada para exibir um orçamento no detalhe, tanto em modo cliente como em modo administrador.</p>
 * <p>Esta Classe disponibiliza métodos para.</p>
 * <ul>
 * <li>Confirma um status de orçamento como serviço e como entregue, em modo admin.
 * <li>Fazer uma ligação telefônica para o cliente do orçamento, em modo admin.
 * <li>Enviar um torpedo SMS para o cliente do orçamento, em modo admin.
 * <li>Enviar e-mail via JavaMail Api para o cliente do orçamento, em modo admin.
 * <li>Fazer a exclusão (lógica) do cliente do sistema, em modo admin e modo cliente.
 * <li>Retornar ao Menu Principal, em modo admin e modo cliente.</li>
 * </ul>
 * @author cesar sousa
 *
 */
public class DetalheOrcamento extends Activity {
	
	private static final String URL_DELETAR = Http.SERVIDOR + Servlet.androidOrcamentoDeletar;	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.detalhe_orcamento);
		
		final String tipoDoLayout = getIntent().getStringExtra("tipoDoLayout");		
		
		// recupera o orçamento.
		final Orcamento orcamento = (Orcamento) getIntent().getSerializableExtra("orcamento");
		// recupera o cliente da seção
		final Cliente cliente = (Cliente) getIntent().getSerializableExtra("cliente");
		// recupera o administrador da seção
		final Cliente clienteAdm = (Cliente) getIntent().getSerializableExtra("clienteAdm");
		
		TextView textoStatusCliente = (TextView)findViewById(R.id.detalheOrcamento_statusCliente);
		textoStatusCliente.setText("Dados do Cliente");
		textoStatusCliente.setTextColor(Color.WHITE);
		
		LinearLayout linearLayout = (LinearLayout) findViewById(R.id.detalheOrcamento_layoutFotoVan);		
		Button btFotoVan = (Button) findViewById(R.id.detalheOrcamento_btFotoVan);
		linearLayout.setBackgroundResource(R.drawable.van_orcamento_30_30);
		btFotoVan.setBackgroundResource(R.drawable.van_orcamento_30_30);
		if(tipoDoLayout.equals("admin")){
			btFotoVan.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					Intent intent = new Intent(DetalheOrcamento.this, ConfirmarOrcamentoComoServico.class);
					intent.putExtra("orcamento", orcamento);
					intent.putExtra("clienteAdm", clienteAdm);
					startActivity(intent);					
				}
			});
		}	
		
		TextView textoData = (TextView)findViewById(R.id.detalheOrcamento_data);
		textoData.setText("Orçamento enviado em " + CesareUtil.formatarData(orcamento.getDataCadastro(), "dd/MM/yyyy"));
		
		TextView textoDataPrevEntrega = (TextView) findViewById(R.id.detalheOrcamento_dataPrevEntrega);
		String dataPrevEntrega = "Não Disponível";
		textoDataPrevEntrega.setText("Data prevista para entrega - " + dataPrevEntrega);
		
		TextView textoDataEntrega = (TextView) findViewById(R.id.detalheOrcamento_dataEntrega);
		String dataEntrega = "Não Disponível";
		textoDataEntrega.setText("Data da entrega - " + dataEntrega);
		
		TextView textoNome = (TextView)findViewById(R.id.detalheOrcamento_nome);
		textoNome.setText(orcamento.getCliente().getNome());
		
		TextView textoEmail = (TextView)findViewById(R.id.detalheOrcamento_email);
		textoEmail.setText(orcamento.getCliente().getEmail());
		
		TextView textoTelefone = (TextView)findViewById(R.id.detalheOrcamento_telefone);
		textoTelefone.setText(orcamento.getCliente().getTelefone().formatado());
		
		Button btLigar = (Button)findViewById(R.id.detalheOrcamento_btLigar);
		btLigar.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				Uri uri = Uri.parse("tel:" + orcamento.getCliente().getTelefone().getUri());
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
				Intent intent = new Intent(DetalheOrcamento.this, EscreverMensagem.class);
				intent.putExtra("telefone", orcamento.getCliente().getTelefone().getUri());
				intent.putExtra("nome", orcamento.getCliente().getNome());
				startActivity(intent);				
			}
		});
		if(tipoDoLayout.equals("cliente")){
			btSms.setVisibility(Button.INVISIBLE);
		}
		
		TextView textoCodigo = (TextView)findViewById(R.id.detalheOrcamento_codigo);
		textoCodigo.setText("Código " + String.valueOf(orcamento.getIdOrcamento()));
		
		TextView textoStatusOrcamento = (TextView)findViewById(R.id.detalheOrcamento_statusOrcamento);
		if(orcamento.isOrcamentoLido()){
			textoStatusOrcamento.setText("Lido");
			textoStatusOrcamento.setTextColor(Color.GREEN);
		}else{
			textoStatusOrcamento.setText("Não lido");
			textoStatusOrcamento.setTextColor(Color.RED);
		}
		TextView textoStatusOrcamentoResposta = (TextView)findViewById(R.id.detalheOrcamento_statusOrcamentoResposta);
		if(orcamento.getMensagem().startsWith("Res:")){
			textoStatusOrcamentoResposta.setText("Respondido");
			textoStatusOrcamentoResposta.setTextColor(Color.GREEN);
		}else{
			textoStatusOrcamentoResposta.setText("Não Respondido");
			textoStatusOrcamentoResposta.setTextColor(Color.RED);
		}
		
		TextView textoOrigem = (TextView)findViewById(R.id.detalheOrcamento_origem);		
		textoOrigem.setText(orcamento.getDetalheOrigem());		
		
		TextView textoDestino = (TextView)findViewById(R.id.detalheOrcamento_destino);
		textoDestino.setText(orcamento.getDetalheDestino());		
		
		TextView textoPeso = (TextView)findViewById(R.id.detalheOrcamento_peso);
		textoPeso.setText(orcamento.getPeso());
		
		TextView textoDimensao = (TextView)findViewById(R.id.detalheOrcamento_dimensao);
		textoDimensao.setText(orcamento.getDimensao());
		
		TextView textoValor = (TextView)findViewById(R.id.detalheOrcamento_valor);
		textoValor.setText("Não disponível");
		
		TextView textoMensagem = (TextView)findViewById(R.id.detalheOrcamento_mensagem);
		textoMensagem.setText(orcamento.getMensagem());
		
		Button btResposta = (Button)findViewById(R.id.detalheOrcamento_btResposta);		
		btResposta.setOnClickListener(new OnClickListener() {			
			public void onClick(View v) {
				Intent intent = new Intent(DetalheOrcamento.this, ResponderOrcamento.class);
				intent.putExtra("orcamento", orcamento);
				intent.putExtra("clienteAdm", clienteAdm);
				startActivity(intent);
			}
		});
		if(orcamento.getMensagem().startsWith("Res:")){
			btResposta.setEnabled(false);
		}
		if(tipoDoLayout.equals("cliente")){
			btResposta.setVisibility(Button.INVISIBLE);
		}
		
		Button btExcluir = (Button)findViewById(R.id.detalheOrcamento_btExcluir);
		btExcluir.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				// Alert dialog com sim ou não
				AlertDialog.Builder alerta = new AlertDialog.Builder(DetalheOrcamento.this);
				alerta.setIcon(R.drawable.icon_aguia);
				alerta.setTitle("Excluir Orçamento");
				alerta.setMessage("Tem certeza que deseja excluir este orçamento?");
				
				// configurações dos botões.
				alerta.setPositiveButton("Sim", new DialogInterface.OnClickListener() {					
					public void onClick(DialogInterface dialog, int which) {
						HttpOrcamento httpOrcamento = new HttpOrcamento();
						String resultado = httpOrcamento.doPost(URL_DELETAR, Parametros.getIdParams(orcamento.getIdOrcamento()));						
						
						if(resultado.startsWith("ERRO")){
							Intent intent = new Intent(DetalheOrcamento.this, TelaErro.class);
							intent.putExtra("mensagemErro", resultado);
							intent.putExtra("clienteAdm", clienteAdm);
							startActivity(intent);
						}else{
							Toast.makeText(DetalheOrcamento.this, "Orçamento excluído com sucesso.", Toast.LENGTH_SHORT).show();						
							if(tipoDoLayout.equals("cliente")){
								startActivity(new Intent(DetalheOrcamento.this, MenuCliente.class).putExtra("cliente", cliente));
							}else{
								startActivity(new Intent(DetalheOrcamento.this, MenuAdmin.class).putExtra("clienteAdm", clienteAdm));
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
		if(tipoDoLayout.equals("cliente")){
			btExcluir.setVisibility(Button.INVISIBLE);
		}
		
		Button btEmail = (Button)findViewById(R.id.detalheOrcamento_btEmail);
		btEmail.setOnClickListener(new OnClickListener() {			
			public void onClick(View v) {
				Intent intent = new Intent(DetalheOrcamento.this, EnviarEmail.class);
				intent.putExtra("nome", orcamento.getCliente().getNome());
				intent.putExtra("email", orcamento.getCliente().getEmail());
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
					startActivity(new Intent(DetalheOrcamento.this, MenuCliente.class).putExtra("cliente", cliente));
				}else{
					startActivity(new Intent(DetalheOrcamento.this, MenuAdmin.class).putExtra("clienteAdm", clienteAdm));
				}						
			}
		});	
	}
}