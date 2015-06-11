/*
Copyright 2007 Brian Tanner
brian@tannerpages.com
http://brian.tannerpages.com

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
 */
package edu.bbte.roboCommunication;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.bbte.agent.Agent;
import edu.bbte.agentEnvironmentList.AgentEnvironmentList;
import edu.bbte.dataModel.BaseTest;
import edu.bbte.dataModel.JDBCTestDao;
import edu.bbte.environment.Environment;
import edu.bbte.packages.types.Action;
import edu.bbte.packages.types.Observation;
import edu.bbte.packages.types.ObservationAction;
import edu.bbte.packages.types.RewardObservationActionTerminal;
import edu.bbte.packages.types.RewardObservationTerminal;
import edu.bbte.robo.Robo;

/**
 * This is a local implementation of RL-Glue. It should be identical in behavior
 * to the RL-Glue code in the C/C++ RLGlueCore project.
 * @since 2.03
 * @author btanner
 */
/**
 * RoboCommunication osztály, mely megkap
 * egy Agent és egy Environment példányt
 * majd ezek közötti kommunikációért felelős.
 * Az RL-Glue s elgondolás megmaradt, viszont a hálózati kommunikációhoz
 * szükséges dolgok kiiktatásra kerültek és kiegészítésre került az OSGI nak megfelelően.
 * A c- c++ - os metódusnevek teljesen Javasra lettek cserélve
 * @author Gáll
 *
 */
public class RoboCommunication implements Robo {

	private static final Logger logger = LoggerFactory.getLogger (RoboCommunication.class);
	
    private Environment environment;
    private Agent agent;
    private Action lastAction;

    private int numSteps;
    private int numEpisodes;
    
    private double totalReward;
    

    private int episodeCounter;
    private int percent;
    
    private FileWriter resultWriter;
    private FileWriter statWriter;
    
    private String     envName;
    private String     agentName;
    private String     fileNameResults;
    private String     dateToDatabase;
    
    private DateFormat dateFormat;
    private Date       date;
    
    private  ArrayList<Integer> rlNumSteps;
    private  ArrayList<Double>  rlReturn;
    
    private int    avgSteps;
    private int    randNum;
    
    private double avgReturn;
    
	private Bundle bundle;
	private BundleContext context;
	
	private ServiceReference<?> agentEnvironmentList;
	private AgentEnvironmentList list;
	
	private Random random;

	
    public RoboCommunication() {
    	
    	environment = null;
    	agent       = null;
    	lastAction  = null;
    	
    	numSteps    = 0;
    	numEpisodes = 0;
    	
    	totalReward = 0.0d;
    	
    	episodeCounter = 0;
    	percent        = 0;
    	
    	rlNumSteps = new ArrayList<Integer>();
        rlReturn   = new ArrayList<Double>();
        
    	dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
    	date       = new Date();
    	
    	fileNameResults = dateFormat.format(date);
    	
    	random = new Random();
 
    	randNum = random.nextInt(16000 - 4000) + 4000;
    	
    	
    	
    	try {
			
    		resultWriter = new FileWriter(new File("D:\\" + fileNameResults + randNum + "Results" + ".txt"), true);
    		statWriter   = new FileWriter(new File("D:\\" + fileNameResults + randNum + "Stat" + ".txt"), true);
		
    	} catch (IOException e) {
			
			e.printStackTrace();
		}
    	
    	dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    	date       = new Date();
    	
    	dateToDatabase = dateFormat.format(date);
    	

    	
    }
    
    
    public void setCommunication(Environment environment, Agent agent, String envName, String agentName) {
    	
        this.environment = environment;
        this.agent       = agent;
        this.envName     = envName;
        this.agentName   = agentName;
        
    	
		JDBCTestDao test = new JDBCTestDao();
		
		BaseTest baseTest = new BaseTest();
		baseTest.setAgent(this.agentName);
		baseTest.setEnvironment(this.envName);
		baseTest.setExecutionTime(dateToDatabase);
		baseTest.setFileNameResults("D:\\" + fileNameResults + randNum + "Results" + ".txt");
		baseTest.setFileNameStat("D:\\" + fileNameResults + randNum + "Stat" + ".txt");
		
		test.insert(baseTest);
    	
    	bundle = FrameworkUtil.getBundle(AgentEnvironmentList.class);
		
		if (bundle != null) {
		
			context = bundle.getBundleContext();
			agentEnvironmentList =
					context.getServiceReference(AgentEnvironmentList.class.getName());

			list = (AgentEnvironmentList) context.getService(agentEnvironmentList);
			list.addTest(agentName, envName, dateToDatabase, this);
		}
		
		
    }

    public String roboInit() {
    	
        String taskSpec = environment.envInit();
        
        agent.agentInit(taskSpec);
        
        numEpisodes = 0;
        numSteps    = 0;
        
        return taskSpec;
    }

    public ObservationAction roboStart() {
    	
        Observation observation             = roboEnvStart();
        lastAction                          = roboAgentStart(observation);
        ObservationAction observationAction = new ObservationAction(observation, lastAction);
        
        return observationAction;
    }

