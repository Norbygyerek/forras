package edu.bbte.robo;


import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A RoboControl batyu aktivator osztálya
 * Informativ kiíratásokat tartalmaz, a batyu állapotáról
 * @author Gáll
 *
 */
public class RoboActivator implements BundleActivator  {
	
	private static final Logger logger = LoggerFactory.getLogger (RoboActivator.class);
	
    
    public void start(BundleContext context) throws Exception {
    	
    	logger.info("The RoboControl Interface Bundle Started Successfully!");
    	logger.info("Now you can reach the edu.bbte.roboControl package!");
   
    }
    
    public void stop(BundleContext context) throws Exception {
    	
    	logger.info("The RoboControl Interface Bundle Stopped Successfully!");
    	logger.info("Start again if you want to use the RoboControl Interface package!");
    }
    
}