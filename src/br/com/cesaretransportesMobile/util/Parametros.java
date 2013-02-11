package br.com.cesaretransportesMobile.util;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * Classe responsável por criar e distribuir mapas de parâmetros para
 * as classes do sistema que se comunicam com o servidor de aplicação.
 * 
 * @author cesar sousa
 *
 */
public class Parametros {

	/**
	 * @param id o identificador do modelo.
	 * @return o mapa.
	 * <ul><li>id</li></ul>
	 */
	public static Map<String, String> getIdParams(int id) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("id", String.valueOf(id));
		return params;
	}

	/**
	 * 
	 * @param usuario o email do cliente.
	 * @param senha a senha do cliente
	 * @return o mapa
	 * <ul>
	 * <li>login=true</li>
	 * <li>usuario</li>
	 * <li>senha</li></ul>
	 */
	public static Map<String, String> getLoginParams(String usuario, String senha) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("login", "true");
		params.put("usuario", usuario);
		params.put("senha", senha);
		return params;
	}

	/**
	 * 
	 * @param veiculo o veículo a ser editado na base de dados
	 * @return o mapa dos dados do veículo.
	 * <ul><li>id</li>
	 * <li>dataSaida</li>
	 * <li>origem</li>
	 * <li>enderecoOrigem</li>
	 * <li>estadoOrigem</li>
	 * <li>dataChegada</li>
	 * <li>destino</li>
	 * <li>enderecoDestino</li>
	 * <li>estadoDestino</li>
	 * <li>localizacao</li></ul>
	 */
	/*public static Map<String, String> getAlterarParams(Veiculo veiculo) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("id", String.valueOf(veiculo.getIdVeiculo()));
		params.put("dataSaida", veiculo.getDataSaida() == null ? "0" : String.valueOf(veiculo.getDataSaida().getTimeInMillis()));
		params.put("origem", veiculo.getOrigem());
		params.put("enderecoOrigem", veiculo.getEnderecoOrigem());
		params.put("estadoOrigem", veiculo.getEstadoOrigem());
		params.put("dataChegada", veiculo.getDataChegada() == null ? "0" : String.valueOf(veiculo.getDataChegada().getTimeInMillis()));
		params.put("destino", veiculo.getDestino());
		params.put("enderecoDestino", veiculo.getEnderecoDestino());
		params.put("estadoDestino", veiculo.getEstadoDestino());
		params.put("localizacao", veiculo.getLocalizacao());
		
		return params;
	}*/

	/**
	 * mapeamento de parâmetros para cadastrar um novo orçamento.
	 * @return o mapa chave=valor dos parâmetros.
	 */
	public static Map<String, String> getCadastrarOrcamentosParams(
			int idCliente, String cidadeOrigem, String estadoOrigem,
			String enderecoOrigem, String cidadeDestino, String estadoDestino,
			String enderecoDestino, String peso, String dimensao, String mensagem) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("idCliente", String.valueOf(idCliente));		
		params.put("cidadeOrigem", cidadeOrigem);
		params.put("estadoOrigem", estadoOrigem);
		params.put("enderecoOrigem", enderecoOrigem);
		params.put("cidadeDestino", cidadeDestino);
		params.put("estadoDestino", estadoDestino);
		params.put("enderecoDestino", enderecoDestino);
		params.put("peso", peso);
		params.put("dimensao", dimensao);
		params.put("mensagem", mensagem);
		return params;
	}

	/**
	 * <p>Mapa de parâmetros para realizar a alteração de senha do cliente.</p>
	 * 
	 * @param id o id do cliente
	 * @param novaSenha1 a nova senha a ser atualizada.
	 * @return o mapa dos parâmetros.
	 * <ul><li>opcao</li>
	 * <li>id</li>
	 * <li>senha</li>
	 */
	public static Map<String, String> getAlterarSenhaParams(int id,	String novaSenha1) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("opcao", "senha");
		params.put("id", String.valueOf(id));
		params.put("senha", novaSenha1);
		return params;
	}

	/**
	 * <p>Mapa de parâmetros para realizar a alteração de dados do cliente.</p>
	 * 
	 * @return o mapa dos parâmetros.
	 * <ul><li>opcao</li>
	 * <li>id</li>
	 * <li>ddd</li>
	 * <li>telefone</li>
	 * <li>complemento</li>
	 * <li>tipoDocumento</li>
	 * <li>numeroDocumento</li>
	 * <li>localizacao</li>
	 * <li>cidade</li>
	 * <li>estado</li>
	 */
	public static Map<String, String> getAlterarDadosParams(int id, String ddd, String telefone, String complemento, 
			String tipoDocumento, String numeroDocumento, String localizacao, String cidade, String estado) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("opcao", "dados");
		params.put("id", String.valueOf(id));
		params.put("ddd", ddd);
		params.put("telefone", telefone);
		params.put("complemento", complemento);
		params.put("tipoDocumento", tipoDocumento);
		params.put("numeroDocumento", numeroDocumento);
		params.put("localizacao", localizacao);
		params.put("cidade", cidade);
		params.put("estado", estado);
		return params;
	}
	
	/**
	 * 
	 * @param email o endereço de email.
	 * @return o mapa
	 * <ul><li>email=email</li></ul>
	 */
	public static Map<String, String> getEmailParams(String email) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("email", email);
		return params;
	}
	
	/**
	 * 
	 * @param id o id do cliente.
	 * @param nome o nome do cliente.
	 * @param email o endereço de email do cliente.
	 * @param mensagem a mensagem a ser enviada.
	 * @return o mapa
	 * <ul><li>id</li>
	 * <li>email</li>
	 * <li>mensagem</li></ul>
	 */
	public static Map<String, String> getEmailParams(int id, String nome, String email, String mensagem) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("id", String.valueOf(id));
		params.put("nome", nome);
		params.put("email", email);
		params.put("mensagem", mensagem);
		return params;
	}

	/**
	 * 
	 * @param id o ide do orçamento
	 * @param resposta a mensagem de resposta referente ao orçamento.
	 * @return o maapa
	 * <ul><li>id</li>
	 * <li>resposta</li></ul>
	 */
	public static Map<String, String> getResponderOrcamentoParams(int id, String resposta) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("id", String.valueOf(id));
		params.put("resposta", resposta);
		return params;
	}

	/**
	 * 
	 * @param email o endereço de email do destinatário
	 * @param mensagem a mensagem a ser enviada
	 * @return o mapa
	 * <ul><li>email</li>
	 * <li>assunto</li>
	 * <li>mensagem</li></ul>
	 */
	public static Map<String, String> getEnviarEmailParams(String email, String assunto, String mensagem) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("email", email);
		params.put("assunto", assunto);
		params.put("mensagem", mensagem);
		return params;
	}
	
	/**
	 * 
	 * @param opcao 'servico' para confirmar orçamento como uma ordem de serviço.
	 * @param codigo o código do orçamento.
	 * @param valor o valor do serviço.
	 * @param dataPrevEntrega a data prevista para entrega do serviço.
	 * @param idVeiculo o veículo que irá realizar a entrega.
	 * @return o mapa
	 * <ul><li>opcao</li>
	 * <li>codigo</li>
	 * <li>valor</li>
	 * <li>dataPrevEntrega</li>
	 * <li>idVeiculo</li> 
	 * </ul>
	 */
	public static Map<String, String> getConfirmarOrcamentoParams(
			String opcao, int codigo, String valor, Calendar dataPrevEntrega, int idVeiculo) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("opcao", opcao);
		params.put("codigo", String.valueOf(codigo));
		params.put("valor", valor);
		long data = dataPrevEntrega.getTimeInMillis();
		params.put("dataPrevEntrega", String.valueOf(data));
		params.put("idVeiculo", String.valueOf(idVeiculo));
		return params;
	}

	/**
	 * 
	 * @param opcao 'entrega' para confirma uma ordem de serviço como entregue.
	 * @param codigo o código do orçamento a ser editado.
	 * @return o mapa
	 * <ul><li>opcao</li>
	 * <li>codigo</li>	 
	 * </ul> 
	 */
	public static Map<String, String> getConfirmarOrcamentoParams(String opcao, int codigo) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("opcao", opcao);
		params.put("codigo", String.valueOf(codigo));
		
		return params;
	}

	/**
	 * Parâmetro flag para listar cliente a serem confirmados na base de dados
	 * @return o mapa
	 * <ul><li>pendentes=true</li> 	 
	 * </ul> 
	 */
	public static Map<String, String> getVisualizarClientesPendentesParams() {
		Map<String, String> params = new HashMap<String, String>();
		params.put("pendentes", "true");
		return params;
	}
	
	/**
	 * 
	 * @param filtro o filtro para listagem dos clientes (nome ou documento)
	 * @param parametro o parâmetro de busca
	 * @return o mapa
	 * <ul><li>pendentes=false</li>
	 * <li>filtro</li>
	 * <li>parametro</li> 	 
	 * </ul> 
	 */
	public static Map<String, String> getVisualizarClientes(String filtro, String parametro) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("pendentes", "false");
		params.put("filtro", filtro);
		params.put("parametro", parametro);
		return params;
	}

	

		
}
