package com.FreeSocial.com.Service.UsuarioService;

import com.FreeSocial.com.Config.JwtTokenUtil;
import com.FreeSocial.com.Config.JwtUserDetails;
import com.FreeSocial.com.Exception.FreeSocialException;
import com.FreeSocial.com.Repository.RolRepository;
import com.FreeSocial.com.Repository.UsuarioRepository;
import com.FreeSocial.com.Service.CustomUserDetailsService.CustomUserDetailsService;
import com.FreeSocial.com.Utils.CryptComponent;
import com.FreeSocial.com.Utils.FreeSocialConstants;
import com.FreeSocial.com.Utils.Mapping;
import com.FreeSocial.com.Utils.UsuarioResponse;
import com.FreeSocial.com.V.O.DTO.*;
import com.FreeSocial.com.V.O.Entity.*;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.transaction.Transactional;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static com.FreeSocial.com.Utils.Mapping.toUsuarioDTO;
import static com.FreeSocial.com.Utils.ValidadorUtils.setIfNotNull;
import static java.security.KeyRep.Type.SECRET;


@Service
public class UsuarioServiceImpl implements UsuarioService{

    @Autowired
    private CryptComponent cryptComponent;

    @Autowired
    private Mapping mapping;

    @Autowired
    private RolRepository rolRepository;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;


    @Override
    public UsuarioResponse saveOrUpdateUser(UsuarioDTO usuarioDTO) {
        try {
            UsuarioEntity usuario;
            boolean isNewUser = false;
            if (usuarioDTO.getId() == null) {

                    isNewUser = true;
                    usuario = new UsuarioEntity();
                    usuario.setEmail(usuarioDTO.getEmail());
                    FreelancerEntity freelancerEntity = null;
                    ContratanteEntity contratanteEntity = null;
                    String encryptedPassword = cryptComponent.encrypt(usuarioDTO.getPassword());
                    usuario.setPassword(encryptedPassword);

                    if (usuarioDTO.getImagenPerfilBase64() != null && !usuarioDTO.getImagenPerfilBase64().isEmpty()) {
                        byte[] imagenPerfilBytes = Base64.getDecoder().decode(usuarioDTO.getImagenPerfilBase64());
                        usuario.setImagenPerfil(imagenPerfilBytes);
                    }
                    usuario.setFechaCreacion(new Date());
                    usuario.setAparicionBusqueda(0);
                    UsuarioEntity existingUser = usuarioRepository.findByEmail(usuarioDTO.getEmail());
                    if (existingUser != null) {
                        throw new FreeSocialException("error.user.exist");
                    }

                    // Procesar imagen de fondo si está presente
                    if (usuarioDTO.getImagenBackgroundBase64() != null && !usuarioDTO.getImagenBackgroundBase64().isEmpty()) {
                        byte[] imagenBackgroundBytes = Base64.getDecoder().decode(usuarioDTO.getImagenBackgroundBase64());
                        usuario.setImagenBackground(imagenBackgroundBytes);
                    }
                    // Procesar roles
                    Set<RolEntity> rolesEntity = new HashSet<>();
                    if (usuarioDTO.getRoles() != null) {
                        for (RolDTO rolDTO : usuarioDTO.getRoles()) {
                            RolEntity rolEntity = new RolEntity();
                            rolEntity.setId(rolDTO.getId());
                            rolEntity.setNombre(rolDTO.getNombre());
                            rolesEntity.add(rolEntity);
                        }
                    }
                    usuario.setRoles(rolesEntity);

                    // Procesar datos personales
                    if (usuarioDTO.getDatosPersonales() != null) {
                        DatosPersonalesDTO datosDTO = usuarioDTO.getDatosPersonales();
                        DatosPersonalesEntity datosEntity = new DatosPersonalesEntity();
                        datosEntity.setNombre(datosDTO.getNombre());
                        datosEntity.setApellidos(datosDTO.getApellidos());
                        datosEntity.setTelefono(datosDTO.getTelefono());
                        datosEntity.setFechaNacimiento(datosDTO.getFechaNacimiento());
                        datosEntity.setCiudad(datosDTO.getCiudad());
                        usuario.setDatosPersonales(datosEntity);
                    }

                    UsuarioEntity savedUsuario = usuarioRepository.save(usuario);
                    UsuarioDTO savedDTO = toUsuarioDTO(savedUsuario);


                    String token = null;

                    // Autenticar si es un nuevo usuario
                    if (isNewUser) {
                        LoginRequest loginRequest = new LoginRequest();
                        loginRequest.setPassword(savedDTO.getPassword());
                        loginRequest.setEmail(savedUsuario.getEmail());
                        loginRequest.setRecordado(savedDTO.getRecordado());

                        ResponseDTO authResponse = authenticate(loginRequest, true);
                        token = authResponse.getToken();
                    }

                    return new UsuarioResponse(savedDTO, token);


            } else {
                UsuarioEntity savedUsuario = usuarioRepository.save(mapping.toUsuarioEntity(usuarioDTO));
                UsuarioDTO savedDTO = toUsuarioDTO(savedUsuario);
                String token = null;

                // Convertir Entity guardado de vuelta a DTO

                return new UsuarioResponse(savedDTO, token);
            }

        } catch (DataAccessException e) {
            throw new FreeSocialException(e.getMessage());
        }

    }



