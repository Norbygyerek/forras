/*
Copyright 2007 Brian Tanner
brian@tannerpages.com
http://brian.tannerpages.com

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
*/

  
package edu.bbte.robo;

import edu.bbte.packages.types.Action;
import edu.bbte.packages.types.Observation;
import edu.bbte.packages.types.ObservationAction;
import edu.bbte.packages.types.RewardObservationActionTerminal;
import edu.bbte.packages.types.RewardObservationTerminal;

/**
 * This is the definition of an RLGlue 'engine'.  The network codec is one such engine.
 * <p>If you want to create your own network codec, or a 'direct-compile' RL-Glue 
 * engine (like the one in RL-Viz), you should implement this interface.
 * @since 2.0
 * @author btanner
 */
/**
 * A RoboControl interfész, melyet a RoboControl és
 *  RoboCommunication szervíz implementál
 *  Az RL-Glue elgondolás megmaradt,
 *  viszont kiegészítésre került, és a metódusnevek
 *  Javas -ra lettek cserélve
 * @author Gáll
 *
 */
public interface Robo {
	
	public String                             roboInit();
	public ObservationAction                  roboStart();
    public RewardObservationActionTerminal    roboStep();
	public int                                roboEpisode(int numSteps, int numEpisodes);

    public Observation                        roboEnvStart();
    public RewardObservationTerminal          roboEnvStep(Action theAction);
    
    public Action                             roboAgentStart(Observation theObservation);
    public Action                             roboAgentStep(double theReward, Observation theObservation);
    public void                               roboAgentEnd(double theReward);

	
	public double                             roboReturn();
	
	public int                                roboNumOfSteps();
	public int                                roboNumOfEpisodes();
	public int 								  getPercent();
	public void                               terminate();

}
