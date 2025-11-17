package com.bio.clinic.controllers;

import com.bio.clinic.dtos.LoginDTO;
import com.bio.clinic.dtos.CadastroDTO;
import com.bio.clinic.dtos.FaceLoginRequest; // <-- IMPORTAR NOVO DTO
import com.bio.clinic.services.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*; // <-- MUDAR PARA @*

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

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginDTO loginDTO) {
        try {
            String token = authService.login(loginDTO);
            // ATENÇÃO: Retorne o token como um JSON para facilitar no frontend
            // Ex: {"token": "seu.token.aqui"}
            return ResponseEntity.ok("{\"token\": \"" + token + "\"}");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("CPF ou senha inválidos.");
        }
    }

    // --- ENDPOINT 1 (LOGIN FACIAL) ---
    // Busca o descritor facial salvo no banco para este CPF
    @GetMapping("/face-descriptor/{cpf}")
    public ResponseEntity<String> getFaceDescriptor(@PathVariable String cpf) {
        try {
            String descriptor = authService.getFaceDescriptor(cpf);
            return ResponseEntity.ok(descriptor); // Retorna a string longa do descritor
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    // --- ENDPOINT 2 (LOGIN FACIAL) ---
    // O frontend validou o rosto e agora só pede o token
    @PostMapping("/login-face")
    public ResponseEntity<String> loginWithFace(@RequestBody FaceLoginRequest loginRequest) {
        try {
            String token = authService.loginWithFace(loginRequest.getCpf());
            // Retorna o token da mesma forma que o login padrão
            return ResponseEntity.ok("{\"token\": \"" + token + "\"}");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
    }
}