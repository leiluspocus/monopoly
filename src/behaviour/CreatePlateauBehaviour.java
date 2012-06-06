package behaviour;

import jade.core.AID;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;
import jade.wrapper.AgentController;
import jade.wrapper.StaleProxyException;

import java.beans.PropertyChangeSupport;
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
	private Plateau plateau;

	public CreatePlateauBehaviour(AgentMonopoly agentMonopoly) {
		this.agentMonopoly = agentMonopoly;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void action() {
		AID agentBDC = new AID("BDC", AID.ISLOCALNAME);
		AgentController bc;
		
		try {
			bc = MainContainer.getMc().createNewAgent("BDC","agent.AgentBDC", null);
			bc.start();
		} catch (StaleProxyException e) {e.printStackTrace();}
		
		Vector<Case> v1 = null;
		Vector<Carte> v2 = null;
		ACLMessage request = new ACLMessage(ACLMessage.QUERY_IF);
		request.addReceiver(agentBDC);
		
		request.setContent("Pourrais-je avoir la liste des cartes ?");
		agentMonopoly.send(request);
		
		ACLMessage messageReceived = agentMonopoly.receive();
		while(messageReceived == null) {
			block();
			agentMonopoly.receive();
		}
		try {v2 = (Vector<Carte>) messageReceived.getContentObject();
		} catch (UnreadableException e) {e.printStackTrace();}
		
		request = new ACLMessage(ACLMessage.QUERY_REF);
		request.addReceiver(agentBDC);
		request.setContent("Pourrais-je avoir la liste des cases ?");
		agentMonopoly.send(request);
		
		messageReceived = agentMonopoly.receive();
		while(messageReceived == null) {
			block();
			agentMonopoly.receive();
		}
		try {v1 = (Vector<Case>) messageReceived.getContentObject();
		} catch (UnreadableException e) {e.printStackTrace();}
		
		System.out.println("Le plateau vient d'etre cree");
		
		plateau = new Plateau(v1, v2);
	}
	
	public int onEnd(){
		Monopoly m = new Monopoly(agentMonopoly, plateau);
		agentMonopoly.addChangeListener(m);
	    return super.onEnd();
	}
}
