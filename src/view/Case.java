package view;

import jade.core.AID;

import java.io.Serializable;
import java.util.Vector;

public abstract class Case implements Serializable{
	private static final long serialVersionUID = 1L;
	protected int position; //Position de la case sur le plateau
	protected String nomCase;  // Nom de la case 
	protected Vector<AID> joueurPresents; // AgentJoueur présent sur la case
	
	public String getNom() { return nomCase; }
	public int getPosition() { return position; }
	
	public Vector<AID> getJoueurPresents() {return joueurPresents;}
	public void addJoueurPresent(AID aid) {this.joueurPresents.add(aid);}
	public void removeJoueurPresent(AID aid) {this.joueurPresents.removeElement(aid);}
	
	public Case() { 
		position = 0; 
		nomCase = "";
		joueurPresents = new Vector<AID>();
	}
	
	public Case(Case c) { 
		position = c.getPosition(); 
		nomCase = c.getNom();
		joueurPresents = c.getJoueurPresents();
	}
	
	public Case(int p, String n) {
		position = p; 
		nomCase = n;
		joueurPresents = new Vector<AID>();
	}
}