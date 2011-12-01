package br.ufrj.dcc.so.cliente.opcoes;

import java.util.Scanner;

import br.ufrj.dcc.so.modelo.Mensagem;

public abstract class Opcao {
	protected Scanner in = new Scanner(System.in);
	
	public abstract void receberParametros();
	public abstract Mensagem gerarMensagem();
}