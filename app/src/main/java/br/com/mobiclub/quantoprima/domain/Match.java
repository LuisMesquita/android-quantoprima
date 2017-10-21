package br.com.mobiclub.quantoprima.domain;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Match implements Serializable{
	
	private int establishmentId;
	private int loteId;
	
	public Match(){}
	
	public int getEstablishmentId(){
		return this.establishmentId;
	}
	public int getLoteId(){
		return this.loteId;
	}
	public void setEstablishmentId(int establishmentId){
		this.establishmentId = establishmentId;
	}
	public void setLoteId(int loteId){
		this.loteId = loteId;
	}

}
