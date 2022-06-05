package gr.ntua.ece.db.hfri.types;

import java.util.Date;

public class Project {
	
	private int id;
	private String title;
	private String description;
	private Date startDate;
	private Date finishDate;
	private int durationYears;
	private int organisationId;
	private int executiveId;
	private int supervisorId;
	
	public Project(int id, String title, String description, Date startDate, Date finishDate,
			int durationYears, int organisationId, int executiveId, int supervisorId) {
		
		this.id = id;
		this.title = title;
		this.description = description;
		this.startDate = startDate;
		this.finishDate = finishDate;
		this.durationYears = durationYears;
		this.organisationId = organisationId;
		this.executiveId = executiveId;
		this.supervisorId = supervisorId;
	}
	
	public int getId() {
		return id;
	}
	
	public String getTitle() {
		return title;
	}
	
	public String getDescription() {
		return description;
	}
	
	public Date getStartDate() {
		return startDate;
	}
	
	public Date getFinishDate() {
		return finishDate;
	}
	
	public int getDurationYears() {
		return durationYears;
	}
	
	public int getStatusCode() {
		Date now  = new Date();
		
		if(finishDate.after(now)) {
			if(startDate.before(now)) return 1; // Active
			else return 2; // Scheduled
		} else return 0; // Inactive
	}
	
	public int getOrganisationId() {
		return organisationId;
	}
	
	public int getExecutiveId() {
		return executiveId;
	}
	
	public int getSupervisorId() {
		return supervisorId;
	}
	
}