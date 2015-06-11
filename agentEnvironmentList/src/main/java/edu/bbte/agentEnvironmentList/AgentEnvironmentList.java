package edu.bbte.agentEnvironmentList;

import java.util.List;

import edu.bbte.robo.Robo;

/**
 * Az AgentEnvironmentList interfész,
 * melynek szerepe, információkat gyűjteni a telepített
 * Agent és Environment illetve RoboCommunication példányok
 * állapotairól és ezt megosztani a webes felülettel
 * @author Gáll
 *
 */
public interface AgentEnvironmentList {
	
	public List<String> getAgentList();
	public List<String> getEnvironmentList();
	public List<TestList> getTests();
	
	public void addAgent(String agent);
	public void addEnvironment(String environment);
	public void removeAgent(String agent);
	public void removeEnvironment(String environment);
	public void addTest(String agentName, String envName, String date, Robo robo);
	public void removeTest(Robo robo);


}
