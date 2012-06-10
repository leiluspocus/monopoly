package behaviour.player;

import jade.lang.acl.ACLMessage;
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

	@Override
	protected void decideAchatTerrain(CaseAchetable caseCourante) {
		if (caseCourante.getProprietaireCase() == null){
			if(agentJoueur.getCapitalJoueur() > caseCourante.getValeurTerrain()){
				if(1 == 2){
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
