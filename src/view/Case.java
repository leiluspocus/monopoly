package view;

public abstract class Case{
	protected int position; //Position de la case sur le plateau
	protected String nomCase;  // Nom de la case
	
	public String getNom() { return nomCase; }
	
	public Case(int p, String n) { 
		position = p; nomCase = n;
	} 
}
