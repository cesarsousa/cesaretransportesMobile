package br.com.cesaretransportesMobile.adapter;

import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;
import br.com.cesaretransportesMobile.activity.R;
import br.com.cesaretransportesMobile.domain.Cliente;
import br.com.cesaretransportesMobile.util.CesareUtil;

/**
 * Classe adaptadora para listar customizadamente os clientes. 
 * 
 * @author cesar sousa.
 *
 */
public class ClienteAdapter extends BaseAdapter implements ListAdapter {
	
	private Context context;
	private List<Cliente> clientes;	

	public ClienteAdapter(Context context, List<Cliente> clientes) {
		this.context = context;
		this.clientes = clientes;
	}

	@Override
	public int getCount() {
		return clientes.size();
	}

	@Override
	public Object getItem(int position) {
		return clientes.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {
		// Recupera o cliente da posição
		Cliente cliente = clientes.get(position);
		
		LayoutInflater inflater = 
			(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View clienteView = inflater.inflate(R.layout.list_adapter_clientes, null);
		
		// atualiza os valores do TextView para os dados do cliente
		TextView textViewNome = (TextView) clienteView.findViewById(R.id.listAdapterCliente_nome);
		textViewNome.setText("Cod." + cliente.getIdCliente() + " " + cliente.getNome());
		// codigo rgb para a cor laranja
		textViewNome.setTextColor(Color.rgb(255, 165, 0));
		
		TextView textViewDataCadastro = (TextView) clienteView.findViewById(R.id.listAdapterCliente_dataCadastro);
		textViewDataCadastro.setText(CesareUtil.formatarData(cliente.getDataCadastro(), "dd/MM/yyyy"));
		
		TextView textViewStatusCliente = (TextView) clienteView.findViewById(R.id.listAdapterCliente_statusCliente);
		textViewStatusCliente.setText(cliente.isStatusCliente() ? "Confirmado" : "Pendente");
		textViewStatusCliente.setTextColor(cliente.isStatusCliente() ? Color.GREEN : Color.RED);		
		
		return clienteView;
	}

}
