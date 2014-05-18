package pl.edu.agh.rps.metricsprovider.model;

import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import pl.edu.agh.rps.metricsprovider.Properties;
import pl.edu.agh.rps.metricsprovider.persistence.MetricValueDAO;

@Entity
@Table(name="resources")
public class Resource {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="resource_id")
	private long resourceId;
	
	@ManyToOne
	@JoinColumn(name="system_id")
	private SupervisedSystem system;
	
	@Transient
	private List<MetricValue> valuesBuffer;
	
	private final static Comparator<MetricValue> comparator;
	
	static {
		comparator = new Comparator<MetricValue>() {
			public int compare(MetricValue o1, MetricValue o2) {
				return o1.getTimestamp().before(o2.getTimestamp()) ? 1 : -1;
			}
		};
	}
	
	public Resource() {};
	
	public Resource(SupervisedSystem system) {
		this.system = system;
		valuesBuffer = new LinkedList<MetricValue>();
	}

	public long getResourceId() {
		return resourceId;
	}
	
	public SupervisedSystem getSystem() {
		return system;
	}
	
	public List<MetricValue> getMetricValues(Date beginTimestamp, Date endTimestamp) {
		int startIndex = getIndexWithTime(beginTimestamp, true);
		int endIndex = getIndexWithTime(endTimestamp, false);
		
		if (startIndex >= 0) {
			return Collections.unmodifiableList(valuesBuffer.subList(startIndex, endIndex));
		} else if (endIndex >= 0) {
			List<MetricValue> list = MetricValueDAO.getValuesListWithTime(system, this, beginTimestamp, valuesBuffer.get(0).getTimestamp());
			list.addAll(valuesBuffer.subList(0, endIndex));
			return list;
		} else {
			return MetricValueDAO.getValuesListWithTime(system, this, beginTimestamp, endTimestamp);
		}
	}

	private int getIndexWithTime(Date timestamp, boolean first) {
		int index = Collections.binarySearch(valuesBuffer, MetricValue.valueWithTime(timestamp), comparator);
		if (index >= 0) {
			return index;
		} else if (index < -1) {
			return first ? -index-1 : -index-2;
		}
		return -1;
	}
	
	public void initializeBuffer() {
		valuesBuffer = MetricValueDAO.getRecentValues(system, this, Properties.VALUES_BUFFER_SIZE);
	}
	
}
