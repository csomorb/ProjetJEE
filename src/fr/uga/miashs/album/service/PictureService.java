package fr.uga.miashs.album.service;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.DirectoryNotEmptyException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.persistence.Query;

import fr.uga.miashs.album.model.Album;
import fr.uga.miashs.album.model.AppUser;
import fr.uga.miashs.album.model.Picture;

public class PictureService extends JpaService<Long,Picture>{

	PictureAnnotationService pictureAnnotationService;
	
	PictureService(){
		this.pictureAnnotationService = new PictureAnnotationService();
	}
	
	public Picture create(Picture p, Album a, Path pa, String filename) throws ServiceException {
		
		p.setAlbum(a);
		p.setTitle(filename);
		System.out.println(filename + " nom du fichier ");
		p.setLocalfile("resources/img/"+a.getOwner().getId()+"/"+a.getId()+"/"+ filename );
		System.out.println("phase2"+pa.toString());
		p.setUri(pa.toUri());
		pictureAnnotationService.insertPicture(pa.toUri()); 
		super.create(p);
		return p;
	}
	
	public List<Picture> listPictureAlbum(Album a) throws ServiceException {
		//La requete est définie dans la classe Picture grâce à une annotation
		Query query = getEm().createNamedQuery("Picture.findAlbum");
		query.setParameter("album", getEm().merge(a));
		return query.getResultList();
	}
	
	public ArrayList<Picture> listPictureURIList(ArrayList<String> liste){
		ArrayList<Picture> list = new ArrayList<Picture>();
		for (String str : liste){
			System.out.println("recherche de" + str);
			Query query = getEm().createNamedQuery("Picture.findByURI");
			URI uri = URI.create(str);
			System.out.println(uri.toString());
			query.setParameter("uri", uri);
			Picture p = (Picture) query.getSingleResult();
			System.out.println(p.getLocalfile() + "trouve");
			list.add(p);
		}
		
		return list;
	}
	
	
	public Picture pictureById(long id) throws ServiceException {
		Query query = getEm().createNamedQuery("Picture.findById");
		query.setParameter("id", id);
		return (Picture) query.getSingleResult();
		
	}
	
	public void sup(long id) throws ServiceException {
	//	Query query = getEm().createNamedQuery("Picture.deleteById");
	//	query.setParameter("id", id);
	//	query.executeUpdate();
	//	return query.getSingleResult();
		super.deleteById(id);
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
		 try {
			super.delete(this.pictureById(id));
		} catch (ServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// supprimer de la bdd
		//super.deleteById(id);
	}
	
	public List<Picture> getListPicture() throws ServiceException {
		//La requete est définie dans la classe Picture grâce à une annotation
		Query query = getEm().createNamedQuery("Picture.findAllPicture");
	///	query.setParameter();
		return query.getResultList();
	}
	
}
