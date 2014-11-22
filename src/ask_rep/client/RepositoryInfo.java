package ask_rep.client;

import java.io.Serializable;
import java.util.Date;

@SuppressWarnings("serial")
public class RepositoryInfo implements Serializable {
	
	private int repositoryID;
	private String name;
	private Date createdDate;
	private Date updatedDate;
	
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
	public Date getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	public Date getUpdatedDate() {
		return updatedDate;
	}
	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}
	

}
