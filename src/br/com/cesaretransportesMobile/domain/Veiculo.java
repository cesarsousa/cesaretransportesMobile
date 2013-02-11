package br.com.cesaretransportesMobile.domain;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;

import br.com.cesaretransportesMobile.util.CesareUtil;

public class Veiculo implements Serializable{
	private static final long serialVersionUID = 1L;	
	private static final String SEM_INFORMACAO = "Não disponível";
	
	private int idVeiculo;
	private int totalOrcamento;
	private String marca;
	private String cor;
	private String placa;	
	private String localizacao;
	private Calendar dataExclusao;
	private boolean emRota;
	
	ArrayList<Orcamento> orcamentos = new ArrayList<Orcamento>();

	public int getIdVeiculo() {
		return idVeiculo;
	}
	
	public void setIdVeiculo(int idVeiculo) {
		this.idVeiculo = idVeiculo;
	}
	
	public int getTotalOrcamento() {
		return totalOrcamento;
	}
	
	public void setTotalOrcamento(int totalOrcamento) {
		this.totalOrcamento = totalOrcamento;
	}

	public String getMarca() {
		return marca;
	}
	
	public void setMarca(String marca) {
		this.marca = marca;
	}

	public String getCor() {
		return cor;
	}

	public void setCor(String cor) {
		this.cor = cor;
	}

	public String getPlaca() {
		return placa;
	}
	
	public String getInfoPlaca() {
		return placa.substring(0, 3) + "-" + placa.substring(3);
	}

	public void setPlaca(String placa) {
		this.placa = placa;
	}

	public String getLocalizacao() {
		return localizacao;
	}
	
	public String getInfoLocalizacao() {
		return "".equals(localizacao) ? SEM_INFORMACAO : localizacao;
	}

	public void setLocalizacao(String localizacao) {
		this.localizacao = localizacao;
	}

	public Calendar getDataExclusao() {
		return dataExclusao;
	}
	
	public void setDataExclusao(Calendar dataExclusao) {
		this.dataExclusao = dataExclusao;
	}
	
	public String getInfoDataExclusao(){
		return dataExclusao == null ? SEM_INFORMACAO : CesareUtil.formatarData(dataExclusao, "dd/MM/yyyy");
	}
	
	public ArrayList<Orcamento> getOrcamentos() {
		return orcamentos;
	}
	
	public void setOrcamentos(ArrayList<Orcamento> orcamentos) {
		this.orcamentos = orcamentos;
	}
	
	public boolean isEmRota() {
		return emRota;
	}

	public void setEmRota(boolean emRota) {
		this.emRota = emRota;
	}

	public void deserialize(DataInputStream dataIn) throws IOException {
		idVeiculo = dataIn.readInt();
		marca = dataIn.readUTF();
		cor = dataIn.readUTF();
		placa = dataIn.readUTF();		
		localizacao = dataIn.readUTF();
		
		long dtExclusao = dataIn.readLong();
		if(dtExclusao == 0){
			dataExclusao = null;
		}else{
			dataExclusao = Calendar.getInstance();
			dataExclusao.setTimeInMillis(dtExclusao);
		}
		
		totalOrcamento = dataIn.readInt();		
		emRota = totalOrcamento == 0 ? false : true;
	}	
}