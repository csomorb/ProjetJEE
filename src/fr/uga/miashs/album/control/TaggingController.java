package fr.uga.miashs.album.control;

import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import fr.uga.miashs.album.model.Picture;
import fr.uga.miashs.album.service.PictureAnnotationService;
import fr.uga.miashs.album.service.PictureService;
import fr.uga.miashs.album.service.ServiceException;
import fr.uga.miashs.album.util.Pages;

@Named
@RequestScoped
public class TaggingController {

	@Inject
	private PictureService pictureService;
	
	private Picture picture;
	
	@Inject
	private PictureAnnotationService pictureAnnotationService;
	
	@PostConstruct
	public void init(){
		this.listeLieu = pictureAnnotationService.tousLesLieux();
		this.listePersonne = pictureAnnotationService.tousLesPersonnes();
		this.listeObjet = pictureAnnotationService.tousLesObjets();
	}	
	
	private ArrayList<String> selectedLieu; 
	
	private ArrayList<String> selectedPersonne;
	
	private Date date;

	private ArrayList<String> selectedObjet;
	
	private ArrayList<String> listeObjet;
	
	private ArrayList<String> listeLieu;
	
	private ArrayList<String> listePersonne;
	
	
	
	public String taguer(){
		Map<String,String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
		String idImage = params.get("photoId");
		System.out.println(idImage + " id de l'image ");
		try {
			this.picture = (Picture) pictureService.pictureById(Long.parseLong(idImage));
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//l'utilisateur a sélectionné des lieux à ajouter à la photo
		if (this.selectedLieu.size() > 0 ) {
			for (String lieu  : selectedLieu ){
				pictureAnnotationService.insertLieu(picture.getUri(), lieu);
			}
		}
		
		if (this.date != null){ 
			String mois = date.toString().substring(4, 7);
			String m;
			switch (mois) {
				case "Jan" : m = "01"; break;
				case "Feb" : m = "02"; break;
				case "Mar" : m = "03"; break;
				case "Apr" : m = "04"; break;
				case "May" : m = "05"; break;
				case "Jun" : m = "06"; break;
				case "Jul" : m = "07"; break;
				case "Aug" : m = "08"; break;
				case "Sep" : m = "09"; break;
				case "Oct" : m = "10"; break;
				case "Nov" : m = "11"; break;
				default :  m = "12"; break;
			}
			String date_s = date.toString().substring(24)+"-"+m+"-"+date.toString().substring(8,10)+"T"+date.toString().substring(11, 19);
			System.out.println(date_s);
			pictureAnnotationService.insertDatePriseDeVue(picture.getUri(), date_s);
		}
		//l'utilisateur a sélectionné des personnes à ajouter à la photo
		if (this.selectedPersonne.size() > 0 ) {
			for (String per  : selectedPersonne ){
				pictureAnnotationService.insertPersonne(picture.getUri(), per);
			}
		}
		
		//l'utilisateur a sélectionné des objets à ajouter à la photo
		if (this.selectedObjet.size() > 0 ){
			for (String ob  : selectedObjet ){
				pictureAnnotationService.insertObjet(picture.getUri(), ob);
			} 
		}
				
		return Pages.tag;
	}
	
	public String tagPhoto(){
		Map<String,String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
		String idAlbum = params.get("photoId");
		System.out.println(idAlbum + " id de l'image ");
		try {
			this.picture = (Picture) pictureService.pictureById(Long.parseLong(idAlbum));
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.selectedLieu = this.pictureAnnotationService.tousLesLieuxPicture(picture.getUri().toString());
		this.selectedObjet = this.pictureAnnotationService.tousLesObjetsPicture(picture.getUri().toString());
		this.selectedPersonne = this.pictureAnnotationService.tousLesPersonnesPicture(picture.getUri().toString());
		return Pages.tag;
	}
	
	// getters et setters
	
    public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
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

	public ArrayList<String> getSelectedObjet() {
		return selectedObjet;
	}

	public void setSelectedObjet(ArrayList<String> selectedObjet) {
		this.selectedObjet = selectedObjet;
	}

	public ArrayList<String> getListeObjet() {
		return listeObjet;
	}

	public void setListeObjet(ArrayList<String> listeObjet) {
		this.listeObjet = listeObjet;
	}

	public Picture getPicture(){
		return this.picture;
	}
}
