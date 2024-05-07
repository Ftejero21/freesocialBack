package com.FreeSocial.com.Controller.UsuarioController;

import com.FreeSocial.com.Config.JwtTokenUtil;
import com.FreeSocial.com.Service.UsuarioService.UsuarioService;
import com.FreeSocial.com.Utils.FreeSocialConstants;
import com.FreeSocial.com.Utils.UsuarioResponse;
import com.FreeSocial.com.Utils.ValidadorUtils;
import com.FreeSocial.com.V.O.DTO.LoginRequest;
import com.FreeSocial.com.V.O.DTO.MessageResponseDTO;
import com.FreeSocial.com.V.O.DTO.ResponseDTO;
import com.FreeSocial.com.V.O.DTO.UsuarioDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping(value = FreeSocialConstants.USUARIO_URL)
@CrossOrigin(origins = "*")
public class UsuarioController {

    @Autowired
    private UsuarioValidador usuarioValidador;

    @Autowired
    private ValidadorUtils validadorUtils;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;


    @PostMapping(value = FreeSocialConstants.SAVE_OR_UPDATE)
    public ResponseEntity<?> saveOrUpdateUser(@Validated @RequestBody UsuarioDTO usuarioDTO, BindingResult result)  {


        usuarioValidador.validate(usuarioDTO, result);

        if (result.hasErrors()) {
            return validadorUtils.obtenerResponseErroresValidacion(result);
        }
        UsuarioResponse usuarioResponse = usuarioService.saveOrUpdateUser(usuarioDTO);

        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("user", usuarioResponse.getUsuarioDTO());
        responseMap.put("token", usuarioResponse.getToken());
        // Verificar si el usuario tiene ID para determinar si es una actualización o un nuevo registro
        String message;
        if (usuarioDTO.getId() != null) {
            message = "Usuario actualizado con éxito";
        } else {
            message = "Usuario registrado con éxito";
        }

        responseMap.put("message", message);

        return ResponseEntity.ok(responseMap);
    }

    @PostMapping(value = FreeSocialConstants.LOGIN)
    public ResponseEntity<?> loginUser(@Validated @RequestBody LoginRequest usuarioDTO, BindingResult result) {


        usuarioValidador.validateLogin(usuarioDTO, result);

        if (result.hasErrors()) {
            return validadorUtils.obtenerResponseErroresValidacion(result);
        }


        ResponseDTO dto = usuarioService.authenticate(usuarioDTO, false);
        return ResponseEntity.ok(Collections.singletonMap("token", dto.getToken()));
    }

    @GetMapping(value = FreeSocialConstants.OBTENER_USUARIO_NAME)
    public ResponseEntity<List<UsuarioDTO>> buscarUsuariosPorNombreYActualizarVisualizaciones(@RequestParam String nombre) {
        List<UsuarioDTO> usuarios = usuarioService.buscarUsuariosPorNombreYActualizarVisualizaciones(nombre);
        return ResponseEntity.ok(usuarios);
    }

    @GetMapping(value = FreeSocialConstants.GET_USUARIO + "/{id}")
    public ResponseEntity<UsuarioDTO> cogerUsuario(@PathVariable Long id) {
            UsuarioDTO usuarioDTO = usuarioService.cogerUsuario(id);
            return ResponseEntity.ok(usuarioDTO);
    }



