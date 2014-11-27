package ask_rep.server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import com.google.appengine.api.utils.SystemProperty;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class ConnectionServiceImpl {
	
	public Connection getConnection() {
	
		Connection conn = null;
		
	    try {
	    	String url;
	    	
	    	  if (SystemProperty.environment.value() == SystemProperty.Environment.Value.Production) {
	    	        // Load the class that provides the new "jdbc:google:mysql://" prefix.
	    	        Class.forName("com.mysql.jdbc.GoogleDriver");
	    	        url = "jdbc:google:mysql://" + System.getProperty("instance") + "/" + System.getProperty("db") + "?user=" + 
	    	                                	   System.getProperty("username");
		      } else {
			        // Local MySQL instance to use during development.
			        Class.forName("com.mysql.jdbc.Driver");
			        url = "jdbc:mysql://" + System.getProperty("instance") + "/" + System.getProperty("db") + "?user=" + 
                        					System.getProperty("username") + "&password=" + System.getProperty("password");
		      }
	    	  
              conn = DriverManager.getConnection(url);
	    } catch (SQLException e) {
               System.err.println("mySQL Connection Error: ");
	            e.printStackTrace();
	    } catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    
	    return conn;
	}
}
