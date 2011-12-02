package br.ufrj.dcc.so.cliente;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Scanner;

import org.apache.log4j.Logger;

import br.ufrj.dcc.so.modelo.Mensagem;
import br.ufrj.dcc.so.modelo.MensagemBuilder;
import br.ufrj.dcc.so.util.GsonUtil;
import br.ufrj.dcc.so.util.LoggerUtil;

import com.google.gson.stream.JsonWriter;

public class Cliente {

	private static final Logger logger = Logger.getLogger(Cliente.class);
	
	private Socket clientSocket;
	
	private boolean ocorreuErro = false;
	private boolean desligar = false;
	
	private String agencia;
	private String conta;
	private String senha;
	
	private CLI cli = new CLI();
	
	public static void main(String[] args) {
		LoggerUtil.initLog4j();
		
		Cliente cliente = new Cliente();
		
		cliente.init();
		cliente.loop();
	}
	
	private void init() {
		clientSocket = new Socket();
		
		try {
			clientSocket.connect(new InetSocketAddress("0.0.0.0", 1234));
		} catch (IOException e) {
			logger.debug("Erro ao inicializar a conexao", e);
		}
	}
	
	private void recuperarInformacoesLogin() {
		Scanner in = new Scanner(System.in);
		
		cli.exibirMensagem("Digite sua agencia:");
		agencia = in.nextLine();
		
		cli.exibirMensagem("Digite sua conta:");
		conta = in.nextLine();
		
		cli.exibirMensagem("Digite a senha");
		senha = in.nextLine();
	}
	
	private void loop() {
		
		recuperarInformacoesLogin();
		
		Mensagem loginMsg = new MensagemBuilder().semErro()
    										  .comando("login")
											  .mensagem("Autenticar")
											  .param("agencia", agencia)
											  .param("conta", conta)
											  .param("senha", senha)
											  .criar();
		
		enviar(loginMsg);
		Mensagem loginResposta = receber();
		if (loginResposta.isErro()) {
			logger.debug("Login invalido!");
			
			cli.exibirMensagem(loginResposta.param("msg"));
			
			return;
		}
		
		while (!desligar || !ocorreuErro) {
			logger.debug("Esperando nova mensagem");
			
			Mensagem msg = cli.escolher();
			
			boolean enviado = enviar(msg);
			
			if (enviado) {
				receber();
			}
		}
		
		terminar();
	}

	public Mensagem receber() {
		try {
			logger.debug("Esperando resposta");
	
			Mensagem response = GsonUtil.gson().fromJson(GsonUtil.criarJsonReader(clientSocket), Mensagem.class);
			
			logger.debug("Resposta do servidor: " + response);
			
			return response;
		} catch (Exception e) {
			ocorreuErro = true;
			return null;
		}
	}
	
	public boolean enviarTexto(String msg) {
		logger.debug("Enviando mensagem '" + msg + "' para o servidor");

		Mensagem msgObj = new MensagemBuilder().comando("textmsg")
											   .mensagem(msg)
											   .semErro()
											   .criar();
		
		return enviar(msgObj);
	}

	private boolean enviar(Mensagem msgObj) {
		try {
			JsonWriter writer = GsonUtil.criarJsonWriter(clientSocket);
			
			GsonUtil.gson().toJson(msgObj, Mensagem.class, writer);
			
			writer.flush();
		} catch (IOException e) {
			logger.error("Erro ao enviar mensagem");
			
			ocorreuErro = true;
			
			return false;
		}
		
		return true;
	}

	private void terminar() {
		try {
			clientSocket.close();
		} catch (IOException e) {
			logger.error("Erro ao tentar fechar o socket", e);
		}
	}
	
}
