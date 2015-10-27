package au.edu.unsw.security.model;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * The Java Bean model for a security expert
 * @author Group 6
 *
 */
@XmlRootElement
public class Expert {
	private int id;
	private String cobaltLink;
	private String name;
	private String email;
	private String nationality;
	private String education;
	private String experience;
	private String skills;
	private String linkedin;
	private String twitter;
	private String github;
	private String website;
	private int cobaltRank;
	private int cobaltRep;
	private float cobaltReportQuality;
	
	public Expert() {
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCobaltLink() {
		return cobaltLink;
	}

	public void setCobaltLink(String cobaltLink) {
		this.cobaltLink = cobaltLink;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getNationality() {
		return nationality;
	}

	public void setNationality(String nationality) {
		this.nationality = nationality;
	}

	public String getEducation() {
		return education;
	}

	public void setEducation(String education) {
		this.education = education;
	}

	public String getExperience() {
		return experience;
	}

	public void setExperience(String experience) {
		this.experience = experience;
	}

	public String getSkills() {
		return skills;
	}

	public void setSkills(String skills) {
		this.skills = skills;
	}

	public String getLinkedin() {
		return linkedin;
	}

	public void setLinkedin(String linkedin) {
		this.linkedin = linkedin;
	}

	public String getTwitter() {
		return twitter;
	}

	public void setTwitter(String twitter) {
		this.twitter = twitter;
	}

	public String getGithub() {
		return github;
	}

	public void setGithub(String github) {
		this.github = github;
	}

	public String getWebsite() {
		return website;
	}

	public void setWebsite(String website) {
		this.website = website;
	}

	public int getCobaltRank() {
		return cobaltRank;
	}

	public void setCobaltRank(int cobaltRank) {
		this.cobaltRank = cobaltRank;
	}

	public int getCobaltRep() {
		return cobaltRep;
	}

	public void setCobaltRep(int cobaltRep) {
		this.cobaltRep = cobaltRep;
	}

	public float getCobaltReportQuality() {
		return cobaltReportQuality;
	}

	public void setCobaltReportQuality(float cobaltReportQuality) {
		this.cobaltReportQuality = cobaltReportQuality;
	}
	
	
	
	
}
