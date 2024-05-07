package com.FreeSocial.com.Controller.UsuarioController;

import com.FreeSocial.com.Exception.FreeSocialException;
import com.FreeSocial.com.Repository.UsuarioRepository;
import com.FreeSocial.com.V.O.DTO.LoginRequest;
import com.FreeSocial.com.V.O.DTO.UsuarioDTO;
import com.FreeSocial.com.V.O.Entity.UsuarioEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
import org.springframework.stereotype.Component;

@Component
public class UsuarioValidador implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return UsuarioDTO.class.equals(clazz);
    }

    @Autowired
    UsuarioRepository usuarioRepository;

    @Override
    public void validate(Object target, Errors errors) {
        UsuarioDTO usuarioDTO = (UsuarioDTO) target;

        if (usuarioDTO.getEmail() == null || usuarioDTO.getEmail().trim().isEmpty()) {
            errors.reject("error.username");
        }

        if (usuarioDTO.getPassword() == null || usuarioDTO.getPassword().trim().isEmpty()) {
            errors.reject("error.password");
        }
    }


    public void validateLogin(Object target,Errors errors){
        LoginRequest loginRequest = (LoginRequest) target;

        UsuarioEntity user = usuarioRepository.findByEmail(loginRequest.getEmail());

        if (user == null) {
            errors.reject("error.email.mismatch");
        }

    }

    public void validatePassword(String password, String repetirPassword, Errors errors) {
        if (password == null || password.trim().isEmpty()) {
            errors.reject("error.password");
        }

        if (!password.equals(repetirPassword)) {
            errors.reject("error.password.mismatch");
        }
    }

    public void validateEmail(String email, Errors errors) {

        if(email.isEmpty()){
            errors.reject("error.email.notFoundRecover");
        }else {

            UsuarioEntity user = usuarioRepository.findByEmail(email);

            if (user == null) {
                errors.reject("error.email.mismatch");
            }
        }
    }
}
