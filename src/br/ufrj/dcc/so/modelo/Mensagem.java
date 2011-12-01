package br.ufrj.dcc.so.modelo;

import java.util.HashMap;
import java.util.Map;

public class Mensagem {

	private Map<String, Object> parametros;

	public Mensagem() {
		this(false, "");
	}
	
	public Mensagem(boolean erro, String conteudo) {
		parametros = new HashMap<String, Object>();
		parametros.put("conteudo", conteudo);
		parametros.put("erro", erro);
	}

	public Mensagem(String conteudo) {
		this(false, conteudo);
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
	
	public String getConteudo() {
		return (String) parametros.get("conteudo");
	}
	
	public void setConteudo(String conteudo) {
		parametros.put("conteudo", conteudo);
	}

	protected Map<String, Object> getParametros() {
		return parametros;
	}

	protected void setParametros(Map<String, Object> parametros) {
		this.parametros = parametros;
	}

	@Override
	public String toString() {
		return "Mensagem=" + parametros;
	}
}