package br.ufrj.dcc.so.cliente;

import java.util.Scanner;

import org.apache.log4j.Logger;

import br.ufrj.dcc.so.modelo.Mensagem;
import br.ufrj.dcc.so.modelo.MensagemBuilder;

public class InterfaceCLI {

	private static final Logger logger = Logger.getLogger(InterfaceCLI.class);
	
	private Scanner in = new Scanner(System.in);

	private static Class<?>[] opcoes = {
		OpcaoTransferencia.class
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
			e.printStackTrace();
			//logger.debug("Impossivel executar opcao " + opcao, e);
			return null;
		}
	}
	
	private abstract static class Opcao {
		protected Scanner in = new Scanner(System.in);
		
		public abstract void receberParametros();
		public abstract Mensagem gerarMensagem();
	}
	
	public static class OpcaoTransferencia extends Opcao {
		private String conta;
		private float valor;
		
		@Override
		public void receberParametros() {
			console("Digite a conta de destino:");
			conta = in.nextLine();
			
			console("Digite o valor:");
			valor = in.nextFloat();
		}

		@Override
		public Mensagem gerarMensagem() {
			return new MensagemBuilder().semErro()
										.mensagem("Transferencia")
										.param("cmd", "transferencia")
										.param("conta", conta)
										.param("valor", valor)
										.criar();
		}
		
	}
}
