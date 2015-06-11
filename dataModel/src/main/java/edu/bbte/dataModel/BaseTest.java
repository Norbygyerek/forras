package edu.bbte.dataModel;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 
 * @author Gáll
 *
 */
@Entity
@Table (name = "roborunresults")
public class BaseTest implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	@Id
	@GeneratedValue (strategy = GenerationType.IDENTITY)
	@Column (unique = true, nullable = false)
	private Long id;
	
	@Column (unique = true, nullable = false, length = 128)
	private String agent;
	
	@Column (unique = true, nullable = false, length = 128)
	private String environment;
	
	@Column (nullable = false)
	private String executionTime;
	
	@Column (unique = true, nullable = false, length = 256)
	private String fileNameResults;
	private String fileNameStat;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getAgent() {
		return agent;
	}
	public void setAgent(String agent) {
		this.agent = agent;
	}
	public String getEnvironment() {
		return environment;
	}
	public void setEnvironment(String environment) {
		this.environment = environment;
	}
	public String getExecutionTime() {
		return executionTime;
	}
	public void setExecutionTime(String executionTime) {
		this.executionTime = executionTime;
	}
	public String getFileNameResults() {
		return fileNameResults;
	}
	public void setFileNameResults(String fileNameResults) {
		this.fileNameResults = fileNameResults;
	}
	public String getFileNameStat() {
		return fileNameStat;
	}
	public void setFileNameStat(String fileNameStat) {
		this.fileNameStat = fileNameStat;
	}
	
	

	
}
