package br.ufrj.dcc.so.cliente;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Scanner;

import org.apache.log4j.Logger;

import br.ufrj.dcc.so.log.Log4jHelper;

public class Cliente {

	private static final Logger logger = Logger.getLogger(Cliente.class);
	
	private Socket clientSocket;
	private PrintWriter out;
	private BufferedReader in;
	
	private Scanner scanner;
	
	public static void main(String[] args) {
		Log4jHelper.initLog4j();
		
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
			out = new PrintWriter(clientSocket.getOutputStream());
			in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
		} catch (IOException e) {
			logger.error("Erro ao inicializar streams", e);
		}
		
		scanner = new Scanner(System.in);
	}
	
	private void loop() {
		String cmd = null;
		String response = null;
		
		while (true) {
			logger.debug("Esperando novo comando");
			
			cmd = scanner.nextLine();
			if (cmd.equals("close")) {
				terminar();
				break;
			}
			
			logger.debug("Enviando comando '" + cmd + "' para o servidor");
			out.println(cmd);
			out.flush();
			
			logger.debug("Esperando resposta");
			response = null;
			try {
				response = in.readLine();
				logger.debug("Resposta do servidor: " + response);
			} catch (IOException e) {
				logger.error("Erro esperando pela resposta do servidor", e);
				
				terminar();
				break;
			}
			
		}
	}

	private void terminar() {
		try {
			clientSocket.close();
		} catch (IOException e) {
			logger.error("Erro ao tentar fechar o socket", e);
		}
	}
	
}
