package br.ufrj.dcc.so.cliente;

import java.util.Scanner;

import br.ufrj.dcc.so.cliente.opcoes.Opcao;
import br.ufrj.dcc.so.cliente.opcoes.OpcaoDOC;
import br.ufrj.dcc.so.cliente.opcoes.OpcaoDeposito;
import br.ufrj.dcc.so.cliente.opcoes.OpcaoExtrato;
import br.ufrj.dcc.so.cliente.opcoes.OpcaoSair;
import br.ufrj.dcc.so.cliente.opcoes.OpcaoSaldo;
import br.ufrj.dcc.so.cliente.opcoes.OpcaoSaque;
import br.ufrj.dcc.so.cliente.opcoes.OpcaoTransferencia;
import br.ufrj.dcc.so.modelo.Mensagem;

public class CLI {

	private Scanner in = new Scanner(System.in);

	private static Opcao[] opcoes = {
		new OpcaoTransferencia(),
		new OpcaoDOC(),
		new OpcaoSaque(),
		new OpcaoDeposito(),
		new OpcaoSaldo(),
		new OpcaoExtrato(),
		new OpcaoSair()
	};
	
	public static void console(String line) {
		System.out.println(line);
	}
	
	private void exibirMenu() {
		console("Escolha uma opção:");
		
		for (int i = 0; i < opcoes.length; i++) {
			console((i+1) + ") " + opcoes[i].getNomeDescritivo());
		}
		
		console("Escolha a opção...");
	}
	
	public Mensagem escolher() {
		exibirMenu();
		
		int opcaoindex = in.nextInt();
		
		Opcao opcao = opcoes[opcaoindex - 1];
		
		opcao.receberParametros();
		return opcao.gerarMensagem();		
	}
	
	public void exibirMensagem(String msg) {
		console(msg);
	}
}
