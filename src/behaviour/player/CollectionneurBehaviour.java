package behaviour.player;

import java.util.ArrayList;

import jade.lang.acl.ACLMessage;
import util.Logger;
import util.Constantes.Couleur;
import view.CaseAchetable;
import view.CaseTerrain;
import agent.AgentJoueur;

/**
 * Comportement visant à collectionner les terrains d'une même couleur (Ici 2 couleurs maximum)
 */
public class CollectionneurBehaviour extends ActivePlayerBehaviour {
	private static final long serialVersionUID = 1L;
	private static Couleur couleur1 = null;
	private static Couleur couleur2 = null;
	private int virementEnAttente = 0;
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
					Logger.info(agentJoueur.getLocalName() + " demande a acheter " + caseCourante.getNom());
					agentJoueur.addProprieteToJoueur(caseCourante);
					virementEnAttente = caseCourante.getValeurTerrain();
					
					if(doitAcheter == 1)
						couleur1 = caseCourante.getCouleur();
					else
						couleur2 = caseCourante.getCouleur();
				}
				else
					System.out.println(agentJoueur.getLocalName() + " n'a pas assez d'argent pour acheter " + caseCourante.getNom());
			}
		}
	}

	@Override
	protected void decideAchatMaison() {
ArrayList<CaseTerrain> cpp = agentJoueur.peutPoserMaisons();
		
		if(cpp.size() != 0){
			for(CaseTerrain ct : cpp){
				int prix = ct.getValeurMaison();
				
				if(agentJoueur.getCapitalJoueur() > (prix + virementEnAttente)){ //Le joueur a t-il assez d'argent pour acheter les maisons ?
					ACLMessage demandeAchat = new ACLMessage(ACLMessage.PROXY);
					demandeAchat.setContent(ct.getPosition() + "#" + prix);
					demandeAchat.addReceiver(agentJoueur.getMonopoly());
					agentJoueur.send(demandeAchat);
					System.out.println(agentJoueur.getLocalName() + " demande a acheter des maisons sur " + ct.getNom());
					virementEnAttente += prix;
				}
				else
					System.out.println(agentJoueur.getLocalName() + " n'a pas assez d'argent pour acheter des maisons sur " + ct.getNom());
			}
		}
		else
			Logger.info(agentJoueur.getLocalName() + " ne peut pas encore acheter de maisons");
		
		virementEnAttente = 0;
	}
}
