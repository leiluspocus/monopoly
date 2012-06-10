package behaviour.player;

import jade.lang.acl.ACLMessage;

import java.util.ArrayList;

import util.Constantes.Couleur;
import view.CaseAchetable;
import agent.AgentJoueur;


/**
 * Comportement visant à acheter un terrain dès qu'il est disponible
 */
public class AvideBehaviour extends ActivePlayerBehaviour {
	private static final long serialVersionUID = 1L;
	private AgentJoueur agentJoueur;

	public AvideBehaviour(AgentJoueur agentJoueur) {
		super(agentJoueur);
		this.agentJoueur = agentJoueur;
		this.agentJoueur.setProbaDemandeLoyer(45);
	}

	@Override
	protected void decideAchatTerrain(CaseAchetable caseCourante) {
		if (caseCourante.getProprietaireCase() == null){
			if(agentJoueur.getCapitalJoueur() > caseCourante.getValeurTerrain()){
				ACLMessage demandeAchat = new ACLMessage(ACLMessage.SUBSCRIBE);
				demandeAchat.setContent(caseCourante.getPosition() + "");
				demandeAchat.addReceiver(agentJoueur.getMonopoly());
				agentJoueur.send(demandeAchat);
				System.out.println(agentJoueur.getLocalName() + " demande a acheter " + caseCourante.getNom());
				agentJoueur.addProprieteToJoueur(caseCourante);
			}
			else
				System.out.println("Not enough money to buy " + caseCourante.getNom());
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
