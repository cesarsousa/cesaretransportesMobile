package br.com.cesaretransportesMobile.activity;

import java.util.List;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import br.com.cesaretransportesMobile.adapter.ClienteAdapter;
import br.com.cesaretransportesMobile.domain.Cliente;


/**
 * <p>Tela admin</p>
 * <p>Classe utilizada para listar os clientes customizadamente.</p>
 * 
 * @author cesar sousa
 *
 */
public class VisualizarClientes extends ListActivity {
	
	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);		
		setListAdapter(new ClienteAdapter(this, (List<Cliente>) getIntent().getSerializableExtra("clientes")));			
	}
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {		
		super.onListItemClick(l, v, position, id);
		// recupera o cliente da posição clicada
		Cliente cliente = (Cliente)this.getListAdapter().getItem(position);	
		
		// recupera o administrador da seção
		Cliente clienteAdm = (Cliente) getIntent().getSerializableExtra("clienteAdm");
		
		Intent intent = new Intent(this, DetalheCliente.class);		
		intent.putExtra("cliente", cliente);
		intent.putExtra("clienteAdm", clienteAdm);
		startActivity(intent);
		}
}