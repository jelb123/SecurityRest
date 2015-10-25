package au.edu.unsw.security;

import java.io.IOException;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.UriInfo;

import au.edu.unsw.security.dao.support.SecurityDAOImpl;
import au.edu.unsw.security.model.Expert;

/**
 * NEED TO DO:
 * IMPROVE SECURITY OF ALL FUNCTIONS
 * Archiving a job posting - restrict to manager
 * ALSO NEED TO TEST
 *
 */

@Path("/jobPostings")
public class SecurityService {
	// Allows to insert contextual objects into the class, 
	// e.g. ServletContext, Request, Response, UriInfo
	@Context
	UriInfo uriInfo;
	@Context
	Request request;
	
	@GET
	@Path("setUpDatabase")
	public void setUpDatabase() {
		SecurityDAOImpl jobsDAO = new SecurityDAOImpl();
		jobsDAO.setUpDatabase();
	}
	
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<Expert> getAllExperts() throws IOException {
		SecurityDAOImpl securityDAO = new SecurityDAOImpl();
		
		List<Expert> expertList = securityDAO.getAllExperts();
		return expertList;
	}
	
	@GET
	@Path("{expertID}")
	@Produces(MediaType.APPLICATION_JSON)
	public Expert getJobById(@PathParam("expertID") String idString) throws IOException {
		int expertID = Integer.parseInt(idString);
		
		SecurityDAOImpl securityDAO = new SecurityDAOImpl();
		Expert expert = securityDAO.getExpertById(expertID);
		
		return expert;
		
	}
	
	
	// Can Search using jobName, position, location, status
	@GET
	@Path("expertSearch")
	@Consumes(MediaType.TEXT_PLAIN)
	@Produces(MediaType.APPLICATION_JSON)
	public List<Expert> searchJobs(
			@DefaultValue("") @QueryParam("expertName") String name,
			@DefaultValue("") @QueryParam("email") String email,
			@DefaultValue("") @QueryParam("education") String education,
			@DefaultValue("") @QueryParam("nationality") String nationality,
			@DefaultValue("") @QueryParam("experience") String experience,
			@DefaultValue("") @QueryParam("skills") String skills) {
		
		Expert expert = new Expert();
		expert.setName(name);
		expert.setEmail(email);
		expert.setEducation(education);
		expert.setNationality(nationality);
		expert.setExperience(experience);
		expert.setSkills(skills);
		
		SecurityDAOImpl securityDAO = new SecurityDAOImpl();
		List<Expert> expertSearchResults = securityDAO.getExpertsBySearch(expert);
		
		
		return expertSearchResults;
	}
	
}
