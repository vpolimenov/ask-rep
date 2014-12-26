package ask_rep.client;

import java.io.Serializable;
import java.sql.Timestamp;

@SuppressWarnings("serial")
public class FolderInfo implements Serializable {
	
	private int folderID;
	private int parentFolderID;
	private String name;
	private Timestamp datecreated;
	private Timestamp dateupdated;
	private RepositoryInfo repository;
	
	public int getFolderID() {
		return folderID;
	}
	public void setFolderID(int folderID) {
		this.folderID = folderID;
	}
	public int getParentFolderID() {
		return parentFolderID;
	}
	public void setParentFolderID(int parentFolderID) {
		this.parentFolderID = parentFolderID;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Timestamp getDatecreated() {
		return datecreated;
	}
	public void setDatecreated(Timestamp datecreated) {
		this.datecreated = datecreated;
	}
	public Timestamp getDateupdated() {
		return dateupdated;
	}
	public void setDateupdated(Timestamp dateupdated) {
		this.dateupdated = dateupdated;
	}
	public RepositoryInfo getRepository() {
		return repository;
	}
	public void setRepository(RepositoryInfo repository) {
		this.repository = repository;
	}
}
