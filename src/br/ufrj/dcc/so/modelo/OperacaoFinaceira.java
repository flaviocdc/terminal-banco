package br.ufrj.dcc.so.modelo;

import java.util.Date;

public class OperacaoFinaceira {

	public enum Tipo {
		TRANSFERENCIA(true), DOC(true), SAQUE(true), DEPOSITO, DESCONHECIDO;
		
		private boolean negativo = false;
		
		private Tipo() {
			this(false);
		}
		
		private Tipo(boolean negativo) {
			this.negativo = negativo;
		}
	}
	
	private Date data;
	private double valor;
	private Tipo tipo;
	
	public OperacaoFinaceira(Tipo tipo, double valor) {
		this.tipo = tipo;
		this.valor = valor;
		this.data = new Date();
		
		if (tipo.negativo && valor > 0.0) {
			throw new IllegalStateException(tipo + " deve possuir valores negativos");
		}
	}
	
	public OperacaoFinaceira() {
		this(Tipo.DESCONHECIDO, 0f);
	}

	public double getValor() {
		return valor;
	}
	
	public Tipo getTipo() {
		return tipo;
	}

	public Date getData() {
		return data;
	}
	
}