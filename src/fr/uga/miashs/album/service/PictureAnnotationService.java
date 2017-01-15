package fr.uga.miashs.album.service;

import java.net.URI;
import java.util.ArrayList;

import org.apache.jena.query.*;
import org.apache.jena.rdf.model.*;
import org.apache.jena.update.*;

public class PictureAnnotationService {

	
	// ajoute l'image pURI a fuseki
	public void insertPicture(URI pURI){
		String req = "{<" + pURI.toString() + "> a <http://www.ILoveWebSemantic.com/photoApp#Picture> .}";
		UpdateRequest request = UpdateFactory.create("PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>"+
				"INSERT DATA" + req );
		UpdateProcessor up = UpdateExecutionFactory.createRemote(request, "http://localhost:3030/ALBUM/update");
		up.execute();
	}
	
	
	public void insertLieu(URI pURI, String lieu){
		String req = "{<" + pURI.toString() + "> <http://www.ILoveWebSemantic.com/photoApp#photoPriseA> <http://www.ILoveWebSemantic.com/photoApp#"+lieu+"> .}";
		UpdateRequest request = UpdateFactory.create("PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>"+
				"INSERT DATA" + req );
				//{"+
				//" <http://imss.univ-grenoble-alpes.fr/ns/album#Selfie> rdfs:subClassOf <http://imss.univ-grenoble-alpes.fr/ns/album#Picture> ."+
				//" <http://imss.univ-grenoble-alpes.fr/ns/album#picture321> a <http://imss.univ-grenoble-alpes.fr/ns/album#Selfie> .}");

		UpdateProcessor up = UpdateExecutionFactory.createRemote(request, "http://localhost:3030/ALBUM/update");
		up.execute();
	}
	
	public ArrayList<String> tousLesLieux(){
		ArrayList<String> liste = new ArrayList<String>();
		Query query = QueryFactory.create("prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#> prefix owl: <http://www.w3.org/2002/07/owl#> prefix xsd: <http://www.w3.org/2001/XMLSchema#> prefix myo: <http://www.ILoveWebSemantic.com/photoApp#> SELECT ?subject ?predicate ?object WHERE {  ?subject rdfs:subClassOf myo:lieu }");
		  try (QueryExecution qexec = QueryExecutionFactory.sparqlService("http://localhost:3030/ALBUM/sparql",query)) {
			  System.out.println("Execution requete : ");
		    ResultSet results = qexec.execSelect() ;
		    for ( ; results.hasNext() ; )
		    {
		      QuerySolution soln = results.nextSolution() ;
		      RDFNode x = soln.get("subject") ;       // Get a result variable by name.
		   //   Resource r = soln.getResource("predicate") ; // Get a result variable - must be a resource
		   //   Literal l = (Literal)soln.getLiteral("object") ;   // Get a result variable - must be a literal
		   //   System.out.println("Resultat requete : ");
		      if (x.toString().contains("http://www.ILoveWebSemantic.com/photoApp")) {
		    	  liste.add(x.toString().substring(41));
			      System.out.println(x.toString().substring(41));  
		      }
		      
		    }
		  }
		  System.out.println("Fin requete : ");
		return liste;
	}
	
	
	public void test() {
		System.out.println("enter test");
		
		// SPARQL Update
		UpdateRequest request = UpdateFactory.create("PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>"+
													"INSERT DATA {"+
													" <http://imss.univ-grenoble-alpes.fr/ns/album#Selfie> rdfs:subClassOf <http://imss.univ-grenoble-alpes.fr/ns/album#Picture> ."+
													" <http://imss.univ-grenoble-alpes.fr/ns/album#picture321> a <http://imss.univ-grenoble-alpes.fr/ns/album#Selfie> .}");

		UpdateProcessor up = UpdateExecutionFactory.createRemote(request, "http://localhost:3030/ALBUM/update");
		up.execute();
		
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