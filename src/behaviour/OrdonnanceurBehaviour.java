package behaviour;

import jade.core.AID;
import jade.core.behaviours.Behaviour;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;

import java.io.IOException;
import java.util.HashMap;
import java.util.Random;
import java.util.Vector;

import util.Constantes;
import util.Constantes.Pion;
import util.Logger;
import view.Carte;
import view.CaseAchetable;
import view.Plateau;
import agent.AgentMonopoly;

public class OrdonnanceurBehaviour extends Behaviour {

	private static final long serialVersionUID = 1L;
	private Vector<DFAgentDescription> lesJoueurs;
	private HashMap<DFAgentDescription, Integer> lesPositionsDesJoueurs;
	private HashMap<DFAgentDescription, Boolean> canPlayerBeReleasedFromJail;
	private HashMap<Pion, Integer> lesJoueursEtLesPions;
	private Vector<String> joueursEnFaillite;
	private int currentTour;
	private AID prison;
	private AID banque;
	private Plateau plateau;
	private int newPos;
	private int oldPosition;

	public OrdonnanceurBehaviour(AgentMonopoly agentMonopoly, Plateau pl, Vector<DFAgentDescription> j, AID p) {
		super(agentMonopoly);
		lesJoueurs = j;
		prison = p;
		plateau = pl;
		banque = new AID("BANQUE", AID.ISLOCALNAME);
		currentTour = 0; 
		
		lesPositionsDesJoueurs = new HashMap<DFAgentDescription, Integer>();
		lesJoueursEtLesPions = new HashMap<Pion, Integer>();
		joueursEnFaillite = new Vector<String>();
		canPlayerBeReleasedFromJail = new HashMap<DFAgentDescription, Boolean>();
		
		for ( DFAgentDescription joueur : lesJoueurs ) {
			lesPositionsDesJoueurs.put(joueur, 0);
			int num = Integer.parseInt(joueur.getName().getLocalName().substring(6));
			lesJoueursEtLesPions.put(Constantes.lesPions[num-1], 0);
		}
		plateau.setPositionJoueurs(lesJoueursEtLesPions);
		
		// Tous les joueurs doivent-être sur la case depart
		myAgent.blockingReceive(); 
	}
	
	public void sendToJail(AID player) {
		System.out.println("Envoi du joueur " + player.getLocalName() + " en prison");
		ACLMessage tick = new ACLMessage(ACLMessage.CONFIRM);
		tick.addReceiver(prison);
		try {
			tick.setContentObject(player);
			myAgent.send(tick);
		} 
		catch (IOException e) { System.out.println(e.getMessage()); }
	}
	
