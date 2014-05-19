package pl.edu.agh.rps.predictor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.stat.regression.OLSMultipleLinearRegression;

import pl.edu.agh.rps.metricsprovider.model.MetricValue;

public class MultiApacheRegression {

	RealMatrix coefs = null;
	Map<Long, Integer> idOrder = new HashMap<Long, Integer>();
	int frequency;

	public void setData(Map<Long, List<MetricValue>> values,
			Long chosenResource, int frequency) {
		this.frequency = frequency;
		OLSMultipleLinearRegression regression = new OLSMultipleLinearRegression();

		List<MetricValue> chosen = values.get(chosenResource);

		int variableCount = values.keySet().size();
		int count = chosen.size();
		double[] data = new double[count * (variableCount + 1)];

		int j = 1;
		for (Long resourceId : values.keySet()) {
			if (resourceId != chosenResource) {
				idOrder.put(resourceId, j);
				++j;
			}
		}

		for (int i = 0; i < count; ++i) {
			j = 1;
			data[i * count] = chosen.get(i).getValue();
			for (Long resourceId : values.keySet()) {
				if (resourceId != chosenResource) {
					data[i * count + j] = values.get(resourceId).get(i)
							.getValue();
					++j;
				}
			}
			data[i * count + j] = chosen.get(i).getValue();
		}
		regression.newSampleData(data, count, variableCount);
		coefs = MatrixUtils.createColumnRealMatrix(regression
				.estimateRegressionParameters());
	}

	public double predict(Map<Long, MetricValue> values) {
		double[] vector = new double[values.keySet().size() + 1];
		boolean timeSet = false;
		for (Long resourceId : values.keySet()) {
			vector[idOrder.get(resourceId)] = values.get(resourceId).getValue();
			if (!timeSet) {
				vector[values.keySet().size()] = values.get(resourceId)
						.getTimestamp().getTime()
						/ frequency;
				timeSet = true;
			}
		}
		return coefs.preMultiply(vector)[0];
	}
}
