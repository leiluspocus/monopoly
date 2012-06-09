package view;

import jade.core.AID;

import java.util.Vector;

import util.Constantes.Couleur;

public class CaseAchetable extends Case{
	private static final long serialVersionUID = 1L;
	
	protected int valeurTerrain; // Valeur initiale du terrain
	protected Vector<Integer> loyers;  // Valeurs des loyers (selon le nombre de terrains poss�d�s)
	protected AID proprietaireCase; // Propri�taire de la case
	protected Couleur couleur; // Couleur � laquelle appartient
	
	public CaseAchetable(int pos, String nom, int valT, Vector<Integer> loy, Couleur c) {
		super(pos, nom);
		valeurTerrain = valT;
		loyers = loy;
		couleur = c;
	}
	
	public String toString(){
		return "Case : Position -> " + position + " : Nom -> " + nomCase + " : Proprietaire -> " + proprietaireCase;
	}

	public Vector<Integer> getLoyers() {
		return loyers;
	}

	public AID getProprietaireCase() {
		return proprietaireCase;
	}

	public void setProprietaireCase(AID proprietaireCase) {
		this.proprietaireCase = proprietaireCase;
	}
}
