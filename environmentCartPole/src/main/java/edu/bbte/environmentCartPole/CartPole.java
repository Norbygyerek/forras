package edu.bbte.environmentCartPole;


import edu.bbte.environment.EnvironmentBase;
import edu.bbte.packages.ParameterHolder;
import edu.bbte.packages.taskSpec.TaskSpec;
import edu.bbte.packages.taskSpec.TaskSpecVRLGLUE3;
import edu.bbte.packages.taskSpec.ranges.DoubleRange;
import edu.bbte.packages.taskSpec.ranges.IntRange;
import edu.bbte.packages.types.Action;
import edu.bbte.packages.types.Observation;
import edu.bbte.packages.types.RewardObservationTerminal;

/**
 * This is based on David Finton's code from:
 * http://pages.cs.wisc.edu/~finton/poledriver.html which in turn is credited to
 * The Barto, Sutton, and Anderson cart-pole simulation. 
 * Available (not in 2008) by anonymous ftp from ftp.gte.com, as 
 * /pub/reinforcement-learning/pole.c.
 * 
 * @author btanner
 */
public class CartPole extends EnvironmentBase {

    final static double GRAVITY = 9.8;
    final static double MASSCART = 1.0;
    final static double MASSPOLE = 0.1;
    final static double TOTAL_MASS = (MASSPOLE + MASSCART);
    final static double LENGTH = 0.5;	  /* actually half the pole's length */

    final static double POLEMASS_LENGTH = (MASSPOLE * LENGTH);
    final static double FORCE_MAG = 10.0;
    final static double TAU = 0.02;	  /* seconds between state updates */

    final static double FOURTHIRDS = 4.0d / 3.0d;
    final static double DEFAULTLEFTCARTBOUND = -2.4;
    final static double DEFAULTRIGHTCARTBOUND = 2.4;
    final static double DEFAULTLEFTANGLEBOUND = -Math.toRadians(12.0d);
    final static double DEFAULTRIGHTANGLEBOUND = Math.toRadians(12.0d);
    double leftCartBound;
    double rightCartBound;
    double leftAngleBound;
    double rightAngleBound;    //State variables
    double x;			/* cart position, meters */

    double x_dot;			/* cart velocity */

    double theta;			/* pole angle, radians */

    double theta_dot;		/* pole angular velocity */


    public CartPole() {
        this(getDefaultParameters());
    }

    public CartPole(ParameterHolder p) {
        super();
        if (p != null) {
            if (!p.isNull()) {
                leftAngleBound = p.getDoubleParam("leftAngle");
                rightAngleBound = p.getDoubleParam("rightAngle");
                this.leftCartBound = p.getDoubleParam("leftCart");
                rightCartBound = p.getDoubleParam("rightCart");

            }
        }
    }


    public static ParameterHolder getDefaultParameters() {
    	
        ParameterHolder p = new ParameterHolder();

        p.addDoubleParam("Left Terminal Angle", DEFAULTLEFTANGLEBOUND);
        p.addDoubleParam("Right Terminal Angle", DEFAULTRIGHTANGLEBOUND);
        p.addDoubleParam("Terminal Left Cart Position", DEFAULTLEFTCARTBOUND);
        p.addDoubleParam("Terminal Right Cart Position", DEFAULTRIGHTCARTBOUND);

        p.setAlias("leftCart", "Terminal Left Cart Position");
        p.setAlias("rightCart", "Terminal Right Cart Position");
        p.setAlias("leftAngle", "Left Terminal Angle");
        p.setAlias("rightAngle", "Right Terminal Angle");
        return p;
    }


    /*RL GLUE METHODS*/
    public String envInit() {
    	
        x = 0.0f;
        x_dot = 0.0f;
        theta = 0.0f;
        theta_dot = 0.0f;

        return makeTaskSpec();
    }

    public Observation envStart() {
        x = 0.0f;
        x_dot = 0.0f;
        theta = 0.0f;
        theta_dot = 0.0f;
        return makeObservation();
    }

    public RewardObservationTerminal envStep(Action action) {
    	
        double xacc;
        double thetaacc;
        double force;
        double costheta;
        double sintheta;
        double temp;

        if (action.intArray[0] > 0) {
            force = FORCE_MAG;
        } else {
            force = -FORCE_MAG;
        }

        costheta = Math.cos(theta);
        sintheta = Math.sin(theta);

        temp = (force + POLEMASS_LENGTH * theta_dot * theta_dot * sintheta) / TOTAL_MASS;

        thetaacc = (GRAVITY * sintheta - costheta * temp) / (LENGTH * (FOURTHIRDS - MASSPOLE * costheta * costheta / TOTAL_MASS));

        xacc = temp - POLEMASS_LENGTH * thetaacc * costheta / TOTAL_MASS;

        /*** Update the four state variables, using Euler's method. ***/
        x += TAU * x_dot;
        x_dot += TAU * xacc;
        theta += TAU * theta_dot;
        theta_dot += TAU * thetaacc;

        while (theta >= Math.PI) {
            theta -= 2.0d * Math.PI;
        }
        while (theta < -Math.PI) {
            theta += 2.0d * Math.PI;
        }



        if (inFailure()) {
            return new RewardObservationTerminal(-1.0d, makeObservation(), 1);
        } else {
            return new RewardObservationTerminal(1.0d, makeObservation(), 0);
        }
    }


    @Override
    protected Observation makeObservation() {
    	
        Observation returnObs = new Observation(0, 4);
        returnObs.doubleArray[0] = x;
        returnObs.doubleArray[1] = x_dot;
        returnObs.doubleArray[2] = theta;
        returnObs.doubleArray[3] = theta_dot;

        return returnObs;
    }

    private boolean inFailure() {
        if (x < leftCartBound || x > rightCartBound || theta < leftAngleBound || theta > rightAngleBound) {
            return true;
        } /* to signal failure */
        return false;
    }

    public double getLeftCartBound() {
        return this.leftCartBound;
    }

    public double getRightCartBound() {
        return this.rightCartBound;
    }

    public double getRightAngleBound() {
        return this.rightAngleBound;
    }

    public double getLeftAngleBound() {
        return this.leftAngleBound;
    }

    private String makeTaskSpec() {

        double xMin = leftCartBound;
        double xMax = rightCartBound;

        //Dots are guesses
        double xDotMin = -6.0d;
        double xDotMax = 6.0d;
        double thetaMin = leftAngleBound;
        double thetaMax = rightAngleBound;
        double thetaDotMin = -6.0d;
        double thetaDotMax = 6.0d;

        TaskSpecVRLGLUE3 theTaskSpecObject = new TaskSpecVRLGLUE3();
        theTaskSpecObject.setEpisodic();
        theTaskSpecObject.setDiscountFactor(1.0d);
        theTaskSpecObject.addContinuousObservation(new DoubleRange(xMin, xMax));
        theTaskSpecObject.addContinuousObservation(new DoubleRange(xDotMin, xDotMax));
        theTaskSpecObject.addContinuousObservation(new DoubleRange(thetaMin, thetaMax));
        theTaskSpecObject.addContinuousObservation(new DoubleRange(thetaDotMin, thetaDotMax));
        theTaskSpecObject.addDiscreteAction(new IntRange(0, 1));
        theTaskSpecObject.setRewardRange(new DoubleRange(-1, 0));
        theTaskSpecObject.setExtra("EnvName:CartPole");

        String newTaskSpecString = theTaskSpecObject.toTaskSpec();
        TaskSpec.checkTaskSpec(newTaskSpecString);

        return newTaskSpecString;
    }

}