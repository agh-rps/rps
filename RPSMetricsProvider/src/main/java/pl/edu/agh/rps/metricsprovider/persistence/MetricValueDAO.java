package pl.edu.agh.rps.metricsprovider.persistence;

import java.util.Date;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import pl.edu.agh.rps.metricsprovider.model.MetricValue;
import pl.edu.agh.rps.metricsprovider.model.Resource;
import pl.edu.agh.rps.metricsprovider.model.SupervisedSystem;

public class MetricValueDAO {

	public static List<MetricValue> getValuesListWithTime(SupervisedSystem system, Resource resource, Date beginTimestamp, Date endTimestamp) {
		
		Session session = HibernateUtils.getSessionFactory().openSession();
		
		@SuppressWarnings("unchecked")
		List<MetricValue> values = session.createCriteria(MetricValue.class)
								   .add(Restrictions.eq("system", system.getSystemId()))
								   .add(Restrictions.eq("resource", resource.getResourceId()))
								   .add(Restrictions.between("timestamp", beginTimestamp, endTimestamp))
								   .list();
		session.close();
		return values;
	}
	
	public static List<MetricValue> getRecentValues(SupervisedSystem system, Resource resource, int amount) {
		
		Session session = HibernateUtils.getSessionFactory().openSession();
		
		@SuppressWarnings("unchecked")
		List<MetricValue> values = session.createCriteria(MetricValue.class)
										  .addOrder(Order.desc("timestamp"))
										  .setMaxResults(amount)
										  .list();
		session.close();
		return values;
		
	}
	
}
