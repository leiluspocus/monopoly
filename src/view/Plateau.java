package view;

import jade.core.AID;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.Vector;

import util.Constantes.ActionSpeciale;
import util.Constantes.Couleur;
import util.Constantes.Pion;
import util.Helper;
import util.Logger;

public class Plateau {
	private Vector<Case> plateau; // Ensemble des cases du plateau
	private Vector<Carte> cartes; // Ensemble des cartes (chance et communaute)
	private HashMap<Pion, Integer> positionPions;
	private Monopoly m;
	//private int tmp = 1;
	
	private int numCourantCartesChance;
	private int numCourantCartesCommunaute;
	
	private ArrayList<Integer> listCasesChance;
	private ArrayList<Integer> listCasesCommunaute;

	public Plateau(Vector<Case> vcas, Vector<Carte> vcar){
		Random intGenerator = new Random();
		new Logger();
		
		plateau = vcas;
		cartes = vcar;
		numCourantCartesChance = intGenerator.nextInt(15);
		numCourantCartesCommunaute = intGenerator.nextInt(15) +16;
		
		listCasesChance = new ArrayList<Integer>();
		listCasesCommunaute = new ArrayList<Integer>();
		
		for(Case c : plateau){
			if(c instanceof CaseSpeciale){
				if(((CaseSpeciale) c).getActionSpeciale() == ActionSpeciale.CHANCE){
					listCasesChance.add(c.getPosition());
				}
					
				if(((CaseSpeciale) c).getActionSpeciale() == ActionSpeciale.CAISSECOMMUNAUTE){
					listCasesCommunaute.add(c.getPosition());
				}
			}	
		}
	}
	
	public void setFrame(Monopoly m){this.m = m;}
	public void redrawFrame(){m.redraw();}

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
	
	public Carte tirageChance(){
		Carte c = cartes.get(numCourantCartesChance);
		
		if(numCourantCartesChance == 15)
			numCourantCartesChance = 0;
		else
			numCourantCartesChance++;
		
		return c;
	}
	
	public Carte tirageCommunaute(){
		Carte c = cartes.get(numCourantCartesCommunaute);
		
		if(numCourantCartesCommunaute == 31)
			numCourantCartesCommunaute = 0;
		else
			numCourantCartesCommunaute++;
		
		return c;
	}

	
	public boolean allCasesHaveBeenSold(){
		/*if(tmp == 1){
			tmp = 2;
			return false;
		}
		return true;*/
		
		
		for (Case c : plateau)
			if(c instanceof CaseAchetable)
				if(((CaseAchetable)c).getProprietaireCase() == null)
					return false;
				
		return true;
	}
	
	public boolean isCaseChance(int position) {
		Integer i = new Integer(position);
		return listCasesChance.contains(i);
	}

	public boolean isCaseCommunaute(int position) {
		Integer i = new Integer(position);
		return listCasesCommunaute.contains(i);
	}

	public Case getCaseAtPosition(int positionCase){
		for(Case c : plateau){
			if(c.getPosition() == positionCase)
				return c;
		}
		return null;
	}
	
	// Retourne le nombre de terrains d'une couleur donnee que possede un proprietaire
	public int getNbTerrains(Couleur c, AID proprietaire) {
		int nb=0;
		for ( Case terrain : plateau ) {
			if ( terrain instanceof CaseAchetable ) {   
				CaseAchetable cx = (CaseAchetable) terrain;
				if ( cx.getCouleur() == c && Helper.compareAID( cx.getProprietaireCase() , proprietaire)) {
					nb++;
				}
			}
		}
		return nb;
	}
	
	public Vector<AID> getProprietaires(Couleur c) {
		Vector<AID> res = new Vector<AID>();
		for ( Case terrain : plateau ) {
			if ( terrain instanceof CaseAchetable ) {
				AID tmp = ((CaseAchetable) terrain).getProprietaireCase();
				if ( ((CaseAchetable) terrain).getCouleur() == c && tmp != null ) {
					if ( ! res.contains(tmp))
						res.add(tmp);
				}
			}
		}
		return res;
	}
	
	public CaseAchetable nouveauProprietaire(int positionCaseAchetee, AID proprietaire) {
		CaseAchetable c = (CaseAchetable) getCaseAtPosition(positionCaseAchetee);
		c.setProprietaireCase(proprietaire);
		c.setProprietairesPotentiels(getProprietaires(c.getCouleur()));
		return c;
	}

	/**
	 * A l'achat d'une propriete, les autres cases appartenant a la meme propriete notifient
	 * le vecteur des "proprietaires potentiels"
	 * @param prop le vecteur de proprietaires a jour
	 */
	public void setProprietairesPotentielsPourLesCouleurs(Vector<AID> prop) {
		for ( Case terrain : plateau ) {
			if ( terrain instanceof CaseAchetable ) {
				((CaseAchetable) terrain).setProprietairesPotentiels(prop);
			}
		}
	}

	/**
	 * Ajoute des maisons sur les cases de couleur c
	 * @param c : La couleur
	 */
	public void addHouses(Couleur couleurRecue, AID proprietaireRecue) {
		for(Case c : plateau){
			if(c instanceof CaseTerrain){
				Couleur couleurCourante = ((CaseTerrain) c).getCouleur();
				String proprietaireCourant;
				if(((CaseTerrain) c).getProprietaireCase() == null)
					proprietaireCourant = "";
				else
					proprietaireCourant = ((CaseTerrain) c).getProprietaireCase().getLocalName();
				
				if(couleurCourante == couleurRecue && proprietaireCourant.equals(proprietaireRecue.getLocalName())){
					((CaseTerrain) c).ajouterMaison();
				}
			}
		}
	}
	
	public void liquideJoueur(AID joueurEnFaillite){
		for(Case c : plateau){
			if(c instanceof CaseAchetable){
				CaseAchetable ca = (CaseAchetable)c;
				String proprietaireCourant;
				if( ca.getProprietaireCase() == null)
					proprietaireCourant = "";
				else
					proprietaireCourant = ca.getProprietaireCase().getLocalName();
				
				if(proprietaireCourant.equals(joueurEnFaillite.getLocalName())){
					ca.setProprietaireCase(null); 
					if(c instanceof CaseTerrain)
						((CaseTerrain)c).setNbMaisons(0);
				}
				ca.setNbTerrainsPossedes(0);
				Vector<AID> propPotentiels = ca.getProprietairesPotentiels();
				if ( propPotentiels.contains(joueurEnFaillite)) {
					propPotentiels.remove(joueurEnFaillite);
				}
			}
		}
		System.out.println("Toutes les proprietes de " + joueurEnFaillite.getLocalName() + "(s'il en possedait) on ete saisies par la Banque");
	}
 
	public void setNbTerrainsPossedes(int nb, Couleur couleur, AID proprietaire) {
		for(Case c : plateau){
			if(c instanceof CaseAchetable){ 
				CaseAchetable obj = (CaseAchetable) c;
				if(Helper.compareAID( obj.getProprietaireCase() , proprietaire)  && obj.getCouleur() == couleur ) {
					obj.setNbTerrainsPossedes(nb); 
				}
			}
		}
	}
}
