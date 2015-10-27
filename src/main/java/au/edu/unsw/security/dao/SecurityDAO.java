package au.edu.unsw.security.dao;

import java.util.List;

import au.edu.unsw.security.model.Expert;

/**
 * The Data Access Object for jobs
 *
 */
public interface SecurityDAO {
		
	/**
	 * Retrieves an Experts data from the database
	 * @param expertID the id of the expert to retrieve
	 * @return the Expert requested
	 */
	Expert getExpertById (int expertID);
	
	/**
	 * Retrieves all the experts from the database
	 * @return all experts
	 */
	List<Expert> getAllExperts ();
	
	/**
	 * Retrieves the experts according to the search
	 * @param expert the model containing the search terms
	 * @return list of all experts matching the search
	 */
	List<Expert> getExpertsBySearch (Expert expert);
	
	/**
	 * Sets Up Database for use in the API
	 */
	void setUpDatabase();
	
	/**
	 * Inserts a new row of expert data into the database
	 * @param expert the expert data to insert
	 * @return the id of the inserted expert
	 */
	int insertExpert(Expert expert);
	
	/**
	 * Updates the data of an existing expert specified by their id 
	 * @param updatedExpertData the bean containing the updated data
	 * @return the updated expert
	 */
	Expert updateExpertData(Expert updatedExpertData);

	/**
	 * Retrieves the list of experts matching the filter from the database 
	 * @param minRank the minimum rank of expert (i.e rank 1)
	 * @param maxRank the maximum rank of expert (i.e rank 100)
	 * @param minRep the minimum reputation of the expert
	 * @param maxRep the maximum reputation of the expert
	 * @param minQuality the minimum report quality of the expert
	 * @param maxQuality the maximum report quality of the expert
	 * @return a list of all experts matching the given filters
	 */
	List<Expert> getExpertsByFilter(int minRank, int maxRank, int minRep, int maxRep, float minQuality,
			float maxQuality);
	
}
