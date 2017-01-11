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

//
//@RequestScoped

//@SessionScoped
//@ViewScoped
//@ViewScoped
@Named
@RequestScoped
public class PartageController  implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Inject
	private AlbumService albumService;
	
	@Inject 
	private AppUserService appUserService;
	
	private Album album;
	
	private Map<AppUser, String> selectedUsers;
	
	public Album getAlbum() {
		return album;
	}
	
	public Map<AppUser, String> getSelectedUsers(){
		return selectedUsers;
	}

	public void setAlbum(Album album) {
		this.album = album;
	}
	
	@PostConstruct
	public void init() {
		System.out.println("postconstruct 2");
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
		System.out.println("mise a jour");
		Map<String,String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
		String idAlbum = params.get("idAlbum"); 
		String actionPartage = params.get("action_partage");
		System.out.println(idAlbum);
		Long idUser = Long.parseLong(params.get("idUser"));
		album = getAlbumById(idAlbum);
		System.out.println(idUser);
		AppUser userToModify;
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
		System.out.println("mise a jour");
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
			 System.out.println("----------user without current user" + user.getFirstname() + user.getLastname());			 
		}
		for ( AppUser user : album.getSharedWith() ) {
			 listPartage.add(user);
			 System.out.println("----------user shared with current user" + user.getFirstname() + user.getLastname());	
		}
		listUsers.removeAll(listPartage);
		selectedUsers = new HashMap<AppUser,String>();
		for ( AppUser user : listUsers ) {
			selectedUsers.put(user, "partager");
			System.out.println(user.getLastname() + "non t shar");
		}
		 
		for ( AppUser user : listPartage ) {
			selectedUsers.put(user, "annuler le partage");
			System.out.println(user.getLastname() + "shar");
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
	
	
}
