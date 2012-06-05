package playerBehaviour;

import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import util.Logger;
import agent.AgentJoueur;


/**
 * Comportement visant � acheter un terrain d�s qu'il est disponible
 */
public class AvideBehaviour extends OneShotBehaviour {

	private int probaDemandeLoyer = 95;

	private static final long serialVersionUID = 1L;

	public AvideBehaviour(Agent agentJoueur) {
		super(agentJoueur);
	}

	@Override
	public void action() 
	{
		Logger.info("Action d'avide");
		ACLMessage msgReceived = myAgent.receive();

		if (msgReceived != null)
		{
			switch (msgReceived.getPerformative()) 
			{
			/*
			 * Demande de paiement: � destination d'un joueur (loyer/terrain/case sp�ciale), ou de la banque (taxes)
			 * Envoi du paiement au destinataire
			 */
			case ACLMessage.REQUEST:
				((AgentJoueur)myAgent).payerMontantDu(msgReceived);
				break;
				/*
				 * Un joueur est sur une des propri�t�s de myAgent
				 */
			case ACLMessage.INFORM:
				((AgentJoueur)myAgent).demanderLoyer(probaDemandeLoyer, msgReceived);
				break;
				/*
				 * Indique au joueur sur quelle case il se trouve apr�s le d�placement effectu� (d� au jet� de d�s)
				 */
			case ACLMessage.INFORM_REF:
				//TODO: acheter la propri�t� si celle-ci est disponible
				break;
				/*
				 * Message receptionnant l'argent d'un joueur ou de la banque
				 */
			case ACLMessage.AGREE:
				int sommeRecue = Integer.parseInt(msgReceived.getContent().trim());
				((AgentJoueur)myAgent).setCapitalJoueur(((AgentJoueur)myAgent).getCapitalJoueur()+sommeRecue);
				break;

			default: Logger.info("Message non g�r� : "+msgReceived.getContent());
			break;
			}
		}
		else
		{
			block();
		}
	}

}
