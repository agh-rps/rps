package pl.edu.agh.rps.generator;

import pl.edu.agh.rps.metricsprovider.model.MetricValue;

public interface IResource {
	public MetricValue nextValue();
	public double getCurrentValue();
}
