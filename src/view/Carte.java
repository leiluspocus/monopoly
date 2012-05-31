package view;

public class Carte {
	private int valeur;
	private String msg;
	private int typeCarte;
	
	public Carte(int v, String m, int tp) { valeur = v; msg = m; typeCarte = tp; }
	
	public String toString() {
		return "[" + typeCarte + "] - " + msg + " (" + valeur + ")\n";
	}
	
}
