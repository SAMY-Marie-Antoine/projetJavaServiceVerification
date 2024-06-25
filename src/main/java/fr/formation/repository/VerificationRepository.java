package fr.formation.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import fr.formation.enumerator.VerificationEtat;
import fr.formation.model.Verification;

public interface VerificationRepository extends JpaRepository<Verification, String> {
    public Verification findByEtat(VerificationEtat etat);
	 //Optional<Verification> findByMotDePasse(String motDePasse);
    public Verification findByMotDePasse(String motDePasse);
    //public Verification findByEmail(String email);
     //Optional<Verification> findById(String utilisateurId);
   //public Verification findByIdAndEtat(String utilisateurId, VerificationEtat etat);

    
}
