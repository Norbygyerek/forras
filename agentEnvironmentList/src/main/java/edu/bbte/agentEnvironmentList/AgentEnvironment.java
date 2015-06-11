package edu.bbte.agentEnvironmentList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.bbte.robo.Robo;

/**
 * Az AgentEnvrionent osztály, amely implementálja az
 * AgentEnvironmentList interfészt. Szerepe listák
 * összeállítása az Agent és Environment példányokról, illetve
 * a tesztekhez tartozó állapotokról, melyet a RoboCommunication
 * osztálytól kap meg
 * @author Gáll
 *
 */
public class AgentEnvironment implements AgentEnvironmentList {

	private List<String> agentList       = new ArrayList<String>();
	private List<String> environmentList = new ArrayList<String>();
	
	private Map<Robo, TestList> testList = new HashMap<Robo, TestList>();
	
	
	public List<String> getAgentList() {
		
		return agentList;
	}

	public List<String> getEnvironmentList() {
		
		return environmentList;
	}

	public void addAgent(String agent) {
		
		agentList.add(agent);
		
	}

	public void addEnvironment(String environment) {
		
		environmentList.add(environment);
		
	}

	public void removeAgent(String agent) {
		
		agentList.remove(agent);
		
	}

	public void removeEnvironment(String environment) {
		
		environmentList.remove(environment);
		
	}
	
	public void addTest(String agentName, String envName, String date, Robo robo) {
		
		// Saját adatszerkezet mely 4 adattaggal rendelkezik
		TestList test = new TestList();
		
		test.setS1(agentName);
		test.setS2(envName);
		test.setS3(date);
		test.setRobo(robo);
		
		testList.put(robo, test);
		
	}
	
	
	public void removeTest(Robo robo) {
		
		testList.remove(robo);
		
	}
	
	
	public List<TestList> getTests() {
		
		return new ArrayList<TestList>(testList.values());
	}

}
