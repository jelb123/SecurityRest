package au.edu.unsw.security;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
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
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import au.edu.unsw.security.dao.SecurityDAO;
import au.edu.unsw.security.dao.support.SecurityDAOImpl;
import au.edu.unsw.security.model.Expert;

/**
 * NEED TO DO:
 * IMPROVE SECURITY OF ALL FUNCTIONS
 * Archiving a job posting - restrict to manager
 * ALSO NEED TO TEST
 *
 */

@Path("/ExpertService")
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
		SecurityDAO securityDAO = new SecurityDAOImpl();
		securityDAO.setUpDatabase();
	}
	
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAllExperts() throws IOException {
		SecurityDAO securityDAO = new SecurityDAOImpl();
		
		List<Expert> expertList = securityDAO.getAllExperts();
		return Response.ok().entity(expertList).build();
	}
	
	@GET
	@Path("{expertID}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getExpertById(@PathParam("expertID") String idString) throws IOException {
		int expertID = Integer.parseInt(idString);
		
		SecurityDAO securityDAO = new SecurityDAOImpl();
		Expert expert = securityDAO.getExpertById(expertID);
		
		return Response.ok().entity(expert).build();
		
	}
	
	
	// Can Search using jobName, position, location, status
	@GET
	@Path("expertSearch")
	@Consumes(MediaType.TEXT_PLAIN)
	@Produces(MediaType.APPLICATION_JSON)
	public Response searchExperts(
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
		
		SecurityDAO securityDAO = new SecurityDAOImpl();
		List<Expert> expertSearchResults = securityDAO.getExpertsBySearch(expert);
		
		return Response.ok().entity(expertSearchResults).build();
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.TEXT_PLAIN)
	public Response createExpert(Expert expert) throws IOException, URISyntaxException {
		SecurityDAO securityDAO = new SecurityDAOImpl();
		int expertId = securityDAO.insertExpert(expert);
		return Response.created(new URI(uriInfo.getBaseUri() + "ExpertService/" + expertId)).entity("The created experts ID is " + expertId).build();
	}
	
	@PUT
	@Path("{expertID}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateExpertData(@PathParam("expertID") int expertId, Expert updatedExpertData) {
		SecurityDAO securityDAO = new SecurityDAOImpl();
		Expert updatedExpert = null;
		
		updatedExpertData.setId(expertId);
		updatedExpert = securityDAO.updateExpertData(updatedExpertData);
		
		return Response.ok().entity(updatedExpert).build();
	}
	
	@GET
	@Path("expertFilter")
	@Consumes(MediaType.TEXT_PLAIN)
	@Produces(MediaType.APPLICATION_JSON)
	public Response filterExperts(
			@DefaultValue("0") @QueryParam("minRank") int minRank,
			@DefaultValue("-1") @QueryParam("maxRank") int maxRank,
			@DefaultValue("0") @QueryParam("minRep") int minRep,
			@DefaultValue("-1") @QueryParam("maxRep") int maxRep,
			@DefaultValue("0") @QueryParam("minQuality") float minQuality,
			@DefaultValue("-1") @QueryParam("maxQuality") float maxQuality) {

		
		SecurityDAO securityDAO = new SecurityDAOImpl();
		List<Expert> expertFilterResults = securityDAO.getExpertsByFilter(minRank, maxRank, minRep, maxRep, minQuality, maxQuality);
		
		return Response.ok().entity(expertFilterResults).build();
	}
	
}
