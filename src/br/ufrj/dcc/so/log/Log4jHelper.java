package br.ufrj.dcc.so.log;

import org.apache.log4j.BasicConfigurator;

public class Log4jHelper {

	public static void initLog4j() {
	     BasicConfigurator.configure();
	}
	
}