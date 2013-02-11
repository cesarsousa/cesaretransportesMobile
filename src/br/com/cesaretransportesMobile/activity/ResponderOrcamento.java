package br.com.cesaretransportesMobile.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
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
 * <p>Classe utilizada para enviar uma resposta a um orçamento via e-mail JavaMail Api.</p>
 * 
 * @author cesar sousa
 *
 */
public class ResponderOrcamento extends Activity implements Runnable{
	
	private static final String RESPONDER_ORCAMENTO = Http.SERVIDOR + Servlet.androidResponderOrcamento;
	
	private EditText campoResposta;
	private Orcamento orcamento;
	private Cliente clienteAdm;
	
	private Handler handler = new Handler();
	private ProgressDialog dialog;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.responder_orcamento);
		
		orcamento = (Orcamento) getIntent().getSerializableExtra("orcamento");
		clienteAdm = (Cliente) getIntent().getSerializableExtra("clienteAdm");
		
		TextView textCodigo = (TextView) findViewById(R.id.responderOrcamento_codigo);
		textCodigo.setText("Cód: " + String.valueOf(orcamento.getIdOrcamento()));
		
		TextView textNome = (TextView) findViewById(R.id.responderOrcamento_nome);
		textNome.setText(orcamento.getCliente().getNome());
		
		TextView textEmail = (TextView) findViewById(R.id.responderOrcamento_email);
		textEmail.setText(orcamento.getCliente().getEmail());
		
		TextView textTelefone = (TextView) findViewById(R.id.responderOrcamento_telefone);
		textTelefone.setText(CesareUtil.formatarTelefone(orcamento.getCliente().getTelefone().toString()));
		
		TextView textOrigem = (TextView) findViewById(R.id.responderOrcamento_origem);
		textOrigem.setText("De: " +  orcamento.getDetalheOrigem());
		
		TextView textDestino = (TextView) findViewById(R.id.responderOrcamento_destino);
		textDestino.setText("Para: " +  orcamento.getDetalheDestino());
		
		TextView textPeso = (TextView) findViewById(R.id.responderOrcamento_peso);
		textPeso.setText("Peso: " + orcamento.getPeso());
		
		TextView textDimensao = (TextView) findViewById(R.id.responderOrcamento_dimensao);
		textDimensao.setText("Dimensão: " + orcamento.getDimensao());
		
		TextView textMensagem = (TextView) findViewById(R.id.responderOrcamento_mensagem);
		textMensagem.setText("Mensagem: " + orcamento.getMensagem());
		
		Button btCancelar = (Button) findViewById(R.id.responderOrcamento_btCancelar);
		btCancelar.setOnClickListener(new OnClickListener() {			
			public void onClick(View v) {
				finish();				
			}
		});
		
		Button btEnviar = (Button) findViewById(R.id.responderOrcamento_btEnviar);
		btEnviar.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				campoResposta = (EditText) findViewById(R.id.responderOrcamento_textoResposta);
				if("".equals(campoResposta.getText().toString().trim())){
					Toast.makeText(ResponderOrcamento.this, "Por favor digite a mensagem.", Toast.LENGTH_SHORT).show();
					return;
				}
				
				// abre a tela de progresso e roda a solicitação em outra thread
				dialog = ProgressDialog.show(ResponderOrcamento.this, "Cesare Transportes", "Enviando resposta, por favor aguarde...", false, true);
				new Thread(ResponderOrcamento.this).start();				
			}
		});		
	}

	@Override
	public void run() {
		try {
			HttpOrcamento httpOrcamento = new HttpOrcamento();
			String mensagem = campoResposta.getText().toString().trim();
			final String resposta = httpOrcamento.doPost(RESPONDER_ORCAMENTO, 
					Parametros.getResponderOrcamentoParams(orcamento.getIdOrcamento(), mensagem));
			
			handler.post(new Runnable() {
				public void run() {
					if(resposta.startsWith("ERRO")){
						Intent intent = new Intent(ResponderOrcamento.this, TelaErro.class);
						intent.putExtra("mensagemErro", resposta);
						intent.putExtra("clienteAdm", clienteAdm);
						startActivity(intent);
					}else{
						orcamento.setOrcamentoLido(true);
						orcamento.setOrcamentoRespondido(true);
						orcamento.setMensagem(resposta);
						Intent intent = new Intent(ResponderOrcamento.this, DetalheOrcamento.class);
						intent.putExtra("orcamento", orcamento);
						intent.putExtra("tipoDoLayout", "admin");
						intent.putExtra("clienteAdm", clienteAdm);
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