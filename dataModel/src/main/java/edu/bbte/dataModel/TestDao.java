package edu.bbte.dataModel;

import java.util.List;

/**
 * 
 * @author Gáll
 *
 */
public interface TestDao {

	List<BaseTest> getAll();
	BaseTest insert(BaseTest baseTest);
	
}
