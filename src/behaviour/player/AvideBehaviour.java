package behaviour.player;

import jade.lang.acl.ACLMessage;

import java.io.IOException;

import view.CaseAchetable;
import agent.AgentJoueur;


/**
 * Comportement visant à acheter un terrain dès qu'il est disponible
 */
public class AvideBehaviour extends ActivePlayerBehaviour {
	private static final long serialVersionUID = 1L;
	private AgentJoueur agentJoueur;

	public AvideBehaviour(AgentJoueur agentJoueur) {
		super(agentJoueur);
		this.agentJoueur = agentJoueur;
		this.agentJoueur.setProbaDemandeLoyer(95);
	}

	@Override
	protected void decideAchatTerrain(CaseAchetable caseCourante) {
		if (caseCourante.getProprietaireCase() != null){
			if(agentJoueur.getCapitalJoueur() > caseCourante.getValeurTerrain()){
				ACLMessage demandeAchat = new ACLMessage(ACLMessage.SUBSCRIBE);
				try {
					demandeAchat.setContentObject(caseCourante);
				} catch (IOException e) {e.printStackTrace();}
			}
			else
				System.out.println("Not enough money to buy " + caseCourante.getNom());
		}
	}
}
