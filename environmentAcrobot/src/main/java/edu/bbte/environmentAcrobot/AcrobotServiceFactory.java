package edu.bbte.environmentAcrobot;

import org.osgi.framework.Bundle;
import org.osgi.framework.ServiceFactory;
import org.osgi.framework.ServiceRegistration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.bbte.environment.Environment;

/**
 * A AcrobotServiceFactory példányosítja a 
 * Acrobot osztályt. Arra használható, hogy amikor
 * regisztráljuk szervízként a Acrobot batyut ezt a szervíz
 * factory osztályt adjuk meg paraméterként, így mindig amikor
 * egy batyu lekérdezi ezt a szervízt egy új példányt fog visszakapni.
 * @author Gáll
 *
 */
@SuppressWarnings("rawtypes")
public class AcrobotServiceFactory implements ServiceFactory {
	
	private static final Logger logger = LoggerFactory.getLogger (AcrobotServiceFactory.class);
	
    private int usageCounter = 0;
    
    public Object getService(Bundle bundle, ServiceRegistration registration) {
     
        usageCounter++;
    	
    	logger.info("Create object of Acrobot for " + bundle.getSymbolicName());
    	logger.info("Number of bundles using service " + usageCounter);
        
        Environment environment = new Acrobot();
        
        return environment;
    }
    
    
    public void ungetService(Bundle bundle, ServiceRegistration registration,Object service) {
    
    	usageCounter--;
    	
    	logger.info("Release object of Acrobot for " + bundle.getSymbolicName());
    	logger.info("Number of bundles using service " + usageCounter);
    }


}