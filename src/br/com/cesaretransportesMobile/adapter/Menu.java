package br.com.cesaretransportesMobile.adapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum Menu {
	ADM, CLIENTE;

	public static List<TextoMenuPrincipal> getMenuAdm() {
		return new ArrayList<TextoMenuPrincipal>(Arrays.asList(
				new TextoMenuPrincipal("Visualizar Clientes", "Confirmar, excluir e Acessar clientes"),
				new TextoMenuPrincipal("Visualizar Or�amentos", "Informa��es sobre or�amentos de servi�os"),
				new TextoMenuPrincipal("Visualizar Contatos", "Acessa sua conta de email"),
				new TextoMenuPrincipal("Visualizar Ve�culos", "Configura��o de rotas e ve�culos"),
				new TextoMenuPrincipal("Sair", "Logout da Aplica��o")));				
	}

	public static List<TextoMenuPrincipal> getMenuCliente() {
		return new ArrayList<TextoMenuPrincipal>(Arrays.asList(
				new TextoMenuPrincipal("Meu Perfil", "Acesse e edite o seu perfil"),
				new TextoMenuPrincipal("Or�amentos", "Fa�a um or�amento de servi�o"),
				new TextoMenuPrincipal("Contatos", "Entre em contato com a Cesare Transportes"),				
				new TextoMenuPrincipal("Sair", "Logout da Aplica��o")));				
	}
}