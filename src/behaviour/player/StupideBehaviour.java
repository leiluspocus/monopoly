package behaviour.player;

import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;
import util.Logger;
import view.Case;
import agent.AgentJoueur;

/**
 * Behaviour déclenché après tirage de dés
 * N'acheter aucun terrain
 */
public class StupideBehaviour extends OneShotBehaviour {
	
	private static final long serialVersionUID = 1L;

	
	
	public StupideBehaviour(Agent agentJoueur) {
		super(agentJoueur);
	}

	@Override
	public void action() {
		
		ACLMessage msgReceived = myAgent.blockingReceive();

		if (msgReceived != null){
			switch (msgReceived.getPerformative()) {
				/*
				 * Indique au joueur sur quelle case il se trouve après le déplacement effectué (dû au jeté de dés)
				 */
				case ACLMessage.INFORM_REF:
					try {
						((AgentJoueur)myAgent).setCaseCourante((Case) msgReceived.getContentObject());
					} catch (UnreadableException e) {e.printStackTrace();}
					
					System.out.println(((AgentJoueur)myAgent).getCaseCourante());
					
				break;
				/*
				 * Message receptionnant l'argent d'un joueur ou de la banque
				 */
				case ACLMessage.AGREE:
					int sommeRecue = Integer.parseInt(msgReceived.getContent().trim());
					((AgentJoueur)myAgent).setCapitalJoueur(((AgentJoueur)myAgent).getCapitalJoueur()+sommeRecue);
					break;
					
				default: Logger.err("Message non géré par le behaviour Stupide : "+msgReceived);
					break;
			}
		}
	}
}
