package behaviour;

import jade.core.behaviours.Behaviour;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.lang.acl.ACLMessage;

import java.util.HashMap;
import java.util.Vector;

import util.Constantes;
import util.Logger;
import agent.AgentMonopoly;

public class OrdonnanceurBehaviour extends Behaviour {

	private static final long serialVersionUID = 1L;
	private Vector<DFAgentDescription> lesJoueurs;
	private HashMap<DFAgentDescription, Integer> lesPositionsDesJoueurs;
	private int currentTour;
	
	public OrdonnanceurBehaviour(AgentMonopoly agentMonopoly,
			Vector<DFAgentDescription> j) {
		super(agentMonopoly);
		lesJoueurs = j;
		lesPositionsDesJoueurs = new HashMap<DFAgentDescription, Integer>();
		for ( DFAgentDescription joueur : lesJoueurs ) {
			lesPositionsDesJoueurs.put(joueur, 0);
		}
		currentTour = 0; 
		// Tous les joueurs sont initialement sur la case depart
	}

	@Override
	public void action() { 
		DFAgentDescription joueur = lesJoueurs.get(currentTour);
		Logger.info("indice fou " + currentTour);
		
		// Envoi d'un message au joueur pour qu'il lance les des
		ACLMessage tick = new ACLMessage(ACLMessage.PROPAGATE);
		tick.addReceiver(joueur.getName());
		myAgent.send(tick);
 
		// Reception du score fait par le joueur
		ACLMessage message = myAgent.blockingReceive(); 
		// Deplacement du pion du joueur
		if ( message != null ) { 
			if ( message.getPerformative() == ACLMessage.INFORM) { 
				Integer value = lesPositionsDesJoueurs.get(joueur);
				Integer delta = Integer.parseInt(message.getContent());
				Integer newPos;
				if ( value + delta < Constantes.CASE_FIN ) {
					newPos = value + delta;
				}
				else {
					// On a fait un tour de plateau -> donner de l'argent au joueur !
					newPos = value + delta - Constantes.CASE_FIN;
				}
				lesPositionsDesJoueurs.put(joueur, newPos);
				try {
					Thread.sleep(Constantes.DUREE_ANIMATION);
				} catch (InterruptedException e) { 
					e.printStackTrace();
				}
			}
		}  
		// On passe au joueur suivant
		tourSuivant();
	}

	private void tourSuivant() {
		currentTour ++;
		if ( currentTour >= Constantes.NB_JOUEURS ) {
			currentTour = 0;
		}
	}

	@Override
	public boolean done() {
		// A voir
		return false;
	}

}
