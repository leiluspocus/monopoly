package behaviour;

import jade.core.AID;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;
import jade.wrapper.AgentController;
import jade.wrapper.StaleProxyException;

import java.util.Vector;

import platform.MainContainer;
import view.Carte;
import view.Case;
import view.Monopoly;
import view.Plateau;
import agent.AgentMonopoly;

public class CreatePlateauBehaviour extends OneShotBehaviour {
	private static final long serialVersionUID = 1L;
	private AgentMonopoly agentMonopoly;


	public CreatePlateauBehaviour(AgentMonopoly agentMonopoly) {
		this.agentMonopoly = agentMonopoly;
		
		try {
			AgentController bc;
			bc = MainContainer.getMc().createNewAgent("BDC","agent.AgentBDC", null);
			bc.start();
		} catch (StaleProxyException e) {e.printStackTrace();}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void action() {
		AID agentBDC = new AID("BDC", AID.ISLOCALNAME);
		
		Vector<Case> v1 = null;
		Vector<Carte> v2 = null;
		
		ACLMessage request = new ACLMessage(ACLMessage.QUERY_IF);
		request.addReceiver(agentBDC);
		request.setContent("Pourrais-je avoir la liste des cartes ?");
		agentMonopoly.send(request);
		
		request = new ACLMessage(ACLMessage.QUERY_REF);
		request.addReceiver(agentBDC);
		request.setContent("Pourrais-je avoir la liste des cases ?");
		agentMonopoly.send(request);
		
		ACLMessage messageReceived = agentMonopoly.blockingReceive();
		if (messageReceived != null) {
			if(messageReceived.getPerformative() == ACLMessage.INFORM_IF){
				try {v2 = (Vector<Carte>) messageReceived.getContentObject();
				} catch (UnreadableException e) {e.printStackTrace();}
			}
			else
				if(messageReceived.getPerformative() == ACLMessage.INFORM_REF){
					try {v1 = (Vector<Case>) messageReceived.getContentObject();
					} catch (UnreadableException e) {e.printStackTrace();}
				}
				else
					System.err.println("Je suis l'agent " + myAgent.getLocalName() + " et l'agent " + messageReceived.getSender().getLocalName() + " a envoye un performatif incorrect");
		}
		
		messageReceived = agentMonopoly.blockingReceive();
		if (messageReceived != null) {
			if(messageReceived.getPerformative() == ACLMessage.INFORM_IF){
				try {v2 = (Vector<Carte>) messageReceived.getContentObject();
				} catch (UnreadableException e) {e.printStackTrace();}
			}
			else
				if(messageReceived.getPerformative() == ACLMessage.INFORM_REF){
					try {v1 = (Vector<Case>) messageReceived.getContentObject();
					} catch (UnreadableException e) {e.printStackTrace();}
				}
				else
					System.err.println("Je suis l'agent " + myAgent.getLocalName() + " et l'agent " + messageReceived.getSender().getLocalName() + " a envoye un performatif incorrect");
		} 
		
		((AgentMonopoly) myAgent).setPlateau(new Plateau(v1, v2));
	}
	
	public Plateau getPlateau() {
		return ((AgentMonopoly) myAgent).getPlateau();
	}
	public int onEnd(){
		//System.out.println("Le behaviour CreatePlateau a termine");
		reset();
		agentMonopoly.addBehaviour(new GivePlayersToOthers(agentMonopoly, agentMonopoly.getLesJoueurs()));
		
		Monopoly m = new Monopoly(((AgentMonopoly) myAgent).getPlateau() );
		((AgentMonopoly) myAgent).getPlateau().setFrame(m);
		agentMonopoly.addChangeListener(m);
	    return super.onEnd();
	}
}
