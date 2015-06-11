/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.bbte.agentEpsilonGreedy.agentLib.learningBoosters.experienceReplay;

import edu.bbte.packages.types.Observation;

/**
 *
 * @author Brian Tanner
 */
public class DataPoint {

    public final Observation s;
    public final Observation sprime;
    public final int action;
    public final double reward;
    public final long uniqueSId;
    public final long uniqueSPrimeId;
    static final Object syncLock = new Object();
    static long nextUniqueStepId = 0;

    public DataPoint(Observation s, int action, double reward, Observation sprime) {
        this.s = s;
        this.action = action;
        this.reward = reward;
        this.sprime = sprime;

        synchronized (syncLock) {
            uniqueSId = nextUniqueStepId;
            nextUniqueStepId++;
            uniqueSPrimeId = nextUniqueStepId;
            nextUniqueStepId++;
        }

    }

    public DataPoint(Observation s, int action, double reward) {
        this(s, action, reward, null);
    }

}
