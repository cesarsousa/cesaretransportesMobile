package br.com.cesaretransportesMobile.activity;

import java.io.IOException;

import br.com.cesaretransportesMobile.domain.Cliente;
import br.com.cesaretransportesMobile.http.Http;
import br.com.cesaretransportesMobile.http.HttpCliente;
import br.com.cesaretransportesMobile.util.Parametros;
import br.com.cesaretransportesMobile.util.Servlet;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ClienteMudarSenha extends Activity {

	private static final String URL_ALTERAR_SENHA = Http.SERVIDOR + Servlet.androidClienteAlterarDados;
	private static final String URL_GET_CLIENTE = Http.SERVIDOR + Servlet.androidClienteVisualizar;
	private static final String MENSAGEM = "Erro para efetuar a conexão com Cesare Transportes.";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mudar_senha);

		final Cliente cliente = (Cliente) getIntent().getSerializableExtra(
				"cliente");

		Button btCancelar = (Button) findViewById(R.id.mudarSenha_btCancelar);
		btCancelar.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				finish();
			}
		});

		Button btConfirmar = (Button) findViewById(R.id.mudarSenha_btConfirmar);
		btConfirmar.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				EditText camposenhaAtual = (EditText) findViewById(R.id.mudaSenha_atual);
				String senhaAtual = camposenhaAtual.getText().toString();
				if ("".equals(senhaAtual)) {
					alerta("O campo 'senha atual' deve ser preenchido!");
					return;
				}

				if (!senhaAtual.equals(cliente.getSenha())) {
					alerta("A 'senha atual' digitada não é válida!");
					return;
				}

				EditText campoNovaSenha1 = (EditText) findViewById(R.id.mudaSenha_nova1);
				String novaSenha1 = campoNovaSenha1.getText().toString();
				if ("".equals(novaSenha1)) {
					alerta("O campo 'nova senha' deve ser preenchio!");
					return;
				}

				EditText campoNovaSenha2 = (EditText) findViewById(R.id.mudaSenha_nova2);
				String novaSenha2 = campoNovaSenha2.getText().toString();
				if ("".equals(novaSenha2)) {
					alerta("O campo 'confirme sua senha' deve ser preenchido");
					return;
				}

				if (!novaSenha1.equals(novaSenha2)) {
					alerta("Os campos da nova senha não conferem");
					return;
				}

				HttpCliente httpCliente = new HttpCliente();
				String resultado = httpCliente.doPost(URL_ALTERAR_SENHA,
						Parametros.getAlterarSenhaParams(cliente.getIdCliente(), novaSenha1));
				Log.i(Http.LOGGER, resultado);

				// verifica se a requisição ao servidor não retornou erro
				if (resultado.startsWith("ERRO")) {
					Log.i(Http.LOGGER, "começo com erro");
					Intent intent = new Intent(ClienteMudarSenha.this, TelaErroCliente.class);
					intent.putExtra("mensagemErro", resultado);
					startActivity(intent);
				} else {
					/*
					 * atualizar o cliente na aplicação.
					 */
					int id = cliente.getIdCliente();
					try {
						Cliente cliente = httpCliente.getCliente(URL_GET_CLIENTE, Parametros.getIdParams(id));
						Intent intent = new Intent(ClienteMudarSenha.this, ClientePerfil.class);
						intent.putExtra("cliente", cliente);

						alerta("Sua senha foi alterada com sucesso!");

						startActivity(intent);
					} catch (IOException e) {
						Toast.makeText(ClienteMudarSenha.this, MENSAGEM,
								Toast.LENGTH_LONG).show();
						Log.e(Http.LOGGER, e.getMessage(), e);
					}
				}
			}
		});
	}

	private void alerta(String campo) {
		Toast.makeText(this, campo, Toast.LENGTH_LONG).show();
	}
}
