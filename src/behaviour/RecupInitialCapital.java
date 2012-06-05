package behaviour;

import agent.AgentJoueur;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;

public class RecupInitialCapital extends OneShotBehaviour {
	private static final long serialVersionUID = 1L;
	private AgentJoueur agentJoueur;

	public RecupInitialCapital(AgentJoueur agentJoueur) {
		this.agentJoueur = agentJoueur;
	}

	@Override
	public void action() {
		ACLMessage messageReceived = myAgent.receive();
		if (messageReceived != null) {
			if(messageReceived.getPerformative() == ACLMessage.INFORM){
				String line = messageReceived.getContent();
				agentJoueur.setCapitalJoueur(Integer.parseInt(line));
				System.out.println("L'agent Joueur a recupere sa dotation initiale");
			}
			else
				System.out.println("Le comportement RecupInitialCapital a reçu un message imprévu de type : " + messageReceived.getPerformative());
		}
		else{
			block();
		}
	}
}
