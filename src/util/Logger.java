package util;

public class Logger {

	// Info classique li�e � l'action d'un joueur
	public static void info(String msg) {
		System.out.println(msg);
	}
	
	// Erreur li�e � l'ex�cution des agents
	public static void err(String msg) {
		System.err.println(msg);
	}
}
