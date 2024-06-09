package fr.formation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableFeignClients // Activer les clients Feign
public class ProjetJavaServiceVerificationApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProjetJavaServiceVerificationApplication.class, args);
	}

}
