package com.FreeSocial.com.TareasAutomatizadas;

import com.FreeSocial.com.Repository.UsuarioRepository;
import com.FreeSocial.com.V.O.Entity.UsuarioEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
public class Automatizacion {

    @Autowired
    private UsuarioRepository usuarioRepository;




    @Scheduled(cron = "0 39 0 * * ?")
    public void eliminarCodigosExpirados() {
        System.out.println("Se ejecuta la automatizacion de los codigos");
        List<UsuarioEntity> usuariosConCodigosExpirados = usuarioRepository.findUsuariosConCodigosExpirados(new Date());
        for (UsuarioEntity usuario : usuariosConCodigosExpirados) {
            usuario.setCodigoRecuperacion(null);
            usuario.setFechaExpiracionCodigoRecuperacion(null);
            usuarioRepository.save(usuario);
        }
    }
}