    @Override
    public ResponseDTO authenticate(LoginRequest usuarioDTO, boolean isRegistering) {
        try {
            UsuarioEntity usuario = usuarioRepository.findByEmail(usuarioDTO.getEmail());
            String rawPassword = usuarioDTO.getPassword();
            String encodedPassword = cryptComponent.decrypt(usuario.getPassword());
            if ( isRegistering || rawPassword.equals(encodedPassword)) {
                usuario.setUltimoLogin(LocalDateTime.now());
                if(!isRegistering) {
                    usuario.setRecordado(usuarioDTO.getRecordado());
                    usuarioRepository.save(usuario);
                }

                UserDetails userDetails = new JwtUserDetails(usuarioDTO.getEmail(), usuario.getRoles());
                String token = jwtTokenUtil.generateToken(userDetails);
                return new ResponseDTO("Autenticación exitosa.", token);
            } else {
                throw new FreeSocialException("error.login");
            }

        } catch (DataAccessException e) {
            throw new FreeSocialException("error.database");
        } catch (Exception e) {
            e.printStackTrace();
            e.getMessage();
            throw new FreeSocialException("error.unknown");

        }
    }

    @Override
    public boolean validateToken(String token) {
        try {
            String username = jwtTokenUtil.getUsernameFromToken(token);
            UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);
            return jwtTokenUtil.validateToken(token, userDetails);
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public void enviarCodigoRecuperacion(String email) {
        UsuarioEntity usuario = usuarioRepository.findByEmail(email);
        if (usuario == null) {
            throw new FreeSocialException("error.usuario.noEncontrado");
        }
        String codigo = generarCodigoAleatorio();
        // Guardar el código en algún lugar para su posterior verificación, podría ser en la base de datos.
        guardarCodigoRecuperacion(usuario, codigo);
        // Lógica para enviar el correo electrónico con JavaMailSender.
        enviarCorreoConCodigo(email, codigo);
    }


    private String generarCodigoAleatorio() {
        int length = 10; // longitud del código
        boolean useLetters = true;
        boolean useNumbers = true;
        return RandomStringUtils.random(length, useLetters, useNumbers);
    }
    @Override
    public boolean validarCodigoRecuperacion(String email, String codigo) {
        // Obtener el código guardado previamente para el usuario.
        String codigoGuardado = obtenerCodigoGuardado(email);
        return codigoGuardado.equals(codigo);
    }



    private String obtenerCodigoGuardado(String email) {
        UsuarioEntity usuario = usuarioRepository.findByEmail(email);
        if (usuario != null) {
            // Verificar si el código aún no ha expirado.
            Date ahora = new Date();
            if (usuario.getFechaExpiracionCodigoRecuperacion() != null &&
                    usuario.getFechaExpiracionCodigoRecuperacion().after(ahora)) {
                return usuario.getCodigoRecuperacion();
            }
        }
        return null; // Si no hay usuario o el código ha expirado, retornar null.
    }

    private void guardarCodigoRecuperacion(UsuarioEntity usuario, String codigo) {
        usuario.setCodigoRecuperacion(codigo);
        usuario.setFechaExpiracionCodigoRecuperacion(calcularFechaExpiracion());
        usuarioRepository.save(usuario);
    }

    public String cargarHTMLYReemplazarCodigo(String nombre,String codigo) {
        try {
            ClassPathResource resource = new ClassPathResource("static/PlantillaEmail.html");
            byte[] bytes = Files.readAllBytes(resource.getFile().toPath());
            String contenido = new String(bytes, StandardCharsets.UTF_8);

            // Reemplazar el marcador de posición con el valor real
            contenido = contenido.replace("{{codigo}}", codigo);
            contenido = contenido.replace("{{nombre}}", nombre);
            return contenido;
        } catch (IOException e) {
            throw new RuntimeException("Error al leer la plantilla de correo electrónico", e);
        }
    }

    private void enviarCorreoConCodigo(String email, String codigo) {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        UsuarioEntity usuario = usuarioRepository.findByEmail(email);
        String htmlContent = cargarHTMLYReemplazarCodigo(usuario.getDatosPersonales().getNombre(),codigo);

        try {
            helper.setTo(email);
            helper.setSubject("Tu código de verificación");
            helper.setText(htmlContent, true);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }

        mailSender.send(message);
    }

    public void cambiarContraseña(String email, String nuevaContraseña) {
        UsuarioEntity usuario = usuarioRepository.findByEmail(email);
        if (usuario == null) {
            throw new FreeSocialException("error.usuario.noEncontrado");
        }
        String encryptedPassword = cryptComponent.encrypt(nuevaContraseña);
        usuario.setPassword(encryptedPassword);
        usuarioRepository.save(usuario);
    }

    @Override
    public UsuarioDTO getUsuarioByToken() {
        String token = jwtTokenUtil.getCurrentToken();
        if (token != null) {
            Long userId = jwtTokenUtil.getUserIdFromToken(token);
            UsuarioEntity usuario = usuarioRepository.findById(userId).orElse(null);

            if (usuario != null) {
                // Conversión de UsuarioEntity a UsuarioDTO
                return toUsuarioDTO(usuario);
            }
        }
        return null;
    }

    @Override
    public Set<Long> getRolUsuario() {
        String token = jwtTokenUtil.getCurrentToken();
        if (token != null) {
            Long userId = jwtTokenUtil.getUserIdFromToken(token);
            UsuarioEntity usuario = usuarioRepository.findById(userId).orElse(null);

            if (usuario != null && usuario.getRoles() != null) {
                return usuario.getRoles().stream()
                        .map(RolEntity::getId)
                        .collect(Collectors.toSet());
            }
        }
        return Collections.emptySet();
    }

    @Override
    @Transactional
    public List<UsuarioDTO> buscarUsuariosPorNombreYActualizarVisualizaciones(String nombre) {
        String token = jwtTokenUtil.getCurrentToken();

        Long  userId = jwtTokenUtil.getUserIdFromToken(token);

        List<UsuarioEntity> usuarios = usuarioRepository.findByNombreUsuario(nombre,userId);

        if(usuarios.size() > 1){
            usuarios.forEach(usuario -> {
                int visualizacionesActuales = Optional.ofNullable(usuario.getAparicionBusqueda()).orElse(0);
                usuario.setAparicionBusqueda(visualizacionesActuales + 1);
                usuarioRepository.save(usuario);
            });

           
        }
        return usuarios.stream()
                .map(usuario -> toUsuarioDTO(usuario)) // Utiliza el método estático toUsuarioDTO
                .collect(Collectors.toList());

    }

    @Override
    public UsuarioDTO cogerUsuario(Long id) {
        Optional<UsuarioEntity> usuarioEntity = usuarioRepository.findById(id);

        if (usuarioEntity.isPresent()) {
            return toUsuarioDTO(usuarioEntity.get());
        } else {
            // Manejar el caso en que el usuario no se encuentra.
            // Podrías lanzar una excepción o retornar null dependiendo de tu caso de uso.
            throw new RuntimeException("Usuario no encontrado con ID: " + id);
        }
    }

    @Override
    public boolean seguirUsuario(Long usuarioASeguirId) {
        String token = jwtTokenUtil.getCurrentToken();
        if (token == null) {
            return false;
        }
        Long userId = jwtTokenUtil.getUserIdFromToken(token);
        UsuarioEntity usuario = usuarioRepository.findById(userId).orElse(null);
        if (usuario == null) {
            return false;
        }

        UsuarioEntity usuarioASeguir = usuarioRepository.findById(usuarioASeguirId)
                .orElseThrow(() -> new IllegalArgumentException("Usuario a seguir no encontrado: " + usuarioASeguirId));

        if (usuario.getSiguiendo().contains(usuarioASeguir)) {
            // Si ya está siguiendo al usuario, proceder a dejar de seguir
            usuario.getSiguiendo().remove(usuarioASeguir);
            usuarioASeguir.getSeguidores().remove(usuario);
            usuarioRepository.save(usuario);
            usuarioRepository.save(usuarioASeguir);
            return true; // Devolver false puede indicar que se dejó de seguir
        } else {
            // Si no está siguiendo al usuario, proceder a seguirlo
            usuario.getSiguiendo().add(usuarioASeguir);
            usuarioASeguir.getSeguidores().add(usuario);
            usuarioRepository.save(usuario);
            usuarioRepository.save(usuarioASeguir);
            return true; // Devolver true puede indicar que ahora está siguiendo
        }
    }

    @Override
    public List<UsuarioDTO> obtenerSeguidores() {
        String token = jwtTokenUtil.getCurrentToken();
        Long userId = jwtTokenUtil.getUserIdFromToken(token);
        UsuarioEntity usuario = usuarioRepository.findById(userId).orElse(null);

        System.out.println(usuario.getSeguidores());

        return usuario.getSeguidores().stream()
                .map(seguidor -> Mapping.toUsuarioDTO(seguidor))
                .collect(Collectors.toList());
    }

    @Override
    public List<UsuarioDTO> obtenerUsuariosQueSigo() {
        String token = jwtTokenUtil.getCurrentToken();
        Long userId = jwtTokenUtil.getUserIdFromToken(token);
        UsuarioEntity usuario = usuarioRepository.findById(userId).orElse(null);

        if (usuario == null) {
            return Collections.emptyList(); // Devolver una lista vacía si el usuario no se encuentra
        }

        return usuario.getSiguiendo().stream()
                .map(usuarioSeguido -> Mapping.toUsuarioDTO(usuarioSeguido))
                .collect(Collectors.toList());
    }

    private Date calcularFechaExpiracion() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.HOUR, 1); // El código expira en 1 hora
        return calendar.getTime();
    }

}

