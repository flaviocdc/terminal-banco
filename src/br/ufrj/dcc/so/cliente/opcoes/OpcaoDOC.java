package br.ufrj.dcc.so.cliente.opcoes;

import br.ufrj.dcc.so.cliente.InterfaceCLI;
import br.ufrj.dcc.so.modelo.Mensagem;
import br.ufrj.dcc.so.modelo.MensagemBuilder;

public class OpcaoDOC extends Opcao {
	private String banco;
	private String agencia;
	private String conta;
	private float valor;
	
	@Override
	public void receberParametros() {
		InterfaceCLI.console("Digite o nome do banco:");
		banco = in.nextLine();
		
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
									.param("banco", banco)
									.param("agencia", agencia)
									.param("conta", conta)
									.param("valor", valor)
									.criar();
	}
}