package behaviour.player;

import jade.lang.acl.ACLMessage;

import java.util.Vector;

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
				}
			}
			else
				System.out.println("Not enough money to buy " + caseCourante.getNom());
		}
	}
}
