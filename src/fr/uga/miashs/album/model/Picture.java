package fr.uga.miashs.album.model;

import java.net.URI;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.validation.constraints.NotNull;

@Entity
@NamedQueries({
    @NamedQuery(name="Picture.findAlbum",
                query="SELECT p FROM Picture p WHERE p.album=:album"),
    @NamedQuery(name="Picture.findAllPicure",
    			query="SELECT * FROM Picture"),
    @NamedQuery(name="Picture.findById",
				query="SELECT p FROM Picture p WHERE p.id=:id")
})
public class Picture {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	@ManyToOne
	private Album album;
	
	@NotNull
	private String title;
	
	@NotNull
	private URI uri;
	
	@NotNull
	private String localfile;
	
/*	public Picture(Album album){
		this.album = album;
	}
*/
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Album getAlbum() {
		return album;
	}

	public void setAlbum(Album album) {
		this.album = album;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public URI getUri() {
		return uri;
	}

	public void setUri(URI uri) {
		this.uri = uri;
	}

	public String getLocalfile() {
		return localfile;
	}

	public void setLocalfile(String localfile) {
		this.localfile = localfile;
	}
	
	
}
