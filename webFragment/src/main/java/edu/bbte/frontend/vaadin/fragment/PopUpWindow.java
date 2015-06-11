package edu.bbte.frontend.vaadin.fragment;

import com.vaadin.ui.UI;
import com.vaadin.ui.Window;

/**
 * 
 * @author Gáll
 *
 */
public class PopUpWindow extends Window {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public PopUpWindow() {
		
		this.setModal(true);
		this.setResizable(false);
		this.center();
	}
	
	public void showWindow() {
		
		UI.getCurrent().addWindow(this);
		
	}
	
	public void hideWindow() {
		
		this.close();
	}

}
