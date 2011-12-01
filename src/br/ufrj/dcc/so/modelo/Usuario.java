package br.ufrj.dcc.so.modelo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import br.ufrj.dcc.so.modelo.OperacaoFinaceira.Tipo;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;

public class Usuario {

	private String nome;
	
	private String agencia;
	private String conta;
	
	private transient Lock lock = new ReentrantLock();
	
	private List<OperacaoFinaceira> operacoes;

	public String getNome() {
		return nome;
	}

	public String getAgencia() {
		return agencia;
	}

	public String getConta() {
		return conta;
	}

	public List<OperacaoFinaceira> getOperacoes() {
		return Collections.unmodifiableList(operacoes);
	}
	
	protected void setOperacoes(List<OperacaoFinaceira> operacoes) {
		this.operacoes = operacoes;
	}

	public double saldo() {
		double saldo = 0.0;
		for (OperacaoFinaceira op : operacoes) {
			saldo += op.getValor();
		}
		return saldo;
	}
	
	public void adicionarOperacao(OperacaoFinaceira op) {
		try {
			lock.lock();
			
			operacoes.add(op);
		} finally {
			lock.unlock();
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
	
	public List<OperacaoFinaceira> recuperarPorTipo(final Tipo tipo) {
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
}