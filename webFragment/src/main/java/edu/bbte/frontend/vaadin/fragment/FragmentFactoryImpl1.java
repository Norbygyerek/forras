package edu.bbte.frontend.vaadin.fragment;


import java.util.List;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceReference;

import com.vaadin.server.Sizeable.Unit;
import com.vaadin.ui.Component;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Grid.SelectionMode;
import com.vaadin.ui.Label;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.MenuBar.Command;
import com.vaadin.ui.MenuBar.MenuItem;
import com.vaadin.ui.Slider;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.renderers.ProgressBarRenderer;

import edu.bbte.agentEnvironmentList.AgentEnvironmentList;
import edu.bbte.agentEnvironmentList.TestList;
import edu.bbte.frontend.vaadin.FragmentFactory;
import elemental.json.JsonValue;


/**
 * A FragmentFactoryImpl1 osztály, amely a FragmentFactory
 * interfészt imolementálja. Szerepe, hogy összeállítsa a meglfelõ
 * komponenseket , amelyek a webes felületen megjelenítésre kerülnek.
 * @author Gáll
 *
 */
public class FragmentFactoryImpl1 implements FragmentFactory {

	private AgentEnvironmentList list;

	private List<TestList> testList;

	private VerticalLayout layout;
	private MenuBar menubar;
	private Grid grid;

	private Bundle bundle;
	private BundleContext context;

	private ServiceReference<?> getAgentEnvironmentList;

	@Override
	public Component getFragment() {

		layout = null;
		
		bundle = FrameworkUtil.getBundle(AgentEnvironmentList.class);

		if (bundle != null) {

			context = bundle.getBundleContext();
			getAgentEnvironmentList =
					context.getServiceReference(AgentEnvironmentList.class.getName());

			list = (AgentEnvironmentList) context.getService(getAgentEnvironmentList);

			testList        = list.getTests();
		}



		layout  = new VerticalLayout();
		menubar = new MenuBar();


		@SuppressWarnings("unused")
		MenuItem deployedBundles = menubar.addItem("Deployed Bundles", new Command() {

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			public void menuSelected(MenuItem selectedItem) {

				DeployedBundlePopUp deployed = new DeployedBundlePopUp();
				deployed.showWindow();

			}
		});

		@SuppressWarnings("unused")
		MenuItem results = menubar.addItem("Robo Results", new Command() {

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			public void menuSelected(MenuItem selectedItem) {

				ResultsTablePopUp resultsPopUp = new ResultsTablePopUp();
				resultsPopUp.showWindow();

			}
		});


		//-----------------------------------------------------------------
		MenuItem activeTests     = menubar.addItem("Active Tests", null, null);


		String temp;
		for (TestList test : testList) {

			temp = "Agent: " + test.getS1() + " With " +
					"Environment: " + test.getS2() + " " +
					test.getS3() + " " + Integer.toString(test.getRobo().getPercent());

			activeTests.addItem(temp, null, null);
		}

		//----------------------------------------------------------------

		layout.addComponent(menubar);

		createActiveTestsTable();

		return layout;
	}

	private void createActiveTestsTable() {

		grid = new Grid();

		grid.setCaption("Active test list");
		grid.setSizeFull();
		grid.setEditorEnabled(true);
		grid.setSelectionMode(SelectionMode.NONE);

		final String agentName       = "Agent";
		final String environmentName = "Environment";
		final String startTime       = "Start time";
		final String progress        = "Progress";


		grid.addColumn(agentName, String.class);
		grid.addColumn(environmentName, String.class);
		grid.addColumn(startTime, String.class);

		Slider progressEditor = new Slider();

		progressEditor.setWidth(10, Unit.PERCENTAGE);
		progressEditor.setMin(1);
		progressEditor.setMax(10);

		//grid.addColumn(progress, Integer.class);

		grid.addColumn(progress, Double.class)
		.setRenderer(new ProgressBarRenderer() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public JsonValue encode(Double value) {
				if (value != null) {
					value = (value - 1) / 10.0;
				}
				return super.encode(value);
			}
		});

		Label label = null;
		if (testList.size() == 0) {

			label = new Label("There is no active test available!");

			layout.addComponent(label);

		} else {

			for (TestList test : testList) {

				grid.addRow(test.getS1(), test.getS2(), test.getS3(), (double)test.getRobo().getPercent());

			}
			
			layout.addComponent(grid);

		}
	}
}
