package behaviour;

import jade.core.AID;
import jade.core.behaviours.Behaviour;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.lang.acl.ACLMessage;

import java.io.IOException;
import java.util.HashMap;
import java.util.Vector;

import util.Constantes;
import util.Constantes.Pion;
import view.Plateau;
import agent.AgentMonopoly;

public class OrdonnanceurBehaviour extends Behaviour {

	private static final long serialVersionUID = 1L;
	private Vector<DFAgentDescription> lesJoueurs;
	private HashMap<DFAgentDescription, Integer> lesPositionsDesJoueurs;
	HashMap<Pion, Integer> lesJoueursEtLesPions;
	private AgentMonopoly agentMonopoly;
	private int currentTour;
	private AID prison;
	private AID banque;
	
	public OrdonnanceurBehaviour(AgentMonopoly agentMonopoly, Vector<DFAgentDescription> j, AID p) {
		super(agentMonopoly);
		this.agentMonopoly = agentMonopoly;
		lesJoueurs = j;
		prison = p;
		banque = new AID("BANQUE", AID.ISLOCALNAME);
		lesPositionsDesJoueurs = new HashMap<DFAgentDescription, Integer>();
		lesJoueursEtLesPions = new HashMap<Pion, Integer>();
		for ( DFAgentDescription joueur : lesJoueurs ) {
			lesPositionsDesJoueurs.put(joueur, 0);
			int num = Integer.parseInt(joueur.getName().getLocalName().substring(6));
			lesJoueursEtLesPions.put(Constantes.lesPions[num-1], 0);
		}
		agentMonopoly.getPlateau().setPositionJoueurs(lesJoueursEtLesPions);
		currentTour = 0; 
		// Tous les joueurs sont initialement sur la case depart
		myAgent.blockingReceive(); 
	}
	
	public void sendToJail(AID player) {
		System.out.println("Envoi du joueur " + player + " en prison");
		ACLMessage tick = new ACLMessage(ACLMessage.CONFIRM);
		tick.addReceiver(prison);
		try {
			tick.setContentObject(player);
			myAgent.send(tick);
		} 
		catch (IOException e) { System.out.println(e.getMessage()); }
	}
	
	public void libererJoueur(AID player) {
		System.out.println("Liberation du  joueur " + player + " emprisonne");
		ACLMessage tick = new ACLMessage(ACLMessage.DISCONFIRM);
		tick.addReceiver(prison);
		try {
			tick.setContentObject(player);
			myAgent.send(tick);
		} 
		catch (IOException e) { System.out.println(e.getMessage()); }
	}


	@Override
	public void action() {  
		DFAgentDescription joueur = lesJoueurs.get(currentTour); 
		ACLMessage messageToTheBank = new ACLMessage(ACLMessage.SUBSCRIBE);
		messageToTheBank.addReceiver(banque);
		
		//System.out.println("Envoi d'un message a " + joueur.getName().getLocalName() + " pour qu'il lance les des");
		throwDice(joueur);
		
		//System.out.println("Reception du score du joueur " + messageReceived.getSender().getLocalName() + " : " + message.getContent());
		ACLMessage messageReceived = myAgent.blockingReceive(); 
		
		// Deplacement du pion du joueur
		if ( messageReceived != null ) { 
			if ( messageReceived.getPerformative() == ACLMessage.INFORM ) { 
				String playerName = messageReceived.getSender().getLocalName();
				Integer oldPosition = lesPositionsDesJoueurs.get(joueur);
				Integer diceValue = Integer.parseInt(messageReceived.getContent());
				int newPos;
				
				// Le joueur doit aller sur la case prison
				if ( diceValue < 0 ) {
					newPos = Constantes.CASE_PRISON;
					sendToJail(joueur.getName());
				}
				
				// Cas ou le joueur est en prison : il doit faire 12 pour en sortir 
				if ( oldPosition == Constantes.CASE_PRISON && diceValue != 12 ) {
					newPos = Constantes.CASE_PRISON; // S'il n'a pas fait 12, il reste sur sa case
				}
				else {
					// Le joueur peut se deplacer
					if ( oldPosition == Constantes.CASE_PRISON ) { // On envoie un message a l'agent prison pour liberer le joueur
						libererJoueur(joueur.getName());
					}
					if ( oldPosition + diceValue < Constantes.CASE_FIN ) {
						newPos = oldPosition + diceValue;
					}
					else {
						// On a fait un tour de plateau -> donner de l'argent au joueur !
						System.out.println("Le joueur " + playerName + " a fini un tour");
						messageToTheBank.setContent(playerName + "#" + "+20000");
						myAgent.send(messageToTheBank);
						
						newPos = oldPosition + diceValue - Constantes.CASE_FIN;
					}
					
					if(newPos == Constantes.CASE_IMPOTS){
						System.out.println("Le joueur " + playerName + " est tombe sur la case IMPOTS");
						messageToTheBank.setContent(playerName + "#" + "-20000");
						myAgent.send(messageToTheBank);
					}
					if(newPos == Constantes.CASE_TAXE){
						System.out.println("Le joueur " + playerName + " est tombe sur la case TAXE");
						messageToTheBank.setContent(playerName + "#" + "-10000");
						myAgent.send(messageToTheBank);
					}
				}
				lesPositionsDesJoueurs.put(joueur, newPos);
				int num = Integer.parseInt(joueur.getName().getLocalName().substring(6));
				lesJoueursEtLesPions.put(Constantes.lesPions[num-1], newPos);
				agentMonopoly.getPlateau().setPositionJoueurs(lesJoueursEtLesPions);
				System.out.println(lesJoueursEtLesPions);
				
				// L'agent Monopoly envoie au joueur la case
				ACLMessage caseCourante = new ACLMessage(ACLMessage.INFORM_REF);
				Plateau p = ((AgentMonopoly) myAgent).getPlateau();
				try {
					caseCourante.setContentObject(p.getCase(newPos));
					caseCourante.addReceiver(joueur.getName());
					myAgent.send(caseCourante);
				} 
				catch (IOException e1) { e1.printStackTrace(); }
				try {
					Thread.sleep(Constantes.DUREE_ANIMATION);
				} catch (InterruptedException e) { 
					e.printStackTrace();
				}
			}
			else if (messageReceived.getPerformative() == ACLMessage.INFORM_REF)
			{
				// Joueur en faillite
			}
		}  
		// On passe au joueur suivant
		tourSuivant();
	}

	private void throwDice(DFAgentDescription joueur) {
		ACLMessage tick = new ACLMessage(ACLMessage.PROPAGATE);
		tick.addReceiver(joueur.getName());
		myAgent.send(tick);
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
