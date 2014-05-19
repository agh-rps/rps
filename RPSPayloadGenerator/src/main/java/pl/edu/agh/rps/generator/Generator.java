package pl.edu.agh.rps.generator;

import java.util.Date;
import java.util.Random;
import java.util.Scanner;

import javax.swing.plaf.SliderUI;

import pl.edu.agh.rps.metricsprovider.Provider;
import pl.edu.agh.rps.metricsprovider.model.MetricValue;
import pl.edu.agh.rps.metricsprovider.model.Resource;
import pl.edu.agh.rps.metricsprovider.model.SupervisedSystem;


public class Generator implements Runnable {
	
	private static SupervisedSystem system;
	private static Resource resource;
	private static GenerationMode mode = GenerationMode.RANDOM;
	
	private static final double minValue = 0.0;
	private static final double maxValue = 100.0;
	
	
	public void run() {
		initialize();
		
		MetricValue value;
		Random rand = new Random();
		while (mode != GenerationMode.SHUTDOWN) {
			switch (mode) {
			case RANDOM:
				value = new MetricValue(system, resource, new Date(), rand.nextDouble() * (maxValue-minValue) + minValue);
				Provider.addMetricValue(value);
			}
			
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	
	private static void initialize() {
		system = new SupervisedSystem();
		resource = new Resource(system);
		
		Provider.registerSystem(system);
		Provider.registerResource(resource);
	}
	
	public static void setMode(GenerationMode mode) {
		Generator.mode = mode;
	}
	
	
}
