package edu.bbte.dataModel;

import java.io.Externalizable;
import java.io.IOException;
import java.io.NotSerializableException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

public final class ConnectionManager implements Serializable, Externalizable {
	
	private static ConnectionManager instance;
	
	private final List<Connection> connectionPool;
	
	private final int connectionPoolSize;
	
	
	private ConnectionManager() {
		
		connectionPool = new LinkedList<Connection>();
		
		try {
			
			Class.forName("com.mysql.jdbc.Driver");
			
			connectionPoolSize = 10;
			
			for (int i = 0; i < connectionPoolSize; i++) {
				connectionPool.add(
					DriverManager.getConnection(
						"jdbc:mysql://localhost:3306/roborun", 
						"root", 
						"xampp"
					)
				);
			}
		
		} 
		
		catch (final ClassNotFoundException e) {
			throw new RuntimeException("Missing database driver", e);
		} 
		
		catch (final SQLException e) {
			throw new RuntimeException("Can not connect to the database", e);
		}
		
	}
	
	
	public synchronized static ConnectionManager getInstance () {
        
		if (instance == null) {
            instance = new ConnectionManager();
        }
		
        return instance;
        
    }
    
    public synchronized Connection getConnection() throws RuntimeException {
    	
        Connection connection = null;
        
        if (connectionPool.size () > 0) {
            connection = connectionPool.remove(0);
        }
        
        if (connection == null) {
            throw new RuntimeException("No connections in pool");
        }
        
        return connection;
        
    }
    
    public synchronized void returnConnection(final Connection connection) {
        
    	if (connectionPool.size() < connectionPoolSize) {
        	connectionPool.add(connection);
        }
    	
    }

	
	public final void writeObject(Object x) throws IOException {
		throw new NotSerializableException();
	}
	
	public final Object readObject() throws IOException, ClassNotFoundException {
		throw new NotSerializableException();
	}

	public void writeExternal(final ObjectOutput out) throws IOException {
		throw new NotSerializableException();
	}

	public void readExternal(final ObjectInput in) throws IOException, ClassNotFoundException {
		throw new NotSerializableException();
	}

}
