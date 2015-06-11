package edu.bbte.environmentAcrobot;

import java.util.Random;

import edu.bbte.environment.EnvironmentBase;
import edu.bbte.packages.ParameterHolder;
import edu.bbte.packages.taskSpec.TaskSpec;
import edu.bbte.packages.taskSpec.TaskSpecVRLGLUE3;
import edu.bbte.packages.taskSpec.ranges.DoubleRange;
import edu.bbte.packages.taskSpec.ranges.IntRange;
import edu.bbte.packages.types.Action;
import edu.bbte.packages.types.Observation;
import edu.bbte.packages.types.RewardObservationTerminal;

public class Acrobot extends EnvironmentBase {

    final static int stateSize = 4;
    final static int numActions = 3;
    final static double maxTheta1 = Math.PI;
    final static double maxTheta2 = Math.PI;
    final static double maxTheta1Dot = 4 * Math.PI;
    final static double maxTheta2Dot = 9 * Math.PI;
    final static double m1 = 1.0;
    final static double m2 = 1.0;
    final static double l1 = 1.0;
    final static double l2 = 1.0;
    final static double lc1 = 0.5;
    final static double lc2 = 0.5;
    final static double I1 = 1.0;
    final static double I2 = 1.0;
    final static double g = 9.8;
    final static double dt = 0.05;
    final static double acrobotGoalPosition = 1.0;
    /*State Variables*/
    private double theta1,  theta2,  theta1Dot,  theta2Dot;
    private int seed = 0;
    private Random ourRandomNumber = new Random(seed);
    boolean setRandomStarts; //if true then do random starts, else, start at static position

    
    /*Constructor Business*/
    public Acrobot() {
        this(getDefaultParameters());
    }

    public Acrobot(ParameterHolder p) {
        super();
        if (p != null) {
            if (!p.isNull()) {
                setRandomStarts = p.getBooleanParam("rstarts");
            }
        }
    }
    //This method creates the object that can be used to easily set different problem parameters
    public static ParameterHolder getDefaultParameters() {
    	
        ParameterHolder p = new ParameterHolder();
        p.addBooleanParam("Random Starts", false);
        p.setAlias("rstarts", "Random Starts");
        
        return p;
    }
    
    
    private String makeTaskSpec(){
        TaskSpecVRLGLUE3 theTaskSpecObject = new TaskSpecVRLGLUE3();
        theTaskSpecObject.setEpisodic();
        theTaskSpecObject.setDiscountFactor(1.0d);
        theTaskSpecObject.addContinuousObservation(new DoubleRange(-maxTheta1, maxTheta1));
        theTaskSpecObject.addContinuousObservation(new DoubleRange(-maxTheta2, maxTheta2));
        theTaskSpecObject.addContinuousObservation(new DoubleRange(-maxTheta1Dot, maxTheta1Dot));
        theTaskSpecObject.addContinuousObservation(new DoubleRange(-maxTheta2Dot, maxTheta2Dot));

        theTaskSpecObject.addDiscreteAction(new IntRange(0, numActions - 1));

        //Apparently we don't say the reward range.
        theTaskSpecObject.setRewardRange(new DoubleRange());
        theTaskSpecObject.setExtra("EnvName:Acrobot");
        String taskSpecString = theTaskSpecObject.toTaskSpec();
        TaskSpec.checkTaskSpec(taskSpecString);
        return taskSpecString;        
    }



    /*Beginning of RL-GLUE methods*/
    public String envInit() {
    	
        return makeTaskSpec();
    }

    public Observation envStart() {
    	
        if (setRandomStarts) {
            set_initial_position_random();
        } else {
            set_initial_position_at_bottom();
        }
        return makeObservation();
    }

