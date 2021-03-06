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
		Generator gen1 = new Generator();
		gen1.setVerbose(true);
		gen1.start();

		String line;
		Scanner scanner = new Scanner(System.in);
		System.out.println(">> ");
		while (!(line = scanner.nextLine()).equals("QUIT")) {
		
			if ("VERB".equals(line)) {
				gen1.setVerbose(true);
			} else if ("NOVERB".equals(line)) {
				gen1.setVerbose(false);
			} else if ("ADDLIN".equals(line)) {
				gen1.addGeneratedResource(GenerationMode.LINEAR);
			} else if ("ADDRAN".equals(line)) {
				gen1.addGeneratedResource(GenerationMode.RANDOM);
			} else if ("ADDSIN".equals(line)) {
				gen1.addGeneratedResource(GenerationMode.SINUSOID);
			} else if ("ADDREL".equals(line)) {
				gen1.addRelatedGeneratedResource(gen1.getGeneratedResources());
			} else {
				System.out.println("Invalid command.");
			}
			System.out.print(">> ");
		}
		scanner.close();
	
	}
	
	
}
