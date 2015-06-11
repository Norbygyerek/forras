package edu.bbte.roboCommunication;

import java.util.Hashtable;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.Constants;
import org.osgi.framework.ServiceRegistration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.bbte.robo.Robo;

/**
 * A RoboCommunication szerviz Activator osztalya
 * Be regisztrálja a szervízt az OSGi környzet Registry tárolójába,
 * amely elérhető lesz egyéb batyuk számára
 * @author Gáll
 *
 */
public class RoboCommunicationActivator implements BundleActivator  {
	
	private static final Logger logger = LoggerFactory.getLogger(RoboCommunicationActivator.class);
	
	
	private ServiceRegistration<?> roboCommunicationServiceReg;
    
    public void start(BundleContext context) throws Exception {
    	
    	RoboCommunicationFactory roboCommunicationFactory = new RoboCommunicationFactory();
		Hashtable<String, String> dictionary              = new Hashtable<String, String>();

    	dictionary.put(Constants.SERVICE_DESCRIPTION, "RoboCommunication Bundle.");
    	dictionary.put("Robo", "Communication");
   
    	roboCommunicationServiceReg  = context.registerService(Robo.class.getName(), roboCommunicationFactory, dictionary);
    	
    	logger.info("The RoboCommunication service Started Successfully!");
    	logger.info("Now you can reach the edu.bbte.roboCommunication service!");
   
    }
    
    public void stop(BundleContext context) throws Exception {

    	roboCommunicationServiceReg.unregister();
    	 
    	logger.info("The RoboCommunication service Bundle Stopped Successfully!");
    	logger.info("Start again if you want to use the RoboCommunication service!");
     	
    }
    
}