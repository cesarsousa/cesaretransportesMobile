package br.com.cesaretransportesMobile.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import br.com.cesaretransportesMobile.domain.Cliente;
import br.com.cesaretransportesMobile.http.Http;
import br.com.cesaretransportesMobile.http.HttpCliente;
import br.com.cesaretransportesMobile.util.CesareUtil;
import br.com.cesaretransportesMobile.util.Parametros;
import br.com.cesaretransportesMobile.util.Servlet;

public class ClientePerfil extends Activity{
	
	private static final String URL_DELETAR = Http.SERVIDOR + Servlet.androidClienteDeletar;
	private Cliente cliente;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		cliente = (Cliente) getIntent().getSerializableExtra("cliente");		
		setContentView(R.layout.cliente_perfil);
		
		TextView textoData = (TextView)findViewById(R.id.clientePerfil_data);
		textoData.setText("Cliente desde " + CesareUtil.formatarData(cliente.getDataCadastro(), "dd/MM/yyyy"));
		
		TextView textoNome = (TextView)findViewById(R.id.clientePerfil_nome);
		textoNome.setText(cliente.getNome());
		
		TextView textoEmail = (TextView)findViewById(R.id.clientePerfil_email);
		textoEmail.setText(cliente.getEmail());	
		
		TextView textoDocumento = (TextView)findViewById(R.id.clientePerfil_documento);
		textoDocumento.setText(cliente.getTipoDoDocumento() + " = " + CesareUtil.formatarDocumento(cliente.getTipoDoDocumento(), cliente.getNumeroDoDocumento()));
			
		TextView textoTelefone = (TextView)findViewById(R.id.clientePerfil_telefone);
		textoTelefone.setText(cliente.getTelefone().formatado());
		
		TextView textoTelefoneComplemento = (TextView) findViewById(R.id.clientePerfil_telefoneComplemento);
		textoTelefoneComplemento.setText(cliente.getTelefone().getComplemento() == null ? "" : cliente.getTelefone().getComplemento());
		
		TextView enderecoLocalizacao = (TextView) findViewById(R.id.clientePerfil_enderecoLocalizacao);
		enderecoLocalizacao.setText(cliente.getEndereco().getLocalizacao());
		
		TextView textoEnderecoCidadeEstado = (TextView) findViewById(R.id.clientePerfil_enderecoCidadeEstado);
		textoEnderecoCidadeEstado.setText(cliente.getEndereco().getCidade() + " - " + cliente.getEndereco().getEstado());
		
		Button btMudarSenha = (Button)findViewById(R.id.clientePerfil_btMudarSenha);
		btMudarSenha.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(ClientePerfil.this, ClienteMudarSenha.class);
				intent.putExtra("cliente", cliente);
				startActivity(intent);				
			}
		});
		
		Button btEditar = (Button) findViewById(R.id.clientePerfil_btEditar);
		btEditar.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(ClientePerfil.this, ClienteAlterarDados.class);
				intent.putExtra("cliente", cliente);
				startActivity(intent);
			}
		});
		
		Button btMenu = (Button) findViewById(R.id.clientePerfil_btMenu);
		btMenu.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(ClientePerfil.this, MenuCliente.class);
				intent.putExtra("cliente", cliente);
				startActivity(intent);
			}
		});
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuItem item = menu.add(0, 0, 0, "Excluir minha conta");
		item.setIcon(R.drawable.icone_excluir);
		return true;
	}
	
	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		AlertDialog.Builder alerta = new AlertDialog.Builder(ClientePerfil.this);
		alerta.setIcon(R.drawable.icone_excluir);
		alerta.setTitle("Excluir Conta");
		alerta.setMessage("Tem certeza que deseja excluir seu perfil ?");
		
		alerta.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				HttpCliente httpCliente = new HttpCliente();
				httpCliente.doPost(URL_DELETAR, Parametros.getIdParams(cliente.getIdCliente()));
				
				startActivity(new Intent(ClientePerfil.this, Login.class));					
			}
		});
		
		alerta.setNegativeButton("Não", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {}
		});
		
		alerta.show();
		return true;
	}
}
