package edu.bbte.agentEpsilonGreedy.agents;

import java.util.Vector;

import edu.bbte.agent.Agent;
import edu.bbte.agentEpsilonGreedy.agentLib.actionSelectors.ActionSelectorInterface;
import edu.bbte.agentEpsilonGreedy.agentLib.learningBoosters.AbstractLearningBoosterFactory;
import edu.bbte.agentEpsilonGreedy.agentLib.learningBoosters.LearningBoosterInterface;
import edu.bbte.agentEpsilonGreedy.agentLib.learningModules.AbstractLearningModuleFactory;
import edu.bbte.agentEpsilonGreedy.agentLib.learningModules.LearningModuleInterface;
import edu.bbte.agentEpsilonGreedy.agentLib.rewardModifiers.AbstractRewardModifierFactory;
import edu.bbte.agentEpsilonGreedy.agentLib.rewardModifiers.RewardModifierInterface;
import edu.bbte.agentEpsilonGreedy.agentLib.rewardModifiers.nullRewardModifier.NullRewardModifierFactory;
import edu.bbte.packages.ParameterHolder;
import edu.bbte.packages.taskSpec.TaskSpec;
import edu.bbte.packages.types.Action;
import edu.bbte.packages.types.Observation;

public abstract class AbstractSarsa implements Agent {

    protected int actionCount;
    protected LearningModuleInterface theLearningModule = null;
    protected Vector<LearningBoosterInterface> theBoosters = null;
    protected ActionSelectorInterface theActionSelector = null;
    protected RewardModifierInterface theRewardModifier = null;
    protected TaskSpec theTaskObject = null;
    protected ParameterHolder theParamHolder;

    public static ParameterHolder getDefaultParameters() {
        ParameterHolder p = new ParameterHolder();
        return p;
    }

    protected abstract AbstractLearningModuleFactory makeLearningModuleFactory();

    protected abstract Vector<AbstractLearningBoosterFactory> makeBoosterFactories();

    /**
     * Override this if yoi want to have reward modifiers.
     * @return
     */
    protected AbstractRewardModifierFactory makeRewardModifierFactory() {
        return new NullRewardModifierFactory();
    }

    public static ParameterHolder getDefaultParameters(AbstractLearningModuleFactory LMF) {
        ParameterHolder p = getDefaultParameters();
        LMF.addToParameterHolder(p);
        return p;
    }

    public AbstractSarsa() {
        this(AbstractSarsa.getDefaultParameters());
    }

    public AbstractSarsa(ParameterHolder p) {
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
        theBoosters = new Vector<LearningBoosterInterface>();
        theTaskObject = new TaskSpec(theTaskSpec);

        actionCount = 1 + theTaskObject.getDiscreteActionRange(0).getMax() - theTaskObject.getDiscreteActionRange(0).getMin();
        assert (actionCount > 0);

        //here we're asserting there IS only one discrete action variable. 
        assert (theTaskObject.getNumDiscreteActionDims() == 1); //check the number of discrete actions is only 1
        assert (theTaskObject.getNumContinuousActionDims() == 0); //check that there is no continuous actions

        AbstractLearningModuleFactory theLMF = makeLearningModuleFactory();
        theActionSelector = theLMF.makeActionSelector(theTaskObject, theParamHolder);
        theLearningModule = theLMF.makeLearningModule(theTaskObject, theParamHolder);

        Vector<AbstractLearningBoosterFactory> theBoosterFactories = makeBoosterFactories();

        for (AbstractLearningBoosterFactory thisBoosterFactory : theBoosterFactories) {
            theBoosters.add(thisBoosterFactory.makeLearningBooster(theTaskObject, theParamHolder, theLearningModule, theActionSelector));
        }

        AbstractRewardModifierFactory theRewardModifierFactory = makeRewardModifierFactory();
        theRewardModifier = theRewardModifierFactory.makeRewardModifier(theTaskObject, theParamHolder);
    }

    public Action agentStart(Observation theObservation) {
        int thePrimitiveAction = chooseAction(theObservation);
        theLearningModule.start(theObservation, thePrimitiveAction);
        for (LearningBoosterInterface thisBooster : theBoosters) {
            thisBooster.start(theObservation, thePrimitiveAction);
        }

        theRewardModifier.start(theObservation);

        return makeAction(thePrimitiveAction);
    }

    public Action agentStep(double originalReward, Observation theObservation) {
        int thePrimitiveAction = chooseAction(theObservation);
        double modifiedReward = theRewardModifier.step(theObservation, originalReward);

        theLearningModule.step(theObservation, modifiedReward, thePrimitiveAction);
        for (LearningBoosterInterface thisBooster : theBoosters) {
            thisBooster.step(theObservation, modifiedReward, thePrimitiveAction);
        }
        return makeAction(thePrimitiveAction);
    }

    public void agentEnd(double originalReward) {
        double modifiedReward = theRewardModifier.end(originalReward);
        theLearningModule.end(modifiedReward);
        for (LearningBoosterInterface thisBooster : theBoosters) {
            thisBooster.end(modifiedReward);
        }
    }


    protected int chooseAction(Observation theObservation) {
        return theActionSelector.sampleAction(theObservation, theLearningModule);
    }


    private Action makeAction(int theNormalizeAction) {
        Action action = new Action(1, 0);/* The Action constructor takes two arguements: 1) the size of the int array 2) the size of the double array*/
        action.intArray[0] = theNormalizeAction - theTaskObject.getDiscreteActionRange(0).getMin(); /*Set the action value*/
        return action;
    }

    public double getValueForStateAction(Observation theObservation, int whichAction) {
        //this could be called before init if things aren't synchronized
        if (theLearningModule == null) {
            return 0.0d;
        }
        return theLearningModule.queryNoSideEffect(theObservation, whichAction);
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
