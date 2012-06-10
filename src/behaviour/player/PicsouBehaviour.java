package behaviour.player;

import jade.lang.acl.ACLMessage;
import view.CaseAchetable;
import agent.AgentJoueur;

/**
 * Comportement tendant à économiser au maximum avant d'acheter un terrain
 */

public class PicsouBehaviour extends ActivePlayerBehaviour {
	
	private static final long serialVersionUID = 1L;
	private static final int SEUIL_ACHAT = 80000;
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
				System.out.println(agentJoueur.getLocalName() + " demande a acheter " + caseCourante.getNom());
				agentJoueur.addProprieteToJoueur(caseCourante);
			}
			else
				System.out.println("Not enough money to buy " + caseCourante.getNom());
		}
	}

	@Override
	protected void decideAchatMaison() {
		// TODO Auto-generated method stub
		
	}
}
