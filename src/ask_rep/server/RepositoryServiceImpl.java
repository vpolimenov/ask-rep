package ask_rep.server;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import ask_rep.client.RepositoryInfo;
import ask_rep.client.RepositoryService;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

@SuppressWarnings("serial")
public class RepositoryServiceImpl extends RemoteServiceServlet implements RepositoryService {

	@Override
	public RepositoryInfo insertRepository(String Name, int UserID) {
		
		RepositoryInfo objRepInfo = new RepositoryInfo();
		
		try {
			
			Connection myConnection = ConnectionServiceImpl.getConnection();
			
			String objStatement = "INSERT INTO repositories (name, userID, datecreated, dateupdated) VALUES(?, ?, NOW(), NOW())";
	        PreparedStatement objPrepStatement = myConnection.prepareStatement(objStatement, Statement.RETURN_GENERATED_KEYS);
	        objPrepStatement.setString(1, Name);
	        objPrepStatement.setInt(2, UserID);
	
	        objPrepStatement.executeUpdate();
	        
	        ResultSet rs = objPrepStatement.getGeneratedKeys();
	        
	        if (rs.next()) {
	        	objRepInfo.setRepositoryID(rs.getInt(1));
	        }
        
		}catch(SQLException e)  {
			
		}
		
		return objRepInfo;
		
	}

	
	
	

}
