package fr.formation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@EnableFeignClients // Activer les clients Feign
public class ProjetJavaServiceVerificationApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProjetJavaServiceVerificationApplication.class, args);
	}
	  
}
