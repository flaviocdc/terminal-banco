package br.ufrj.dcc.so.cliente.opcoes;

import br.ufrj.dcc.so.cliente.CLI;
import br.ufrj.dcc.so.modelo.Mensagem;
import br.ufrj.dcc.so.modelo.MensagemBuilder;

public class OpcaoTransferencia extends Opcao {
	private String agencia;
	private String conta;
	private float valor;
	
	@Override
	public void receberParametros() {
		CLI.console("Digite a agencia:");
		agencia = in.nextLine();
		
		CLI.console("Digite a conta de destino:");
		conta = in.nextLine();
		
		CLI.console("Digite o valor:");
		valor = in.nextFloat();
	}

	@Override
	public Mensagem gerarMensagem() {
		return new MensagemBuilder().semErro()
									.mensagem("Transferencia")
									.comando("transferencia")
									.param("agencia", agencia)
									.param("conta", conta)
									.param("valor", valor)
									.criar();
	}
}