package com.FreeSocial.com.Utils;

import com.FreeSocial.com.V.O.DTO.ErrorResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.filter.OncePerRequestFilter;

import java.util.HashSet;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;

@Component
public class ValidadorUtils {

    @Autowired
    private MessageSource messageSource;
    public ResponseEntity<Object> obtenerResponseErroresValidacion(BindingResult result) {
        ErrorResponse errorResponse = new ErrorResponse();
        Set<String> errores = new HashSet<>();

        result.getAllErrors().stream().forEach(e -> errores.addAll(obtenerValorKeyMessague(e.getCode(),e.getArguments())));

        errorResponse.setMessages(errores);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    public Set<String> obtenerValorKeyMessague(String codigo) {
        return this.obtenerValorKeyMessague(codigo, null);
    }

    public Set<String> obtenerValorKeyMessague(String codigo, Object[] args) {
        Set<String> mensajes = new HashSet<>();

        String mensaje = messageSource.getMessage(codigo, args, LocaleContextHolder.getLocale());
        mensajes.add(mensaje);

        return mensajes;
    }

    /**
     * Se encarga de ver si hay valores nulos
     * @param setter
     * @param value
     * @param <T>
     */
    public static <T> void setIfNotNull(Consumer<T> setter, T value) {
        Optional.ofNullable(value).ifPresent(setter);
    }
}
