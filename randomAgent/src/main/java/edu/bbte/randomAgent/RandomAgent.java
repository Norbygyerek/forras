
package edu.bbte.randomAgent;

import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.bbte.agent.Agent;
import edu.bbte.packages.ParameterHolder;
import edu.bbte.packages.taskSpec.TaskSpec;
import edu.bbte.packages.taskSpec.ranges.AbstractRange;
import edu.bbte.packages.taskSpec.ranges.DoubleRange;
import edu.bbte.packages.taskSpec.ranges.IntRange;
import edu.bbte.packages.types.Action;
import edu.bbte.packages.types.Observation;

/**
 * 
 * @author Gáll
 *
 */
public class RandomAgent implements Agent {

	private static final Logger logger = LoggerFactory.getLogger (RandomAgent.class);
	
    private Action action;
    private Random random = new Random();
    TaskSpec TSO          = null;

    
    public RandomAgent() {
        this(getDefaultParameters());
    }

    public RandomAgent(ParameterHolder p) {
        super();
    }
    
    /**
     * Tetris doesn't really have any parameters
     * @return
     */
    public static ParameterHolder getDefaultParameters() {
    	
        ParameterHolder p = new ParameterHolder();
        
        return p;
    }

    public void agentInit(String taskSpec) {

    	TSO = new TaskSpec(taskSpec);

        //Do some checking on the ranges here so we don't feel bad if we crash later for not checking them.
        for (int i = 0; i < TSO.getNumDiscreteActionDims(); i++) {
            AbstractRange thisActionRange = TSO.getDiscreteActionRange(i);

            if (thisActionRange.hasSpecialMinStatus() || thisActionRange.hasSpecialMaxStatus()) {
                logger.error("The random agent does not know how to deal with actions that are unbounded or unspecified ranges.");
            }
        }
        
        for (int i = 0; i < TSO.getNumContinuousActionDims(); i++) {
            AbstractRange thisActionRange = TSO.getContinuousActionRange(i);

            if (thisActionRange.hasSpecialMinStatus() || thisActionRange.hasSpecialMaxStatus()) {
            	logger.error("The random agent does not know how to deal with actions that are unbounded or unspecified ranges.");
            }
        }

        action = new Action(TSO.getNumDiscreteActionDims(), TSO.getNumContinuousActionDims());
    }

    public Action agentStart(Observation o) {
        
    	setRandomActions(action);
        
    	return action;
    }

    public Action agentStep(double arg0, Observation o) {
        
    	setRandomActions(action);
        
    	return action;
    }

    public void agentEnd(double reward) {
    }

    private void setRandomActions(Action action) {
        
    	for (int i = 0; i < TSO.getNumDiscreteActionDims(); i++) {
            IntRange thisActionRange = TSO.getDiscreteActionRange(i);
            action.intArray[i] = random.nextInt(thisActionRange.getRangeSize()) + thisActionRange.getMin();
        }
        
    	for (int i = 0; i < TSO.getNumContinuousActionDims(); i++) {
            DoubleRange thisActionRange = TSO.getContinuousActionRange(i);
            action.doubleArray[i] = random.nextDouble() * (thisActionRange.getRangeSize()) + thisActionRange.getMin();
        }
    }
}


