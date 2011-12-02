package br.ufrj.dcc.so.servidor.exceptions;

public class LimitePorDOCException extends OperacaoFinanceiraException {

	public LimitePorDOCException(double limitePorDOC) {
		super("Nao eh possivel efetuar DOCs de mais mais de " + limitePorDOC);
	}

}
