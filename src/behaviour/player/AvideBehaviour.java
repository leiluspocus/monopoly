package behaviour.player;

import jade.lang.acl.ACLMessage;
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
			}
			else
				System.out.println("Not enough money to buy " + caseCourante.getNom());
		}
	}
}
