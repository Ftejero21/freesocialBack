package com.FreeSocial.com.Controller.MensajeriaController;


import com.FreeSocial.com.Service.MensajeriaService.MensajeriaService;
import com.FreeSocial.com.Utils.FreeSocialConstants;
import com.FreeSocial.com.V.O.DTO.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = FreeSocialConstants.MENSAJERIA_URL)
@CrossOrigin(origins = "*")
public class MensajeriaController {


    @Autowired
    private MensajeriaService mensajeService;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;


    @PostMapping("/enviar")
    public ResponseEntity<MensajeDTO> enviarMensaje(@RequestBody MensajeEnvioDTO mensajeEnvio) {
        MensajeDTO mensajeDTO = mensajeService.enviarMensaje(mensajeEnvio);
        return ResponseEntity.ok(mensajeDTO);
    }


    @PostMapping("/escribiendo")
    public ResponseEntity<?> usuarioEscribiendo(@RequestBody EstadoEscribiendoDTO estadoEscribiendo) {
        // Suponiendo que EstadoEscribiendoDTO contiene chatId y boolean escribiendo
        messagingTemplate.convertAndSend("/topic/escribiendo." + estadoEscribiendo.getChatId(), estadoEscribiendo.isEscribiendo());
        return ResponseEntity.ok().build();
    }



    @GetMapping("/getChats")
    public ResponseEntity<List<ChatDTO>> obtenerExperienciaDelUsuario() {
        List<ChatDTO> chatDTO = mensajeService.getAllChatsUserLogged();
        return ResponseEntity.ok(chatDTO);
    }

    @GetMapping("/chat/{chatId}")
    public ResponseEntity<List<MensajeDTO>> obtenerMensajesPorChatId(@PathVariable Long chatId) {
            List<MensajeDTO> mensajes = mensajeService.obtenerMensajesPorChatId(chatId);
            return ResponseEntity.ok(mensajes); // Devuelve la lista de mensajes como un cuerpo de respuesta HTTP 200
    }

    @PostMapping("/marcarLeido/{mensajeId}")
    public ResponseEntity<Boolean> marcarMensajeComoLeido(@PathVariable Long mensajeId) {
        boolean resultado = mensajeService.marcarMensajeComoLeido(mensajeId);
        return ResponseEntity.ok(resultado);
    }

    @DeleteMapping("/deleteMensaje/{mensajeId}")
    public ResponseEntity<?> borrarMensaje(@PathVariable Long mensajeId) throws Exception {
        boolean result = mensajeService.eliminarMensaje(mensajeId);
        if (result) {
            return ResponseEntity.ok().body("Mensaje eliminado");
        } else {
            return ResponseEntity.badRequest().body("No se pudo eliminar el mensaje");
        }
    }
}
