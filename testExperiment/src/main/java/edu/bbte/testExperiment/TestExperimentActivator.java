package edu.bbte.testExperiment;


import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.bbte.agent.Agent;
import edu.bbte.environment.Environment;
import edu.bbte.robo.Robo;
import edu.bbte.roboCommunication.RoboCommunication;

/**
 * A TestExperimentActivator osztály lekérdezi
 * a szükséges szolgáltatásokat és általuk
 * elidít egy tesztet
 * @author Gáll
 *
 */
public class TestExperimentActivator implements BundleActivator {
	
	private static final Logger logger = LoggerFactory.getLogger(TestExperimentActivator.class);

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
      	logger.info("STARTED the TestExperiment!");
    	
    }
    
    public void stop(BundleContext context) throws Exception {
    	
        context.ungetService(agentServiceReference[0]);
        context.ungetService(environmentServiceReference[0]);
        context.ungetService(roboCommunicationServiceReference[0]);
       
    	logger.info("STOPPED the TestExperiment!");
    }
    
    public void startExperiment() throws InvalidSyntaxException {
    	
    	// a szolgáltatások lekérdezése
    	agentServiceReference             =
    			bundleContext.getServiceReferences(Agent.class.getName(), "(AgentName=EpsilonGreedy)");
    	agent                             = (Agent) bundleContext.getService(agentServiceReference[0]);
        
        environmentServiceReference       = 
        		bundleContext.getServiceReferences(Environment.class.getName(), "(EnvironmentName=CartPole)");
        environment                       = (Environment)bundleContext.getService(environmentServiceReference[0]);
        
        roboCommunicationServiceReference = 
        		bundleContext.getServiceReferences(Robo.class.getName(), "(Robo=Communication)");
        roboCommunication                 = (RoboCommunication) bundleContext.getService(roboCommunicationServiceReference[0]);
        

    	// a roboCommunication beállítása a lekérdezett agent és environment példányokra
    	roboCommunication.setCommunication(environment, agent, "CartPole", "EpsilonGreedy");

    	// a TestExperiment osztály setExperiment() metódusa által beállításra kerül a kommunikáció
		TestExperiment.setExperiment(roboCommunication);
    }
    
}