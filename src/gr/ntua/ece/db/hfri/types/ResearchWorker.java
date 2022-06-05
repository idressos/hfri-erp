package gr.ntua.ece.db.hfri.types;

import java.util.Date;

public class ResearchWorker {
	
	private int id;
	private int organisationId;
	private String firstName;
	private String lastName;
	private Sex sex;
	private int age;
	private Date birthDate;
	private Date joinDate;
	
	public ResearchWorker(int id, int organisationId, String firstName, String lastName,
			Sex sex, int age, Date birthDate, Date joinDate) {
		
		this.id = id;
		this.organisationId = organisationId;
		this.firstName = firstName;
		this.lastName = lastName;
		this.sex = sex;
		this.age = age;
		this.birthDate = birthDate;
		this.joinDate = joinDate;
	}
	
	public int getId() {
		return id;
	}
	
	public int getOrganisationId() {
		return organisationId;
	}
	
	public String getFirstName() {
		return firstName;
	}
	
	public String getLastName() {
		return lastName;
	}
	
	public Sex getSex() {
		return sex;
	}
	
	public int getAge() {
		return age;
	}
	
	public Date getBirthDate() {
		return birthDate;
	}
	
	public Date getJoinDate() {
		return joinDate;
	}
	
	public enum Sex {
		MALE, FEMALE
	}
	
}