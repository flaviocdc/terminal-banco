package br.ufrj.dcc.so.modelo;

import java.util.Date;

public class OperacaoFinaceira {

	public enum Tipo {
		TRANSFERENCIA(true), DOC(true), SAQUE(true), DEPOSITO, TARIFA_SERVICO(true), DESCONHECIDO;
		
		private boolean negativo = false;
		
		private Tipo() {
			this(false);
		}
		
		private Tipo(boolean negativo) {
			this.negativo = negativo;
		}
		
		public boolean negativo() {
			return negativo;
		}
	}
	
	private Date data;
	private double valor;
	private Tipo tipo;
	
	public OperacaoFinaceira(Tipo tipo, double valor) {
		this.tipo = tipo;
		this.data = new Date();
		
		if (valor < 0.0)
			throw new IllegalStateException("'valor' nao pode ser negativo (gerenciado automaticamente)");
		
		if (tipo.negativo) {
			this.valor = -valor;
		} else {
			this.valor = valor;
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