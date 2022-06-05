package gr.ntua.ece.db.hfri.types;

import java.util.Date;

public class Review {
	
	private int projectId;
	private int researchWorkerId;
	private Rating rating;
	private Date date;
	
	public Review(int projectId, int researchWorkerId, Rating rating, Date date) {
		this.projectId = projectId;
		this.researchWorkerId = researchWorkerId;
		this.rating = rating;
		this.date = date;
	}
	
	public int getProjectId() {
		return projectId;
	}
	
	public int getResearchWorkerId() {
		return researchWorkerId;
	}
	
	public Rating getRating() {
		return rating;
	}
	
	public Date getDate() {
		return date;
	}
	
	public enum Rating {
		ONE(1), TWO(2), THREE(3), FOUR(4), FIVE(5), SIX(6), SEVEN(7), EIGHT(8), NINE(9), TEN(10);
		
		private int value;
		
		Rating(int value) {
			this.value = value;
		}
		
	    public int getValue() {
	        return value;
	    }
	}
	
}