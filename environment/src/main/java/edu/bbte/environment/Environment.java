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
*  $HeadURL: http://rl-glue-ext.googlecode.com/svn/trunk/projects/codecs/Java/src/org/rlcommunity/rlglue/codec/EnvironmentInterface.java $
* 
*/
package edu.bbte.environment;


import edu.bbte.packages.types.Action;
import edu.bbte.packages.types.Observation;
import edu.bbte.packages.types.RewardObservationTerminal;


/**
 * This is the interface that all environments should implement.
 * @author btanner
 */
/**
 * Az Environment interface, melyet minden Environment implementál
 * Az RL-Glue elgondolás megmaradt, viszont a
 * metódus nevek Javasra cserélődtek
 * @author Gáll
 *
 */
public interface Environment {
	
    public String                      envInit();
    public Observation                 envStart();
    public RewardObservationTerminal   envStep(Action action);

}
