package pl.edu.agh.rps.generator;

import java.util.Date;

import pl.edu.agh.rps.metricsprovider.model.MetricValue;
import pl.edu.agh.rps.metricsprovider.model.Resource;

import com.jezhumble.javasysmon.CpuTimes;
import com.jezhumble.javasysmon.JavaSysMon;

public class CPUUsageMonitor implements IResource {

	private JavaSysMon sysMon;
	private CpuTimes cpuTimes;
	private Resource resource;
	private double currentValue = 0;
	
	public CPUUsageMonitor(Resource resource) {
		this.resource = resource;
		sysMon = new JavaSysMon();
		cpuTimes = sysMon.cpuTimes();
	}
	
	public MetricValue nextValue() {
		currentValue = sysMon.cpuTimes().getCpuUsage(cpuTimes);
		cpuTimes = sysMon.cpuTimes();
		
		return new MetricValue(resource.getSystem(), resource, new Date(), currentValue);
	}

	public double getCurrentValue() {
		return currentValue;
	}

}
