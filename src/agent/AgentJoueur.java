package agent;

import jade.core.AID;
import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;

import java.util.Map;
import java.util.Random;

import org.codehaus.jackson.map.ObjectMapper;

import util.Constantes;
import util.Constantes.Pion;
import view.Case;
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
			System.out.println("L'agent " + getLocalName() + " est connecte a l'agent SEED");
		}
		catch(FIPAException fe) { System.out.println("Exception � la recuperation du seedagent par le joueur "); fe.printStackTrace(); }
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
        catch (FIPAException e) { System.out.println("Enregistrement de l'agent joueur au service echoue - Cause : " + e); }  
	}
	
	protected void setup() {
		// Sequential Behaviour => Je lance le des, et j'applique ma tactique de jeu
		fetchSeedAgent(); 
		Object[] params = this.getArguments();		
		setPion((Pion)params[0]);
		setNom((String)params[2]);
		//setCapitalJoueur(Constantes.CAPITAL_DEPART);
		setEnFaillite(false);
		registerPlayer(); 
		
		// Comportement initial
		addBehaviour(new RecupInitialCapital(this, params));
	}
	
	/**
	 * Calcule une valeur entre 1 et 100. Si celle-ci correspond � la probabilit� de demande,
	 * on envoi un message au joueur concern� lui demandant de payer le loyer qu'il doit � this
	 * M�thode utilis�e par les comportements du joueur
	 * @param joueurCreditaire le joueur devant payer le loyer
	 */
	@SuppressWarnings("unchecked")
	public void demanderLoyer(int probaDemandeLoyer, ACLMessage msgReceived)
	{
		Random rand = new Random();
		int value = rand.nextInt(100)+1;
		if (value <= probaDemandeLoyer)
		{
			/*
			 * Message re�u de la forme
			 * {
			 * 	"destinataire":"NomCompletAgent";
			 * 	"montant":2000
			 * }
			 */
			String content = msgReceived.getContent();

			Map<Object,String> map = null;
			ObjectMapper mapper = new ObjectMapper();

			try
			{
				map = mapper.readValue(content,Map.class);
			}
			catch(Exception e) {System.out.println("Erreur deserialisation message : "+e);}

			ACLMessage request = new ACLMessage(ACLMessage.REQUEST);

			String receiverName = map.get("destinataire");
			AID receiver = new AID(receiverName, true);
			request.addReceiver(receiver);
			request.setContent(map.get("montant"));
			send(request);
			System.out.println(getName()+" demande "+map.get("montant")+" � "+receiverName);
		}
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
}
