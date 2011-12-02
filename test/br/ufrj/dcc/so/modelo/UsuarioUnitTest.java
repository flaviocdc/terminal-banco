package br.ufrj.dcc.so.modelo;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.testng.annotations.Test;

import br.ufrj.dcc.so.modelo.OperacaoFinaceira.Tipo;

public class UsuarioUnitTest {

	@Test
	public void testRecuperarOperacoesPorTipo() {
		OperacaoFinaceira[] opsArray = {
				new OperacaoFinaceira(Tipo.DOC, 10.0),
				new OperacaoFinaceira(Tipo.TRANSFERENCIA, 10.0),
				new OperacaoFinaceira(Tipo.SAQUE, 10.0),
				new OperacaoFinaceira(Tipo.DEPOSITO, 10.0)
		};
		
		List<OperacaoFinaceira> ops = Arrays.asList(opsArray);
		
		Usuario usuario = new Usuario();
		usuario.setOperacoes(ops);
		
		assertEquals(usuario.recuperarDepositos().size(), 1);
		assertEquals(usuario.recuperarDepositos().get(0).getTipo(), Tipo.DEPOSITO);
		
		assertEquals(usuario.recuperarDOCs().size(), 1);
		assertEquals(usuario.recuperarDOCs().get(0).getTipo(), Tipo.DOC);
		
		assertEquals(usuario.recuperarTransferencias().size(), 1);
		assertEquals(usuario.recuperarTransferencias().get(0).getTipo(), Tipo.TRANSFERENCIA);
		
		assertEquals(usuario.recuperarSaques().size(), 1);
		assertEquals(usuario.recuperarSaques().get(0).getTipo(), Tipo.SAQUE);
	}
	
	@Test
	public void testSaldo_Somente_Depositos() {
		OperacaoFinaceira[] opsArray = {
				new OperacaoFinaceira(Tipo.DEPOSITO, 10.0),
				new OperacaoFinaceira(Tipo.DEPOSITO, 20.0)
		};
		
		List<OperacaoFinaceira> ops = Arrays.asList(opsArray);
		
		Usuario usuario = new Usuario();
		usuario.setOperacoes(ops);
		
		assertEquals(usuario.saldo(), 30.0);
	}
	
	@Test
	public void testSaldo_Varias_Operacoes() {
		OperacaoFinaceira[] opsArray = {
				new OperacaoFinaceira(Tipo.DEPOSITO, 10.0),
				new OperacaoFinaceira(Tipo.DEPOSITO, 20.0),
				new OperacaoFinaceira(Tipo.DOC, 20.0),
				new OperacaoFinaceira(Tipo.SAQUE, 10.0)
		};
		
		List<OperacaoFinaceira> ops = Arrays.asList(opsArray);
		
		Usuario usuario = new Usuario();
		usuario.setOperacoes(ops);
		
		assertEquals(usuario.saldo(), 0.0);
	}
	
	@Test
	public void testContadorLogin() {
		Usuario u = new Usuario();
		
		assertTrue(u.podeLogar());
		
		u.logou();
		
		assertFalse(u.podeLogar());
		assertEquals(u.contadorLogin, 1);
		
		u.deslogou();
		
		assertTrue(u.podeLogar());
		assertEquals(u.contadorLogin, 0);
	}
	
}
