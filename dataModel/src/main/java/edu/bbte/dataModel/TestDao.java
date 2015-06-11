package edu.bbte.dataModel;

import java.util.List;

public interface TestDao {

	List<BaseTest> getAll();
	BaseTest insert(BaseTest baseTest);
	
}
