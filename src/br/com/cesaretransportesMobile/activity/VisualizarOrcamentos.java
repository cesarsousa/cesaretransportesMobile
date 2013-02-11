package br.com.cesaretransportesMobile.activity;

import java.util.List;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import br.com.cesaretransportesMobile.adapter.OrcamentoAdapter;
import br.com.cesaretransportesMobile.domain.Cliente;
import br.com.cesaretransportesMobile.domain.Orcamento;
import br.com.cesaretransportesMobile.http.Http;
import br.com.cesaretransportesMobile.http.HttpOrcamento;
import br.com.cesaretransportesMobile.util.Parametros;
import br.com.cesaretransportesMobile.util.Servlet;
import br.com.cesaretransportesMobile.util.TipoDoLayout;

/**
 * <p>Tela admin</p>
 * <p>Classe utilizada para listar os or�amentos customizadamente.</p>
 * 
 * @author cesar sousa
 *
 */
public class VisualizarOrcamentos extends ListActivity{
	/*
	 * Vis�o da listagem dos or�amentos no modo ADMIN.
	 */
	
	private static final String UPDATE_ORCAMENTO = Http.SERVIDOR + Servlet.androidOrcamentoUpdateLido; 
	
	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);		
		setListAdapter(new OrcamentoAdapter(this, (List<Orcamento>) getIntent().getSerializableExtra("orcamentos"), TipoDoLayout.ADMIN));
	}
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		
		// recupera o or�amento da posi��o selecionada
		Orcamento orcamento = (Orcamento) this.getListAdapter().getItem(position);
		
		// recupera o administrador da se��o
		Cliente clienteAdm = (Cliente) getIntent().getSerializableExtra("clienteAdm");
		
		if(!orcamento.isOrcamentoLido()){
			HttpOrcamento httpOrcamento = new HttpOrcamento();
			String resultado = httpOrcamento.doPost(UPDATE_ORCAMENTO, Parametros.getIdParams(orcamento.getIdOrcamento()));
			
			if(resultado.startsWith("ERRO")){
				Intent intent = new Intent(VisualizarOrcamentos.this, TelaErro.class);
				intent.putExtra("mensagemErro", resultado);
				intent.putExtra("clienteAdm", clienteAdm);
				startActivity(intent);
			}			
		}	
		
		Intent intent = new Intent(this, DetalheOrcamento.class);
		intent.putExtra("orcamento", orcamento);
		intent.putExtra("clienteAdm", clienteAdm);
		intent.putExtra("tipoDoLayout", "admin");
		startActivity(intent);
	}
}
