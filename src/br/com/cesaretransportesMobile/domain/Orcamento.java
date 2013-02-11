package br.com.cesaretransportesMobile.domain;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.Calendar;
import java.util.List;

import br.com.cesaretransportesMobile.domain.Endereco.StatusEndereco;
import br.com.cesaretransportesMobile.util.CesareUtil;


public class Orcamento implements Serializable{
	
	private static final long serialVersionUID = 1L;
	private int idOrcamento;
	
	private Cliente cliente;	
	
	private String peso;
	private String dimensao;
	private String mensagem;	
	private Calendar dataCadastro;	
	private Calendar dataExclusao;
	private boolean orcamentoLido;
	private boolean orcamentoRespondido;
	
	private List<Endereco> enderecos;
	
	public Orcamento(){
		this.orcamentoLido = false;
		this.orcamentoRespondido = false;
	}
	
	public void setIdOrcamento(int idOrcamento) {
		this.idOrcamento = idOrcamento;
	}
	
	public int getIdOrcamento() {
		return idOrcamento;
	}	
	
	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}
	
	public String getPeso() {
		return peso;
	}
	
	public void setPeso(String peso) {
		this.peso = peso;
	}
	
	public String getDimensao() {
		return dimensao;
	}
	
	public void setDimensao(String dimensao) {
		this.dimensao = dimensao;
	}
	
	public String getMensagem() {
		return mensagem;
	}
	
	public void setMensagem(String mensagem) {
		this.mensagem = mensagem;
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
	
	public boolean isOrcamentoLido() {
		return orcamentoLido;
	}
	
	public void setOrcamentoLido(boolean orcamentoLido) {
		this.orcamentoLido = orcamentoLido;
	}
	
	public boolean isOrcamentoRespondido() {
		return orcamentoRespondido;
	}
	
	public void setOrcamentoRespondido(boolean orcamentoRespondido) {
		this.orcamentoRespondido = orcamentoRespondido;
	}
	
	public List<Endereco> getEnderecos() {
		return enderecos;
	}
	
	public void setEnderecos(List<Endereco> enderecos) {
		this.enderecos = enderecos;
	}	
	
	// métodos utilitario para a camada de visão
	public String getDetalheOrigem(){
		for(Endereco endereco : enderecos){
			if(StatusEndereco.ORIGEM == endereco.getStatusEndereco()){
				return endereco.toString();
			}
		}
		return "Nao disponivel";
	}
	
	public String getDetalheDestino(){
		for(Endereco endereco : enderecos){
			if(StatusEndereco.DESTINO == endereco.getStatusEndereco()){
				return endereco.toString();
			}
		}
		return "Nao disponivel";
	}
	
	public String getInfoDataCadastro(){
		return CesareUtil.formatarData(dataCadastro, "dd/MM/yyyy");
	}
	
	public String getInfoDataPrevEntrega(){
		return "Nao disponivel";
	}
	public String getInfoDataEntrega(){
		return "Nao disponivel";
	}
	
	public boolean isDeletado(){
		return dataExclusao != null ? true : false;
	}
	
	

	public void deserialize(DataInputStream dataIn) throws IOException {
		idOrcamento = dataIn.readInt();
		cliente = new Cliente();
		cliente.setIdCliente(dataIn.readInt());
		peso = dataIn.readUTF();
		dimensao = dataIn.readUTF();		
		mensagem = dataIn.readUTF();	
		
		long dtCadastro = dataIn.readLong();
		if(dtCadastro == 0){
			dataCadastro = null;
		}else{
			dataCadastro = Calendar.getInstance();
			dataCadastro.setTimeInMillis(dtCadastro);
		}		
		
		long dtExclusao = dataIn.readLong();
		if(dtExclusao == 0){
			dataExclusao = null;
		}else{
			dataExclusao = Calendar.getInstance();
			dataExclusao.setTimeInMillis(dtExclusao);
		}		
		
		orcamentoLido = Boolean.parseBoolean(dataIn.readUTF());
		orcamentoRespondido = Boolean.parseBoolean(dataIn.readUTF());		
	}
}
