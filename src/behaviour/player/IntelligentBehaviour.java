package behaviour.player;

import jade.lang.acl.ACLMessage;

import java.util.ArrayList;
import java.util.Vector;

import util.Constantes.Couleur;
import view.CaseAchetable;
import agent.AgentJoueur;

/**
 * Comportement visant à acheter des terrains en fonction des prix que lui apportent les terrains
 */
public class IntelligentBehaviour extends ActivePlayerBehaviour {
	
	private static final long serialVersionUID = 1L;
	
	private static final int SEUIL_LOYER_INTERESSANT = 3000;
	private AgentJoueur agentJoueur;
	

	public IntelligentBehaviour(AgentJoueur myAgent) {
		super(myAgent);
		this.agentJoueur = myAgent;
		this.agentJoueur.setProbaDemandeLoyer(90);
	}
	
	public boolean isInteressantLoyer(Vector<Integer> loyers) {
		if ( loyers.get(0) * 2 < loyers.get(1) ) {
			return true;
		}
		return loyers.get(0) > SEUIL_LOYER_INTERESSANT;
	}


	@Override
	protected void decideAchatTerrain(CaseAchetable caseCourante) {
		if (caseCourante.getProprietaireCase() == null){
			if(agentJoueur.getCapitalJoueur() > caseCourante.getValeurTerrain()){
				Vector<Integer> loyers = caseCourante.getLoyers();
				if( isInteressantLoyer(loyers) ){
					ACLMessage demandeAchat = new ACLMessage(ACLMessage.SUBSCRIBE);
					demandeAchat.setContent(caseCourante.getPosition() + "");
					demandeAchat.addReceiver(agentJoueur.getMonopoly());
					agentJoueur.send(demandeAchat);
					System.out.println(agentJoueur.getLocalName() + " demande a acheter " + caseCourante.getNom());
					agentJoueur.addProprieteToJoueur(caseCourante);
				}
			}
			else
				System.out.println("Not enough money to buy " + caseCourante.getNom());
		}
	}

	@Override
	protected void decideAchatMaison() {
		ArrayList<Couleur> cpp = agentJoueur.possedeLaCouleur();
		
		if(cpp.size() != 0){
			for(Couleur coul : cpp){
				int prix[] = agentJoueur.getPrixMaison(coul);
				int prixTotal = prix[0] * prix[1];
				
				if(agentJoueur.getCapitalJoueur() > prixTotal){ //Le joueur a t-il assez d'argent pour acheter les maisons ?
					ACLMessage demandeAchat = new ACLMessage(ACLMessage.PROXY);
					demandeAchat.setContent(coul + "#" + prixTotal);
					demandeAchat.addReceiver(agentJoueur.getMonopoly());
					agentJoueur.send(demandeAchat);
					System.out.println(agentJoueur.getLocalName() + " demande a acheter des maisons pour les cases " + coul);
				}
				else
					System.out.println("Not enough money to buy houses on" + coul);
			}
		}
		else
			System.out.println(agentJoueur.getLocalName() + " ne peut pas encore acheter de maisons");
	}
}
