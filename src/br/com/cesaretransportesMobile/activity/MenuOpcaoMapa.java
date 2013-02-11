package br.com.cesaretransportesMobile.activity;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import android.app.ListActivity;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;
import br.com.cesaretransportesMobile.adapter.EnderecoAdapter;

public class MenuOpcaoMapa extends ListActivity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		String enderecoOrigem = getIntent().getStringExtra("endereco");
		
		try {
			Geocoder geocoder = new Geocoder(this, Locale.getDefault());
			List<Address> enderecos = geocoder.getFromLocationName(enderecoOrigem, 5);		
			
			Toast.makeText(this, "listar endereços" + enderecos.size(), Toast.LENGTH_LONG);
			
			setListAdapter(new EnderecoAdapter(this, enderecoOrigem, enderecos));
		} catch (IOException e) {
			Toast.makeText(this, "Erro durante a busca do endereço: " + enderecoOrigem, Toast.LENGTH_LONG).show();
		}	
	}
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		
		// recupera o endereço da posição selecionada
		Address endereco = (Address) this.getListAdapter().getItem(position);
		
		Toast.makeText(this, "gerar mapa pra " + endereco.toString(), Toast.LENGTH_LONG);
		
		// gera o mapa do endereço selecionado
		startActivity(new Intent(MenuOpcaoMapa.this, Mapa.class).putExtra("endereco", endereco));
	}

}
