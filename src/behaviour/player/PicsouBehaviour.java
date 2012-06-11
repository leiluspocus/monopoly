package behaviour.player;

import jade.lang.acl.ACLMessage;

import java.util.ArrayList;

import util.Constantes.Couleur;
import util.Logger;
import view.CaseAchetable;
import agent.AgentJoueur;

/**
 * Comportement tendant à économiser au maximum avant d'acheter un terrain
 */

public class PicsouBehaviour extends ActivePlayerBehaviour {
	
	private static final long serialVersionUID = 1L;
	private static final int SEUIL_ACHAT = 100000;
	private AgentJoueur agentJoueur;

	public PicsouBehaviour(AgentJoueur myAgent) {
		super(myAgent);
		this.agentJoueur = myAgent;
		this.agentJoueur.setProbaDemandeLoyer(100);
	}

	@Override
	protected void decideAchatTerrain(CaseAchetable caseCourante) {
		if (caseCourante.getProprietaireCase() == null){
			if(agentJoueur.getCapitalJoueur() > SEUIL_ACHAT){
				ACLMessage demandeAchat = new ACLMessage(ACLMessage.SUBSCRIBE);
				demandeAchat.setContent(caseCourante.getPosition() + "");
				demandeAchat.addReceiver(agentJoueur.getMonopoly());
				agentJoueur.send(demandeAchat);
				Logger.info(agentJoueur.getLocalName() + " demande a acheter " + caseCourante.getNom());
				agentJoueur.addProprieteToJoueur(caseCourante);
			}
			else
				Logger.info(agentJoueur.getLocalName() + " n'a pas assez d'argent pour acheter " + caseCourante.getNom());
		}
	}

	@Override
	protected void decideAchatMaison() {
		ArrayList<Couleur> cpp = agentJoueur.possedeLaCouleur();
		
		if(cpp.size() != 0){
			for(Couleur coul : cpp){
				int prix[] = agentJoueur.getPrixMaison(coul);
				int prixTotal = prix[0] * prix[1];
				
				if(agentJoueur.getCapitalJoueur() > SEUIL_ACHAT){
					ACLMessage demandeAchat = new ACLMessage(ACLMessage.PROXY);
					demandeAchat.setContent(coul + "#" + prixTotal);
					demandeAchat.addReceiver(agentJoueur.getMonopoly());
					agentJoueur.send(demandeAchat);
					Logger.info(agentJoueur.getLocalName() + " demande a acheter des maisons pour les cases " + coul);
				}
				else
					Logger.info(agentJoueur.getLocalName() + " n'a pas assez d'argent pour acheter " + coul);
			}
		}
		else
			Logger.info(agentJoueur.getLocalName() + " ne peut pas encore acheter de maisons");
		
	}
}
