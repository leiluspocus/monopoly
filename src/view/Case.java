package view;

import jade.core.AID;

import java.io.Serializable;

public abstract class Case implements Serializable{
	private static final long serialVersionUID = 1L;
	protected int position; //Position de la case sur le plateau
	protected String nomCase;  // Nom de la case 
	protected AID joueurPresent; // AgentJoueur présent sur la case
	
	public String getNom() { return nomCase; }
	public int getPosition() { return position; }
	
	public Case() { 
		position = 0; 
		nomCase = "";
	}
	
	public Case(Case c) { 
		position = c.getPosition(); 
		nomCase = c.getNom();
	}
	
	public Case(int p, String n) {
		position = p; 
		nomCase = n;
	}
	public AID getJoueurPresent() {
		return joueurPresent;
	}
	public void setJoueurPresent(AID aid) {
		this.joueurPresent = aid;
	}
}
