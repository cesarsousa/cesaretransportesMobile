package br.com.cesaretransportesMobile.activity;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;

import android.app.Activity;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import br.com.cesaretransportesMobile.domain.Cliente;
import br.com.cesaretransportesMobile.domain.Orcamento;
import br.com.cesaretransportesMobile.http.Http;
import br.com.cesaretransportesMobile.http.HttpOrcamento;
import br.com.cesaretransportesMobile.util.Parametros;
import br.com.cesaretransportesMobile.util.Servlet;

/**
 * <p>Tela cliente</p>
 * <p>Esta classe define os acessos ao mapas dos veículos.</p>
 * 
 * @author cesar sousa
 *
 */
public class ClienteVeiculo extends Activity{
	
	private static final String URL_LISTAR_SERVICOS = Http.SERVIDOR + Servlet.androidServicosPorCliente;
	private static final String MENSAGEM = "Erro para efetuar a conexão com Cesare Transportes para listar orçamentos do cliente.";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.cliente_veiculo);
		
		final Cliente cliente = (Cliente) getIntent().getSerializableExtra("cliente");
		
		try {
			cliente.setOrcamentos(new HttpOrcamento().downloadOrcamentos(URL_LISTAR_SERVICOS, Parametros.getIdParams(cliente.getIdCliente())));
			
			TextView textoResultado = (TextView) findViewById(R.id.clienteVeiculo_textoResultado);
			Button btTextoResultado = (Button) findViewById(R.id.clienteVeiculo_btTextoResultado);
			TextView textoVeiculo = (TextView) findViewById(R.id.clienteVeiculo_textoVeiculos);
			Button btTextoVeiculos = (Button) findViewById(R.id.clienteVeiculo_btTextoVeiculos);
			
			if(cliente.getOrcamentos().size() == 0){
				// cliente não possui nenhuma entrega em andamento.
				textoVeiculo.setVisibility(TextView.INVISIBLE);
				btTextoVeiculos.setVisibility(Button.INVISIBLE);
				
				btTextoResultado.setOnClickListener(new OnClickListener() {
					public void onClick(View v) {
						finish();						
					}
				});
			}else{
				textoResultado.setVisibility(TextView.INVISIBLE);
				btTextoResultado.setVisibility(Button.INVISIBLE);
				
				textoVeiculo.setText("Você possui " + cliente.getOrcamentos().size() + " serviço(s) em andamento");
				btTextoVeiculos.setOnClickListener(new OnClickListener() {
					public void onClick(View v) {
						Intent intent = new Intent(ClienteVeiculo.this, VisualizarVeiculos.class);
						intent.putExtra("gerarMapa", "true");
						intent.putExtra("orcamentos", (ArrayList<Orcamento>) cliente.getOrcamentos());
						startActivityForResult(intent, 2);						
					}
				});				
			}
			
			Button btCancelar = (Button) findViewById(R.id.clienteVeiculo_btCancelar);
			btCancelar.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					finish();					
				}
			});
			
			Button btMenu = (Button) findViewById(R.id.clienteVeiculo_btMenu);
			btMenu.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					startActivity(new Intent(ClienteVeiculo.this, MenuCliente.class).putExtra("cliente", cliente));					
				}
			});
		} catch (IOException e) {
			Log.e(Http.LOGGER, e.getMessage(), e);
			Intent intent = new Intent(ClienteVeiculo.this, TelaErro.class);
			intent.putExtra("mensagemErro", getClass().getSimpleName() + ": " + MENSAGEM);
			startActivity(intent);
		}				
	}
	
	@Override
	protected void onActivityResult(int codigoOrigem, int resultado, Intent intent) {
		if(codigoOrigem == 2){
			String endereco = intent.getStringExtra("endereco");
			try {
				Geocoder geocoder = new Geocoder(this, Locale.getDefault());
				Address address = new Mapa().getEndereco(endereco, geocoder);
				if (address == null){
					Toast.makeText(ClienteVeiculo.this, "Não foi possível gerar mapa para: " + endereco, Toast.LENGTH_LONG).show();
				}else{
					startActivity(new Intent(ClienteVeiculo.this, Mapa.class).putExtra("endereco", address));
				}				
			} catch (IOException e) {
				Toast.makeText(ClienteVeiculo.this, "Não foi possível gerar mapa para: " + endereco, Toast.LENGTH_LONG).show();
			}			
		}else{
			Toast.makeText(ClienteVeiculo.this, "veículo não definido", Toast.LENGTH_SHORT).show();
		}
	}
}
