package gr.ntua.ece.db.hfri.erp;

import java.util.List;
import java.util.ArrayList;

import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.PreparedStatement;

import java.text.SimpleDateFormat;

import com.jcraft.jsch.JSchException;

import org.mariadb.jdbc.MariaDbDataSource;

import gr.ntua.ece.db.hfri.types.*;

public class Database {
	
	private static SSHTunnel sshTunnel;
	private static MariaDbDataSource dataSource = new MariaDbDataSource();
	
	private static SimpleDateFormat sqlDateFormat = new SimpleDateFormat("yyyy-MM-dd");
	
	/*
	 * Initializes the dataSource object according to user configuration.
	 * If enabled, also initializes the sshTunnel object for tunneling database connections through SSH (random port is forwarded).
	 */
	public static void configureServerConnection(String databaseUser, String databasePassword) throws SQLException, JSchException {
		String databaseType = Settings.getString("database.type");
		String databaseHost = Settings.getString("database.host");
		int databasePort = Settings.getInt("database.port");
		String databaseSchema = Settings.getString("database.schema");
		boolean databaseAllowPublicKeyRetrieval = Settings.getBoolean("database.allow-public-key-retrieval");
		
		if(Settings.getBoolean("ssh.tunnel")) {
			String sshHost = Settings.getString("ssh.host");
			int sshPort = Settings.getInt("ssh.port");
			String sshUser = Settings.getString("ssh.user");
			String sshPassword = Settings.getString("ssh.password");
			
			boolean sshStrictHostKeyChecking = Settings.getBoolean("ssh.strict-host-key-checking.enabled");
			String sshKnownHostsFile = Settings.getString("ssh.strict-host-key-checking.known-hosts-file");
			
			sshTunnel = new SSHTunnel(sshHost, sshPort, sshUser, sshKnownHostsFile, sshStrictHostKeyChecking);
			
			if(Settings.getBoolean("ssh.rsa-authentication.enabled")) {
				String keyFile = Settings.getString("ssh.rsa-authentication.key-file");
				String passphrase = Settings.getString("ssh.rsa-authentication.passphrase");
				
				sshTunnel.addIdentity(keyFile, (!passphrase.isEmpty() ? passphrase : null));
			} else sshTunnel.setPassword(sshPassword);
			
			sshTunnel.connect();
			
			databasePort = sshTunnel.forwardRandomLocalPort(databaseHost, databasePort);
			databaseHost = "127.0.0.1";
		}
		
		dataSource.setUrl(
			"jdbc:" + databaseType + "://"
					+ databaseHost + ":"
					+ databasePort + "/"
					+ databaseSchema
					+ (databaseUser != null && !databaseUser.isEmpty() ? "?user=" + databaseUser : "")
					+ (databasePassword != null && !databasePassword.isEmpty() ? "&password=" + databasePassword : "")
					+ (databaseType.equalsIgnoreCase("mysql") ? "&permitMysqlScheme" : "")
					+ (databaseAllowPublicKeyRetrieval ? "&allowPublicKeyRetrieval=true" : ""));
	}
	
	/*
	 * Used to verify connection to the database.
	 */
	public static boolean verifyConnection() throws SQLException {
		if(dataSource != null) {
			try(Connection connection = dataSource.getConnection()) {
				return connection.isValid(1);
			}
		}
		
		return false;
	}
	
	/*
	 * The methods that follow are used for manipulating the database entities.
	 */
	
	public static int getProjectCount() throws SQLException {
		try(Connection connection = dataSource.getConnection()) {
			try(PreparedStatement statement = connection.prepareStatement("SHOW TABLE STATUS WHERE NAME='projects';")) {
				try(ResultSet results = statement.executeQuery()) {
					while(results.next()) {
						return results.getInt("ROWS");
					}
				}
			}
		}
		
		return 0;
	}
	
	public static Project getProject(int id) throws SQLException {
		try(Connection connection = dataSource.getConnection()) {
			try(PreparedStatement statement = connection.prepareStatement("SELECT * FROM projects WHERE project_id = ?;")) {
				statement.setInt(1, id);
				
				try(ResultSet results = statement.executeQuery()) {
					if(results.next()) {
						return new Project(
							id,
							results.getString("title"),
							results.getString("description"),
							results.getDate("start_date"),
							results.getDate("finish_date"),
							results.getInt("duration_years"),
							results.getInt("organisation_id"),
							results.getInt("executive_id"),
							results.getInt("supervisor_id")
						);
					}
				}
			}
		}
		
		return null;
	}
	
	public static List<Project> getProjects(String... filters) throws SQLException {
		List<Project> projects;
		
		try(Connection connection = dataSource.getConnection()) {
			try(PreparedStatement statement = connection.prepareStatement("SELECT * FROM projects" + (filters.length > 0 ? " WHERE " + filtersToSql(filters) : "") + " ORDER BY project_id ASC;")) {
				try(ResultSet results = statement.executeQuery()) {
					projects = new ArrayList<Project>();
					
					while(results.next()) {
						projects.add(new Project(
							results.getInt("project_id"),
							results.getString("title"),
							results.getString("description"),
							results.getDate("start_date"),
							results.getDate("finish_date"),
							results.getInt("duration_years"),
							results.getInt("organisation_id"),
							results.getInt("executive_id"),
							results.getInt("supervisor_id")
						));
					}
				}
			}
		}
		
		return projects;
	}
	
