package edu.bbte.environmentMountainCar;

import java.util.Random;
import java.util.Vector;

import edu.bbte.environment.Environment;
import edu.bbte.environment.EnvironmentBase;
import edu.bbte.packages.ParameterHolder;
import edu.bbte.packages.taskSpec.TaskSpec;
import edu.bbte.packages.taskSpec.TaskSpecVRLGLUE3;
import edu.bbte.packages.taskSpec.ranges.DoubleRange;
import edu.bbte.packages.taskSpec.ranges.IntRange;
import edu.bbte.packages.types.Action;
import edu.bbte.packages.types.Observation;
import edu.bbte.packages.types.RewardObservationTerminal;



public class MountainCar extends EnvironmentBase implements Environment {


	static final int numActions                    = 3;
	protected MountainCarState theState            = null;
	protected Vector<MountainCarState> savedStates = null;
	private Random randomGenerator                 = new Random();

	private String makeTaskSpec() {
		
		TaskSpecVRLGLUE3 theTaskSpecObject = new TaskSpecVRLGLUE3();
		
		theTaskSpecObject.setEpisodic();
		theTaskSpecObject.setDiscountFactor(1.0d);
		theTaskSpecObject.addContinuousObservation(new DoubleRange(theState.minPosition, theState.maxPosition));
		theTaskSpecObject.addContinuousObservation(new DoubleRange(theState.minVelocity, theState.maxVelocity));
		theTaskSpecObject.addDiscreteAction(new IntRange(0, 2));
		theTaskSpecObject.setRewardRange(new DoubleRange(-1, 0));
		theTaskSpecObject.setExtra("EnvName:Mountain-Car Revision:" + this.getClass().getPackage().getImplementationVersion());

		String taskSpecString = theTaskSpecObject.toTaskSpec();
		TaskSpec.checkTaskSpec(taskSpecString);

		return taskSpecString;

	}
	
	 public String envInit() {
		 
	        savedStates = new Vector<MountainCarState>();
	        
	        return makeTaskSpec();

	 }

	    /**
	     * Restart the car on the mountain.  Pick a random position and velocity if
	     * randomStarts is set.
	     * @return
	     */
	    public Observation envStart() {
	    	
	        if (theState.randomStarts) {
	            @SuppressWarnings("unused")
				double randStartPosition =
	            		(randomGenerator.nextDouble() * (theState.maxPosition + Math.abs((theState.minPosition))) - Math.abs(theState.minPosition));
	            theState.position = theState.minVelocity;
	            
	        } else {
	        	
	            theState.position = theState.defaultInitPosition;
	        }
	        theState.velocity     = theState.defaultInitVelocity;

	        return makeObservation();
	    }

	    /**
	     * Takes a step.  If an invalid action is selected, choose a random action.
	     * @param theAction
	     * @return
	     */
	    public RewardObservationTerminal envStep(Action theAction) {

	        int a = theAction.intArray[0];

	        if (a > 2 || a < 0) {
	            System.err.println("Invalid action selected in mountainCar: " + a);
	            a = randomGenerator.nextInt(3);
	        }

	        theState.update(a);

	        return makeRewardObservation(theState.getReward(), theState.inGoalRegion());
	    }
	    
	    /**
	     * Return the ParameterHolder object that contains the default parameters for
	     * mountain car.  The only parameter is random start states.
	     * @return
	     */
	    public static ParameterHolder getDefaultParameters() {
	    	
	        ParameterHolder p = new ParameterHolder();

	        p.addBooleanParam("randomStartStates", false);
	        
	        return p;
	    }

	    /**
	     * Create a new mountain car environment using parameter settings in p.
	     * @param p
	     */
	    public MountainCar(ParameterHolder p) {
	        super();
	        theState = new MountainCarState(randomGenerator);
	        if (p != null) {
	            if (!p.isNull()) {
	                theState.randomStarts = p.getBooleanParam("randomStartStates");
	            }
	        }
	    }

	    public MountainCar() {
	        this(getDefaultParameters());
	    }

	    /**
	     * Turns theState object into an observation.
	     * @return
	     */
	    @Override
	    protected Observation makeObservation() {
	    	
	        Observation currentObs = new Observation(0, 2);

	        currentObs.doubleArray[0] = theState.position;
	        currentObs.doubleArray[1] = theState.velocity;

	        return currentObs;
	    }


	    /**
	     * The value function will be drawn over the position and velocity.  This 
	     * method provides the max values for those variables.
	     * @param dimension
	     * @return
	     */
	    public double getMaxValueForQuerableVariable(int dimension) {
	    	
	        if (dimension == 0) {
	            return theState.maxPosition;
	        } else {
	            return theState.maxVelocity;
	        }
	    }

	    /**
	     * The value function will be drawn over the position and velocity.  This 
	     * method provides the min values for those variables.
	     * @param dimension
	     * @return
	     */
	    public double getMinValueForQuerableVariable(int dimension) {
	    	
	        if (dimension == 0) {
	            return theState.minPosition;
	        } else {
	            return theState.minVelocity;
	        }
	    }

	    /**
	     * Given a state, return an observation.  This is trivial in mountain car
	     * because the observation is the same as the internal state 
	     * @param theState
	     * @return
	     */
	    public Observation getObservationForState(Observation theState) {
	        return theState;
	    }

	    /**
	     * How many state variables are there (used for value function drawing)
	     * @return
	     */
	    public int getNumVars() {
	        return 2;
	    }

	    /**
	     * Used by MCHeightRequest Message
	     * @return
	     */
	    @SuppressWarnings("unused")
		private double getHeight() {
	        return theState.getHeightAtPosition(theState.position);
	    }


	    @SuppressWarnings("unused")
		private Random getRandomGenerator() {
	        return randomGenerator;
	    }

}