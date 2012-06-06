package behaviour;

import behaviour.player.PlayerBehaviour;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import agent.AgentJoueur;

public class RecupInitialCapital extends Behaviour {
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
		messageReceived = myAgent.receive();
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

	@Override
	public boolean done() {
		return messageReceived != null;
	}
	
	public int onEnd(){ //Démarre le comportement normal de l'agent joueur
		System.out.println("Le behaviour RecupPlayersList a termine");
		reset();
		// SequentialBehaviour cyclique
		agentJoueur.addBehaviour(new PlayerBehaviour(agentJoueur, params));;
	    return super.onEnd();
	}
}
