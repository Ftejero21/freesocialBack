package com.FreeSocial.com.Controller.ProyectoController;


import com.FreeSocial.com.Service.ProyectoService.ProyectoService;
import com.FreeSocial.com.Utils.FreeSocialConstants;
import com.FreeSocial.com.Utils.ValidadorUtils;
import com.FreeSocial.com.V.O.DTO.ExperienciaLaboralDTO;
import com.FreeSocial.com.V.O.DTO.ProyectosDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = FreeSocialConstants.PROYECTO_URL)
@CrossOrigin(origins = "*")
public class ProyectoController {

    @Autowired
    private ProyectoService proyectoService;

    @Autowired
    private ProyectoValidator proyectoValidator;

    @Autowired
    private ValidadorUtils validadorUtils;


    @PostMapping(value = FreeSocialConstants.SAVEORUPDATE_PROYECTO, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> guardarProyecto(@RequestBody  @Validated ProyectosDTO proyectosDTO, BindingResult result) {

        proyectoValidator.validate(proyectosDTO, result);

        if (result.hasErrors()) {
            return validadorUtils.obtenerResponseErroresValidacion(result);
        }


        ProyectosDTO proyectoGuardado = proyectoService.agregarProyecto(proyectosDTO);
        if (proyectoGuardado != null) {
            Map<String, Object> responseMap = new HashMap<>();
            responseMap.put("proyecto", proyectoGuardado);

            String message = (proyectosDTO.getId() != null) ? "proyecto actualizado con éxito" : "proyecto guardado con éxito";
            responseMap.put("message", message);

            return ResponseEntity.ok(responseMap);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping(value = FreeSocialConstants.GET_PROYECTO)
    public ResponseEntity<List<ProyectosDTO>> obtenerExperienciaDelUsuario() {
        List<ProyectosDTO> proyectos = proyectoService.obtenerProyectosDelUsuario();
        return ResponseEntity.ok(proyectos);
    }

    @GetMapping(value = FreeSocialConstants.GET_PROYECTO + "/{userId}")
    public ResponseEntity<List<ProyectosDTO>> obtenerExperienciaDeOtroUsuario(@PathVariable Long userId) {
        List<ProyectosDTO> proyectos = proyectoService.obtenerProyectosDeOtroUsuario(userId);
        return ResponseEntity.ok(proyectos);
    }
}
