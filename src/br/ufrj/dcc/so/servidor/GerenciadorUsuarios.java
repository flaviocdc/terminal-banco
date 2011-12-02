package br.ufrj.dcc.so.servidor;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.log4j.Logger;

import br.ufrj.dcc.so.modelo.Usuario;
import br.ufrj.dcc.so.util.GsonUtil;

import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

public class GerenciadorUsuarios {

	private static final Logger logger = Logger.getLogger(GerenciadorUsuarios.class);
	private static final TypeToken<HashMap<String, Usuario>> typeToken = new TypeToken<HashMap<String, Usuario>>() { };
	
	private static GerenciadorUsuarios instance;
	
	private Map<String, Usuario> usuarios;
	private Lock writeLock = new ReentrantLock();
	
	private GerenciadorUsuarios() {
	}
	
	public void init() throws IOException {
		logger.debug("Carregando usuarios");
		
		JsonReader reader = new JsonReader(new FileReader("users.json"));
		
		usuarios = GsonUtil.gson().fromJson(reader, typeToken.getType());
		
		logger.debug("Usuarios carregados");
	}
	
	public boolean verificarSenha(String agencia, String conta, String senha) {
		String id = Usuario.gerarIdUsuario(agencia, conta);
		
		Usuario user = usuarios.get(id);
		
		if (user == null)
			return false;
		
		return user.getSenha().equals(senha);
	}
	
	public Usuario recuperarUsuario(String agencia, String conta) {
		return usuarios.get(Usuario.gerarIdUsuario(agencia, conta));
	}
	
	public static GerenciadorUsuarios instance() {
		synchronized (GerenciadorUsuarios.class) {
			if (instance == null) {
				instance = new GerenciadorUsuarios();
				
				try {
					instance.init();
				} catch (IOException e) {
					logger.debug("Erro ao carregar base de usuarios", e);
				}
			}
			
			return instance;
		}
	}

	public void salvar() throws IOException {
		try {
			writeLock.lock();
			logger.debug("Salvando dados de usuario!");
			
			JsonWriter writer = new JsonWriter(new FileWriter("users.json"));
			
			GsonUtil.gson().toJson(usuarios, typeToken.getType(), writer);
			
			writer.flush();
			writer.close();
		} finally {
			logger.debug("Dados salvos");
			writeLock.unlock();
		}
	}
}
