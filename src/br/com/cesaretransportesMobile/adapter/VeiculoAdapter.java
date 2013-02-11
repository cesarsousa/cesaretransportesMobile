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
import br.com.cesaretransportesMobile.domain.Veiculo;

/**
 * Classe adaptadora para listar customizadamente os veículos
 * 
 * @author cesar sousa.
 *
 */
public class VeiculoAdapter extends BaseAdapter implements ListAdapter {
	
	private Context context;
	private List<Veiculo> veiculos;

	public VeiculoAdapter(Context context,
			List<Veiculo> veiculos) {
		this.context = context;
		this.veiculos = veiculos;
	}

	@Override
	public int getCount() {
		return veiculos.size();
	}

	@Override
	public Object getItem(int position) {
		return veiculos.get(position);
	}

	@Override
	public long getItemId(int position) {		
		return position;
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {
		// recupera o veiculo da posição
		Veiculo veiculo = veiculos.get(position);
		
		LayoutInflater inflater = 
			(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View veiculoView = inflater.inflate(R.layout.list_adapter_veiculos, null);
		
		// atualiza os valores
		TextView textViewNome = (TextView)veiculoView.findViewById(R.id.listAdapterVeiculo_placa);
		textViewNome.setText(veiculo.getInfoPlaca() + " - Cód " + String.valueOf(veiculo.getIdVeiculo()));
		// cogigo rgb para a cor laranja.
		textViewNome.setTextColor(Color.rgb(255, 165, 0));
		
		TextView textViewMarca = (TextView)veiculoView.findViewById(R.id.listAdapterVeiculo_marca);
		textViewMarca.setText(veiculo.getMarca());
		
		TextView textViewCor = (TextView)veiculoView.findViewById(R.id.listAdapterVeiculo_cor);
		textViewCor.setText(veiculo.getCor());
		
		TextView textViewSiuacao = (TextView)veiculoView.findViewById(R.id.listAdapterVeiculo_status);
		if (!veiculo.isEmRota()){
			textViewSiuacao.setText("Disponível");
			textViewSiuacao.setTextColor(Color.GREEN);
		}else{
			textViewSiuacao.setText("Em rota");
			textViewSiuacao.setTextColor(Color.RED);
		}
		
		TextView textViewLocalizao = (TextView) veiculoView.findViewById(R.id.listAdapterVeiculo_localizacao);
		textViewLocalizao.setText("".equals(veiculo.getLocalizacao()) ? "Localização não disponível" : veiculo.getLocalizacao());
		return veiculoView;
	}
}
