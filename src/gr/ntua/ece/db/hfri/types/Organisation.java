package gr.ntua.ece.db.hfri.types;

public class Organisation {
	
	private int id;
	private String acronym;
	private String name;
	private OrganisationType type;
	private int addressId;
	private int budget;
	
	public Organisation(int id, String acronym, String name, OrganisationType type, int addressId,
			int budget) {
		
		this.id = id;
		this.acronym = acronym;
		this.name = name;
		this.type = type;
		this.addressId = addressId;
		this.budget = budget;
	}
	
	public int getId() {
		return id;
	}
	
	public String getAcronym() {
		return acronym;
	}
	
	public String getName() {
		return name;
	}
	
	public OrganisationType getType() {
		return type;
	}
	
	public int getAddressId() {
		return addressId;
	}
	
	public int getBudget() {
		return budget;
	}
	
	public enum OrganisationType {
		COLLEGE, RESEARCH_CENTER, COMPANY
	}
	
}