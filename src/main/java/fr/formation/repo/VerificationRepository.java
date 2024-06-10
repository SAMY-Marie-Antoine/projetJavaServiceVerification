package fr.formation.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import fr.formation.enumerator.VerificationEtat;
import fr.formation.model.Verification;

public interface VerificationRepository extends JpaRepository<Verification, String> {
    //public Verification findByEtat(VerificationEtat etat);
    public Verification findByMotDePasse(String motDePasse);
    //public Verification findByEmail(String email);
    //public Optional<Verification> findById(String utilisateurId);
   //public Verification findByIdAndEtat(String utilisateurId, VerificationEtat etat);

    
}
