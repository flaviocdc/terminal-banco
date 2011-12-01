package br.ufrj.dcc.so.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Modifier;
import java.net.Socket;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

public class GsonUtil {

	public static Gson gson() {
		return new GsonBuilder().excludeFieldsWithModifiers(Modifier.STATIC, Modifier.TRANSIENT)
								.setPrettyPrinting()
								.create();
	}
	

	public static JsonReader criarJsonReader(Socket socket) throws IOException {
		return new JsonReader( new BufferedReader(new InputStreamReader(socket.getInputStream())));
	}

	public static JsonWriter criarJsonWriter(Socket socket) throws IOException {
		return new JsonWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())));
	}
}
