package edu.bbte.environment;


import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Az Environment batyu aktivator osztálya
 * Informativ kiíratásokat tartalmaz, a batyu állapotáról
 * @author Gáll
 *
 */
public class EnvironmentActivator implements BundleActivator  {
	
	private static final Logger logger = LoggerFactory.getLogger (EnvironmentActivator.class);
	
    
    public void start(BundleContext context) throws Exception {
    	
    	logger.info("The Environment Interface Bundle Started Successfully!");
    	logger.info("Now you can import the edu.bbte.environment package!");
   
    }
    
    public void stop(BundleContext context) throws Exception {
    	
    	logger.info("The Environment Interface Bundle Stopped Successfully!");
    	logger.info("Start again if you want to use the Environment Interface package!");
    }
    
}