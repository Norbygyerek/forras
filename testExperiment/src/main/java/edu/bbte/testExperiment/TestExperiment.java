package edu.bbte.testExperiment;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.bbte.robo.Robo;

/**
 * 
 * @author Gáll
 *
 */
public class TestExperiment {
	
	private static final Logger logger = LoggerFactory.getLogger(TestExperiment.class);
	
	private final static int numOfEpisodes      = 10;
	private final static int maxStepsPerEpisode = 10000;
	
	private static int       numOfSteps[];
	private static double    returnValues[];
	
	private static Robo      robo;
	
	
	public TestExperiment() {
		
		
	}

	private static void run(int numEpisodes) throws IOException {
		
		// tesz futtatása       	
		for(int i = 0; i < numEpisodes; ++i) {
			
			logger.info("Episode: "+(i+1));

			robo.roboEpisode(maxStepsPerEpisode, numOfEpisodes);
			
			logger.info("\t steps: " + robo.roboNumOfSteps());
			
			
			numOfSteps[i]   = robo.roboNumOfSteps();
			returnValues[i] = robo.roboReturn();
		}
		
		robo.terminate();
	}

	public static void setExperiment(Robo roboControl) {
		
		robo = roboControl;
		
		double avgSteps  = 0.0;
		double avgReturn = 0.0;

		numOfSteps   = new int[TestExperiment.numOfEpisodes];
		returnValues = new double[TestExperiment.numOfEpisodes];

		robo.roboInit();

		logger.info("Running: " + numOfEpisodes + " with a cutoff each of: " + maxStepsPerEpisode + " steps.");
		
		try {
			
			run(numOfEpisodes);
			
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		
		
		for (int i = 0; i < TestExperiment.numOfEpisodes; ++i) {
		    
			avgSteps  += numOfSteps[i];
		    avgReturn += returnValues[i];
		}
		
		avgSteps  /= (double)TestExperiment.numOfEpisodes;
		avgReturn /= (double)TestExperiment.numOfEpisodes;
		
		//----------------------------------------------------------------
		logger.info("Number of episodes: " + TestExperiment.numOfEpisodes);
		logger.info("Average number of steps per episode: " +  avgSteps);
		logger.info("Average return per episode: " + avgReturn);
		logger.info("-----------------------------------------------\n");
	}   
}
