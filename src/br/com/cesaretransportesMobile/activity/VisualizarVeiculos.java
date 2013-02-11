package br.com.cesaretransportesMobile.activity;

import java.io.IOException;
import java.util.List;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import br.com.cesaretransportesMobile.adapter.VeiculoAdapter;
import br.com.cesaretransportesMobile.domain.Cliente;
import br.com.cesaretransportesMobile.domain.Veiculo;
import br.com.cesaretransportesMobile.http.Http;
import br.com.cesaretransportesMobile.http.HttpVeiculo;
import br.com.cesaretransportesMobile.util.Servlet;

/**
 * <p>Tela admin</p>
 * <p>Classe utilizada para listar os veículos customizadamente.</p>
 * <p>Esta classe utiliza a variável de classe <code>codigoOrigem</code> para retornar
 * para a activity que solicita a listagem do veículo.</p>
 * 
 * @author cesar sousa
 *
 */
public class VisualizarVeiculos extends ListActivity{
	
	private static final String URL_GET_VEICULOS = Http.SERVIDOR + Servlet.androidVeiculoVisualizarTodos;
	private static final String MENSAGEM = "Download Veículos. Erro para efetuar a comunicação com Cesare Transportes.";
	
	private int codigoOrigem = 0;
	List<Veiculo> veiculos;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		HttpVeiculo httpVeiculo = new HttpVeiculo();
	
		
		try {
			veiculos = httpVeiculo.downloadVeiculos(URL_GET_VEICULOS);
		} catch (IOException e1) {			
			e1.printStackTrace();
			Log.e(Http.LOGGER, MENSAGEM);
		}
		
		/*
		 * Flag enviada pela activity ConfirmarOrcamentoComoServico para retornar
		 * o id do veiculo selecionado
		 */
		if(Boolean.parseBoolean(getIntent().getStringExtra("retornarId"))){
			codigoOrigem = 1;
		}		
			
		setListAdapter(new VeiculoAdapter(this, veiculos));
				
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {		
		super.onListItemClick(l, v, position, id);
		
		// recupera o veiculo da posição clicada
		Veiculo veiculo = (Veiculo) this.getListAdapter().getItem(position);
		
		
		if(codigoOrigem == 1){
			Intent intent = new Intent();
			intent.putExtra("idVeiculo", veiculo.getIdVeiculo());
			intent.putExtra("placa", veiculo.getPlaca());
			setResult(1, intent);
			finish();
		}else{
			// recupera o administrador da seção
			Cliente clienteAdm = (Cliente) getIntent().getSerializableExtra("clienteAdm");
		
			Intent intent = new Intent(this, DetalheVeiculo.class);
			intent.putExtra("veiculo", veiculo);
			intent.putExtra("tipoDoLayout", "admin");
			intent.putExtra("clienteAdm", clienteAdm);
			startActivity(intent);
		}
	}
}