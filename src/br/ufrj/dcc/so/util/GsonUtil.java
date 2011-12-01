package br.ufrj.dcc.so.util;

import java.lang.reflect.Modifier;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class GsonUtil {

	public static Gson gson() {
		return new GsonBuilder().excludeFieldsWithModifiers(Modifier.STATIC, Modifier.TRANSIENT)
								.setPrettyPrinting()
								.create();
	}
	
}
