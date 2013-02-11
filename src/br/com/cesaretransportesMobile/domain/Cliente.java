package br.com.cesaretransportesMobile.domain;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.Calendar;
import java.util.List;


public class Cliente implements Serializable{
	private static final long serialVersionUID = 1L;	
	private int idCliente;
	private String nome;
	private String tipoCliente;
	private String email;
	private TipoDoDocumento tipoDoDocumento;
	private String numeroDoDocumento;
	private Telefone telefone;
	private Endereco endereco;
	private String senha;
	private Calendar dataCadastro;
	private Calendar dataExclusao;
	private boolean statusCliente;
	private List<Orcamento> orcamentos;	
	
	public int getIdCliente() {
		return idCliente;
	}
	
	public void setIdCliente(int idCliente) {
		this.idCliente = idCliente;
	}
	
	public String getNome() {
		return nome;
	}
	
	public void setNome(String nome) {
		this.nome = nome;
	}
	
	public String getTipoCliente() {
		return tipoCliente;
	}
	
	public void setTipoCliente(String tipo) {
		this.tipoCliente = tipo;
	}

	public String getEmail() {
		return email;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}

	public TipoDoDocumento getTipoDoDocumento() {
		return tipoDoDocumento;
	}
	
	public void setTipoDoDocumento(TipoDoDocumento tipoDoDocumento) {
		this.tipoDoDocumento = tipoDoDocumento;
	}

	public String getNumeroDoDocumento() {
		return numeroDoDocumento;
	}
	
	public void setNumeroDoDocumento(String numeroDoDocumento) {
		this.numeroDoDocumento = numeroDoDocumento;
	}

	public Telefone getTelefone() {
		return telefone;
	}
	
	public void setTelefone(Telefone telefone) {
		this.telefone = telefone;
	}
	
	public Endereco getEndereco() {
		return endereco;
	}
	
	public void setEndereco(Endereco endereco) {
		this.endereco = endereco;
	}

	public String getSenha() {
		return senha;
	}
	
	public void setSenha(String senha) {
		this.senha = senha;
	}

	public Calendar getDataCadastro() {
		return dataCadastro;
	}
	
	public void setDataCadastro(Calendar dataCadastro) {
		this.dataCadastro = dataCadastro;
	}
	
	public Calendar getDataExclusao() {
		return dataExclusao;
	}
	
	public void setDataExclusao(Calendar dataExclusao) {
		this.dataExclusao = dataExclusao;
	}

	public boolean isStatusCliente() {
		return statusCliente;
	}
	
	public void setStatusCliente(boolean statusCliente) {
		this.statusCliente = statusCliente;
	}
	
	public List<Orcamento> getOrcamentos() {
		return orcamentos;
	}
	
	public void setOrcamentos(List<Orcamento> orcamentos) {
		this.orcamentos = orcamentos;
	}
	
	public enum TipoDoDocumento {
		CPF("cpf"),
		CNPJ("cnpj");
		
		private String codigo;
		
		private TipoDoDocumento(String codigo){
			this.codigo = codigo;
		}
		
		public static TipoDoDocumento criarPorCodigo(String codigo){
			if (codigo == null) {
				throw new IllegalArgumentException("código de documento nulo");
			}
			
			for (TipoDoDocumento tipo : TipoDoDocumento.values()){
				if (codigo.equals(tipo.codigo)){
					return tipo;
				}
			}			
			throw new IllegalArgumentException("código de documento inválido: " + codigo);			
		}
		
		public String toString() {			
			return codigo;
		}
	}

	public void deserialize(DataInputStream dataIn) throws IOException {		
		idCliente = dataIn.readInt();
		nome = dataIn.readUTF();		
		tipoCliente = dataIn.readUTF();
		email = dataIn.readUTF();
		
		String codigo = dataIn.readUTF();
		tipoDoDocumento = TipoDoDocumento.criarPorCodigo(codigo);
		
		numeroDoDocumento = dataIn.readUTF();
		
		senha = dataIn.readUTF();		
		
		long time = dataIn.readLong();		
		dataCadastro = Calendar.getInstance();
		dataCadastro.setTimeInMillis(time);
		
		long dtExclusao = dataIn.readLong();
		if(dtExclusao == 0){
			dataExclusao = null;
		}else{
			dataExclusao = Calendar.getInstance();
			dataExclusao.setTimeInMillis(dtExclusao);
		}		
		
		String status = dataIn.readUTF();
		statusCliente = Boolean.valueOf(status);
		
		// deserialização do telefone
		telefone = new Telefone();
		telefone.setIdTelefone(dataIn.readInt());		
		telefone.setIdCliente(dataIn.readInt());
		telefone.setDdd(dataIn.readUTF());
		telefone.setNumero(dataIn.readUTF());
		String complemento = dataIn.readUTF();
		telefone.setComplemento(complemento.equals("null") ? "" : complemento);
		
		// deserialização do endereço
		endereco = new Endereco();
		endereco.setCliente(new Cliente());
		
		endereco.setIdEndereco(dataIn.readInt());
		endereco.getCliente().setIdCliente(dataIn.readInt());
		endereco.setCidade(dataIn.readUTF());
		endereco.setEstado(dataIn.readUTF());
		endereco.setLocalizacao(dataIn.readUTF());		
	}
}
