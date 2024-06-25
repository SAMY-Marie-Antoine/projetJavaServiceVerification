package fr.formation.api;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.security.SecureRandom;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.apache.commons.validator.routines.RegexValidator;
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

	private static final String CHAR_LOWER = "abcdefghijklmnopqrstuvwxyz";
	private static final String CHAR_UPPER = CHAR_LOWER.toUpperCase();
	private static final String NUMBER = "0123456789";
	private static final String OTHER_CHAR = "!@#&()–[{}]:;',?/*~$^+=<>";
	private static final String PASSWORD_ALLOW_BASE = CHAR_LOWER + CHAR_UPPER + NUMBER + OTHER_CHAR;
	private static SecureRandom random = new SecureRandom();

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

			// Longueur minimale et maximale du mot de passe
    int minLength = 16;
    int maxLength = 128;
    int passwordLength = 20;  // Longueur par défaut

    // Ajustement de la longueur si elle est en dehors des limites définies
    passwordLength = Math.max(passwordLength, minLength);
    passwordLength = Math.min(passwordLength, maxLength);

    log.info("Début de la génération du mot de passe fort.");

    // Assurer que le mot de passe contient au moins une lettre minuscule, une lettre majuscule, un chiffre et un caractère spécial
    StringBuilder sb = new StringBuilder(passwordLength);
    sb.append(CHAR_LOWER.charAt(random.nextInt(CHAR_LOWER.length())));
    sb.append(CHAR_UPPER.charAt(random.nextInt(CHAR_UPPER.length())));
    sb.append(NUMBER.charAt(random.nextInt(NUMBER.length())));
    sb.append(OTHER_CHAR.charAt(random.nextInt(OTHER_CHAR.length())));

    // Chars disponibles pour le reste du mot de passe
    String passwordChars = CHAR_LOWER + CHAR_UPPER + NUMBER + OTHER_CHAR;
    for (int i = 4; i < passwordLength; i++) {
        // Générer un caractère aléatoire parmi les caractères disponibles
        int rndCharAt = random.nextInt(passwordChars.length());
        char rndChar = passwordChars.charAt(rndCharAt);
        sb.append(rndChar);
    }

    // Mélanger les caractères pour éviter que les 4 premiers caractères soient toujours dans le même ordre
    List<Character> passwordCharsList = sb.chars().mapToObj(c -> (char) c).collect(Collectors.toList());
    Collections.shuffle(passwordCharsList);
    String password = passwordCharsList.stream().map(String::valueOf).collect(Collectors.joining());

    log.info("Mot de passe généré : {}", password);
    return password;	
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
		//return motDePasse.matches("^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,}$");
		//motDePasse.matches("^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,}$");
		// Exemple de vérification de la force du mot de passe
		//boolean motdepasse= motDePasse.matches("^(?!.*\\\\s)(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#&()–[{}]:;',?/*~$^+=<>]).{8,20}$");

		//return motdepasse; 

		// Création des validateurs de regex
		RegexValidator hasUppercase = new RegexValidator(".*[A-Z].*");
		RegexValidator hasLowercase = new RegexValidator(".*[a-z].*");
		RegexValidator hasDigit = new RegexValidator(".*\\d.*");
		RegexValidator hasSpecialChar = new RegexValidator(".*[!@#&()–[{}]:;',?/*~$^+=<>].*");
		RegexValidator hasNoSpace = new RegexValidator("^(?!.*\\\\s).*");
		RegexValidator length = new RegexValidator(".{8,20}"); 

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
