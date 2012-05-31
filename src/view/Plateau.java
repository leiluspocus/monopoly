package view;

import java.util.List;
import java.util.Vector;

import util.Constantes;
import util.Constantes.ActionSpeciale;
import util.Constantes.Couleur;
import util.Convert;

import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSetFormatter;

import database.CaseModel;

public class Plateau {
	private Vector<Case> plateau; // Ensemble des cases du plateau
	private CaseModel model;
	private Vector<Carte> cartes; // Ensemble des cartes (chance et communaute)
	
	public Plateau(){
		plateau = new Vector<Case>();
		model = new CaseModel();
		cartes = new Vector<Carte>();
		buildCasesSpeciales();
		buildCasesAchetables();
		buildCasesTerrains();
		System.out.println(plateau);
		buildCartesChances();
		buildCartesCommunaute();
	}

	public Vector<Case> getPlateau(){ return this.plateau; }
	
	private void buildCartesChances() {
		List<QuerySolution> casesChances = ResultSetFormatter.toList(model.lookForCartesChance());
		for ( QuerySolution carte : casesChances ) {
			String msg = carte.getLiteral(Constantes.texte).getString();
			int val = carte.getLiteral(Constantes.valeur).getInt();
			cartes.add(new Carte(val, msg, Constantes.CHANCE));
		}  
	}

	private void buildCartesCommunaute() {
		List<QuerySolution> casesChances = ResultSetFormatter.toList(model.lookForCartesCommunaute());
		for ( QuerySolution carte : casesChances ) {
			String msg = carte.getLiteral(Constantes.texte).getString();
			int val = carte.getLiteral(Constantes.valeur).getInt();
			cartes.add(new Carte(val, msg, Constantes.COMMUNAUTE));
		} 
		
	}
	private void buildCasesTerrains() {
		List<QuerySolution> casesTerrains = ResultSetFormatter.toList(model.lookForTerrains());
		for ( QuerySolution carte : casesTerrains ) {
			int pos = carte.getLiteral(Constantes.position).getInt();
			String nom = carte.getLiteral(Constantes.nom).getString();
			int valeur = carte.getLiteral(Constantes.valeur).getInt();
			int valeurMaison = carte.getLiteral(Constantes.valeurMaison).getInt();
			Couleur couleur = Convert.stringToCouleur(carte.getLiteral(Constantes.couleur).getString());
			Vector<Integer> loyers = new Vector<Integer>();
			loyers.add(carte.getLiteral(Constantes.loyerNu).getInt());
			loyers.add(carte.getLiteral(Constantes.loyer1).getInt());
			loyers.add(carte.getLiteral(Constantes.loyer2).getInt());
			loyers.add(carte.getLiteral(Constantes.loyer3).getInt());
			loyers.add(carte.getLiteral(Constantes.loyer4).getInt());
			loyers.add(carte.getLiteral(Constantes.loyerHotel).getInt());
			plateau.add(new CaseTerrain(pos, nom, valeur, loyers, couleur, valeurMaison));
		} 
	}

	private void buildCasesAchetables() {
		List<QuerySolution> casesTerrains = ResultSetFormatter.toList(model.lookForCasesAchetables());
		for ( QuerySolution carte : casesTerrains ) {
			int pos = carte.getLiteral(Constantes.position).getInt();
			String nom = carte.getLiteral(Constantes.nom).getString();
			int valeur = carte.getLiteral(Constantes.valeur).getInt(); 
			Couleur couleur = Convert.stringToCouleur(carte.getLiteral(Constantes.couleur).getString());
			Vector<Integer> loyers = new Vector<Integer>();
			loyers.add(carte.getLiteral(Constantes.loyerNu).getInt());
			loyers.add(carte.getLiteral(Constantes.loyer1).getInt());
			loyers.add(carte.getLiteral(Constantes.loyer2).getInt());
			loyers.add(carte.getLiteral(Constantes.loyer3).getInt()); 
			plateau.add(new CaseAchetable(pos, nom, valeur, loyers, couleur));
		} 
		
	}

	private void buildCasesSpeciales() {
		List<QuerySolution> casesTerrains = ResultSetFormatter.toList(model.lookForCasesSpeciales());
		for ( QuerySolution carte : casesTerrains ) {
			int pos = carte.getLiteral(Constantes.position).getInt();
			String nom = carte.getLiteral(Constantes.nom).getString();
			ActionSpeciale type = Convert.stringToAction(carte.getLiteral(Constantes.type).getString());
			plateau.add(new CaseSpeciale(pos, nom, type));
		} 
	}
	
}
