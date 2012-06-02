package util;


public class Constantes {
	public enum Couleur { ROUGE, JAUNE, VERT, BLEU_FONCE , MAGENTA, BLEU_CIEL, VIOLET, ORANGE, NOIR, BLANC };
	public enum ActionSpeciale { DEPART, PRISON, CAISSECOMMUNAUTE, CHANCE, PARCGRATUIT, ALLERENPRISON, IMPOTS };
	public enum Pion { Cheval, Canon, Voiture, Bateau, Chapeau, Brouette, Chaussure, Fer };
	public static final Pion[] lesPions = {Pion.Cheval, Pion.Canon, Pion.Voiture, Pion.Bateau, Pion.Chapeau, Pion.Brouette, Pion.Chaussure, Pion.Fer};
	public static final int NB_JOUEURS = 6;
	public static final int CHANCE = 0;
	public static final int COMMUNAUTE = 1;

	/** Constantes utilisees pour les requetes **/
	public static final String position = "?position";
	public static final String nom = "?nom";
	public static final String type = "?type";
	public static final String valeur = "?valeur";
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
	public static final long DUREE_ANIMATION = 3000;
	
	public static final int CASE_DEPART = 0;
	public static final int CASE_FIN = 39;
	

}