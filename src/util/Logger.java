package util;

public class Logger {

	// Info classique liee l'action d'un joueur
	public static void info(String msg) {
		System.out.println(msg);
	}
	
	// Erreur liee l'execution des agents
	public static void err(String msg) {
		System.err.println(msg);
	}
}
