package edu.bbte.dataModel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;


public class JDBCTestDao implements TestDao {

	public List<BaseTest> getAll() {
		
		Connection conn = null;
		
		ArrayList<BaseTest> list = new ArrayList<BaseTest>();
 
		try {
			conn = ConnectionManager.getInstance().getConnection();
			
			Statement statement;
			
			statement = conn.createStatement();
			
			ResultSet resultSet = statement.executeQuery("SELECT * FROM roborunresults"); 
			
			while (resultSet.next()) {
				
				BaseTest baseTest = new BaseTest();
				baseTest.setAgent(resultSet.getString("agent"));
				baseTest.setEnvironment(resultSet.getString("environment"));
				baseTest.setExecutionTime(resultSet.getString("executionTime"));
				baseTest.setFileNameResults(resultSet.getString("fileNameResults"));
				baseTest.setFileNameStat(resultSet.getString("fileNameStats"));
				
				list.add(baseTest);
				
			}
			
		} catch (SQLException e) {
			
			e.printStackTrace();
		
		} finally {
			
			if (conn != null) {
				
				ConnectionManager.getInstance().returnConnection(conn);
			}
		}
				
		
		
		return list;
	}

	public BaseTest insert(BaseTest baseTest) {
		
		Connection conn = null;
		
		try {
			
			conn = ConnectionManager.getInstance().getConnection();
			
			PreparedStatement preparedStatement =
					conn.prepareStatement("INSERT INTO roborunresults (`agent`, `environment`, `executionTime`, `fileNameResults`, `fileNameStats`) VALUES (?, ?, ?, ?, ?)");
			
			preparedStatement.setString(1, baseTest.getAgent());
			preparedStatement.setString(2, baseTest.getEnvironment());
			preparedStatement.setString(3, baseTest.getExecutionTime().toString());
			preparedStatement.setString(4, baseTest.getFileNameResults());
			preparedStatement.setString(5, baseTest.getFileNameStat());
			
			preparedStatement.execute();
			
		} catch (SQLException e) {
			
			e.getStackTrace();
		
		} finally {
			
			if (conn != null) {
				
				ConnectionManager.getInstance().returnConnection(conn);
			}
		}
		
		
		
		return null;
	}

}
