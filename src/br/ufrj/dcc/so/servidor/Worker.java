package br.ufrj.dcc.so.servidor;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

import org.apache.log4j.Logger;

import br.ufrj.dcc.so.modelo.Mensagem;
import br.ufrj.dcc.so.util.GsonUtil;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

public class Worker extends Thread {
	
	private static final Logger logger = Logger.getLogger(Worker.class);

	private Socket clientSocket;
	private int clientId;
	
	private JsonWriter jsonWriter;
	private JsonReader jsonReader;
	
	public Worker(int paramClientId, Socket paramClientSocket) {
		clientId = paramClientId;
		clientSocket = paramClientSocket;
		
		try {
			jsonWriter = new JsonWriter(new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream())));
			jsonReader = new JsonReader(new BufferedReader(new InputStreamReader(clientSocket.getInputStream())));
		} catch (IOException e) {
			logger.error("Problema ao inicial stream de comunicacao", e);
		}
		
		setName("Worker-" + clientId);
	}
	
	@Override
	public void run() {
		
		Mensagem msg = null;
		while (true) {
			logger.debug("Esperando mensagem");
			try {
				msg = lerMensagem();
				if (msg == null) {
					break;
				}
				
				logger.debug("Mensagem recebida: '" + msg + "'");
				
				GsonUtil.gson().toJson(new Mensagem("Recebida!"), Mensagem.class, jsonWriter);
				
				jsonWriter.flush();
			} catch (IOException e) {
				logger.error("Erro ao ler mensagem", e);
				break;
			}
		}
		
		terminar();
	}

	private Mensagem lerMensagem() throws IOException {
		Mensagem msg = new Mensagem();
		
		try {
			GsonUtil.gson().fromJson(jsonReader, Mensagem.class);
		} catch (Exception e) {
			logger.error("Erro ao ler mensagem.", e);
			return null;
		}
		
		return msg;
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
