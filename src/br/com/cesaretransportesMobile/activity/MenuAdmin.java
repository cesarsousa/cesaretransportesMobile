package br.com.cesaretransportesMobile.activity;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import br.com.cesaretransportesMobile.adapter.Menu;
import br.com.cesaretransportesMobile.adapter.MenuPrincipalAdapter;
import br.com.cesaretransportesMobile.domain.Cliente;

/**
 * <p>Tela admin</p>
 * <p>Menu principal do administrador.</p>
 * @author cesar sousa
 *
 */
public class MenuAdmin extends ListActivity{
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);		
		setListAdapter(new MenuPrincipalAdapter(this, Menu.getMenuAdm()));		
	}
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		Cliente clienteAdm = (Cliente) getIntent().getSerializableExtra("clienteAdm");
		switch (position) {
		case 0:
			startActivity(new Intent(this, OpcaoVisualizarClientes.class).putExtra("clienteAdm", clienteAdm));			
			break;
		case 1:
			startActivity(new Intent(this, OpcaoVisualizarOrcamentos.class).putExtra("clienteAdm", clienteAdm));			
			break;
		case 2:
			startActivity(new Intent(this, AcessarGmail.class).putExtra("clienteAdm", clienteAdm));			
			break;
		case 3:
			startActivity(new Intent(this, VisualizarVeiculos.class).putExtra("clienteAdm", clienteAdm));
			break;
		case 4:
			startActivity(new Intent(this, Login.class));
		default:
			finish();
		}
	}

}
