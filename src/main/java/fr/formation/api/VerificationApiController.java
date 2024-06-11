package fr.formation.api;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import fr.formation.enumerator.VerificationEtat;
import fr.formation.feignclient.PrincipalFeignClient;
import fr.formation.model.Verification;
import fr.formation.repository.VerificationRepository;
import fr.formation.request.VerificationRequest;
import fr.formation.response.VerificationResponse;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/verification")
@CrossOrigin("*")
public class VerificationApiController {

	private static final Logger log = LoggerFactory.getLogger(VerificationApiController.class);

	//@Autowired
	private final VerificationRepository verificationRepository;



	public VerificationApiController(VerificationRepository verificationRepository) {
		this.verificationRepository = verificationRepository;
		log.info("Initialisation de VerificationApiController");
	}



	//Vérification de la force d'un mot de passe

	@PostMapping("/mot-de-passe/force/{motDePasse}")
	private boolean getForceMotDePasse(@Valid @PathVariable("motDePasse") String motDePasse) {

		log.info("Vérification de la force du mot de passe.");
		boolean isStrong = isForceMotDePasse(motDePasse);
		log.info("Résultat de la vérification de la force du mot de passe : {}", isStrong);
		return isStrong;
	}


	// Méthode pour vérifier si un mot de passe est fort
	private boolean isForceMotDePasse(String motDePasse) {
		// Exemple de vérification de la force du mot de passe
		return motDePasse.length() > 8 && motDePasse.matches(".*\\d.*") && motDePasse.matches(".*[a-z].*") && motDePasse.matches(".*[A-Z].*");
	}

	//Vérification mot de passe compromis

	@PostMapping("/mot-de-passe/vulnerable/{motDePasse}")
	private boolean  getMotDePasseVulnerable(@Valid @PathVariable String motDePasse) {

		log.info("Exécution de la méthode getMotDePasseVulnerableById pour le mot de passe: {}", motDePasse);

		log.info("Vérification si le mot de passe est compromis.");
		boolean isCompromis = isPasswordCompromis(motDePasse);
		log.info("Résultat de la vérification du mot de passe compromis : {}", isCompromis);
		return isCompromis;

	}


	// Méthode pour vérifier si un mot de passe est compromis
	private boolean isPasswordCompromis(String motDePasse) {
		// Exemple de vérification contre une liste de mots de passe compromis
		// Ici, il faudrait comparer avec les mots de passe hachés en SHA-1 stockés dans les fichiers TXT
		return false; // À implémenter
	}


	// Génération d'un mot de passe fort
	@GetMapping("/generateMotDePasseFort")
	public String generateMotDePasseFort() {
		log.info("Génération d'un mot de passe fort.");
		String motDePasseFort = generateStrongPassword();
		log.info("Mot de passe fort généré : {}", motDePasseFort);
		return motDePasseFort;
	}

	// Méthode pour générer un mot de passe fort
	private String generateStrongPassword() {
	// Exemple de génération de mot de passe fort
	return new BCryptPasswordEncoder().encode("StrongP@ssw0rd!");
	}
	
	
	@PutMapping("/{id}")
	@ResponseStatus(HttpStatus.CREATED)
	public String update(@Valid @PathVariable("id") String id,@RequestBody VerificationResponse request) {

		log.info("Exécution de la méthode update avec l'id: " + id);

		Verification verificationbdd=this.verificationRepository.findById(id).get();
		Verification verification = new Verification();
		BeanUtils.copyProperties(request, verificationbdd);

		this.verificationRepository.save(verificationbdd);

		log.info("La méthode update a été exécutée avec succès");
		return verification.getId();
	}

	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.CREATED)
	public String delete(@Valid @PathVariable("id") String id,@RequestBody VerificationRequest request) {

		log.info("Exécution de la méthode delete avec l'id: " + id);

		Optional<Verification> verificationbdd=this.verificationRepository.findById(id);
		Verification verification = new Verification();

		if (verificationbdd.isEmpty()) {
			log.error("verification non trouvée avec l'id : {}", id);
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Id verification inexistant");
		}

		BeanUtils.copyProperties(request, verificationbdd);

		this.verificationRepository.deleteById(id);

		log.info("La méthode delete a été exécutée avec succès");
		return verification.getId();
	}



}
