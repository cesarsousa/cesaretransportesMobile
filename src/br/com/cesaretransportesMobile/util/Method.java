package br.com.cesaretransportesMobile.util;

public enum Method {
	
	POST("POST"), GET("GET");
	
	private String code;
	
	private Method(String code){
		this.code = code;
	}

	public String get() {		
		return code;
	}

}
