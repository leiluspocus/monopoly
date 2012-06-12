package view;

import java.util.Vector;
 
import util.Constantes;
import util.Constantes.Couleur;

public class CaseTerrain  extends CaseAchetable{
	private static final long serialVersionUID = 1L;
	
	private int valeurMaison; // Valeur de la maison
	private int nbMaisons; // Nombre de maisons posées sur la case 
	
	public CaseTerrain ( int pos, String nom, int valeurT, Vector<Integer> loyers, Couleur couleur, int valM) {
		super(pos, nom, valeurT, loyers, couleur);
		valeurMaison = valM;
		nbMaisons = 0;
	}
	
	public int getNbMaisons(){ return nbMaisons; }
	public void setNbMaisons(int nb){ nbMaisons = nb; }
	public int getValeurMaison() { return valeurMaison; }
	public void ajouterMaison() { 
		if(nbMaisons < Constantes.NB_MAX_MAISONS_PAR_CASE)
			nbMaisons++; 
		else
			System.err.println("Il y a deja trop de maison sur la case " + this.nomCase);
	}
}
