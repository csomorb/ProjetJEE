package fr.uga.miashs.album.service;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
//import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import javax.faces.context.FacesContext;
import javax.persistence.Query;

import fr.uga.miashs.album.model.Album;
import fr.uga.miashs.album.model.AppUser;
import fr.uga.miashs.album.model.Picture;

import javax.servlet.http.Part;

public class AlbumService extends JpaService<Long,Album> {

	public void create(Album a) throws ServiceException {
		a.setOwner(getEm().merge(getEm().merge( a.getOwner())));		
		super.create(a);
	}
	
	public Album update(Album a) {
		super.update(a);
		System.out.println("MISE A JOUR ALBUM EN THEORIE" + a.getSharedWith().size());
		
		return a;
	}
	
	AlbumService(){
		this.pictureService = new PictureService();
	}
	
	
	public List<AppUser> getNotSharedWith(Album a){
		AppUserService ap = new AppUserService();
		
		Set<AppUser> sharedWith = a.getSharedWith();
		List<AppUser>listePartage = new ArrayList<AppUser>(a.getSharedWith());
		List<AppUser>userNotShared = ap.listUsers();
		
		return userNotShared;
	}
	
	private PictureService pictureService;
	
	public void create2(Album a, Part zip) throws ServiceException {
		a.setOwner(getEm().merge(getEm().merge( a.getOwner())));
		System.out.println("phase1");
		System.out.println(zip.getSize());
		Path rootDir = Paths.get(FacesContext.getCurrentInstance().getExternalContext().getRealPath("/resources/img/"));
	//	Path rootDir = Paths.get(FacesContext.getCurrentInstance().getExternalContext().getInitParameter("directory"));
	//	Path utilPath = rootDir.resolve(a.getOwner().getFirstname() +" " + a.getOwner().getLastname() );
		Path utilPath = rootDir.resolve( Long.toString(a.getOwner().getId()) );
	//	Path albPath = utilPath.resolve(a.getTitle() );
		super.create(a);
		Path albPath = utilPath.resolve(Long.toString( a.getId()) );
		System.out.println(albPath.toString());
	//	super.create(a);
		try {
			Files.createDirectories(albPath);
			saveFiles(new ZipInputStream(zip.getInputStream()), albPath,a);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//System.out.println(albPath.toString());
		
		
	}
	
	public List<Album> listAlbumOwnedBy(AppUser a) throws ServiceException {
		//La requete est définie dans la classe Album grâce à une annotation
		Query query = getEm().createNamedQuery("Album.findAllOwned");
		query.setParameter("owner", getEm().merge(a));
		return query.getResultList();
	}
	
	public Album albumById(long id) throws ServiceException {
		Query query = getEm().createNamedQuery("Album.findById");
		query.setParameter("id", id);
		return (Album) query.getSingleResult();
		
	}
	
	
	private Album saveFiles(ZipInputStream zis, Path whereDir, Album a) {
		ZipEntry ent = null;
		try {
			while ((ent = zis.getNextEntry()) != null) {
				// si repertoire, on ne fait rien
				if (ent.getName().endsWith("/"))
					continue;
				int idx = ent.getName().lastIndexOf('/');
				String filename = ent.getName();
				if (idx > -1) {
					filename = ent.getName().substring(idx + 1);
				}
				// si fichier caché, on ne fait rien non plus
				if (filename.charAt(0) == '.')
					continue;
				try {
				//	Set<Picture> setPicture = null;
					Path path = whereDir.resolve(filename);
					
					// Files.createFile(path);
					OutputStream bos = new BufferedOutputStream(Files.newOutputStream(path));
					byte[] b = new byte[4094];
					int read = -1;
					while ((read = zis.read(b)) != -1) {
						bos.write(b, 0, read);
					}
					bos.close();
					zis.closeEntry();
					// determin le type mime en fonction de l'extension du
					// fichier (en minuscule)
					String mime = FacesContext.getCurrentInstance().getExternalContext()
							.getMimeType(path.toString().toLowerCase());
					// si le type mime du fichier n'est pas une image, on la
					// supprime.
					if (mime == null || !mime.startsWith("image/"))
						Files.delete(path);
					else{
						// on ajoute l'image à la bdd
						Picture p = new Picture();
						try {
							p = pictureService.create(p, a, path, filename);
					//		setPicture.add(p);
						} catch (ServiceException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
				//		a.setPictures(setPicture);
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return a;
	}
	
	
	
}
