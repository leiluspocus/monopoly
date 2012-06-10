package behaviour.player;

import jade.core.behaviours.SequentialBehaviour;
import agent.AgentJoueur;
import behaviour.DropDiceBehaviour;

public class PlayerBehaviour extends SequentialBehaviour {

	private static final long serialVersionUID = 1L;
	private Integer strategy;
	
	public String getNom() {return ((AgentJoueur) myAgent).getNom();}
	 
	public PlayerBehaviour(AgentJoueur agentJoueur, Object[] params) {
		super(agentJoueur);
		// Sequential Behaviour => Je lance le des, et j'applique ma tactique de jeu
		addSubBehaviour(new DropDiceBehaviour(myAgent));
		strategy = (Integer) params[1];
		pickStrategy();
		addSubBehaviour(new PassivePlayerBehaviour(myAgent));
	}

	private void pickStrategy() {
		switch(strategy){
			case 0:
				System.out.println("Joueur " + myAgent.getLocalName() + " adopte la strategie Avide !");
				addSubBehaviour(new AvideBehaviour((AgentJoueur) myAgent));
			break;
			case 1:
				System.out.println("Joueur " + myAgent.getLocalName() + " adopte la strategie Collectionneur !");
				addSubBehaviour(new CollectionneurBehaviour((AgentJoueur) myAgent));
			break;
			case 2:
				System.out.println("Joueur " + myAgent.getLocalName() + " adopte la strategie Evil !");
				addSubBehaviour(new EvilBehaviour((AgentJoueur) myAgent));
			break;
			case 3:
				System.out.println("Joueur " + myAgent.getLocalName() + " adopte la strategie Intelligent !");
				addSubBehaviour(new IntelligentBehaviour((AgentJoueur) myAgent));
			break;
			case 4:
				System.out.println("Joueur " + myAgent.getLocalName() + " adopte la strategie Picsou !");
				addSubBehaviour(new PicsouBehaviour((AgentJoueur) myAgent));
			break;
			case 5:
				System.out.println("Joueur " + myAgent.getLocalName() + " adopte la strategie Stupide !");
				addSubBehaviour(new StupideBehaviour((AgentJoueur) myAgent));
			break;
			default:
				System.out.println("Joueur " + myAgent.getLocalName() + " adopte la strategie Avide !");
				addSubBehaviour(new AvideBehaviour((AgentJoueur) myAgent));
				break;
		}
	}
	
	@Override
	public int onEnd() { 
	    reset();
	    // Ne pas rescheduler les behaviour si le joueur a perdu la partie
	    if (!((AgentJoueur)myAgent).isEnFaillite()){
		    myAgent.addBehaviour(this);
	    }
	    return super.onEnd();
	}

}
