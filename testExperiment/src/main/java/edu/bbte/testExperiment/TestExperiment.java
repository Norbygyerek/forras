package edu.bbte.testExperiment;

import java.io.IOException;

import edu.bbte.robo.Robo;

public class TestExperiment {
	
	protected static final int kNumEpisodes = 10;
	protected static int rlNumSteps[];
	protected static double rlReturn[];
	
	//cutoff
	protected static int maxStepsPerEpisode=100;
	
	
	private static  Robo robo;
	

	protected static void run(int numEpisodes) throws IOException {
		
		/*run for num_episode number of episodes and store the number of steps and return from each episode*/        	
		for(int x = 0; x < numEpisodes; ++x) {
			System.out.print("Episode: "+(x+1));
			robo.roboEpisode(maxStepsPerEpisode, kNumEpisodes);
			System.out.println("\t steps: " + robo.roboNumOfSteps()); 
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			rlNumSteps[x] = robo.roboNumOfSteps();
			rlReturn[x] = robo.roboReturn();
		}
		
		robo.terminate();
	}

	public static void setExperiment(Robo roboControl) {
		
		robo = roboControl;
		
		double avgSteps = 0.0;
		double avgReturn = 0.0;

		rlNumSteps = new int[TestExperiment.kNumEpisodes];
		rlReturn = new double[TestExperiment.kNumEpisodes];

		robo.roboInit();

		System.out.println("Running: "+kNumEpisodes+" with a cutoff each of: "+maxStepsPerEpisode+" steps.");
		
		try {
			run(kNumEpisodes);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		/*add up all the steps and all the returns*/
		for (int i = 0; i < TestExperiment.kNumEpisodes; ++i) {
		    avgSteps += rlNumSteps[i];
		    avgReturn += rlReturn[i];
		}
		
		/*average steps and returns*/
		avgSteps /= (double)TestExperiment.kNumEpisodes;
		avgReturn /= (double)TestExperiment.kNumEpisodes;
		
		/*print out results*/
		System.out.println("Number of episodes: " + TestExperiment.kNumEpisodes);
		System.out.println("Average number of steps per episode: " +  avgSteps);
		System.out.println("Average return per episode: " + avgReturn);
		System.out.println("-----------------------------------------------\n");
	}   
}
