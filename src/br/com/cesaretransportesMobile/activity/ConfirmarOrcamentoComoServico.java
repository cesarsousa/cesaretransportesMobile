package br.com.cesaretransportesMobile.activity;

import java.util.Calendar;

import br.com.cesaretransportesMobile.domain.Cliente;
import br.com.cesaretransportesMobile.domain.Orcamento;
import br.com.cesaretransportesMobile.http.Http;
import br.com.cesaretransportesMobile.http.HttpOrcamento;
import br.com.cesaretransportesMobile.util.CesareUtil;
import br.com.cesaretransportesMobile.util.Parametros;
import br.com.cesaretransportesMobile.util.Servlet;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * <p>Tela admin</p>
 * <p>Classe utilizada para confirmar um orçamento em uma ordem de serviço.</p>
 * 
 * @author cesar sousa
 *
 */
public class ConfirmarOrcamentoComoServico extends Activity implements Runnable{
	
	private static final String URL_CONFIRMAR = Http.SERVIDOR + Servlet.androidConfirmarOrcamento;
	
	private ProgressDialog dialog;
	private Handler handler = new Handler();
	private Calendar dataPrevEntrega = null;
	private Button btDataEntrega;
	private int idVeiculo;
	private Button btVeiculo;
	private Cliente clienteAdm;
	private Orcamento orcamento;
	private String valor;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);		
		setContentView(R.layout.confirmar_servico);
		
		orcamento = (Orcamento) getIntent().getSerializableExtra("orcamento");
		clienteAdm = (Cliente) getIntent().getSerializableExtra("clienteAdm");
		
		TextView textCodigo = (TextView) findViewById(R.id.confirmarServico_codigo);
		textCodigo.setText("Cod: " + orcamento.getIdOrcamento());
		
		TextView textNome = (TextView) findViewById(R.id.confirmarServico_nome);
		textNome.setText("Nome: " + orcamento.getCliente().getNome());
		
		TextView textOrigem = (TextView) findViewById(R.id.confirmarServico_origem);
		textOrigem.setText("Origem: " + orcamento.getDetalheOrigem());
		
		TextView textDestino = (TextView) findViewById(R.id.confirmarServico_destino);
		textDestino.setText("Destino: " + orcamento.getDetalheDestino());
		
		TextView textPeso = (TextView) findViewById(R.id.confirmarServico_peso);
		textPeso.setText("Peso: " + orcamento.getPeso());
		
		TextView textDimensao = (TextView) findViewById(R.id.confirmarServico_dimensao);
		textDimensao.setText("Dimensão: " + orcamento.getDimensao());
		
		
		btDataEntrega = (Button) findViewById(R.id.confirmarServico_btdata);
		btDataEntrega.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				showDialog(1);				
			}
		});
		
		btVeiculo = (Button) findViewById(R.id.confirmarServico_btVeiculo);
		btVeiculo.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(ConfirmarOrcamentoComoServico.this, VisualizarVeiculos.class);
				intent.putExtra("retornarId", "true");
				startActivityForResult(intent, 1);				
			}
		});
		
		Button btConfirmar = (Button) findViewById(R.id.confirmarServico_btConfirmar);
		btConfirmar.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				EditText campoValor = (EditText) findViewById(R.id.confirmarServico_valor);
				valor = campoValor.getText().toString().trim();
				
				if(valor.equals("") || !valor.matches("(\\d+,\\d{2})|(\\d+)")){
					Toast.makeText(ConfirmarOrcamentoComoServico.this, "Digite um valor. Campo deve ser numérico e separdado por vírgula", Toast.LENGTH_LONG).show();
					return;
				}				
				if (dataPrevEntrega == null){
					Toast.makeText(ConfirmarOrcamentoComoServico.this, "Escolha a data da entrega", Toast.LENGTH_SHORT).show();
					return;
				}
				if (idVeiculo == 0){
					Toast.makeText(ConfirmarOrcamentoComoServico.this, "Escolha o veículo para a entrega", Toast.LENGTH_SHORT).show();
					return;
				}
				
				dialog = ProgressDialog.show(ConfirmarOrcamentoComoServico.this, "Cesare Transportes", "Confirmando serviço, por favor aguarde...", false, true);
				
				// delega para outra thread o acesso ao servidor.
				new Thread(ConfirmarOrcamentoComoServico.this).start();
			}
		});
		
		Button btCancelar = (Button) findViewById(R.id.confirmarServico_btCancelar);
		btCancelar.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				finish();				
			}
		});
		
		Button btMenu = (Button) findViewById(R.id.confirmarServico_btMenu);
		btMenu.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				startActivity(new Intent(ConfirmarOrcamentoComoServico.this, MenuAdmin.class).putExtra("clienteAdm", clienteAdm));				
			}
		});		
	}
	
	@Override
	protected void onActivityResult(int codigoOrigem, int resultado, Intent intent) {
		try {
			if(codigoOrigem == 1){
				idVeiculo = intent.getIntExtra("idVeiculo", 0);
				String placa = intent.getStringExtra("placa");
				btVeiculo.setText(idVeiculo + " - " + CesareUtil.formatarPlaca(placa));
			}else{
				Toast.makeText(ConfirmarOrcamentoComoServico.this, "veículo não definido", Toast.LENGTH_SHORT).show();
			}			
		} catch (RuntimeException e) {
			Toast.makeText(ConfirmarOrcamentoComoServico.this, "veículo não definido", Toast.LENGTH_SHORT).show();
		}		
	}
	
	@Override
	protected Dialog onCreateDialog(int id) {		
		Calendar dtHoje = Calendar.getInstance();
		int ano = dtHoje.get(Calendar.YEAR);
		int mes = dtHoje.get(Calendar.MONTH);
		int dia = dtHoje.get(Calendar.DAY_OF_MONTH);
		
		return new DatePickerDialog(this, DateSetListenerSaida, ano, mes, dia);	
	}	
	private DatePickerDialog.OnDateSetListener DateSetListenerSaida = new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        	Calendar novaData = Calendar.getInstance();
        	novaData.set(year, monthOfYear, dayOfMonth);
        	dataPrevEntrega = novaData;
        	btDataEntrega.setText(CesareUtil.formatarData(dataPrevEntrega, "dd/MM/yyyy"));        	
        }
    };

	@Override
	public void run() {
		try {
			final HttpOrcamento httpOrcamento = new HttpOrcamento();
			final String resultado = httpOrcamento.doPost(URL_CONFIRMAR, Parametros.getConfirmarOrcamentoParams("servico", orcamento.getIdOrcamento(), valor, dataPrevEntrega, idVeiculo));
			 handler.post(new Runnable() {
				public void run() {
					if(resultado.startsWith("ERRO")){
						Intent intent = new Intent(ConfirmarOrcamentoComoServico.this, TelaErro.class);
						intent.putExtra("mensagemErro", resultado);
						intent.putExtra("clienteAdm", clienteAdm);
						startActivity(intent);
					}else{
						Toast.makeText(ConfirmarOrcamentoComoServico.this, resultado, Toast.LENGTH_LONG).show();
						startActivity(new Intent(ConfirmarOrcamentoComoServico.this, MenuAdmin.class).putExtra("clienteAdm", clienteAdm));
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