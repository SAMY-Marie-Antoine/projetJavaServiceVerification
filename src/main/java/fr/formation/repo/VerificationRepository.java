package fr.formation.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import fr.formation.enumerator.VerificationEtat;
import fr.formation.model.Verification;

public interface VerificationRepository extends JpaRepository<Verification, String> {
    public List<Verification> findAllByEtat(VerificationEtat etat);

    public List<Verification> findAllByUtilisateurtId(String utilisateurId);
    
    public List<Verification> findAllByUtilisateurIdAndEtat(String utilisateurId, VerificationEtat etat);

    
}