	public static int insert(Project project) throws SQLException {
		try(Connection connection = dataSource.getConnection()) {
			try(PreparedStatement statement = connection.prepareStatement(
				"INSERT INTO projects"
			  + "(title, description, start_date, finish_date, organisation_id, executive_id, supervisor_id)"
			  + "VALUES"
			  + "(?, ?, ?, ?, ?, ?, ?);", Statement.RETURN_GENERATED_KEYS)) {
				statement.setString(1, project.getTitle());
				statement.setString(2, project.getDescription());
				statement.setDate(3, new java.sql.Date(project.getStartDate().getTime()));
				statement.setDate(4, new java.sql.Date(project.getFinishDate().getTime()));
				statement.setInt(5, project.getOrganisationId());
				statement.setInt(6, project.getExecutiveId());
				statement.setInt(7, project.getSupervisorId());
				
				statement.executeUpdate();
				
				try(ResultSet results = statement.getGeneratedKeys()) {
					if(results.next()) return results.getInt(1);
				}
			}
		}
		
		return -1;
	}
	
	public static void update(Project project) throws SQLException {
		try(Connection connection = dataSource.getConnection()) {
			try(PreparedStatement statement = connection.prepareStatement(
				"UPDATE projects "
			  + "SET"
			  + "  title = ?,"
			  + "  description = ?,"
			  + "  start_date = ?,"
			  + "  finish_date = ?,"
			  + "  organisation_id = ?,"
			  + "  executive_id = ?,"
			  + "  supervisor_id = ? "
			  + "WHERE"
			  + "  project_id = ?;")) {
				statement.setString(1, project.getTitle());
				statement.setString(2, project.getDescription());
				statement.setDate(3, new java.sql.Date(project.getStartDate().getTime()));
				statement.setDate(4, new java.sql.Date(project.getFinishDate().getTime()));
				statement.setInt(5, project.getOrganisationId());
				statement.setInt(6, project.getExecutiveId());
				statement.setInt(7, project.getSupervisorId());
				statement.setInt(8, project.getId());
				
				statement.executeUpdate();
			}
		}
	}
	
	public static List<ResearchField> getProjectResearchFields(int projectId) throws SQLException {
		List<ResearchField> projectFields;
		
		try(Connection connection = dataSource.getConnection()) {
			try(PreparedStatement statement = connection.prepareStatement(
				"SELECT"
			  + "  rf.research_field_id,"
			  + "  rf.description "
			  + "FROM"
			  + "  research_fields rf "
			  + "INNER JOIN project_research_fields prf"
			  + "  ON prf.research_field_id = rf.research_field_id "
			  + "WHERE prf.project_id = ? "
			  + "ORDER BY research_field_id ASC;")) {
				statement.setInt(1, projectId);
				
				try(ResultSet results = statement.executeQuery()) {
					projectFields = new ArrayList<ResearchField>();
					
					while(results.next()) {
						projectFields.add(new ResearchField(
							results.getInt("research_field_id"),
							results.getString("description")
						));
					}
				}
			}
		}
		
		return projectFields;
	}
	
	public static List<ResearchWorker> getProjectResearchWorkers(int projectId, String... filters) throws SQLException {
		List<ResearchWorker> projectResearchWorkers;
		
		try(Connection connection = dataSource.getConnection()) {
			try(PreparedStatement statement = connection.prepareStatement(
				"SELECT"
			  + "  rw.research_worker_id,"
			  + "  rw.organisation_id,"
			  + "  rw.first_name,"
			  + "  rw.last_name,"
			  + "  rw.sex,"
			  + "  rw.age,"
			  + "  rw.birth_date,"
			  + "  rw.join_date "
			  + "FROM"
			  + "  research_workers rw "
			  + "INNER JOIN project_research_workers prw"
			  + "  ON prw.research_worker_id = rw.research_worker_id "
			  + "WHERE prw.project_id = ?" + (filters.length > 0 ? " AND (" + filtersToSql(filters) + ")": "") + " "
			  + "ORDER BY research_worker_id ASC;")) {
				statement.setInt(1, projectId);
				
				try(ResultSet results = statement.executeQuery()) {
					projectResearchWorkers = new ArrayList<ResearchWorker>();
					
					while(results.next()) {
						projectResearchWorkers.add(new ResearchWorker(
							results.getInt("research_worker_id"),
							results.getInt("organisation_id"),
							results.getString("first_name"),
							results.getString("last_name"),
							ResearchWorker.Sex.valueOf(results.getString("sex")),
							results.getInt("age"),
							results.getDate("birth_date"),
							results.getDate("join_date")
						));
					}
				}
			}
		}
		
		return projectResearchWorkers;
	}
	
	public static List<Review> getProjectReviews(int projectId) throws SQLException {
		List<Review> projectReviews;
		
		try(Connection connection = dataSource.getConnection()) {
			try(PreparedStatement statement = connection.prepareStatement("SELECT * FROM reviews WHERE project_id = ? ORDER BY research_worker_id ASC;")) {
				statement.setInt(1, projectId);
				
				try(ResultSet results = statement.executeQuery()) {
					projectReviews = new ArrayList<Review>();
					
					while(results.next()) {
						projectReviews.add(new Review(
							projectId,
							results.getInt("research_worker_id"),
							Review.Rating.values()[results.getInt("rating")],
							results.getDate("date")
						));
					}
				}
			}
		}
		
		return projectReviews;
	}
	
