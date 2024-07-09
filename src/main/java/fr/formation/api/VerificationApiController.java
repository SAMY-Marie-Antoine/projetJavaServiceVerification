package fr.formation.api;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDateTime;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.apache.commons.validator.routines.RegexValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fr.formation.command.CreateVerificationCommand;
import fr.formation.repository.VerificationRepository;
import fr.formation.service.HashingService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/verification")
@CrossOrigin("*")
public class VerificationApiController {

	private static final Logger log = LoggerFactory.getLogger(VerificationApiController.class);

	private static final String CHAR_LOWER = "abcdefghijklmnopqrstuvwxyz";
	private static final String CHAR_UPPER = CHAR_LOWER.toUpperCase();
	private static final String NUMBER = "0123456789";
	private static final String OTHER_CHAR = "!@#&()–[{}]:;',?/*~$^+=<>";
	private static final String PASSWORD_ALLOW_BASE = CHAR_LOWER + CHAR_UPPER + NUMBER + OTHER_CHAR;
	private static SecureRandom random = new SecureRandom();
	
	//@Autowired
	private final VerificationRepository verificationRepository;

	private final HashingService hashingService;
	
	CreateVerificationCommand command = new CreateVerificationCommand();

	@Autowired
	private StreamBridge streamBridge;
	
	public VerificationApiController(VerificationRepository verificationRepository, HashingService hashingService) {
		this.verificationRepository = verificationRepository;
		this.hashingService = hashingService;
		log.info("Initialisation de VerificationApiController");
	}


	// Génération d'un mot de passe fort
	@PostMapping("/generateMotDePasseFort")
	public String generateMotDePasseFort() {
		log.info("Début de la génération du mot de passe fort.");
		
		int passwordLength = 20; // Longueur du mot de passe
		StringBuilder password = new StringBuilder(passwordLength);

		// Ajouter au moins un caractère de chaque type
		password.append(CHAR_LOWER.charAt(random.nextInt(CHAR_LOWER.length())));
		password.append(CHAR_UPPER.charAt(random.nextInt(CHAR_UPPER.length())));
		password.append(NUMBER.charAt(random.nextInt(NUMBER.length())));
		password.append(OTHER_CHAR.charAt(random.nextInt(OTHER_CHAR.length())));

		// Compléter le reste du mot de passe avec des caractères aléatoires
		for (int i = 4; i < passwordLength; i++) {
			String charSet = PASSWORD_ALLOW_BASE;
			// Assurer qu'au moins un chiffre est inclus dans le mot de passe
			if (!password.toString().matches(".*\\d.*")) {
				charSet = NUMBER;
			}
			password.append(charSet.charAt(random.nextInt(charSet.length())));
		}
	

		log.info("Mot de passe généré : {}", password);
		// Préparation du message
		command.setMessage("Mot de passe généré: ->");
		command.setPassword(password.toString());
		command.setTimestamp(LocalDateTime.now());

		log.debug("Mot de passe généré : {}", password, (this.streamBridge.send("verification.validated",command)));
		
		return password.toString();
	}

	

	//Vérification de la force d'un mot de passe
	@PostMapping("/mot-de-passe/force")
	private boolean getForceMotDePasse(@Valid @RequestBody String motDePasse) {

		log.info("Vérification de la force du mot de passe.");
		boolean isStrong = isForceMotDePasse(motDePasse);
		log.info("Résultat de la vérification de la force du mot de passe : {}", isStrong);
		
		command.setMessage("Vérification de la force du mot de passe: ->");
		command.setVerificationPassword(isStrong);
		command.setTimestamp(LocalDateTime.now());
		
		if(isStrong) {
			log.debug("Vérification de la force du mot de passe : {}",isStrong, this.streamBridge.send("verification.validated",command));
		}else {
			log.debug("Vérification de la force du mot de passe. : {}",isStrong, this.streamBridge.send("verification.rejected",command));
		}
		return isStrong;
	}


	// Méthode pour vérifier si un mot de passe est fort
	private boolean isForceMotDePasse(String motDePasse) {

		// Création des validateurs de regex
		RegexValidator hasUppercase = new RegexValidator(".*[A-Z].*");
		RegexValidator hasLowercase = new RegexValidator(".*[a-z].*");
		RegexValidator hasDigit = new RegexValidator(".*\\d.*");
		RegexValidator hasSpecialChar = new RegexValidator(".*[!@#&()–[{}]:;',?/*~$^+=<>].*");
		RegexValidator hasNoSpace = new RegexValidator("^(?!.*\\\\s).*");
		RegexValidator length = new RegexValidator(".{8,}"); 

		// Vérification de la force du mot de passe
		return hasUppercase.isValid(motDePasse) && hasLowercase.isValid(motDePasse) && hasDigit.isValid(motDePasse) && hasSpecialChar.isValid(motDePasse) && hasNoSpace.isValid(motDePasse) && length.isValid(motDePasse);	
 
	}

	//Vérification mot de passe compromis
	@PostMapping("/mot-de-passe/compromis")
	private boolean  getMotDePasseCompromis(@Valid @RequestBody String motDePasse) throws InvalidKeyException, NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, BadPaddingException, IllegalBlockSizeException {

		log.info("Exécution de la méthode getMotDePasseCompromis pour le mot de passe: {}", motDePasse);

		log.info("Vérification si le mot de passe est compromis.");
		boolean isCompromis = isPasswordCompromis(motDePasse);
		log.info("Résultat de la vérification du mot de passe compromis : {}", isCompromis);
		
		command.setMessage("Vérification si le mot de passe est compromis: ->");
		command.setVerificationPassword(isCompromis);
		command.setTimestamp(LocalDateTime.now());
		
		if(isCompromis) {
			log.debug("Vérification si le mot de passe est compromis : {}",isCompromis, this.streamBridge.send("verification.rejected",command));
		}else {
			log.debug("Vérification si le mot de passe est compromis : {}",isCompromis, this.streamBridge.send("verification.validated",command));
		}

		return isCompromis;
	}

	// Méthode pour vérifier si un mot de passe est compromis
	private boolean isPasswordCompromis(String motDePasse) throws InvalidKeyException, NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, BadPaddingException, IllegalBlockSizeException {
		// Exemple de vérification contre une liste de mots de passe compromis
		// le mot de passe claire doit etre convertir en SHA-1 pour être comparé aux fichiers
		// Ici, il faudrait comparer avec les mots de passe hachés en SHA-1 stockés dans les fichiers TXT
		
		String hashedPassword = hashingService.hashWithSHA1(motDePasse);
		log.info("Mot de passe haché en SHA-1 : {}", hashedPassword);

		try (Connection connection = DriverManager.getConnection("jdbc:clickhouse://127.0.0.1:8123/contenu",

				"default", "")) {
			connection.setAutoCommit(false);
			try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM contenu WHERE mot LIKE ?")){

				statement.setString(1,hashedPassword+"%");
				log.info("Récupération des données de la base de données ...");
				ResultSet rs = statement.executeQuery();
				while(rs.next()){

					System.out.println("mot = " +rs.getString("mot"));
					if(hashedPassword.equals(rs.getString("mot")))
					{
						log.info("Le mot de passe est compromis.");
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
