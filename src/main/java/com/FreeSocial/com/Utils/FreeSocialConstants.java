package com.FreeSocial.com.Utils;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class FreeSocialConstants {
    /**
     * Constantes para los tipos de Roles
     */
    public static final Long ROL_ADMINISTRADOR = 1L;

    public static final Long ROL_FREELANCER = 2L;

    public static final Long ROL_CONTRATANTE = 3L;


    /**
     * Path para el controller de Usuario
     */
    public static final String USUARIO_URL = "/api/usuario";

    public static final String LOGIN = "/login";

    public static final String ENVIAR_CODIGO = "/enviarCodigo";

    public static final String VERIFICAR_CODIGO = "/verificarCodigo";

    public static final String VERIFICAR_TOKEN = "/verificarToken";

    public static final String SAVE_OR_UPDATE = "/saveOrUpdate";

    public static final String OBTENER_USUARIO = "/obtenerUsuario";

    public static final String OBTENER_USUARIO_NAME = "/buscarUsuario";

    public static final String GET_USUARIO = "/getUsuario";

    /**
     * Path para el controller de DatosPersonales
     */
    public static final String DATOS_PERSONALES_URL = "/api/datosPersonales";

    public static final String SAVE_OR_UPDATE_PERSONAL_DATA = "/saveOrUpdatePersonalData";

    /**
     * Path para el controller de Ciudades
     */
    public static final String CIUDADES_URL = "/api/ciudades";

    public static final String GET_ALL_CITY_URL = "/getAllCitys";


    /**
     * Path para el controller de Educacion
     */
    public static final String EDUCACION_URL = "/api/educacion";

    public static final String SAVEORUPDATE_EDUCACION = "/saveOrUpdate";

    public static final String GET_EDUCACION = "/getEducacion";

    public static final String DELETE_EDUCACION = "/deleteEducacion";


    /**
     * Path para el controller de experiencia laboral
     */
    public static final String EXPERIENCIA_URL = "/api/experiencia";

    public static final String SAVEORUPDATE_EXPERIENCIA = "/saveOrUpdate";

    public static final String GET_EXPERIENCIA= "/getExperiencia";

    public static final String DELETE_EXPERIENCIA = "/deleteExperiencia";

    public static final String GET_TIEMPO_TOTAL_TRABAJADO = "/tiempoTotalTrabajado";

    /**
     * Path para el controller de proyectos
     */

    public static final String PROYECTO_URL = "/api/proyecto";

    public static final String SAVEORUPDATE_PROYECTO = "/saveOrUpdate";

    public static final String GET_PROYECTO= "/getProyecto";


    /**
     * Path para el controller de Publicaciones
     */

    public static final String PUBLICACION_URL = "/api/publicacion";

    public static final String SAVEORUPDATE_PUBLICACION = "/saveOrUpdate";

    public static final String GET_PUBLICACIONES= "/getPublicaciones";

    public static final String CREATE_COMMENT= "/crearComentario";

    public static final String GET_COMMENTS= "/cogerComentarios";

    /**
     * Path para el controller de Like
     */

    public static final String Like_URL = "/api/like";

    public static final String DAR_LIKE = "/darLike";

    public static final String GET_PUBLICACIONES_LIKE = "/getPublicacionesLike";

    /**
     * Path para el controller de mensajeria
     */

    public static final String MENSAJERIA_URL = "/api/mensajeria";
}
