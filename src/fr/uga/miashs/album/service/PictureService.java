package fr.uga.miashs.album.service;

import java.nio.file.Path;
import java.util.List;
import java.util.Set;

import javax.persistence.Query;

import fr.uga.miashs.album.model.Album;
import fr.uga.miashs.album.model.AppUser;
import fr.uga.miashs.album.model.Picture;

public class PictureService extends JpaService<Long,Picture>{

	public Picture create(Picture p, Album a, Path pa) throws ServiceException {
		
		p.setAlbum(getEm().merge(getEm().merge(p.getAlbum())));
		
	//	p.setAlbum(a);
		
		p.setTitle("titre");
		p.setLocalfile(pa);
		System.out.println("phase2");
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
	
	public List<Picture> getListPicture() throws ServiceException {
		//La requete est définie dans la classe Picture grâce à une annotation
		Query query = getEm().createNamedQuery("Picture.findAllPicture");
	///	query.setParameter();
		return query.getResultList();
	}
	
}
