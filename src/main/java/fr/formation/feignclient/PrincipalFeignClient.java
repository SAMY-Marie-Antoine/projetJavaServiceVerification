package fr.formation.feignclient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import fr.formation.enumerator.VerificationEtat;

// @FeignClient(value = "commentaire-service", url = "http://localhost:8082", path = "/api/commentaire")
// @FeignClient(value = "commentaire-service", path = "/api/commentaire")
@FeignClient(value = "projetJavaServicePrincipal", path = "/api/principal", fallback = PrincipalFeignClient.Fallback.class)
public interface PrincipalFeignClient {
    
    @GetMapping("/{id}")
    public String getMotDePasseById(@PathVariable String id);
    
    @GetMapping("/{email}")
    public String getMotDePasseByEmail(@PathVariable String email);
    
    @GetMapping("/mot-de-passe/vulnerable/{motDePasse}")
    VerificationEtat getMotDePasseVulnerableById(@PathVariable("motDePasse") VerificationEtat verificationEtat);

    @GetMapping("/mot-de-passe/force/{motDePasse}")
    boolean getForceMotDePasse(@PathVariable("motDePasse") int i);
    
    @Component
    public static class Fallback implements PrincipalFeignClient {
        
        @Override
        public String getMotDePasseById(String id) {
            return id;
            //return "Service principal indisponible";
        }

		@Override
		public String getMotDePasseByEmail(String email) {
			// TODO Auto-generated method stub
			return email;
		}

	
		@Override
		public VerificationEtat getMotDePasseVulnerableById(VerificationEtat verificationEtat) {
			// TODO Auto-generated method stub
			return verificationEtat;
		}

		@Override
		public boolean getForceMotDePasse(int i) {
			// TODO Auto-generated method stub
			return true;
		}
    }
}
