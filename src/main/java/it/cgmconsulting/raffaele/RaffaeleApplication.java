package it.cgmconsulting.raffaele;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(info = @Info(title = "raffaele API", version = "3.0", description = "raffaele"))
public class RaffaeleApplication {

	public static void main(String[] args) {
		SpringApplication.run(RaffaeleApplication.class, args);
	}

}
