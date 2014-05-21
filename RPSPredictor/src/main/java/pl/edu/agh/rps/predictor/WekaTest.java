package pl.edu.agh.rps.predictor;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pl.edu.agh.rps.generator.GenerationMode;
import pl.edu.agh.rps.generator.Generator;
import pl.edu.agh.rps.metricsprovider.Provider;
import pl.edu.agh.rps.metricsprovider.model.MetricValue;
import pl.edu.agh.rps.metricsprovider.model.Resource;
import weka.classifiers.functions.GaussianProcesses;
import weka.classifiers.functions.LinearRegression;
import weka.classifiers.functions.MultilayerPerceptron;
import weka.classifiers.meta.RegressionByDiscretization;

public class WekaTest {

	private static Date testDate;

	public static void main(String[] args) {
		Map<Long, List<MetricValue>> values = prepareData();
		SimpleApacheRegression regression = new SimpleApacheRegression();
		regression.setData(values, Generator.FREQUENCY);
		WekaProvider provider = new WekaProvider();
		provider.setData(values, Generator.FREQUENCY);
		DateFormat df = new SimpleDateFormat("HH:mm:ss:SS");
		try {
			Map<Long, Double> predicted = regression.predict(testDate);
			System.out.print("\n\nApache(" + df.format(testDate) + "):");
			for (Long id : predicted.keySet()) {
				System.out.println(predicted.get(id));
			}
			provider.addPredictionLine(testDate);
			System.out.println("\n\nLinear regression(" + df.format(testDate)
					+ "):" + provider.predict(new LinearRegression()));
			System.out.println("\n\nRegressionByDiscretization("
					+ df.format(testDate) + "):"
					+ provider.predict(new RegressionByDiscretization()));
			System.out.println("\n\nGaussianProcesses(" + df.format(testDate)
					+ "):" + provider.predict(new GaussianProcesses()));
			MultilayerPerceptron neural = new MultilayerPerceptron();
			System.out.println("\n\nMultilayerPerceptron("
					+ df.format(testDate) + "):" + provider.predict(neural));
			neural.setLearningRate(0.45);
			System.out.println("\n\nMultilayerPerceptron+customParameters("
					+ df.format(testDate) + "):" + provider.predict(neural));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static Map<Long, List<MetricValue>> prepareData() {
		Date beginTimestamp = new Date();
		Date endTimestamp;

		Provider.initialize();
		Generator gen1 = new Generator();
		gen1.addGeneratedResource(GenerationMode.LINEAR);
		// gen1.addGeneratedResource(GenerationMode.SINUSOID);
		// gen1.addRelatedGeneratedResource(gen1.getGeneratedResources());
		new Thread(gen1).start();

		System.out.println("Waiting for data to gather...");
		try {
			Thread.sleep(Generator.FREQUENCY * 1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		endTimestamp = new Date();

		Map<Long, List<MetricValue>> values = new HashMap<Long, List<MetricValue>>();

		for (Resource resource : Provider.getAllSystems().get(0).getResources()) {
			@SuppressWarnings("unchecked")
			List<MetricValue> reversed = (List<MetricValue>) ((ArrayList<MetricValue>) resource
					.getMetricValues(beginTimestamp, endTimestamp)).clone();
			Collections.reverse(reversed);
			values.put(resource.getResourceId(), reversed);
		}

		System.out.println("Waiting some more to get actual value...");
		try {
			Thread.sleep(Generator.FREQUENCY * 20);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println("Actual:");
		Generator.setVerbose(true);
		try {
			Thread.sleep(Generator.FREQUENCY);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		gen1.shutdown();
		testDate = Generator.lastReadingTime;

		return values;
	}
}
