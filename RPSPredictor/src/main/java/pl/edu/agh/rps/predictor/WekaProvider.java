package pl.edu.agh.rps.predictor;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pl.edu.agh.rps.metricsprovider.model.MetricValue;
import weka.classifiers.AbstractClassifier;
import weka.core.Instance;
import weka.core.Instances;

public class WekaProvider {

	Map<Long, Integer> idOrder = new HashMap<Long, Integer>();
	protected Long startingDate = null;

	public WekaProvider() {
		super();
	}

	public void setData(Map<Long, List<MetricValue>> values, int frequency) {
		BufferedWriter bw = null;

		int j = 1;
		for (Long resourceId : values.keySet()) {
			idOrder.put(resourceId, j);
			++j;
		}

		try {
			bw = new BufferedWriter(new FileWriter("test.arff"));
			bw.write("@Relation resources\n");
			bw.write("@Attribute time NUMERIC\n");
			for (Long id : values.keySet()) {
				bw.write("@Attribute " + id + " NUMERIC\n");
			}

			int count = values.values().iterator().next().size();
			startingDate = values.values().iterator().next().get(0)
					.getTimestamp().getTime();

			bw.write("@Data\n");
			for (int i = 0; i < count; ++i) {
				StringBuilder sb = new StringBuilder("");
				boolean timeAdded = false;
				for (Long resourceId : values.keySet()) {
					if (!timeAdded) {
						timeAdded = true;
						sb.append((values.get(resourceId).get(i).getTimestamp()
								.getTime() - startingDate));
					}
					sb.append("," + values.get(resourceId).get(i).getValue());
				}
				sb.append("\n");
				bw.write(sb.toString());
			}
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} finally {
			if (bw != null) {
				try {
					bw.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	public double predict(AbstractClassifier classifier) throws Exception {
		Instances data = new Instances(new BufferedReader(new FileReader(
				"test.arff")));
		data.setClassIndex(data.numAttributes() - 1);
		// build model
		classifier.buildClassifier(data);
		// System.out.println(classifier);
		// classify the last instance
		Instance result = data.lastInstance();

		return classifier.classifyInstance(result);
	}

	public void addPredictionLine(Date date) throws IOException {
		BufferedWriter bw = new BufferedWriter(
				new FileWriter("test.arff", true));
		bw.write((date.getTime() - startingDate) + ", ?");
		bw.close();
	}

}