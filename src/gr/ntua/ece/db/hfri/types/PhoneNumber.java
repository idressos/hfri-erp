package gr.ntua.ece.db.hfri.types;

public class PhoneNumber {
	
	private int id;
	private int organisationId;
	private String number;
	
	public PhoneNumber(int id, int organisationId, String number) {
		this.id = id;
		this.organisationId = organisationId;
		this.number = number;
	}
	
	public int getId() {
		return id;
	}
	
	public int getOrganisationId() {
		return organisationId;
	}
	
	public String getNumber() {
		return number;
	}
	
}