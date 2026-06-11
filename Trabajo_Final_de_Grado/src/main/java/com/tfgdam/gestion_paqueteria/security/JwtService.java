package com.tfgdam.gestion_paqueteria.security;

import com.tfgdam.gestion_paqueteria.domain.entity.Usuario;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;

@Service
public class JwtService
{
    @Value("${jwt.secret}")
    private String clave;

    private static final long EXPIRACION = 1000 * 60 * 60 * 24; // 24 horas

    public String generarToken (Usuario u)
    {
        return Jwts.builder()
                .subject(u.getDni())
                .claim("rol", u.getRol().getNombre())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + EXPIRACION))
                .signWith(getKey())
                .compact();
    }

    public String extraerDNI(String token)
    {
        return getClaims(token).getSubject();
    }

    public boolean validarToken(String token, Usuario u)
    {
        String dni = extraerDNI(token);
        return dni.equals(u.getDni()) && !tokenExpirado(token);
    }

    public boolean tokenExpirado(String token)
    {
        return getClaims(token).getExpiration().before(new Date());
    }

    private Claims getClaims(String token)
    {
        return Jwts.parser()
                .verifyWith(getKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private SecretKey getKey()
    {
        byte[] bytes = Decoders.BASE64URL.decode(clave);
        return Keys.hmacShaKeyFor(bytes);
    }
}
