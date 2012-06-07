package behaviour;

import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import util.Logger;
import agent.AgentJoueur;
import behaviour.player.ActivePlayerBehaviour;

public class RecupInitialCapital extends OneShotBehaviour {
	private static final long serialVersionUID = 1L;
	private AgentJoueur agentJoueur;
	private ACLMessage messageReceived;
	private Object[] params;

	public RecupInitialCapital(AgentJoueur agentJoueur, Object[] params) {
		this.agentJoueur = agentJoueur;
		this.params = params;
	}

	@Override
	public void action() {
		messageReceived = myAgent.blockingReceive();
		if (messageReceived != null) { 
			if(messageReceived.getPerformative() == ACLMessage.INFORM){ 
				String line = messageReceived.getContent();
				agentJoueur.setCapitalJoueur(Integer.parseInt(line));
				System.err.println("L'agent " + agentJoueur.getLocalName() +  " a recupere sa dotation initiale");
			}
			else
				Logger.err("Le comportement RecupInitialCapital a reçu un message imprévu de type : " + messageReceived);
		} 
	}
	
	public int onEnd(){ //Démarre le comportement normal de l'agent joueur
		System.out.println("Le joueur " + agentJoueur.getLocalName() +  " commence à jouer");
		reset(); 
		agentJoueur.addBehaviour(new ActivePlayerBehaviour(agentJoueur, params));
	    return super.onEnd();
	}
}
