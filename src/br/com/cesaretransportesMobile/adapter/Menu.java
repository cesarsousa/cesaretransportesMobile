package br.com.cesaretransportesMobile.adapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum Menu {
	ADM, CLIENTE;

	public static List<TextoMenuPrincipal> getMenuAdm() {
		return new ArrayList<TextoMenuPrincipal>(Arrays.asList(
				new TextoMenuPrincipal("Visualizar Clientes", "Confirmar, excluir e Acessar clientes"),
				new TextoMenuPrincipal("Visualizar Orçamentos", "Informações sobre orçamentos de serviços"),
				new TextoMenuPrincipal("Visualizar Contatos", "Acessa sua conta de email"),
				new TextoMenuPrincipal("Visualizar Veículos", "Configuração de rotas e veículos"),
				new TextoMenuPrincipal("Sair", "Logout da Aplicação")));				
	}

	public static List<TextoMenuPrincipal> getMenuCliente() {
		return new ArrayList<TextoMenuPrincipal>(Arrays.asList(
				new TextoMenuPrincipal("Meu Perfil", "Acesse e edite o seu perfil"),
				new TextoMenuPrincipal("Orçamentos", "Faça um orçamento de serviço"),
				new TextoMenuPrincipal("Contatos", "Entre em contato com a Cesare Transportes"),				
				new TextoMenuPrincipal("Sair", "Logout da Aplicação")));				
	}
}