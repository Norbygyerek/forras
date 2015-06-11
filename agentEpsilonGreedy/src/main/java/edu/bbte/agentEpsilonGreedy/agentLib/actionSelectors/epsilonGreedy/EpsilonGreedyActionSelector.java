/*
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
package edu.bbte.agentEpsilonGreedy.agentLib.actionSelectors.epsilonGreedy;

import java.security.InvalidParameterException;
import java.util.Random;
import java.util.Vector;

import edu.bbte.agentEpsilonGreedy.agentLib.actionSelectors.ActionSelectorInterface;
import edu.bbte.agentEpsilonGreedy.agentLib.actionSelectors.ProvidesActionProbabilitiesFromValues;
import edu.bbte.agentEpsilonGreedy.agentLib.actionSelectors.ProvidesActionValues;
import edu.bbte.agentEpsilonGreedy.agentLib.actionSelectors.ProvidesExpectedStateValue;
import edu.bbte.agentEpsilonGreedy.agentLib.learningModules.ValueProviderInterface;
import edu.bbte.packages.ParameterHolder;
import edu.bbte.packages.taskSpec.TaskSpec;
import edu.bbte.packages.types.Observation;

/**
 *
 * @author Brian Tanner
 */
public class EpsilonGreedyActionSelector implements ActionSelectorInterface, ProvidesExpectedStateValue, ProvidesActionValues,ProvidesActionProbabilitiesFromValues {

    /**
     * I had a bug in this code when I ran 1800+ experiments for ICML 2008.
     * It was a pretty large bug that affected the action selection.  I've left
     * the possibility of emulating this bug so I can see how that changed the
     * results
     */
    boolean ICML08PaperBug=false;
    
    double epsilon = 0.5;
    int actionCount = 0;
    Random R = new Random();

    public EpsilonGreedyActionSelector(TaskSpec theTaskObject, ParameterHolder p) {
        this.epsilon = p.getDoubleParam("epsilon-action-selector-epsilon");
        this.ICML08PaperBug = p.getBooleanParam("epsilon-action-selector-icml08bug");

        actionCount = 1 + theTaskObject.getDiscreteActionRange(0).getMax() - theTaskObject.getDiscreteActionRange(0).getMin();
        assert (actionCount > 0);

        //here we're asserting there IS only one discrete action variable. 
        assert (theTaskObject.getNumDiscreteActionDims() == 1); //check the number of discrete actions is only 1
        assert (theTaskObject.getNumContinuousActionDims() == 0); //check that there is no continuous actions
        

    }

    /**
     *for testin
     */
    protected EpsilonGreedyActionSelector(double epsilon, int actionCount) {
        this.epsilon = epsilon;
        this.actionCount = actionCount;
    }

    public int sampleAction(Observation theObservation, ValueProviderInterface theValueProvider, long uniqueId) {
        int action = 0;

        double r = R.nextDouble();

        double[] theProbs = getActionProbabilities(theObservation, theValueProvider);

//        //Temp code
//        double[] theValues = getActionValues(theObservation, theValueProvider);
//        System.out.print("Values: \t"+theValues[0]+"\t"+theValues[1]+"\t"+theValues[2]+"\t"+theValues[3]);
//        System.out.println("\n Probs: \t"+theProbs[0]+"\t"+theProbs[1]+"\t"+theProbs[2]+"\t"+theProbs[3]);

        while (theProbs[action] < r && (action < actionCount - 1)) {
            r -= theProbs[action];
            action++;
        }

        return action;
    }

    public static void addToParameterHolder(ParameterHolder p) {
        p.addDoubleParam("epsilon-action-selector-epsilon", .05d);
        p.addBooleanParam("epsilon-action-selector-icml08bug", false);
    }

    public int sampleAction(Observation sprime, ValueProviderInterface theValueProvider) {
        return sampleAction(sprime, theValueProvider, -1);

    }

    public double[] getActionValues(Observation theObservation, ValueProviderInterface theValueProvider){
        double[] actionValues = new double[actionCount];
        for (int a = 0; a < actionCount; a++) {
            actionValues[a] = theValueProvider.query(theObservation, a);
        }
        return actionValues;
    }
    

    public double[] getActionProbabilities(Observation theObservation, ValueProviderInterface theValueProvider) {
        double[] actionValues=getActionValues(theObservation, theValueProvider);
        return getActionProbabilities(actionValues);
    }

    public double getEpsilon() {
        return epsilon;
    }

    public void setEpsilon(double newEpsilon) {
        if (Double.isNaN(newEpsilon)) {
            throw new InvalidParameterException("You set epsilon to Nan.  That's bad.");
        }
        if (Double.isInfinite(newEpsilon)) {
            throw new InvalidParameterException("You set epsilon to Inf.  That's bad.");
        }
        if (newEpsilon < 0) {
            throw new InvalidParameterException("You set epsilon to: " + newEpsilon + ".  That's less than 0, which is bad.");
        }
        if (newEpsilon > 1.0d) {
            throw new InvalidParameterException("You set epsilon to: " + newEpsilon + ".  That's more than 1.0, which is bad.");
        }
        this.epsilon = newEpsilon;
    }

    public double[] getActionProbabilities(double[] actionValues) {

        Vector<Integer> bestActions = new Vector<Integer>();
        double bestValue = actionValues[0];
        bestActions.add(0);
        int action = 0;

        for (int a = 1; a < actionValues.length; a++) {
            double thisActionValue = actionValues[a];
            
            if(ICML08PaperBug)thisActionValue=actionValues[1];

            if (thisActionValue >= bestValue) {
                if (thisActionValue > bestValue) {
                    action = a;
                    bestActions.clear();
                    bestActions.add(a);
                    bestValue = thisActionValue;
                } else {
                    bestActions.add(a);
                }
            }
        }

        double[] actionProbs = new double[actionCount];
        double epsProb = epsilon / (double) actionCount;
        double maxsProb = (1.0d - epsilon) / (double) bestActions.size();
        for (int a = 0; a < actionCount; a++) {
            actionProbs[a] = epsProb;
        }

        for (Integer thisBestAction : bestActions) {
            actionProbs[thisBestAction] += maxsProb;
        }
        return actionProbs;
    }

    public double getStateValue(Observation theObservation, ValueProviderInterface theValueProvider) {
        double[] actionValues = new double[actionCount];
        for (int a = 0; a < actionCount; a++) {
            actionValues[a] = theValueProvider.query(theObservation, a);
        }
        double[] probs = getActionProbabilities(actionValues);

        double stateValue = 0.0d;
        for (int i = 0; i < actionCount; i++) {
            stateValue += actionValues[i] * probs[i];
        }
        return stateValue;
    }

    public void cleanup() {
        R=null;
    }


}
