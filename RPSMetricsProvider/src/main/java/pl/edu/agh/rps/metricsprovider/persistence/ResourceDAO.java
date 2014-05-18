package pl.edu.agh.rps.metricsprovider.persistence;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import pl.edu.agh.rps.metricsprovider.model.Resource;
import pl.edu.agh.rps.metricsprovider.model.SupervisedSystem;

public class ResourceDAO {

	public List<Resource> getAllFromSystem(SupervisedSystem system) {
		Session session = HibernateUtils.getSessionFactory().openSession();
		
		@SuppressWarnings("unchecked")
		List<Resource> resources = session.createCriteria(Resource.class)
								   .add(Restrictions.eq("system", system.getSystemId()))
								   .list();
		
		session.close();
		return resources;
	}
	
	public static void registerResource(Resource resource) {
		Session session = HibernateUtils.getSessionFactory().openSession();
		session.beginTransaction();
		session.save(resource);
		session.update(resource.getSystem());
		session.getTransaction().commit();
		session.close();
	}
	
}
