package fr.formation.model;

import org.hibernate.annotations.UuidGenerator;

import fr.formation.enumerator.VerificationEtat;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;


@Entity
@Table(name = "verification")
public class Verification {
	
	@Id
	@UuidGenerator
	private String id;

	@Column(name="email")
	private String email;

	@Column(name="mot_de_passe", length = 512)
	private String motDePasse;
	
	private boolean forceMotDePasse;
	
	private boolean motDePasseCompromis;
	
	@Enumerated(EnumType.STRING)
	private VerificationEtat etat;
	
	private String utilisateurId;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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

	public boolean isForceMotDePasse() {
		return forceMotDePasse;
	}

	public void setForceMotDePasse(boolean forceMotDePasse) {
		this.forceMotDePasse = forceMotDePasse;
	}

	public boolean isMotDePasseCompromis() {
		return motDePasseCompromis;
	}

	public void setMotDePasseCompromis(boolean motDePasseCompromis) {
		this.motDePasseCompromis = motDePasseCompromis;
	}

	public VerificationEtat getEtat() {
		return etat;
	}

	public void setEtat(VerificationEtat etat) {
		this.etat = etat;
	}

	public String getUtilisateurId() {
		return utilisateurId;
	}

	public void setUtilisateurId(String utilisateurId) {
		this.utilisateurId = utilisateurId;
	}
	

	
	
}
