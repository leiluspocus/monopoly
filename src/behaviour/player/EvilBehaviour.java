package behaviour.player;

import jade.core.AID;
import jade.lang.acl.ACLMessage;

import java.util.Vector;

import util.Logger;
import view.CaseAchetable;
import agent.AgentJoueur;

/**
 * Comportement visant à bloquer les autres agents en visant les cases 
 * de couleur ayant déjà été achetés par des agents
 */
public class EvilBehaviour extends ActivePlayerBehaviour {
	private static final long serialVersionUID = 1L;
	private AgentJoueur agentJoueur;

	public EvilBehaviour(AgentJoueur agentJoueur) {
		super(agentJoueur);
		this.agentJoueur = agentJoueur;
		this.agentJoueur.setProbaDemandeLoyer(80);
	}

	public boolean canBlockPeople(CaseAchetable c) {
		Vector<AID> potentialAgents = c.getProprietairesPotentiels();
		//System.err.println("Proprios de la case: " + potentialAgents);
		if ( potentialAgents != null ) {
			if ( potentialAgents.size() == 0 ) {
				return false;
			}
			else {
				return true;
			}
		} 
		return false;
	}
	
	@Override
	protected void decideAchatTerrain(CaseAchetable caseCourante) {
		if (caseCourante.getProprietaireCase() == null){
			if(agentJoueur.getCapitalJoueur() > caseCourante.getValeurTerrain()){
				if( canBlockPeople(caseCourante)){
					ACLMessage demandeAchat = new ACLMessage(ACLMessage.SUBSCRIBE);
					demandeAchat.setContent(caseCourante.getPosition() + "");
					demandeAchat.addReceiver(agentJoueur.getMonopoly());
					agentJoueur.send(demandeAchat);
					System.out.println(agentJoueur.getLocalName() + " demande a acheter " + caseCourante.getNom() );
					agentJoueur.addProprieteToJoueur(caseCourante);
				}
			}
			else
				System.out.println(agentJoueur.getLocalName() + " n'a pas assez d'argent pour acheter " + caseCourante.getNom());
		}
	}

	@Override
	protected void decideAchatMaison() {
		
	}
}
