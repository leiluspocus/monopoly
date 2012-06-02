package view;

import util.Constantes.ActionSpeciale;

public class CaseSpeciale extends Case {
	private ActionSpeciale spec; // Type de case sp�ciale
	
	public CaseSpeciale(int p, String n, ActionSpeciale s) {
		super(p, n); 
		spec = s;
	}
	
	public ActionSpeciale getActionSpeciale() { return spec; }
	
}