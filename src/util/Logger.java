package util;

public class Logger {

	// Info classique liée à l'action d'un joueur
	public static void info(String msg) {
		System.out.println(msg);
	}
	
	// Erreur liée à l'exécution des agents
	public static void err(String msg) {
		System.err.println(msg);
	}
}
