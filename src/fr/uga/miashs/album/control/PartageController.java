package fr.uga.miashs.album.control;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import fr.uga.miashs.album.model.Album;
import fr.uga.miashs.album.model.AppUser;
import fr.uga.miashs.album.service.AlbumService;
import fr.uga.miashs.album.service.AppUserService;
import fr.uga.miashs.album.service.PictureService;
import fr.uga.miashs.album.service.ServiceException;
import fr.uga.miashs.album.util.Pages;

@Named
@RequestScoped
public class PartageController  implements Serializable{
	
	private static final long serialVersionUID = 1L;

	@Inject
	private AppUserSession appUserSession;
	
	@Inject
	private AlbumService albumService;
	
	@Inject 
	private AppUserService appUserService;
	
	private Album album;
	
	private Map<AppUser, String> selectedUsers;
	
	@PostConstruct
	public void init() {
		Map<String,String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
		String idAlbum = params.get("idAlbum");
		album = getAlbumById(idAlbum);
		initialiserListeUser();
    }
	
	public String partagerAlbum(){
		Map<String,String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
		String idAlbum = params.get("idAlbum");
		album = getAlbumById(idAlbum);
		initialiserListeUser();	
		return Pages.partage;
	}
	
	
	public String modifierPartage() {
		System.out.println("mise a jour du partage");
		Map<String,String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
		String idAlbum = params.get("idAlbum"); 
		String actionPartage = params.get("action_partage");
		Long idUser = Long.parseLong(params.get("idUser"));
		album = getAlbumById(idAlbum);
		AppUser userToModify;
		this.appUserService.update(appUserSession.getConnectedUser());
		try {
			userToModify = getUserById(idUser);
			// si on partage l'album
			if ( actionPartage.equalsIgnoreCase("partager")) {
				album.addUserShared(userToModify);
				System.out.println("Ajout du partage avec " + userToModify.getFirstname() + userToModify.getLastname());
			}
			else {
				album.removeUserShared(userToModify);
				System.out.println("Supression du partage avec " + userToModify.getFirstname() + userToModify.getLastname());
			}			
		} catch (ServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
				
		this.album = albumService.update(album);
		initialiserListeUser();	
		return Pages.partage;
	}
	
	private void initialiserListeUser(){
		// récupération de tous les utilisateurs sans l'utilisateur courrant
		List<AppUser> listUsers = new ArrayList<AppUser>();
		List<AppUser> listPartage = new ArrayList<AppUser>();
		// liste des utilisateurs partagés
		
		for ( AppUser user : appUserService.listUserWithout(album.getOwner().getId()) ) {
			 listUsers.add(user);		 
		}
		
		for ( AppUser user : album.getSharedWith() ) {
			 listPartage.add(user);	
		}
		listUsers.removeAll(listPartage);
		selectedUsers = new HashMap<AppUser,String>();
		for ( AppUser user : listUsers ) {
			selectedUsers.put(user, "partager");
		}
		 
		for ( AppUser user : listPartage ) {
			selectedUsers.put(user, "annuler le partage");
		}
	}
	
	public AppUser getUserById(Long idUser) throws ServiceException {
		return appUserService.userWithId(idUser);
	}
	
	public Album getAlbumById(String idAlbum){
		try {
			return albumService.albumById(Long.parseLong(idAlbum));
		} catch (ServiceException e){
			e.printStackTrace();
		}
		return null;
	}
	
	// getters & setters
	public Album getAlbum() {
		return album;
	}
	
	public Map<AppUser, String> getSelectedUsers(){
		return selectedUsers;
	}

	public void setAlbum(Album album) {
		this.album = album;
	}
}
