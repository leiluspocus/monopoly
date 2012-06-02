package agent;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.SequentialBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import util.Constantes;
import util.Constantes.Pion;
import view.Case;
import behaviour.AvideBehaviour;
import behaviour.CollectionneurBehaviour;
import behaviour.DropDiceBehaviour;
import behaviour.EvilBehaviour;
import behaviour.IntelligentBehaviour;
import behaviour.PicsouBehaviour;
import behaviour.StupideBehaviour;

public class AgentJoueur extends Agent{
	private static final long serialVersionUID = 1L;
	
	private String 				nomJoueur;
	private DFAgentDescription 	seed;
	private DFAgentDescription 	monopoly;
	private Pion 				pion;
	private Case 				caseCourante;
	private int					capitalJoueur;
	
	private void fetchSeedAgent() {
		DFAgentDescription template = new DFAgentDescription();
		ServiceDescription sd = new ServiceDescription();
		sd.setType("seed"); 
		template.addServices(sd);
		try {
			DFAgentDescription[] result = DFService.search(this, template);
			if (result.length > 0) {
				seed = result[0];
			}
		}
		catch(FIPAException fe) { System.out.println("Exception ‡ la recuperation du seedagent par le joueur "); fe.printStackTrace(); }
	}
	
	private void fetchMonopoly() {
		DFAgentDescription template = new DFAgentDescription();
		ServiceDescription sd = new ServiceDescription();
		sd.setType("monopoly"); 
		template.addServices(sd);
		try {
			DFAgentDescription[] result =
					DFService.search(this, template);
			if (result.length > 0) {
				monopoly = result[0];
			}
		}
		catch(FIPAException fe) { System.out.println("Exception ‡ la recuperation du monopoly par le joueur "); fe.printStackTrace(); }
		
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
        catch (FIPAException e) { System.out.println("Enregistrement de l'agent au service echoue - Cause : " + e); }  
	}
	
	protected void setup() {
		// Sequential Behaviour => Je lance le des, et j'applique ma tactique de jeu
		fetchSeedAgent();
		fetchMonopoly();
		Object[] params = this.getArguments();		
		setPion((Pion)params[0]);
		setNom((String)params[2]);
		setCapitalJoueur(Constantes.CAPITAL_DEPART);
		registerPlayer(); 
		// SequentialBehaviour cyclique
		SequentialBehaviour seqBehaviour = new SequentialBehaviour() { 
			private static final long serialVersionUID = 1L;

			public int onEnd() {
			    reset();
			    myAgent.addBehaviour(this);
			    return super.onEnd();
			}
		};
		seqBehaviour.addSubBehaviour(new DropDiceBehaviour(this));
		switch((Integer) params[1])
		{
			case 0:
			{
				System.out.println("Joueur " + getNom() + " adopte la stratégie Avide !");
				seqBehaviour.addSubBehaviour(new AvideBehaviour());
				break;
			}
			case 1:
			{
				System.out.println("Joueur " + getNom() + " adopte la stratégie Collectionneur !");
				seqBehaviour.addSubBehaviour(new CollectionneurBehaviour());
				break;
			}
			case 2:
			{
				System.out.println("Joueur " + getNom() + " adopte la stratégie Evil !");
				seqBehaviour.addSubBehaviour(new EvilBehaviour());
				break;
			}
			case 3:
			{
				System.out.println("Joueur " + getNom() + " adopte la stratégie Intelligent !");
				seqBehaviour.addSubBehaviour(new IntelligentBehaviour());
				break;
			}
			case 4:
			{
				System.out.println("Joueur " + getNom() + " adopte la stratégie Picsou !");
				seqBehaviour.addSubBehaviour(new PicsouBehaviour());
				break;
			}
			case 5:
			{
				System.out.println("Joueur " + getNom() + " adopte la stratégie Stupide !");
				seqBehaviour.addSubBehaviour(new StupideBehaviour());
				break;
			}
			default:
				seqBehaviour.addSubBehaviour(new AvideBehaviour());
				break;
		} 
		addBehaviour(seqBehaviour);
	}
	
	public AID getSeed() { return seed.getName();	}
	public AID getMonopoly() { return monopoly.getName(); }
	public Pion getPion() { return pion; }
	public String getNom() { return nomJoueur; }
	public Case getCaseCourante() { return caseCourante; }
	public int getCapitalJoueur() { return capitalJoueur; }

	public void setPion(Pion pion) { this.pion = pion; }
	public void setNom(String nom) { nomJoueur = nom; }
	public void setCaseCourante(Case caseCourante) { this.caseCourante = caseCourante; }
	public void setCapitalJoueur(int capitalJoueur) { this.capitalJoueur = capitalJoueur; } 
	
}
