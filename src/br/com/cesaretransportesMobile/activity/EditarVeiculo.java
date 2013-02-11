package br.com.cesaretransportesMobile.activity;

import android.app.Activity;

public class EditarVeiculo extends Activity{
	
	/*private static final String URL_EDITAR = Http.SERVIDOR + Servlet.androidVeiculoEditar;
	
	private static final int DTSAIDA_DIALOG_ID = 0;
	private static final int DTCHEGADA_DIALOG_ID = 1;
	private static final int MENU_CONFIRMAR_ROTA = 0;
	private static final int MENU_ZERAR_DATAS = 1;
	
	private Veiculo veiculo;
	private Button btDtSaida;
	private Button btDtChegada;
	private TextView textPlaca;
	private EditText campoEnderecoOrigem;
	private EditText campoCidadeOrigem;
	private AutoCompleteTextView campoEstadoOrigem;
	private EditText campoEnderecoDestino;
	private EditText campoCidadeDestino;
	private AutoCompleteTextView campoEstadoDestino;
	private EditText campoLocalizacao;
	
	
	private static final String[] ESTADOS = new String[]{
		"AC", "AL", "AP", "AM", "BA", "CE", "DF", "ES", "GO", "MA", "MT", "MS",	"MG", "PA",
		"PB", "PR", "PE", "PI", "RN", "RS", "RJ", "RO", "RR", "SC", "SP", "SE", "TO"
	}; 
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);		
		setContentView(R.layout.update_veiculo);
		
		veiculo = (Veiculo) getIntent().getSerializableExtra("veiculo");
		
		
		 * adaptador para mostrar o AutoComplete do estado.
		 
		ArrayAdapter<String> adaptador = 
			new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, ESTADOS);
		
		
		 * Campos não configuráveis.
		 		
		textPlaca = (TextView)findViewById(R.id.editarVeiculo_placa);
		textPlaca.setText("Placa do veículo: " + veiculo.getInfoPlaca());
		
		TextView textMarca = (TextView)findViewById(R.id.editarVeiculo_marca);
		textMarca.setText(veiculo.getMarca().toUpperCase());
		
		TextView textCor = (TextView)findViewById(R.id.editarVeiculo_cor);
		textCor.setText(veiculo.getCor());
		
		
		 * Campos configuráveis.
		 
		btDtSaida = (Button) findViewById(R.id.editarVeiculo_btDtSaida);		
		if(veiculo.getDataSaida() != null){
			btDtSaida.setText(CesareUtil.formatarData(veiculo.getDataSaida(), "dd/MM/yyyy"));
		}
		btDtSaida.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				showDialog(DTSAIDA_DIALOG_ID);				
			}
		});
		
		campoEnderecoOrigem = (EditText)findViewById(R.id.editarVeiculo_enderecoOrigem);
		campoEnderecoOrigem.setText(veiculo.getEnderecoOrigem());
		
		campoCidadeOrigem = (EditText)findViewById(R.id.editarVeiculo_cidadeOrigem);
		campoCidadeOrigem.setText(veiculo.getOrigem());	
		
		campoEstadoOrigem = (AutoCompleteTextView) findViewById(R.id.editarVeiculo_estadoOrigem);
		campoEstadoOrigem.setAdapter(adaptador);
		campoEstadoOrigem.setText(veiculo.getEstadoOrigem());
		
		btDtChegada = (Button) findViewById(R.id.editarVeiculo_btDtChegada);
		if(veiculo.getDataChegada() != null){
			btDtChegada.setText(CesareUtil.formatarData(veiculo.getDataChegada(), "dd/MM/yyyy"));
		}
		btDtChegada.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				showDialog(DTCHEGADA_DIALOG_ID);				
			}
		});		
		
		campoEnderecoDestino = (EditText)findViewById(R.id.editarVeiculo_enderecoDestino);
		campoEnderecoDestino.setText(veiculo.getEnderecoDestino());
		
		campoCidadeDestino = (EditText)findViewById(R.id.editarVeiculo_cidadeDestino);
		campoCidadeDestino.setText(veiculo.getDestino());
		
		campoEstadoDestino = (AutoCompleteTextView) findViewById(R.id.editarVeiculo_estadoDestino);
		campoEstadoDestino.setAdapter(adaptador);
		campoEstadoDestino.setText(veiculo.getEstadoDestino());
		
		campoLocalizacao = (EditText)findViewById(R.id.editarVeiculo_localizacao);
		campoLocalizacao.setText(veiculo.getLocalizacao());
		
		Button btOK = (Button)findViewById(R.id.editarVeiculo_btOK);
		btOK.setOnClickListener(new OnClickListener() {			
			public void onClick(View v) {
				// validar o veiculo com os campos do texts
				// testar se data esta atualizando
				veiculo.setEnderecoOrigem(campoEnderecoOrigem.getText().toString());
				veiculo.setOrigem(campoCidadeOrigem.getText().toString());
				veiculo.setEstadoOrigem(campoEstadoOrigem.getText().toString());
				veiculo.setEnderecoDestino(campoEnderecoDestino.getText().toString());
				veiculo.setDestino(campoCidadeDestino.getText().toString());
				veiculo.setEstadoDestino(campoEstadoDestino.getText().toString());
				veiculo.setLocalizacao(campoLocalizacao.getText().toString());
				
				HttpVeiculo httpVeiculo = new HttpVeiculo();
				String resultado = httpVeiculo.doPost(URL_EDITAR, Parametros.getAlterarParams(veiculo));
				
				if (resultado.startsWith("ERRO")){
					Intent intent = new Intent(EditarVeiculo.this, TelaErro.class);
					intent.putExtra("mensagemErro", resultado);
					startActivity(intent);
				}else{
					Toast.makeText(EditarVeiculo.this, resultado, Toast.LENGTH_SHORT).show();
					startActivity(new Intent(EditarVeiculo.this, VisualizarVeiculos.class));
				}
			}
		});
		
		Button btCancelar = (Button)findViewById(R.id.editarVeiculo_btCancelar);
		btCancelar.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				finish();				
			}
		});
		
		Button btMenu = (Button)findViewById(R.id.editarVeiculo_btMenu);
		btMenu.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				startActivity(new Intent(EditarVeiculo.this, MenuAdmin.class));				
			}
		});
	}
	
	@Override
    protected Dialog onCreateDialog(int id) {
        Calendar calendario = Calendar.getInstance();
         
        int ano = calendario.get(Calendar.YEAR);
        int mes = calendario.get(Calendar.MONTH);
        int dia = calendario.get(Calendar.DAY_OF_MONTH);
         
        switch (id) {
        case DTSAIDA_DIALOG_ID:
        	if(veiculo.getDataSaida() != null){
        		ano = veiculo.getDataSaida().get(Calendar.YEAR);
        		mes = veiculo.getDataSaida().get(Calendar.MONTH);
        		dia = veiculo.getDataSaida().get(Calendar.DAY_OF_MONTH);
        	}
            return new DatePickerDialog(this, DateSetListenerSaida, ano, mes,
                    dia);
        
        case DTCHEGADA_DIALOG_ID:
        	if(veiculo.getDataChegada() != null){
        		ano = veiculo.getDataChegada().get(Calendar.YEAR);
        		mes = veiculo.getDataChegada().get(Calendar.MONTH);
        		dia = veiculo.getDataChegada().get(Calendar.DAY_OF_MONTH);
        	}
            return new DatePickerDialog(this, DateSetListenerChegada, ano, mes,
                    dia);
        }
        return null;
    }
	
	private DatePickerDialog.OnDateSetListener DateSetListenerSaida = new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        	Calendar novaDtSaida = Calendar.getInstance();
        	novaDtSaida.set(year, monthOfYear, dayOfMonth);
        	veiculo.setDataSaida(novaDtSaida);
        	btDtSaida.setText(CesareUtil.formatarData(veiculo.getDataSaida(), "dd/MM/yyyy"));
        }
    };
    
    private DatePickerDialog.OnDateSetListener DateSetListenerChegada = new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            Calendar novaDtChegada = Calendar.getInstance();
            novaDtChegada.set(year, monthOfYear, dayOfMonth);
            veiculo.setDataChegada(novaDtChegada);
            btDtChegada.setText(CesareUtil.formatarData(veiculo.getDataChegada(), "dd/MM/yyyy"));
        }
    };
    
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
    	MenuItem item = menu.add(0, MENU_CONFIRMAR_ROTA, 0, "Finalizar Rota");
    	item.setIcon(R.drawable.menu_calendario); 
    	item = menu.add(0, MENU_ZERAR_DATAS, 0, "Excluir Datas");
    	item.setIcon(R.drawable.menu_confirmar);
    	return true;
    };
    
    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
    	switch(item.getItemId()){
    	case MENU_CONFIRMAR_ROTA:
    		HttpVeiculo httpVeiculo = new HttpVeiculo();
    		Map<String, String> params = Parametros.getIdParams(veiculo.getIdVeiculo());
    		params.put("finalizarRota", "true");
    		
    		String resultado = httpVeiculo.doPost(URL_EDITAR, params);
    		
    		if(resultado.startsWith("ERRO")){
    			Intent intent = new Intent(EditarVeiculo.this, TelaErro.class);
    			intent.putExtra("mensagemErro", resultado);
    			startActivity(intent);
    			return true;
    		}else{
    			Toast.makeText(EditarVeiculo.this, "O veículo " + veiculo.getInfoPlaca() + " esta disponível", Toast.LENGTH_LONG).show();
    			startActivity(new Intent(EditarVeiculo.this, VisualizarVeiculos.class));
    			return true;
    		}
    	case MENU_ZERAR_DATAS:
    		veiculo.setDataSaida(null);
    		veiculo.setDataChegada(null);
    		setTextoBotoes();
    		return true;
    	}
    	return false;
    }

	private void setTextoBotoes() {		
		btDtSaida.setText(veiculo.getDataSaida() == null ? "Alterar" : CesareUtil.formatarData(veiculo.getDataSaida(), "dd/MM/yyyy"));
		btDtChegada.setText(veiculo.getDataChegada() == null ? "Alterar" : CesareUtil.formatarData(veiculo.getDataChegada(), "dd/MM/yyyy"));
		
		
	}*/
}