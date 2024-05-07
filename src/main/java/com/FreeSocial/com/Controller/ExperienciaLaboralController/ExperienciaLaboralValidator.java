package com.FreeSocial.com.Controller.ExperienciaLaboralController;

import com.FreeSocial.com.Service.ExperienciaLaboralService.ExperienciaLaboralService;
import com.FreeSocial.com.V.O.DTO.ExperienciaLaboralDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

@Component
public class ExperienciaLaboralValidator implements Validator {

    @Autowired
    private ExperienciaLaboralService experienciaLaboralService;

    @Override
    public boolean supports(Class<?> clazz) {
        return false;
    }

    @Override
    public void validate(Object target, Errors errors) {
        ExperienciaLaboralDTO experienciaDTO = (ExperienciaLaboralDTO) target;

        // Obtiene las experiencias actuales del usuario
        List<ExperienciaLaboralDTO> experienciasExistentes = experienciaLaboralService.obtenerExperienciaDelUsuario();

        // Verifica si hay alguna experiencia con fechaFin en null (actual)
        boolean existeActual = experienciasExistentes.stream()
                .anyMatch(exp -> exp.getFechaFin() == null);


        // Verifica que la fecha de inicio no sea posterior a la fecha actual para experiencias marcadas como actuales
        if (experienciaDTO.getFechaFin() == null) {
            // Si la fecha de inicio es posterior a la fecha actual, es incoherente
            if (experienciaDTO.getFechaInicio().after(new Date())) {
                errors.reject("error.fechaInicio.futura");

            }
        }


        if (experienciaDTO.getFechaFin() != null) {
            // Comprobando que fechaFin no sea futura a la fecha actual
            if (experienciaDTO.getFechaFin().after(new Date())) {
                errors.reject("error.fechaFin.futura");
                errors.rejectValue("fechaFin", "error.fechaFin.futura", "La fecha de fin no puede ser futura a la fecha actual.");
            }
        }
    }
}

