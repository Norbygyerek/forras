package edu.bbte.environmentHelicopter;

import org.osgi.framework.Bundle;
import org.osgi.framework.ServiceFactory;
import org.osgi.framework.ServiceRegistration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.bbte.environment.Environment;

/**
 * A HelicopterServiceFactory példányosítja a 
 * Helicopter osztályt. Arra használható, hogy amikor
 * regisztráljuk szervízként a Helicopter batyut ezt a szervíz
 * factory osztályt adjuk meg paraméterként, így mindig amikor
 * egy batyu lekérdezi ezt a szervízt egy új példányt fog visszakapni.
 * @author Gáll
 *
 */
@SuppressWarnings("rawtypes")
public class HelicopterServiceFactory implements ServiceFactory {
	
	private static final Logger logger = LoggerFactory.getLogger (HelicopterServiceFactory.class);
	
    private int usageCounter = 0;
    
    public Object getService(Bundle bundle, ServiceRegistration registration) {
     
        usageCounter++;
    	
    	logger.info("Create object of Helicopter for " + bundle.getSymbolicName());
    	logger.info("Number of bundles using service " + usageCounter);
        
        Environment environment = new Helicopter();
        
        return environment;
    }
    
    
    public void ungetService(Bundle bundle, ServiceRegistration registration,Object service) {
    
    	usageCounter--;
    	
    	logger.info("Release object of Helicopter for " + bundle.getSymbolicName());
    	logger.info("Number of bundles using service " + usageCounter);
    }


}