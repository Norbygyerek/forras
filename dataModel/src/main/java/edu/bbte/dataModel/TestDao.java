package edu.bbte.dataModel;

import java.util.List;

/**
 * 
 * @author G�ll
 *
 */
public interface TestDao {

	List<BaseTest> getAll();
	BaseTest insert(BaseTest baseTest);
	
}
