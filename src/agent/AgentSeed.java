package agent;

import jade.core.Agent;

import java.util.Random;

public class AgentSeed extends Agent{
	private static final long serialVersionUID = 1L;
	
	private Random intGenerator;
	
	public int jeterDes() {
		return intGenerator.nextInt(11) +1;
	}
}
