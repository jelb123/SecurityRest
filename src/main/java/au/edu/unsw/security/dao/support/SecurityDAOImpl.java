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
	      System.exit(0);
	    }
	    
	}
	
	@Override
	public Expert getExpertById(int jobID) {
		Connection c = null;
		PreparedStatement stmt = null;
		Expert expert = null;
	    try {
	    	Class.forName("org.sqlite.JDBC");
	    	c = DriverManager.getConnection("jdbc:sqlite:rest.db");
	    	c.setAutoCommit(false);
		    System.out.println("Opened database successfully");
		    
		    stmt = c.prepareStatement("SELECT * FROM SECURITYEXPERTS WHERE ID = ? "); 
		    stmt.setInt(1, jobID);
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
	      System.exit(0);
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
	      ResultSet rs = stmt.executeQuery( "SELECT * FROM JOBPOSTINGS;" );
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
	      System.exit(0);
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
	      System.exit(0);
	    }
		
	    return expertList;
	}

}
