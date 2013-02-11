package br.com.cesaretransportesMobile.domain;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Calendar;

public class Servico implements Serializable{	
	
	private static final long serialVersionUID = 1L;
	private int idServico;
	private Orcamento orcamento;
	private Veiculo veiculo;
	private BigDecimal valor;
	private Calendar dataPrevEntrega;
	private Calendar dataEntrega;
	private Calendar dataExclusao;
	public int getIdServico() {
		return idServico;
	}
	public void setIdServico(int idServico) {
		this.idServico = idServico;
	}
	public Orcamento getOrcamento() {
		return orcamento;
	}
	public void setOrcamento(Orcamento orcamento) {
		this.orcamento = orcamento;
	}
	public Veiculo getVeiculo() {
		return veiculo;
	}
	public void setVeiculo(Veiculo veiculo) {
		this.veiculo = veiculo;
	}
	public BigDecimal getValor() {
		return valor;
	}
	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}
	public Calendar getDataPrevEntrega() {
		return dataPrevEntrega;
	}
	public void setDataPrevEntrega(Calendar dataPrevEntrega) {
		this.dataPrevEntrega = dataPrevEntrega;
	}
	public Calendar getDataEntrega() {
		return dataEntrega;
	}
	public void setDataEntrega(Calendar dataEntrega) {
		this.dataEntrega = dataEntrega;
	}
	public Calendar getDataExclusao() {
		return dataExclusao;
	}
	public void setDataExclusao(Calendar dataExclusao) {
		this.dataExclusao = dataExclusao;
	}
	public void deserialize(DataInputStream dataIn) throws IOException {
		idServico = dataIn.readInt();
		
		orcamento = new Orcamento();
		orcamento.setIdOrcamento(dataIn.readInt());
		orcamento.setCliente(new Cliente());
		orcamento.getCliente().setIdCliente(dataIn.readInt());
		orcamento.setPeso(dataIn.readUTF());
		orcamento.setDimensao(dataIn.readUTF());
		orcamento.setMensagem(dataIn.readUTF());
		orcamento.setOrcamentoLido(dataIn.readBoolean());
		orcamento.setOrcamentoRespondido(dataIn.readBoolean());
		
		Calendar orcmDtCadastro = Calendar.getInstance();
		orcmDtCadastro.setTimeInMillis(dataIn.readLong());
		orcamento.setDataCadastro(orcmDtCadastro);
		
		veiculo = new Veiculo();
		veiculo.setIdVeiculo(dataIn.readInt());
		veiculo.setMarca(dataIn.readUTF());
		veiculo.setCor(dataIn.readUTF());
		veiculo.setPlaca(dataIn.readUTF());
		veiculo.setLocalizacao(dataIn.readUTF());
		
		valor = new BigDecimal(dataIn.readUTF());
		
		long dtPrevEntrega = dataIn.readLong();
		if(dtPrevEntrega == 0){
			dataPrevEntrega = null;
		}else{
			dataPrevEntrega = Calendar.getInstance();
			dataPrevEntrega.setTimeInMillis(dtPrevEntrega);
		}
		
		long dtEntrega = dataIn.readLong();
		if(dtEntrega == 0){
			dataEntrega = null;
		}else{
			dataEntrega = Calendar.getInstance();
			dataEntrega.setTimeInMillis(dtEntrega);
		}
		
		dataExclusao = null;		
	}
}
