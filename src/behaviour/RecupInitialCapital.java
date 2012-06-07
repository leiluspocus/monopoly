package behaviour;

import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
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
				System.out.println("L'agent " + agentJoueur.getLocalName() +  " a recupere sa dotation initiale");
			}
			else
				System.out.println("Le comportement RecupInitialCapital a reçu un message imprévu de type : " + messageReceived.getPerformative());
		}
		else{
			System.out.println("L'agent " + agentJoueur.getLocalName() +  " est en attente du virement initial de la banque");
			block();
		}
	}
	
	public int onEnd(){ //Démarre le comportement normal de l'agent joueur
		System.out.println("Le joueur " + agentJoueur.getLocalName() +  " commence à jouer");
		reset();
		// SequentialBehaviour cyclique
		agentJoueur.addBehaviour(new PlayerBehaviour(agentJoueur, params));;
	    return super.onEnd();
	}
}
