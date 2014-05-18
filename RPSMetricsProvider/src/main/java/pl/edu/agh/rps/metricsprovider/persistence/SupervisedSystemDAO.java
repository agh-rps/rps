package pl.edu.agh.rps.metricsprovider.persistence;

import java.util.List;

import org.hibernate.Session;

import pl.edu.agh.rps.metricsprovider.model.SupervisedSystem;

public class SupervisedSystemDAO {

	public static List<SupervisedSystem> getAll() {
		Session session = HibernateUtils.getSessionFactory().openSession();
		
		@SuppressWarnings("unchecked")
		List<SupervisedSystem> systems = session.createCriteria(SupervisedSystem.class).list();
		
		session.close();
		return systems;
	}
	
	public static void registerSystem(SupervisedSystem system) {
		Session session = HibernateUtils.getSessionFactory().openSession();
		session.beginTransaction();
		session.save(system);
		session.getTransaction().commit();
		session.close();
	}
	
}
