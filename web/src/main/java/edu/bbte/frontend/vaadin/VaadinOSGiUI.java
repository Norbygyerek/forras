package edu.bbte.frontend.vaadin;

import org.osgi.framework.ServiceReference;
import org.osgi.util.tracker.ServiceTracker;
import org.osgi.util.tracker.ServiceTrackerCustomizer;

import com.vaadin.annotations.Theme;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.Component;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

/**
 * A megjelenitésért felelős osztály. A ServiceTracker által
 * beregisztrált fragmentektől kéri el a komponenseket és jeleníti meg a 
 * webes felületen.
 * @author Gáll
 *
 */

@Theme("valo")
@SuppressWarnings("serial")
public class VaadinOSGiUI extends UI implements
		ServiceTrackerCustomizer<FragmentFactory, FragmentFactory> {

	private VerticalLayout layout;
	
	
	public VaadinOSGiUI() {
	 
		layout = null;
	}
	
	@Override
	protected void init(VaadinRequest request) {
	    
		ServiceTracker<FragmentFactory, FragmentFactory> tracker = new ServiceTracker<FragmentFactory, FragmentFactory>(
				VaadinActivator.context, FragmentFactory.class, this);
		tracker.open();
		
		addDetachListener(new DetachListener() {
			
			@Override
			public void detach(DetachEvent event) {
				tracker.close();
			}
		});
	    		
		layout = new VerticalLayout();
		layout.setMargin(true);
		
		setContent(layout);	
		
	}

	@Override
	public FragmentFactory addingService(
			ServiceReference<FragmentFactory> reference) {
		
		FragmentFactory ff = VaadinActivator.context.getService(reference);
		addNewComponents(reference);
		
		return ff;
	}
	
	public void addNewComponents(ServiceReference<FragmentFactory> reference) {
		
		FragmentFactory ff = VaadinActivator.context.getService(reference);
		Component fragment = ff.getFragment();
		
		addedComponents(fragment);
		
	}
	
	private void addedComponents(Component c) {
		access(new Runnable() {
			
			@Override
			public void run() {
				layout.addComponent(c);
			}
		});
	}

	@Override
	public void modifiedService(ServiceReference<FragmentFactory> reference,
			FragmentFactory service) {
	}

	@Override
	public void removedService(ServiceReference<FragmentFactory> reference,
			FragmentFactory service) {
	}
}
