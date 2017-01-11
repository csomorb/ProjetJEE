package fr.uga.miashs.album.service;

import java.nio.file.Path;
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
	
	public List<Picture> getListPicture() throws ServiceException {
		//La requete est définie dans la classe Picture grâce à une annotation
		Query query = getEm().createNamedQuery("Picture.findAllPicture");
	///	query.setParameter();
		return query.getResultList();
	}
	
}
