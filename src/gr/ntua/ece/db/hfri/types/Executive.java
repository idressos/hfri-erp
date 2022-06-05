package gr.ntua.ece.db.hfri.types;

public class Executive {
	
	private int id;
	private String name;
	
	public Executive(int id, String name) {
		this.id = id;
		this.name = name;
	}
	
	public int getId() {
		return id;
	}
	
	public String getName() {
		return name;
	}
	
}