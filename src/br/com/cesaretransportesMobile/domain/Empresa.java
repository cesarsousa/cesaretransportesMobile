package br.com.cesaretransportesMobile.domain;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Empresa implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private int idEmpresa;	
	private Endereco endereco;
	private String nome;
	private String cnpj;
	private String msn;
	private String email;
	private String senha;

	List<Telefone> telefones;
	
	public Empresa(int idEmpresa, Endereco endereco, String nome, String cnpj,
			String msn, String email, String senha, List<Telefone> telefones) {
		this.idEmpresa = idEmpresa;
		this.endereco = endereco;
		this.nome = nome;
		this.cnpj = cnpj;
		this.msn = msn;
		this.email = email;
		this.senha = senha;
		this.telefones = telefones;
	}

	public Empresa() {}

	public int getIdEmpresa() {
		return idEmpresa;
	}

	public void setIdEmpresa(int idEmpresa) {
		this.idEmpresa = idEmpresa;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public Endereco getEndereco() {
		return endereco;
	}
	
	public void setEndereco(Endereco endereco) {
		this.endereco = endereco;
	}	

	public String getCnpj() {
		return cnpj;
	}

	public void setCnpj(String cnpj) {
		this.cnpj = cnpj;
	}

	public String getMsn() {
		return msn;
	}

	public void setMsn(String msn) {
		this.msn = msn;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public List<Telefone> getTelefones() {
		return telefones;
	}
	
	public void setTelefones(List<Telefone> telefones) {
		this.telefones = telefones;
	}
	
	public void deserialize(DataInputStream dataIn) throws IOException {
		idEmpresa = dataIn.readInt();
		nome = dataIn.readUTF();		
		cnpj = dataIn.readUTF();
		msn = dataIn.readUTF();
		email = dataIn.readUTF();
		senha = dataIn.readUTF();
		endereco = new Endereco();
		endereco.setIdEndereco(dataIn.readInt());
		endereco.setCidade(dataIn.readUTF());
		endereco.setEstado(dataIn.readUTF());
		endereco.setLocalizacao(dataIn.readUTF());
		
		telefones = new ArrayList<Telefone>();
		int totalTelefones = dataIn.readInt();		
		for(int i=0;i<totalTelefones;i++){
			Telefone telefone = new Telefone();
			telefone.setDdd(dataIn.readUTF());
			telefone.setNumero(dataIn.readUTF());
			telefone.setComplemento(dataIn.readUTF());
			telefones.add(telefone);
		}		
	}
}
