package edu.bbte.packages;


import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A Packages batyu aktivator osztálya
 * Informativ kiíratásokat tartalmaz, a batyu állapotáról
 * @author Gáll
 *
 */
public class PackagesActivator implements BundleActivator  {
	
	private static final Logger logger = LoggerFactory.getLogger (PackagesActivator.class);
	
    
    public void start(BundleContext context) throws Exception {
    	
    	logger.info("The Packages Bundle Started Successfully!");
    	logger.info("Now you can reach all packages needed!");
   
    }
    
    public void stop(BundleContext context) throws Exception {
    	
    	logger.info("The Packages Bundle Stopped Successfully!");
    	logger.info("Start again if you want to use the Packages!");
    }
    
}