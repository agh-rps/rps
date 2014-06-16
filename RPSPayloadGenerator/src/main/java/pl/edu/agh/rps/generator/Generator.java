package pl.edu.agh.rps.generator;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import pl.edu.agh.rps.metricsprovider.Provider;
import pl.edu.agh.rps.metricsprovider.model.Resource;
import pl.edu.agh.rps.metricsprovider.model.SupervisedSystem;

public class Generator extends Thread {

	public static final int FREQUENCY = 5;
	private static boolean VERBOSE = false;
	private final SupervisedSystem system;
	private final List<IResource> resources;
	private boolean isRunning = true;
	public static Date lastReadingTime = null;

	public Generator() {
		system = new SupervisedSystem();
		Provider.registerSystem(system);
		resources = new LinkedList<IResource>();
	}

	public List<IResource> getResources() {
		return resources;
	}

	public void shutdown() {
		this.isRunning = false;
	}

	public static void setVerbose(boolean verbose) {
		VERBOSE = verbose;
	}
	
	public void addCpuMonitor() {
		Resource resource = new Resource(system);
		Provider.registerResource(resource);
		
		CPUUsageMonitor monitor = new CPUUsageMonitor(resource);
		resources.add(monitor);
	}

	public void addGeneratedResource(GenerationMode mode) {
		Resource resource = new Resource(system);
		Provider.registerResource(resource);

		GeneratedResource generated = new GeneratedResource(resource);
		generated.setMode(mode);
		resources.add(generated);
	}

	public void addRelatedGeneratedResource(List<IResource> related) {
		Resource resource = new Resource(system);
		Provider.registerResource(resource);

		GeneratedResource generated = new GeneratedResource(resource);
		generated.setMode(GenerationMode.RELATED);
		generated.addRelated(related);
		resources.add(generated);
	}

	@Override
	public void run() {

		DateFormat df = new SimpleDateFormat("HH:mm:ss:SS");
		DecimalFormat format = new DecimalFormat("##.00");
		while (isRunning) {
			lastReadingTime = new Date();
			if (VERBOSE) {
				System.out.print(df.format(lastReadingTime) + " ");
			}
			for (IResource res : resources) {
				Provider.addMetricValue(res.nextValue());
				if (VERBOSE) {
					System.out
							.print(format.format(res.getCurrentValue()) + "|");
				}
			}

			if (VERBOSE && !resources.isEmpty()) {
				System.out.println("");
			}

			try {
				Thread.sleep(FREQUENCY);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}
}
