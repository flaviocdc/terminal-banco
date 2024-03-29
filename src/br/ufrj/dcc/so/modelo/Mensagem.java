package br.ufrj.dcc.so.modelo;

import java.util.HashMap;
import java.util.Map;

public class Mensagem {

	private Map<String, Object> parametros;

	public Mensagem() {
		parametros = new HashMap<String, Object>();
		parametros.put("erro", false);
	}

	public boolean isErro() {
		Boolean aux = (Boolean) parametros.get("erro");
		if (aux == null)
			aux = false;
		
		return aux;
	}
	
	public void setErro(boolean erro) {
		parametros.put("erro", erro);
	}
	
	protected Map<String, Object> getParametros() {
		return parametros;
	}

	protected void setParametros(Map<String, Object> parametros) {
		this.parametros = parametros;
	}
	
	@SuppressWarnings("unchecked")
	public <T> T param(String key, Class<T> clazz) {
		return (T) parametros.get(key);
	}
	
	public String param(String key) {
		return param(key, String.class);
	}

	@Override
	public String toString() {
		return "Mensagem=" + parametros;
	}
}