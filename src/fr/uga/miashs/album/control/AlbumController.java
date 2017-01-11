package fr.uga.miashs.album.control;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import javax.servlet.http.Part;

import fr.uga.miashs.album.model.Album;
import fr.uga.miashs.album.model.AppUser;
import fr.uga.miashs.album.model.Picture;
import fr.uga.miashs.album.service.AlbumService;
import fr.uga.miashs.album.service.AppUserService;
import fr.uga.miashs.album.service.PictureService;
import fr.uga.miashs.album.service.ServiceException;
import fr.uga.miashs.album.util.Pages;

@Named
@RequestScoped
public class AlbumController {

	@Inject
	private AppUserSession appUserSession;
	
	@Inject
	private AlbumService albumService;
	
	@Inject
	private PictureService pictureService;
	
	@Inject 
	private AppUserService appUserService;

	private Part zip;
	
	private Album album;
	
	@PostConstruct
	public void init() {
		System.out.println("postconstruct");
        listeImage = listPictureAlbum();
    }
	
	private List<Picture> listeImage;
	
	public List<Picture> getListeImage() {
		return listeImage;
	}
	
	public Album getAlbum() {
		if (album==null) {
			album = new Album(appUserSession.getConnectedUser());
		}
		return album;
	}
	
	
	public String createAlbum() {
		try {
		//	albumService.create(album);
			albumService.create2(album,zip);
		} catch (ServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return Pages.list_album;
	}
	
	public List<Album> getListAlbumOwnedByCurrentUser() {
		try {
			return albumService.listAlbumOwnedBy(appUserSession.getConnectedUser());
		} catch (ServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public List<Picture> listPictureAlbum(){
		try {
			return pictureService.listPictureAlbum(album);
		} catch (ServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	
	public String afficherAlbum(){
		Map<String,String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
		String idAlbum = params.get("idAlbum");
		album = getAlbumById(idAlbum);
		this.listeImage = listPictureAlbum();
		
		return Pages.album;
	}
	
	public String changerDescription(){
		this.listeImage = listPictureAlbum();
		Map<String,String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
		String idAlbum = params.get("idAlbum");
		album = getAlbumById(idAlbum);
		
		return Pages.album;
	}
	
	public Album getAlbumById(String idAlbum){
		try {
			return albumService.albumById(Long.parseLong(idAlbum));
		} catch (ServiceException e){
			e.printStackTrace();
		}
		return null;
	}


	public Part getZip() {
		return zip;
	}


	public void setZip(Part zip) {
		this.zip = zip;
	}
}
