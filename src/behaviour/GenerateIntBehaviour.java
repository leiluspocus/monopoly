package behaviour;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;

import java.util.Random;

public class GenerateIntBehaviour extends CyclicBehaviour {
 
	private static final long serialVersionUID = 1L;
	
	private Random intGenerator;
	
	public GenerateIntBehaviour(Agent a) {
		super(a);
		intGenerator = new Random();
	}

	@Override
	public void action() {
        ACLMessage message = myAgent.blockingReceive(); 
        if (message != null) {
	        if ( message.getPerformative() == ACLMessage.REQUEST ) {
	        	// Demande de jet de des de la part d'un agent joueur. on genere un numero compris entre 1 et 12
	        	ACLMessage reponse = message.createReply();
	        	reponse.setPerformative(ACLMessage.CONFIRM);
	        	reponse.setContent(jeterDes());
	        	myAgent.send(reponse);
	        }
        } 

	}
	
	public String jeterDes() {
		int res = intGenerator.nextInt(11) +1; 
		return res + "";
	}

}
