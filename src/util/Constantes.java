package util;

import java.io.Serializable;


public class Constantes implements Serializable { 
	private static final long serialVersionUID = 1L;
	
	public enum Couleur { ROUGE, JAUNE, VERT, BLEU_FONCE , MAGENTA, BLEU_CIEL, VIOLET, ORANGE, NOIR, BLANC };
	public enum ActionSpeciale { DEPART, VISITERPRISON, CAISSECOMMUNAUTE, CHANCE, PARCGRATUIT, ALLERENPRISON, IMPOTS };
	public enum Pion { Cheval, Canon, Voiture, Bateau, Chapeau, Brouette, Chaussure, Fer };
	public static final Pion[] lesPions = {Pion.Cheval, Pion.Canon, Pion.Voiture, Pion.Bateau, Pion.Chapeau, Pion.Brouette, Pion.Chaussure, Pion.Fer};
	public static final int NB_JOUEURS = 6;
	public static final int NB_AGENT_SERVICE = 1; //Banque, BDC, Prison, Seed
	public static final int CHANCE = 0;
	public static final int COMMUNAUTE = 1;

	/** Constantes utilisees pour les requetes **/
	public static final String position = "?position";
	public static final String nom = "?nom";
	public static final String type = "?type";
	public static final String valeur = "?valeur";
	public static final String deplacement = "?deplacement";
	public static final String texte = "?texte";
	public static final String loyerNu = "?loyerNu";
	public static final String loyer1 = "?loyer1";
	public static final String loyer2 = "?loyer2";
	public static final String loyer3 = "?loyer3";
	public static final String loyer4 = "?loyer4";
	public static final String loyerHotel = "?loyerHotel";
	public static final String valeurMaison = "?valeurMaison";
	public static final String couleur = "?couleur";

	public static final int CAPITAL_DEPART = 150000;
	public static final long DUREE_ANIMATION = 2000;
	
	/* Valeurs de cases utilisees mais definies dans l'ontologie*/
	public static final int CASE_DEPART = 0;
	public static final int CASE_FIN = 39;
	public static final int CASE_GOTOPRISON = 30;
	public static final int CASE_PRISON = 10;
	public static final int CASE_TAXE = 38;
	public static final int CASE_IMPOTS = 4;
	
	
	public static final String PATH_IMG = "../monopoly/res/";
}