package pl.edu.agh.rps.predictor;

import java.util.Date;
import java.util.List;
import java.util.Scanner;

import pl.edu.agh.rps.generator.GenerationMode;
import pl.edu.agh.rps.generator.Generator;
import pl.edu.agh.rps.metricsprovider.Provider;
import pl.edu.agh.rps.metricsprovider.model.MetricValue;
import pl.edu.agh.rps.metricsprovider.model.Resource;
import pl.edu.agh.rps.metricsprovider.model.SupervisedSystem;

public class Test {

	//private static Calendar calendar = Calendar.getInstance();
	private static List<SupervisedSystem> systems;
	private static Date beginTimestamp;
	
	public static void main(String[] args) {
		
		Provider.initialize();
		new Thread(new Generator()).start();
		
		beginTimestamp = new Date();
	
		String line;
		Scanner scanner = new Scanner(System.in);
		while (!(line = scanner.nextLine()).equals("quit")) {
		
			if ("RANDOM".equals(line)) {
				Generator.setMode(GenerationMode.RANDOM);
			} else if ("INFO".equals(line)) {
				showInfo();
			} else {
				System.out.println("Invalid mode.");
			}
			System.out.print(">> ");
		}
		Generator.setMode(GenerationMode.SHUTDOWN);
		scanner.close();
	
	}
	
	public static void showInfo() {
		systems = Provider.getAllSystems();
		
		System.out.println("timestamp: " + new Date());
		System.out.println("registered systems: " + systems.size());
		for (SupervisedSystem s : systems) {
			System.out.println("system " + s.getSystemId());
			System.out.println("\tregistered resources: " + s.getResources().size());
			for (Resource res : s.getResources()) {
				System.out.print("\t\t" + res.getResourceId() + ": {");
				for (MetricValue value : res.getMetricValues(beginTimestamp, new Date())) {
					System.out.print(value.getValue() + " ");
				}
				System.out.println("}");
			}
		}
	}
	
}