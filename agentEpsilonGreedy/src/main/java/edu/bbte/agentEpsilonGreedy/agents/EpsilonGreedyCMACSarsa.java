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
package edu.bbte.agentEpsilonGreedy.agents;



import java.util.Vector;

import edu.bbte.agentEpsilonGreedy.agentLib.actionSelectors.ActionSelectorFactoryInterface;
import edu.bbte.agentEpsilonGreedy.agentLib.actionSelectors.epsilonGreedy.EpsilonGreedyActionSelectorFactory;
import edu.bbte.agentEpsilonGreedy.agentLib.functionApproximators.FunctionApproximatorFactoryInterface;
import edu.bbte.agentEpsilonGreedy.agentLib.functionApproximators.CMAC.CMACFunctionApproximatorFactory;
import edu.bbte.agentEpsilonGreedy.agentLib.learningBoosters.AbstractLearningBoosterFactory;
import edu.bbte.agentEpsilonGreedy.agentLib.learningModules.AbstractLearningModuleFactory;
import edu.bbte.agentEpsilonGreedy.agentLib.learningModules.sarsa0.Sarsa0LearningModuleFactory;
import edu.bbte.packages.ParameterHolder;

/**
 *
 * @author Brian Tanner
 */
public class EpsilonGreedyCMACSarsa extends AbstractSarsa {

    public static ParameterHolder getDefaultParameters() {
        FunctionApproximatorFactoryInterface FAF=new CMACFunctionApproximatorFactory();
        ActionSelectorFactoryInterface ASF=new EpsilonGreedyActionSelectorFactory();
        Sarsa0LearningModuleFactory S0LMF=new Sarsa0LearningModuleFactory(FAF, ASF);
        ParameterHolder p = AbstractSarsa.getDefaultParameters(S0LMF);
        return p;
    }
   @Override
    protected AbstractLearningModuleFactory makeLearningModuleFactory() {
        return new Sarsa0LearningModuleFactory(makeFunctionApproximatorFactory(), makeActionSelectorFactory());
    }
    
    protected FunctionApproximatorFactoryInterface makeFunctionApproximatorFactory() {
        return new CMACFunctionApproximatorFactory();
    }

    protected ActionSelectorFactoryInterface makeActionSelectorFactory() {
        return new EpsilonGreedyActionSelectorFactory();
    }

   public EpsilonGreedyCMACSarsa(ParameterHolder p){
        super(p);
    }

    public EpsilonGreedyCMACSarsa(){
        this(getDefaultParameters());
    }
    
    @Override
    protected Vector<AbstractLearningBoosterFactory> makeBoosterFactories() {
       return new Vector<AbstractLearningBoosterFactory>();
    }

}
