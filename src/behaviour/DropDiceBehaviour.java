package behaviour;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import util.Logger;
import agent.AgentJoueur;

public class DropDiceBehaviour extends OneShotBehaviour {
 
	private static final long serialVersionUID = 1L; 
	
	public DropDiceBehaviour(Agent a) {
		super(a); 
	} 
	
	public AID getMonopoly() {
		try {
			AID res = ((AgentJoueur) myAgent).getMonopoly();
			return res;
		}
		catch ( Exception o ) { o.printStackTrace(); }
		return null;
	}
	
	public AID getSeed() {
		try {
			AID res = ((AgentJoueur) myAgent).getSeed();
			return res;
		}
		catch ( Exception o ) { o.printStackTrace(); }
		return null;
	}	
	
	public String getNom() {
		try {
			String res = ((AgentJoueur) myAgent).getNom();
			return res;
		}
		catch ( Exception o ) { o.printStackTrace(); }
		return "";		
	}
	
	private void jeterLesDes() {
    	ACLMessage jetDes = new ACLMessage(ACLMessage.REQUEST);
    	AID seed = getSeed();
    	jetDes.addReceiver(seed);
    	myAgent.send(jetDes);
    	Logger.info("Joueur " + ((AgentJoueur)myAgent).getNom() + " jette les des");
	}

	private void deplacerPion(String diceValue) { 
        ACLMessage diceMsgToMonopoly = new ACLMessage(ACLMessage.INFORM);
        diceMsgToMonopoly.addReceiver(getMonopoly());
        diceMsgToMonopoly.setContent(diceValue);
        myAgent.send(diceMsgToMonopoly);
        Logger.info("Joueur " + ((AgentJoueur)myAgent).getNom() + " a fait " + diceValue);
	}
	
	@Override
	public void action() { 
        ACLMessage message = myAgent.blockingReceive(); 
        if (message != null) {
        	// Attente de la valeur des des émise par l'AgentSeed
        	switch ( message.getPerformative() ) {
        	case ACLMessage.PROPAGATE :
        		// C'est au tour du joueur : il faut jeter les des
        		((AgentJoueur) myAgent).setMonopoly(message.getSender());
        		jeterLesDes();
                break;
        	case ACLMessage.CONFIRM:
        		// Les des sont jetes, il faut deplacer le pion
        		deplacerPion(message.getContent());
                break;
             default:
            	 Logger.err("Message inconnu non traite: " + message);
        	} 
        } 
        
	}
 

}
