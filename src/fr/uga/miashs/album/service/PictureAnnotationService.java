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
	
	// ajoute le lieu ou a été prise la photo
	public void insertLieu(URI pURI, String lieu){
		String str = lieu.replace(' ', '_');
		String req = "{<" + pURI.toString() + "> <http://www.ILoveWebSemantic.com/photoApp#photoPriseA> <http://www.ILoveWebSemantic.com/photoApp#"+str+"> .}";
		UpdateRequest request = UpdateFactory.create("PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>"+
				"INSERT DATA" + req );
		UpdateProcessor up = UpdateExecutionFactory.createRemote(request, "http://localhost:3030/ALBUM/update");
		up.execute();
	}
	
	
	// ajoute l'objet présent la photo
		public void insertObjet(URI pURI, String objet){
			String str = objet.replace(' ', '_');
			String req = "{<" + pURI.toString() + "> <http://www.ILoveWebSemantic.com/photoApp#objetPresent> <http://www.ILoveWebSemantic.com/photoApp#"+str+"> .}";
			UpdateRequest request = UpdateFactory.create("PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>"+
					"INSERT DATA" + req );
			UpdateProcessor up = UpdateExecutionFactory.createRemote(request, "http://localhost:3030/ALBUM/update");
			up.execute();
		}
	
	// ajoute la date de la prise de vue de la photo     
	public void insertDatePriseDeVue(URI pURI, String date_s){
		String req = "{<" + pURI.toString() + "> <http://www.ILoveWebSemantic.com/photoApp#datePriseDeVue> \""+date_s+"\"^^xsd:dateTime .}";
		UpdateRequest request = UpdateFactory.create("PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> prefix xsd: <http://www.w3.org/2001/XMLSchema#> "+
				"INSERT DATA" + req );
		UpdateProcessor up = UpdateExecutionFactory.createRemote(request, "http://localhost:3030/ALBUM/update");
		up.execute();
	}
	
	// ajoute la personne présente sur la photo
		public void insertPersonne(URI pURI, String p){
			String str = p.replace(' ', '_');
			String req = "{<" + pURI.toString() + "> <http://www.ILoveWebSemantic.com/photoApp#estPresent> <http://www.ILoveWebSemantic.com/photoApp#"+str+"> .}";
			UpdateRequest request = UpdateFactory.create("PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>"+
					"INSERT DATA" + req );
			UpdateProcessor up = UpdateExecutionFactory.createRemote(request, "http://localhost:3030/ALBUM/update");
			up.execute();
		}
	
	// retourne une liste de tous les lieux
	public ArrayList<String> tousLesLieux(){
		ArrayList<String> liste = new ArrayList<String>();
		Query query = QueryFactory.create("PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> SELECT ?subject ?predicate ?object WHERE {  ?subject a <http://www.ILoveWebSemantic.com/photoApp#Lieu> }");
		  try (QueryExecution qexec = QueryExecutionFactory.sparqlService("http://localhost:3030/ALBUM/sparql",query)) {
			System.out.println("Execution requete de la liste des leiyx: ");
		    ResultSet results = qexec.execSelect() ;
		    for ( ; results.hasNext() ; )
		    {
		      QuerySolution soln = results.nextSolution() ;
		      RDFNode x = soln.get("subject") ;
		      if (x.toString().contains("http://www.ILoveWebSemantic.com/photoApp")) {
		    	  String str = x.toString().substring(41).replace('_',' ');
		    	  liste.add(str);
		      }
		    }
		  }
		  System.out.println("Fin requete : ");
		return liste;
	}
	
	// retourne une liste des lieux de l'image
		public ArrayList<String> tousLesLieuxPicture(String uri){
			ArrayList<String> liste = new ArrayList<String>();
			Query query = QueryFactory.create("PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> SELECT ?subject ?predicate ?object WHERE {  <"+uri+"> <http://www.ILoveWebSemantic.com/photoApp#photoPriseA> ?object }");
			  try (QueryExecution qexec = QueryExecutionFactory.sparqlService("http://localhost:3030/ALBUM/sparql",query)) {
				System.out.println("Execution requete de la liste des leiyx: ");
			    ResultSet results = qexec.execSelect() ;
			    for ( ; results.hasNext() ; )
			    {
			      QuerySolution soln = results.nextSolution() ;
			      RDFNode x = soln.get("object") ;
			      if (x.toString().contains("http://www.ILoveWebSemantic.com/photoApp")) {
			    	  String str = x.toString().substring(41).replace('_',' ');
			    	  liste.add(str);
			      }
			    }
			  }
			  System.out.println("Fin requete : ");
			return liste;
		}
	
	// retourne une liste de tous les objets
		public ArrayList<String> tousLesObjetsPicture(String uri){
			ArrayList<String> liste = new ArrayList<String>();
			Query query = QueryFactory.create("PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> SELECT ?subject ?predicate ?object WHERE {  <"+uri+"> <http://www.ILoveWebSemantic.com/photoApp#objetPresent> ?object }");
			  try (QueryExecution qexec = QueryExecutionFactory.sparqlService("http://localhost:3030/ALBUM/sparql",query)) {
				System.out.println("Execution requete de la liste des objets: ");
			    ResultSet results = qexec.execSelect() ;
			    for ( ; results.hasNext() ; )
			    {
			      QuerySolution soln = results.nextSolution() ;
			      RDFNode x = soln.get("object") ;
			      if (x.toString().contains("http://www.ILoveWebSemantic.com/photoApp")) {
			    	  String str = x.toString().substring(41).replace('_',' ');
			    	  liste.add(str);
			      }
			    }
			  }
			  System.out.println("Fin requete : ");
			return liste;
		}
		
		// retourne une liste des objets de l'image
				public ArrayList<String> tousLesObjets(){
					ArrayList<String> liste = new ArrayList<String>();
					Query query = QueryFactory.create("PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> SELECT ?subject ?predicate ?object WHERE {  ?subject a <http://www.ILoveWebSemantic.com/photoApp#Objet> }");
					  try (QueryExecution qexec = QueryExecutionFactory.sparqlService("http://localhost:3030/ALBUM/sparql",query)) {
						System.out.println("Execution requete de la liste des objets: ");
					    ResultSet results = qexec.execSelect() ;
					    for ( ; results.hasNext() ; )
					    {
					      QuerySolution soln = results.nextSolution() ;
					      RDFNode x = soln.get("subject") ;
					      if (x.toString().contains("http://www.ILoveWebSemantic.com/photoApp")) {
					    	  String str = x.toString().substring(41).replace('_',' ');
					    	  liste.add(str);
					      }
					    }
					  }
					  System.out.println("Fin requete : ");
					return liste;
				}
	
	// retourne une liste de tous les personnes
		public ArrayList<String> tousLesPersonnes(){
			ArrayList<String> liste = new ArrayList<String>();
			Query query = QueryFactory.create("PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> SELECT ?subject ?predicate ?object WHERE {  ?subject a <http://www.ILoveWebSemantic.com/photoApp#Personne> }");
			  try (QueryExecution qexec = QueryExecutionFactory.sparqlService("http://localhost:3030/ALBUM/sparql",query)) {
				System.out.println("Execution requete de la liste des personnes: ");
			    ResultSet results = qexec.execSelect() ;
			    for ( ; results.hasNext() ; )
			    {
			      QuerySolution soln = results.nextSolution() ;
			      RDFNode x = soln.get("subject") ;
			      if (x.toString().contains("http://www.ILoveWebSemantic.com/photoApp")) {
			    	  String str = x.toString().substring(41).replace('_',' ');
			    	  liste.add(str);
			      }
			    }
			  }
			  System.out.println("Fin requete : ");
			return liste;
		}
		
		// retourne une liste des personnes de l'image
				public ArrayList<String> tousLesPersonnesPicture(String uri){
					ArrayList<String> liste = new ArrayList<String>();
					Query query = QueryFactory.create("PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> SELECT ?subject ?predicate ?object WHERE {  <"+uri+"> <http://www.ILoveWebSemantic.com/photoApp#estPresent> ?object }");
					  try (QueryExecution qexec = QueryExecutionFactory.sparqlService("http://localhost:3030/ALBUM/sparql",query)) {
						System.out.println("Execution requete de la liste des personnes: ");
					    ResultSet results = qexec.execSelect() ;
					    for ( ; results.hasNext() ; )
					    {
					      QuerySolution soln = results.nextSolution() ;
					      RDFNode x = soln.get("object") ;
					      if (x.toString().contains("http://www.ILoveWebSemantic.com/photoApp")) {
					    	  String str = x.toString().substring(41).replace('_',' ');
					    	  liste.add(str);
					      }
					    }
					  }
					  System.out.println("Fin requete : ");
					return liste;
				}
	
	// retourne une liste des images du résultat de la requete
		public ArrayList<String> recherche(String req){
			ArrayList<String> liste = new ArrayList<String>();
			Query query = QueryFactory.create(req);
			try (QueryExecution qexec = QueryExecutionFactory.sparqlService("http://localhost:3030/ALBUM/sparql",query)) {
				System.out.println("Execution requete de recherche: ");
			    ResultSet results = qexec.execSelect() ;
			    for ( ; results.hasNext() ; )
			    {
			      QuerySolution soln = results.nextSolution() ;
			      RDFNode x = soln.get("subject") ;
			    	  System.out.println(x.toString());
			    	  liste.add(x.toString());
			      
			    }
			  }
			  System.out.println("Fin requete : ");
			return liste;
		}
		
		// retourne la liste des images des personnes, des objets et de lieux de la requette
		public ArrayList<String> recherchePersonnesEtLieu(ArrayList<String> selectedPersonne, ArrayList<String> selectedLieu, ArrayList<String> selectedObjet){
			ArrayList<String> liste = new ArrayList<String>();
			String req = " SELECT ?subject ?predicate ?object WHERE {  ";
			for (String st : selectedPersonne){
				String str = st.replace(' ', '_');
				req+= " ?subject <http://www.ILoveWebSemantic.com/photoApp#estPresent> <http://www.ILoveWebSemantic.com/photoApp#"+str+ "> . ";
			}
			for (String st : selectedLieu){
				String str = st.replace(' ', '_');
				req+= " ?subject <http://www.ILoveWebSemantic.com/photoApp#photoPriseA> <http://www.ILoveWebSemantic.com/photoApp#"+str+ "> . ";
			}
			for (String st : selectedObjet){
				String str = st.replace(' ', '_');
				req+= " ?subject <http://www.ILoveWebSemantic.com/photoApp#objetPresent> <http://www.ILoveWebSemantic.com/photoApp#"+str+ "> . ";
			}
			req += " } ";	
			System.out.println(req);
			Query query = QueryFactory.create(req);
			try (QueryExecution qexec = QueryExecutionFactory.sparqlService("http://localhost:3030/ALBUM/sparql",query)) {
				System.out.println("Execution requete de recherche: ");
			    ResultSet results = qexec.execSelect() ;
			    for ( ; results.hasNext() ; )
			    {
			      QuerySolution soln = results.nextSolution() ;
			      RDFNode x = soln.get("subject") ;
			    	  System.out.println(x.toString());
			    	  liste.add(x.toString());
			      
			    }
			  }
			  System.out.println("Fin requete : ");
			return liste;
		}
	
/*	public void test() {
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
	}*/
	
	
	
}