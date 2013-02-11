package br.com.cesaretransportesMobile.activity;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import br.com.cesaretransportesMobile.adapter.Menu;
import br.com.cesaretransportesMobile.adapter.MenuPrincipalAdapter;
import br.com.cesaretransportesMobile.domain.Cliente;

public class MenuCliente extends ListActivity{
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setListAdapter(new MenuPrincipalAdapter(this, Menu.getMenuCliente()));
	}
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		Cliente cliente = (Cliente) getIntent().getSerializableExtra("cliente");
		Intent intent;
		switch (position) {
		case 0:
			intent = new Intent(this, ClientePerfil.class);
			intent.putExtra("cliente", cliente);
			startActivity(intent);
			break;
			
		case 1:
			intent = new Intent(this, OpcaoOrcamento.class);
			intent.putExtra("cliente", cliente);			
			startActivity(intent);
			break;
			
		case 2:
			intent = new Intent(this, ClienteContato.class);
			intent.putExtra("cliente", cliente);			
			startActivity(intent);
			break;	
			
		case 3:
			startActivity(new Intent(this, Login.class));
			break;

		default:
			break;
		}
	}

}
