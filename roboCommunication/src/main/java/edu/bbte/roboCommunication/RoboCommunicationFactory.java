package edu.bbte.roboCommunication;

import org.osgi.framework.Bundle;
import org.osgi.framework.ServiceFactory;
import org.osgi.framework.ServiceRegistration;



/**
 * A RoboCommunicationFactory példányosítja a 
 * RoboCommunication osztályt. Arra használható, hogy amikor
 * regisztráljuk szervízként a RoboCommunication batyut ezt a szervíz
 * factory osztályt adjuk meg paraméterként, így mindig amikor
 * egy batyu lekérdezi ezt a szervízt egy új példányt fog visszakapni.
 * @author Gáll
 *
 */
@SuppressWarnings("rawtypes")
public class RoboCommunicationFactory implements ServiceFactory {
	
    private int usageCounter = 0;
 
    
    public Object getService(Bundle bundle, ServiceRegistration registration) {
     
        usageCounter++;
    	
    	System.out.println("Create object of RoboCommunication for " + bundle.getSymbolicName());
        System.out.println("Number of bundles using service " + usageCounter);
        
        RoboCommunication roboCommunication = new RoboCommunication();
        
        return roboCommunication;
    }
    
    
    public void ungetService(Bundle bundle, ServiceRegistration registration,Object service) {
    
    	usageCounter--;
    	
    	System.out.println("Release object of RoboCommunication for " + bundle.getSymbolicName());
    	System.out.println("Number of bundles using service " + usageCounter);
    }


}