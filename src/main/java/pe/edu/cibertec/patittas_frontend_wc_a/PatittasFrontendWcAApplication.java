package pe.edu.cibertec.patittas_frontend_wc_a;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class PatittasFrontendWcAApplication {

	public static void main(String[] args) {
		SpringApplication.run(PatittasFrontendWcAApplication.class, args);
	}

}
