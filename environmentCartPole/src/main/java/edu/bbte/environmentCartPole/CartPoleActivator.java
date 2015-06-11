package edu.bbte.environmentCartPole;

import java.util.Hashtable;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.Constants;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.ServiceRegistration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.bbte.agentEnvironmentList.AgentEnvironmentList;
import edu.bbte.environment.Environment;

/**
 * A CartPole szerviz Activator osztalya
 * Be regisztrálja a szervízt az OSGi környzet Registry tárolójába,
 * amely elérhető lesz egyéb batyuk számára
 * @author Gáll
 *
 */
public class CartPoleActivator implements BundleActivator  {
	
	private static final Logger logger = LoggerFactory.getLogger (CartPoleActivator.class);
	
	@SuppressWarnings("rawtypes")
	private ServiceRegistration environmentServiceReg;
	private ServiceReference<?> getAgentEnvironmentReference;
	private AgentEnvironmentList list;
	
    public void start(BundleContext context) throws Exception {
    	
    	CartPoleServiceFactory environmentServiceFactory = new CartPoleServiceFactory();
    	Hashtable<String, String> dictionary             = new Hashtable<String, String>();
    	
    	dictionary.put(Constants.SERVICE_DESCRIPTION, "CartPole Environment Bundle");
    	dictionary.put("EnvironmentName", "CartPole");
    	
   
    	environmentServiceReg  = context.registerService(Environment.class.getName(), environmentServiceFactory, dictionary);

   
    	if (environmentServiceReg != null) {
    		
        	logger.info("The CartPole service Bundle Started Successfully!");
        	logger.info("Now you can reach the edu.bbte.environmentCartPole package!");
       
        	
        	getAgentEnvironmentReference = context.getServiceReference(AgentEnvironmentList.class.getName());
        	list          = (AgentEnvironmentList) context.getService(getAgentEnvironmentReference);
        	
        	list.addEnvironment("EnvironmentName=CartPole");
        	
    	}
    	
    }
    
    public void stop(BundleContext context) throws Exception {

    	environmentServiceReg.unregister();
    	 
    	list.removeEnvironment("EnvironmentName=CartPole");
    	
     	logger.info("The CartPole service Bundle Stopped Successfully!");
     	logger.info("Start again if you want to use the CartPole service!");
     	
    }
    
}