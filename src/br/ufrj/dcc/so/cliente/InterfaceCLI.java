package br.ufrj.dcc.so.cliente;

import java.util.Scanner;

import org.apache.log4j.Logger;

import br.ufrj.dcc.so.cliente.opcoes.Opcao;
import br.ufrj.dcc.so.cliente.opcoes.OpcaoDOC;
import br.ufrj.dcc.so.cliente.opcoes.OpcaoDeposito;
import br.ufrj.dcc.so.cliente.opcoes.OpcaoSaque;
import br.ufrj.dcc.so.cliente.opcoes.OpcaoTransferencia;
import br.ufrj.dcc.so.modelo.Mensagem;

public class InterfaceCLI {

	private static final Logger logger = Logger.getLogger(InterfaceCLI.class);
	
	private Scanner in = new Scanner(System.in);

	private static Class<?>[] opcoes = {
		OpcaoTransferencia.class,
		OpcaoDOC.class,
		OpcaoSaque.class,
		OpcaoDeposito.class
	};
	
	public static void console(String line) {
		System.out.println(line);
	}
	
	private void exibirMenu() {
		console("Escolha uma opção:");
		
		console("1) Transferencia");
		console("2) DOC");
		console("3) Saque");
		console("4) Deposito");
		
		console("Escolha a opção...");
	}
	
	public Mensagem escolher() {
		exibirMenu();
		
		int opcaoindex = in.nextInt();
		
		Opcao opcao = instanciarOpcao(opcaoindex);
		
		opcao.receberParametros();
		return opcao.gerarMensagem();		
	}
	
	private Opcao instanciarOpcao(int opcao) {
		Class<?> clazz = opcoes[opcao - 1];
		
		try {
			return (Opcao) clazz.newInstance();
		} catch (Exception e) {
			logger.debug("Impossivel executar opcao " + opcao, e);
			return null;
		}
	}
}
