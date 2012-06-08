package behaviour;

import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.ParallelBehaviour;
import jade.lang.acl.ACLMessage;
import agent.AgentJoueur;
import behaviour.player.ActivePlayerBehaviour;

public class RecupInitialCapital extends OneShotBehaviour {
	private static final long serialVersionUID = 1L;
	private AgentJoueur agentJoueur;
	private ACLMessage messageReceived;
	private Object[] params;

	public RecupInitialCapital(AgentJoueur agentJoueur, Object[] params) {
		super(agentJoueur);
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
				//System.out.println("L'agent " + agentJoueur.getLocalName() +  " a recupere sa dotation initiale");
			}
			else
				System.err.println("Le comportement RecupInitialCapital a reçu un message imprévu de type : "+messageReceived);
		} 
	}
	
	public int onEnd(){ //Démarre le comportement normal de l'agent joueur
		System.out.println(agentJoueur.getLocalName()+" commence à jouer avec un capital de "+agentJoueur.getCapitalJoueur());
		reset(); 
		ParallelBehaviour parallel = new ParallelBehaviour(ParallelBehaviour.WHEN_ALL);
		parallel.addSubBehaviour(new ActivePlayerBehaviour(agentJoueur, params));
		//parallel.addSubBehaviour(new PassivePlayerBehaviour(agentJoueur));
		agentJoueur.addBehaviour(parallel);
	    return super.onEnd();
	}
}
