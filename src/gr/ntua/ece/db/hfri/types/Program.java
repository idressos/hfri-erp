package gr.ntua.ece.db.hfri.types;

public class Program {
	
	private int id;
	private String name;
	private int addressId;
	
	public Program(int id, String name, int addressId) {
		this.id = id;
		this.name = name;
		this.addressId = addressId;
	}
	
	public int getId() {
		return id;
	}
	
	public String getName() {
		return name;
	}
	
	public int getAddressId() {
		return addressId;
	}
	
}