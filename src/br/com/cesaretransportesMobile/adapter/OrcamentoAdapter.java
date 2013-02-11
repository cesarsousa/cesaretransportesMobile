package br.com.cesaretransportesMobile.adapter;

import java.util.List;

import br.com.cesaretransportesMobile.activity.R;
import br.com.cesaretransportesMobile.domain.Orcamento;
import br.com.cesaretransportesMobile.util.CesareUtil;
import br.com.cesaretransportesMobile.util.TipoDoLayout;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;

/**
 * Classe adaptadora para listar customizadamente os orçamentos.
 * 
 * @author cesar sousa.
 *
 */
public class OrcamentoAdapter extends BaseAdapter implements ListAdapter {
	
	private Context context;
	private List<Orcamento> orcamentos;
	private TipoDoLayout tipoDoLayout;

	public OrcamentoAdapter(Context context, List<Orcamento> orcamentos, TipoDoLayout tipoDoLayout) {
		this.context = context;
		this.orcamentos = orcamentos;
		this.tipoDoLayout = tipoDoLayout;
	}

	@Override
	public int getCount() {		
		return orcamentos.size();
	}

	@Override
	public Object getItem(int position) {		
		return orcamentos.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {		
			// Recupera o orçamento da posição
			Orcamento orcamento = orcamentos.get(position);
			
			LayoutInflater inflater = 
				(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View orcamentoView = inflater.inflate(R.layout.list_adapter_orcamento, null);
			
			TextView textViewNome = (TextView)orcamentoView.findViewById(R.id.listAdapterOrcamento_nome);
			textViewNome.setTextColor(Color.GRAY);		
			
			if(TipoDoLayout.ADMIN == tipoDoLayout){				
				textViewNome.setText("Cod." + orcamento.getIdOrcamento() + " " + (orcamento.getCliente().getNome().length() > 15 ? orcamento.getCliente().getNome().substring(0, 15) + "..." : orcamento.getCliente().getNome()));
			}else{
				textViewNome.setText("Cod." + orcamento.getIdOrcamento());
			}
			
			TextView textViewData = (TextView)orcamentoView.findViewById(R.id.listAdapterOrcamento_data);
			textViewData.setText(CesareUtil.formatarData(orcamento.getDataCadastro(), "dd/MM/yyyy"));
			
			TextView textViewOrigem = (TextView)orcamentoView.findViewById(R.id.listAdapterOrcamento_origem);
			//textViewOrigem.setText(orcamento.getDetalheOrigem().length() > 50 ? "De: " + orcamento.getDetalheOrigem().substring(0, 50) + "..." : "De: " + orcamento.getDetalheOrigem());
			textViewOrigem.setText("De: " + orcamento.getDetalheOrigem());
			
			TextView textViewDestino = (TextView)orcamentoView.findViewById(R.id.listAdapterOrcamento_destino);
			//textViewDestino.setText(orcamento.getDetalheDestino().length() > 50 ? "Para: " + orcamento.getDetalheDestino().substring(0, 50) + "..." : "Para: " + orcamento.getDetalheDestino());
			textViewDestino.setText("Para: " + orcamento.getDetalheDestino());
			
			TextView textStatusLido = (TextView) orcamentoView.findViewById(R.id.listAdapterOrcamento_statusLido);
			if(orcamento.isOrcamentoLido()){
				textStatusLido.setText("LIDO");
				textStatusLido.setTextColor(Color.GREEN);
			}else{
				textStatusLido.setText("NAO LIDO");
				textStatusLido.setTextColor(Color.RED);
			}
			
			TextView textStatusRespondido = (TextView) orcamentoView.findViewById(R.id.listAdapterOrcamento_statusRespondido);
			if(orcamento.isOrcamentoRespondido()){
				textStatusRespondido.setText("RESPONDIDO");
				textStatusRespondido.setTextColor(Color.GREEN);
			}else{
				textStatusRespondido.setText("NAO RESPONDIDO");
				textStatusRespondido.setTextColor(Color.RED);
			}
			
			return orcamentoView;		
	}
}
