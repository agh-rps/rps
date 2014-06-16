package pl.edu.agh.rps.generator;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import pl.edu.agh.rps.metricsprovider.model.MetricValue;
import pl.edu.agh.rps.metricsprovider.model.Resource;

public class GeneratedResource implements IResource {

	private static final double MIN_VALUE = 0.0;
	private static final double MAX_VALUE = 100.0;
	
	private Resource resource;
	private double currentValue;
	private GenerationMode mode;
	private boolean ascending;
	private double diff = MIN_VALUE;
	private List<IResource> relatedResources;
	
	private static final Random rand = new Random();
	
	public GeneratedResource(Resource resource) {
		this.resource = resource;
		this.ascending = true;
		this.relatedResources = new LinkedList<IResource>();
		this.mode = GenerationMode.RANDOM;
	}
	
	public void setMode(GenerationMode mode) {
		this.mode = mode;
	}
	
	public GenerationMode getMode() {
		return mode;
	}
	
	public void addRelated(List<IResource> resources) {
		relatedResources.addAll(resources);
	}
	
	public MetricValue nextValue() {
		switch (mode) {
		case RANDOM:
			currentValue = rand.nextDouble() * (MAX_VALUE - MIN_VALUE) + MIN_VALUE;
			break;
		case LINEAR:
			currentValue = diff / 100.0 * (MAX_VALUE - MIN_VALUE) + MIN_VALUE;
			break;
		case SINUSOID:
			currentValue = Math.sin(diff / 100.0 * Math.PI) * (MAX_VALUE - MIN_VALUE) + MIN_VALUE;
			break;
		case RELATED:
			if (!relatedResources.isEmpty()) {
				double average = 0.0;
				for (IResource res : relatedResources) {
					average += res.getCurrentValue();
				}
				average /= relatedResources.size();
				currentValue = average + rand.nextGaussian() * 2.0;
			} else {
				currentValue = 0.5 * (MAX_VALUE + MIN_VALUE);
			}
		}
		
		diff += (2.0 * rand.nextDouble() - 0.5 * rand.nextDouble()) * (ascending ? 1 : -1);
		if (diff > 100.0) {
			ascending = false;
			diff = 100.0;
		} else if (diff < 0.0) {
			ascending = true;
			diff = 0.0;
		}
		
		return new MetricValue(resource.getSystem(), resource, new Date(), currentValue);
	}

	public double getCurrentValue() {
		return currentValue;
	}
	
}
