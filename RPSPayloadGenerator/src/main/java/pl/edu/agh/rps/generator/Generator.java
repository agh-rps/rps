package pl.edu.agh.rps.generator;

import java.text.DecimalFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import pl.edu.agh.rps.metricsprovider.Provider;
import pl.edu.agh.rps.metricsprovider.model.MetricValue;
import pl.edu.agh.rps.metricsprovider.model.Resource;
import pl.edu.agh.rps.metricsprovider.model.SupervisedSystem;


public class Generator extends Thread {
	
	private static boolean VERBOSE = false;
	private SupervisedSystem system;
	private List<GeneratedResource> resources;
	private boolean isRunning = true;

	public Generator() {
		system = new SupervisedSystem();
		Provider.registerSystem(system);
		resources = new LinkedList<GeneratedResource>();
	}
	
	public List<GeneratedResource> getGeneratedResources() {
		return resources;
	}
	
	public void shutdown() {
		this.isRunning = false;
	}
	
	public static void setVerbose(boolean verbose) {
		VERBOSE = verbose;
	}
	
	public void addGeneratedResource(GenerationMode mode) {
		Resource resource = new Resource(system);
		Provider.registerResource(resource);
		
		GeneratedResource generated = new GeneratedResource(resource);
		generated.setMode(mode);
		resources.add(generated);
	}
	
	public void addRelatedGeneratedResource(List<GeneratedResource> related) { 
		Resource resource = new Resource(system);
		Provider.registerResource(resource);
		
		GeneratedResource generated = new GeneratedResource(resource);
		generated.setMode(GenerationMode.RELATED);
		generated.addRelated(related);
		resources.add(generated);
	}
	
	public void run() {
	
		DecimalFormat format = new DecimalFormat("##.00");
		while (isRunning) {
			
			for (GeneratedResource res : resources) {
				Provider.addMetricValue(res.nextValue());
				if (VERBOSE) {
					System.out.print(format.format(res.getCurrentValue()) + "|");
				}
			}
			
			if (VERBOSE && !resources.isEmpty()) {
				System.out.println("");
			}
			
			try {
				Thread.sleep(150);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	
	
}
