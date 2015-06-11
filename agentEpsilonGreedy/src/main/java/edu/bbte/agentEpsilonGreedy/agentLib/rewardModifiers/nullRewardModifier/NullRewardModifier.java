package edu.bbte.agentEpsilonGreedy.agentLib.rewardModifiers.nullRewardModifier;


import edu.bbte.agentEpsilonGreedy.agentLib.rewardModifiers.RewardModifierInterface;
import edu.bbte.packages.ParameterHolder;
import edu.bbte.packages.taskSpec.TaskSpec;
import edu.bbte.packages.types.Observation;

/**
 * This class will generate potential function rewards from s,s' using the difference
 * between their CONTINUOUS-VALUED state variables only.
 *
 * The observations will NOT be scaled to be in [0,1].  The actual Euclidean distance is used
 * to calculate the difference between subsequent observations.
 *
 * Unlike the Potential Function Scaling used by Marc Bellemare in his PotentialFuncContinuousGridWorld,
 * we will multiply the distance by the scaling factor, not divide by it.
 *
 * This is nice because it means if we set the scaling factor to 0, we have no scaling.
 * @author btanner
 */
public final class NullRewardModifier implements RewardModifierInterface {

    public static void addToParameterHolder(ParameterHolder p) {
    }

    public NullRewardModifier(TaskSpec theTaskObject, ParameterHolder p) {

    }

    public void init() {

    }

    public void start(Observation theObservation) {
    }

    public double step(Observation theObservation, double r) {
    return 0;
   }

    public double end(double r) {
        return 0;
    }

    public void cleanup() {
    }
}
