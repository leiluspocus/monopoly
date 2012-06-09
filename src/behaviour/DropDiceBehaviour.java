package behaviour;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;

import java.io.Serializable;

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
    	//System.out.println("Joueur " + ((AgentJoueur)myAgent).getNom() + " jette les des");
	}

	private void deplacerPion(Serializable diceValue) { 
		try {
        ACLMessage diceMsgToMonopoly = new ACLMessage(ACLMessage.INFORM);
        diceMsgToMonopoly.addReceiver(getMonopoly());
        diceMsgToMonopoly.setContentObject(diceValue);
        myAgent.send(diceMsgToMonopoly);
        Logger.info(myAgent.getLocalName() + " a fait " + diceValue);
		}
		catch ( Exception o ) { o.printStackTrace(); }
	}
	
	@Override
	public void action() { 
		//System.out.println("Drop dice : " + myAgent.getLocalName()); 
		
        ACLMessage message = myAgent.blockingReceive(); 
        if (message != null) {
        	//System.err.println(" >>> alert " + message);
        	// Attente de la valeur des des émise par l'AgentSeed
	       	 if ( message.getPerformative() == ACLMessage.PROPAGATE ) {
        		// C'est au tour du joueur : il faut jeter les des
        		((AgentJoueur) myAgent).setMonopoly(message.getSender());
        		jeterLesDes();
                message = myAgent.blockingReceive(); 
            	//System.err.println(" >>> alert 2 " + message);
                if ( message.getPerformative() == ACLMessage.CONFIRM ) {
                	// Les des sont jetes, il faut deplacer le pion
                	try {
                		deplacerPion(message.getContentObject()); 
                	}
                	catch ( Exception o ) { o.printStackTrace(); }
                }
                else {
               	 System.err.println("Message inconnu non traite from DropDiceBehaviour apres avoir jete les des: " + message.getSender().getName());
                }
	       	 }
	       	 else { 
	       		System.err.println("Message inconnu non traite from DropDiceBehaviour: " + message.getSender().getName()); 
        	} 
        } 
        
	}
 

}
