package behaviour.player;

import java.util.ArrayList;

import jade.lang.acl.ACLMessage;
import util.Constantes.Couleur;
import view.CaseAchetable;
import agent.AgentJoueur;

/**
 * Comportement visant à collectionner les terrains d'une même couleur (Ici 2 couleurs maximum)
 */
public class CollectionneurBehaviour extends ActivePlayerBehaviour {
	
	private static final long serialVersionUID = 1L;
	private static Couleur couleur1 = null;
	private static Couleur couleur2 = null;
	private AgentJoueur agentJoueur;
	
	public CollectionneurBehaviour(AgentJoueur agentJoueur) {
		super(agentJoueur);
		this.agentJoueur = agentJoueur;
		this.agentJoueur.setProbaDemandeLoyer(95);
	}

	@Override
	protected void decideAchatTerrain(CaseAchetable caseCourante) {
		if (caseCourante.getProprietaireCase() == null){
		
			int doitAcheter = 0;
			
			if(couleur1 == null || couleur1 == caseCourante.getCouleur())
				doitAcheter = 1;
			
			if(couleur2 == null || couleur2 == caseCourante.getCouleur())
				doitAcheter = 2;
			
			if(doitAcheter != 0){
				if(agentJoueur.getCapitalJoueur() > caseCourante.getValeurTerrain()){
					ACLMessage demandeAchat = new ACLMessage(ACLMessage.SUBSCRIBE);
					demandeAchat.setContent(caseCourante.getPosition() + "");
					demandeAchat.addReceiver(agentJoueur.getMonopoly());
					agentJoueur.send(demandeAchat);
					System.out.println(agentJoueur.getLocalName() + " demande a acheter " + caseCourante.getNom());
					agentJoueur.addProprieteToJoueur(caseCourante);
					
					if(doitAcheter == 1)
						couleur1 = caseCourante.getCouleur();
					else
						couleur2 = caseCourante.getCouleur();
				}
				else
					System.out.println("Not enough money to buy " + caseCourante.getNom());
			}
		}
	}

	@Override
	protected void decideAchatMaison() {
		ArrayList<Couleur> cpp = agentJoueur.possedeLaCouleur();
		
		if(cpp.size() != 0){
			for(Couleur coul : cpp){
				int prix[] = agentJoueur.getPrixMaison(coul);
				int prixTotal = prix[0] * prix[1];
				
				if(agentJoueur.getCapitalJoueur() > prixTotal){ //Le joueur a t-il assez d'argent pour acheter les maisons ?
					ACLMessage demandeAchat = new ACLMessage(ACLMessage.PROXY);
					demandeAchat.setContent(coul + "#" + prixTotal);
					demandeAchat.addReceiver(agentJoueur.getMonopoly());
					agentJoueur.send(demandeAchat);
					System.out.println(agentJoueur.getLocalName() + " demande a acheter des maisons pour les cases " + coul);
				}
				else
					System.out.println("Not enough money to buy houses on" + coul);
			}
		}
		else
			System.out.println(agentJoueur.getLocalName() + " ne peut pas encore acheter de maisons");
		
	}
}
