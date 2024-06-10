package fr.formation.request;

public class VerificationRequest {

	private String emailVerif;
	
	private String motDePasseVerif;
	
	private int forceMotDePasse;
	

	
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
