package edu.bbte.environmentTetris;

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
 * A Tetris szerviz Activator osztalya
 * Be regisztrálja a szervízt az OSGi környzet Registry tárolójába,
 * amely elérhető lesz egyéb batyuk számára
 * @author Gáll
 *
 */
public class TetrisActivator implements BundleActivator  {
	
	private static final Logger logger = LoggerFactory.getLogger (TetrisActivator.class);
	
	@SuppressWarnings("rawtypes")
	private ServiceRegistration environmentServiceReg;
	private ServiceReference<?> getAgentEnvironmentReference;
	private AgentEnvironmentList list;
	
    public void start(BundleContext context) throws Exception {
    	
    	TetrisServiceFactory environmentServiceFactory = new TetrisServiceFactory();
    	Hashtable<String, String> dictionary           = new Hashtable<String, String>();
    	
    	dictionary.put(Constants.SERVICE_DESCRIPTION, "Tetris Environment Bundle");
    	dictionary.put("EnvironmentName", "Tetris");
    	
   
    	environmentServiceReg  = context.registerService(Environment.class.getName(), environmentServiceFactory, dictionary);

   
    	if (environmentServiceReg != null) {
    		
        	logger.info("The Tetris service Bundle Started Successfully!");
        	logger.info("Now you can reach the edu.bbte.environmentTetris package!");
       
        	
        	getAgentEnvironmentReference = context.getServiceReference(AgentEnvironmentList.class.getName());
        	list          = (AgentEnvironmentList) context.getService(getAgentEnvironmentReference);
        	
        	list.addEnvironment("EnvironmentName=Tetris");
        	
    	}
    	
    }
    
    public void stop(BundleContext context) throws Exception {

    	environmentServiceReg.unregister();
    	 
    	list.removeEnvironment("EnvironmentName=Tetris");
    	
     	logger.info("The Tetris service Bundle Stopped Successfully!");
     	logger.info("Start again if you want to use the Tetris service!");
     	
    }
    
}