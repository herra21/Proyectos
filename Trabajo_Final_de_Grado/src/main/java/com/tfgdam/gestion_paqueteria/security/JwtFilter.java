package com.tfgdam.gestion_paqueteria.security;

import com.tfgdam.gestion_paqueteria.domain.entity.Usuario;
import com.tfgdam.gestion_paqueteria.repository.UsuarioRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        // 1. Leer el header Authorization
        String authHeader = request.getHeader("Authorization");

        // 2. Si no hay token o no empieza por "Bearer ", dejamos pasar sin autenticar
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // 3. Extraer el token quitando "Bearer "
        String token = authHeader.substring(7);

        // 4. Extraer el DNI del token
        String dni = jwtService.extraerDNI(token);

        // 5. Si hay DNI y el usuario no está ya autenticado en este request
        if (dni != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            Optional<Usuario> optional = usuarioRepository.findById(dni);

            if (optional.isPresent()) {
                Usuario usuario = optional.get();

                // 6. Validar el token
                if (jwtService.validarToken(token, usuario)) {

                    // 7. Crear el objeto de autenticación con el rol del usuario
                    String rol = usuario.getRol().getNombre();
                    UsernamePasswordAuthenticationToken auth =
                            new UsernamePasswordAuthenticationToken(
                                    usuario,
                                    null,
                                    List.of(new SimpleGrantedAuthority("ROLE_" + rol))
                            );

                    // 8. Registrar la autenticación en el contexto de Spring Security
                    SecurityContextHolder.getContext().setAuthentication(auth);
                }
            }
        }

        // 9. Dejar pasar la petición al siguiente filtro o controlador
        filterChain.doFilter(request, response);
    }
}