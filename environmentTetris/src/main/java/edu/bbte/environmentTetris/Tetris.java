/*
Copyright 2007 Brian Tanner
http://rl-library.googlecode.com/
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
package edu.bbte.environmentTetris;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.bbte.environment.EnvironmentBase;
import edu.bbte.packages.ParameterHolder;
import edu.bbte.packages.taskSpec.TaskSpec;
import edu.bbte.packages.taskSpec.TaskSpecVRLGLUE3;
import edu.bbte.packages.taskSpec.ranges.DoubleRange;
import edu.bbte.packages.taskSpec.ranges.IntRange;
import edu.bbte.packages.types.Action;
import edu.bbte.packages.types.Observation;
import edu.bbte.packages.types.RewardObservationTerminal;

public class Tetris extends EnvironmentBase {

	private static final Logger logger = LoggerFactory.getLogger (Tetris.class);
	
	private int currentScore        = 0;
	protected TetrisState gameState = null;
	static final int terminalScore  = 0;

	public Tetris() {
		this(getDefaultParameters());
	}

	public Tetris(ParameterHolder p) {
		super();
		gameState = new TetrisState();
	}

	/**
	 * Tetris doesn't really have any parameters
	 * @return
	 */
	public static ParameterHolder getDefaultParameters() {

		ParameterHolder p = new ParameterHolder();

		return p;
	}

	public String envInit() {

		return makeTaskSpec();
	}


	public Observation envStart() {

		gameState.reset();
		gameState.spawn_block();
		gameState.blockMobile = true;
		currentScore = 0;

		Observation o = gameState.getObservation();
		return o;
	}

	public RewardObservationTerminal envStep(Action actionObject) {

		int theAction = 0;

		try {

			theAction = actionObject.intArray[0];

		} catch (ArrayIndexOutOfBoundsException e) {

			logger.error("Error: Action was expected to have 1 dimension but got ArrayIndexOutOfBoundsException when trying to get element 0:" + e);
			logger.error("Error: Choosing action 0");
			theAction = 0;
		}

		if (theAction > 5 || theAction < 0) {

			System.err.println("Invalid action selected in Tetrlais: " + theAction);
			theAction = gameState.getRandom().nextInt(5);
		}

		if (gameState.blockMobile) {
			gameState.take_action(theAction);
			gameState.update();

		} else {

			gameState.spawn_block();
		}

		RewardObservationTerminal ro = new RewardObservationTerminal();

		ro.terminal = 1;
		ro.o = gameState.getObservation();

		if (!gameState.gameOver()) {

			ro.terminal = 0;
			ro.r = gameState.get_score() - currentScore;
			currentScore = gameState.get_score();

		} else {

			ro.r = Tetris.terminalScore;
			currentScore = 0;
		}

		return ro;
	}



	/*End of Base RL-Glue Functions */
	/*RL-Viz Methods*/
	@Override
	protected Observation makeObservation() {
		return gameState.getObservation();
	}


	private String makeTaskSpec() {
		int boardSize = gameState.getHeight() * gameState.getWidth();
		int numPieces = gameState.possibleBlocks.size();

		TaskSpecVRLGLUE3 theTaskSpecObject = new TaskSpecVRLGLUE3();
		theTaskSpecObject.setEpisodic();
		theTaskSpecObject.setDiscountFactor(1.0d);
		//First add the binary variables for the board
		theTaskSpecObject.addDiscreteObservation(new IntRange(0, 1, boardSize));
		//Now the binary features to tell what piece is falling
		theTaskSpecObject.addDiscreteObservation(new IntRange(0, 1, numPieces));
		//Now the actual board size in the observation. The reason this was here is/was because
		//there was no way to add meta-data to the task spec before.
		//First height
		theTaskSpecObject.addDiscreteObservation(new IntRange(gameState.getHeight(), gameState.getHeight()));
		//Then width
		theTaskSpecObject.addDiscreteObservation(new IntRange(gameState.getWidth(), gameState.getWidth()));

		theTaskSpecObject.addDiscreteAction(new IntRange(0, 5));
		//This is actually a lie... the rewards aren't in that range.
		theTaskSpecObject.setRewardRange(new DoubleRange(0, 8.0d));

		//This is a better way to tell the rows and cols
		theTaskSpecObject.setExtra("EnvName:Tetris HEIGHT:" + gameState.getHeight() + " WIDTH:" + gameState.getWidth() + " Revision: " + this.getClass().getPackage().getImplementationVersion());

		String taskSpecString = theTaskSpecObject.toTaskSpec();

		TaskSpec.checkTaskSpec(taskSpecString);
		return taskSpecString;
	}


}

