package com.FreeSocial.com.Service.MensajeriaService;

import com.FreeSocial.com.V.O.DTO.ChatDTO;
import com.FreeSocial.com.V.O.DTO.MensajeDTO;
import com.FreeSocial.com.V.O.DTO.MensajeEnvioDTO;


import java.util.List;

public interface MensajeriaService {
    public MensajeDTO enviarMensaje(MensajeEnvioDTO mensajeEnvioDTO);

    public List<ChatDTO> getAllChatsUserLogged();

    public List<MensajeDTO> obtenerMensajesPorChatId(Long chatId);

    public boolean marcarMensajeComoLeido(Long mensajeId);

    public boolean eliminarMensaje(Long mensajeId) throws Exception;


}
