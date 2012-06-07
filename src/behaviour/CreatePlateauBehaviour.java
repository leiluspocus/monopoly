package behaviour;

import jade.core.AID;
import jade.core.behaviours.Behaviour;
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

public class CreatePlateauBehaviour extends Behaviour {
	private static final long serialVersionUID = 1L;
	private AgentMonopoly agentMonopoly;
	private Plateau plateau;
	private int end = 2;

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
		System.out.println("L'agent MONOPOLY commence la creation du plateau");
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
		
		ACLMessage messageReceived = agentMonopoly.receive();
		if (messageReceived != null) {
			if(messageReceived.getPerformative() == ACLMessage.INFORM_IF){
				try {v2 = (Vector<Carte>) messageReceived.getContentObject();
				} catch (UnreadableException e) {e.printStackTrace();}
				end--;
			}
			else
				if(messageReceived.getPerformative() == ACLMessage.INFORM_REF){
					try {v1 = (Vector<Case>) messageReceived.getContentObject();
					} catch (UnreadableException e) {e.printStackTrace();}
					end--;
				}
				else
					System.out.println("Je suis l'agent " + myAgent.getLocalName() + " et l'agent " + messageReceived.getSender().getLocalName() + " a envoye un performatif incorrect");
		}
		else {
			block();
		}
		System.out.println("Le plateau vient d'etre cree");
		
		plateau = new Plateau(v1, v2);
	}
	
	public int onEnd(){
		System.out.println("Le behaviour RecupPlayersList a termine");
		reset();
		agentMonopoly.addBehaviour(new GivePlayersToOthers(agentMonopoly, agentMonopoly.getLesJoueurs()));
		
		Monopoly m = new Monopoly(agentMonopoly, plateau);
		agentMonopoly.addChangeListener(m);
	    return super.onEnd();
	}

	@Override
	public boolean done() {
		System.out.println("Le Behaviour de creation du plateau a termine : " + end);
		return end == 0;
	}
}
