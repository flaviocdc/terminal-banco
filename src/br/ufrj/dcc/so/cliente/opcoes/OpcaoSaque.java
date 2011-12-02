package br.ufrj.dcc.so.cliente.opcoes;

import br.ufrj.dcc.so.cliente.CLI;
import br.ufrj.dcc.so.modelo.Mensagem;
import br.ufrj.dcc.so.modelo.MensagemBuilder;

public class OpcaoSaque extends Opcao {
	private double valor;
	
	@Override
	public void receberParametros() {
		CLI.console("Digite o valor:");
		valor = in.nextDouble();
	}

	@Override
	public Mensagem gerarMensagem() {
		return new MensagemBuilder().semErro()
									.mensagem("Saque")
									.comando("saque")
									.param("cmd", "transferencia")
									.param("valor", valor)
									.criar();
	}
}