package br.ufrj.dcc.so.modelo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.log4j.Logger;

import br.ufrj.dcc.so.modelo.OperacaoFinaceira.Tipo;
import br.ufrj.dcc.so.servidor.GerenciadorUsuarios;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;

public class Usuario {
	
	private static final Logger logger = Logger.getLogger(Usuario.class);

	private String nome;
	
	private String agencia;
	private String conta;
	private String senha;
	
	private transient Lock lock = new ReentrantLock();
	protected transient int contadorLogin = 0;
	
	private List<OperacaoFinaceira> operacoes;

	public Usuario() {
	}
	
	public Usuario(String nome, String agencia, String conta, String senha) {
		this.nome = nome;
		this.agencia = agencia;
		this.conta = conta;
		this.senha = senha;
	}

	public String getNome() {
		return nome;
	}

	public String getAgencia() {
		return agencia;
	}

	public String getConta() {
		return conta;
	}

	public String getSenha() {
		return senha;
	}
	
	public boolean podeLogar() {
		try {
			lock.lock();
			
			return contadorLogin == 0;
		} finally {
			lock.unlock();
		}
	}
	
	public void logou() {
		try {
			lock.lock();
			
			contadorLogin++;
		} finally {
			lock.unlock();
		}
	}
	
	public void deslogou() {
		try {
			lock.lock();
			
			contadorLogin--;
		} finally {
			lock.unlock();
		}
	}
	
	public String getId() {
		return gerarIdUsuario(this);
	}

	public static String gerarIdUsuario(Usuario usuario) {
		return gerarIdUsuario(usuario.agencia, usuario.conta);
	}
	
	public static String gerarIdUsuario(String agencia, String conta) {
		return agencia + "|" + conta;
	}

	public List<OperacaoFinaceira> getOperacoes() {
		try {
			lock.lock();
			
			return Collections.unmodifiableList(operacoes);
		} finally {
			lock.unlock();
		}
	}
	
	protected void setOperacoes(List<OperacaoFinaceira> operacoes) {
		this.operacoes = operacoes;
	}

	public double saldo() {
		try {
			lock.lock();
			
			inicializarOperacaoes();
			
			double saldo = 0.0;
			for (OperacaoFinaceira op : operacoes) {
				saldo += op.getValor();
			}
			return saldo;
		} finally {
			lock.unlock();
		}
	}
	
	public void adicionarOperacao(OperacaoFinaceira op) {
		try {
			lock.lock();
			
			inicializarOperacaoes();
			
			operacoes.add(op);
			
			salvarDados();
		} finally {
			lock.unlock();
		}
	}

	private void inicializarOperacaoes() {
		if (operacoes == null)
			operacoes = new ArrayList<OperacaoFinaceira>();
	}
	
	private void salvarDados() {
		try {
			GerenciadorUsuarios.instance().salvar();
		} catch (IOException e) {
			logger.debug("Erro ao tentar salvar dados dos usuarios", e);
		}
	}
	
	public List<OperacaoFinaceira> recuperarDOCs() {
		return recuperarPorTipo(Tipo.DOC);
	}
	
	public List<OperacaoFinaceira> recuperarSaques() {
		return recuperarPorTipo(Tipo.SAQUE);
	}
	
	public List<OperacaoFinaceira> recuperarTransferencias() {
		return recuperarPorTipo(Tipo.TRANSFERENCIA);
	}
	
	public List<OperacaoFinaceira> recuperarDepositos() {
		return recuperarPorTipo(Tipo.DEPOSITO);
	}
	
	private List<OperacaoFinaceira> recuperarPorTipo(final Tipo tipo) {
		try {
			lock.lock();
		
			Collection<OperacaoFinaceira> aux = Collections2.filter(operacoes, new Predicate<OperacaoFinaceira>() {
				@Override
				public boolean apply(OperacaoFinaceira op) {
					return op.getTipo().equals(tipo);
				}
			});
			
			return new ArrayList<OperacaoFinaceira>(aux);
		} finally {
			lock.unlock();
		}
	}

	public double getLimiteTransferencias() {
		return 5000.0;
	}

	public double getLimitePorDOC() {
		return 3000.0;
	}

	public int getLimiteQuantidadeDeSaques() {
		return 3;
	}

	public double getLimiteSaques() {
		return 2000.0;
	}
}