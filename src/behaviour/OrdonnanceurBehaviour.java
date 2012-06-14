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
import view.Case;
import view.CaseAchetable;
import view.CaseTerrain;
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
	private AgentMonopoly agentMonopoly;
	private int first;

	public OrdonnanceurBehaviour(AgentMonopoly agentMonopoly, Plateau pl, Vector<DFAgentDescription> j, AID p) {
		super(agentMonopoly);
		this.agentMonopoly = agentMonopoly;
		lesJoueurs = j;
		prison = p;
		plateau = pl;
		banque = new AID("BANQUE", AID.ISLOCALNAME);
		currentTour = 0;
		first = Constantes.NB_JOUEURS;
		
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
		Logger.info("Envoi du joueur " + player.getLocalName() + " en prison");
		ACLMessage tick = new ACLMessage(ACLMessage.CONFIRM);
		tick.addReceiver(prison);
		try {
			tick.setContentObject(player);
			myAgent.send(tick);
		} 
		catch (IOException e) { System.out.println(e.getMessage()); }
	}
	
	public void libererJoueur(AID player) {
		Logger.info("Liberation du  joueur " + player.getLocalName() + " emprisonne");
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
		throwDice(joueur); 
		ACLMessage messageReceived = myAgent.blockingReceive(); 
		
		if (messageReceived != null) { 
			//System.out.println("Ordonnanceur a recu un message : " + messageReceived.getPerformative() + ":" + messageReceived.getSender().getLocalName());
			if ( messageReceived.getPerformative() == ACLMessage.INFORM ) { 
				// Cas classique : le joueur n'est pas en faillite
				String playerLocalName = joueur.getName().getLocalName();
				oldPosition = lesPositionsDesJoueurs.get(joueur);
				@SuppressWarnings("unchecked")
				Vector<Integer> des = (Vector<Integer>) messageReceived.getContentObject();
				Integer diceValue = des.get(0) + des.get(1);
				boolean canPlayerPlay = true;

				//Logger.majInfosForPlayer(playerLocalName, " a fait " + diceValue + " aux des");
				// Calcul de la nouvelle position
				newPos = oldPosition + diceValue;
				
				
				//Pratique : Triche pour tester l'achat des maisons.
				/*if(playerLocalName.equals("JOUEUR1")){
					if(newPos == Constantes.CASE_GOTOPRISON){
						newPos = newPos + 5; 
					} 
				}
				
				if(playerLocalName.equals("JOUEUR2")){ 
						newPos = 5;  
				}*/
								
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
						Logger.info(playerLocalName + " a fait un tour complet");
						giveMoneyToPlayer(playerLocalName, 20000); // Le joueur a fait un tour complet
					}
					
					if (plateau.isCaseChance(newPos)) {
						Carte c = plateau.tirageChance();
						Logger.info(playerLocalName + " tire une carte Chance :\n" + c.getMsg());
						executeActionCarte(joueur, playerLocalName, c);
						
					}
					if (plateau.isCaseCommunaute(newPos)) {
						Carte c = plateau.tirageCommunaute();
						Logger.info(playerLocalName + " tire une carte Communaute :\n" + c.getMsg());
						executeActionCarte(joueur, playerLocalName, c);
					}
					
					switch(newPos) {
						case Constantes.CASE_GOTOPRISON :
							// Le joueur tombe sur la case prison, il faut l'y envoyer
							newPos = Constantes.CASE_PRISON;
							sendToJail(joueur.getName());
						break;
						case Constantes.CASE_IMPOTS :
							Logger.info(playerLocalName + " est tombe sur la case IMPOTS");
							makePlayerPay(playerLocalName, 20000);
						break;
						case  Constantes.CASE_TAXE :
							Logger.info(playerLocalName + " est tombe sur la case TAXE");
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
							Logger.info(playerLocalName + " vient de tomber sur " + caseJoueurCourant.getNom());
							ACLMessage joueurSurVotreTerritoire = new ACLMessage(ACLMessage.INFORM);
							joueurSurVotreTerritoire.addReceiver(caseJoueurCourant.getProprietaireCase());
							joueurSurVotreTerritoire.setContentObject(plateau.getCase(newPos));
							myAgent.send(joueurSurVotreTerritoire);
						}
					}
				}
				
				// Changer les règles
				if(plateau.allCasesHaveBeenSold() && first != 0){
					first--;
					ACLMessage informEndOfRules = new ACLMessage(ACLMessage.UNKNOWN);
					informEndOfRules.addReceiver(joueur.getName());
					informEndOfRules.setContent("Fin de regle limitant l'achat des maisons");
					myAgent.send(informEndOfRules);
				}
				
				//Avant de s'endormir, l'ordonnanceur doit vérifier sa liste de message !
				messageReceived = myAgent.blockingReceive(Constantes.TEMPS_DE_PAUSE);
				while(messageReceived != null){
					//System.out.println("Ordonnanceur a recu un message : " + messageReceived.getPerformative() + ":" + messageReceived.getSender().getLocalName());
					
					if (messageReceived.getPerformative() == ACLMessage.INFORM_REF){ //Un joueur a fait faillite
						if(!joueursEnFaillite.contains(messageReceived.getSender().getLocalName())){
							joueursEnFaillite.add(messageReceived.getSender().getLocalName());
							Logger.info("Ajout du " + messageReceived.getSender().getLocalName() + " a la liste des faillites");
							
							agentMonopoly.addFaillite(messageReceived.getSender().getLocalName());
							
							DFAgentDescription remove = null;
							for (DFAgentDescription df : lesJoueurs){
								if(df.getName().getLocalName().equals(messageReceived.getSender().getLocalName())){
									remove = df;
									break;
								}
							}
							if(remove != null)
								lesJoueurs.remove(remove);
							plateau.liquideJoueur(messageReceived.getSender());
						}
					}
					else if (messageReceived.getPerformative() == ACLMessage.SUBSCRIBE){ //Un joueur souhaite acheter une case
						AID proprietaire = messageReceived.getSender();
						int positionCaseAchetee = Integer.parseInt(messageReceived.getContent());
						
						CaseAchetable propriete = plateau.nouveauProprietaire(positionCaseAchetee, proprietaire);
						int nb = plateau.getNbTerrains(propriete.getCouleur(), proprietaire); 
						// On set le nombre de terrains possedes pour les cases de la meme famille ET ayant le meme PROPRIETAIRE
						plateau.setNbTerrainsPossedes(nb, propriete.getCouleur(), proprietaire);
						
						Vector<AID> prop = plateau.getProprietaires(propriete.getCouleur());
						plateau.setProprietairesPotentielsPourLesCouleurs(prop);
						makePlayerPay(proprietaire.getLocalName(), propriete.getValeurTerrain());
						Logger.info(proprietaire.getLocalName() + " est désormais proprietaire de " + propriete.getNom());
						agentMonopoly.addPossession(proprietaire.getLocalName(), propriete.getNom());
					}
					else if (messageReceived.getPerformative() == ACLMessage.PROXY){ //Un joueur souhaite acheter des maisons
						AID proprietaire = messageReceived.getSender();
						String[] res = messageReceived.getContent().split("#");
						int position = Integer.parseInt(res[0]);
						int prix = Integer.parseInt(res[1]);
						
						Case c = plateau.getCaseAtPosition(position);
						
						if(c instanceof CaseTerrain){
							if(((CaseTerrain)c).getProprietaireCase() != null && ((CaseTerrain)c).getProprietaireCase().getLocalName().equals(proprietaire.getLocalName())){
								if(prix == ((CaseTerrain)c).getValeurMaison()){
									((CaseTerrain)c).ajouterMaison();
									makePlayerPay(proprietaire.getLocalName(), prix);
									Logger.info(proprietaire.getLocalName() + " vient d'acheter une maison sur " + c.getNom());
								}
								else
									System.err.println(proprietaire.getLocalName() + " ne sait pas compter");
							}
							else
								System.err.println(proprietaire.getLocalName() + " tente d'acheter des maisons sur un terrain qui ne lui appartient pas");
						}
						else
							System.err.println(proprietaire.getLocalName() + " tente d'acheter des maisons sur le mauvais terrain");
					}
					
					messageReceived = myAgent.blockingReceive(Constantes.TEMPS_DE_PAUSE);
				}
				
				Logger.info(plateau.getCase(newPos).toString());
				try {
					Thread.sleep(Constantes.DUREE_ANIMATION);
				} 
				catch (InterruptedException e) {  return; }
			}
			else{
				System.err.println("OrdonnanceurBehaviour a reçu un message qu'il n'a pas compris !");
			}

			ACLMessage aclM = new ACLMessage(ACLMessage.CFP);
			aclM.addReceiver(joueur.getName());
			myAgent.send(aclM);
			
			ACLMessage replyP = myAgent.blockingReceive();
			if ( replyP != null ) {
				if ( replyP.getPerformative() == ACLMessage.ACCEPT_PROPOSAL) {
					String money = replyP.getContent(); 
					Logger.majInfosForPlayer(joueur.getName().getLocalName(), " a un capital de " + money);
				}
			}
		}
		
		// On passe au joueur suivant
		plateau.redrawFrame();
		Logger.info("\n-------------------\n");
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
			Logger.info("Le joueur " + playerName + " a fait un double et sort de prison! Quelle chance !");
			return true;
		} 
		int nbTours = getTimePassedInJail(joueur.getName());
		if ( nbTours > 3 ) {
			makePlayerPay(playerName, 5000);
			return true;
		}
		else {
			if ( canPlayerBeReleasedFromJail.containsKey(joueur) ) {
				// Le joueur decide de se servir de sa carte
				canPlayerBeReleasedFromJail.remove(joueur);
				Logger.info("Le joueur " + playerName + " utilise sa carte et sort de prison!");
				return true;
			}
			else {
				// Le joueur decide ou non de payer pour sortir TODO le faire dependre du behaviour ?
				boolean pay = new Random().nextBoolean();
				if ( pay ) {
					makePlayerPay(playerName, 5000);
					Logger.info("Le joueur " + playerName + " decide de payer 5000F et de sortir de prison!");
					return true;
				}
				Logger.info("Le joueur " + playerName + " prefere rester en prison");
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
						Logger.info(playerName + " vient de passer par la case Depart grace a la carte Communaute");
					else
						Logger.info(playerName + " vient de passer par la case Depart grace a la carte Chance");
					giveMoneyToPlayer(playerName, 20000); // Le joueur a fait un tour complet
				}
			}
			if (c.getDeplacement() == -3){
				newPos -= 3;
				Logger.info(playerName + " recule de 3 cases");
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

		if(currentTour >= Constantes.NB_JOUEURS - joueursEnFaillite.size())
			currentTour = 0;
	}

	private boolean isGameOver() {
		if(joueursEnFaillite.size() == (Constantes.NB_JOUEURS -1)){
			String gagnant = lesJoueurs.get(0).getName().getLocalName();
			
			Logger.info("LA PARTIE EST TERMINE");
			Logger.info(gagnant + " A GAGNE");
		}
		
		return joueursEnFaillite.size() == (Constantes.NB_JOUEURS -1);
	}

	@Override
	public boolean done() {
		return isGameOver();
	}

}
