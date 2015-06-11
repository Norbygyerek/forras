package edu.bbte.frontend.vaadin.fragment;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

import edu.bbte.frontend.vaadin.FragmentFactory;

/**
 * A Fragment példány regisztrálásáért felelõs osztály
 * @author Gáll
 *
 */
public class FragmentFactory1Activator implements BundleActivator {

	private FragmentFactoryImpl1 service;
	private ServiceRegistration<?> reg;

	@Override
	public void start(BundleContext context) throws Exception {
		
		service = new FragmentFactoryImpl1();
		
		reg     = context.registerService(FragmentFactory.class, service, null);
	}

	@Override
	public void stop(BundleContext context) throws Exception {
	
		reg.unregister();
		
	}

}
