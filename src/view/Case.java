package view;

import jade.core.AID;

import java.io.Serializable;

public abstract class Case implements Serializable{
	private static final long serialVersionUID = 1L;
	protected int position; //Position de la case sur le plateau
	protected String nomCase;  // Nom de la case 
	protected AID joueurQuiVientdArriver; // AgentJoueur présent sur la case
	
	public String getNom() { return nomCase; }
	public int getPosition() { return position; }
	
	public AID getJoueurQuiVientdArriver() {return joueurQuiVientdArriver;}
	public void setJoueurQuiVientdArriver(AID aid) {this.joueurQuiVientdArriver = aid;}
	
	public Case() { 
		position = 0; 
		nomCase = "";
		joueurQuiVientdArriver = null;
	}
	
	public Case(Case c) { 
		position = c.getPosition(); 
		nomCase = c.getNom();
		joueurQuiVientdArriver = c.getJoueurQuiVientdArriver();
	}
	
	public Case(int p, String n) {
		position = p; 
		nomCase = n;
		joueurQuiVientdArriver = null;
	}
}