package gr.ntua.ece.db.hfri.types;

import java.util.Date;

public class Commission {
	
	private int id;
	private int projectId;
	private String title;
	private String description;
	private Date deadline;
	
	public Commission(int id, int projectId, String title, String description, Date deadline) {
		this.id = id;
		this.projectId = projectId;
		this.title = title;
		this.description = description;
		this.deadline = deadline;
	}
	
	public int getId() {
		return id;
	}
	
	public int getProjectId() {
		return projectId;
	}
	
	public String getTitle() {
		return title;
	}
	
	public String getDescription() {
		return description;
	}
	
	public Date getDeadline() {
		return deadline;
	}
	
}