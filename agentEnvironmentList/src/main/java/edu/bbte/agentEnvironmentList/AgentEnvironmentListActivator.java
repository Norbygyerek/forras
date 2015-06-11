package edu.bbte.agentEnvironmentList;


import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class AgentEnvironmentListActivator implements BundleActivator  {
	
	private static final Logger logger = LoggerFactory.getLogger (AgentEnvironmentListActivator.class);

	ServiceRegistration<?> service;
	
    public void start(BundleContext context) throws Exception {
    	
        AgentEnvironment list = new AgentEnvironment();
        service = context.registerService(AgentEnvironmentList.class.getName(), list, null);
      	
    	
    	logger.info("The AgentEnvironment SERVICE  Started Successfully!");
    	logger.info("Now you can reach the AgentEnvironment SERVICE!");
   
    }
    
    public void stop(BundleContext context) throws Exception {

    	service.unregister();
    	 
     	logger.info("The AgentEnvironment SERVICE Stopped Successfully!");
     	logger.info("Start again if you want to use the AgentEnvironment SERVICE!");
     	
    }
}