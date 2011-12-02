package br.ufrj.dcc.so.servidor;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import org.testng.annotations.Test;

import br.ufrj.dcc.so.modelo.OperacaoFinaceira;
import br.ufrj.dcc.so.modelo.OperacaoFinaceira.Tipo;
import br.ufrj.dcc.so.modelo.Usuario;
import br.ufrj.dcc.so.servidor.exceptions.LimitePorDOCException;
import br.ufrj.dcc.so.servidor.exceptions.LimiteQuantidadeDeSaquesException;
import br.ufrj.dcc.so.servidor.exceptions.LimiteSaqueException;
import br.ufrj.dcc.so.servidor.exceptions.LimiteTransferenciaException;
import br.ufrj.dcc.so.servidor.exceptions.OperacaoFinanceiraException;
import br.ufrj.dcc.so.servidor.exceptions.SaldoInsuficienteException;

public class GerenciadorContaUnitTest {

	@Test
	public void testVerificarSaldo_Saldo_Suficiente() {
		Usuario usuario = new Usuario();
		usuario.adicionarOperacao(new OperacaoFinaceira(Tipo.SAQUE, 10.0));
		usuario.adicionarOperacao(new OperacaoFinaceira(Tipo.DEPOSITO, 50.0));
		
		GerenciadorConta gerenciador = new GerenciadorConta(usuario);
		
		assertTrue(gerenciador.verificarSaldo(39.0));
	}
	
	@Test
	public void testVerificarSaldo_Saldo_Insuficiente() {
		Usuario usuario = new Usuario();
		
		usuario.adicionarOperacao(new OperacaoFinaceira(Tipo.SAQUE, 40.0));
		usuario.adicionarOperacao(new OperacaoFinaceira(Tipo.DEPOSITO, 50.0));
		
		GerenciadorConta gerenciador = new GerenciadorConta(usuario);
		
		assertFalse(gerenciador.verificarSaldo(40.0));
	}
	
	@Test(expectedExceptions = SaldoInsuficienteException.class)
	public void testEfetuarTransferencia_Saldo_Insuficiente() throws SaldoInsuficienteException {
		Usuario usuario = new Usuario();
		
		usuario.adicionarOperacao(new OperacaoFinaceira(Tipo.SAQUE, 40.0));
		usuario.adicionarOperacao(new OperacaoFinaceira(Tipo.DEPOSITO, 50.0));
		
		GerenciadorConta gerenciador = new GerenciadorConta(usuario);	
		gerenciador.garantirSaldoSuficiente(40);
	}
	
	@Test(expectedExceptions = LimiteTransferenciaException.class)
	public void testEfetuarTransferencia_Limite_Ultrapassado() throws OperacaoFinanceiraException {
		Usuario usuario = new Usuario();
		
		usuario.adicionarOperacao(new OperacaoFinaceira(Tipo.DEPOSITO, 10000.0)); // preciso de dinheiro para poder transferir
		usuario.adicionarOperacao(new OperacaoFinaceira(Tipo.TRANSFERENCIA, 3000.0));
		usuario.adicionarOperacao(new OperacaoFinaceira(Tipo.TRANSFERENCIA, 2100.0));

		GerenciadorConta gerenciador = new GerenciadorConta(usuario);	
		gerenciador.efetuarTransferencia("foo", "bar", 100.0);
	}
	
	@Test(expectedExceptions = LimiteTransferenciaException.class)
	public void testEfetuarTransferencia_Limite_Igual_Com_Valor_Da_Transferencia() throws OperacaoFinanceiraException {
		Usuario usuario = new Usuario();
		
		usuario.adicionarOperacao(new OperacaoFinaceira(Tipo.DEPOSITO, 10000.0)); // preciso de dinheiro para poder transferir
		usuario.adicionarOperacao(new OperacaoFinaceira(Tipo.TRANSFERENCIA, 3000.0));
		usuario.adicionarOperacao(new OperacaoFinaceira(Tipo.TRANSFERENCIA, 1900.0)); // ainda sobra 100 de limite
		

		GerenciadorConta gerenciador = new GerenciadorConta(usuario);	
		gerenciador.efetuarTransferencia("foo", "bar", 100.0); // 110 ultrapassa o limite.
	}
	
	@Test(expectedExceptions = LimiteTransferenciaException.class)
	public void testEfetuarTransferencia_Limite_Ultrapassado_Com_Valor_Da_Transferencia() throws OperacaoFinanceiraException {
		Usuario usuario = new Usuario();
		
		usuario.adicionarOperacao(new OperacaoFinaceira(Tipo.DEPOSITO, 10000.0)); // preciso de dinheiro para poder transferir
		usuario.adicionarOperacao(new OperacaoFinaceira(Tipo.TRANSFERENCIA, 3000.0));
		usuario.adicionarOperacao(new OperacaoFinaceira(Tipo.TRANSFERENCIA, 1900.0)); // ainda sobra 100 de limite
		

		GerenciadorConta gerenciador = new GerenciadorConta(usuario);	
		gerenciador.efetuarTransferencia("foo", "bar", 110.0); // 110 ultrapassa o limite.
	}
	
	@Test
	public void testEfetuarTransferencia_Com_Sucesso() throws OperacaoFinanceiraException {
		Usuario usuario = new Usuario();
		
		usuario.adicionarOperacao(new OperacaoFinaceira(Tipo.DEPOSITO, 10000.0)); // preciso de dinheiro para poder transferir
		usuario.adicionarOperacao(new OperacaoFinaceira(Tipo.TRANSFERENCIA, 3000.0));

		double saldoAnterior = usuario.saldo();
		double valorATransferir = 1500.0;
		
		GerenciadorConta gerenciador = new GerenciadorConta(usuario);	
		gerenciador.efetuarTransferencia("foo", "bar", valorATransferir); // 110 ultrapassa o limite.
		
		assertEquals(saldoAnterior - valorATransferir, usuario.saldo());
	}
	
