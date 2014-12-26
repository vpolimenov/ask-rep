package ask_rep.client;

import java.io.Serializable;
import java.sql.Timestamp;

@SuppressWarnings("serial")
public class RepositoryInfo implements Serializable {
	
	private int repositoryID;
	private String name;
	private Timestamp createdDate;
	private Timestamp updatedDate;	
	private UserInfo user;
	
	public int getRepositoryID() {
		return repositoryID;
	}
	public void setRepositoryID(int repositoryID) {
		this.repositoryID = repositoryID;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Timestamp getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Timestamp createdDate) {
		this.createdDate = createdDate;
	}
	public Timestamp getUpdatedDate() {
		return updatedDate;
	}
	public void setUpdatedDate(Timestamp updatedDate) {
		this.updatedDate = updatedDate;
	}
	public UserInfo getUser() {
		return user;
	}
	public void setUser(UserInfo user) {
		this.user = user;
	}

}
