package br.ufrj.dcc.so.modelo;

import static org.testng.Assert.assertEquals;

import org.testng.annotations.Test;

import br.ufrj.dcc.so.modelo.OperacaoFinaceira.Tipo;

public class OperacaoFinaceiraUnitTest {

	@Test
	public void testTipoDOC_Tem_Que_Ser_Negativo() {
		OperacaoFinaceira op = new OperacaoFinaceira(Tipo.DOC, 10.0);
		assertEquals(op.getValor(), -10.0);
	}
	
	@Test
	public void testTipoSaque_Tem_Que_Ser_Negativo() {
		OperacaoFinaceira op = new OperacaoFinaceira(Tipo.SAQUE, 10.0);
		assertEquals(op.getValor(), -10.0);
	}
	
	@Test
	public void testTipoTransferencia_Tem_Que_Ser_Negativo() {
		OperacaoFinaceira op = new OperacaoFinaceira(Tipo.TRANSFERENCIA, 10.0);
		assertEquals(op.getValor(), -10.0);
	}
	
}
