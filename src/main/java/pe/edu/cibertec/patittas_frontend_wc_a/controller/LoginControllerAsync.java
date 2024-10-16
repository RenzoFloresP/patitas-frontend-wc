package pe.edu.cibertec.patittas_frontend_wc_a.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import pe.edu.cibertec.patittas_frontend_wc_a.client.LogoutClient;
import pe.edu.cibertec.patittas_frontend_wc_a.dto.LoginRequestDTO;
import pe.edu.cibertec.patittas_frontend_wc_a.dto.LoginResponseDTO;
import pe.edu.cibertec.patittas_frontend_wc_a.dto.LogoutRequestDTO;
import pe.edu.cibertec.patittas_frontend_wc_a.dto.LogoutResponseDTO;
import reactor.core.publisher.Mono;


@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/login")
public class LoginControllerAsync {

    @Autowired
    WebClient webClientAutenticacion;

    @Autowired
    LogoutClient logoutClient;

    @PostMapping("/autenticar-async")
    public Mono<LoginResponseDTO> autenticar(@RequestBody LoginRequestDTO loginRequestDTO) {

        //validar
        if (loginRequestDTO.tipoDocumento() == null || loginRequestDTO.tipoDocumento().trim().length() == 0 ||
                loginRequestDTO.numeroDocumento() == null || loginRequestDTO.numeroDocumento().trim().length() == 0 ||
                loginRequestDTO.password() == null || loginRequestDTO.password().trim().length() == 0) {
            return Mono.just(new LoginResponseDTO("99", "Ingrese nuevamente sus datos", "", ""));
        }

        try {
            //consumir servicio de autenticacion
            return webClientAutenticacion.post()
                    .uri("/login")
                    .body(Mono.just(loginRequestDTO), LoginRequestDTO.class)
                    .retrieve()
                    .bodyToMono(LoginResponseDTO.class)
                    .flatMap(response -> {
                        if (response.codigo().equals("00")) {
                            System.out.println(response.nombreUsuario());
                            return Mono.just(new LoginResponseDTO("00", "", response.nombreUsuario(), ""));
                        } else {
                            return Mono.just(new LoginResponseDTO("02", "Error al autenticar sus credenciales", "", ""));
                        }
                    });
        } catch (Exception e) {
            return Mono.just(new LoginResponseDTO("99", "Fail: " + e.getMessage(), "", ""));
        }
    }

    @PostMapping("/logout-async")
    public Mono<LogoutResponseDTO> salir(@RequestBody LogoutRequestDTO logoutRequestDTO) {
        System.out.println(logoutRequestDTO.nombreUsuario());

        if (logoutRequestDTO == null) {
            return Mono.just(new LogoutResponseDTO("99", "Error: No se pudo cerrar la sesion"));
        }

        try {
            return webClientAutenticacion.post()
                    .uri("/logout")
                    .body(Mono.just(logoutRequestDTO), LogoutRequestDTO.class)
                    .retrieve()
                    .bodyToMono(LogoutResponseDTO.class)
                    .flatMap(response -> {
                        if (response.codigo().equals("00")) {
                            return Mono.just(new LogoutResponseDTO("00", ""));
                        } else {
                            return Mono.just(new LogoutResponseDTO("02", "Error al finalizar sesion"));
                        }
                    });
        } catch (Exception e) {
            return Mono.just(new LogoutResponseDTO("99", "Error: " + e.getMessage()));
        }
    }

    @PostMapping("/logout-async-feign")
    public Mono<LogoutResponseDTO> salirFeignClient(@RequestBody LogoutRequestDTO logoutRequestDTO) {
        System.out.println("Consuming with FeignClient!");

        if (logoutRequestDTO == null) {
            return Mono.just(new LogoutResponseDTO("99", "Dato no enviado"));
        }

        try {
            ResponseEntity<LogoutResponseDTO> respuesta = logoutClient.logout(logoutRequestDTO);

            if (respuesta.getStatusCode().is2xxSuccessful()) {
                LogoutResponseDTO response = respuesta.getBody();
                if (response != null && "00".equals(response.codigo())) {
                    return Mono.just(new LogoutResponseDTO("00", ""));
                } else {
                    return Mono.just(new LogoutResponseDTO("02", "Error al finalizar sesion"));
                }
            } else {
                return Mono.just(new LogoutResponseDTO("99", "Ocurrio un problema"));
            }
        } catch (Exception e) {
            return Mono.just(new LogoutResponseDTO("99", "Fail: " + e.getMessage()));
        }
    }
}
