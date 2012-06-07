package behaviour;

import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import util.Logger;
import agent.AgentJoueur;
import behaviour.player.PlayerBehaviour;

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
				Logger.err("Le comportement RecupInitialCapital a re�u un message impr�vu de type : " + messageReceived);
		} 
	}
	
	public int onEnd(){ //D�marre le comportement normal de l'agent joueur
		System.out.println("Le joueur " + agentJoueur.getLocalName() +  " commence � jouer");
		reset(); 
		agentJoueur.addBehaviour(new PlayerBehaviour(agentJoueur, params));
	    return super.onEnd();
	}
}
