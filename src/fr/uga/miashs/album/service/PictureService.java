package fr.uga.miashs.album.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryNotEmptyException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Set;

import javax.persistence.Query;

import fr.uga.miashs.album.model.Album;
import fr.uga.miashs.album.model.AppUser;
import fr.uga.miashs.album.model.Picture;

public class PictureService extends JpaService<Long,Picture>{

	public Picture create(Picture p, Album a, Path pa, String filename) throws ServiceException {
		
	//	p.setAlbum(getEm().merge(getEm().merge(p.getAlbum())));
		
		p.setAlbum(a);
	//	System.out.println(p.getAlbum().getOwner().getEmail());
		p.setTitle(filename);
		p.setLocalfile("resources/img/"+a.getOwner().getId()+"/"+a.getId()+"/"+ filename );
		System.out.println("phase2"+pa.toString());
		p.setUri(pa.toUri());
		super.create(p);
		return p;
	}
	
	public List<Picture> listPictureAlbum(Album a) throws ServiceException {
		//La requete est définie dans la classe Picture grâce à une annotation
		Query query = getEm().createNamedQuery("Picture.findAlbum");
		query.setParameter("album", getEm().merge(a));
		return query.getResultList();
	}
	
	
	public Picture pictureById(long id) throws ServiceException {
		Query query = getEm().createNamedQuery("Picture.findById");
		query.setParameter("id", id);
		return (Picture) query.getSingleResult();
		
	}
	
	public void deletePictureById(Long id) {
		// supprimer du disque
	/*	try {
			Picture p = pictureById(id);
			// construction du chemin
			System.out.println("suppression du fichier " + p.getLocalfile() + " du disque dur");
			File image = new File(p.getLocalfile());
			image.delete();
		} catch (ServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		try {
			Picture p = pictureById(id);
			System.out.println("suppression du fichier " + p.getLocalfile() + " du disque dur");
			System.out.println("suppression du fichier " + p.getUri() + " du disque dur");
			Path path = Paths.get(p.getLocalfile());
			Path path2 = Paths.get(p.getUri());
		    Files.delete(path2);
		} catch (NoSuchFileException x) {
		    System.err.format(" no such " + " file or directory");
		} catch (DirectoryNotEmptyException x) {
		    System.err.format("directory not empty");
		} catch (IOException x) {
		    // File permission problems are caught here.
		    System.err.println(x);
		} catch (ServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		// supprimer de la bdd
		super.deleteById(id);
	}
	
	public List<Picture> getListPicture() throws ServiceException {
		//La requete est définie dans la classe Picture grâce à une annotation
		Query query = getEm().createNamedQuery("Picture.findAllPicture");
	///	query.setParameter();
		return query.getResultList();
	}
	
}
