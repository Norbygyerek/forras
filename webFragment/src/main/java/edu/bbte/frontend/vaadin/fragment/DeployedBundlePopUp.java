package edu.bbte.frontend.vaadin.fragment;

import java.util.List;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceReference;

import com.vaadin.ui.TreeTable;
import com.vaadin.ui.VerticalLayout;

import edu.bbte.agentEnvironmentList.AgentEnvironmentList;

/**
 * DeployedBundlePopUp osztály, mely 
 * a telepített Batyuknak biztosít
 * egy felugró ablakot a webes felületen
 * @author Gáll
 *
 */
public class DeployedBundlePopUp extends PopUpWindow {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3390737432162774437L;

	private AgentEnvironmentList list;
	
	private List<String> agentList;
	private List<String> environmentList;
	
	private VerticalLayout layout;
	
	private Bundle bundle;
	private BundleContext context;
	
	private ServiceReference<?> getAgentEnvironmentList;
	
	private TreeTable table;
	
	
	public DeployedBundlePopUp() {
		
		bundle = FrameworkUtil.getBundle(AgentEnvironmentList.class);
		
		if (bundle != null) {
		
			context = bundle.getBundleContext();
			getAgentEnvironmentList =
					context.getServiceReference(AgentEnvironmentList.class.getName());
		
			list = (AgentEnvironmentList) context.getService(getAgentEnvironmentList);
			
			agentList       = list.getAgentList();
			environmentList = list.getEnvironmentList();
		}
		
		layout = new VerticalLayout();
		table  = new TreeTable("Active Bundles");
		
		createTable();
		
		this.setContent(layout);
	
	}
	
	private void createTable() {
		
		final String bundleName      = "Name";
		final String bundleReference = "Reference";
		
		table.addContainerProperty(bundleName, String.class, null);
		table.addContainerProperty(bundleReference, String.class, null);

		

		int index = 1;
		table.addItem(new Object[]{"Agents", ""}, index);
		
		for (String str : agentList) {
			
			String temp[] = str.split("=");
			String name = temp[1];
			
			table.addItem(new Object[]{name, str}, ++index);
			
			
			table.setParent(index, 1);
			table.setChildrenAllowed(index, false);
		}
		
		int index2 = index + 1;
		int parent = index2;
		table.addItem(new Object[]{"Environmets", ""}, index2);
		for (String str : environmentList) {
			
			String temp[] = str.split("=");
			String name = temp[1];
			
			table.addItem(new Object[]{name, str}, ++index2);
			table.setParent(index2, parent);
			table.setChildrenAllowed(index2, false);
		}
		
		
		table.setImmediate(true);
		table.setSelectable(true);
		table.setMultiSelect(false);
		table.setSortEnabled(true);
		
		
		table.setWidth("800px");
		table.setHeight("600px");
		
		layout.addComponent(table);
		layout.setSizeUndefined();
		

	}

}
