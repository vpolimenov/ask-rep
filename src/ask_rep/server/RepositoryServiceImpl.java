package ask_rep.server;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import ask_rep.client.RepositoryInfo;
import ask_rep.client.RepositoryService;
import ask_rep.client.UserInfo;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

@SuppressWarnings("serial")
public class RepositoryServiceImpl extends RemoteServiceServlet implements RepositoryService {
	ConnectionServiceImpl connService = new ConnectionServiceImpl();
	Connection myConnection = connService.getConnection();

	@Override
	public int insertRepository(String Name, int UserID) {

		int repositoryID = 0;

		try {

			String objStatement = "INSERT INTO repositories (name, userID, datecreated, dateupdated) VALUES(?, ?, ?, ?)";
			PreparedStatement objPrepStatement = myConnection.prepareStatement(objStatement, Statement.RETURN_GENERATED_KEYS);
			objPrepStatement.setString(1, Name);
			objPrepStatement.setInt(2, UserID);
			objPrepStatement.setTimestamp(3, new Timestamp(System.currentTimeMillis()));
			objPrepStatement.setTimestamp(4, new Timestamp(System.currentTimeMillis()));

			objPrepStatement.executeUpdate();

			ResultSet rs = objPrepStatement.getGeneratedKeys();

			if (rs.next()) {
				repositoryID = rs.getInt(1);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return repositoryID;

	}

	@Override
	public RepositoryInfo getRepository(int RepositoryID) {
		// TODO Auto-generated method stub

		RepositoryInfo objRepInfo = new RepositoryInfo();
		
		try {

			String objStatement = "SELECT repositories.repositoryID, repositories.name, repositories.datecreated, repositories.dateupdated, "
								+ "repositories.userID, users.name, users.email "
								+ "FROM repositories "
								+ "INNER JOIN users ON repositories.userID = users.userID "
								+ "WHERE repositories.repositoryID = ?";

			PreparedStatement objPrepStatement = myConnection.prepareStatement(objStatement);
			objPrepStatement.setInt(1, RepositoryID);
			
			ResultSet rs = objPrepStatement.executeQuery();

			if (rs.next()) {
				objRepInfo.setRepositoryID(rs.getInt(1));
				objRepInfo.setName(rs.getString(2));
				objRepInfo.setCreatedDate(rs.getTimestamp(3));
				objRepInfo.setUpdatedDate(rs.getTimestamp(4));

				UserInfo objUserInfo = new UserInfo();

				objUserInfo.setUserID(rs.getInt(5));
				objUserInfo.setName(rs.getString(6));
				objUserInfo.setEmail(rs.getString(7));

				objRepInfo.setUser(objUserInfo);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return objRepInfo;
	}
	
	@Override
	public List<RepositoryInfo> getRepositoryByUserID(int UserID) {
		// TODO Auto-generated method stub

		List<RepositoryInfo> lstRepositories = new ArrayList<RepositoryInfo>();
		
		try {

			String objStatement = "SELECT repositories.repositoryID, repositories.name, repositories.datecreated, repositories.dateupdated, "
								+ "repositories.userID, users.name, users.email "
								+ "FROM repositories "
								+ "INNER JOIN users ON repositories.userID = users.userID "
								+ "WHERE repositories.userID = ?";

			PreparedStatement objPrepStatement = myConnection.prepareStatement(objStatement);
			objPrepStatement.setInt(1, UserID);
			
			ResultSet rs = objPrepStatement.executeQuery();

			while (rs.next()) {
				
				RepositoryInfo objRepInfo = new RepositoryInfo();
				
				objRepInfo.setRepositoryID(rs.getInt(1));
				objRepInfo.setName(rs.getString(2));
				objRepInfo.setCreatedDate(rs.getTimestamp(3));
				objRepInfo.setUpdatedDate(rs.getTimestamp(4));

				UserInfo objUserInfo = new UserInfo();

				objUserInfo.setUserID(rs.getInt(5));
				objUserInfo.setName(rs.getString(6));
				objUserInfo.setEmail(rs.getString(7));

				objRepInfo.setUser(objUserInfo);
				
				lstRepositories.add(objRepInfo);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return lstRepositories;
	}
	
	@Override
	public List<RepositoryInfo> getLatestRepositories(int UserID) {
		// TODO Auto-generated method stub

		List<RepositoryInfo> lstRepositories = new ArrayList<RepositoryInfo>();
		
		try {

			String objStatement = "";
			
			if(UserID > 0) {
				objStatement = "SELECT repositories.repositoryID, repositories.name, repositories.datecreated, repositories.dateupdated, "
						+ "repositories.userID, users.name, users.email "
						+ "FROM repositories "
						+ "INNER JOIN users ON repositories.userID = users.userID "
						+ "WHERE repositories.userID != ? "
						+ "ORDER BY repositories.dateupdated DESC";
			} else {
				objStatement = "SELECT repositories.repositoryID, repositories.name, repositories.datecreated, repositories.dateupdated, "
						+ "repositories.userID, users.name, users.email "
						+ "FROM repositories "
						+ "INNER JOIN users ON repositories.userID = users.userID "
						+ "ORDER BY repositories.dateupdated DESC";
			}

			PreparedStatement objPrepStatement = myConnection.prepareStatement(objStatement);

			if(UserID > 0) {
				objPrepStatement.setInt(1, UserID);
			}
			
			ResultSet rs = objPrepStatement.executeQuery();

			while (rs.next()) {
				
				RepositoryInfo objRepInfo = new RepositoryInfo();
				
				objRepInfo.setRepositoryID(rs.getInt(1));
				objRepInfo.setName(rs.getString(2));
				objRepInfo.setCreatedDate(rs.getTimestamp(3));
				objRepInfo.setUpdatedDate(rs.getTimestamp(4));

				UserInfo objUserInfo = new UserInfo();

				objUserInfo.setUserID(rs.getInt(5));
				objUserInfo.setName(rs.getString(6));
				objUserInfo.setEmail(rs.getString(7));

				objRepInfo.setUser(objUserInfo);
				
				lstRepositories.add(objRepInfo);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return lstRepositories;
	}

}
