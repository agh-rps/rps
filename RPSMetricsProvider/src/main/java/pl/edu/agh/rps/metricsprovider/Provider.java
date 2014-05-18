package pl.edu.agh.rps.metricsprovider;

import java.util.List;

import pl.edu.agh.rps.metricsprovider.model.MetricValue;
import pl.edu.agh.rps.metricsprovider.model.Resource;
import pl.edu.agh.rps.metricsprovider.model.SupervisedSystem;
import pl.edu.agh.rps.metricsprovider.persistence.ResourceDAO;
import pl.edu.agh.rps.metricsprovider.persistence.SupervisedSystemDAO;


public class Provider {

	private static List<SupervisedSystem> systems;
	
	public static void initialize() {
		systems = SupervisedSystemDAO.getAll();
		for (SupervisedSystem s : systems) {
			for (Resource r : s.getResources())
				r.initializeBuffer();
		}
	}
	
	public static List<SupervisedSystem> getAllSystems() {
		return systems;
	}

	public static void registerSystem(SupervisedSystem system) {
		systems.add(system);
		SupervisedSystemDAO.registerSystem(system);
	}
	
	public static void registerResource(Resource resource) {
		resource.getSystem().getResources().add(resource);
		ResourceDAO.registerResource(resource);
	}
	
	public static void addMetricValue(MetricValue value) {
		value.getResource().addValue(value);
	}
	
}