	public static List<Funding> getProjectFundings(int projectId) throws SQLException {
		List<Funding> projectFundings;
		
		try(Connection connection = dataSource.getConnection()) {
			try(PreparedStatement statement = connection.prepareStatement("SELECT * FROM fundings WHERE project_id = ? ORDER BY program_id ASC;")) {
				statement.setInt(1, projectId);
				
				try(ResultSet results = statement.executeQuery()) {
					projectFundings = new ArrayList<Funding>();
					
					while(results.next()) {
						projectFundings.add(new Funding(
							results.getInt("program_id"),
							projectId,
							results.getInt("sum")
						));
					}
				}
			}
		}
		
		return projectFundings;
	}
	
	public static int getExecutiveCount() throws SQLException {
		try(Connection connection = dataSource.getConnection()) {
			try(PreparedStatement statement = connection.prepareStatement("SHOW TABLE STATUS WHERE NAME='executives';")) {
				try(ResultSet results = statement.executeQuery()) {
					while(results.next()) {
						return results.getInt("ROWS");
					}
				}
			}
		}
		
		return 0;
	}
	
	public static List<Executive> getExecutives() throws SQLException {
		List<Executive> executives;
		
		try(Connection connection = dataSource.getConnection()) {
			try(PreparedStatement statement = connection.prepareStatement("SELECT * FROM executives ORDER BY executive_id ASC;")) {
				try(ResultSet results = statement.executeQuery()) {
					executives = new ArrayList<Executive>();
					
					while(results.next()) {
						executives.add(new Executive(
							results.getInt("executive_id"),
							results.getString("name")
						));
					}
				}
			}
		}
		
		return executives;
	}
	
	public static Executive getExecutive(int id) throws SQLException {
		try(Connection connection = dataSource.getConnection()) {
			try(PreparedStatement statement = connection.prepareStatement("SELECT * FROM executives WHERE executive_id = ?;")) {
				statement.setInt(1, id);
				
				try(ResultSet results = statement.executeQuery()) {
					if(results.next()) {
						return new Executive(
							id,
							results.getString("name")
						);
					}
				}
			}
		}
		
		return null;
	}
	
	public static int insert(Executive executive) throws SQLException {
		try(Connection connection = dataSource.getConnection()) {
			try(PreparedStatement statement = connection.prepareStatement(
				"INSERT INTO executives"
			  + "(name)"
			  + "VALUES"
			  + "(?);", Statement.RETURN_GENERATED_KEYS)) {
				statement.setString(1, executive.getName());
				
				statement.executeUpdate();
				
				try(ResultSet results = statement.getGeneratedKeys()) {
					if(results.next()) return results.getInt(1);
				}
			}
		}
		
		return -1;
	}
	
	public static void update(Executive executive) throws SQLException {
		try(Connection connection = dataSource.getConnection()) {
			try(PreparedStatement statement = connection.prepareStatement(
				"UPDATE executives "
			  + "SET"
			  + "  name = ? "
			  + "WHERE"
			  + "  executive_id = ?;")) {
				statement.setString(1, executive.getName());
				statement.setInt(2, executive.getId());
				
				statement.executeUpdate();
			}
		}
	}
	
	public static Commission getCommission(int id) throws SQLException {
		try(Connection connection = dataSource.getConnection()) {
			try(PreparedStatement statement = connection.prepareStatement("SELECT * FROM commissions WHERE commission_id = ?;")) {
				statement.setInt(1, id);
				
				try(ResultSet results = statement.executeQuery()) {
					if(results.next()) {
						return new Commission(
							id,
							results.getInt("project_id"),
							results.getString("title"),
							results.getString("description"),
							results.getDate("deadline")
						);
					}
				}
			}
		}
		
		return null;
	}
	
	public static int getOrganisationCount() throws SQLException {
		try(Connection connection = dataSource.getConnection()) {
			try(PreparedStatement statement = connection.prepareStatement("SHOW TABLE STATUS WHERE NAME='organisations';")) {
				try(ResultSet results = statement.executeQuery()) {
					while(results.next()) {
						return results.getInt("ROWS");
					}
				}
			}
		}
		
		return 0;
	}
	
	public static Organisation getOrganisation(int id) throws SQLException {
		try(Connection connection = dataSource.getConnection()) {
			try(PreparedStatement statement = connection.prepareStatement("SELECT * FROM organisations WHERE organisation_id = ?;")) {
				statement.setInt(1, id);
				
				try(ResultSet results = statement.executeQuery()) {
					if(results.next()) {
						return new Organisation(
							id,
							results.getString("acronym"),
							results.getString("name"),
							Organisation.OrganisationType.valueOf(results.getString("type")),
							results.getInt("address_id"),
							results.getInt("budget")
						);
					}
				}
			}
		}
		
		return null;
	}
	
