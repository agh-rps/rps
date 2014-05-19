package pl.edu.agh.rps.predictor;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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

public class ApacheTest {

	private static Date testDate;

	public static void main(String[] args) {
		Map<Long, List<MetricValue>> values = prepareData();
		SimpleApacheRegression regression = new SimpleApacheRegression();
		regression.setData(values, Generator.FREQUENCY);
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.SECOND, 20);
		Map<Long, Double> predicted = regression.predict(testDate);
		DateFormat df = new SimpleDateFormat("HH:mm:ss:SS");
		System.out.println("\n\nPredicted(" + df.format(testDate) + "):");
		for (Long id : predicted.keySet()) {
			System.out.println(id + ": " + predicted.get(id));
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

		System.out.println("Waiting some more to get actual value...");
		try {
			Thread.sleep(Generator.FREQUENCY * 1);
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

		Map<Long, List<MetricValue>> values = new HashMap<Long, List<MetricValue>>();

		for (Resource resource : Provider.getAllSystems().get(0).getResources()) {
			@SuppressWarnings("unchecked")
			List<MetricValue> reversed = (List<MetricValue>) ((ArrayList<MetricValue>) resource
					.getMetricValues(beginTimestamp, endTimestamp)).clone();
			Collections.reverse(reversed);
			values.put(resource.getResourceId(), reversed);
		}

		return values;
	}
}