	@Test(expectedExceptions = LimitePorDOCException.class)
	public void testEfetuarDOC_Limite_Por_DOC_Excedido() throws OperacaoFinanceiraException {
		Usuario usuario = new Usuario();
		
		usuario.adicionarOperacao(new OperacaoFinaceira(Tipo.DEPOSITO, 10000.0)); // preciso de dinheiro para poder transferir

		GerenciadorConta gerenciador = new GerenciadorConta(usuario);	
		gerenciador.efetuarDOC("foo", "bar", "baz", usuario.getLimitePorDOC() + 1.0);
	}
	
	@Test(expectedExceptions = SaldoInsuficienteException.class)
	public void testEfetuarDOC_Saldo_Insuficiente() throws OperacaoFinanceiraException {
		Usuario usuario = new Usuario();
		
		usuario.adicionarOperacao(new OperacaoFinaceira(Tipo.DEPOSITO, 1000.0)); // preciso de dinheiro para poder transferir

		GerenciadorConta gerenciador = new GerenciadorConta(usuario);	
		gerenciador.efetuarDOC("foo", "bar", "baz", 1100.0);
	}
	
	@Test
	public void testEfetuarDOC_Com_Sucesso() throws OperacaoFinanceiraException {
		Usuario usuario = new Usuario();
		
		usuario.adicionarOperacao(new OperacaoFinaceira(Tipo.DEPOSITO, 10000.0)); // preciso de dinheiro para poder transferir

		double saldoAnterior = usuario.saldo();
		double valorATransferir = 1500.0;
		double taxaDOC = 10.0;
		
		GerenciadorConta gerenciador = new GerenciadorConta(usuario);	
		gerenciador.efetuarDOC("foo", "bar", "baz", valorATransferir);
		
		assertEquals(saldoAnterior - valorATransferir - taxaDOC, usuario.saldo());
	}
	
	@Test(expectedExceptions = SaldoInsuficienteException.class)
	public void testEfetuarSaque_Saldo_Insuficiente() throws OperacaoFinanceiraException {
		Usuario usuario = new Usuario();
		
		usuario.adicionarOperacao(new OperacaoFinaceira(Tipo.DEPOSITO, 1000.0)); // preciso de dinheiro para poder transferir

		GerenciadorConta gerenciador = new GerenciadorConta(usuario);	
		gerenciador.efetuarSaque(1100.0);
	}
	
	@Test(expectedExceptions = LimiteQuantidadeDeSaquesException.class)
	public void testEfetuarSaque_Quantidade_Saques_Excedida() throws OperacaoFinanceiraException {
		Usuario usuario = new Usuario();
		
		usuario.adicionarOperacao(new OperacaoFinaceira(Tipo.DEPOSITO, 1000.0)); // preciso de dinheiro para poder transferir
		usuario.adicionarOperacao(new OperacaoFinaceira(Tipo.SAQUE, 300.0));
		usuario.adicionarOperacao(new OperacaoFinaceira(Tipo.SAQUE, 300.0));
		usuario.adicionarOperacao(new OperacaoFinaceira(Tipo.SAQUE, 300.0));

		GerenciadorConta gerenciador = new GerenciadorConta(usuario);	
		gerenciador.efetuarSaque(50.0);
	}
	
	@Test(expectedExceptions = LimiteSaqueException.class)
	public void testEfetuarSaque_Limite_Saques_Excedida() throws OperacaoFinanceiraException {
		Usuario usuario = new Usuario();
		
		usuario.adicionarOperacao(new OperacaoFinaceira(Tipo.DEPOSITO, 5000.0)); // preciso de dinheiro para poder transferir
		usuario.adicionarOperacao(new OperacaoFinaceira(Tipo.SAQUE, 1000.0));
		usuario.adicionarOperacao(new OperacaoFinaceira(Tipo.SAQUE, 1000.0));

		GerenciadorConta gerenciador = new GerenciadorConta(usuario);	
		gerenciador.efetuarSaque(50.0);
	}
	
	@Test
	public void testEfetuarSaque_Com_Sucesso() throws OperacaoFinanceiraException {
		Usuario usuario = new Usuario();
		
		usuario.adicionarOperacao(new OperacaoFinaceira(Tipo.DEPOSITO, 5000.0)); // preciso de dinheiro para poder transferir
		usuario.adicionarOperacao(new OperacaoFinaceira(Tipo.SAQUE, 1000.0));
		usuario.adicionarOperacao(new OperacaoFinaceira(Tipo.SAQUE, 500.0));

		double saldoAnterior = usuario.saldo();
		double valorSaque = 400.0;
		
		GerenciadorConta gerenciador = new GerenciadorConta(usuario);	
		gerenciador.efetuarSaque(valorSaque);
		
		assertEquals(saldoAnterior - valorSaque, usuario.saldo());
	}
	
	@Test
	public void testEfeutuarDeposito() {
		Usuario usuario = new Usuario();
		GerenciadorConta gerenciador = new GerenciadorConta(usuario);	
		
		double saldoAntigo = usuario.saldo();
		double valorDeposito = 100.0;
		
		assertEquals(saldoAntigo, 0.0);
		
		gerenciador.efetuarDeposito(valorDeposito);
		
		assertEquals(saldoAntigo + valorDeposito, usuario.saldo());
	}
}