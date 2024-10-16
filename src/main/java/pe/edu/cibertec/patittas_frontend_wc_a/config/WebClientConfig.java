package pe.edu.cibertec.patittas_frontend_wc_a.config;

import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

@Configuration
public class WebClientConfig {

    HttpClient httpClient = HttpClient.create()
            .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 1000)
            .responseTimeout(Duration.ofSeconds(10))
            .doOnConnected(conn -> conn.addHandlerLast(new ReadTimeoutHandler(10, TimeUnit.SECONDS))); //timeout para recepcion de cada paquete

    @Bean
    public WebClient webClientAutenticacion(WebClient.Builder builder) {
        return builder
                .baseUrl("http://localhost:8090/autenticacion")
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .build();
    }
}
