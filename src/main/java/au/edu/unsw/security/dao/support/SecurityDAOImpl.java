package au.edu.unsw.security.dao.support;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import au.edu.unsw.security.dao.SecurityDAO;
import au.edu.unsw.security.model.Expert;

public class SecurityDAOImpl implements SecurityDAO {
	
	public SecurityDAOImpl() {
	}
	
	public void setUpDatabase () {
		Connection c = null;
	    Statement stmt = null;
	    try {
	      Class.forName("org.sqlite.JDBC");
	      c = DriverManager.getConnection("jdbc:sqlite:rest.db");
	      System.out.println("Opened database successfully");

	      stmt = c.createStatement();
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
	    } catch ( Exception e ) {
	      System.err.println( e.getClass().getName() + ": " + e.getMessage() );
	      System.out.println("Couldnt Set up database");
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
