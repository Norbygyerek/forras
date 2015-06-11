/* 
* Copyright (C) 2007, Brian Tanner
* 
http://rl-glue-ext.googlecode.com/

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

     http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
* 
*  $Revision: 489 $
*  $Date: 2009-01-31 14:34:21 -0700 (Sat, 31 Jan 2009) $
*  $Author: brian@tannerpages.com $
*  $HeadURL: http://rl-glue-ext.googlecode.com/svn/trunk/projects/codecs/Java/src/org/rlcommunity/rlglue/codec/AgentInterface.java $
* 
*/

package edu.bbte.agent;

import edu.bbte.packages.types.Action;
import edu.bbte.packages.types.Observation;

/**
 * 
 * This is the interface that all agents should implement.
 * <p> Changes
 * <ul>
 * <li>Agent_freeze no longer exists as per RL-Glue 3.x spec.
 * </ul>
 * @author btanner
 */
/**
 * Az Agent interface, melyet minden Agent impelentál
 * Az RL-Glue elgondolás megmaradt, viszont a
 * metódus nevek Javasra cserélődtek
 * @author Gáll
 *
 */
public interface Agent {
	 
    public void   agentInit(final String taskSpecification);
    public Action agentStart(Observation observation);
    public Action agentStep(double reward, Observation observation);
    public void   agentEnd(double reward);
}
