package br.ufrj.dcc.so.servidor;

import java.io.IOException;
import java.net.Socket;

import org.apache.log4j.Logger;

import br.ufrj.dcc.so.modelo.Mensagem;
import br.ufrj.dcc.so.modelo.MensagemBuilder;
import br.ufrj.dcc.so.modelo.Usuario;
import br.ufrj.dcc.so.servidor.exceptions.OperacaoFinanceiraException;
import br.ufrj.dcc.so.util.GsonUtil;

import com.google.gson.stream.JsonWriter;

public class Worker extends Thread {
	
	private static final Logger logger = Logger.getLogger(Worker.class);

	private Socket clientSocket;
	private int clientId;
	
	private boolean autenticado;
	private Usuario usuarioAtual;
	
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
						String agencia = msg.param("agencia");
						String conta = msg.param("conta");

						String senha = msg.param("senha");
						
						autenticado = GerenciadorUsuarios.instance().verificarSenha(agencia, conta, senha);
						usuarioAtual = GerenciadorUsuarios.instance().recuperarUsuario(agencia, conta);
						
						if (!autenticado || usuarioAtual == null) {
							logger.debug("Usuario inexistente ou senha errada!");
							enviarMensagem(new MensagemBuilder().comErro().comando("baduserpass").mensagem("Autenticacao falhou!").criar());
							
							break;
						}
						
						if (usuarioAtual.podeLogar()) {
							logger.debug("Autenticado!");
							
							usuarioAtual.logou();
							
							enviarMensagem(new MensagemBuilder().semErro().comando("authreply").mensagem("Autenticacao foi feita com sucesso!").criar());
						} else {
							logger.debug("Autenticado, mas numero maximo de conexoes excedidos");
							enviarMensagem(new MensagemBuilder().comErro().comando("authlimit").mensagem("Autenticado, mas numero maximo de conexoes excedidos!").criar());
						}
						continue;
					} else {
						enviarMensagem(new MensagemBuilder().comErro().comando("authfail").mensagem("Nao autenticado, logar primeiro").criar());
						break;
					}
				}
				
				try {
					Mensagem resposta = interpretarMensagem(msg);
					enviarMensagem(resposta);
				} catch (OperacaoFinanceiraException e) {
					enviarMensagem(new MensagemBuilder().comErro().comando("operacaofinanceira").mensagem(e.getMessage()).criar());
					
					logger.debug(e);
				}
				
			} catch (IOException e) {
				logger.error("Erro ao ler mensagem", e);
				break;
			}
		}
		
		terminar();
	}

	private Mensagem interpretarMensagem(Mensagem msg) throws OperacaoFinanceiraException {
		GerenciadorConta gerenciador = new GerenciadorConta(usuarioAtual);
		
		MensagemBuilder builder = new MensagemBuilder().semErro().comando("cmdreply").mensagem("Operação realizada!");
		
		String cmd = msg.param("cmd");
		Double valor = msg.param("valor", Double.class);
		
		if (cmd.equals("deposito")) {
			gerenciador.efetuarDeposito(valor);
			
			builder.mensagem("Deposito realizado com sucesso!");
		} else if (cmd.equals("doc")) {
			String banco = msg.param("banco");
			String agencia = msg.param("agencia");
			String conta = msg.param("conta");
			
			gerenciador.efetuarDOC(banco, agencia, conta, valor);
			
			builder.mensagem("DOC efetuado com sucesso!");
		} else if (cmd.equals("saque")) {
			gerenciador.efetuarSaque(valor);
			
			builder.mensagem("Saque efetuado com sucesso!");
		} else if (cmd.equals("transferencia")) {
			String agencia = msg.param("agencia");
			String conta = msg.param("conta");
			
			gerenciador.efetuarTransferencia(agencia, conta, valor);
			
			builder.mensagem("Transferencia efetuada com sucesso!");
		} else if (cmd.equals("saldo")) {
			builder.mensagem("Saldo atual: " + usuarioAtual.saldo());
		}
		
		return builder.criar();
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
		
		if (usuarioAtual != null) {
			usuarioAtual.deslogou();
		}
		
		try {
			clientSocket.close();
		} catch (IOException e) {
			logger.debug("Erro ao fechar worker socket #" + clientId, e);
		}
	}
	
}