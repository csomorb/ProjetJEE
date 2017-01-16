package fr.uga.miashs.album.control;

import java.util.ArrayList;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import fr.uga.miashs.album.model.Picture;
import fr.uga.miashs.album.service.PictureAnnotationService;
import fr.uga.miashs.album.service.PictureService;
import fr.uga.miashs.album.util.Pages;

@Named
@RequestScoped
public class RechercheController {

	@Inject
	private PictureService pictureService;
	
	@Inject
	private PictureAnnotationService pictureAnnotationService;
	
	private ArrayList<Picture> pictureListe;
	
	private ArrayList<String> selectedPersonne;
	
	private ArrayList<String> listePersonne;
	
	private String rechercheArea;
	
	private ArrayList<String> selectedLieu;
	
	private ArrayList<String> listeLieu;
	
	@PostConstruct
	public void init(){
		rechercheArea = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> " +
                        "prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#> "+
                        "prefix owl: <http://www.w3.org/2002/07/owl#> " +
                        "prefix xsd: <http://www.w3.org/2001/XMLSchema#> " +
                        "prefix myo: <http://www.ILoveWebSemantic.com/photoApp#> " +
                        "SELECT ?subject " +
                                " WHERE { " +
                                      " ?subject myo:datePriseDeVue ?object " +
                                       " } ";
		this.listePersonne = pictureAnnotationService.tousLesPersonnes();
		this.listeLieu = pictureAnnotationService.tousLesLieux();
	}
	
	public String rech(){
		this.pictureListe = new ArrayList<Picture>();
		return Pages.recherche;		
	}
	
	public String rechercherPersonnesEtLieu(){
		
		ArrayList<String> liste = this.pictureAnnotationService.recherchePersonnesEtLieu(selectedPersonne,selectedLieu);
		this.pictureListe = pictureService.listPictureURIList(liste);
		return Pages.recherche;
	}
	
	public String rechercher(){
	//	System.out.print(this.rechercheArea);
		ArrayList<String> liste = this.pictureAnnotationService.recherche(this.rechercheArea);
		this.pictureListe = pictureService.listPictureURIList(liste);
		return Pages.recherche;
	}
	
	
	//Getters & Setters
	
	public String getRechercheArea() {
		return rechercheArea;
	}

	public void setRechercheArea(String rechercheArea) {
		this.rechercheArea = rechercheArea;
	}

	public ArrayList<Picture> getPictureListe() {
		return pictureListe;
	}

	public void setPictureListe(ArrayList<Picture> pictureListe) {
		this.pictureListe = pictureListe;
	}

	public ArrayList<String> getSelectedPersonne() {
		return selectedPersonne;
	}

	public void setSelectedPersonne(ArrayList<String> selectedPersonne) {
		this.selectedPersonne = selectedPersonne;
	}

	public ArrayList<String> getListePersonne() {
		return listePersonne;
	}

	public void setListePersonne(ArrayList<String> listePersonne) {
		this.listePersonne = listePersonne;
	}

	public ArrayList<String> getSelectedLieu() {
		return selectedLieu;
	}

	public void setSelectedLieu(ArrayList<String> selectedLieu) {
		this.selectedLieu = selectedLieu;
	}

	public ArrayList<String> getListeLieu() {
		return listeLieu;
	}

	public void setListeLieu(ArrayList<String> listeLieu) {
		this.listeLieu = listeLieu;
	}
	
	
}
