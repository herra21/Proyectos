package com.tfgdam.gestion_paqueteria.service.impl;

import com.tfgdam.gestion_paqueteria.domain.dto.*;
import com.tfgdam.gestion_paqueteria.domain.entity.Camion;
import com.tfgdam.gestion_paqueteria.domain.entity.Rol;
import com.tfgdam.gestion_paqueteria.domain.entity.Usuario;
import com.tfgdam.gestion_paqueteria.repository.RolRepository;
import com.tfgdam.gestion_paqueteria.repository.UsuarioRepository;
import com.tfgdam.gestion_paqueteria.security.JwtService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UsuarioService
{

    private static final String PATRONDNI = "^[0-9]{8}[A-Za-z]$";
    private static final String LETRASDNI = "TRWAGMYFPDXBNJZSQVHLCKE";

    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private RolRepository rolRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtService jwtService;

    //private JwtService jwtService;
    private AuthenticationManager authenticationManager;

    public UsuarioRegisterResponseDTO registrarUsuario(UsuarioRegisterRequestDTO dto)
    {
        if (!validarDni(dto.getDni()))
        {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Error. El DNI: " + dto.getDni() + " no es válido.");
        }

        if (usuarioRepository.existsById(dto.getDni()))
        {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Error. Ya existe un usuario registrado con el DNI: " + dto.getDni());
        }

        Usuario usuario = new Usuario();

        usuario.setDni(dto.getDni());
        usuario.setNombre(dto.getNombre());
        usuario.setApellidos(dto.getApellidos());
        //Encriptar contraseña
        usuario.setContrasenia(passwordEncoder.encode(dto.getContrasenia()));

        Rol rol = rolRepository.findByNombre(dto.getRol());
        usuario.setRol(rol);

        Usuario guardado = usuarioRepository.save(usuario);
        return new UsuarioRegisterResponseDTO(guardado.getDni(), guardado.getNombre(), guardado.getApellidos());
    }

    @Transactional
    public UsuarioLoginResponseDTO loginUsuario(UsuarioLoginRequestDTO dto)
    {
        Optional<Usuario> optional = usuarioRepository.findById(dto.getDni());

        if (optional.isEmpty())
        {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Error. Usuario o contraseña no válidos.");
        }

        Usuario usuario = optional.get();

        if (!passwordEncoder.matches(dto.getContrasenia(), usuario.getContrasenia()))
        {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Error. Usuario o contraseña no válidos.");
        }

        // Accedemos al rol dentro de la transacción para evitar LazyInitializationException
        String nombreRol = usuario.getRol() != null ? usuario.getRol().getNombre() : "EMPLEADO";

        // Generar el token
        String token = jwtService.generarToken(usuario);
        return new UsuarioLoginResponseDTO(usuario.getDni(), nombreRol, token);
    }

    public List<UsuarioListaResponseDTO> listarUsuarios()
    {
        return usuarioRepository.findAll()
                .stream()
                .map(u -> new UsuarioListaResponseDTO(
                        u.getDni(),
                        u.getNombre(),
                        u.getApellidos(),
                        (u.getCamion() != null) ? u.getCamion().getMatricula() : null,
                        u.getRol().getNombre()
                )).toList();
    }

    private boolean validarDni(String dni)
    {
        if (dni == null || !dni.matches(PATRONDNI))
        {
            return false;
        }

        int numero = Integer.parseInt(dni.substring(0, 8));
        char letraCorrecta = LETRASDNI.charAt(numero % 23);
        char letraIntroducida = Character.toUpperCase(dni.charAt(8));

        return letraIntroducida == letraCorrecta;
    }
/*
    public Optional<Usuario> getByDni(String dni) {

        Optional<Usuario> usuario = usuarioRepository.findById(dni);

        if (usuario.isPresent()) {

            return usuarioRepository.findById(dni);
        }else {

            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado"); }

    }

    public void borrarUsuario(String dni){
    Optional<Usuario> usuario = usuarioRepository.findById(dni);

    if (usuario.isPresent()){

        usuarioRepository.delete(usuario.get());
    }else {
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado");
    }


    }
    public void actualizarUsuario(Usuario usuario, String dni, String nombre, String apellidos){
        Optional<Usuario> optional = usuarioRepository.findById(dni);

        if(optional.isPresent()){

            Usuario usuarioExistente = optional.get();
            usuario.setNombre(nombre);
            usuario.setApellidos(apellidos);
            usuarioRepository.save(usuarioExistente);

        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado");

        }
    }
    public void actualizarContrasenia(String contrasenia, String dni){

        Optional<Usuario> optional = usuarioRepository.findById(dni);
        if (optional.isPresent()) {

           Usuario usuarioExistente = optional.get();

           usuarioExistente.setContrasenia(passwordEncoder.encode(contrasenia));
           usuarioRepository.save(usuarioExistente);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado");
        }
    }
    public void asignarCamion(String dni) {

        Optional<Usuario> usuario = usuarioRepository.findById(dni);
        if (usuario.isPresent()) {

            Usuario usuarioExistente = usuario.get();
            Camion camion = new Camion();
            //usuarioExistente.set(camion);

        }
    }
*/

}
