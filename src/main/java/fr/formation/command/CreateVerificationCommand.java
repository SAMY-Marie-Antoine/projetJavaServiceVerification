package fr.formation.command;

public class CreateVerificationCommand {
	private String message;
	private String password;
	private boolean verificationPassword;
	private String verificationId;
	private String utilisateurId;

	public boolean isVerificationPassword() {
		return verificationPassword;
	}
	public void setVerificationPassword(boolean verificationPassword) {
		this.verificationPassword = verificationPassword;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}

	public String getVerificationId() {
		return verificationId;
	}
	public void setVerificationId(String verificationId) {
		this.verificationId = verificationId;
	}
	public String getUtilisateurId() {
		return utilisateurId;
	}
	public void setUtilisateurId(String utilisateurId) {
		this.utilisateurId = utilisateurId;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
