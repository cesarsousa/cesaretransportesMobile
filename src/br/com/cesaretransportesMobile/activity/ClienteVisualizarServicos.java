package br.com.cesaretransportesMobile.activity;

import java.util.List;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import br.com.cesaretransportesMobile.adapter.ServicoAdapter;
import br.com.cesaretransportesMobile.domain.Cliente;
import br.com.cesaretransportesMobile.domain.Servico;
import br.com.cesaretransportesMobile.util.TipoDoLayout;

public class ClienteVisualizarServicos extends ListActivity {
	
	/*
	 * Visão da listagem dos serviços no modo CLIENTE
	 */
	private Cliente cliente;
	
	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {		
		super.onCreate(savedInstanceState);
		setListAdapter(new ServicoAdapter(this, (List<Servico>)getIntent().getSerializableExtra("servicos"), TipoDoLayout.CLIENTE));
	}
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		
		// recupera o serviço da posição desejada
		Servico servico = (Servico) getListAdapter().getItem(position);
		// recupera o cliente da seção
		cliente = (Cliente) getIntent().getSerializableExtra("cliente");
		
		Intent intent = new Intent(this, DetalheServico.class);
		intent.putExtra("servico", servico);
		intent.putExtra("cliente", cliente);
		intent.putExtra("tipoDoLayout", "cliente");
		startActivity(intent);		
	}
}