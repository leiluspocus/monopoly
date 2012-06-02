package behaviour;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.lang.acl.ACLMessage;
import agent.AgentJoueur;

public class DropDiceBehaviour extends OneShotBehaviour {
 
	private static final long serialVersionUID = 1L; 
	
	public DropDiceBehaviour(Agent a) {
		super(a); 
	} 

	@Override
	public void action() {
		System.out.println("A mon tour " +  ((AgentJoueur)myAgent).getNom());
		try {
			Thread.sleep(60000);
		} catch (InterruptedException e) { 
			e.printStackTrace();
		}  
        ACLMessage message = myAgent.receive();
        if (message != null) {
        	// Attente de la valeur des des émise par l'AgentSeed
            if ( message.getPerformative() == ACLMessage.CONFIRM) {
                String diceValue = message.getContent();  
                ACLMessage diceMsgToMonopoly = new ACLMessage(ACLMessage.INFORM);
                diceMsgToMonopoly.setContent(diceValue);
                myAgent.send(diceMsgToMonopoly);
                System.out.println("Joueur " + ((AgentJoueur)myAgent).getNom() + " a fait " + diceValue);
            }
        }
        else {
        	// On lance les dès en faisant appel à l'agent Seed
            block();
        	ACLMessage jetDes = new ACLMessage(ACLMessage.REQUEST);
        	AID seed = ((AgentJoueur) myAgent).getSeed();
        	jetDes.addReceiver(seed);
        	myAgent.send(jetDes);
            System.out.println("Joueur " + ((AgentJoueur)myAgent).getNom() + " jette les des");
        }
        
	}
 

}
