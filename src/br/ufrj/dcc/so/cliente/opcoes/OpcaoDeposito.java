package br.ufrj.dcc.so.cliente.opcoes;

import br.ufrj.dcc.so.cliente.CLI;
import br.ufrj.dcc.so.modelo.Mensagem;
import br.ufrj.dcc.so.modelo.MensagemBuilder;

public class OpcaoDeposito extends Opcao {
	private double valor;
	
	@Override
	public void receberParametros() {
		CLI.console("Digite o valor:");
		valor = in.nextDouble();
	}

	@Override
	public Mensagem gerarMensagem() {
		return new MensagemBuilder().semErro()
									.mensagem("Deposito")
									.comando("deposito")
									.param("valor", valor)
									.criar();
	}

	@Override
	public String getNomeDescritivo() {
		return "Efetuar Depósito";
	}
}