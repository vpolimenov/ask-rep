package ask_rep.server;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import ask_rep.client.FileInfo;
import ask_rep.client.FileService;
import ask_rep.client.FolderInfo;
import ask_rep.client.RepositoryInfo;

@SuppressWarnings("serial")
public class FileServiceImpl extends RemoteServiceServlet implements FileService {
	
	RepositoryServiceImpl repService = new RepositoryServiceImpl();
	FolderServiceImpl foldService = new FolderServiceImpl();
	ConnectionServiceImpl connService = new ConnectionServiceImpl();
	Connection myConnection = connService.getConnection();
	
	@Override
	public List<FileInfo> getFiles(int RepositoryID, int FolderID) {
		
		List<FileInfo> lstFiles = new ArrayList<FileInfo>();

		try {
			
			String objStatement = "";
			
			if(FolderID > 0) {
				objStatement = "SELECT * FROM files where repositoryID = ? and folderID = ?";
			} else {
				objStatement = "SELECT * FROM files where repositoryID = ? and folderID IS NULL";
			}
		
			PreparedStatement objPrepStatement = myConnection.prepareStatement(objStatement);
			objPrepStatement.setInt(1, RepositoryID);
			
			if(FolderID > 0)
				objPrepStatement.setInt(2, FolderID);
			
			ResultSet rs = objPrepStatement.executeQuery();
			
			while(rs.next()) {
				
				FileInfo objFileInfo = new FileInfo();
				
				objFileInfo.setFileID(rs.getInt(1));
				objFileInfo.setName(rs.getString(2));
				objFileInfo.setExtension(rs.getString(3));
				objFileInfo.setFileContent(rs.getBytes(4));
				
				if(FolderID > 0) {
					
					FolderInfo objFolderInfo = foldService.getFolder(rs.getInt(5));
					objFileInfo.setFolder(objFolderInfo);
				}
				
				RepositoryInfo objRepInfo = repService.getRepository(rs.getInt(6));
				objFileInfo.setRepository(objRepInfo);
				
				objFileInfo.setDatecreated(rs.getDate(7));
				
				lstFiles.add(objFileInfo);
			}
			
		} catch(SQLException e) {
			
		}
		
		return lstFiles;
	}
	
}
