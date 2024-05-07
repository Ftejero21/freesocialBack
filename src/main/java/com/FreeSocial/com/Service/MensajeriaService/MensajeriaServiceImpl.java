package com.FreeSocial.com.Service.MensajeriaService;

import com.FreeSocial.com.Config.JwtTokenUtil;
import com.FreeSocial.com.Repository.ChatRepository;
import com.FreeSocial.com.Repository.MensajeRepository;
import com.FreeSocial.com.Repository.UsuarioRepository;
import com.FreeSocial.com.Utils.CryptComponent;
import com.FreeSocial.com.Utils.Mapping;
import com.FreeSocial.com.V.O.DTO.ChatDTO;
import com.FreeSocial.com.V.O.DTO.MensajeDTO;
import com.FreeSocial.com.V.O.DTO.MensajeEnvioDTO;
import com.FreeSocial.com.V.O.Entity.ChatEntity;
import com.FreeSocial.com.V.O.Entity.Mensaje;
import com.FreeSocial.com.V.O.Entity.UsuarioEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MensajeriaServiceImpl implements MensajeriaService{


    @Autowired
    private MensajeRepository mensajeRepository;

    @Autowired
    private Mapping mapping;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private ChatRepository chatRepository;

    @Autowired
    private CryptComponent cryptComponent;


    @Override
    @Transactional
    public MensajeDTO enviarMensaje(MensajeEnvioDTO mensajeEnvioDTO) {
        String token = jwtTokenUtil.getCurrentToken();
        if (token == null) {
            throw new RuntimeException("Usuario no autenticado");
        }
        Long emisorId = jwtTokenUtil.getUserIdFromToken(token);
        UsuarioEntity emisor = usuarioRepository.findById(emisorId).orElseThrow(() -> new RuntimeException("Usuario emisor no encontrado"));
        UsuarioEntity receptor = usuarioRepository.findById(mensajeEnvioDTO.getReceptorId()).orElseThrow(() -> new RuntimeException("Usuario receptor no encontrado"));

        ChatEntity chat = chatRepository.findChatByUsers(emisor.getId(), receptor.getId());
        if (chat == null) {
            chat = new ChatEntity();
            chat.setUsuario(emisor);
            chat.setReceptor(receptor);
            chat.setFechaCreacion(LocalDateTime.now());
            chatRepository.save(chat);
        }else{
            chat.setFechaCreacion(LocalDateTime.now());
            chatRepository.save(chat);
        }

        Mensaje mensaje = new Mensaje();
        String contenidoCifrado = cryptComponent.encrypt(mensajeEnvioDTO.getContenido());
        mensaje.setContenido(contenidoCifrado);
        mensaje.setUserId(emisor.getId());
        if (mensajeEnvioDTO.getImagenMensajeBase64() != null && !mensajeEnvioDTO.getImagenMensajeBase64().isEmpty()) {
            byte[] imagenMensajeBytes = Base64.getDecoder().decode(mensajeEnvioDTO.getImagenMensajeBase64());
            mensaje.setImagenMensaje(imagenMensajeBytes);
        }
        ZoneId zoneId = ZoneId.of("Europe/Madrid");
        LocalDateTime fechaHoraMadrid = LocalDateTime.now(zoneId);
        mensaje.setFechaCreacion(fechaHoraMadrid);
        mensaje.setLeido(false);
        mensaje.setChat(chat);

        mensaje = mensajeRepository.save(mensaje);
        chat.getMensajes().add(mensaje);
        chatRepository.save(chat);

        return convertirAMensajeDTO(mensaje);
    }

    @Override
    public List<ChatDTO> getAllChatsUserLogged() {
        String token = jwtTokenUtil.getCurrentToken();
        if (token == null) {
            throw new RuntimeException("Usuario no autenticado");
        }
        Long usuarioId = jwtTokenUtil.getUserIdFromToken(token);
        UsuarioEntity usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        List<ChatEntity> chats = chatRepository.findAllChatsByUserId(usuarioId);

        return chats.stream()
                .map(chat -> {
                    ChatDTO chatDTO = new ChatDTO();
                    chatDTO.setId(chat.getId());

                    // Determinar los roles de los usuarios en el chat
                    boolean isLoggedUserEmisor = chat.getUsuario().getId().equals(usuarioId);
                    UsuarioEntity receptor = isLoggedUserEmisor ? chat.getReceptor() : chat.getUsuario();

                    chatDTO.setUserId(usuarioId);
                    chatDTO.setReceptorId(receptor.getId());

                    // Concatenar nombre y apellidos para obtener el nombre completo del receptor
                    String nombreCompletoReceptor = receptor.getDatosPersonales().getNombre() + " " + receptor.getDatosPersonales().getApellidos();
                    chatDTO.setNombreCompletoReceptor(nombreCompletoReceptor);
                    chatDTO.setTitularReceptor(receptor.getTitular());
                    if (receptor.getImagenPerfil() != null) {
                        byte[] imagenPerfilBytes = receptor.getImagenPerfil();
                        String imagenPerfilBase64 = Base64.getEncoder().encodeToString(imagenPerfilBytes);
                        chatDTO.setImagenPerfilReceptorBase64(imagenPerfilBase64);
                    } else {
                        chatDTO.setImagenPerfilReceptorBase64(null); // O manejar según sea necesario
                    }

                    // Aquí, también podrías incluir la lista de mensajes, si el DTO debe contenerlos
                    List<MensajeDTO> mensajesDTO = chat.getMensajes().stream()
                            .map(this::convertirAMensajeDTO)  // Descifra el contenido de cada mensaje
                            .collect(Collectors.toList());

                    chatDTO.setMensajes(mensajesDTO);

                    return chatDTO;
                })
                .collect(Collectors.toList());

    }

    @Override
    public List<MensajeDTO> obtenerMensajesPorChatId(Long chatId) {
        String token = jwtTokenUtil.getCurrentToken();
        if (token == null) {
            throw new RuntimeException("Usuario no autenticado");
        }
        Long usuarioId = jwtTokenUtil.getUserIdFromToken(token);
        UsuarioEntity usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Asumiendo que necesitas validar que el usuario es parte del chat o algo similar
        List<Mensaje> mensajes = mensajeRepository.findAllByChatId(chatId);
        return mensajes.stream().map(this::convertirAMensajeDTO).collect(Collectors.toList());
    }

    @Override
    public boolean marcarMensajeComoLeido(Long mensajeId) {
        Optional<Mensaje> mensajeOpt = mensajeRepository.findById(mensajeId);
        if (mensajeOpt.isPresent()) {
            Mensaje mensaje = mensajeOpt.get();
            mensaje.setLeido(true);
            mensajeRepository.save(mensaje);
            return true;
        }
        return false;
    }

    @Override
    public boolean eliminarMensaje(Long mensajeId) throws Exception{
        boolean eliminado = true;
        String token = jwtTokenUtil.getCurrentToken();
        if (token == null) {
            throw new RuntimeException("Usuario no autenticado");
        }
        Long usuarioId = jwtTokenUtil.getUserIdFromToken(token);
        UsuarioEntity usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Mensaje mensaje = mensajeRepository.findById(mensajeId)
                .orElseThrow(() -> new Exception("El mensaje con ID: " + mensajeId + " no existe."));

        if (!mensaje.getUserId().equals(usuario.getId())) {
            eliminado =  false;
            throw new RuntimeException("No tiene permiso para eliminar este mensaje");

        }

        mensajeRepository.deleteById(mensajeId);
        return eliminado;
    }

    public MensajeDTO convertirAMensajeDTO(Mensaje mensaje) {
        MensajeDTO dto = new MensajeDTO();
        dto.setId(mensaje.getId());
        String contenidoDescifrado = cryptComponent.decrypt(mensaje.getContenido());
        dto.setContenido(contenidoDescifrado);
        dto.setUserId(mensaje.getUserId());
        dto.setImagenMensaje(mensaje.getImagenMensaje());
        dto.setLeido(mensaje.getLeido());
        dto.setFechaCreacion(Mapping.calculateTimeSince(mensaje.getFechaCreacion()));
        dto.setChatId(mensaje.getChat().getId());  // Asegúrate de que el mensaje tiene una referencia al Chat.
        return dto;
    }

}