	public static int insert(Organisation organisation) throws SQLException {
		try(Connection connection = dataSource.getConnection()) {
			try(PreparedStatement statement = connection.prepareStatement(
				"INSERT INTO organisations"
			  + "(acronym, name, type, address_id, budget)"
			  + "VALUES"
			  + "(?, ?, ?, ?, ?);", Statement.RETURN_GENERATED_KEYS)) {
				statement.setString(1, organisation.getAcronym());
				statement.setString(2, organisation.getName());
				statement.setString(3, organisation.getType().toString());
				statement.setInt(4, organisation.getAddressId());
				statement.setInt(5, organisation.getBudget());
				
				statement.executeUpdate();
				
				try(ResultSet results = statement.getGeneratedKeys()) {
					if(results.next()) return results.getInt(1);
				}
			}
		}
		
		return -1;
	}
	
	public static void update(Organisation organisation) throws SQLException {
		try(Connection connection = dataSource.getConnection()) {
			try(PreparedStatement statement = connection.prepareStatement(
				"UPDATE organisations "
			  + "SET"
			  + "  acronym = ?,"
			  + "  name = ?,"
			  + "  type = ?,"
			  + "  address_id = ?,"
			  + "  budget = ? "
			  + "WHERE"
			  + "  organisation_id = ?;")) {
				statement.setString(1, organisation.getAcronym());
				statement.setString(2, organisation.getName());
				statement.setString(3, organisation.getType().toString());
				statement.setInt(4, organisation.getAddressId());
				statement.setInt(5, organisation.getBudget());
				statement.setInt(6, organisation.getId());
				
				statement.executeUpdate();
			}
		}
	}
	
	public static List<ResearchWorker> getOrganisationResearchWorkers(int organisationId) throws SQLException {
		List<ResearchWorker> organisationWorkers;
		
		try(Connection connection = dataSource.getConnection()) {
			try(PreparedStatement statement = connection.prepareStatement("SELECT * FROM research_workers WHERE organisation_id = ? ORDER BY research_worker_id ASC;")) {
				statement.setInt(1, organisationId);
				
				try(ResultSet results = statement.executeQuery()) {
					organisationWorkers = new ArrayList<ResearchWorker>();
					
					while(results.next()) {
						organisationWorkers.add(new ResearchWorker(
							results.getInt("research_worker_id"),
							organisationId,
							results.getString("first_name"),
							results.getString("last_name"),
							ResearchWorker.Sex.valueOf(results.getString("sex")),
							results.getInt("age"),
							results.getDate("birth_date"),
							results.getDate("join_date")
						));
					}
				}
			}
		}
		
		return organisationWorkers;
	}
	
	public static List<PhoneNumber> getOrganisationPhoneNumbers(int organisationId) throws SQLException {
		List<PhoneNumber> organisationPhoneNumbers;
		
		try(Connection connection = dataSource.getConnection()) {
			try(PreparedStatement statement = connection.prepareStatement("SELECT * FROM phone_numbers WHERE organisation_id = ? ORDER BY phone_number_id ASC;")) {
				statement.setInt(1, organisationId);
				
				try(ResultSet results = statement.executeQuery()) {
					organisationPhoneNumbers = new ArrayList<PhoneNumber>();
					
					while(results.next()) {
						organisationPhoneNumbers.add(new PhoneNumber(
							results.getInt("phone_number_id"),
							organisationId,
							results.getString("number")
						));
					}
				}
			}
		}
		
		return organisationPhoneNumbers;
	}
	
	public static Funding getFunding(int programId, int projectId) throws SQLException {
		try(Connection connection = dataSource.getConnection()) {
			try(PreparedStatement statement = connection.prepareStatement("SELECT * FROM fundings WHERE program_id = ? AND project_id = ?;")) {
				statement.setInt(1, programId);
				statement.setInt(2, projectId);
				
				try(ResultSet results = statement.executeQuery()) {
					if(results.next()) {
						return new Funding(
							programId,
							projectId,
							results.getInt("sum")
						);
					}
				}
			}
		}
		
		return null;
	}
	
	public static Review getReview(int projectId, int researchWorkerId) throws SQLException {
		try(Connection connection = dataSource.getConnection()) {
			try(PreparedStatement statement = connection.prepareStatement("SELECT * FROM reviews WHERE project_id = ? AND research_worker_id = ?;")) {
				statement.setInt(1, projectId);
				statement.setInt(2, researchWorkerId);
				
				try(ResultSet results = statement.executeQuery()) {
					if(results.next()) {
						return new Review(
							projectId,
							researchWorkerId,
							Review.Rating.values()[results.getInt("rating")],
							results.getDate("date")
						);
					}
				}
			}
		}
		
		return null;
	}
	
	public static int getResearchWorkerCount() throws SQLException {
		try(Connection connection = dataSource.getConnection()) {
			try(PreparedStatement statement = connection.prepareStatement("SHOW TABLE STATUS WHERE NAME='research_workers';")) {
				try(ResultSet results = statement.executeQuery()) {
					while(results.next()) {
						return results.getInt("ROWS");
					}
				}
			}
		}
		
		return 0;
	}
	
