package gr.ntua.ece.db.hfri.types;

public class Address {
	
	private int id;
	private int number;
	private String street;
	private String city;
	private String postalCode;
	
	public Address(int id, int number, String street, String city, String postalCode) {
		this.id = id;
		this.number = number;
		this.street = street;
		this.city = city;
		this.postalCode = postalCode;
	}
	
	public int getId() {
		return id;
	}
	
	public int getNumber() {
		return number;
	}
	
	public String getStreet() {
		return street;
	}
	
	public String getCity() {
		return city;
	}
	
	public String getPostalCode() {
		return postalCode;
	}
	
}