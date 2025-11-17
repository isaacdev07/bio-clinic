package com.bio.clinic.services;

import com.bio.clinic.dtos.CadastroDTO;
import com.bio.clinic.dtos.LoginDTO;
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
        // Remove máscara do CPF, se houver, antes de salvar
        String cleanCpf = cadastroDTO.getCpf().replaceAll("[.\\-\\/]", "");
        
        if (userRepository.findByCpf(cleanCpf).isPresent()) {
            throw new RuntimeException("Este CPF já está cadastrado.");
        }

        String senhaCriptografada = passwordEncoder.encode(cadastroDTO.getSenha());
        User newUser = new User(cadastroDTO.getNome(), senhaCriptografada, cleanCpf);

        userRepository.save(newUser);
    }

    public String login(LoginDTO loginDTO) {
        // Remove máscara do CPF, se houver, antes de autenticar
        String cleanCpf = loginDTO.getCpf().replaceAll("[.\\-\\/]", "");

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        cleanCpf, // Usa CPF limpo
                        loginDTO.getSenha()
                )
        );

        var user = userRepository.findByCpf(cleanCpf)
                .orElseThrow(() -> new IllegalStateException("Usuário não encontrado após autenticação."));

        return jwtService.generateToken(user);
    }

    // --- NOVO MÉTODO (LOGIN FACIAL 1) ---
    /**
     * Busca o descritor facial de um usuário pelo CPF.
     * @param cpf O CPF do usuário (pode estar com máscara).
     * @return A string do descritor facial.
     */
    public String getFaceDescriptor(String cpf) {
        String cleanCpf = cpf.replaceAll("[.\\-\\/]", "");
        
        User user = userRepository.findByCpf(cleanCpf)
                .orElseThrow(() -> new RuntimeException("CPF não encontrado."));

        String descriptor = user.getFaceDescriptor();
        
        if (descriptor == null || descriptor.isEmpty()) {
            throw new RuntimeException("Nenhum rosto cadastrado para este CPF.");
        }
        
        return descriptor;
    }

    // --- NOVO MÉTODO (LOGIN FACIAL 2) ---
    /**
     * Gera um token JWT para um usuário com base apenas no CPF.
     * Usado após o frontend validar o rosto.
     * @param cpf O CPF do usuário (pode estar com máscara).
     * @return O token JWT.
     */
    public String loginWithFace(String cpf) {
        String cleanCpf = cpf.replaceAll("[.\\-\\/]", "");

        var user = userRepository.findByCpf(cleanCpf)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado."));

        // Gera o token diretamente, sem checar senha
        return jwtService.generateToken(user);
    }
}