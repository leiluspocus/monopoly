package view;

import java.util.HashMap;
import java.util.Vector;

import util.Constantes.Pion;

public class Plateau {
	private Vector<Case> plateau; // Ensemble des cases du plateau
	private Vector<Carte> cartes; // Ensemble des cartes (chance et communaute)
	private HashMap<Pion, Integer> positionPions;
	
	public Plateau(Vector<Case> vcas, Vector<Carte> vcar){
		plateau = vcas;
		cartes = vcar;
	}

	public Vector<Case> getPlateau(){ return this.plateau; }
	public Vector<Carte> getCartes(){ return this.cartes; }
	
	public HashMap<Pion, Integer> getPositionJoueurs() {return positionPions;}
	public void setPositionJoueurs(HashMap<Pion, Integer> positionPions) {this.positionPions = positionPions;}
	
	public Case getCase(int pos) {
		for ( Case o : plateau ) {
			if ( o.getPosition() == pos ) {
				return o;
			}
		}
		return null;
	}
}
