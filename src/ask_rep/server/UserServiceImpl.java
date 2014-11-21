package ask_rep.server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import ask_rep.client.UserService;

import com.google.appengine.api.utils.SystemProperty;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

@SuppressWarnings("serial")
public class UserServiceImpl extends RemoteServiceServlet implements UserService {
	
	public int insertUser(String Name, String Email) {
	
		int returnID = 0;
		
		try {
			
			Connection myConnection = ConnectionServiceImpl.getConnection();
			
			String objStatement = "INSERT INTO users (name, email) VALUES( ? , ? )";
	        PreparedStatement objPrepStatement = myConnection.prepareStatement(objStatement, Statement.RETURN_GENERATED_KEYS);
	        objPrepStatement.setString(1, Name);
	        objPrepStatement.setString(2, Email);
	
	        objPrepStatement.executeUpdate();
	        
	        ResultSet rs = objPrepStatement.getGeneratedKeys();
	        if (rs.next()) {
	        	returnID = rs.getInt(1);
	        }
        
		}catch(SQLException e)  {
			
		}
		
		return returnID;
	}

	@Override
	public int checkUserExists(String Email) {
		// TODO Auto-generated method stub
		int count = 0;
		
		try {
			
			Connection myConnection = ConnectionServiceImpl.getConnection();

			String objStatement = "SELECT userID FROM users WHERE email = ?";
			PreparedStatement objPrepStatement = myConnection.prepareStatement(objStatement);
			objPrepStatement.setString(1, Email);
			
			ResultSet objResultSet = objPrepStatement.executeQuery();
			
			while(objResultSet.next()) {
				count = objResultSet.getInt(1);
			}
			
		} catch(SQLException e) {
			
		}
		
		return count;
	}
}
