package edu.bbte.agentEpsilonGreedy.agents;

import edu.bbte.agent.Agent;
import edu.bbte.agentEpsilonGreedy.agentLib.actionSelectors.epsilonGreedy.EpsilonGreedyActionSelector;
import edu.bbte.agentEpsilonGreedy.agentLib.functionApproximators.CMAC.CMACFunctionApproximatorFactory;
import edu.bbte.agentEpsilonGreedy.agentLib.functionApproximators.CMAC.NDCMACTileCoder;
import edu.bbte.agentEpsilonGreedy.agentLib.learningBoosters.experienceReplay.ExpectedExperienceReplayLambdaLearningBooster;
import edu.bbte.agentEpsilonGreedy.agentLib.learningModules.sarsaLambda.SarsaLambdaLearningModule;
import edu.bbte.packages.ParameterHolder;
import edu.bbte.packages.taskSpec.TaskSpec;
import edu.bbte.packages.types.Action;
import edu.bbte.packages.types.Observation;

public class SRRLAgent implements Agent {

    protected int actionCount;
    protected SarsaLambdaLearningModule theLearningModule = null;
    protected ExpectedExperienceReplayLambdaLearningBooster theBooster = null;
    protected EpsilonGreedyActionSelector theActionSelector = null;
    protected TaskSpec theTaskObject = null;
    protected ParameterHolder theParamHolder;

    public static ParameterHolder getDefaultParameters() {
        ParameterHolder p = new ParameterHolder();
        return p;
    }

    public SRRLAgent() {
        this(SRRLAgent.getDefaultParameters());
    }

    public SRRLAgent(ParameterHolder p) {
        super();
        this.theParamHolder = p;
        SarsaLambdaLearningModule.addToParameterHolder(p);
        ExpectedExperienceReplayLambdaLearningBooster.addToParameterHolder(p);
        EpsilonGreedyActionSelector.addToParameterHolder(p);
        NDCMACTileCoder.addToParameterHolder(p);    

        theParamHolder.setDoubleParam("sarsalambda-alpha", .125d);
        theParamHolder.setDoubleParam("sarsalambda-lambda", 0.0d);
        theParamHolder.setIntegerParam("e-replay-lesson-size", 16);
        theParamHolder.setIntegerParam("e-replay-replay-count", 64);
        theParamHolder.setDoubleParam("e-replay-lambda", 0.9d);
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
        

        theActionSelector = new EpsilonGreedyActionSelector(theTaskObject, theParamHolder);
        theLearningModule = new SarsaLambdaLearningModule(theTaskObject, theParamHolder, new CMACFunctionApproximatorFactory());
        theBooster = new ExpectedExperienceReplayLambdaLearningBooster(theTaskObject, theParamHolder, theLearningModule, theActionSelector);
    }

    public Action agentStart(Observation theObservation) {
        int theAction = chooseAction(theObservation);
        theLearningModule.start(theObservation, theAction);
        theBooster.start(theObservation, theAction);

        return makeAction(theAction);
    }

    public Action agentStep(double reward, Observation theObservation) {
        int theAction = chooseAction(theObservation);
        theLearningModule.step(theObservation, reward, theAction);
        theBooster.step(theObservation, reward, theAction);
        return makeAction(theAction);
    }

    public void agentEnd(double reward) {
        theLearningModule.end(reward);
        theBooster.end(reward);
    }

    protected int chooseAction(Observation theObservation) {
        return theActionSelector.sampleAction(theObservation, theLearningModule);
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
