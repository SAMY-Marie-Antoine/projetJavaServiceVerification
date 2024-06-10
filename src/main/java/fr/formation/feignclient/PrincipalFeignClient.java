package fr.formation.feignclient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

// @FeignClient(value = "commentaire-service", url = "http://localhost:8082", path = "/api/commentaire")
// @FeignClient(value = "commentaire-service", path = "/api/commentaire")
@FeignClient(value = "projetJavaServicePrincipal", path = "/api/principal", fallback = PrincipalFeignClient.Fallback.class)
public interface PrincipalFeignClient {
    
    @GetMapping("/{id}")
    public String getMotDePasseById(@PathVariable String id);
    
    @GetMapping("/{email}")
    public String getMotDePasseByEmail(@PathVariable String email);
    
    @GetMapping("/mot-de-passe/vulnerable/{motDePasse}")
    String getMotDePasseVulnerableById(@PathVariable("motDePasse") String motDePasse);

    @GetMapping("/mot-de-passe/force")
    int getForceMotDePasse(@RequestParam("motDePasse") String motDePasse);
    
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
		public String getMotDePasseVulnerableById(String motDePasse) {
			// TODO Auto-generated method stub
			return motDePasse;
		}

		@Override
		public int getForceMotDePasse(String motDePasse) {
			// TODO Auto-generated method stub
			return 0;
		}
    }
}
