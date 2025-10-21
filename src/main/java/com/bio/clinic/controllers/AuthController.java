package com.bio.clinic.controllers;

// Adicione a importação para o LoginDTO
import com.bio.clinic.dtos.LoginDTO;
import com.bio.clinic.dtos.CadastroDTO;
import com.bio.clinic.services.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/registrar")
    public ResponseEntity<String> registrar(@Valid @RequestBody CadastroDTO cadastroDTO) {
        try {
            authService.cadastrar(cadastroDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body("Usuário cadastrado com sucesso!");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    // --- NOVO ENDPOINT DE LOGIN ADICIONADO ---
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginDTO loginDTO) {
        try {
            // Chama o método de login do serviço, que retorna o token
            String token = authService.login(loginDTO);
            return ResponseEntity.ok(token);
        } catch (Exception e) {
            // Captura falhas de autenticação (usuário/senha errados)
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("CPF ou senha inválidos.");
        }
    }
}