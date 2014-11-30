package ask_rep.server;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import ask_rep.client.RepositoryInfo;
import ask_rep.client.RepositoryService;
import ask_rep.client.UserInfo;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

@SuppressWarnings("serial")
public class RepositoryServiceImpl extends RemoteServiceServlet implements
		RepositoryService {
	ConnectionServiceImpl connService = new ConnectionServiceImpl();
	Connection myConnection = connService.getConnection();

	@Override
	public int insertRepository(String Name, int UserID) {

		int repositoryID = 0;

		try {

			String objStatement = "INSERT INTO repositories (name, userID, datecreated, dateupdated) VALUES(?, ?, NOW(), NOW())";
			PreparedStatement objPrepStatement = myConnection.prepareStatement(
					objStatement, Statement.RETURN_GENERATED_KEYS);
			objPrepStatement.setString(1, Name);
			objPrepStatement.setInt(2, UserID);

			objPrepStatement.executeUpdate();

			ResultSet rs = objPrepStatement.getGeneratedKeys();

			if (rs.next()) {
				repositoryID = rs.getInt(1);
			}

		} catch (SQLException e) {

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

			PreparedStatement objPrepStatement = myConnection
					.prepareStatement(objStatement);

			ResultSet rs = objPrepStatement.executeQuery();

			if (rs.next()) {
				objRepInfo.setRepositoryID(rs.getInt(1));
				objRepInfo.setName(rs.getString(2));
				objRepInfo.setCreatedDate(rs.getDate(3));
				objRepInfo.setUpdatedDate(rs.getDate(4));

				UserInfo objUserInfo = new UserInfo();

				objUserInfo.setUserID(rs.getInt(5));
				objUserInfo.setName(rs.getString(6));
				objUserInfo.setEmail(rs.getString(7));

				objRepInfo.setUser(objUserInfo);
			}

		} catch (SQLException e) {

		}

		return objRepInfo;
	}

}
