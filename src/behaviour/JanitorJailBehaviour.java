package behaviour;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class JanitorJailBehaviour extends CyclicBehaviour {

	private static final long serialVersionUID = 1L;
	private Map<AID, Integer> prisonniers;  // AID -> Nom du prisonnier, INT nombre de tours passes en prison

	public JanitorJailBehaviour(Agent a) {
		super(a);
		prisonniers = new HashMap<AID, Integer>(); 
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
					prisonniers.put(res, 0);
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
			case ACLMessage.REQUEST_WHEN : // Le monopoly demande le nombre de tours passes en prison par un joueur
				getTimeInJail(message);
				break;
			case ACLMessage.REQUEST_WHENEVER: // Le monopoly notifie la prison que le joueur souhaite y rester
				incrementTimeInJail(message);
				break;
			}
		}

	}
	/**
	 * Incremente le temps passe en prison par un joueur
	 * @param message
	 */
	private void incrementTimeInJail(ACLMessage message) {
		AID x;
		try {
			x = (AID) message.getContentObject();
			int nbT = prisonniers.get(x);
			prisonniers.put(x, nbT +1);
		} 
		catch (UnreadableException e) { 
			e.printStackTrace();
		} 
	}
	
	/**
	 * Envoie au monopoly le nombre de tours passes en prison par un joueur
	 * @param message
	 */
	private void getTimeInJail(ACLMessage message) {
		AID x;
		try {
			x = (AID) message.getContentObject();
			Integer isPlayerInJail = prisonniers.get(x);
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
	
	/**
	 * Retourne si un joueur est en prison actuellement ou non
	 * @param message contenant l'AID du joueur
	 */
	private void checkIsPlayerInJail(ACLMessage message) {
		AID x;
		try {
			x = (AID) message.getContentObject();
			boolean isPlayerInJail = prisonniers.containsKey(x);
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
