package fr.uga.miashs.album.control;

import java.util.ArrayList;
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
	}	
	
	private ArrayList<String> selectedLieu;   
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

	private ArrayList<String> listeLieu;
	
	public Picture getPicture(){
		return this.picture;
	}
	
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
		
		System.out.println(this.selectedLieu.size() + " taille selected lieu " );
		//l'utilisateur a sélectionné des lieux à ajouter à la photo
		if (this.selectedLieu.size() > 0 ) {
			for (String lieu  : selectedLieu ){
				pictureAnnotationService.insertLieu(picture.getUri(), lieu);
			}
		}
	//	pictureAnnotationService.test();
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
		
		
	//	pictureAnnotationService.test();
		return Pages.tag;
	}
}
