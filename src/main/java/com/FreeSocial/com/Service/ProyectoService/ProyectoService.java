package com.FreeSocial.com.Service.ProyectoService;

import com.FreeSocial.com.V.O.DTO.ExperienciaLaboralDTO;
import com.FreeSocial.com.V.O.DTO.ProyectosDTO;
import org.springframework.stereotype.Service;

import java.util.List;


public interface ProyectoService {


    public ProyectosDTO agregarProyecto(ProyectosDTO proyectosDTO);

    public List<ProyectosDTO> obtenerProyectosDelUsuario();

    public List<ProyectosDTO> obtenerProyectosDeOtroUsuario(Long userId);
}
