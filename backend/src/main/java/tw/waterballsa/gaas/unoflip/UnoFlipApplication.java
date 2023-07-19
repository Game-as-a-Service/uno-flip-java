package tw.waterballsa.gaas.unoflip;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(info = @Info(title = "Uno Flip API", version = "1.0.0"))
public class UnoFlipApplication {

	public static void main(String[] args) {
		SpringApplication.run(UnoFlipApplication.class, args);
	}

}
