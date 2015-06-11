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

package edu.bbte.agentEpsilonGreedy.agentLib.learningModules.sarsaLambda;

import edu.bbte.agentEpsilonGreedy.agentLib.actionSelectors.ActionSelectorFactoryInterface;
import edu.bbte.agentEpsilonGreedy.agentLib.functionApproximators.FunctionApproximatorFactoryInterface;
import edu.bbte.agentEpsilonGreedy.agentLib.learningModules.AbstractLearningModuleFactory;
import edu.bbte.agentEpsilonGreedy.agentLib.learningModules.LearningModuleInterface;
import edu.bbte.packages.ParameterHolder;
import edu.bbte.packages.taskSpec.TaskSpec;

/**
 *
 * @author Brian Tanner
 */
public class SarsaLambdaLearningModuleFactory extends AbstractLearningModuleFactory {
    public SarsaLambdaLearningModuleFactory(FunctionApproximatorFactoryInterface FAF, ActionSelectorFactoryInterface ASF){
        super(FAF,ASF);
    }
    
    public LearningModuleInterface makeLearningModule(TaskSpec theTaskObject, ParameterHolder p) {
        return new SarsaLambdaLearningModule(theTaskObject, p, FAF);
    }


    @Override
    protected void addToPH(ParameterHolder p) {
       SarsaLambdaLearningModule.addToParameterHolder(p);
    }

}
