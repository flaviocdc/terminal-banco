package br.ufrj.dcc.so.modelo;

import org.testng.annotations.Test;

import br.ufrj.dcc.so.modelo.OperacaoFinaceira.Tipo;

public class OperacaoFinaceiraUnitTest {

	@Test(expectedExceptions = { IllegalStateException.class })
	public void testTipoDOC_Tem_Que_Ser_Negativo() {
		OperacaoFinaceira op = new OperacaoFinaceira(Tipo.DOC, 10.0);
	}
	
	@Test(expectedExceptions = { IllegalStateException.class })
	public void testTipoSaque_Tem_Que_Ser_Negativo() {
		OperacaoFinaceira op = new OperacaoFinaceira(Tipo.SAQUE, 10.0);
	}
	
	@Test(expectedExceptions = { IllegalStateException.class })
	public void testTipoTransferencia_Tem_Que_Ser_Negativo() {
		OperacaoFinaceira op = new OperacaoFinaceira(Tipo.TRANSFERENCIA, 10.0);
	}
	
}
