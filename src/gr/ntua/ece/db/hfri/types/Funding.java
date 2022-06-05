package gr.ntua.ece.db.hfri.types;

public class Funding {
	
	private int programId;
	private int projectId;
	private int sum;
	
	public Funding(int programId, int projectId, int sum) {
		this.programId = programId;
		this.projectId = projectId;
		this.sum = sum;
	}
	
	public int getProgramId() {
		return programId;
	}
	
	public int getProjectId() {
		return projectId;
	}
	
	public int getSum() {
		return sum;
	}
	
}