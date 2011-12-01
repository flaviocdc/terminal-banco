package br.ufrj.dcc.so.cliente.opcoes;

import br.ufrj.dcc.so.cliente.InterfaceCLI;
import br.ufrj.dcc.so.modelo.Mensagem;
import br.ufrj.dcc.so.modelo.MensagemBuilder;

public class OpcaoTransferencia extends Opcao {
	private String agencia;
	private String conta;
	private float valor;
	
	@Override
	public void receberParametros() {
		InterfaceCLI.console("Digite a agencia:");
		agencia = in.nextLine();
		
		InterfaceCLI.console("Digite a conta de destino:");
		conta = in.nextLine();
		
		InterfaceCLI.console("Digite o valor:");
		valor = in.nextFloat();
	}

	@Override
	public Mensagem gerarMensagem() {
		return new MensagemBuilder().semErro()
									.mensagem("Transferencia")
									.param("cmd", "transferencia")
									.param("agencia", agencia)
									.param("conta", conta)
									.param("valor", valor)
									.criar();
	}
}