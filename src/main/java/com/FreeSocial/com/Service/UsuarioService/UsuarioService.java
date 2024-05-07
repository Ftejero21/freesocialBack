package com.FreeSocial.com.Service.UsuarioService;

import com.FreeSocial.com.Utils.UsuarioResponse;
import com.FreeSocial.com.V.O.DTO.LoginRequest;
import com.FreeSocial.com.V.O.DTO.ResponseDTO;
import com.FreeSocial.com.V.O.DTO.UsuarioDTO;
import com.FreeSocial.com.V.O.Entity.UsuarioEntity;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface UsuarioService {


    /**
     * Nos encargamos de registrar o actualizar un usuario en la aplicacion
     * @param usuarioDTO
     * @return
     */
    public UsuarioResponse saveOrUpdateUser(UsuarioDTO usuarioDTO);

    /**
     * Nos encragamos de autenticar/logear un usuario en la aplicacion
     * @param usuarioDTO
     * @return
     */
    public ResponseDTO authenticate(LoginRequest usuarioDTO, boolean isRegistering);


    /**
     * Se encarga de verificar el Token
     * @param token
     * @return
     */
    public boolean validateToken(String token);

    /**
     * Se encarga de enviar un codigo de recuperacion de cuenta
     * @param email
     */
    public void enviarCodigoRecuperacion(String email);

    /**
     * Se encarga de validar el codigo de verificacion
     * @param email
     * @param codigo
     * @return
     */
    public boolean validarCodigoRecuperacion(String email, String codigo);

    /**
     * Se encarga de actualizar la contraseña
     * @param email
     * @param password
     */
    public void cambiarContraseña(String email, String password);

    /**
     * Se encarga de coger los datos del usuario mediante el token que genera
     */
    public UsuarioDTO getUsuarioByToken();

    /**
     * Se encarga de coger el rol del usuario mediante el token
     */
    public Set<Long> getRolUsuario();

    public List<UsuarioDTO> buscarUsuariosPorNombreYActualizarVisualizaciones(String nombre);


    public UsuarioDTO cogerUsuario(Long id);

    public boolean seguirUsuario(Long usuarioASeguirId);

    public List<UsuarioDTO> obtenerSeguidores();

    public List<UsuarioDTO> obtenerUsuariosQueSigo();

}
