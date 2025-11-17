package com.bio.clinic.controllers;

import com.bio.clinic.dtos.FaceRegistrationRequest;
import com.bio.clinic.services.FacialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/facial") 
public class FacialController {

    private final FacialService facialService;

    @Autowired
    public FacialController(FacialService facialService) {
        this.facialService = facialService;
    }

    /**
     * Endpoint para o usuário logado cadastrar seu próprio rosto.
     */
    @PostMapping("/register-face")
    public ResponseEntity<String> registerFace(@RequestBody FaceRegistrationRequest request) {
        try {
            facialService.registerFace(request.getDescriptor());
            return ResponseEntity.ok("Rosto cadastrado com sucesso.");
        } catch (RuntimeException e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }
}