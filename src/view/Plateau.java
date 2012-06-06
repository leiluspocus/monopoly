package view;

import java.util.Vector;

public class Plateau {
	private Vector<Case> plateau; // Ensemble des cases du plateau
	private Vector<Carte> cartes; // Ensemble des cartes (chance et communaute)
	
	public Plateau(Vector<Case> vcas, Vector<Carte> vcar){
		plateau = vcas;
		cartes = vcar;
		
		//System.out.println(plateau);
	}

	public Vector<Case> getPlateau(){ return this.plateau; }
	public Vector<Carte> getCartes(){ return this.cartes; }
}
