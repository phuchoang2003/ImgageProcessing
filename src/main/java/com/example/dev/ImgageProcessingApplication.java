package com.example.dev;

//import io.swagger.v3.oas.annotations.OpenAPIDefinition;
//import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(info = @Info(
		title = "My API Documentation",
		version = "1.0",
		description = "This is a secured API with JWT authentication"
))
public class ImgageProcessingApplication {

	public static void main(String[] args) {
		SpringApplication.run(ImgageProcessingApplication.class, args);
	}

}
