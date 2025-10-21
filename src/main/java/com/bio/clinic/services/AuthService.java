package com.bio.clinic.services;

import com.bio.clinic.dtos.CadastroDTO;
import com.bio.clinic.dtos.LoginDTO; // Importe o LoginDTO
import com.bio.clinic.entities.User;
import com.bio.clinic.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    // ATUALIZE O CONSTRUTOR para incluir os novos serviços
    @Autowired
    public AuthService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            AuthenticationManager authenticationManager,
            JwtService jwtService
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    public void cadastrar(CadastroDTO cadastroDTO) {
        if (userRepository.findByCpf(cadastroDTO.getCpf()).isPresent()) {
            throw new RuntimeException("Este CPF já está cadastrado.");
        }

        String senhaCriptografada = passwordEncoder.encode(cadastroDTO.getSenha());
        User newUser = new User(cadastroDTO.getNome(), senhaCriptografada, cadastroDTO.getCpf());

        userRepository.save(newUser);
    }

    // --- NOVO MÉTODO DE LOGIN ADICIONADO ---
    public String login(LoginDTO loginDTO) {
        // 1. Usa o AuthenticationManager para validar o CPF e a senha
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginDTO.getCpf(),
                        loginDTO.getSenha()
                )
        );

        // 2. Se a autenticação passar, busca o usuário no banco
        var user = userRepository.findByCpf(loginDTO.getCpf())
                .orElseThrow(() -> new IllegalStateException("Usuário não encontrado após autenticação."));

        // 3. Gera e retorna o token JWT
        return jwtService.generateToken(user);
    }
}