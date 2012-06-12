package behaviour.player;

import jade.lang.acl.ACLMessage;

import java.util.ArrayList;

import util.Logger;
import view.CaseAchetable;
import view.CaseTerrain;
import agent.AgentJoueur;


/**
 * Comportement visant à acheter un terrain dès qu'il est disponible
 */
public class AvideBehaviour extends ActivePlayerBehaviour {
	private static final long serialVersionUID = 1L;
	private AgentJoueur agentJoueur;
	private int virementEnAttente = 0;

	public AvideBehaviour(AgentJoueur agentJoueur) {
		super(agentJoueur);
		this.agentJoueur = agentJoueur;
		this.agentJoueur.setProbaDemandeLoyer(90);
	}

	@Override
	protected void decideAchatTerrain(CaseAchetable caseCourante) {
		if (caseCourante.getProprietaireCase() == null){
			if(agentJoueur.getCapitalJoueur() > caseCourante.getValeurTerrain()){
				ACLMessage demandeAchat = new ACLMessage(ACLMessage.SUBSCRIBE);
				demandeAchat.setContent(caseCourante.getPosition() + "");
				demandeAchat.addReceiver(agentJoueur.getMonopoly());
				agentJoueur.send(demandeAchat);
				Logger.info(agentJoueur.getLocalName() + " demande a acheter " + caseCourante.getNom());
				
				caseCourante.setProprietaireCase(agentJoueur.getAID());
				agentJoueur.addProprieteToJoueur(caseCourante);
				virementEnAttente = caseCourante.getValeurTerrain();
			}
			else
				Logger.info(agentJoueur.getLocalName() + " n'a pas assez d'argent pour acheter " + caseCourante.getNom());
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
