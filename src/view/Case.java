package view;

import java.io.Serializable;

public abstract class Case implements Serializable{
	private static final long serialVersionUID = 1L;
	protected int position; //Position de la case sur le plateau
	protected String nomCase;  // Nom de la case
	protected String proprietaireCase; // Agent propriétaire de la case (nom du joueur, défini dans AgentJoueur)
	
	public String getNom() { return nomCase; }
	public String getProprietaireCase() { return proprietaireCase; }
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
}
