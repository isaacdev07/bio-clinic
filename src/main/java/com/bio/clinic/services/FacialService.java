package com.bio.clinic.services;

import com.bio.clinic.entities.User;
import com.bio.clinic.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class FacialService {

    private final UserRepository userRepository;

    @Autowired
    public FacialService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Salva o descritor facial para o usuário que está logado.
     * @param descriptor A string longa do descritor facial.
     */
    public void registerFace(String descriptor) {
        // 1. Pega o usuário que está autenticado (logado)
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userCpf = authentication.getName(); // Isso pega o CPF (username) do token

        // 2. Busca o usuário no banco
        User user = userRepository.findByCpf(userCpf)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado na sessão."));

        // 3. Define o descritor e salva
        user.setFaceDescriptor(descriptor);
        userRepository.save(user);
    }
}