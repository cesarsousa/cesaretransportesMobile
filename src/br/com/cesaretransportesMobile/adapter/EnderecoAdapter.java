package br.com.cesaretransportesMobile.adapter;

import java.util.List;

import android.content.Context;
import android.location.Address;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;
import br.com.cesaretransportesMobile.activity.R;

public class EnderecoAdapter extends BaseAdapter implements ListAdapter {
	
	private Context context;
	private String enderecoOrigem;
	private List<Address> enderecos;

	public EnderecoAdapter(Context context, String enderecoOrigem, List<Address> enderecos) {
		this.context = context;
		this.enderecoOrigem = enderecoOrigem;
		this.enderecos = enderecos;
	}

	@Override
	public int getCount() {
		return enderecos.size();
	}

	@Override
	public Object getItem(int position) {		
		return enderecos.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {
		// recupera o endereço
		Address endereco = enderecos.get(position);
		
		LayoutInflater inflater = 
			(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View currentView = inflater.inflate(R.layout.list_adapter_enderecos, null);
		
		TextView texto = (TextView) currentView.findViewById(R.id.listAdapterEndereco_texto);
		texto.setText(enderecoOrigem);
		
		TextView detalhe = (TextView) currentView.findViewById(R.id.listAdapterEndereco_detalhe);
		detalhe.setText(endereco.getAddressLine(0) + ", " + endereco.getAddressLine(1) + ", " + endereco.getAddressLine(2) + ", " + endereco.getAddressLine(3));
		
		return currentView;
	}

}