	public static ResearchWorker getResearchWorker(int id) throws SQLException {
		try(Connection connection = dataSource.getConnection()) {
			try(PreparedStatement statement = connection.prepareStatement("SELECT * FROM research_workers WHERE research_worker_id = ?;")) {
				statement.setInt(1, id);
				
				try(ResultSet results = statement.executeQuery()) {
					if(results.next()) {
						return new ResearchWorker(
							id,
							results.getInt("organisation_id"),
							results.getString("first_name"),
							results.getString("last_name"),
							ResearchWorker.Sex.valueOf(results.getString("sex")),
							results.getInt("age"),
							results.getDate("birth_date"),
							results.getDate("join_date")
						);
					}
				}
			}
		}
		
		return null;
	}
	
	public static int insert(ResearchWorker researchWorker) throws SQLException {
		try(Connection connection = dataSource.getConnection()) {
			try(PreparedStatement statement = connection.prepareStatement(
				"INSERT INTO research_workers"
			  + "(organisation_id, first_name, last_name, sex, age, birth_date, join_date)"
			  + "VALUES"
			  + "(?, ?, ?, ?, ?, ?, ?);", Statement.RETURN_GENERATED_KEYS)) {
				statement.setInt(1, researchWorker.getOrganisationId());
				statement.setString(2, researchWorker.getFirstName());
				statement.setString(3, researchWorker.getLastName());
				statement.setString(4, researchWorker.getSex().toString());
				statement.setInt(5, researchWorker.getAge());
				statement.setDate(6, new java.sql.Date(researchWorker.getBirthDate().getTime()));
				statement.setDate(7, new java.sql.Date(researchWorker.getJoinDate().getTime()));
				
				statement.executeUpdate();
				
				try(ResultSet results = statement.getGeneratedKeys()) {
					if(results.next()) return results.getInt(1);
				}
			}
		}
		
		return -1;
	}
	
	public static void update(ResearchWorker researchWorker) throws SQLException {
		try(Connection connection = dataSource.getConnection()) {
			try(PreparedStatement statement = connection.prepareStatement(
				"UPDATE research_workers "
			  + "SET"
			  + "  organisation_id = ?,"
			  + "  first_name = ?,"
			  + "  last_name = ?,"
			  + "  sex = ?,"
			  + "  age = ?,"
			  + "  birth_date = ?,"
			  + "  join_date = ? "
			  + "WHERE"
			  + "  research_worker_id = ?;")) {
				statement.setInt(1, researchWorker.getOrganisationId());
				statement.setString(2, researchWorker.getFirstName());
				statement.setString(3, researchWorker.getLastName());
				statement.setString(4, researchWorker.getSex().toString());
				statement.setInt(5, researchWorker.getAge());
				statement.setDate(6, new java.sql.Date(researchWorker.getBirthDate().getTime()));
				statement.setDate(7, new java.sql.Date(researchWorker.getJoinDate().getTime()));
				statement.setInt(8, researchWorker.getId());
				
				statement.executeUpdate();
			}
		}
	}
	
	public static List<ResearchWorker> getResearchWorkers(String... filters) throws SQLException {
		List<ResearchWorker> researchWorkers;
		
		try(Connection connection = dataSource.getConnection()) {
			try(PreparedStatement statement = connection.prepareStatement("SELECT * FROM research_workers" + (filters.length > 0 ? " WHERE " + filtersToSql(filters) : "") + " ORDER BY research_worker_id ASC;")) {
				try(ResultSet results = statement.executeQuery()) {
					researchWorkers = new ArrayList<ResearchWorker>();
					
					while(results.next()) {
						researchWorkers.add(new ResearchWorker(
							results.getInt("research_worker_id"),
							results.getInt("organisation_id"),
							results.getString("first_name"),
							results.getString("last_name"),
							ResearchWorker.Sex.valueOf(results.getString("sex")),
							results.getInt("age"),
							results.getDate("birth_date"),
							results.getDate("join_date")
						));
					}
				}
			}
		}
		
		return researchWorkers;
	}
	
	public static List<Project> getResearchWorkerProjects(int researchWorkerId, String... filters) throws SQLException {
		List<Project> researchWorkerProjects;
		
		try(Connection connection = dataSource.getConnection()) {
			try(PreparedStatement statement = connection.prepareStatement(
				"SELECT"
			  + "  p.project_id,"
			  + "  p.title,"
			  + "  p.description,"
			  + "  p.start_date,"
			  + "  p.finish_date,"
			  + "  p.duration_years,"
			  + "  p.organisation_id,"
			  + "  p.executive_id,"
			  + "  p.supervisor_id "
			  + "FROM"
			  + "  projects p "
			  + "INNER JOIN project_research_workers prw"
			  + "  ON prw.project_id = p.project_id "
			  + "WHERE prw.research_worker_id = ?" + (filters.length > 0 ? " AND (" + filtersToSql(filters) + ")": "") + " "
			  + "ORDER BY project_id ASC;")) {
				statement.setInt(1, researchWorkerId);
				
				try(ResultSet results = statement.executeQuery()) {
					researchWorkerProjects = new ArrayList<Project>();
					
					while(results.next()) {
						researchWorkerProjects.add(new Project(
							results.getInt("project_id"),
							results.getString("title"),
							results.getString("description"),
							results.getDate("start_date"),
							results.getDate("finish_date"),
							results.getInt("duration_years"),
							results.getInt("organisation_id"),
							results.getInt("executive_id"),
							results.getInt("supervisor_id")
						));
					}
				}
			}
		}
		
		return researchWorkerProjects;
	}
	