	public void libererJoueur(AID player) {
		System.out.println("Liberation du  joueur " + player.getLocalName() + " emprisonne");
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
		try {
		DFAgentDescription joueur = lesJoueurs.get(currentTour); 
		
		//System.out.println("Envoi d'un message a " + joueur.getName().getLocalName() + " pour qu'il lance les des");
		throwDice(joueur);
		
		//System.out.println("Reception du score du joueur " + messageReceived.getSender().getLocalName() + " : " + message.getContent());
		ACLMessage messageReceived = myAgent.blockingReceive(); 
		
		if (messageReceived != null) {
			System.out.println("Ordonnanceur a recu un message : " + messageReceived.getPerformative() + ":" + messageReceived.getSender().getLocalName());
			if ( messageReceived.getPerformative() == ACLMessage.INFORM ) { 
				// Cas classique : le joueur n'est pas en faillite
				String playerLocalName = joueur.getName().getLocalName();
				oldPosition = lesPositionsDesJoueurs.get(joueur);
				@SuppressWarnings("unchecked")
				Vector<Integer> des = (Vector<Integer>) messageReceived.getContentObject();
				Integer diceValue = des.get(0) + des.get(1);
				boolean canPlayerPlay = true;
				
				// Calcul de la nouvelle position
				newPos = oldPosition + diceValue;
				
				if (wasPlayerInJail(joueur)) {
					// Le joueur etait en prison
					if (! playerCanBeRealeased(des, joueur, playerLocalName)) {
						newPos = Constantes.CASE_PRISON;
						canPlayerPlay = false;
					}
					else {
						libererJoueur(joueur.getName());
					}
					
				}
				if (canPlayerPlay) {
					// Libre de se deplacer
					if (newPos > Constantes.CASE_FIN) {
						newPos -= Constantes.CASE_FIN;
						System.out.println(playerLocalName + " a fini un tour");
						giveMoneyToPlayer(playerLocalName, 20000); // Le joueur a fait un tour complet
					}
					
					if (plateau.isCaseChance(newPos)) {
						Carte c = plateau.tirageChance();
						System.out.println(playerLocalName + " tire une carte Chance :\n" + c.getMsg());
						executeActionCarte(joueur, playerLocalName, c);
						
					}
					if (plateau.isCaseCommunaute(newPos)) {
						Carte c = plateau.tirageCommunaute();
						System.out.println(playerLocalName + " tire une carte Communaute :\n" + c.getMsg());
						executeActionCarte(joueur, playerLocalName, c);
					}
					
					switch(newPos) {
						case Constantes.CASE_GOTOPRISON :
							// Le joueur tombe sur la case prison, il faut l'y envoyer
							newPos = Constantes.CASE_PRISON;
							sendToJail(joueur.getName());
						break;
						case Constantes.CASE_IMPOTS :
							System.out.println(playerLocalName + " est tombe sur la case IMPOTS");
							makePlayerPay(playerLocalName, 20000);
						break;
						case  Constantes.CASE_TAXE :
							System.out.println(playerLocalName + " est tombe sur la case TAXE");
							makePlayerPay(playerLocalName, 10000);
						break;
					}
				}

				lesPositionsDesJoueurs.put(joueur, newPos);
				int num = Integer.parseInt(playerLocalName.substring(6));
				lesJoueursEtLesPions.put(Constantes.lesPions[num-1], newPos);
				plateau.setPositionJoueurs(lesJoueursEtLesPions);
				//System.out.println(lesJoueursEtLesPions);
				
				// L'agent Monopoly envoie au joueur la case
				ACLMessage caseCourante = new ACLMessage(ACLMessage.INFORM_REF);
				plateau.getCase(newPos).setJoueurQuiVientdArriver(joueur.getName());
				caseCourante.setContentObject(plateau.getCase(newPos));
				caseCourante.addReceiver(joueur.getName());
				myAgent.send(caseCourante);
				
				/**
				 * Avertir le proprietaire de la case qu'un joueur se trouve sur son terrain
				 */
				if (plateau.getCase(newPos) instanceof CaseAchetable){
					CaseAchetable caseJoueurCourant = (CaseAchetable) plateau.getCase(newPos);
					// Si la case a un propriétaire
					if (caseJoueurCourant.getProprietaireCase() != null){
						// et que c'est quelqu'un d'autre que le joueur qui vient de tomber dessus
						if (!(caseJoueurCourant.getProprietaireCase().getLocalName().equals(playerLocalName))){
							System.out.println(playerLocalName + " vient de tomber sur " + caseJoueurCourant.getNom());
							ACLMessage joueurSurVotreTerritoire = new ACLMessage(ACLMessage.INFORM);
							joueurSurVotreTerritoire.addReceiver(caseJoueurCourant.getProprietaireCase());
							joueurSurVotreTerritoire.setContentObject(plateau.getCase(newPos));
							myAgent.send(joueurSurVotreTerritoire);
						}
					}
				}
				
				//Avant de s'endormir, l'ordonnanceur doit vérifier sa liste de message !
				messageReceived = myAgent.blockingReceive(100);
				while(messageReceived != null){
					System.out.println("Ordonnanceur a recu un message : " + messageReceived.getPerformative() + ":" + messageReceived.getSender().getLocalName());
					
					if (messageReceived.getPerformative() == ACLMessage.INFORM_REF){
						joueursEnFaillite.add(messageReceived.getSender().getLocalName());
						System.out.println("Ajout du " + messageReceived.getSender().getLocalName() + " a la liste des faillites");
					}
					
					if (messageReceived.getPerformative() == ACLMessage.SUBSCRIBE){
						AID proprietaire = messageReceived.getSender();
						int positionCaseAchetee = Integer.parseInt(messageReceived.getContent());
						
						CaseAchetable proprietee = plateau.nouveauProprietaire(positionCaseAchetee, proprietaire);
						makePlayerPay(proprietaire.getLocalName(), proprietee.getValeurTerrain());
						Logger.info(proprietaire.getLocalName() + " est désormais proprietaire de " + proprietee.getNom());
					}
					
					messageReceived = myAgent.blockingReceive(100);
				}
				
				System.out.println(plateau.getCase(newPos));
				try {
					Thread.sleep(Constantes.DUREE_ANIMATION);
				} 
				catch (InterruptedException e) {  return; }
			}
			else{
				Logger.err("OrdonnanceurBehaviour a reçu un message qu'il n'a pas compris !");
			}
		}
		
		// On passe au joueur suivant
		plateau.redrawFrame();
		tourSuivant();
		}
		catch (Exception o) {o.printStackTrace();}
	}

