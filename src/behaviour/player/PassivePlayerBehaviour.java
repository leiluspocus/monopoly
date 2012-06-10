package behaviour.player;

import util.Logger;
import agent.AgentJoueur;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;

public class PassivePlayerBehaviour extends Behaviour{

	private static final long serialVersionUID = 1L;
	private boolean waitingForMoney;

	public PassivePlayerBehaviour(Agent a){
		super(a);
		waitingForMoney = false;
	}
	
	@Override
	public void action(){
		ACLMessage msgReceived = myAgent.receive();
		Logger.info(myAgent.getLocalName() + " a atteint son comportement passif");
		if (msgReceived != null)
		{
			switch(msgReceived.getPerformative()){
				/*
				 * Un joueur est sur une des propriétés de myAgent
				 */
				case ACLMessage.INFORM:
					waitingForMoney = ((AgentJoueur)myAgent).demanderLoyer(msgReceived);
				break;
				/*
				 * Demande de paiement: à destination d'un joueur (loyer/terrain/case spéciale), ou de la banque (taxes)
				 * Envoi du paiement au destinataire
				 */
				case ACLMessage.REQUEST:
					((AgentJoueur)myAgent).payerMontantDu(msgReceived);
				break;
				/*
				 * Message receptionnant l'argent d'un joueur ou de la banque
				 */
				case ACLMessage.AGREE:
					int sommeRecue = Integer.parseInt(msgReceived.getContent().trim());
					((AgentJoueur)myAgent).setCapitalJoueur(((AgentJoueur)myAgent).getCapitalJoueur()+sommeRecue);
					waitingForMoney = false;
				break;
					
				default: Logger.err("PassivePlayerBehaviour a recu un message inconnu de " + msgReceived.getSender().getLocalName() + ":" + msgReceived.getPerformative()); 
					break;
			}
		}
		else
		{
			block();
		}
	}

	@Override
	public boolean done() 
	{
		return !waitingForMoney;
	}

}
