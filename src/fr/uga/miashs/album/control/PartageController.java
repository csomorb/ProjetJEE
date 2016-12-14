package fr.uga.miashs.album.control;

import java.util.ArrayList;
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
public class PartageController {

	@Inject
	private AppUserSession appUserSession;
	
	@Inject
	private AlbumService albumService;
	
	@Inject
	private PictureService pictureService;
	
	@Inject 
	private AppUserService appUserService;
	
	private Album album;
	
	private String[] selectedUsers;
	private List<String[]> users;
	
	public String[] getSelectedUsers() {
		return selectedUsers;
	}

	public void setSelectedUsers(String[] selectedUsers) {
		this.selectedUsers = selectedUsers;
	}

	public List<String[]> getUsers() {
		return users;
	}

	public void setUsers(List<String[]> users) {
		this.users = users;
	}
	
	public Album getAlbum() {
		return album;
	}

	public void setAlbum(Album album) {
		this.album = album;
	}

	public String partagerAlbum(){
		Map<String,String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
		String idAlbum = params.get("idAlbum");
		album = getAlbumById(idAlbum);
	//	selectedUsers = new ArrayList<String[]>();
		initialiserListeUser();
		
		return Pages.partage;
	}
	
	public void validerPartage(){
		System.out.print("coucou");
		for (String tmp : selectedUsers){
			System.out.println(tmp);
		}
	}

/*	public String validerPartage(){
		
	}
*/	
	private void initialiserListeUser(){
	/*	List<AppUser> userNotSharedTmp = appUserService.listUsers();
		userNotSharedTmp.remove(album.getOwner());
		userNotSharedTmp.removeAll(userShared);
		return userNotSharedTmp;*/ 
		//List<String[]> liste = new ArrayList<String[]>();
		this.users = new ArrayList<String[]>();
		for (AppUser user : appUserService.listUsers()) {
			if (user.getId() != album.getOwner().getId() /*&& ! album.getSharedWith().contains(user)*/){
				
				String[] tmp = new String[3];
				tmp[0] = Long.toString(user.getId());
				tmp[1] = user.getFirstname();
				tmp[2] = user.getLastname();
				users.add(tmp);
			//	liste.add(tmp);
			}		
		}
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
