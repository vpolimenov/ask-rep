package ask_rep.server;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

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
				
				objFileInfo.setDatecreated(rs.getTimestamp(7));
				
				lstFiles.add(objFileInfo);
			}
			
		} catch(SQLException e) {
			e.printStackTrace();
		}
		
		return lstFiles;
	}
	
	@Override
	public FileInfo getFile(int FileID) {
		
		FileInfo objFileInfo = new FileInfo();
		try {
			
			String objStatement = "SELECT * FROM files where fileID = ?";
			
			PreparedStatement objPrepStatement = myConnection.prepareStatement(objStatement);
			objPrepStatement.setInt(1, FileID);
			
			ResultSet rs = objPrepStatement.executeQuery();
			
			if(rs.next()) {
				objFileInfo.setFileID(rs.getInt(1));
				objFileInfo.setName(rs.getString(2));
				objFileInfo.setExtension(rs.getString(3));
				objFileInfo.setFileContent(rs.getBytes(4));
				
				if(rs.getInt(5) > 0) {
					FolderInfo objFoldInfo = foldService.getFolder(rs.getInt(5));
					objFileInfo.setFolder(objFoldInfo);
				}
				
				RepositoryInfo objRepInfo = repService.getRepository(rs.getInt(6));
				objFileInfo.setRepository(objRepInfo);
				
				objFileInfo.setDatecreated(rs.getTimestamp(7));
			}
			
		} catch(SQLException e) {
			e.printStackTrace();
		}
		
		return objFileInfo;
	}

	@Override
	public int insertFile(String filename, String extension, String fileContent, int folderID, int repositoryID) {
		// TODO Auto-generated method stub
		int fileID = 0;

		try {

			String objStatement = "";
			
			if(folderID > 0) {
				objStatement = "INSERT INTO files (name, extension, fileContent, folderID, repositoryID, datecreated) VALUES(?, ?, ?, ?, ?, ?)";
			} else {
				objStatement = "INSERT INTO files (name, extension, fileContent, repositoryID, datecreated) VALUES(?, ?, ?, ?, ?)";
			}
			PreparedStatement objPrepStatement = myConnection.prepareStatement(objStatement, Statement.RETURN_GENERATED_KEYS);
			
			if(folderID > 0) {
			objPrepStatement.setString(1, filename);
			objPrepStatement.setString(2,  extension);
			objPrepStatement.setBytes(3, fileContent.getBytes());
			objPrepStatement.setInt(4, folderID);
			objPrepStatement.setInt(5, repositoryID);
			objPrepStatement.setTimestamp(6, new Timestamp(System.currentTimeMillis()));
			} else {
				objPrepStatement.setString(1, filename);
				objPrepStatement.setString(2,  extension);
				objPrepStatement.setBytes(3, fileContent.getBytes());
				objPrepStatement.setInt(4, repositoryID);
				objPrepStatement.setTimestamp(5, new Timestamp(System.currentTimeMillis()));
			}

			objPrepStatement.executeUpdate();

			ResultSet rs = objPrepStatement.getGeneratedKeys();

			if (rs.next()) {
				fileID = rs.getInt(1);
				
				String strRepStatement = "UPDATE repositories SET dateupdated = ? WHERE repositoryID = ?";	
				PreparedStatement objRepPrepStatement = myConnection.prepareStatement(strRepStatement);
				objRepPrepStatement.setTimestamp(1, new Timestamp(System.currentTimeMillis()));
				objRepPrepStatement.setInt(2, repositoryID);
				
				objRepPrepStatement.executeUpdate();
				
				while(folderID > 0) {
					String strFoldStatement = "UPDATE folders SET dateupdated = ? WHERE folderID = ?";
					PreparedStatement objFoldPrepStatement = myConnection.prepareStatement(strFoldStatement);
					objFoldPrepStatement.setTimestamp(1, new Timestamp(System.currentTimeMillis()));
					objFoldPrepStatement.setInt(2, folderID);			
					
					objFoldPrepStatement.executeUpdate();
					
					//update date for all sub-folders
					folderID = foldService.getFolder(folderID).getParentFolderID();
				}
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return fileID;
	}
	
}
