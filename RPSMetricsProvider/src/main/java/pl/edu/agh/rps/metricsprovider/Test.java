package pl.edu.agh.rps.metricsprovider;

import java.util.Calendar;
import java.util.Date;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

import pl.edu.agh.rps.metricsprovider.model.MetricValue;
import pl.edu.agh.rps.metricsprovider.model.Resource;
import pl.edu.agh.rps.metricsprovider.model.SupervisedSystem;
import pl.edu.agh.rps.metricsprovider.persistence.HibernateUtils;

public class Test {

	private static Calendar calendar = Calendar.getInstance();
	
	public static void main(String[] args) {
		
	Provider.initialize();
	//	calendar.set(2014, 05, 18, 10, 0);
	//	System.out.println(Provider.getAllSystems().get(0).getResources().get(0).getMetricValues(calendar.getTime(), new Date()).get(0).getValue());
	}
	
}
