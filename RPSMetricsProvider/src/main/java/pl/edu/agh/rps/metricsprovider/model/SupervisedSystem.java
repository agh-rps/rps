package pl.edu.agh.rps.metricsprovider.model;

import java.util.LinkedList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name="systems")
public class SupervisedSystem {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="system_id")
	private Long systemId;
	
	@OneToMany(fetch=FetchType.EAGER, mappedBy="system")
	private List<Resource> resources;

	public SupervisedSystem() {
		resources = new LinkedList<Resource>();
	};
	
	public long getSystemId() {
		return systemId;
	}
	
	public List<Resource> getResources() {
		return resources;
	}
	
}