	private boolean wasPlayerInJail(DFAgentDescription joueur) {
		try {
			ACLMessage req = new ACLMessage(ACLMessage.REQUEST);
			req.setContentObject(joueur.getName());
			req.addReceiver(prison);
			myAgent.send(req);
			ACLMessage reply = myAgent.blockingReceive();
			if ( reply != null ) {
				if ( reply.getPerformative() == ACLMessage.CONFIRM ) { 
					return (Boolean) reply.getContentObject();
				}
				else{
					System.err.println("OrdonnanceurBehaviour a reçu un message au mauvais moment : " + 
							reply.getPerformative() + ":" + reply.getSender().getLocalName());
				}
					
			}
		} 
		catch (IOException e) { e.printStackTrace();}
		catch (UnreadableException e) { e.printStackTrace();}
		
		return false;
	}

	/**
	 * Retourne si un joueur peut sortir ou non de prison
	 * @param diceValue
	 * @param joueur
	 * @return
	 */
	private boolean playerCanBeRealeased(Vector<Integer> diceValue,
			DFAgentDescription joueur, String playerName) {
		// Sur les valeurs du des
		if ( diceValue.get(0) == diceValue.get(1) ) {
			// S'il fait 12, il sort
			System.err.println("Le joueur " + playerName + " a fait un double et sort de prison! Quelle chance !");
			return true;
		} 
		int nbTours = getTimePassedInJail(joueur.getName());
		if ( nbTours == 3 ) {
			makePlayerPay(playerName, 5000);
			return true;
		}
		else {
			if ( canPlayerBeReleasedFromJail.containsKey(joueur) ) {
				// Le joueur decide de se servir de sa carte
				canPlayerBeReleasedFromJail.remove(joueur);
				System.err.println("Le joueur " + playerName + " utilise sa carte et sort de prison!");
				return true;
			}
			else {
				// Le joueur decide ou non de payer pour sortir TODO le faire dependre du behaviour ?
				boolean pay = new Random().nextBoolean();
				if ( pay ) {
					makePlayerPay(playerName, 5000);
					System.err.println("Le joueur " + playerName + " decide de payer 5000F et de sortir de prison!");
					return true;
				}
				System.err.println("Le joueur " + playerName + " prefere rester en prison");
				incrementTimePassedInJail(joueur.getName());
				return false;
			}
		}
	}

