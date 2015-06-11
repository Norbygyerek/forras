package edu.bbte.agentEpsilonGreedy;

import java.util.Hashtable;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.Constants;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.ServiceRegistration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.bbte.agent.Agent;
import edu.bbte.agentEnvironmentList.AgentEnvironmentList;


/**
 * A EpsilonGreedy szerviz Activator osztalya
 * Be regisztrálja a szervízt az OSGi környzet Registry tárolójába,
 * amely elérhető lesz egyéb batyuk számára
 * @author Gáll
 *
 */
public class EpsilonGreedyActivator implements BundleActivator  {
	
	private static final Logger logger = LoggerFactory.getLogger (EpsilonGreedyActivator.class);
	
	private ServiceRegistration<?> agentServiceReg;
	private ServiceReference<?> getAgentEnvironmentReference;
	private AgentEnvironmentList list;
	
	public void start(BundleContext context) throws Exception {
    	
		EpsilonGreedyServiceFactory agentServiceFactory  = new EpsilonGreedyServiceFactory();
		Hashtable<String, String> dictionary             = new Hashtable<String, String>();
    	
    	dictionary.put(Constants.SERVICE_DESCRIPTION, "EpsilonGreedy Bundle. You can use with every test!");
    	dictionary.put("AgentName", "EpsilonGreedy");
    	
    	agentServiceReg  = context.registerService(Agent.class.getName(), agentServiceFactory, dictionary);
    	
    	if (agentServiceReg != null) {
    		
        	logger.info("The EpsilonGreedy service Bundle Started Successfully!");
        	logger.info("Now you can reach the edu.bbte.agentEpsilonGreedy package!");
        
        	getAgentEnvironmentReference = context.getServiceReference(AgentEnvironmentList.class.getName());
        	list          = (AgentEnvironmentList) context.getService(getAgentEnvironmentReference);
        	
        	list.addAgent("AgentName=EpsilonGreedy");
        	
    	}
   
    }
    
    public void stop(BundleContext context) throws Exception {

    	agentServiceReg.unregister();
    	 
    	list.removeAgent("AgentName=EpsilonGreedy");
    	
     	logger.info("The EpsilonGreedy service Bundle Stopped Successfully!");
     	logger.info("Start again if you want to use the EpsilonGreedy service!");
     	
    }
    
}