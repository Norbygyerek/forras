package edu.bbte.frontend.vaadin;

import java.util.ArrayList;
import java.util.List;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.util.tracker.ServiceTracker;
import org.osgi.util.tracker.ServiceTrackerCustomizer;


/**
 * VaadinActivator osztály, amely elindítja a ServiceTrackert
 * és beregisztrálja a webes felülethez szükséges Fragment példányokat
 * @author Gáll
 *
 */
public class VaadinActivator implements BundleActivator,
		ServiceTrackerCustomizer<FragmentFactory, FragmentFactory> {
	
	private static final Logger logger = Logger.getLogger(VaadinActivator.class.getSimpleName());
	private static final List<FragmentFactory> factories = new ArrayList<>();
	protected static BundleContext context;

	ServiceTracker<FragmentFactory, FragmentFactory> fragmentFactoriesTracker;

	@Override
	public void start(BundleContext context) throws Exception {
		
		VaadinActivator.context = context;

		fragmentFactoriesTracker = new ServiceTracker<>(
				context, FragmentFactory.class, this);
		fragmentFactoriesTracker.open();
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		fragmentFactoriesTracker.close();
		context = null;
	}

	@Override
	public FragmentFactory addingService(
			ServiceReference<FragmentFactory> reference) {
		FragmentFactory ff = context.getService(reference);
		
		logger.log(Level.INFO, "Registering FragmentFactory service {0}", ff);
		
		if(!factories.contains(ff)) {
			factories.add(ff);
		}
		
		return ff;
	}

	@Override
	public void modifiedService(ServiceReference<FragmentFactory> reference,
			FragmentFactory service) {
		
	}

	@Override
	public void removedService(ServiceReference<FragmentFactory> reference,
			FragmentFactory ff) {
		logger.log(Level.INFO, "De-registering FragmentFactory service {0}", ff);
		factories.remove(ff);
	}
}
