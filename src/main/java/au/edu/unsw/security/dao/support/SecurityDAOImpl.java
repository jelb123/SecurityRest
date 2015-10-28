package au.edu.unsw.security.dao.support;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;

import au.edu.unsw.security.dao.SecurityDAO;
import au.edu.unsw.security.model.Expert;

public class SecurityDAOImpl implements SecurityDAO {
	
	public SecurityDAOImpl() {
	}
	
	@Override
	public void setUpDatabase (ServletContext context) {
		Connection c = null;
	    Statement stmt = null;
	    try {
	      Class.forName("org.sqlite.JDBC");
	      c = DriverManager.getConnection("jdbc:sqlite:rest.db");
	      System.out.println("Opened database successfully");

	      stmt = c.createStatement();
	      
	      // If table isnt already in the database, create it
	      String sql = "CREATE TABLE IF NOT EXISTS SECURITYEXPERTS " +
	                   "(ID INTEGER PRIMARY KEY	AUTOINCREMENT NOT NULL," +
	                   " COBALTLINK        	TEXT    NOT NULL, " + 
	                   " NAME				TEXT,"
	                   + "EMAIL				TEXT,"
	                   + "NATIONALITY		TEXT,"
	                   + "EDUCATION			TEXT,"
	                   + "EXPERIENCE		TEXT,"
	                   + "SKILLS			TEXT,"
	                   + "LINKEDIN			TEXT,"
	                   + "TWITTER			TEXT,"
	                   + "GITHUB			TEXT,"
	                   + "WEBSITE			TEXT,"
	                   + "COBALTRANK		REAL,"
	                   + "COBALTREP			REAL,"
	                   + "COBALTQUALITY		REAL)";
	      stmt.executeUpdate(sql);
	      System.out.println("Job Postings Table created successfully");
	      
	     
	      
	      stmt.close();
	      c.close();
	      if (getAllExperts().isEmpty()) {
	    	  load_data(context);
	    	  System.out.println("Added initial experts");
	      }
	    } catch ( Exception e ) {
	      System.err.println( e.getClass().getName() + ": " + e.getMessage() );
	      System.out.println("Couldnt Set up database");
	    }
	    
	}
	
