package br.ufrj.dcc.so.servidor;

import java.io.IOException;
import java.net.Socket;

import org.apache.log4j.Logger;

import br.ufrj.dcc.so.modelo.Mensagem;
import br.ufrj.dcc.so.modelo.MensagemBuilder;
import br.ufrj.dcc.so.modelo.Usuario;
import br.ufrj.dcc.so.util.GsonUtil;

import com.google.gson.stream.JsonWriter;

public class Worker extends Thread {
	
	private static final Logger logger = Logger.getLogger(Worker.class);

	private Socket clientSocket;
	private int clientId;
	
	private boolean autenticado;
	
	public Worker(int paramClientId, Socket paramClientSocket) {
		clientId = paramClientId;
		clientSocket = paramClientSocket;
		
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
				
				if (!autenticado) {
					String cmd = msg.param("cmd");
					if (cmd != null && cmd.equals("login")) {
						String login = msg.param("login");
						String senha = msg.param("senha");
						
						// TODO validar
						
						autenticado = true;
						logger.debug("Autenticado!");
						enviarMensagem(new MensagemBuilder().semErro().mensagem("Autenticacao foi feita com sucesso!").criar());
						continue;
					} else {
						enviarMensagem(new MensagemBuilder().comErro().mensagem("Nao autenticado, logar primeiro").criar());
						continue;
					}
				}
				
				enviarMensagem(new Mensagem("Recebida!"));
			} catch (IOException e) {
				logger.error("Erro ao ler mensagem", e);
				break;
			}
		}
		
		terminar();
	}

	private void enviarMensagem(Mensagem msgObj) throws IOException {
		JsonWriter writer = GsonUtil.criarJsonWriter(clientSocket);
		
		GsonUtil.gson().toJson(msgObj, Mensagem.class, writer);

		writer.flush();
	}

	private Mensagem lerMensagem() throws IOException {
		try {
			return GsonUtil.gson().fromJson(GsonUtil.criarJsonReader(clientSocket), Mensagem.class);
		} catch (Exception e) {
			logger.error("Erro ao ler mensagem.", e);
			return null;
		}
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