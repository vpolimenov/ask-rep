package ask_rep.client;

import java.io.Serializable;
import java.util.Date;

@SuppressWarnings("serial")
public class FileInfo implements Serializable {
	
	private int fileID;
	private String name;
	private String extension;
	private byte[] fileContent;
	private FolderInfo folder;
	private RepositoryInfo repository;
	private Date datecreated;
	
	public int getFileID() {
		return fileID;
	}
	public void setFileID(int fileID) {
		this.fileID = fileID;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getExtension() {
		return extension;
	}
	public void setExtension(String extension) {
		this.extension = extension;
	}
	public byte[] getFileContent() {
		return fileContent;
	}
	public void setFileContent(byte[] fileContent) {
		this.fileContent = fileContent;
	}
	public FolderInfo getFolder() {
		return folder;
	}
	public void setFolder(FolderInfo folder) {
		this.folder = folder;
	}
	public RepositoryInfo getRepository() {
		return repository;
	}
	public void setRepository(RepositoryInfo repository) {
		this.repository = repository;
	}
	public Date getDatecreated() {
		return datecreated;
	}
	public void setDatecreated(Date datecreated) {
		this.datecreated = datecreated;
	}
}
