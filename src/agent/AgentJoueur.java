package agent;

import jade.core.AID;
import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;

import java.util.Random;

import util.Constantes.Pion;
import util.Logger;
import view.Case;
import view.CaseAchetable;
import view.CaseTerrain;
import behaviour.RecupInitialCapital;

public class AgentJoueur extends Agent{
	private static final long serialVersionUID = 1L;
	
	private String 	nomJoueur;
	private AID 	seed;
	private AID	 	monopoly;
	private Pion 	pion;
	private Case 	caseCourante;
	private int		capitalJoueur;
	private boolean	enFaillite;
	private int 	probaDemandeLoyer;
	
	private void fetchSeedAgent() {
		DFAgentDescription template = new DFAgentDescription();
		ServiceDescription sd = new ServiceDescription();
		sd.setType("seed"); 
		template.addServices(sd);
		DFAgentDescription[] result;
		
		try {
			do{ 
				result = DFService.search(this, template);
			}while (result.length == 0);
			
			seed = result[0].getName();
			//System.out.println("L'agent " + getLocalName() + " est connecte a l'agent SEED");
		}
		catch(FIPAException fe) { Logger.err("Exception � la recuperation du seedagent par le joueur "); fe.printStackTrace(); }
	} 
	
	private void registerPlayer() 
	{
		DFAgentDescription agentDescription = new DFAgentDescription();
        agentDescription.setName(getAID());
        ServiceDescription serviceDescription  = new ServiceDescription();
        serviceDescription.setType("joueur");
        serviceDescription.setName(getLocalName());
        agentDescription.addServices(serviceDescription);
        try {
            DFService.register(this, agentDescription);
        } 
        catch (FIPAException e) { Logger.err("Enregistrement de l'agent joueur au service echoue - Cause : " + e); }  
	}
	
	protected void setup() {
		fetchSeedAgent(); 
		Object[] params = this.getArguments();		
		pion = ((Pion)params[0]);
		nomJoueur = ((String)params[2]);
		capitalJoueur = 0;
		enFaillite = false;
		registerPlayer(); 
		
		// Comportement initial
		addBehaviour(new RecupInitialCapital(this, params));
	}
	
	/**
	 * Calcule une valeur entre 1 et 100. Si celle-ci correspond � la probabilit� de demande,
	 * on envoi un message au joueur concern� lui demandant de payer le loyer qu'il doit � this
	 * M�thode utilis�e par les comportements du joueur
	 * @param msgReceived message de requ�te re�u
	 */

	public boolean demanderLoyer(ACLMessage msgReceived){
		Random rand = new Random();
		int value = rand.nextInt(100)+1;
		if (value <= probaDemandeLoyer){
			Case caseJoueur = null;
			try{
				caseJoueur = (Case) msgReceived.getContentObject();
			}
			catch (UnreadableException e1){e1.printStackTrace();}

			ACLMessage demandeLoyer = new ACLMessage(ACLMessage.REQUEST);
			int montantLoyer = 0;
			if (caseJoueur instanceof CaseTerrain){
				int nbMaisons = ((CaseTerrain)caseJoueur).getNbMaisons(); 
				montantLoyer = ((CaseAchetable)caseJoueur).getLoyers().get(nbMaisons);
			}
			else if (caseJoueur instanceof CaseAchetable){
				// TODO: loyer pour les gares / compagnies
			}
			demandeLoyer.setContent(String.valueOf(montantLoyer));
			
			// TODO: Le joueur risque de demander deux fois le loyer � un m�me joueur
			for(AID a : caseJoueur.getJoueurPresents())
				demandeLoyer.addReceiver(a);
				
			send(demandeLoyer);
			
			return true;
		}
		
		return false;
	}
	
	/**
	 * Envoi un message contenant le montant demand� 
	 * � l'agent destinataire de cette somme (un joueur ou la banque)
	 * @param msgReceived le message de demande d'argent re�u par le joueur
	 */
	public void payerMontantDu(ACLMessage msgReceived)
	{
		ACLMessage response = msgReceived.createReply();
		int montantDu = Integer.parseInt(msgReceived.getContent().trim());
		setCapitalJoueur(capitalJoueur-montantDu);
		response.setContent(String.valueOf(montantDu));
		response.setPerformative(ACLMessage.AGREE);
		send(response);
	}
	
	/**
	 * Envoi un message � l'agent monopoly pour lui signaler que this est en faillite
	 * Le joueur n'a plus d'argent, il a perdu
	 */
	public void faillite()
	{
		ACLMessage msgFaillite = new ACLMessage(ACLMessage.INFORM_REF);
		msgFaillite.setContent(getNom()+ " a perdu");
		msgFaillite.addReceiver(monopoly);
		send(msgFaillite);
	}
	
	public AID getSeed() { return seed;	}
	public AID getMonopoly() { return monopoly; }
	public Pion getPion() { return pion; }
	public String getNom() { return nomJoueur; }
	public Case getCaseCourante() { return caseCourante; }
	public int getCapitalJoueur() { return capitalJoueur; }
	public boolean isEnFaillite() {return enFaillite;}

	public void setPion(Pion pion) { this.pion = pion; }
	public void setNom(String nom) { nomJoueur = nom; }
	public void setCaseCourante(Case caseCourante) { this.caseCourante = caseCourante; }
	public void setMonopoly(AID m) { monopoly = m; }
	public void setEnFaillite(boolean enFaillite) {this.enFaillite = enFaillite;}
	public void setCapitalJoueur(int capitalJoueur) {
		if (capitalJoueur <= 0)
		{
			faillite(); // Joueur en faillite
		}
		else
		{
			this.capitalJoueur = capitalJoueur;
		}
	}

	public int getProbaDemandeLoyer() {
		return probaDemandeLoyer;
	}

	public void setProbaDemandeLoyer(int probaDemandeLoyer) {
		this.probaDemandeLoyer = probaDemandeLoyer;
	}
}
