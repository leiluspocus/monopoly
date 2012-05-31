package view;

import java.util.Vector;
 
import util.Constantes.Couleur;

public class CaseTerrain  extends CaseAchetable{
	private int valeurMaison; // Valeur de la maison
	private int nbMaisons; // Nombre de maisons pos�es sur la case 
	
	public CaseTerrain ( int pos, String nom, int valeurT, Vector<Integer> loyers, Couleur couleur, int valM) {
		super(pos, nom, valeurT, loyers, couleur);
		valeurMaison = valM;
		nbMaisons = 0;
	}
	
	public int getNbMaisons(){ return nbMaisons; }
}
