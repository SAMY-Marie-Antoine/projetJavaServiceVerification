package fr.formation.response;

public class VerificationResponse {
  
	private String id;
	private String emailVerif;
	private String motDePasseVerif;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	public String getEmail() {
		return emailVerif;
	}
	public void setEmail(String emailVerif) {
		this.emailVerif = emailVerif;
	}
	public String getMotDePasse() {
		return motDePasseVerif;
	}
	public void setMotDePasse(String motDePasseVerif) {
		this.motDePasseVerif = motDePasseVerif;
	}
	

    
}
