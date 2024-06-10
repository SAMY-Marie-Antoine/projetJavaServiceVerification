package fr.formation.model;

import org.hibernate.annotations.UuidGenerator;

import fr.formation.enumerator.VerificationEtat;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

@Entity
@Table(name = "verification")
public class Verification {
	
	@Id
	@UuidGenerator
	private String id;

	@Column(name="emailverif")
	private String emailVerif;

	@Column(name="mot_de_passe")
	private String motDePasse;
	
	private int forceMotDePasse;
	
	@Enumerated(EnumType.STRING)
	private VerificationEtat etat;
	
	private String utilisateurId;
	

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getEmailVerif() {
		return emailVerif;
	}

	public void setEmailVerif(String emailVerif) {
		this.emailVerif = emailVerif;
	}

	public String getMotDePasse() {
		return motDePasse;
	}

	public void setMotDePasse(String motDePasse) {
		this.motDePasse = motDePasse;
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

	public int getForceMotDePasse() {
		return forceMotDePasse;
	}

	public void setForceMotDePasse(int forceMotDePasse) {
		this.forceMotDePasse = forceMotDePasse;
	}
	
	
}
