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
import br.com.cesaretransportesMobile.domain.Servico;
import br.com.cesaretransportesMobile.util.CesareUtil;
import br.com.cesaretransportesMobile.util.TipoDoLayout;

/**
 * Classe adaptadora para listar customizadamente os orçamentos.
 * 
 * @author cesar sousa.
 *
 */
public class ServicoAdapter extends BaseAdapter implements ListAdapter {
	
	private Context context;
	private List<Servico> servicos;
	private TipoDoLayout tipoDoLayout;

	public ServicoAdapter(Context context, List<Servico> servicos, TipoDoLayout tipoDoLayout) {
		this.context = context;
		this.servicos = servicos;
		this.tipoDoLayout = tipoDoLayout;
	}

	@Override
	public int getCount() {
		return servicos.size();
	}

	@Override
	public Object getItem(int position) {
		return servicos.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// Recupera o orçamento da posição
		Servico servico = servicos.get(position);
		
		LayoutInflater inflater = 
			(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View orcamentoView = inflater.inflate(R.layout.list_adapter_orcamento, null);
		
		TextView textViewNome = (TextView)orcamentoView.findViewById(R.id.listAdapterOrcamento_nome);
		if(servico.getDataEntrega() == null){
			// codigo rgd para a cor laranja
			textViewNome.setTextColor(Color.rgb(255, 165, 0));
		}else{
			textViewNome.setTextColor(Color.GREEN);
		}	
		
		if(TipoDoLayout.ADMIN == tipoDoLayout){				
			textViewNome.setText("Cod.Serv " + servico.getIdServico() + " " + (servico.getOrcamento().getCliente().getNome().length() > 15 ? servico.getOrcamento().getCliente().getNome().substring(0, 15) + "..." : servico.getOrcamento().getCliente().getNome()));
		}else{
			textViewNome.setText("Cod.Serv" + servico.getIdServico());
		}
		
		TextView textViewData = (TextView)orcamentoView.findViewById(R.id.listAdapterOrcamento_data);
		textViewData.setText(servico.getOrcamento().getDataCadastro() == null ? "Data não disponivel" : CesareUtil.formatarData(servico.getOrcamento().getDataCadastro(), "dd/MM/yyyy"));
		
		TextView textViewOrigem = (TextView)orcamentoView.findViewById(R.id.listAdapterOrcamento_origem);
		//textViewOrigem.setText(orcamento.getDetalheOrigem().length() > 50 ? "De: " + orcamento.getDetalheOrigem().substring(0, 50) + "..." : "De: " + orcamento.getDetalheOrigem());
		textViewOrigem.setText("De: " + servico.getOrcamento().getDetalheOrigem());
		
		TextView textViewDestino = (TextView)orcamentoView.findViewById(R.id.listAdapterOrcamento_destino);
		//textViewDestino.setText(orcamento.getDetalheDestino().length() > 50 ? "Para: " + orcamento.getDetalheDestino().substring(0, 50) + "..." : "Para: " + orcamento.getDetalheDestino());
		textViewDestino.setText("Para: " + servico.getOrcamento().getDetalheDestino());		
		
		return orcamentoView;
	}
}
