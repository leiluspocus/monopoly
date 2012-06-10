package behaviour.player;

import util.Logger;
import agent.AgentJoueur;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;

public class PassivePlayerBehaviour extends Behaviour{

	private static final long serialVersionUID = 1L;

	public PassivePlayerBehaviour(Agent a){
		super(a);
	}
	
	@Override
	public void action(){
		ACLMessage msgReceived = myAgent.blockingReceive();
		switch(msgReceived.getPerformative()){
			/*
			 * Un joueur est sur une des propriétés de myAgent
			 */
			case ACLMessage.INFORM:
				((AgentJoueur)myAgent).demanderLoyer(msgReceived);
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
				System.out.println("Mouvement d'argent : " + myAgent.getLocalName() + " -> +" + sommeRecue);
			break;
			
			/*
			 * Message reçu de l'ordonnanceur, "reveil toi, c'est à toi de jouer"
			 */
			case ACLMessage.PROPAGATE:
				((AgentJoueur)myAgent).setPropagateMessage(msgReceived);
			break;

			
			case ACLMessage.CFP: // Le monopoly demande au joueur son nouveau capital
				int valCapital = ((AgentJoueur) myAgent).getCapitalJoueur();
				ACLMessage reply = msgReceived.createReply();
				reply.setPerformative(ACLMessage.ACCEPT_PROPOSAL);
				reply.setContent(valCapital + "");
				myAgent.send(reply);
				break;
				
			default: Logger.err("PassivePlayerBehaviour de " + myAgent.getLocalName() + ": message inconnu de " + msgReceived.getSender().getLocalName() + ":" + msgReceived.getPerformative()); 
				break;
		}
	}

	@Override
	public boolean done() {
		return ((AgentJoueur)myAgent).aRecuPropagate();
	}
}
