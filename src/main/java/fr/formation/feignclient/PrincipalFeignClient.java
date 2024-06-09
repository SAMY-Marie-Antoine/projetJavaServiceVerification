package fr.formation.feignclient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

// @FeignClient(value = "commentaire-service", url = "http://localhost:8082", path = "/api/commentaire")
// @FeignClient(value = "commentaire-service", path = "/api/commentaire")
@FeignClient(value = "projetJavaPrincipalVerification", path = "/api/principal", fallback = PrincipalFeignClient.Fallback.class)
public interface PrincipalFeignClient {
    
    @GetMapping("/{id}")
    public String getMotDePasseById(@PathVariable String id);

    @Component
    public static class Fallback implements PrincipalFeignClient {
        
        @Override
        public String getMotDePasseById(String id) {
            return null;
            //return "Service principal indisponible";
        }
    }
}
