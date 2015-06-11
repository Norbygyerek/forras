package edu.bbte.agent;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Az Agent batyu aktivator osztálya
 * Informativ kiíratásokat tartalmaz, a batyu állapotáról
 * @author Gáll
 *
 */
public class AgentActivator implements BundleActivator  {
	
	private static final Logger logger = LoggerFactory.getLogger(AgentActivator.class);
	
    
    public void start(BundleContext context) throws Exception {
    	
    	logger.info("The Agent Interface Bundle Started Successfully!");
    	logger.info("Now you can import the edu.bbte.agent package!");
   
    }
    
    public void stop(BundleContext context) throws Exception {
    	
    	logger.info("The Agent Interface Bundle Stopped Successfully!");
    	logger.info("Start again if you want to use the Agent Interface package!");
    }
    
}