package au.edu.unsw.security.dao;

import java.util.List;

import au.edu.unsw.security.model.Expert;

/**
 * The Data Access Object for jobs
 *
 */
public interface SecurityDAO {
		
	/**
	 * Retrieves a job posting from the database
	 * @param JobID the id of the job to recieve
	 * @return the JobPosting
	 */
	Expert getExpertById (int expertID);
	
	/**
	 * Retrieves all the jobs from the database that arent archived
	 * @return all unarchived jobs
	 */
	List<Expert> getAllExperts ();
	
	/**
	 * Retrieves the jobs according to the search
	 * @param expert the model containing the search terms
	 * @return list of jobs matching the search
	 */
	List<Expert> getExpertsBySearch (Expert expert);
	
}
