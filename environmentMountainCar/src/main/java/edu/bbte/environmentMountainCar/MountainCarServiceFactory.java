package edu.bbte.environmentMountainCar;

import org.osgi.framework.Bundle;
import org.osgi.framework.ServiceFactory;
import org.osgi.framework.ServiceRegistration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.bbte.environment.Environment;

/**
 * A MountainCarServiceFactory példányosítja a 
 * MountainCar osztályt. Arra használható, hogy amikor
 * regisztráljuk szervízként a MountainCar batyut ezt a szervíz
 * factory osztályt adjuk meg paraméterként, így mindig amikor
 * egy batyu lekérdezi ezt a szervízt egy új példányt fog visszakapni.
 * @author Gáll
 *
 */
@SuppressWarnings("rawtypes")
public class MountainCarServiceFactory implements ServiceFactory {
	
	private static final Logger logger = LoggerFactory.getLogger (MountainCarServiceFactory.class);
	
    private int usageCounter = 0;
    
    public Object getService(Bundle bundle, ServiceRegistration registration) {
     
        usageCounter++;
    	
    	logger.info("Create object of MountainCar for " + bundle.getSymbolicName());
    	logger.info("Number of bundles using service " + usageCounter);
        
        Environment environment = new MountainCar();
        
        return environment;
    }
    
    
    public void ungetService(Bundle bundle, ServiceRegistration registration,Object service) {
    
    	usageCounter--;
    	
    	logger.info("Release object of MountainCar for " + bundle.getSymbolicName());
    	logger.info("Number of bundles using service " + usageCounter);
    }


}