package br.ufrj.dcc.so.servidor;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang.time.DateUtils;
import org.apache.log4j.Logger;

import br.ufrj.dcc.so.modelo.OperacaoFinaceira;
import br.ufrj.dcc.so.modelo.OperacaoFinaceira.Tipo;
import br.ufrj.dcc.so.modelo.Usuario;
import br.ufrj.dcc.so.servidor.exceptions.LimitePorDOCException;
import br.ufrj.dcc.so.servidor.exceptions.LimiteQuantidadeDeSaquesException;
import br.ufrj.dcc.so.servidor.exceptions.LimiteSaqueException;
import br.ufrj.dcc.so.servidor.exceptions.LimiteTransferenciaException;
import br.ufrj.dcc.so.servidor.exceptions.OperacaoFinanceiraException;
import br.ufrj.dcc.so.servidor.exceptions.SaldoInsuficienteException;

public class GerenciadorConta {

	private static final Logger logger = Logger.getLogger(GerenciadorConta.class);
	
	private Usuario usuario;

	public GerenciadorConta(Usuario usuario) {
		this.usuario = usuario;
	}

	public boolean verificarSaldo(double retirada) {
		return usuario.saldo() - retirada > 0;
	}

	public void garantirSaldoSuficiente(double valor) throws SaldoInsuficienteException {
		logger.debug("Gerantindo saldo suficiente");
		
		if (!verificarSaldo(valor)) {
			throw new SaldoInsuficienteException("Sem saldo para fazer operação");
		}
		
		logger.debug("Saldo disponivel, continuar!");
	}
	
	public void efetuarTransferencia(String agencia, String conta, double valor) throws OperacaoFinanceiraException {
		logger.debug("Comecando a efetuar transferencia");
		
		garantirSaldoSuficiente(valor);
		
		List<OperacaoFinaceira> transferencias = usuario.recuperarTransferencias();
		
		double totalDiaAtual = 0.0;
		for (OperacaoFinaceira op : transferencias) {
			if (DateUtils.isSameDay(op.getData(), new Date())) {
				totalDiaAtual += op.getValor();
			}
		}
		
		totalDiaAtual = -totalDiaAtual; // valores de transferencias sao sempre negativos
		
		if ((totalDiaAtual + valor) >= usuario.getLimiteTransferencias()) {
			throw new LimiteTransferenciaException();
		}
		
		usuario.adicionarOperacao(new OperacaoFinaceira(Tipo.TRANSFERENCIA, valor));
		
		logger.debug("Transferido!");
		// TODO colocar o dinheiro na conta do outro usuario
	}
	
	public void efetuarDOC(String banco, String agencia, String conta, double valor) throws OperacaoFinanceiraException {
		logger.debug("Comecando a efetuando DOC");
		
		if (valor > usuario.getLimitePorDOC()) {
			throw new LimitePorDOCException(usuario.getLimitePorDOC());
		}
		
		garantirSaldoSuficiente(valor);
		
		usuario.adicionarOperacao(new OperacaoFinaceira(Tipo.DOC, valor));
		usuario.adicionarOperacao(new OperacaoFinaceira(Tipo.TARIFA_SERVICO, 10.0));
		
		logger.debug("DOC efetuado!");
	}
	
	public void efetuarSaque(double valor) throws OperacaoFinanceiraException {
		logger.debug("Comecando a efetuar saque!");
		
		garantirSaldoSuficiente(valor);
		
		List<OperacaoFinaceira> saques = usuario.recuperarSaques();
		
		double valorSaquesHoje = 0.0;
		int qtdSaquesHoje = 0;
		
		for (OperacaoFinaceira op : saques) {
			if (DateUtils.isSameDay(op.getData(), new Date())) {
				valorSaquesHoje += op.getValor();
				qtdSaquesHoje++;
			}
		}
		
		if (qtdSaquesHoje >= usuario.getLimiteQuantidadeDeSaques()) {
			throw new LimiteQuantidadeDeSaquesException(usuario.getLimiteQuantidadeDeSaques());
		}
		
		valorSaquesHoje = -valorSaquesHoje; // valores de saques sao sempre negativos
		
		if (valorSaquesHoje >= usuario.getLimiteSaques()) {
			throw new LimiteSaqueException(usuario.getLimiteSaques());
		}
		
		usuario.adicionarOperacao(new OperacaoFinaceira(Tipo.SAQUE, valor));
		
		logger.debug("Saque realizado!");
	}
	
	public void efetuarDeposito(double valor) {
		logger.debug("Comecando a efetuar deposito!");
		
		usuario.adicionarOperacao(new OperacaoFinaceira(Tipo.DEPOSITO, valor));
		
		logger.debug("Deposito efetuado!");
	}
}