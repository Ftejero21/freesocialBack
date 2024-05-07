package com.FreeSocial.com.Controller.EducacionController;

import com.FreeSocial.com.V.O.DTO.EducacionDTO;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class EducacionValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return false;
    }

    @Override
    public void validate(Object target, Errors errors) {
        EducacionDTO educacionDTO = (EducacionDTO) target; // Asumiendo que estás validando un objeto de tipo EducacionDTO

        if (educacionDTO.getInstitucion() == null || educacionDTO.getInstitucion().trim().isEmpty()) {
            errors.reject("error.username");
        }

        if (educacionDTO.getFechaInicio() == null) {
            errors.reject("error.username");
        }

        if (educacionDTO.getFechaFin() == null) {
            errors.reject("error.username");
        }

        // Validación de nota: ajusta según tus necesidades (por ejemplo, rango válido de notas)
        if (educacionDTO.getNota() < 0 || educacionDTO.getNota() > 10) { // Asumiendo que la nota es entre 0 y 10
            errors.reject("error.username");
        }

        if (educacionDTO.getTitulo() == null || educacionDTO.getTitulo().trim().isEmpty()) {
            errors.reject("error.username");
        }
    }
}
