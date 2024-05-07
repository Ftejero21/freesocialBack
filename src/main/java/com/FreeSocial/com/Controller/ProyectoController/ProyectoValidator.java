package com.FreeSocial.com.Controller.ProyectoController;

import com.FreeSocial.com.V.O.DTO.ProyectosDTO;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class ProyectoValidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        return false;
    }

    @Override
    public void validate(Object target, Errors errors) {
        ProyectosDTO proyectoDTO = (ProyectosDTO) target;

        if (proyectoDTO.getLink() != null && !proyectoDTO.getLink().isEmpty()) {
            if (!validarUrl(proyectoDTO.getLink())) {
                errors.reject("link.invalid");
            }
        }

    }

    private boolean validarUrl(String url) {
        // Expresión regular para validar URLs
        String regex = "^(http:\\/\\/www\\.|https:\\/\\/www\\.|http:\\/\\/|https:\\/\\/)?[a-z0-9]+([\\-\\.]{1}[a-z0-9]+)*\\.[a-z]{2,5}(:[0-9]{1,5})?(\\/.*)?$";

        // Compila la expresión regular
        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);

        // Compara la URL con la expresión regular
        Matcher matcher = pattern.matcher(url);

        // Devuelve true si la URL coincide con la expresión regular
        return matcher.find();
    }
}
