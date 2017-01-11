package fr.uga.miashs.album.control;

import java.util.Map;

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
public class taggingController {

	@Inject
	private PictureService pictureService;
	
	private Picture picture;
	
	@Inject
	private PictureAnnotationService pictureAnnotationService;
	
	public Picture getPicture(){
		return this.picture;
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
		pictureAnnotationService.test();
		return Pages.tag;
	}
}
