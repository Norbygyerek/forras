package edu.bbte.dataModel;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class DataModelActivator implements BundleActivator {

	public void start(BundleContext context) throws Exception {
		
		//Class.forName("org.eclipse.persistence.jpa.PersistenceProvider");
		
		//ServiceReference<?> ref = context.getServiceReference(PersistenceProvider.class.getName());
		
		//PersistenceProvider provider = (PersistenceProvider) context.getService(ref);
		
		
		//EntityManager manager =  Persistence.createEntityManagerFactory("roboRunUnit", null).createEntityManager();
		
		//System.out.println(">>>>>>>>>>>>>>>A manageer: " + manager);
		ClassLoader old = Thread.currentThread().getContextClassLoader();
		try {
			
			Thread.currentThread().setContextClassLoader(getClass().getClassLoader());
			
			EntityManager manager = Persistence.createEntityManagerFactory("roboRunPU", null).createEntityManager();
			System.out.println(">>>>>>>>>>>>>>>A manageer: " + manager);
		} finally {
			
			Thread.currentThread().setContextClassLoader(old);
		}
		
		System.out.println("A data model aktivator elindult");
		
	}

	public void stop(BundleContext context) throws Exception {
		
		
		System.out.println("A data model aktivator leallt");
		
	}

}
