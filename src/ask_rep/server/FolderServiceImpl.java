package ask_rep.server;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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

				objFolderInfo.setDatecreated(rs.getDate(5));
				objFolderInfo.setDateupdated(rs.getDate(6));
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
				
				objFolderInfo.setDatecreated(rs.getDate(5));
				objFolderInfo.setDateupdated(rs.getDate(6));	
			}
			
		} catch(SQLException e) {
			
		}
		
		return objFolderInfo;
	}
}
