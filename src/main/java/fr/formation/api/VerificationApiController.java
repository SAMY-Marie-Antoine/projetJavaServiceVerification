package fr.formation.api;

import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import fr.formation.command.CreateVerificationCommand;
import fr.formation.enumerator.CommentaireEtat;
import fr.formation.enumerator.VerificationEtat;
import fr.formation.model.Commentaire;
import fr.formation.model.Verification;
import fr.formation.repo.VerificationRepository;
import fr.formation.request.VerificationRequest;
import fr.formation.response.VerificationResponse;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/verification")
@CrossOrigin("*")
public class VerificationApiController {
    @Autowired
    private VerificationRepository repository;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private CircuitBreakerFactory circuitBreakerFactory;

    @Autowired
    private StreamBridge streamBridge;

    @GetMapping
    public List<VerificationResponse> findAll() {
        return this.repository.findAllByEtat(VerificationEtat.OK).stream()
            // .map(commentaire -> this.map(commentaire))
            .map(this::map)
            .toList();
    }

    
    @GetMapping("/{utilisateurId}")
    public String getNoteByProduitId(@Valid @PathVariable String utilisateurId) {
        // Calcul de la note moyenne - V1
        List<Verification> verifications = this.repository.findAllByUtilisateurIdAndEtat(utilisateurId, VerificationEtat.OK);
        String resultat ;
        
               resultat = this.repository.findAllByUtilisateurtId(utilisateurId).toString();
           
        
        return resultat;
    }
    
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public String create(@RequestBody VerificationRequest request) {
        // Boolean isNotable = this.restTemplate
        //     .getForObject("http://localhost:8081/api/produit/" + request.getProduitId() + "/is-notable", Boolean.class);
        // Boolean isNotable = this.restTemplate
        //     .getForObject("lb://produit-service/api/produit/" + request.getProduitId() + "/is-notable", Boolean.class);
        
        // Boolean isNotable = this.circuitBreakerFactory.create("produitService").run(
        //     () -> this.restTemplate
        //                 .getForObject("lb://produit-service/api/produit/" + request.getProduitId() + "/is-notable", Boolean.class)
        //     ,
        //     ex -> false
        // );

        // if (isNotable == null || !isNotable) {
        //     // Pas la peine d'aller plus loin
        //     throw new ProduitNotFoundOrNotNotableException();
        // }

        Verification verification = new Verification();
        CreateVerificationCommand command = new CreateVerificationCommand();

        BeanUtils.copyProperties(request, verification);

        verification.setEtat(VerificationEtat.ATTENTE);

        this.repository.save(verification);

        command.setVerificationId(verification.getId());
        command.setUtilisateurId(verification.getId());

        this.streamBridge.send("verification.created", command);

        return verification.getId();
    }

    private VerificationResponse map(Verification verification) {
        VerificationResponse response = new VerificationResponse();

        BeanUtils.copyProperties(verification, response);

        // String name = this.restTemplate
        //     .getForObject("http://localhost:8081/api/produit/" + commentaire.getProduitId() + "/name", String.class);
        // String name = this.restTemplate
        //     .getForObject("lb://produit-service/api/produit/" + commentaire.getProduitId() + "/name", String.class);

        String name = this.circuitBreakerFactory.create("UtilisateurService").run(
            () -> this.restTemplate.getForObject("lb://projetJavaServicePrincipal/api/utilisateur/" + verification.getId() + "/name", String.class)
            ,
            t -> "- no name -"
        );

        response.setId(name);

        return response;
    }
}
