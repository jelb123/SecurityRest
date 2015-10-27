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
 * The java implementation of the Security Expert web service functions 
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
	
	/**
	 * This is called to initially set up and initialize the database
	 * 
	 */
	@GET
	@Path("setUpDatabase")
	public void setUpDatabase() {
		SecurityDAO securityDAO = new SecurityDAOImpl();
		securityDAO.setUpDatabase();
	}
	
	/**
	 * Retrieves all the security experts that are stored in the databases
	 * @return a list of all the security experts and their data in a JSON format
	 * @throws IOException
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAllExperts() throws IOException {
		SecurityDAO securityDAO = new SecurityDAOImpl();
		
		List<Expert> expertList = securityDAO.getAllExperts();
		return Response.ok().entity(expertList).build();
	}
	
	/**
	 * Retrieves the data of the security expert whose id was specified 
	 * @param expertID the id of the expert needed
	 * @return The security experts data in JSON Format
	 * @throws IOException
	 */
	@GET
	@Path("{expertID}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getExpertById(@PathParam("expertID") int expertID) throws IOException {
		
		SecurityDAO securityDAO = new SecurityDAOImpl();
		Expert expert = securityDAO.getExpertById(expertID);
		
		return Response.ok().entity(expert).build();
		
	}
	
	/**
	 * A Search function to find all the experts which fit the needed criteria
	 * @param name the name of the expert
	 * @param email the experts email
	 * @param education the education which the expert has
	 * @param nationality the nationality of the expert
	 * @param experience past experience which the expert might have had
	 * @param skills the skills which the expert has
	 * @return A list of all the security experts which fit the criteria
	 */
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
	
	/**
	 * Create and insert an expert into the database
	 * @param expert The complete expert to be added into the database (this is automatically converted from the json representation when it is passed in)
	 * @return The location where the expert data can be retrieved from in the response header, and a string containing the experts id in the response body
	 * @throws IOException
	 * @throws URISyntaxException
	 */
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.TEXT_PLAIN)
	public Response insertExpert(Expert expert) throws IOException, URISyntaxException {
		SecurityDAO securityDAO = new SecurityDAOImpl();
		int expertId = securityDAO.insertExpert(expert);
		return Response.created(new URI(uriInfo.getBaseUri() + "ExpertService/" + expertId)).entity("The created experts ID is " + expertId).build();
	}
	
	/**
	 * Update an expert profile which is already in the database 
	 * @param expertId the id of the expert to be created
	 * @param updatedExpertData a bean of the expert containing the fields that need to be updated
	 * @return the data of the updated expert if expert exists
	 */

	@PUT
	@Path("{expertID}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateExpertData(@PathParam("expertID") int expertId, Expert updatedExpertData) {
		SecurityDAO securityDAO = new SecurityDAOImpl();
		Expert updatedExpert = null;
		
		if (securityDAO.getExpertById(expertId) != null) {
			updatedExpertData.setId(expertId);
			updatedExpert = securityDAO.updateExpertData(updatedExpertData);
			return Response.ok().entity(updatedExpert).build();
		} else {
			return Response.ok().entity("There is no expert with that id").build();
		}
	}
	
	/**
	 * Filters the experts according to the parameter values
	 * @param minRank the minimum Rank on cobalt.io that the expert should have (i.e 1 for best rank)
	 * @param maxRank the maximum Rank on cobalt.io that the expert should have 
	 * @param minRep the minimum reputation on cobalt.io that the expert should have 
	 * @param maxRep the maximum reputation on cobalt.io that the expert should have
	 * @param minQuality the minimum report quality on cobalt.io that the expert should have
	 * @param maxQuality the maximum report quality on cobalt.io that the expert should have
	 * @return Returns a list of all the security experts which match the filter criteria
	 */
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
