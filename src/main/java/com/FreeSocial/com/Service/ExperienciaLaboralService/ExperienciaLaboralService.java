package com.FreeSocial.com.Service.ExperienciaLaboralService;

import com.FreeSocial.com.V.O.DTO.EducacionDTO;
import com.FreeSocial.com.V.O.DTO.ExperienciaLaboralDTO;

import java.util.List;
import java.util.Set;

public interface ExperienciaLaboralService {

    public ExperienciaLaboralDTO agregarExperiencia(ExperienciaLaboralDTO experienciaLaboralDTO);


    public List<ExperienciaLaboralDTO> obtenerExperienciaDelUsuario();


    public List<ExperienciaLaboralDTO> obtenerExperienciaDeOtroUsuario(Long userId);

    public void borrarExperiencia(Long id) throws Exception;


    public String obtenerTiempoTotalTrabajadoDelUsuario();
}