    public Observation roboEnvStart() {
    	
        numSteps    = 1;
        totalReward = 0.0d;

        Observation observation = environment.envStart();
        
        if (observation == null) {
            logger.error("Observation came back as null from roboStart!");
        }
        
        return observation;
    }
    
    public Action roboAgentStart(Observation theObservation) {
    
    	Action theAction = agent.agentStart(theObservation);
            
    	if (theAction == null) {
    		 logger.error("The Action came back as null from roboStart!");
        }
    	
    	

    	return theAction;
    }
    
    public RewardObservationTerminal roboEnvStep(Action theAction) {
        
    	RewardObservationTerminal rewardObs = environment.envStep(theAction);
        
    	if (rewardObs == null) {
        
    		 logger.error("RewardObservation came back as null from roboStep!");
        }
        
    	if (rewardObs.getObservation() == null) {
        
    		 logger.error("RewardObservation OBSERVATION came back as null from roboStep!");
        }

        totalReward += rewardObs.getReward();

        if (rewardObs.isTerminal()) {
            
        	numEpisodes++;
        
        } else {
        
        	numSteps++;
        }
        
        return rewardObs;
    }


    public Action roboAgentStep(double theReward, Observation theObservation) {
        
    	Action theAction = agent.agentStep(theReward, theObservation);
        
    	if (theAction == null) {
        
    		 logger.error("The Action came back as null from agentStep!");
        }
        
    	return theAction;
    }

    public void roboAgentEnd(double theReward) {
        
    	agent.agentEnd(theReward);
    }

    public RewardObservationActionTerminal roboStep() {
        
    	if (lastAction == null) {
        
    		 logger.error("Last Action came back as null from roboStep!");
        }
        
    	RewardObservationTerminal rewardObs = roboEnvStep(lastAction);
       

        if (rewardObs.isTerminal()) {
            
        	roboAgentEnd(rewardObs.getReward());
       
        } else {
        
        	lastAction = roboAgentStep(rewardObs.getReward(), rewardObs.getObservation());
        }
        
        try {
        	
        	resultWriter.write("EpiosdeNumber: " + episodeCounter + "\n");
        	resultWriter.write("StepNumber: " + numSteps + "\n");
        	resultWriter.write("Action:\n" + lastAction.toString());
        	resultWriter.write("Osbervation:\n" + rewardObs.getObservation().toString());
        	resultWriter.write("Reward: " + rewardObs.getReward() + "\n");
        	resultWriter.write("---------------------------------------------------" + "\n");
        	resultWriter.flush();
			
        } catch (IOException e) {
			
			e.printStackTrace();
		}
        
        return new RewardObservationActionTerminal(rewardObs.getReward(),
        		rewardObs.getObservation(), lastAction, rewardObs.isTerminal());
    }


    public int roboEpisode(int maxStepsThisEpisode, int maxNumOfEpisode) {
    	    	
        RewardObservationActionTerminal rlStepResult = new RewardObservationActionTerminal(0, null, null, 0);
        int currentStep = 0;
        
        roboStart();
        
        /* RL_start sets current step to 1, so we should start x at 1 */
        for (currentStep = 1; rlStepResult.terminal != 1 && (maxStepsThisEpisode == 0 ? true : currentStep < maxStepsThisEpisode); currentStep++) {
            rlStepResult = roboStep();
        }
        
        
		rlNumSteps.add(numSteps);
		rlReturn.add(totalReward);
		
        episodeCounter++;

        double tempPercent =  (double)episodeCounter / (double)maxNumOfEpisode;
        int    value = 0;
        
        while (value < 0.99) {
        	
        	value = (int) (tempPercent * 10);
        }
        
        percent = value;
        
        if (episodeCounter == maxNumOfEpisode) {
        	
    		for (int i = 0; i < rlNumSteps.size(); i++) {
    			
    		    avgSteps  += rlNumSteps.get(i);
    		    avgReturn += rlReturn.get(i);
    		}
    		
    		avgSteps  /= (double)episodeCounter;
    		avgReturn /= (double)episodeCounter;
        	
        	
            try {
            	
            	statWriter.write("Number of episodes: " + episodeCounter + "\n");
            	statWriter.write("Average number of steps per episode: " + avgSteps + "\n");
            	statWriter.write("Average return per episode:" + avgReturn);
            	statWriter.flush();
            	
            } catch (IOException e) {
    			
    			e.printStackTrace();
    		}
            
            list.removeTest(this);
 	
        }
        
        /*Return the value of terminal to tell the caller whether the episode ended naturally or was cut off*/
        return rlStepResult.terminal;
    }

    public int roboNumOfEpisodes() {
        return numEpisodes;
    }

    public int roboNumOfSteps() {
        return numSteps;
    }

    public double roboReturn() {
        return totalReward;
    }
    
    public void terminate() {
    	
    	try {
    		resultWriter.close();
    		statWriter.close();
		} catch (IOException e) {
		
			e.printStackTrace();
		}
    }

    @Override
    public void finalize() {
    	
    	try {
    		
			super.finalize();
			
			if (resultWriter != null) {
				resultWriter.close();
			}
			
			if (statWriter != null) {
				statWriter.close();
			}
			
			
		} catch (Throwable e) {
			
			e.printStackTrace();
		}	
    }
    
    public int getPercent() {
    	
    	return percent; 
    }
}
