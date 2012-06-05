package playerBehaviour;

import agent.AgentJoueur;
import util.Logger;
import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;

/**
 * Behaviour déclenché après tirage de dés
 * N'acheter aucun terrain
 */
public class StupideBehaviour extends OneShotBehaviour {
	
	private static final long serialVersionUID = 1L;

	private int probaDemandeLoyer = 50;
	
	public StupideBehaviour(Agent agentJoueur) {
		super(agentJoueur);
	}

	@Override
	public void action() {
		
ACLMessage msgReceived = myAgent.receive();
		
		if (msgReceived != null)
		{
			switch (msgReceived.getPerformative()) 
			{
				/*
				 * Demande de paiement: à destination d'un joueur (loyer/terrain/case spéciale), ou de la banque (taxes)
				 * Envoi du paiement au destinataire
				 */
				case ACLMessage.REQUEST:
					((AgentJoueur)myAgent).payerMontantDu(msgReceived);
					break;
				/*
				 * Un joueur est sur une des propriétés de myAgent
				 */
				case ACLMessage.INFORM:
					((AgentJoueur)myAgent).demanderLoyer(probaDemandeLoyer, msgReceived);
					break;
				/*
				 * Indique au joueur sur quelle case il se trouve après le déplacement effectué (dû au jeté de dés)
				 */
				case ACLMessage.INFORM_REF:
					//TODO: acheter la propriété si celle-ci est disponible
					break;
				/*
				 * Message receptionnant l'argent d'un joueur ou de la banque
				 */
				case ACLMessage.AGREE:
					int sommeRecue = Integer.parseInt(msgReceived.getContent().trim());
					((AgentJoueur)myAgent).setCapitalJoueur(((AgentJoueur)myAgent).getCapitalJoueur()+sommeRecue);
					break;
					
				default: Logger.info("Message non géré : "+msgReceived.getContent());
					break;
			}
		}
		else
		{
			block();
		}
	}


}
