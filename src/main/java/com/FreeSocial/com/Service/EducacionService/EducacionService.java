package com.FreeSocial.com.Service.EducacionService;

import com.FreeSocial.com.V.O.DTO.EducacionDTO;

import java.util.List;
import java.util.Set;

public interface EducacionService {

    /**
     * Se encarga de añadir la educacion
     * @param educacionDTO
     * @return
     */
    public EducacionDTO agregarEducacion(EducacionDTO educacionDTO);

    /**
     * Se encarga de obterner las educaciones del usuario
     * @return
     */
    public List<EducacionDTO> obtenerEducacionesDelUsuario();


    public List<EducacionDTO> obtenerEducacionesDeOtroUsuario(Long userId);

    /**
     * Se encarga de borrar una educacion
     * @param id
     * @throws Exception
     */
    public void borrarEducacion(Long id) throws Exception;
}
