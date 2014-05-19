package pl.edu.agh.rps.predictor;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.math3.stat.regression.SimpleRegression;

import pl.edu.agh.rps.metricsprovider.model.MetricValue;

public class SimpleApacheRegression {

	Map<Long, SimpleRegression> regressionMap = new HashMap<Long, SimpleRegression>();
	int frequency;
	Long startingDate = null;

	public void setData(Map<Long, List<MetricValue>> values, int frequency) {
		this.frequency = frequency;
		for (Long resourceId : values.keySet()) {
			SimpleRegression regression = new SimpleRegression();
			for (MetricValue value : values.get(resourceId)) {
				if (startingDate == null) {
					startingDate = value.getTimestamp().getTime();
				}
				regression.addData(
						(value.getTimestamp().getTime() - startingDate)
								/ frequency, value.getValue());
			}
			regressionMap.put(resourceId, regression);
		}
	}

	public Map<Long, Double> predict(Date date) {
		Map<Long, Double> result = new TreeMap<Long, Double>();
		for (Long resourceId : regressionMap.keySet()) {
			result.put(
					resourceId,
					regressionMap.get(resourceId).predict(
							(date.getTime() - startingDate) / frequency));
		}
		return result;
	}

}
