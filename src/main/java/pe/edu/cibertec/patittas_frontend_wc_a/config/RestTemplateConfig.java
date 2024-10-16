package pe.edu.cibertec.patittas_frontend_wc_a.config;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

@Configuration
public class RestTemplateConfig {

    @Bean
    public RestTemplate restTemplateAutenticacion(RestTemplateBuilder builder) {
        return builder
                .rootUri("http://localhost:8090/autenticacion")
                .setConnectTimeout(Duration.ofSeconds(5)) //
                .setReadTimeout(Duration.ofSeconds(10)) //
                .build();
    }

    @Bean
    public RestTemplate restTemplateFinanzas(RestTemplateBuilder builder) {
        return builder
                .rootUri("http://localhost:8090/finanzas")
                .setReadTimeout(Duration.ofSeconds(30))
                .build();
    }

    @Bean
    public RestTemplate restTemplateReporteria(RestTemplateBuilder builder) {
        return builder
                .rootUri("http://localhost:8090/reporteria")
                .setReadTimeout(Duration.ofSeconds(60))
                .build();
    }
}
