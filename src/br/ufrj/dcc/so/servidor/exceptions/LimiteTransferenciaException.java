package br.ufrj.dcc.so.servidor.exceptions;

public class LimiteTransferenciaException extends OperacaoFinanceiraException {

	public LimiteTransferenciaException() {
		super("Limite de transferencias no dia de hoje foi ultrapassado");
	}
}
