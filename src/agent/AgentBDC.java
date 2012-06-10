package agent;

import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;

import java.util.List;
import java.util.Vector;

import util.Constantes;
import util.Constantes.ActionSpeciale;
import util.Constantes.Couleur;
import view.Carte;
import view.Case;
import view.CaseAchetable;
import view.CaseSpeciale;
import view.CaseTerrain;
import behaviour.BDCBehaviour;

import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSetFormatter;

import database.CaseModel;

public class AgentBDC extends Agent{
	private static final long serialVersionUID = 1L;
	private CaseModel model;
	
	protected void setup() {
		model = new CaseModel();
		register();
		addBehaviour(new BDCBehaviour(this));
	}
	
	private void register() {
		DFAgentDescription agentDescription = new DFAgentDescription();
        agentDescription.setName(getAID());
        ServiceDescription serviceDescription  = new ServiceDescription();
        serviceDescription.setType("knowledge");
        serviceDescription.setName(getLocalName());
        agentDescription.addServices(serviceDescription);
        try {
            DFService.register(this, agentDescription);
        } 
        catch (FIPAException e) { System.out.println("Enregistrement de l'agent BDC au service echoue - Cause : " + e); }
	}
	
	public Vector<Carte> buildCartesChances() {
		Vector<Carte> cartes = new Vector<Carte>();
		List<QuerySolution> casesChances = ResultSetFormatter.toList(model.lookForCartesChance());
		
		for ( QuerySolution carte : casesChances ) {
			String msg = carte.getLiteral(Constantes.texte).getString();
			int val = carte.getLiteral(Constantes.valeur).getInt();
			int dep = carte.getLiteral(Constantes.deplacement).getInt();
			cartes.add(new Carte(val, msg, Constantes.CHANCE, dep)); 
		}
		return cartes;
	}

	public Vector<Carte> buildCartesCommunaute() {
		Vector<Carte> cartes = new Vector<Carte>();
		List<QuerySolution> casesChances = ResultSetFormatter.toList(model.lookForCartesCommunaute());
		
		for ( QuerySolution carte : casesChances ) {
			String msg = carte.getLiteral(Constantes.texte).getString();
			int val = carte.getLiteral(Constantes.valeur).getInt();
			int dep = carte.getLiteral(Constantes.deplacement).getInt();
			cartes.add(new Carte(val, msg, Constantes.COMMUNAUTE, dep));
		}
		return cartes;
		
	}
	public Vector<Case> buildCasesTerrains() {
		Vector<Case> plateau = new Vector<Case>();
		List<QuerySolution> casesTerrains = ResultSetFormatter.toList(model.lookForTerrains());
		
		for ( QuerySolution carte : casesTerrains ) {
			int pos = carte.getLiteral(Constantes.position).getInt();
			String nom = carte.getLiteral(Constantes.nom).getString();
			int valeur = carte.getLiteral(Constantes.valeur).getInt();
			int valeurMaison = carte.getLiteral(Constantes.valeurMaison).getInt();
			Couleur couleur = Couleur.valueOf(carte.getLiteral(Constantes.couleur).getString().toUpperCase());
			Vector<Integer> loyers = new Vector<Integer>();
			loyers.add(carte.getLiteral(Constantes.loyerNu).getInt());
			loyers.add(carte.getLiteral(Constantes.loyer1).getInt());
			loyers.add(carte.getLiteral(Constantes.loyer2).getInt());
			loyers.add(carte.getLiteral(Constantes.loyer3).getInt());
			loyers.add(carte.getLiteral(Constantes.loyer4).getInt());
			loyers.add(carte.getLiteral(Constantes.loyerHotel).getInt());
			plateau.add(new CaseTerrain(pos, nom, valeur, loyers, couleur, valeurMaison));
		}
		return plateau;
	}

	public Vector<Case> buildCasesAchetables() {
		Vector<Case> plateau = new Vector<Case>();
		List<QuerySolution> casesTerrains = ResultSetFormatter.toList(model.lookForCasesAchetables());
		
		for ( QuerySolution carte : casesTerrains ) {
			int pos = carte.getLiteral(Constantes.position).getInt();
			String nom = carte.getLiteral(Constantes.nom).getString();
			int valeur = carte.getLiteral(Constantes.valeur).getInt(); 
			Couleur couleur = Couleur.valueOf(carte.getLiteral(Constantes.couleur).getString().toUpperCase());
			Vector<Integer> loyers = new Vector<Integer>();
			loyers.add(carte.getLiteral(Constantes.loyerNu).getInt());
			loyers.add(carte.getLiteral(Constantes.loyer1).getInt());
			loyers.add(carte.getLiteral(Constantes.loyer2).getInt());
			loyers.add(carte.getLiteral(Constantes.loyer3).getInt()); 
			plateau.add(new CaseAchetable(pos, nom, valeur, loyers, couleur));
		}
		return plateau;
	}

	public Vector<Case> buildCasesSpeciales() {
		Vector<Case> plateau = new Vector<Case>();
		List<QuerySolution> casesTerrains = ResultSetFormatter.toList(model.lookForCasesSpeciales());
		
		for ( QuerySolution carte : casesTerrains ) {
			int pos = carte.getLiteral(Constantes.position).getInt();
			String nom = carte.getLiteral(Constantes.nom).getString();
			ActionSpeciale type = ActionSpeciale.valueOf(carte.getLiteral(Constantes.type).getString().toUpperCase());
			plateau.add(new CaseSpeciale(pos, nom, type));
		}
		return plateau;
	}
}
