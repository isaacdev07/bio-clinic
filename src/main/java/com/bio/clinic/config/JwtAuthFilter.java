package com.bio.clinic.config;

import com.bio.clinic.services.JwtService;
import io.jsonwebtoken.ExpiredJwtException; // Importante: Adicione este import
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    public JwtAuthFilter(JwtService jwtService, UserDetailsService userDetailsService) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String userCpf;

        // 1. Se não tem cabeçalho ou não começa com Bearer, segue o fluxo (Login/Registro sem token funcionam aqui)
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            jwt = authHeader.substring(7); // Extrai o token do "Bearer "
            userCpf = jwtService.extractUsername(jwt); // Extrai o CPF do token (Aqui pode dar erro de expirado)

            // Se temos o CPF e o usuário ainda não está autenticado no contexto de segurança
            if (userCpf != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = this.userDetailsService.loadUserByUsername(userCpf);

                // Se o token for válido, autenticamos o usuário
                if (jwtService.isTokenValid(jwt, userDetails)) {
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities()
                    );
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }
        } catch (ExpiredJwtException e) {
            // --- CORREÇÃO ---
            // Se o token expirou, apenas logamos (opcional) e deixamos a requisição seguir.
            // Como não setamos a autenticação no SecurityContextHolder, o usuário será tratado como "Anônimo".
            // Se ele estiver tentando acessar /auth/registrar, vai funcionar.
            // Se tentar acessar /perfil, o Spring Security vai bloquear logo em seguida com 403.
            System.out.println("Token expirado, prosseguindo como anônimo: " + e.getMessage());
        } catch (Exception e) {
            // Captura token malformado ou outros erros para não travar a requisição com 500
            System.out.println("Erro ao processar token JWT: " + e.getMessage());
        }

        filterChain.doFilter(request, response);
    }
}