	public static List<Review> getResearchWorkerReviews(int researchWorkerId) throws SQLException {
		List<Review> projectReviews;
		
		try(Connection connection = dataSource.getConnection()) {
			try(PreparedStatement statement = connection.prepareStatement("SELECT * FROM reviews WHERE research_worker_id = ? ORDER BY project_id ASC;")) {
				statement.setInt(1, researchWorkerId);
				
				try(ResultSet results = statement.executeQuery()) {
					projectReviews = new ArrayList<Review>();
					
					while(results.next()) {
						projectReviews.add(new Review(
							results.getInt("project_id"),
							researchWorkerId,
							Review.Rating.values()[results.getInt("rating")],
							results.getDate("date")
						));
					}
				}
			}
		}
		
		return projectReviews;
	}
	
	public static int getProgramCount() throws SQLException {
		try(Connection connection = dataSource.getConnection()) {
			try(PreparedStatement statement = connection.prepareStatement("SHOW TABLE STATUS WHERE NAME='programs';")) {
				try(ResultSet results = statement.executeQuery()) {
					while(results.next()) {
						return results.getInt("ROWS");
					}
				}
			}
		}
		
		return 0;
	}
	
	public static Program getProgram(int id) throws SQLException {
		try(Connection connection = dataSource.getConnection()) {
			try(PreparedStatement statement = connection.prepareStatement("SELECT * FROM programs WHERE program_id = ?;")) {
				statement.setInt(1, id);
				
				try(ResultSet results = statement.executeQuery()) {
					if(results.next()) {
						return new Program(
							id,
							results.getString("name"),
							results.getInt("address_id")
						);
					}
				}
			}
		}
		
		return null;
	}
	
	public static int insert(Program program) throws SQLException {
		try(Connection connection = dataSource.getConnection()) {
			try(PreparedStatement statement = connection.prepareStatement(
				"INSERT INTO programs"
			  + "(name, address_id)"
			  + "VALUES"
			  + "(?, ?);", Statement.RETURN_GENERATED_KEYS)) {
				statement.setString(1, program.getName());
				statement.setInt(2, program.getAddressId());
				
				statement.executeUpdate();
				
				try(ResultSet results = statement.getGeneratedKeys()) {
					if(results.next()) return results.getInt(1);
				}
			}
		}
		
		return -1;
	}
	
	public static void update(Program program) throws SQLException {
		try(Connection connection = dataSource.getConnection()) {
			try(PreparedStatement statement = connection.prepareStatement(
				"UPDATE programs "
			  + "SET"
			  + "  name = ?,"
			  + "  address_id = ? "
			  + "WHERE"
			  + "  program_id = ?;")) {
				statement.setString(1, program.getName());
				statement.setInt(2, program.getAddressId());
				statement.setInt(3, program.getId());
				
				statement.executeUpdate();
			}
		}
	}
	
	public static List<Program> getPrograms(String... filters) throws SQLException {
		List<Program> programs;
		
		try(Connection connection = dataSource.getConnection()) {
			try(PreparedStatement statement = connection.prepareStatement("SELECT * FROM programs" + (filters.length > 0 ? " WHERE " + filtersToSql(filters) : "") + " ORDER BY program_id ASC;")) {
				try(ResultSet results = statement.executeQuery()) {
					programs = new ArrayList<Program>();
					
					while(results.next()) {
						programs.add(new Program(
							results.getInt("program_id"),
							results.getString("name"),
							results.getInt("address_id")
						));
					}
				}
			}
		}
		
		return programs;
	}
	
	public static List<Funding> getProgramFundings(int programId) throws SQLException {
		List<Funding> programFundings;
		
		try(Connection connection = dataSource.getConnection()) {
			try(PreparedStatement statement = connection.prepareStatement("SELECT * FROM fundings WHERE program_id = ? ORDER BY project_id ASC;")) {
				statement.setInt(1, programId);
				
				try(ResultSet results = statement.executeQuery()) {
					programFundings = new ArrayList<Funding>();
					
					while(results.next()) {
						programFundings.add(new Funding(
							programId,
							results.getInt("project_id"),
							results.getInt("sum")
						));
					}
				}
			}
		}
		
		return programFundings;
	}
	
	public static ResearchField getResearchField(int id) throws SQLException {
		try(Connection connection = dataSource.getConnection()) {
			try(PreparedStatement statement = connection.prepareStatement("SELECT * FROM research_fields WHERE research_field_id = ?;")) {
				statement.setInt(1, id);
				
				try(ResultSet results = statement.executeQuery()) {
					if(results.next()) {
						return new ResearchField(
							id,
							results.getString("description")
						);
					}
				}
			}
		}
		
		return null;
	}
	
