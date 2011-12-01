package br.ufrj.dcc.so.modelo;

import java.util.HashMap;
import java.util.Map;

public class MensagemBuilder {

	private Map<String, Object> parametros = new HashMap<String, Object>();
	
	public MensagemBuilder comErro() {
		parametros.put("erro", true);
		return this;
	}
	
	public MensagemBuilder semErro() {
		parametros.put("erro", false);
		return this;
	}
	
	public MensagemBuilder mensagem(String valor) {
		return param("conteudo", valor);
	}
	
	public MensagemBuilder param(String nome, String valor) {
		parametros.put(nome, valor);
		return this;
	}
	
	public Mensagem criar() {
		Mensagem msg = new Mensagem();
		msg.setParametros(parametros);
		return msg;
	}
}