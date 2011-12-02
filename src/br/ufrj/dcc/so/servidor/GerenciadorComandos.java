package br.ufrj.dcc.so.servidor;

import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;

import br.ufrj.dcc.so.modelo.Mensagem;
import br.ufrj.dcc.so.modelo.MensagemBuilder;
import br.ufrj.dcc.so.modelo.OperacaoFinaceira;
import br.ufrj.dcc.so.modelo.Usuario;
import br.ufrj.dcc.so.servidor.exceptions.OperacaoFinanceiraException;

public class GerenciadorComandos {

	private Usuario usuarioAtual;
	
	public GerenciadorComandos(Usuario usuarioAtual) {
		this.usuarioAtual = usuarioAtual;
	}

	public Mensagem interpretarMensagem(Mensagem msg) throws OperacaoFinanceiraException {
		GerenciadorConta gerenciador = new GerenciadorConta(usuarioAtual);
		
		MensagemBuilder builder = new MensagemBuilder().semErro().comando("cmdreply").mensagem("Operação realizada!");
		
		String cmd = msg.param("cmd");
		Double valor = msg.param("valor", Double.class);
		
		if (cmd.equals("deposito")) {
			gerenciador.efetuarDeposito(valor);
			
			builder.mensagem("Deposito realizado com sucesso!");
		} else if (cmd.equals("doc")) {
			String banco = msg.param("banco");
			String agencia = msg.param("agencia");
			String conta = msg.param("conta");
			
			gerenciador.efetuarDOC(banco, agencia, conta, valor);
			
			builder.mensagem("DOC efetuado com sucesso!");
		} else if (cmd.equals("saque")) {
			gerenciador.efetuarSaque(valor);
			
			builder.mensagem("Saque efetuado com sucesso!");
		} else if (cmd.equals("transferencia")) {
			String agencia = msg.param("agencia");
			String conta = msg.param("conta");
			
			gerenciador.efetuarTransferencia(agencia, conta, valor);
			
			builder.mensagem("Transferencia efetuada com sucesso!");
		} else if (cmd.equals("saldo")) {
			builder.mensagem("Saldo atual: " + usuarioAtual.saldo());
		} else if (cmd.equals("extrato")) {
			gerarExtrato(builder);
		}
		
		return builder.criar();
	}

	private void gerarExtrato(MensagemBuilder builder) {
		Set<OperacaoFinaceira> operacoesOrdendas = new TreeSet<OperacaoFinaceira>(new Comparator<OperacaoFinaceira>() {
			@Override
			public int compare(OperacaoFinaceira o1, OperacaoFinaceira o2) {
				return o1.getData().compareTo(o2.getData());
			}
		});
		
		operacoesOrdendas.addAll(usuarioAtual.getOperacoes());
		
		StringBuilder sb = new StringBuilder();
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		for (OperacaoFinaceira op : operacoesOrdendas) {
			String valorTxt = op.getValor() < 0 ? "(" + (-op.getValor()) + ")" : "" + op.getValor();
			
			sb.append(sdf.format(op.getData())).append("\t");
			sb.append(op.getTipo()).append("\t-\t").append(valorTxt).append("\n");
		}
		
		builder.mensagem(sb.toString());
	}

}