	/**
	 * Demande a la prison, le nombre de tours passes par un joueur en prison
	 * @param name le nom du joueur
	 * @return le nombre de tours passes
	 */
	private void incrementTimePassedInJail(AID name) {
		try {
			ACLMessage msg = new ACLMessage(ACLMessage.REQUEST_WHENEVER);
			msg.addReceiver(prison);
			msg.setContentObject(name);
			myAgent.send(msg); 
		} 
		catch (IOException e) {e.printStackTrace();} 
	}

	
	/**
	 * Demande a la prison, le nombre de tours passes par un joueur en prison
	 * @param name le nom du joueur
	 * @return le nombre de tours passes
	 */
	private int getTimePassedInJail(AID name) {
		try {
			ACLMessage msg = new ACLMessage(ACLMessage.REQUEST_WHEN);
			msg.addReceiver(prison);
			msg.setContentObject(name);
			myAgent.send(msg);
			
			//Attend la réponse
			ACLMessage reply = myAgent.blockingReceive();
			
			if (reply != null) {
				if (reply.getPerformative() != ACLMessage.CONFIRM || !(reply.getSender().getLocalName().equals("PRISON"))) { 
					System.err.println("OrdonnanceurBehaviour a reçu un message au mauvais moment : " + 
							reply.getPerformative() + ":" + reply.getSender().getLocalName());
				}	
			}
		} 
		catch (IOException e) {e.printStackTrace();}
		
		return 0;
	}

	/**
	 * Joueur execute action en fonction de la carte tiree
	 * @param joueur le nom du joueur qui a tire la carte
	 * @param messageToTheBank ACLMessage a envoyer a l'agent banque
	 * @param playerName le nom du joueur
	 * @param c la carte tiree
	 */
	private void executeActionCarte(DFAgentDescription joueur, String playerName, Carte c) {
		if (c.getValeur() != 0 ) {
			ACLMessage messageToTheBank = new ACLMessage(ACLMessage.SUBSCRIBE);
			messageToTheBank.addReceiver(banque);
			messageToTheBank.setContent(playerName + "#" + c.getValeur());
			myAgent.send(messageToTheBank);
		}
		else {
			if (c.goToJail()) {
				// Carte qui envoie le joueur en prison
				sendToJail(joueur.getName());
			}					
			if (c.canSetFreeFromJail()) {
				// Carte qui permet de liberer de prison
				canPlayerBeReleasedFromJail.put(joueur, true);
			}
			if (c.getDeplacement() >= 0){
				newPos = c.getDeplacement();
				if (newPos <= oldPosition){
					if(c.getTypeCarte() == Constantes.CHANCE)
						System.out.println(playerName + " vient de passer par la case Depart grace a la carte Communaute");
					else
						System.out.println(playerName + " vient de passer par la case Depart grace a la carte Chance");
					giveMoneyToPlayer(playerName, 20000); // Le joueur a fait un tour complet
				}
			}
			if (c.getDeplacement() == -3){
				newPos -= 3;
				System.out.println(playerName + " recule de 3 cases");
			}
		}
	}

	/**
	 * Faire payer un joueur
	 * @param playerName le nom du joueur
	 * @param money la somme a prelever
	 */
	private void makePlayerPay(String playerName, int money) {
		ACLMessage messageToTheBank = new ACLMessage(ACLMessage.SUBSCRIBE);
		messageToTheBank.addReceiver(banque);
		messageToTheBank.setContent(playerName + "#" + "-" + money);
		myAgent.send(messageToTheBank);
	}
	 
	/**
	 * Donner de l'argent a un joueur
	 * @param playerName le nom du joueur
	 * @param money la somme a donner
	 */
	private void giveMoneyToPlayer(String playerName, int money) {
		ACLMessage messageToTheBank = new ACLMessage(ACLMessage.SUBSCRIBE);
		messageToTheBank.addReceiver(banque);
		messageToTheBank.setContent(playerName + "#" + "+" + money);
		myAgent.send(messageToTheBank);
	}

	private void throwDice(DFAgentDescription joueur) {
		ACLMessage tick = new ACLMessage(ACLMessage.PROPAGATE);
		tick.addReceiver(joueur.getName());
		myAgent.send(tick);
	}

	private void tourSuivant() {
		currentTour ++;
		if(currentTour >= Constantes.NB_JOUEURS) {
			currentTour = 0;
		}
	}
	
	private boolean isGameOver() {
		return joueursEnFaillite.size() == Constantes.NB_JOUEURS;
	}

	@Override
	public boolean done() {
		return isGameOver();
	}

}
