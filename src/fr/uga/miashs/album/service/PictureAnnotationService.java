package fr.uga.miashs.album.service;

import org.apache.jena.query.*;
import org.apache.jena.rdf.model.*;
import org.apache.jena.update.*;

public class PictureAnnotationService {

	
	
	
	public void test() {
		System.out.println("enter test");
		
		// SPARQL Update
/*		UpdateRequest request = UpdateFactory.create("PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>"+
													"INSERT DATA {"+
													" <http://imss.univ-grenoble-alpes.fr/ns/album#Selfie> rdfs:subClassOf <http://imss.univ-grenoble-alpes.fr/ns/album#Picture> ."+
													" <http://imss.univ-grenoble-alpes.fr/ns/album#picture321> a <http://imss.univ-grenoble-alpes.fr/ns/album#Selfie> .}");

		UpdateProcessor up = UpdateExecutionFactory.createRemote(request, "http://localhost:3030/ALBUM/update");
		up.execute();
	*/	
		// SPARQL Query
		Query query = QueryFactory.create("SELECT ?subject ?predicate ?object WHERE { ?subject ?predicate <http://www.ILoveWebSemantic.com/photoApp#homme> }");
		  try (QueryExecution qexec = QueryExecutionFactory.sparqlService("http://localhost:3030/ALBUM/sparql",query)) {
			  System.out.println("Execution requete : ");
		    ResultSet results = qexec.execSelect() ;
		    for ( ; results.hasNext() ; )
		    {
		      QuerySolution soln = results.nextSolution() ;
		      RDFNode x = soln.get("subject") ;       // Get a result variable by name.
		      Resource r = soln.getResource("predicate") ; // Get a result variable - must be a resource
		      Literal l = (Literal)soln.getLiteral("object") ;   // Get a result variable - must be a literal
		      System.out.println("Resultat requete : ");
		      System.out.println(x+" "+r+" "+l);
		    }
		  }
		  System.out.println("Fin requete : ");
	}
	
	
	
}