package br.ufrj.dcc.so.cliente.opcoes;

import br.ufrj.dcc.so.cliente.InterfaceCLI;
import br.ufrj.dcc.so.modelo.Mensagem;
import br.ufrj.dcc.so.modelo.MensagemBuilder;

public class OpcaoDeposito extends Opcao {
	private float valor;
	
	@Override
	public void receberParametros() {
		InterfaceCLI.console("Digite o valor:");
		valor = in.nextFloat();
	}

	@Override
	public Mensagem gerarMensagem() {
		return new MensagemBuilder().semErro()
									.mensagem("Deposito")
									.comando("deposito")
									.param("valor", valor)
									.criar();
	}
}