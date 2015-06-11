/*
 * Copyright 2008 Brian Tanner
 * http://bt-recordbook.googlecode.com/
 * brian@tannerpages.com
 * http://brian.tannerpages.com
 * 
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package edu.bbte.agentEpsilonGreedy;


import edu.bbte.agent.Agent;
import edu.bbte.agentEpsilonGreedy.agentLib.actionSelectors.epsilonGreedy.EpsilonGreedyActionSelector;
import edu.bbte.agentEpsilonGreedy.agentLib.functionApproximators.CMAC.CMACFunctionApproximatorFactory;
import edu.bbte.agentEpsilonGreedy.agentLib.functionApproximators.CMAC.NDCMACTileCoder;
import edu.bbte.agentEpsilonGreedy.agentLib.learningModules.sarsaLambda.SarsaLambdaLearningModule;
import edu.bbte.packages.ParameterHolder;
import edu.bbte.packages.taskSpec.TaskSpec;
import edu.bbte.packages.types.Action;
import edu.bbte.packages.types.Observation;

/**
 *
 * @author Brian Tanner
 */
public class EpsilonGreedyTileCodingSarsaLambda implements Agent {

    protected int actionCount;
    protected SarsaLambdaLearningModule theLearningModule = null;
    protected EpsilonGreedyActionSelector theActionSelector = null;
    protected CMACFunctionApproximatorFactory theFAFactory = null;
    protected TaskSpec theTaskObject = null;
    protected ParameterHolder theParamHolder;

    public static ParameterHolder getDefaultParameters() {
        ParameterHolder p = new ParameterHolder();
        SarsaLambdaLearningModule.addToParameterHolder(p);
        NDCMACTileCoder.addToParameterHolder(p);
        EpsilonGreedyActionSelector.addToParameterHolder(p);
        return p;
    }

    public EpsilonGreedyTileCodingSarsaLambda() {
        this(EpsilonGreedyTileCodingSarsaLambda.getDefaultParameters());
    }

    public EpsilonGreedyTileCodingSarsaLambda(ParameterHolder p) {
        super();
        this.theParamHolder = p;
    }

    /**
     * It is assumed that this  method will be overridden and that the overrider
     * will call super with theTaskSpec and then setup their own stuff
     * like learning module
     * @param theTaskSpec
     */
    public void agentInit(String theTaskSpec) {
        theTaskObject = new TaskSpec(theTaskSpec);

        actionCount = 1 + theTaskObject.getDiscreteActionRange(0).getMax() - theTaskObject.getDiscreteActionRange(0).getMin();
        assert (actionCount > 0);

        //here we're asserting there IS only one discrete action variable. 
        assert (theTaskObject.getNumDiscreteActionDims() == 1); //check the number of discrete actions is only 1
        assert (theTaskObject.getNumContinuousActionDims() == 0); //check that there is no continuous actions


        theLearningModule = new SarsaLambdaLearningModule(theTaskObject, theParamHolder, new CMACFunctionApproximatorFactory());
        theActionSelector = new EpsilonGreedyActionSelector(theTaskObject, theParamHolder);
    }

    public Action agentStart(Observation theObservation) {
        int theAction = chooseAction(theObservation);
        theLearningModule.start(theObservation, theAction);

        return makeAction(theAction);
    }

    public Action agentStep(double reward, Observation theObservation) {
        int theAction = chooseAction(theObservation);
        theLearningModule.step(theObservation, reward, theAction);

        return makeAction(theAction);
    }

    public void agentEnd(double reward) {
        theLearningModule.end(reward);
        double currentEpsilon = theActionSelector.getEpsilon();
        double newEpsilon = currentEpsilon * .99d;
        theActionSelector.setEpsilon(newEpsilon);
    }


    protected int chooseAction(Observation theObservation) {
        return theActionSelector.sampleAction(theObservation, theLearningModule);
    }

    public void agent_cleanup() {
        this.theActionSelector.cleanup();
        this.theLearningModule.cleanup();
        theActionSelector = null;
        theLearningModule = null;
        theFAFactory = null;
        theParamHolder = null;
        theTaskObject = null;

    }


    private Action makeAction(int theNormalizeAction) {
        Action action = new Action(1, 0);/* The Action constructor takes two arguements: 1) the size of the int array 2) the size of the double array*/
        action.intArray[0] = theNormalizeAction - theTaskObject.getDiscreteActionRange(0).getMin(); /*Set the action value*/
        return action;
    }

    public double getValueForState(Observation theObservation) {
        //this could be called before init if things aren't synchronized
        if (theLearningModule == null) {
            return 0.0d;
        }

        double[] probabilities = theActionSelector.getActionProbabilities(theObservation, theLearningModule);
        double totalValue = 0.0d;
        for (int a = 0; a < actionCount; a++) {
            double thisActionValue = theLearningModule.queryNoSideEffect(theObservation, a);
            totalValue += probabilities[a] * thisActionValue;
        }
        return totalValue;
    }

}
