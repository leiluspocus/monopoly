package behaviour;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;

import java.io.IOException;
import java.util.Random;
import java.util.Vector;

public class GenerateIntBehaviour extends CyclicBehaviour {
 
	private static final long serialVersionUID = 1L;
	
	private Random intGenerator;
	
	public GenerateIntBehaviour(Agent a) {
		super(a);
		intGenerator = new Random();
		System.out.println(" -- SEED INITIALISE -- ");
	}

	@Override
	public void action() {
        ACLMessage message = myAgent.blockingReceive(); 
        if (message != null) {
	        if ( message.getPerformative() == ACLMessage.REQUEST ) {
	        	// Demande de jet de des de la part d'un agent joueur. on genere un numero compris entre 2 et 12
	        	ACLMessage reponse = message.createReply();
	        	reponse.setPerformative(ACLMessage.CONFIRM);
	        	try {
					reponse.setContentObject(jeterDes());
		        	myAgent.send(reponse);
				} catch (IOException e) { 
					e.printStackTrace();
				}
	        }
        } 

	}
	
	public Vector<Integer> jeterDes() {
		int int1 = intGenerator.nextInt(6) + 1; 
		int int2 = intGenerator.nextInt(6) + 1; 
		Vector<Integer> res = new Vector<Integer>();
		res.add(int1); res.add(int2);
		return res;
	}

}
