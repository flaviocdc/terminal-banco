package br.ufrj.dcc.so.cliente.opcoes;

import br.ufrj.dcc.so.modelo.Mensagem;
import br.ufrj.dcc.so.modelo.MensagemBuilder;

public class OpcaoSaldo extends Opcao {

	@Override
	public void receberParametros() {

	}

	@Override
	public Mensagem gerarMensagem() {
		return new MensagemBuilder().semErro()
									.mensagem("Saldo")
									.comando("saldo")
									.criar();
	}

}
