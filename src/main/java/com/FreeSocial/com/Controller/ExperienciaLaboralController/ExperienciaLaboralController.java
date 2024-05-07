package com.FreeSocial.com.Controller.ExperienciaLaboralController;

import com.FreeSocial.com.Service.ExperienciaLaboralService.ExperienciaLaboralService;
import com.FreeSocial.com.Utils.FreeSocialConstants;
import com.FreeSocial.com.Utils.ValidadorUtils;
import com.FreeSocial.com.V.O.DTO.EducacionDTO;
import com.FreeSocial.com.V.O.DTO.ExperienciaLaboralDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping(value = FreeSocialConstants.EXPERIENCIA_URL)
@CrossOrigin(origins = "*")
public class ExperienciaLaboralController {

    @Autowired
    private ExperienciaLaboralService experienciaLaboralService;

    @Autowired
    private ValidadorUtils validadorUtils;

    @Autowired
    private ExperienciaLaboralValidator experienciaLaboralValidator;

    @PostMapping(value = FreeSocialConstants.SAVEORUPDATE_EXPERIENCIA, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> guardarExperiencia(@RequestBody @Validated ExperienciaLaboralDTO experienciaDTO, BindingResult result) {

        experienciaLaboralValidator.validate(experienciaDTO, result);

        if (result.hasErrors()) {
            return validadorUtils.obtenerResponseErroresValidacion(result);
        }

        ExperienciaLaboralDTO experienciaGuardada = experienciaLaboralService.agregarExperiencia(experienciaDTO);
        if (experienciaGuardada != null) {
            Map<String, Object> responseMap = new HashMap<>();
            responseMap.put("experiencia", experienciaGuardada);

            String message = (experienciaDTO.getId() != null) ? "Experiencia actualizada con éxito" : "Experiencia guardada con éxito";
            responseMap.put("message", message);

            return ResponseEntity.ok(responseMap);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping(value = FreeSocialConstants.GET_EXPERIENCIA)
    public ResponseEntity<List<ExperienciaLaboralDTO>> obtenerExperienciaDelUsuario() {
        List<ExperienciaLaboralDTO> experiencias = experienciaLaboralService.obtenerExperienciaDelUsuario();
        return ResponseEntity.ok(experiencias);
    }

    @GetMapping(value = FreeSocialConstants.GET_EXPERIENCIA + "/{userId}")
    public ResponseEntity<List<ExperienciaLaboralDTO>> obtenerExperienciaDeOtroUsuario(@PathVariable Long userId) {
        List<ExperienciaLaboralDTO> experiencias = experienciaLaboralService.obtenerExperienciaDeOtroUsuario(userId);
        return ResponseEntity.ok(experiencias);
    }

    @DeleteMapping(value = FreeSocialConstants.DELETE_EXPERIENCIA + "/{id}")
    public ResponseEntity<?> borrarExperiencia(@PathVariable Long id) throws Exception {
        experienciaLaboralService.borrarExperiencia(id);
        Map<String, String> responseMap = new HashMap<>();
        responseMap.put("message", "Experiencia eliminada con éxito.");
        return ResponseEntity.ok(responseMap);
    }

    @GetMapping(value = FreeSocialConstants.GET_TIEMPO_TOTAL_TRABAJADO)
    public ResponseEntity<Map<String, String>> obtenerTiempoTotalTrabajado() {
        Map<String, String> response = new HashMap<>();
        response.put("tiempoTotalTrabajado", experienciaLaboralService.obtenerTiempoTotalTrabajadoDelUsuario());
        return ResponseEntity.ok(response);
    }
}
