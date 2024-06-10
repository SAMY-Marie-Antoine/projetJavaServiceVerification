package fr.formation.api;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import fr.formation.feignclient.PrincipalFeignClient;
import fr.formation.model.Verification;
import fr.formation.repo.VerificationRepository;
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

	@Autowired
	private PrincipalFeignClient principalFeignClient;

	public VerificationApiController(VerificationRepository verificationRepository) {
		this.verificationRepository = verificationRepository;
		log.info("Initialisation de VerificationApiController");
	}
	
	
	@GetMapping
	public List<VerificationResponse> findAll() {

		log.info("Exécution de la méthode findAll");

		List<Verification> verifications = this.verificationRepository.findAll();
		List<VerificationResponse> response = new ArrayList<>();

		for (Verification verification : verifications) {
			VerificationResponse verificationResponse = new VerificationResponse();

			BeanUtils.copyProperties(verification, verificationResponse);

			response.add(verificationResponse);

			String mdp = this.principalFeignClient.getMotDePasseById(verification.getMotDePasse());

			if (mdp != null) {
				verificationResponse.setMotDePasse(mdp);
			}
		}

		log.info("La méthode findAll a été exécutée avec succès");
		return response;
	}


	@GetMapping("/mot-de-passe/vulnerable/{motDePasse}")
	public String getMotDePasseVulnerableById(@Valid @PathVariable String motDePasse) {

		log.info("Exécution de la méthode findByEmail avec l'email: " + motDePasse);		

		Optional<Verification> optVerification = Optional.of(this.verificationRepository.findByMotDePasse(motDePasse));

		if (optVerification.isPresent() && optVerification.get().getMotDePasse().length() >= motDePasse.length()) {

			log.info("La méthode findByEmail a été exécutée avec succès");
			return this.principalFeignClient.getMotDePasseVulnerableById(optVerification.get().getMotDePasse());

		}

		if(optVerification.isPresent() && optVerification.get().getMotDePasse().length() > motDePasse.length() ) {
			log.warn("Mode passe non trouvé dans la méthode findByEmail avec l'id: " + motDePasse);
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Le mot de passe est vulnérable");
		}
		log.warn("Mode passe non trouvé dans la méthode findByEmail avec l'id: " + motDePasse);
		return "- Le mot de passe est incohérent -";
	}

	
	@GetMapping("/mot-de-passe/force/{motDePasse}")
	public int getForceMotDePasse(@Valid @PathVariable("motDePasse") String motDePasse) {

		log.info("Exécution de la méthode findByEmail avec l'email: " + motDePasse);		

		Optional<Verification> optVerification = Optional.of(this.verificationRepository.findByMotDePasse(motDePasse));

		if (optVerification.isPresent() && optVerification.get().getMotDePasse().length() >= motDePasse.length()) {

			log.info("La méthode findByEmail a été exécutée avec succès");
			return this.principalFeignClient.getForceMotDePasse(optVerification.get().getMotDePasse());

		}

		if(optVerification.get().getMotDePasse().length() > motDePasse.length() ) {
			log.warn("Mode passe non trouvé dans la méthode findByEmail avec l'id: " + motDePasse);
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Le mot de passe est vulnérable");
		}
		log.warn("Mode passe non trouvé dans la méthode findByEmail avec l'id: " + motDePasse);
		return 0;
	}
	
	
	
	@GetMapping("/{id}")
	public String findById(@Valid @PathVariable("id") String id) {

		log.info("Exécution de la méthode findByEmail avec l'Id: " + id);		

		Optional<Verification> optVerification = this.verificationRepository.findById(id);

		if (optVerification.isPresent()) {

			log.info("La méthode findById a été exécutée avec succès");
			return this.principalFeignClient.getMotDePasseById(optVerification.get().getMotDePasse());

		}

		log.warn("Utilisateur non trouvé dans la méthode findByEmail avec l'id: " + id);
		return "- Utilisateur non trouvé -";
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
		BeanUtils.copyProperties(request, verificationbdd);

		this.verificationRepository.deleteById(id);

		log.info("La méthode delete a été exécutée avec succès");
		return verification.getId();
	}



}
