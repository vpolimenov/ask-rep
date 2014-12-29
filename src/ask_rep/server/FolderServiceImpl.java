package ask_rep.server;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.mail.Folder;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import ask_rep.client.FolderInfo;
import ask_rep.client.FolderService;
import ask_rep.client.RepositoryInfo;

@SuppressWarnings("serial")
public class FolderServiceImpl extends RemoteServiceServlet implements FolderService {
	
	RepositoryServiceImpl repService = new RepositoryServiceImpl();
	ConnectionServiceImpl connService = new ConnectionServiceImpl();
	Connection myConnection = connService.getConnection();
	
	@Override
	public int insertFolder(String Name, int ParentFolderID, int repositoryID) {

		int folderID = 0;

		try {

			String objStatement;
			
			if(ParentFolderID == 0) {
				objStatement = "INSERT INTO folders (name, repositoryID, datecreated, dateupdated) VALUES(?, ?, ?, ?)";
			} else {
				objStatement = "INSERT INTO folders (parentFolderID, name, repositoryID, datecreated, dateupdated) VALUES(?, ?, ?, ?, ?)";
			}
			
			PreparedStatement objPrepStatement = myConnection.prepareStatement(objStatement, Statement.RETURN_GENERATED_KEYS);
			
			if(ParentFolderID == 0) {
				objPrepStatement.setString(1, Name);
				objPrepStatement.setInt(2, repositoryID);
				objPrepStatement.setTimestamp(3, new Timestamp(System.currentTimeMillis()));
				objPrepStatement.setTimestamp(4, new Timestamp(System.currentTimeMillis()));
			} else {
				objPrepStatement.setInt(1, ParentFolderID);
				objPrepStatement.setString(2,  Name);
				objPrepStatement.setInt(3, repositoryID);
				objPrepStatement.setTimestamp(4, new Timestamp(System.currentTimeMillis()));
				objPrepStatement.setTimestamp(5, new Timestamp(System.currentTimeMillis()));
			}

			objPrepStatement.executeUpdate();

			ResultSet rs = objPrepStatement.getGeneratedKeys();

			if (rs.next()) {
				folderID = rs.getInt(1);
				
				String strRepStatement = "UPDATE repositories SET dateupdated = ? WHERE repositoryID = ?";	
				PreparedStatement objRepPrepStatement = myConnection.prepareStatement(strRepStatement);
				objRepPrepStatement.setTimestamp(1, new Timestamp(System.currentTimeMillis()));
				objRepPrepStatement.setInt(2, repositoryID);
				
				objRepPrepStatement.executeUpdate();
				
				while(ParentFolderID > 0) {
					
					String strFoldStatement = "UPDATE folders SET dateupdated = ? WHERE folderID = ?";
					PreparedStatement objFoldPrepStatement = myConnection.prepareStatement(strFoldStatement);
					objFoldPrepStatement.setTimestamp(1, new Timestamp(System.currentTimeMillis()));
					objFoldPrepStatement.setInt(2, ParentFolderID);			
					
					objFoldPrepStatement.executeUpdate();
					
					//update date for all sub-folders
					ParentFolderID = getFolder(ParentFolderID).getParentFolderID();
				}
			}
			
		} catch (SQLException e) {

		}

		return folderID;

	}
	
	@Override
	public List<FolderInfo> getFolders(int RepositoryID, int ParentFolderID) {

		List<FolderInfo> lstFolders = new ArrayList<FolderInfo>();

		try {
			String objStatement = "";
			
			if(ParentFolderID > 0) {
				objStatement += "SELECT * FROM folders where repositoryID = ? and parentFolderID = ?";
			} else {
				objStatement += "SELECT * FROM folders where repositoryID = ? and parentFolderID IS NULL";
			}
		
			PreparedStatement objPrepStatement = myConnection.prepareStatement(objStatement);
			objPrepStatement.setInt(1, RepositoryID);
			
			if(ParentFolderID > 0)
				objPrepStatement.setInt(2, ParentFolderID);
			
			ResultSet rs = objPrepStatement.executeQuery();
			
			while(rs.next()) {
				
				FolderInfo objFolderInfo = new FolderInfo();
				
				objFolderInfo.setFolderID(rs.getInt(1));
				objFolderInfo.setParentFolderID(rs.getInt(2));
				objFolderInfo.setName(rs.getString(3));
				
				RepositoryInfo objRepInfo = repService.getRepository(rs.getInt(4));

				objFolderInfo.setDatecreated(rs.getTimestamp(5));
				objFolderInfo.setDateupdated(rs.getTimestamp(6));
				objFolderInfo.setRepository(objRepInfo);
				
				lstFolders.add(objFolderInfo);
				
			}
			
		} catch(SQLException e) {
			
		}
		
		return lstFolders;
	}

	@Override
	public FolderInfo getFolder(int FolderID) {
		
		FolderInfo objFolderInfo = new FolderInfo();
		try {
			
			String objStatement = "SELECT * FROM folders where folderID = ?";
			
			PreparedStatement objPrepStatement = myConnection.prepareStatement(objStatement);
			objPrepStatement.setInt(1, FolderID);
			
			ResultSet rs = objPrepStatement.executeQuery();
			
			if(rs.next()) {
				objFolderInfo.setFolderID(rs.getInt(1));
				objFolderInfo.setParentFolderID(rs.getInt(2));
				objFolderInfo.setName(rs.getString(3));
				
				RepositoryInfo objRepInfo = repService.getRepository(rs.getInt(4));
				objFolderInfo.setRepository(objRepInfo);
				
				objFolderInfo.setDatecreated(rs.getTimestamp(5));
				objFolderInfo.setDateupdated(rs.getTimestamp(6));	
			}
			
		} catch(SQLException e) {
			
		}
		
		return objFolderInfo;
	}
}
