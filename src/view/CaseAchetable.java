package view;

import jade.core.AID;

import java.util.Vector;

import util.Constantes.Couleur;

public class CaseAchetable extends Case{
	private static final long serialVersionUID = 1L;
	
	protected int valeurTerrain; // Valeur du terrain
	protected Vector<Integer> loyers;  // Valeurs des loyers (selon le nombre de terrains poss�d�s)
	protected AID proprietaireCase; // Propri�taire de la case
	protected Couleur couleur; // Couleur � laquelle appartient
	protected int nbTerrainsPossedes;
	protected Vector<AID> proprietairesPotentiels;
	
	public CaseAchetable(int pos, String nom, int valT, Vector<Integer> loy, Couleur c) {
		super(pos, nom);
		valeurTerrain = valT;
		loyers = loy;
		couleur = c;
		proprietairesPotentiels = new Vector<AID>();
	}
	
	public CaseAchetable(CaseAchetable c) {
		super(c);
		valeurTerrain = c.valeurTerrain;
		loyers = c.loyers;
		couleur = c.couleur;
	}
	
	public String toString(){
		if(proprietaireCase == null)
			return "Case : Position -> " + position + " : Nom -> " + nomCase + " : Proprietaire -> Aucun";
		else
			return "Case : Position -> " + position + " : Nom -> " + nomCase + " : Proprietaire -> " + proprietaireCase.getLocalName();
	}

	public int getValeurTerrain() {return valeurTerrain;}
	public Vector<Integer> getLoyers() {return loyers;}
	public AID getProprietaireCase() {return proprietaireCase;}
	public void setProprietaireCase(AID proprietaireCase) {this.proprietaireCase = proprietaireCase;}
	public Couleur getCouleur() {return couleur;}

	/*
	 *Le tableau loyers est construit de la maniere suivante :
	 * 	   -> Terrain nu: Indice 0
	 *     -> Deux terrains: Indice 1 
	 *     and so on
	 */
	public int computeLoyer() {
		if ( proprietaireCase != null ) {
			return loyers.get(nbTerrainsPossedes - 1);
		}
		return 0;
	}

	public void setNbTerrainsPossedes(int nb) {  
		nbTerrainsPossedes = nb;
	}
	
	public void setProprietairesPotentiels(Vector<AID> p) { 
		proprietairesPotentiels = p;
	}
	
	public Vector<AID> getProprietairesPotentiels() { return proprietairesPotentiels; }
}
