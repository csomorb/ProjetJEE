package fr.uga.miashs.album.model;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;

@Entity
@NamedQueries({
    @NamedQuery(name="Album.findAllOwned",
                query="SELECT a FROM Album a WHERE a.owner=:owner"),
    @NamedQuery(name="Album.findById",
    			query="SELECT a FROM Album a WHERE  a.id=:id"), 
    @NamedQuery(name="Album.findAllNotOwned",
                query="SELECT a FROM Album a WHERE a.owner <>:owner"),
})
public class Album {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	@NotNull
	private String title;
	
	private String description;
	
	@NotNull
	@ManyToOne(cascade = CascadeType.ALL)
	private AppUser owner;
	
	@ManyToMany
	private Set<AppUser> sharedWith;
	

	@OneToMany(mappedBy="album", cascade = CascadeType.ALL)
	private Set<Picture> pictures;

	protected Album() {
	}
	
	public Album(AppUser owner) {
		this.owner=owner;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public AppUser getOwner() {
		return owner;
	}

	public void setOwner(AppUser owner) {
		this.owner = owner;
	}

	public long getId() {
		return id;
	}

	public Set<AppUser> getSharedWith() {
		return sharedWith;
	}

	public Set<Picture> getPictures() {
		return pictures;
	}
	
	
	public void setSharedWith(AppUser user){
		this.sharedWith.add(user);
	/*	for ( AppUser usert : sharedWith ) {
			 System.out.println("----------liste-utilisateur--------" + usert.getFirstname() + usert.getLastname());	
		}*/
	}
	
	public void addUserShared(AppUser user){
		this.setSharedWith(user);
	/*	for ( AppUser usert : sharedWith ) {
			 System.out.println("----------liste-utilisateur--------" + usert.getFirstname() + usert.getLastname());	
		}*/
	}
	
	public void removeUserShared(AppUser user){
		this.sharedWith.remove(user);
	/*	for ( AppUser usert : sharedWith ) {
			 System.out.println("----------liste-utilisateur--------" + usert.getFirstname() + usert.getLastname());	
		}*/
	}
	
	public void removePicture(Picture p){
		this.pictures.remove(p);
	}
	
	/*
	public void setPictures(Set<Picture> p) {
		this.pictures = p;
	}*/
}