	public static List<Project> getResearchFieldProjects(int researchFieldId) throws SQLException {
		List<Project> fieldProjects;
		
		try(Connection connection = dataSource.getConnection()) {
			try(PreparedStatement statement = connection.prepareStatement(
				"SELECT"
			  + "  p.project_id,"
			  + "  p.title"
			  + "  p.description"
			  + "  p.start_date"
			  + "  p.finish_date"
			  + "  p.duration_years"
			  + "  p.is_active"
			  + "  p.organisation_id"
			  + "  p.executive_id"
			  + "  p.supervisor_id "
			  + "FROM"
			  + "  projects p "
			  + "INNER JOIN project_research_fields prf"
			  + "  ON prf.project_id = p.project_id "
			  + "WHERE prf.research_field_id = ? "
			  + "ORDER BY project_id ASC;")) {
				statement.setInt(1, researchFieldId);
				
				try(ResultSet results = statement.executeQuery()) {
					fieldProjects = new ArrayList<Project>();
					
					while(results.next()) {
						fieldProjects.add(new Project(
							results.getInt("project_id"),
							results.getString("title"),
							results.getString("description"),
							results.getDate("start_date"),
							results.getDate("finish_date"),
							results.getInt("duration_years"),
							results.getInt("organisation_id"),
							results.getInt("executive_id"),
							results.getInt("supervisor_id")
						));
					}
				}
			}
		}
		
		return fieldProjects;
	}
	
	public static PhoneNumber getPhoneNumber(int id) throws SQLException {
		try(Connection connection = dataSource.getConnection()) {
			try(PreparedStatement statement = connection.prepareStatement("SELECT * FROM phone_numbers WHERE phone_number_id = ?;")) {
				statement.setInt(1, id);
				
				try(ResultSet results = statement.executeQuery()) {
					if(results.next()) {
						return new PhoneNumber(
							id,
							results.getInt("organisation_id"),
							results.getString("number")
						);
					}
				}
			}
		}
		
		return null;
	}
	
	public static int getAddressCount() throws SQLException {
		try(Connection connection = dataSource.getConnection()) {
			try(PreparedStatement statement = connection.prepareStatement("SHOW TABLE STATUS WHERE NAME='addresses';")) {
				try(ResultSet results = statement.executeQuery()) {
					while(results.next()) {
						return results.getInt("ROWS");
					}
				}
			}
		}
		
		return 0;
	}
	
	public static Address getAddress(int id) throws SQLException {
		try(Connection connection = dataSource.getConnection()) {
			try(PreparedStatement statement = connection.prepareStatement("SELECT * FROM addresses WHERE address_id = ?;")) {
				statement.setInt(1, id);
				
				try(ResultSet results = statement.executeQuery()) {
					if(results.next()) {
						return new Address(
							id,
							results.getInt("number"),
							results.getString("street"),
							results.getString("city"),
							results.getString("postal_code")
						);
					}
				}
			}
		}
		
		return null;
	}
	
	public static int insert(Address address) throws SQLException {
		try(Connection connection = dataSource.getConnection()) {
			try(PreparedStatement statement = connection.prepareStatement(
				"INSERT INTO addresses"
			  + "(number, street, city, postal_code)"
			  + "VALUES"
			  + "(?, ?, ?, ?);", Statement.RETURN_GENERATED_KEYS)) {
				statement.setInt(1, address.getNumber());
				statement.setString(2, address.getStreet());
				statement.setString(3, address.getCity());
				statement.setString(4, address.getPostalCode());
				
				statement.executeUpdate();
				
				try(ResultSet results = statement.getGeneratedKeys()) {
					if(results.next()) return results.getInt(1);
				}
			}
		}
		
		return -1;
	}
	
	public static void update(Address address) throws SQLException {
		try(Connection connection = dataSource.getConnection()) {
			try(PreparedStatement statement = connection.prepareStatement(
				"UPDATE addresses "
			  + "SET"
			  + "  number = ?,"
			  + "  street = ?,"
			  + "  city = ?,"
			  + "  postal_code = ? "
			  + "WHERE"
			  + "  address_id = ?;")) {
				statement.setInt(1, address.getNumber());
				statement.setString(2, address.getStreet());
				statement.setString(3, address.getCity());
				statement.setString(4, address.getPostalCode());
				statement.setInt(5, address.getId());
				
				statement.executeUpdate();
			}
		}
	}
	
	/*
	 * The methods that follow are used in specific requests required
	 * for the semester project.
	 */
	
	public static List<Object[]> getMostActiveYoungResearchWorkers() throws SQLException {
		List<Object[]> mostActiveYoungResearchWorkers;
		
		try(Connection connection = dataSource.getConnection()) {
			try(PreparedStatement statement = connection.prepareStatement(
				"SELECT"
				+ "  rw.research_worker_id,"
			   	+ "  rw.organisation_id,"
			   	+ "  rw.first_name,"
			  	+ "  rw.last_name,"
		  		+ "  rw.sex,"
		  		+ "  rw.age,"
			    + "  rw.birth_date,"
			    + "  rw.join_date,"
				+ "  COUNT(prw.project_id) AS active_project_count "
				+ "FROM"
				+ "  project_research_workers prw "
				+ "INNER JOIN projects p"
				+ "  ON p.project_id = prw.project_id "
				+ "INNER JOIN research_workers rw"
				+ "  ON rw.research_worker_id = prw.research_worker_id "
				+ "WHERE rw.age < 40 AND p.start_date <= CURDATE() AND p.finish_date >= CURDATE() "
				+ "GROUP BY prw.research_worker_id "
				+ "ORDER BY active_project_count DESC;")) {
				
				try(ResultSet results = statement.executeQuery()) {
					mostActiveYoungResearchWorkers = new ArrayList<Object[]>();
					
					while(results.next()) {
						ResearchWorker researchWorker = new ResearchWorker(
							results.getInt("research_worker_id"),
							results.getInt("organisation_id"),
							results.getString("first_name"),
							results.getString("last_name"),
							ResearchWorker.Sex.valueOf(results.getString("sex")),
							results.getInt("age"),
							results.getDate("birth_date"),
							results.getDate("join_date")
						);
						
						Object[] array = new Object[2];
						array[0] = researchWorker;
						array[1] = results.getInt("active_project_count");
						
						mostActiveYoungResearchWorkers.add(array);
					}
				}
			}
		}
		
		return mostActiveYoungResearchWorkers;
	}
	
