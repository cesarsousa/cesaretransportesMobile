package br.com.cesaretransportesMobile.adapter;

import java.util.List;

import br.com.cesaretransportesMobile.activity.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;

/**
 * Classe adaptadora para exibir o menu do adminitrador.
 */
public class MenuPrincipalAdapter extends BaseAdapter implements ListAdapter {
	
	private Context context;
	List<TextoMenuPrincipal> opcaoMenu;

	public MenuPrincipalAdapter(Context context, List<TextoMenuPrincipal> opcaoMenu) {
		this.context = context;
		this.opcaoMenu = opcaoMenu;
	}

	@Override
	public int getCount() {		
		return opcaoMenu.size();
	}

	@Override
	public Object getItem(int position) {
		return opcaoMenu.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {
		// recupera as opções de menu da posição
		TextoMenuPrincipal menu = opcaoMenu.get(position);
		
		LayoutInflater inflater = 
			(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View currentView = inflater.inflate(R.layout.list_adapter_menuprincipal, null);
		
		TextView texto = (TextView) currentView.findViewById(R.id.listAdapterMenuPrincipal_texto);
		texto.setText(menu.getTextoPrincipal());
		
		TextView detalhe = (TextView) currentView.findViewById(R.id.listAdapterMenuPrincipal_detalhe);
		detalhe.setText(menu.getTextoDetalhe());
		
		return currentView;
	}

}
