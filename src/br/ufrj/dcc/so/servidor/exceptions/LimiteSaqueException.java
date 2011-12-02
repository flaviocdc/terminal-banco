package br.ufrj.dcc.so.servidor.exceptions;

public class LimiteSaqueException extends OperacaoFinanceiraException {

	public LimiteSaqueException(double limiteSaques) {
		super("O limite de " + limiteSaques + " foi ultrapassado!");
	}

}
