// no pacote com.bio.clinic.services
package com.bio.clinic.services;

import com.bio.clinic.repositories.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service // Marca esta classe como um serviço do Spring
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // A lógica que estava na SecurityConfig agora está aqui.
        return userRepository.findByCpf(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário com CPF " + username + " não encontrado."));
    }
}