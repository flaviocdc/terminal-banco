package br.ufrj.dcc.so.cliente.opcoes;

import br.ufrj.dcc.so.modelo.Mensagem;

public class OpcaoSair extends Opcao {

	@Override
	public void receberParametros() {
	}

	@Override
	public Mensagem gerarMensagem() {
		System.exit(0);
		
		return null;
	}

	@Override
	public String getNomeDescritivo() {
		return "Fechar sistema";
	}

}
