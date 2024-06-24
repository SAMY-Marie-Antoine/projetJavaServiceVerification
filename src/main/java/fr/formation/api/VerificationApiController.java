package fr.formation.api;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fr.formation.repository.VerificationRepository;
import fr.formation.service.HashingService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/verification")
@CrossOrigin("*")
public class VerificationApiController {

	private static final Logger log = LoggerFactory.getLogger(VerificationApiController.class);

	//@Autowired
	private final VerificationRepository verificationRepository;

	private final HashingService hashingService;


	public VerificationApiController(VerificationRepository verificationRepository, HashingService hashingService) {
		this.verificationRepository = verificationRepository;
		this.hashingService = hashingService;
		log.info("Initialisation de VerificationApiController");
	}


	// Génération d'un mot de passe fort
	@PostMapping("/generateMotDePasseFort")
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


	//Vérification de la force d'un mot de passe
	@PostMapping("/mot-de-passe/force")
	private boolean getForceMotDePasse(@Valid @RequestBody String motDePasse) {

		log.info("Vérification de la force du mot de passe.");
		boolean isStrong = isForceMotDePasse(motDePasse);
		log.info("Résultat de la vérification de la force du mot de passe : {}", isStrong);
		return isStrong;
	}


	// Méthode pour vérifier si un mot de passe est fort
	private boolean isForceMotDePasse(String motDePasse) {
		return motDePasse.matches("^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,}$");
	}

	//Vérification mot de passe compromis
	@PostMapping("/mot-de-passe/compromis")
	private boolean  getMotDePasseCompromis(@Valid @RequestBody String motDePasse) throws InvalidKeyException, NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, BadPaddingException, IllegalBlockSizeException {

		log.info("Exécution de la méthode getMotDePasseCompromis pour le mot de passe: {}", motDePasse);

		log.info("Vérification si le mot de passe est compromis.");
		boolean isCompromis = isPasswordCompromis(motDePasse);
		log.info("Résultat de la vérification du mot de passe compromis : {}", isCompromis);

		return isCompromis;

	}


	// Méthode pour vérifier si un mot de passe est compromis
	private boolean isPasswordCompromis(String motDePasse) throws InvalidKeyException, NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, BadPaddingException, IllegalBlockSizeException {
		// Exemple de vérification contre une liste de mots de passe compromis
		// le mot de passe claire doit etre convertir en SHA-1 pour être comparé aux fichiers
		// Ici, il faudrait comparer avec les mots de passe hachés en SHA-1 stockés dans les fichiers TXT
		//return false; // À implémenter


		String hashedPassword = hashingService.hashWithSHA1(motDePasse);
		log.info("Mot de passe haché en SHA-1 : {}", hashedPassword);

		try (Connection connection = DriverManager.getConnection("jdbc:clickhouse://127.0.0.1:8123/contenu",

				"default", "")) {
			connection.setAutoCommit(false);

			try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM contenu WHERE mot LIKE ?")){

				statement.setString(1,hashedPassword+"%");
				ResultSet rs = statement.executeQuery();
				while(rs.next()){

					System.out.println("mot = " +rs.getString("mot"));
					if(hashedPassword.equals(rs.getString("mot")))
					{
						return true;

					}
					else {
						return false;
					}

				}
			}
			catch(Exception ex){
				log.error("Problème avec la requête ...");
			}

		}

		catch (Exception ex) {
			log.error("Impossible de se connecter ...");
		}

		
		return false;

	}

}
