package edu.bbte.frontend.vaadin.fragment;

import java.io.File;
import java.util.List;


import com.vaadin.server.FileDownloader;
import com.vaadin.server.FileResource;
import com.vaadin.server.Resource;
import com.vaadin.ui.Button;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;

import edu.bbte.dataModel.BaseTest;
import edu.bbte.dataModel.JDBCTestDao;

/**
 * Az Eredmények megfejelnítéséért felelõs felugró ablak
 * @author Gáll
 *
 */
public class ResultsTablePopUp extends PopUpWindow {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6638261261622954433L;


	private List<BaseTest> databaseList;
	private JDBCTestDao test;

	private VerticalLayout layout;

	private Table table;
	

	public ResultsTablePopUp() {

		test   = new JDBCTestDao();
		table  = new Table("RoboRun Results");
		layout = new VerticalLayout();
		
		databaseList = test.getAll();

		createTable();
		
		this.setContent(layout);

	}

	private void createTable() {

		final String agentName       = "Agent";
		final String environmentName = "Environment";
		final String startTime       = "Start time";

		table.addContainerProperty(agentName, String.class, null);
		table.addContainerProperty(environmentName, String.class, null);
		table.addContainerProperty(startTime, String.class, null);
		table.addContainerProperty("AllResults", Button.class, null);
		table.addContainerProperty("Statistics", Button.class, null);


		int index = 1;
		for (BaseTest basetest : databaseList) {

			Button saveResults = downloadFiles(basetest.getFileNameResults());
			Button saveStat    = downloadFiles(basetest.getFileNameStat());

			table.addItem(new Object[]{basetest.getAgent(), basetest.getEnvironment(),
					basetest.getExecutionTime(), saveResults, saveStat}, index);

			index++;
		}

		table.setImmediate(true);
		table.setSelectable(true);
		table.setMultiSelect(false);
		table.setSortEnabled(true);


		layout.addComponent(table);
		layout.setSizeUndefined();
	}
	
	private Button downloadFiles(String path) {

		Button save  = new Button("Click to download");
		Resource res = new FileResource(new File(path));

		FileDownloader fd = new FileDownloader(res);
		fd.extend(save);

		return save;
	}


}
