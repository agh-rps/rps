package pl.edu.agh.rps.metricsprovider.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name="metric_values")
public class MetricValue {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="value_id")
	private Long valueId;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date timestamp;
	
	@ManyToOne
	@JoinColumn(name="system_id")
	private SupervisedSystem system;
	
	@ManyToOne
	@JoinColumn(name="resource_id")
	private Resource resource;

	private Double value;
	
	public MetricValue() {};
	
	public MetricValue(SupervisedSystem system, Resource resource, Date timestamp, double value) {
		this.system = system;
		this.resource = resource;
		this.timestamp = timestamp;
		this.value = value;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public double getValue() {
		return value;
	}
	
	public SupervisedSystem getSystem() {
		return system;
	}

	public Resource getResource() {
		return resource;
	}

	public static MetricValue valueWithTime(Date timestamp) {
		return new MetricValue(null, null, timestamp, 0);
	}
	
}