    public RewardObservationTerminal envStep(Action a) {
    	
        if ((a.intArray[0] < 0) || (a.intArray[0] > 2)) {
            System.out.printf("Invalid action %d, selecting an action randomly\n", a.intArray[0]);
            a.intArray[0] = (int) ourRandomNumber.nextDouble() * 3;
        }
        
        double torque = a.intArray[0] - 1.0;
        double d1;
        double d2;
        double phi_2;
        double phi_1;

        double theta2_ddot ;
        double theta1_ddot;

        int count = 0;
        
        while (!test_termination() && count < 4) {
            count++;

            d1 = m1 * Math.pow(lc1, 2) + m2 * ( Math.pow(l1, 2) + Math.pow(lc2, 2) + 2 * l1 * lc2 * Math.cos(theta2)) + I1 + I2;
            d2 = m2 * (Math.pow(lc2, 2) + l1 * lc2 * Math.cos(theta2)) + I2;

            phi_2 = m2 * lc2 * g * Math.cos(theta1 + theta2 - Math.PI / 2.0);
            phi_1 = -(m2 * l1 * lc2 * Math.pow(theta2Dot, 2) * Math.sin(theta2) -2 * m2 * l1 * lc2 * theta1Dot * theta2Dot * Math.sin(theta2)) + (m1 * lc1 + m2 * l1) * g * Math.cos(theta1 - Math.PI / 2.0) + phi_2;

            theta2_ddot = (torque + (d2 / d1) * phi_1 - m2*l1*lc2*Math.pow(theta1Dot,2)*Math.sin(theta2)- phi_2) / (m2 * Math.pow(lc2, 2) + I2 - Math.pow(d2, 2) / d1);
            theta1_ddot = - (d2 * theta2_ddot + phi_1) / d1;

            theta1Dot += theta1_ddot * dt;
            theta2Dot += theta2_ddot * dt;

            theta1 += theta1Dot * dt;
            theta2 += theta2Dot * dt;
        }
        if (Math.abs(theta1Dot)>maxTheta1Dot){ theta1Dot = Math.signum(theta1Dot)*maxTheta1Dot;}

        if (Math.abs(theta2Dot)>maxTheta2Dot){ theta2Dot = Math.signum(theta2Dot)*maxTheta2Dot;}
        /* Put a hard constraint on the Acrobot physics, thetas MUST be in [-PI,+PI]
         * if they reach a top then angular velocity becomes zero
         */
        if (Math.abs(theta2)>Math.PI){ theta2 = Math.signum(theta2)*Math.PI; theta2Dot=0; }
        if (Math.abs(theta1)>Math.PI){ theta1 = Math.signum(theta1)*Math.PI; theta1Dot=0;}

        RewardObservationTerminal ro = new RewardObservationTerminal();
        ro.r = -1;
        ro.o = makeObservation();

        ro.terminal = 0;
        
        if (test_termination()) {
            ro.r=0.0d;
            ro.terminal = 1;
        }
        return ro;
    }

    @Override
    protected Observation makeObservation() {
    	
        Observation obs = new Observation(0, 4);
        obs.doubleArray[0] = theta1;
        obs.doubleArray[1] = theta2;

        obs.doubleArray[2] = theta1Dot;
        obs.doubleArray[3] = theta2Dot;
        return obs;
    }


    private void set_initial_position_random() {
        theta1 = (ourRandomNumber.nextDouble() * (Math.PI + Math.abs(-Math.PI)) + (-Math.PI)) * 0.1;
        theta2 = (ourRandomNumber.nextDouble() * (Math.PI + Math.abs(-Math.PI)) + (-Math.PI)) * 0.1;
        theta1Dot = (ourRandomNumber.nextDouble() * (maxTheta1Dot * 2) - maxTheta1Dot) * 0.1;
        theta2Dot = (ourRandomNumber.nextDouble() * (maxTheta2Dot * 2) - maxTheta2Dot) * 0.1;
    }

    private void set_initial_position_at_bottom() {
        theta1 = theta2 = 0.0;
        theta1Dot = theta2Dot = 0.0;
    }
    
    private boolean test_termination() {
    	//Brian: Nov 2008 I don't believe that this is true, but I'd like it to be true.
    	//I'm going to try to recalculate this from code that I believe is working in the 
    	//visualizer            
    	        double feet_height = -(l1 * Math.cos(theta1) + l2 * Math.cos(theta2));

    	        //New Code
    	        double firstJointEndHeight = l1 * Math.cos(theta1);
    	        //Second Joint height (relative to first joint)
    	        double secondJointEndHeight = l2 * Math.sin(Math.PI / 2 - theta1 - theta2);
    	        feet_height = -(firstJointEndHeight + secondJointEndHeight);
    	        return (feet_height > acrobotGoalPosition);
    	    }

}
