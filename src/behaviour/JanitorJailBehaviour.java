package behaviour;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;

import java.io.IOException;
import java.util.Vector;

public class JanitorJailBehaviour extends CyclicBehaviour {

	private static final long serialVersionUID = 1L;
	private Vector<AID> prisonniers; 

	public JanitorJailBehaviour(Agent a) {
		super(a);
		prisonniers = new Vector<AID>(); 
		System.out.println("-- PRISON INITIALISEE -- ");
	}
	@Override
	public void action() {
		ACLMessage message = myAgent.blockingReceive();
		if ( message != null ) {
			switch ( message.getPerformative() ) {
			case ACLMessage.CONFIRM : //Cas ou un joueur doit etre enferme
				AID res;
				try {
					res = (AID) message.getContentObject();
					prisonniers.add(res);
				} catch (UnreadableException e) { 
					e.printStackTrace();
				}
				break;
			case ACLMessage.DISCONFIRM : // Cas ou un joueur doit etre libere
				AID x;
				try {
					x = (AID) message.getContentObject();
					prisonniers.remove(x);
				} catch (UnreadableException e) { 
					e.printStackTrace();
				}
				break;
			case ACLMessage.REQUEST : // Le monopoly demande si le joueur XX est en prison 
				checkIsPlayerInJail(message);
				break;
			}
		}

	}
	private void checkIsPlayerInJail(ACLMessage message) {
		AID x;
		try {
			x = (AID) message.getContentObject();
			boolean isPlayerInJail = prisonniers.contains(x);
			ACLMessage reply = message.createReply();
			reply.setPerformative(ACLMessage.CONFIRM);
			reply.setContentObject(isPlayerInJail);
			myAgent.send(reply);
		} 
		catch (UnreadableException e) { 
			e.printStackTrace();
		}
		catch (IOException excep) { 
			excep.printStackTrace();
		}
	} 

}
