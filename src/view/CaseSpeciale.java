package view;

import util.Constantes.ActionSpeciale;

public class CaseSpeciale extends Case {
	private static final long serialVersionUID = 1L;
	
	private ActionSpeciale spec; // Type de case spéciale
	
	public CaseSpeciale(int p, String n, ActionSpeciale s) {
		super(p, n); 
		spec = s;
	}
	
	public ActionSpeciale getActionSpeciale() { return spec; }
	
	public String toString(){
		return "Case : Position -> " + position + " : Nom -> " + nomCase + " : Action -> " + spec;
	}
	
}