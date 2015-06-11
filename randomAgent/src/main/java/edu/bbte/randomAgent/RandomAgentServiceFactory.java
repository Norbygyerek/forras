package edu.bbte.randomAgent;

import org.osgi.framework.Bundle;
import org.osgi.framework.ServiceFactory;
import org.osgi.framework.ServiceRegistration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.bbte.agent.Agent;

/**
 * A RandomAgentServiceFactory példányosítja a 
 * RandomAgent osztályt. Arra használható, hogy amikor
 * regisztráljuk szervízként a RandomAgent batyut ezt a szervíz
 * factory osztályt adjuk meg paraméterként, így mindig amikor
 * egy batyu lekérdezi ezt a szervízt egy új példányt fog visszakapni.
 * @author Gáll
 *
 */
@SuppressWarnings("rawtypes")
public class RandomAgentServiceFactory implements ServiceFactory {
	
	private static final Logger logger = LoggerFactory.getLogger (RandomAgentServiceFactory.class);
	
    private int usageCounter = 0;
    
    public Object getService(Bundle bundle, ServiceRegistration registration) {
     
        usageCounter++;
    	
    	logger.info("Create object of RandomAgent for " + bundle.getSymbolicName());
    	logger.info("Number of bundles using service " + usageCounter);
        
        Agent agent = new RandomAgent();
        
        return agent;
    }
    
    
    public void ungetService(Bundle bundle, ServiceRegistration registration,Object service) {
    
    	usageCounter--;
    	
    	logger.info("Release object of RandomAgent for " + bundle.getSymbolicName());
    	logger.info("Number of bundles using service " + usageCounter);
    }


}