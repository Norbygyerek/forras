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
package edu.bbte.agentEpsilonGreedy.agentLib.learningModules;

import edu.bbte.agentEpsilonGreedy.agentLib.actionSelectors.ActionSelectorFactoryInterface;
import edu.bbte.agentEpsilonGreedy.agentLib.actionSelectors.ActionSelectorInterface;
import edu.bbte.agentEpsilonGreedy.agentLib.functionApproximators.FunctionApproximatorFactoryInterface;
import edu.bbte.packages.ParameterHolder;
import edu.bbte.packages.taskSpec.TaskSpec;

/**
 *
 * @author btanner
 */
public abstract class AbstractLearningModuleFactory {

    protected FunctionApproximatorFactoryInterface FAF = null;
    protected ActionSelectorFactoryInterface ASF = null;

    public AbstractLearningModuleFactory(FunctionApproximatorFactoryInterface FAF, ActionSelectorFactoryInterface ASF) {
        this.FAF = FAF;
        this.ASF = ASF;
    }

    public ActionSelectorInterface makeActionSelector(TaskSpec theTaskObject, ParameterHolder p) {
        return ASF.makeActionSelector(theTaskObject, p);
    }

    abstract public LearningModuleInterface makeLearningModule(TaskSpec theTaskObject, ParameterHolder p);

    abstract protected void addToPH(ParameterHolder p);

    public void addToParameterHolder(ParameterHolder p) {
        addToPH(p);
        FAF.addToParameterHolder(p);
        ASF.addToParameterHolder(p);
    }
}