	private void load_data(ServletContext context) throws ClassNotFoundException, SQLException{
		//String csvFile = "SecurityRest/resources/cobalt_task.csv";
		String csvFile = context.getRealPath("WEB-INF/cobalt_task.csv");
		//String csvFile = getClass().getResource("/resources/cobalt_task.csv").getPath();
		System.out.println(csvFile);
		BufferedReader br = null;	
		Connection c = null;
		PreparedStatement stmt = null;
		try {
			br = new BufferedReader(new FileReader(csvFile));
			String line = br.readLine();
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:rest.db");
			while ((line = br.readLine()) != null) {
				System.out.println(line);
				String[] tokens = line.split(",");
				String name = tokens[0];
				int cobaltRank = Integer.parseInt(tokens[1]);
				Float cobaltReportQuality = Float.parseFloat(tokens[2]);
				int cobaltRep = Integer.parseInt(tokens[3]);
				String twitter = tokens[4];
				String website = tokens[5];
				String cobaltLink = tokens[6];
				String education = tokens[7];
				String email = tokens[8];
				String experience = tokens[9];
				String nationality = tokens[10];
				String skills = tokens[11];
				String github = tokens[12];
				String linkedin = tokens[13];				
				String sql = "INSERT INTO SECURITYEXPERTS (COBALTLINK,NAME,"
						+ "EMAIL,NATIONALITY,EDUCATION,EXPERIENCE,SKILLS,LINKEDIN,"
						+ "TWITTER,GITHUB,WEBSITE,COBALTRANK,COBALTREP,COBALTQUALITY) "
						+ "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?)"; 
				stmt = c.prepareStatement(sql);
				stmt.setString(1, cobaltLink);
				stmt.setString(2, name);
				stmt.setString(3, email);
				stmt.setString(4, nationality);
				stmt.setString(5, education);
				stmt.setString(6, experience);
				stmt.setString(7, skills);
				stmt.setString(8, linkedin);
				stmt.setString(9, twitter);
				stmt.setString(10, github);
				stmt.setString(11, website);
				stmt.setInt(12, cobaltRank);
				stmt.setInt(13, cobaltRep);
				stmt.setFloat(14, cobaltReportQuality);
				stmt.executeUpdate();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (c != null) {
				try {
					c.close();
				} catch (Exception e) {
				}
			}
			if (stmt != null) {
				try {
					stmt.close();
				} catch (Exception e) {
				}
			}
		}

	}
	
	@Override
	public int insertExpert(Expert expert) {
		Connection c = null;
		PreparedStatement stmt = null;
	    try {
	    	Class.forName("org.sqlite.JDBC");
	    	c = DriverManager.getConnection("jdbc:sqlite:rest.db");
	    	c.setAutoCommit(false);
		    System.out.println("Opened database successfully");
		    
		    stmt = c.prepareStatement("INSERT INTO SECURITYEXPERTS (COBALTLINK, NAME, " +
			    	"EMAIL, NATIONALITY, EDUCATION, EXPERIENCE, SKILLS, "
			    	+ "LINKEDIN, TWITTER, GITHUB, WEBSITE, COBALTRANK, "
			    	+ "COBALTREP, COBALTQUALITY) " +
			    	"VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?);"); 
		    stmt.setString(1, expert.getCobaltLink());
		    stmt.setString(2, expert.getName());
		    stmt.setString(3, expert.getEmail());
		    stmt.setString(4, expert.getNationality());
		    stmt.setString(5, expert.getEducation());
		    stmt.setString(6, expert.getExperience());
		    stmt.setString(7, expert.getSkills());
		    stmt.setString(8, expert.getLinkedin());
		    stmt.setString(9, expert.getTwitter());
		    stmt.setString(10, expert.getGithub());
		    stmt.setString(11, expert.getWebsite());
		    stmt.setInt(12, expert.getCobaltRank());
		    stmt.setInt(13, expert.getCobaltRep());
		    stmt.setFloat(14, expert.getCobaltReportQuality());
		    stmt.executeUpdate();
		  
		    c.commit();
		    
		    c.close();
	    } catch ( Exception e ) {
	    	
	      System.err.println( e.getClass().getName() + ": " + e.getMessage() );
	      System.out.println("Couldnt insert expert");
	    }
	    System.out.println("Records created successfully");
	    return maxExpertId();
	}
	
	/**
	 * Retrieves the Id of the last expert added into the database
	 * @return the largest id
	 */
	private int maxExpertId() {
		Connection c = null;
		Statement stmt = null;
		int lastExpertId = 0;
		
		try {
	      Class.forName("org.sqlite.JDBC");
	      c = DriverManager.getConnection("jdbc:sqlite:rest.db");
	      c.setAutoCommit(false);
	      
	      stmt = c.createStatement();
	      ResultSet rs = stmt.executeQuery( "SELECT MAX(ID) AS LASTEXPERT FROM SECURITYEXPERTS;" );
	      if(rs.next()) {
	    	  lastExpertId = rs.getInt("LASTEXPERT") ;
	      }
    	  rs.close() ;
	      
	      c.commit();
	      c.close();

	    } catch ( Exception e ) {
	      System.err.println( e.getClass().getName() + ": " + e.getMessage() );
	      System.out.println("Couldnt get last Expert");
	    }
		
	    return lastExpertId;
	}
	
	@Override
	public Expert getExpertById(int expertID) {
		Connection c = null;
		PreparedStatement stmt = null;
		Expert expert = null;
	    try {
	    	Class.forName("org.sqlite.JDBC");
	    	c = DriverManager.getConnection("jdbc:sqlite:rest.db");
	    	c.setAutoCommit(false);
		    System.out.println("Opened database successfully");
		    
		    stmt = c.prepareStatement("SELECT * FROM SECURITYEXPERTS WHERE ID = ? "); 
		    stmt.setInt(1, expertID);
		    ResultSet rs = stmt.executeQuery();
		    
		    if(rs.next()) {
			    expert = new Expert();
				expert.setId(rs.getInt("ID"));
				expert.setCobaltLink(rs.getString("COBALTLINK"));
				expert.setName(rs.getString("NAME"));
				expert.setEmail(rs.getString("EMAIL"));
				expert.setNationality(rs.getString("NATIONALITY"));
				expert.setEducation(rs.getString("EDUCATION"));
				expert.setExperience(rs.getString("EXPERIENCE"));
				expert.setSkills(rs.getString("SKILLS"));
				expert.setLinkedin(rs.getString("LINKEDIN"));
				expert.setTwitter(rs.getString("TWITTER"));
				expert.setGithub(rs.getString("GITHUB"));
				expert.setWebsite(rs.getString("WEBSITE"));
				expert.setCobaltRank(rs.getInt("COBALTRANK"));
				expert.setCobaltRep(rs.getInt("COBALTREP"));
				expert.setCobaltReportQuality(rs.getFloat("COBALTQUALITY"));
		    }			
		    
		    rs.close();
		    stmt.close();
		    c.close();
	    } catch ( Exception e ) {
	    	
	      System.err.println( e.getClass().getName() + ": " + e.getMessage() );
	      System.out.println("Couldnt Retrieve expert");
	    }
	    System.out.println("Records created successfully");
	    return expert;
	}

	@Override
	public List<Expert> getAllExperts() {
		Connection c = null;
		Statement stmt = null;
		List<Expert> expertList = new ArrayList<Expert>();
		Expert expert = null;
		try {
	      Class.forName("org.sqlite.JDBC");
	      c = DriverManager.getConnection("jdbc:sqlite:rest.db");
	      c.setAutoCommit(false);
	      
	      stmt = c.createStatement();
	      ResultSet rs = stmt.executeQuery( "SELECT * FROM SECURITYEXPERTS;" );
	      while ( rs.next() ) {
			expert = new Expert();
			expert.setId(rs.getInt("ID"));
			expert.setCobaltLink(rs.getString("COBALTLINK"));
			expert.setName(rs.getString("NAME"));
			expert.setEmail(rs.getString("EMAIL"));
			expert.setNationality(rs.getString("NATIONALITY"));
			expert.setEducation(rs.getString("EDUCATION"));
			expert.setExperience(rs.getString("EXPERIENCE"));
			expert.setSkills(rs.getString("SKILLS"));
			expert.setLinkedin(rs.getString("LINKEDIN"));
			expert.setTwitter(rs.getString("TWITTER"));
			expert.setGithub(rs.getString("GITHUB"));
			expert.setWebsite(rs.getString("WEBSITE"));
			expert.setCobaltRank(rs.getInt("COBALTRANK"));
			expert.setCobaltRep(rs.getInt("COBALTREP"));
			expert.setCobaltReportQuality(rs.getFloat("COBALTQUALITY"));
			
			expertList.add(expert);
	      }
	      rs.close() ;
	      stmt.close();
	      c.close();

	    } catch ( Exception e ) {
	      System.err.println( e.getClass().getName() + ": " + e.getMessage() );
	      System.out.println("Could retrieve experts");
	    }
		
	    return expertList;
	}


	@Override
	public List<Expert> getExpertsBySearch(Expert expertSearchParams) {
		Connection c = null;
		PreparedStatement stmt = null;
		List<Expert> expertList = new ArrayList<Expert>();
		Expert expert = null;
		try {
	      Class.forName("org.sqlite.JDBC");
	      c = DriverManager.getConnection("jdbc:sqlite:rest.db");
	      c.setAutoCommit(false);
	      
	      String wildCard = "%";
	      String name = wildCard + expertSearchParams.getName() + wildCard;
	      String email = wildCard + expertSearchParams.getEmail() + wildCard;
	      String education = wildCard + expertSearchParams.getEducation() + wildCard;
	      String nationality = wildCard + expertSearchParams.getNationality() + wildCard;
	      String experience = wildCard + expertSearchParams.getExperience() + wildCard;
	      String skills = wildCard + expertSearchParams.getSkills() + wildCard; 
	      
	      stmt = c.prepareStatement("SELECT * FROM SECURITYEXPERTS WHERE LOWER(NAME) LIKE LOWER(?)" +
	    		  	"AND LOWER(EMAIL) LIKE LOWER(?)" + 
	    		  	"AND LOWER(EDUCATION) LIKE LOWER(?)" +
	    		  	"AND LOWER(EXPERIENCE) LIKE LOWER(?)" + 
	    		  	"AND LOWER(NATIONALITY) LIKE LOWER(?)" +
	    		  	"AND LOWER(SKILLS) LIKE LOWER(?)");
	      
	      stmt.setString(1, name);
	      stmt.setString(2, email);
	      stmt.setString(3, education);
	      stmt.setString(4, experience);
	      stmt.setString(5, nationality);
	      stmt.setString(6, skills);
	      
	      ResultSet rs = stmt.executeQuery();
	      while ( rs.next() ) {
	    	  expert = new Expert();
	    	  expert.setId(rs.getInt("ID"));
	    	  expert.setCobaltLink(rs.getString("COBALTLINK"));
	    	  expert.setName(rs.getString("NAME"));
	    	  expert.setEmail(rs.getString("EMAIL"));
	    	  expert.setNationality(rs.getString("NATIONALITY"));
	    	  expert.setEducation(rs.getString("EDUCATION"));
	    	  expert.setExperience(rs.getString("EXPERIENCE"));
	    	  expert.setSkills(rs.getString("SKILLS"));
	    	  expert.setLinkedin(rs.getString("LINKEDIN"));
	    	  expert.setTwitter(rs.getString("TWITTER"));
	    	  expert.setGithub(rs.getString("GITHUB"));
	    	  expert.setWebsite(rs.getString("WEBSITE"));
	    	  expert.setCobaltRank(rs.getInt("COBALTRANK"));
	    	  expert.setCobaltRep(rs.getInt("COBALTREP"));
	    	  expert.setCobaltReportQuality(rs.getFloat("COBALTQUALITY"));
	          
	    	  expertList.add(expert);
	      }
	      rs.close() ;
	      stmt.close();
	      c.close();

	    } catch ( Exception e ) {
	      System.err.println( e.getClass().getName() + ": " + e.getMessage() );
	      System.out.println("Couldnt retrieve searched experts");
	    }
		
	    return expertList;
	}

	@Override
	public Expert updateExpertData(Expert updatedExpertData) {
		Expert expert = getExpertById(updatedExpertData.getId());
		
		// Checks whether these fields need to be updated before setting them.
		if(updatedExpertData.getCobaltLink() != null) {
			expert.setCobaltLink(updatedExpertData.getCobaltLink());
		}
		
		if(updatedExpertData.getCobaltRank() != 0) {
			expert.setCobaltRank(updatedExpertData.getCobaltRank());
		}
		
		if(updatedExpertData.getCobaltRep() != 0) {
			expert.setCobaltRep(updatedExpertData.getCobaltRep());
		}
		
		if(updatedExpertData.getCobaltReportQuality() != 0) {
			expert.setCobaltReportQuality(updatedExpertData.getCobaltReportQuality());
		}
		
		if(updatedExpertData.getEducation() != null) {
			expert.setEducation(updatedExpertData.getEducation());
		}
		
		if(updatedExpertData.getEmail() != null) {
			expert.setEmail(updatedExpertData.getEmail());
		}
		
		if(updatedExpertData.getExperience() != null) {
			expert.setExperience(updatedExpertData.getExperience());
		}
		
		if(updatedExpertData.getGithub() != null) {
			expert.setGithub(updatedExpertData.getGithub());
		}
		
		if(updatedExpertData.getLinkedin() != null) {
			expert.setLinkedin(updatedExpertData.getLinkedin());
		}
		
		if(updatedExpertData.getName() != null) {
			expert.setName(updatedExpertData.getName());
		}
		
		if(updatedExpertData.getNationality() != null) {
			expert.setNationality(updatedExpertData.getNationality());
		}
		
		if(updatedExpertData.getSkills() != null) {
			expert.setSkills(updatedExpertData.getSkills());
		}
		
		if(updatedExpertData.getTwitter() != null) {
			expert.setTwitter(updatedExpertData.getTwitter());
		}
		
		if(updatedExpertData.getWebsite() != null) {
			expert.setWebsite(updatedExpertData.getWebsite());
		}
		
		Connection c = null;
		PreparedStatement stmt = null;
	    try {
	    	Class.forName("org.sqlite.JDBC");
	    	c = DriverManager.getConnection("jdbc:sqlite:rest.db");
	    	c.setAutoCommit(false);
		    System.out.println("Opened database successfully");
		
		    //Update the experts row in the database with the new data
		    stmt = c.prepareStatement("UPDATE SECURITYEXPERTS "
	    		+ "SET COBALTLINK = ?, NAME = ?, EMAIL = ?, NATIONALITY = ?, "
	    		+ "EDUCATION = ?, EXPERIENCE = ?, SKILLS = ?, LINKEDIN = ?, "
	    		+ "TWITTER = ?, GITHUB = ?, WEBSITE = ?, COBALTRANK = ?, "
	    		+ "COBALTREP = ?, COBALTQUALITY = ?"
	    		+ "WHERE ID = ?"); 
		    
		    stmt.setString(1, expert.getCobaltLink());
		    stmt.setString(2, expert.getName());
		    stmt.setString(3, expert.getEmail());
		    stmt.setString(4, expert.getNationality());
		    stmt.setString(5, expert.getEducation());
		    stmt.setString(6, expert.getExperience());
		    stmt.setString(7, expert.getSkills());
		    stmt.setString(8, expert.getLinkedin());
		    stmt.setString(9, expert.getTwitter());
		    stmt.setString(10, expert.getGithub());
		    stmt.setString(11, expert.getWebsite());
		    stmt.setInt(12, expert.getCobaltRank());
		    stmt.setInt(13, expert.getCobaltRep());
		    stmt.setFloat(14, expert.getCobaltReportQuality());
		    stmt.setInt(15, expert.getId());
		    
		    stmt.executeUpdate();
		    c.commit();
		    
		    expert = getExpertById(expert.getId());
		    
		    stmt.close();
		    c.close();
	    } catch ( Exception e ) {
	    	
	      System.err.println( e.getClass().getName() + ": " + e.getMessage() );
	      System.out.println("Couldnt update the user data");
	    }
	    System.out.println("Records updated successfully");
	    return expert;
	}

	/**
	 * Gets the highest Cobalt Rank in the database
	 * @return the highest cobalt rank
	 */
	private int maxCobaltRank() {
		Connection c = null;
		Statement stmt = null;
		int maxRank = 0;
		
		try {
	      Class.forName("org.sqlite.JDBC");
	      c = DriverManager.getConnection("jdbc:sqlite:rest.db");
	      c.setAutoCommit(false);
	      
	      stmt = c.createStatement();
	      ResultSet rs = stmt.executeQuery( "SELECT MAX(COBALTRANK) AS MAXRANK FROM SECURITYEXPERTS;" );
	      if(rs.next()) {
	    	  maxRank = rs.getInt("MAXRANK") ;
	      }
    	  rs.close() ;
	      
	      c.commit();
	      c.close();

	    } catch ( Exception e ) {
	      System.err.println( e.getClass().getName() + ": " + e.getMessage() );
	      System.out.println("Couldnt get max rank");
	    }
		
	    return maxRank;
	}
	
	/**
	 * Gets the highest Cobalt Reputation from the database
	 * @return the highest Cobalt Reputation
	 */
	private int maxCobaltRep() {
		Connection c = null;
		Statement stmt = null;
		int maxRep = 0;
		
		try {
	      Class.forName("org.sqlite.JDBC");
	      c = DriverManager.getConnection("jdbc:sqlite:rest.db");
	      c.setAutoCommit(false);
	      
	      stmt = c.createStatement();
	      ResultSet rs = stmt.executeQuery( "SELECT MAX(COBALTREP) AS MAXREP FROM SECURITYEXPERTS;" );
	      if(rs.next()) {
	    	  maxRep = rs.getInt("MAXREP") ;
	      }
    	  rs.close() ;
	      
	      c.commit();
	      c.close();

	    } catch ( Exception e ) {
	      System.err.println( e.getClass().getName() + ": " + e.getMessage() );
	      System.out.println("Couldnt get max rep");
	    }
		
	    return maxRep;
	}
	
	/**
	 * Gets the highest Cobalt Report Quality from the database
	 * @return the highest Cobalt Report Quality
	 */
	private float maxCobaltQuality() {
		Connection c = null;
		Statement stmt = null;
		float maxQuality = 0;
		
		try {
	      Class.forName("org.sqlite.JDBC");
	      c = DriverManager.getConnection("jdbc:sqlite:rest.db");
	      c.setAutoCommit(false);
	      
	      stmt = c.createStatement();
	      ResultSet rs = stmt.executeQuery( "SELECT MAX(COBALTQUALITY) AS MAXQUALITY FROM SECURITYEXPERTS;" );
	      if(rs.next()) {
	    	  maxQuality = rs.getFloat("MAXQUALITY") ;
	      }
    	  rs.close() ;
	      
	      c.commit();
	      c.close();

	    } catch ( Exception e ) {
	      System.err.println( e.getClass().getName() + ": " + e.getMessage() );
	      System.out.println("Couldnt get max quality");
	    }
		
	    return maxQuality;
	}
	
	@Override
	public List<Expert> getExpertsByFilter(int minRank, int maxRank, int minRep, int maxRep, float minQuality, float maxQuality) {
		Connection c = null;
		PreparedStatement stmt = null;
		List<Expert> expertList = new ArrayList<Expert>();
		Expert expert = null;
		
		// Checks whether these were set by the service user
		if (maxRank == -1) {
			maxRank = maxCobaltRank();
		}
		if (maxRep == -1) {
			maxRep = maxCobaltRep();
		}
		if (maxQuality == -1) {
			maxQuality = maxCobaltQuality();
		}
		
		try {
	      Class.forName("org.sqlite.JDBC");
	      c = DriverManager.getConnection("jdbc:sqlite:rest.db");
	      c.setAutoCommit(false);
	      
	      stmt = c.prepareStatement("SELECT * FROM SECURITYEXPERTS "
	      		+ "WHERE COBALTRANK >= ? "
	      		+ "AND COBALTRANK <= ?"
	      		+ "AND COBALTREP >= ?"
	      		+ "AND COBALTREP <= ?"
	      		+ "AND COBALTQUALITY >= ?"
	      		+ "AND COBALTQUALITY <= ?)");
	      
	      stmt.setInt(1, minRank);
	      stmt.setInt(2, maxRank);
	      stmt.setInt(3, minRep);
	      stmt.setInt(4, maxRep);
	      stmt.setFloat(5, minQuality);
	      stmt.setFloat(6, maxQuality);
	      

	      ResultSet rs = stmt.executeQuery();
	      while ( rs.next() ) {
	    	  expert = new Expert();
	    	  expert.setId(rs.getInt("ID"));
	    	  expert.setCobaltLink(rs.getString("COBALTLINK"));
	    	  expert.setName(rs.getString("NAME"));
	    	  expert.setEmail(rs.getString("EMAIL"));
	    	  expert.setNationality(rs.getString("NATIONALITY"));
	    	  expert.setEducation(rs.getString("EDUCATION"));
	    	  expert.setExperience(rs.getString("EXPERIENCE"));
	    	  expert.setSkills(rs.getString("SKILLS"));
	    	  expert.setLinkedin(rs.getString("LINKEDIN"));
	    	  expert.setTwitter(rs.getString("TWITTER"));
	    	  expert.setGithub(rs.getString("GITHUB"));
	    	  expert.setWebsite(rs.getString("WEBSITE"));
	    	  expert.setCobaltRank(rs.getInt("COBALTRANK"));
	    	  expert.setCobaltRep(rs.getInt("COBALTREP"));
	    	  expert.setCobaltReportQuality(rs.getFloat("COBALTQUALITY"));
	          
	    	  expertList.add(expert);
	      }
	      rs.close() ;
	      stmt.close();
	      c.close();

	    } catch ( Exception e ) {
	      System.err.println( e.getClass().getName() + ": " + e.getMessage() );
	      System.out.println("Couldnt retrieve filtered experts");
	    }
		
	    return expertList;
	}
}
