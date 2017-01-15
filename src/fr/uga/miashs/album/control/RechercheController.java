package fr.uga.miashs.album.control;

import java.util.ArrayList;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import fr.uga.miashs.album.model.Picture;
import fr.uga.miashs.album.service.PictureAnnotationService;
import fr.uga.miashs.album.service.PictureService;

@Named
@RequestScoped
public class RechercheController {

	@Inject
	private PictureService pictureService;
	
	private ArrayList<Picture> pictureListe;
	
	@Inject
	private PictureAnnotationService pictureAnnotationService;

	public ArrayList<Picture> getPictureListe() {
		return pictureListe;
	}

	public void setPictureListe(ArrayList<Picture> pictureListe) {
		this.pictureListe = pictureListe;
	}
	
	
	
}
