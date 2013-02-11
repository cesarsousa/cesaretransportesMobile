package br.com.cesaretransportesMobile.activity;

import java.util.List;

import br.com.cesaretransportesMobile.adapter.OrcamentoAdapter;
import br.com.cesaretransportesMobile.domain.Cliente;
import br.com.cesaretransportesMobile.domain.Orcamento;
import br.com.cesaretransportesMobile.util.TipoDoLayout;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

public class ClienteVisualizarOrcamentos extends ListActivity{
	/*
	 * Visão da listagem dos orçamentos no modo CLIENTE
	 */
	private Cliente cliente;
	
	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {		
		super.onCreate(savedInstanceState);
		setListAdapter(new OrcamentoAdapter(this, (List<Orcamento>) getIntent().getSerializableExtra("orcamentos"), TipoDoLayout.CLIENTE));
	}
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		
		// recupera o orçamento da posição desejada
		Orcamento orcamento = (Orcamento) this.getListAdapter().getItem(position);
		// recupera o cliente da seção
		cliente = (Cliente) getIntent().getSerializableExtra("cliente");	
		
		Intent intent = new Intent(this, DetalheOrcamento.class);
		intent.putExtra("orcamento", orcamento);
		intent.putExtra("cliente", cliente);
		intent.putExtra("tipoDoLayout", "cliente");
		startActivity(intent);
	}
}