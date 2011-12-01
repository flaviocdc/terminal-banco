package br.ufrj.dcc.so.servidor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import org.apache.log4j.Logger;

public class Worker extends Thread {
	
	private static final Logger logger = Logger.getLogger(Worker.class);

	private Socket clientSocket;
	private int clientId;
	
	private PrintWriter out;
	private BufferedReader in;
	
	public Worker(int paramClientId, Socket paramClientSocket) {
		clientId = paramClientId;
		clientSocket = paramClientSocket;
		
		setName("Worker-" + clientId);
	}
	
	@Override
	public void run() {
		try {
			out = new PrintWriter(clientSocket.getOutputStream());
			in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
		} catch (IOException e) {
			logger.error("Problema ao inicial stream de comunicacao", e);
		}
		
		String cmd = null;
		while (true) {
			logger.debug("Esperando comando");
			try {
				cmd = in.readLine();
				if (cmd == null) {
					break;
				}
				
				logger.debug("Comando recebido: '" + cmd + "'");
				
				out.println("Comando recebido");
				out.flush();
			} catch (IOException e) {
				logger.error("Erro ao ler comando", e);
				break;
			}
		}
		
		terminar();
	}

	private void terminar() {
		logger.debug("Terminando worker");
		
		try {
			clientSocket.close();
		} catch (IOException e) {
			logger.debug("Erro ao fechar worker socket #" + clientId, e);
		}
	}
	
}
