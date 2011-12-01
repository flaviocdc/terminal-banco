package br.ufrj.dcc.so.cliente;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Scanner;

import org.apache.log4j.Logger;

import br.ufrj.dcc.so.modelo.Mensagem;
import br.ufrj.dcc.so.modelo.MensagemBuilder;
import br.ufrj.dcc.so.util.GsonUtil;
import br.ufrj.dcc.so.util.LoggerUtil;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

public class Cliente {

	private static final Logger logger = Logger.getLogger(Cliente.class);
	
	private Socket clientSocket;
	
	private JsonWriter jsonWriter;
	private JsonReader jsonReader;
	
	private boolean ocorreuErro = false;
	private boolean desligar = false;
	
	private Scanner scanner;
	
	public static void main(String[] args) {
		LoggerUtil.initLog4j();
		
		Cliente cliente = new Cliente();
		
		cliente.init();
		cliente.loop();
	}
	
	private void init() {
		clientSocket = new Socket();
		
		try {
			clientSocket.connect(new InetSocketAddress("127.0.0.1", 1234));
		} catch (IOException e) {
			logger.debug("Erro ao inicializar a conexao", e);
		}
		
		try {
			jsonWriter = new JsonWriter(new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream())));
			jsonReader = new JsonReader( new BufferedReader(new InputStreamReader(clientSocket.getInputStream())));
		} catch (IOException e) {
			logger.error("Erro ao inicializar streams", e);
			
			ocorreuErro = true;
		}
		
		scanner = new Scanner(System.in);
	}
	
	private void loop() {
		String msg = null;
		
		while (!desligar || !ocorreuErro) {
			logger.debug("Esperando nova mensagem");
			
			msg = scanner.nextLine();
			if (msg.equals("close")) {
				desligar = true;
				continue;
			}
			
			boolean enviado = enviar(msg);
			
			if (enviado)
				receber();
		}
		
		terminar();
	}

	public Mensagem receber() {
		logger.debug("Esperando resposta");

		Mensagem response = GsonUtil.gson().fromJson(jsonReader, Mensagem.class);
		
		logger.debug("Resposta do servidor: " + response);
		
		return response;
	}
	
	public boolean enviar(String msg) {
		logger.debug("Enviando mensagem'" + msg + "' para o servidor");

		Mensagem msgObj = new MensagemBuilder().mensagem(msg)
											   .semErro()
											   .criar();
		
		GsonUtil.gson().toJson(msgObj, Mensagem.class, jsonWriter);
		
		try {
			jsonWriter.flush();
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
