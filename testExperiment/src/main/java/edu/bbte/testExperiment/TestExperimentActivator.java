package edu.bbte.testExperiment;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceReference;

import edu.bbte.agent.Agent;
import edu.bbte.environment.Environment;
import edu.bbte.robo.Robo;
import edu.bbte.roboCommunication.RoboCommunication;

public class TestExperimentActivator implements BundleActivator {
	
	private static final Logger logger = Logger.getLogger(TestExperimentActivator.class.getSimpleName());

	private  ServiceReference<?>[] agentServiceReference;
	private  ServiceReference<?>[] environmentServiceReference;
	private  ServiceReference<?>[] roboCommunicationServiceReference;

	
	
	private Agent              agent;
	private Environment        environment;
	private RoboCommunication  roboCommunication;

	private BundleContext bundleContext;

    public void start(BundleContext context) throws Exception {

    	bundleContext = context;
    	

    
    	Thread thread = new Thread() {
    		
    	    public void run() {
	    	     try {
					startExperiment();
				} catch (InvalidSyntaxException e) {
					e.printStackTrace();
				}
    	    }
    	};

    	  thread.start();
      	logger.log(Level.INFO,"STARTED the TestExperiment!");
    	
    }
    
    public void stop(BundleContext context) throws Exception {
    	
        context.ungetService(agentServiceReference[0]);
        context.ungetService(environmentServiceReference[0]);
        context.ungetService(roboCommunicationServiceReference[0]);
       
    	logger.log(Level.INFO,"STOPPED the TestExperiment!");
    }
    
    public void startExperiment() throws InvalidSyntaxException {
    	
    	agentServiceReference             =
    			bundleContext.getServiceReferences(Agent.class.getName(), "(AgentName=EpsilonGreedy)");
    	agent                             = (Agent) bundleContext.getService(agentServiceReference[0]);
        
        environmentServiceReference       = 
        		bundleContext.getServiceReferences(Environment.class.getName(), "(EnvironmentName=CartPole)");
        environment                       = (Environment)bundleContext.getService(environmentServiceReference[0]);
        
        roboCommunicationServiceReference = 
        		bundleContext.getServiceReferences(Robo.class.getName(), "(Robo=Communication)");
        roboCommunication                 = (RoboCommunication) bundleContext.getService(roboCommunicationServiceReference[0]);
        

    	
    	
    	roboCommunication.setCommunication(environment, agent, "CartPole", "EpsilonGreedy");


		TestExperiment.setExperiment(roboCommunication);
    }
    
}