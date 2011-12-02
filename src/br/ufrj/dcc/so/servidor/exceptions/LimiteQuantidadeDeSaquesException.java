package br.ufrj.dcc.so.servidor.exceptions;

public class LimiteQuantidadeDeSaquesException extends OperacaoFinanceiraException {

	public LimiteQuantidadeDeSaquesException(int limiteQuantidadeDeSaques) {
		super("Limite maximo (" + limiteQuantidadeDeSaques + ") de saques em um dia excedido!");
	}

}
