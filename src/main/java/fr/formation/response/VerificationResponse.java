package fr.formation.response;

public class VerificationResponse {

	private String id;
	private String emailVerif;
	private String motDePasseVerif;
	private int forceMotDePasse;


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

	public String getEmailVerif() {
		return emailVerif;
	}
	public void setEmailVerif(String emailVerif) {
		this.emailVerif = emailVerif;
	}
	public String getMotDePasseVerif() {
		return motDePasseVerif;
	}
	public void setMotDePasseVerif(String motDePasseVerif) {
		this.motDePasseVerif = motDePasseVerif;
	}
	public int getForceMotDePasse() {
		return forceMotDePasse;
	}
	public void setForceMotDePasse(int forceMotDePasse) {
		this.forceMotDePasse = forceMotDePasse;
	}

}
