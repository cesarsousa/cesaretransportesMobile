package br.com.cesaretransportesMobile.domain;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.Serializable;

public class Endereco implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//PK
	private int idEndereco;
	
	//FKs
	private Empresa empresa;
	private Cliente cliente;
	private Orcamento orcamento;
	
	private String cidade;
	private String estado;
	private String localizacao;
	private StatusEndereco statusEndereco;

	public Endereco(Empresa empresa, Cliente cliente, Orcamento orcamento, String cidade, String estado, String localizacao, StatusEndereco statusEndereco) {		
		this.empresa = empresa;
		this.cliente = cliente;
		this.orcamento = orcamento;
		this.cidade = cidade;
		this.estado = estado;
		this.localizacao = localizacao;
		this.statusEndereco = statusEndereco;
	}
	
	public Endereco(int idEndereco, Empresa empresa, Cliente cliente, Orcamento orcamento, String cidade, String estado, String localizacao, StatusEndereco statusEndereco) {
		this(empresa, cliente, orcamento, cidade, estado, localizacao, statusEndereco);
		this.idEndereco = idEndereco;
	}	

	public Endereco() {}

	public int getIdEndereco() {
		return idEndereco;
	}

	public void setIdEndereco(int idEndereco) {
		this.idEndereco = idEndereco;
	}

	public Empresa getEmpresa() {
		return empresa;
	}
	
	public void setEmpresa(Empresa empresa) {
		this.empresa = empresa;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public Orcamento getOrcamento() {
		return orcamento;
	}

	public void setOrcamento(Orcamento orcamento) {
		this.orcamento = orcamento;
	}

	public String getCidade() {
		return cidade;
	}

	public void setCidade(String cidade) {
		this.cidade = cidade;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public String getLocalizacao() {
		return localizacao;
	}

	public void setLocalizacao(String localizacao) {
		this.localizacao = localizacao;
	}

	public StatusEndereco getStatusEndereco() {
		return statusEndereco;
	}

	public void setStatusEndereco(StatusEndereco statusEndereco) {
		this.statusEndereco = statusEndereco;
	}
	
	public enum StatusEndereco {
		NAO_SE_APLICA, ORIGEM, DESTINO;
		
		public static StatusEndereco criarPorCodigo(int codigo){
			for(StatusEndereco status : StatusEndereco.values()){
				if(status.ordinal() == codigo){
					return status;
				}
			}
			throw new IllegalArgumentException("Status de endereço inválido para o código " + codigo + ". Valor deve ser 0, 1 ou 2.");		
		}
	}
	
	public String toString() {		
		return localizacao + ", " + cidade + "-" + estado;
	}

	public void deserialize(DataInputStream dataIn) throws IOException {
		idEndereco = dataIn.readInt();
		empresa = new Empresa();
		empresa.setIdEmpresa(dataIn.readInt());
		cliente = new Cliente();
		cliente.setIdCliente(dataIn.readInt());
		orcamento = new Orcamento();
		orcamento.setIdOrcamento(dataIn.readInt());
		cidade = dataIn.readUTF();
		estado = dataIn.readUTF();
		localizacao = dataIn.readUTF();
		statusEndereco = StatusEndereco.criarPorCodigo(dataIn.readInt());
		
	}

}
