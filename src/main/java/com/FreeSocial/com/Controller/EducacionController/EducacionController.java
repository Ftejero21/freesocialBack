package com.FreeSocial.com.Controller.EducacionController;


import com.FreeSocial.com.Service.EducacionService.EducacionService;
import com.FreeSocial.com.Utils.FreeSocialConstants;
import com.FreeSocial.com.Utils.ValidadorUtils;
import com.FreeSocial.com.V.O.DTO.EducacionDTO;
import lombok.Getter;
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
@RequestMapping(value = FreeSocialConstants.EDUCACION_URL)
@CrossOrigin(origins = "*")
public class EducacionController {

    @Autowired
    private EducacionValidator educacionValidator;

    @Autowired
    private ValidadorUtils validadorUtils;

    @Autowired
    private EducacionService educacionService;


    @PostMapping(value = FreeSocialConstants.SAVEORUPDATE_EDUCACION, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> guardarEducacion(@RequestBody @Validated EducacionDTO educacionDTO, BindingResult result) {


        educacionValidator.validate(educacionDTO, result);

        if (result.hasErrors()) {
            return validadorUtils.obtenerResponseErroresValidacion(result);
        }

        EducacionDTO educacionGuardada = educacionService.agregarEducacion(educacionDTO);
        if (educacionGuardada != null) {
            Map<String, Object> responseMap = new HashMap<>();
            responseMap.put("educacion", educacionGuardada);

            String message = (educacionDTO.getId() != null) ? "Educación actualizada con éxito" : "Educación guardada con éxito";
            responseMap.put("message", message);

            return ResponseEntity.ok(responseMap);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping(value = FreeSocialConstants.GET_EDUCACION)
    public ResponseEntity<List<EducacionDTO>> obtenerEducacionesDelUsuario() {
        List<EducacionDTO> educaciones = educacionService.obtenerEducacionesDelUsuario();
        // No necesitas verificar si educaciones está vacío para cambiar el comportamiento
        return ResponseEntity.ok(educaciones);
    }

    @GetMapping(value = FreeSocialConstants.GET_EDUCACION + "/{userId}")
    public ResponseEntity<List<EducacionDTO>> obtenerEducacionesDeOtroUsuario(@PathVariable Long userId) {
        List<EducacionDTO> educaciones = educacionService.obtenerEducacionesDeOtroUsuario(userId);
        return ResponseEntity.ok(educaciones);
    }

    @DeleteMapping(value = FreeSocialConstants.DELETE_EDUCACION + "/{id}")
    public ResponseEntity<?> borrarEducacion(@PathVariable Long id) throws Exception {
        educacionService.borrarEducacion(id);
        Map<String, String> responseMap = new HashMap<>();
        responseMap.put("message", "Educación eliminada con éxito.");
        return ResponseEntity.ok(responseMap);
    }





}
