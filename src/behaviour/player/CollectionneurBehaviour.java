package behaviour.player;

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
		// TODO Auto-generated method stub
		
	}
}