	public static List<Object[]> getTopExecutivesByFundings() throws SQLException {
		List<Object[]> topExecutivesByFundings;
		
		try(Connection connection = dataSource.getConnection()) {
			try(PreparedStatement statement = connection.prepareStatement(
				"SELECT"
				+ "  e.executive_id,"
				+ "  e.name AS executive_name,"
				+ "  o.name AS organisation_name,"
				+ "  SUM(f.sum) AS total_funded "
				+ "FROM executives e "
				+ "INNER JOIN projects p"
				+ "  ON p.executive_id = e.executive_id "
				+ "INNER JOIN fundings f"
				+ "  ON f.project_id = p.project_id "
				+ "INNER JOIN organisations o"
				+ "  ON o.organisation_id = p.organisation_id "
				+ "GROUP BY e.executive_id "
				+ "ORDER BY total_funded DESC "
				+ "LIMIT 5;")) {
				
				try(ResultSet results = statement.executeQuery()) {
					topExecutivesByFundings = new ArrayList<Object[]>();
					
					while(results.next()) {
						Executive executive = new Executive(
							results.getInt("executive_id"),
							results.getString("executive_name")
						);
						
						Object[] array = new Object[3];
						array[0] = executive;
						array[1] = results.getString("organisation_name");
						array[2] = results.getInt("total_funded");
						
						topExecutivesByFundings.add(array);
					}
				}
			}
		}
		
		return topExecutivesByFundings;
	}
	
	public static List<Object[]> getResearchWorkersInProjectsWithoutCommissions() throws SQLException {
		List<Object[]> researchWorkersInProjectsWithoutCommissions;
		
		try(Connection connection = dataSource.getConnection()) {
			try(PreparedStatement statement = connection.prepareStatement(
				"SELECT"
				+ "  rw.research_worker_id,"
				+ "  rw.organisation_id,"				
				+ "  rw.first_name,"
				+ "  rw.last_name,"
				+ "  rw.sex,"
				+ "  rw.age,"
				+ "  rw.birth_date,"
				+ "  rw.join_date,"
				+ "  COUNT(prw.project_id) AS no_commission_project_count "
				+ "FROM research_workers rw "
				+ "INNER JOIN project_research_workers prw"
				+ "  ON prw.research_worker_id = rw.research_worker_id "
				+ "WHERE prw.project_id NOT IN (SELECT project_id FROM commissions) "
				+ "GROUP BY rw.research_worker_id "
				+ "HAVING no_commission_project_count >= 2 "
				+ "ORDER BY no_commission_project_count DESC;")) {
				
				try(ResultSet results = statement.executeQuery()) {
					researchWorkersInProjectsWithoutCommissions = new ArrayList<Object[]>();
					
					while(results.next()) {
						ResearchWorker researchWorker = new ResearchWorker(
							results.getInt("research_worker_id"),
							results.getInt("organisation_id"),
							results.getString("first_name"),
							results.getString("last_name"),
							ResearchWorker.Sex.valueOf(results.getString("sex")),
							results.getInt("age"),
							results.getDate("birth_date"),
							results.getDate("join_date")
						);
						
						Object[] array = new Object[2];
						array[0] = researchWorker;
						array[1] = results.getInt("no_commission_project_count");
						
						researchWorkersInProjectsWithoutCommissions.add(array);
					}
				}
			}
		}
		
		return researchWorkersInProjectsWithoutCommissions;
	}
	
	/*
	 * Miscellaneous Methods
	 */
	
	public static SSHTunnel getSshTunnel() {
		return sshTunnel;
	}
	
	public static String getLastLoginUsername() {
		String lastLoginUsername = Settings.getString("database.last-login-username");
		
		return lastLoginUsername != null ? lastLoginUsername : "";
	}
	
	/*
	 * Converts an array of strings into one string for use in the WHERE clause.
	 * For example, {"age > 40", "height_cm > 175"} is converted into "age > 40 AND height_cm > 175".
	 */
	public static String filtersToSql(String[] filters) {
		List<String> filterList = new ArrayList<String>();
		
		for(int i = 0; i < filters.length; i++) {
			if(filters[i] != null && !filters[i].isEmpty()) filterList.add(filters[i]);
		}
		
		String query = "";
		for(String filter : filterList) query = query + filter + (filterList.indexOf(filter) != filterList.size() - 1 ? " AND " : "");
		
		return query;
	}
	
	public static SimpleDateFormat getSqlDateFormat() {
		return sqlDateFormat;
	}
	
}