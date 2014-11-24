package ask_rep.client;

import java.sql.Date;

public class FolderInfo {
	
	private int folderID;
	private int parentFolderID;
	private String name;
	private Date datecreated;
	private Date dateupdated;
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
	public Date getDatecreated() {
		return datecreated;
	}
	public void setDatecreated(Date datecreated) {
		this.datecreated = datecreated;
	}
	public Date getDateupdated() {
		return dateupdated;
	}
	public void setDateupdated(Date dateupdated) {
		this.dateupdated = dateupdated;
	}
	public RepositoryInfo getRepository() {
		return repository;
	}
	public void setRepository(RepositoryInfo repository) {
		this.repository = repository;
	}
}
