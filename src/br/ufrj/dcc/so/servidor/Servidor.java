package br.ufrj.dcc.so.servidor;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.log4j.Logger;

public class Servidor {

	private static final Logger logger = Logger.getLogger(Servidor.class);
	
	private ServerSocket serverSocket;
	
	private AtomicInteger currentClientId = new AtomicInteger(0);
	
	public static void main(String[] args) {
		Servidor servidor = new Servidor();
		
		try {
			servidor.init();
			servidor.loop();
		} catch (Exception e) {
			logger.error("Error ao executar o servidor", e);
		}
	}
	
	public void init() throws IOException {
		serverSocket = new ServerSocket(1234);
	}
	
	public void loop() {
		while (true) {
			try {
				logger.debug("Esperando novas conexoes...");
				
				/* bloqueio ate receber uma requisicao */ 
				Socket clientSocket = serverSocket.accept();
				
				logger.debug("Conexao recebida, iniciando thread para processar");
				new Worker(currentClientId.incrementAndGet(), clientSocket).start();
				logger.debug("Thread iniciada");
			} catch (IOException e) {
				logger.debug("Erro ao tentar aceitar novas conexoes!", e);
			}
		}
	}
	
}