    @GetMapping(value = FreeSocialConstants.OBTENER_USUARIO)
    public ResponseEntity<UsuarioDTO> obtenerUsuario() {
        UsuarioDTO usuarioDTO = usuarioService.getUsuarioByToken();

        if (usuarioDTO != null) {
            return ResponseEntity.ok(usuarioDTO);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
    }

    @PutMapping("/actualizarPassword")
    public ResponseEntity<?> actualizarContraseña(@RequestParam String email, @RequestParam String password, @RequestParam String repetirPassword) {

        BindingResult result = new BeanPropertyBindingResult(password, "password");

        // Validación de contraseñas
        usuarioValidador.validatePassword(password, repetirPassword, result);
        if (result.hasErrors()) {
            return validadorUtils.obtenerResponseErroresValidacion(result);
        }




        usuarioService.cambiarContraseña(email, password);
        return ResponseEntity.ok().body(new MessageResponseDTO("contraseña Actualizada"));


    }

    // Endpoint para solicitar el envío del código de recuperación
    @PostMapping(value = FreeSocialConstants.ENVIAR_CODIGO)
    public ResponseEntity<?> enviarCodigoRecuperacion(@RequestParam String email) {

        BindingResult result = new BeanPropertyBindingResult(email, "email");


        usuarioValidador.validateEmail(email, result);
        if (result.hasErrors()) {
            return validadorUtils.obtenerResponseErroresValidacion(result);
        }


        usuarioService.enviarCodigoRecuperacion(email);
        return ResponseEntity.ok().body(new MessageResponseDTO("Código de recuperación enviado."));
    }

    // Endpoint para verificar el código de recuperación
    @PostMapping(value = FreeSocialConstants.VERIFICAR_CODIGO)
    public ResponseEntity<?> verificarCodigoRecuperacion(@RequestParam String email, @RequestParam String codigo) {
        boolean esCodigoValido = usuarioService.validarCodigoRecuperacion(email, codigo);
        if (esCodigoValido) {
            return ResponseEntity.ok(new MessageResponseDTO("Código verificado correctamente."));
        } else {
            return ResponseEntity.badRequest().body(new MessageResponseDTO("Código invalido o expirado."));

        }
    }


    @PostMapping(value = FreeSocialConstants.VERIFICAR_TOKEN)
    public ResponseEntity<?> validateToken(@RequestBody Map<String, String> tokenMap) {
        System.out.println("Received tokenMap: " + tokenMap);
        String token = tokenMap.get("token");
        if (token == null || token.isEmpty()) {
            return ResponseEntity.badRequest().body("Token no proporcionado");
        }

        boolean isValid = usuarioService.validateToken(token);
        if (isValid) {
            Boolean recordado = jwtTokenUtil.getRecordadoFromToken(token);  // Asume que el método está en usuarioService
            if (recordado == null) {
                return ResponseEntity.ok(Collections.singletonMap("message", "Token invalido"));

            } else if (recordado) {
                return ResponseEntity.ok(Collections.singletonMap("message", "Token válido y usuario recordado"));

            } else {
                return ResponseEntity.ok(Collections.singletonMap("message", "Token válido"));

            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token inválido");
        }
    }

    @GetMapping("/rolesUsuario")
    public ResponseEntity<Set<Long>> obtenerRolesUsuario() {
        Set<Long> roles = usuarioService.getRolUsuario();

        if (!roles.isEmpty()) {
            return ResponseEntity.ok(roles);
        } else {
            // Puedes decidir devolver una respuesta vacía o un mensaje de error, según lo que sea más adecuado
            return ResponseEntity.noContent().build();
        }
    }

    @PostMapping("/seguir/{usuarioASeguirId}")
    public ResponseEntity<String> seguirUsuario(@PathVariable Long usuarioASeguirId) {
        boolean result = usuarioService.seguirUsuario(usuarioASeguirId);
        if (result) {
            return ResponseEntity.ok("Usuario seguido exitosamente.");
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Acceso denegado o acción no permitida.");
        }
    }

    @GetMapping("/seguidores")
    public ResponseEntity<List<UsuarioDTO>> obtenerSeguidores() {
        List<UsuarioDTO> seguidores = usuarioService.obtenerSeguidores();
        return ResponseEntity.ok(seguidores);
    }

    @GetMapping("/seguidos")
    public ResponseEntity<List<UsuarioDTO>> obtenerUsuariosQueSigo() {
        List<UsuarioDTO> usuariosSeguidos = usuarioService.obtenerUsuariosQueSigo();
        return ResponseEntity.ok(usuariosSeguidos);
    }




}
