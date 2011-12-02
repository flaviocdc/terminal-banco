package br.ufrj.dcc.so.cliente.opcoes;

import br.ufrj.dcc.so.modelo.Mensagem;
import br.ufrj.dcc.so.modelo.MensagemBuilder;

public class OpcaoExtrato extends Opcao {

	@Override
	public void receberParametros() {
		// TODO Auto-generated method stub

	}

	@Override
	public Mensagem gerarMensagem() {
		return new MensagemBuilder().semErro()
									.mensagem("Extrato")
									.comando("extrato")
									.criar();
	}

	@Override
	public String getNomeDescritivo() {
		return "Imprimir extrato";
	}

}
