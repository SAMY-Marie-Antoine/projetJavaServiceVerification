package fr.formation.request;

public class VerificationRequest {

	private String email;
	private String motDePasse;
	private int forceMotDePasse;
	private boolean motDePasseCompromis;
	

	public boolean isMotDePasseCompromis() {
		return motDePasseCompromis;
	}

	public void setMotDePasseCompromis(boolean motDePasseCompromis) {
		this.motDePasseCompromis = motDePasseCompromis;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getMotDePasse() {
		return motDePasse;
	}

	public void setMotDePasse(String motDePasse) {
		this.motDePasse = motDePasse;
	}

	public int getForceMotDePasse() {
		return forceMotDePasse;
	}

	public void setForceMotDePasse(int forceMotDePasse) {
		this.forceMotDePasse = forceMotDePasse;
	}
	

	
	
	
}
