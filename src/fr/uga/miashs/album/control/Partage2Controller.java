package fr.uga.miashs.album.control;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
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
public class Partage2Controller {
	
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
		Long idUser = Long.parseLong(params.get("idUser"));
		album = getAlbumById(idAlbum);
		AppUser userToModify;
		try {
			userToModify = getUserById(idUser);
			album.addUserShared(userToModify);
			
		} catch (ServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		albumService.update(album);
		System.out.println("mise a jour");
		initialiserListeUser();	
		return Pages.partage;
	}
	
	private void initialiserListeUser(){
		// récupération de tous les utilisateurs sans l'utilisateur courrant
		List<AppUser> listUsers = new ArrayList<AppUser>();
		List<AppUser> listPartage = new ArrayList<AppUser>();
		for ( AppUser user : appUserService.listUserWithout(album.getOwner().getId()) ) {
			 listUsers.add(user);
		}
		for ( AppUser user : listPartage ) {
			 listPartage.add(user);
		}
		listUsers.removeAll(listPartage);
		selectedUsers = new HashMap<AppUser,String>();
		for ( AppUser user : listUsers ) {
			selectedUsers.put(user, "partager");
			System.out.println(user.getLastname() + "non t shar");
		}
		 
		for ( AppUser user : listPartage ) {
			selectedUsers.put(user, "ne pas abboner");
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
