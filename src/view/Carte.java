package view;

import java.io.Serializable;

public class Carte implements Serializable{
	private static final long serialVersionUID = 1L;
	private int valeur;
	private String msg;
	private int typeCarte; //CHANCE = 0 | COMMUNAUTE = 1
	
	public int getValeur() {return valeur;}
	public String getMsg() {return msg;}
	public int getTypeCarte() {return typeCarte;}

	public Carte(){
		valeur = 0;
		msg = "";
		typeCarte = 0;
	}
	
	public Carte(Carte c){
		valeur = c.getValeur();
		msg = c.getMsg();
		typeCarte = c.getTypeCarte();
	}
	
	public Carte(int v, String m, int tp){ 
		valeur = v; 
		msg = m; 
		typeCarte = tp; 
	}
	
	public boolean goToJail() {
		return msg.contains("Allez en prison");
	}
	
	public boolean canSetFreeFromJail() {
		return msg.contains("Vous etes libere de Prison.");
	}
	
	public String toString() {
		return "[" + typeCarte + "] - " + msg + " (" + valeur + ")\n";
	}
}
