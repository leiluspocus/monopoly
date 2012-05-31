package database;

import java.io.OutputStream;
import java.util.List;

import javax.xml.stream.XMLStreamWriter;

import util.Constantes;

import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.query.ResultSetFormatter;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;

/**
 * Modele contenant la base de connaissances
 *
 */
public class CaseModel{
	public static final String uri = "http://Monopoly#";
	private Model model;

	/**
	 * Lit la base de connaissances n3
	 */
	public CaseModel(){
		this.model = ModelFactory.createDefaultModel();
		model.read("file:./txt/monopoly.n3", "Monopoly", "TURTLE"); 
	}

	/**
	 * Recherche les cases magentas (Lecourbe & Belleville)
	 * @return Le r�sultat de la requ�te SPARQL
	 */
	public ResultSet lookForMagenta() {
		String sql = "PREFIX mnply: <http://Monopoly#>" +
		"SELECT ?Magenta WHERE { ?Magenta mnply:couleur \"Magenta\"}";
		return runExecQuery(sql);
	}	

	/**
	 * Recherche les cases sp�ciales (D�part, Chance, Caisse de Communaut�, ...)
	 * @return Le r�sultat de la requ�te SPARQL
	 */
	public ResultSet lookForCasesSpeciales() {
		String sql = "PREFIX mnply: <http://Monopoly#>" +
			"SELECT " + Constantes.position + " " + Constantes.nom + " " + Constantes.type +" WHERE { " +
			" ?x a mnply:Speciale ." +
			" ?x mnply:position ?position ." +
			" ?x mnply:nom ?nom ." +
			" ?x mnply:type ?type" +
			"}";
		return runExecQuery(sql);
	}
	
	/**
	 * Recherche les cases achetables (Gare & Compagnies)
	 * @return Le r�sultat de la requ�te SPARQL
	 */
	public ResultSet lookForCasesAchetables() {
		String sql = "PREFIX mnply: <http://Monopoly#>" +
			"SELECT " + Constantes.position + " " + Constantes.nom + " " + 
				Constantes.valeur + " " + Constantes.loyerNu + " " +
			    Constantes.loyer1 + " " + Constantes.loyer2 + " " +
				Constantes.loyer3 + " " + Constantes.couleur + " WHERE { " +
			" ?x a mnply:Case ." +
			" ?x mnply:position ?position ." +
			" ?x mnply:nom ?nom ." +
			" ?x mnply:valeur ?valeur ." +
			" ?x mnply:loyerNu ?loyerNu ." +
			" ?x mnply:loyer1 ?loyer1 ." +
			" ?x mnply:loyer2 ?loyer2 ." +
			" ?x mnply:loyer3 ?loyer3 ." +
			" ?x mnply:couleur ?couleur" +
			"}";
		String s = ResultSetFormatter.asText(runExecQuery(sql));
		return runExecQuery(sql);
	}
	
	/**
	 * Recherche tous les terrains
	 * @return Le r�sultat de la requ�te SPARQL
	 */
	public ResultSet lookForTerrains() {
		String sql = "PREFIX mnply: <http://Monopoly#>" + 
			"SELECT " + Constantes.position + " " + Constantes.nom + " " + 
			Constantes.valeur + " " + Constantes.loyerNu + " " +
		    Constantes.loyer1 + " " + Constantes.loyer2 + " " +
			Constantes.loyer3 + " " + Constantes.loyer4 + " " +
		    Constantes.loyerHotel + " " + Constantes.couleur + 
		    Constantes.valeurMaison + " WHERE { " +
			" ?x a mnply:Terrain ." +
			" ?x mnply:position ?position ." +
			" ?x mnply:nom ?nom ." +
			" ?x mnply:valeur ?valeur ." +
			" ?x mnply:loyerNu ?loyerNu ." +
			" ?x mnply:loyer1 ?loyer1 ." +
			" ?x mnply:loyer2 ?loyer2 ." +
			" ?x mnply:loyer3 ?loyer3 ." +
			" ?x mnply:loyer4 ?loyer4 ." +
			" ?x mnply:loyerHotel ?loyerHotel ." +
			" ?x mnply:couleur ?couleur ." +
			" ?x mnply:valeurMaison ?valeurMaison" +
			"}";
		return runExecQuery(sql);
	}
	
	/**
	 * Recherche toutes les cartes Chance
	 * @return Le r�sultat de la requ�te SPARQL
	 */
	public ResultSet lookForCartesChance() {
		String sql = "PREFIX mnply: <http://Monopoly#>" +
			"SELECT " + Constantes.texte + " " + Constantes.valeur + " WHERE { "+
			" ?x a mnply:CarteChance ." +
			" ?x mnply:texte ?texte ." +
			" ?x mnply:valeur ?valeur" +
			"}"; 
		return runExecQuery(sql);
	}
	
	/**
	 * Recherche toutes les cartes Caisse de Communaut�
	 * @return Le r�sultat de la requ�te SPARQL
	 */
	public ResultSet lookForCartesCommunaute() {
		String sql = "PREFIX mnply: <http://Monopoly#>" + 
			"SELECT " + Constantes.texte + " " + Constantes.valeur + " WHERE { "+
			" ?x a mnply:CarteCommunaute ." +
			" ?x mnply:texte ?texte ." +
			" ?x mnply:valeur ?valeur" +
			"}"; 
		return runExecQuery(sql);
	}
	
	public ResultSet runExecQuery(String qString) {
		Query query = QueryFactory.create(qString) ; 
		QueryExecution queryExecution = QueryExecutionFactory.create(query, model);
		ResultSet r = queryExecution.execSelect();
		//queryExecution.close();
		return r;
	}

}
