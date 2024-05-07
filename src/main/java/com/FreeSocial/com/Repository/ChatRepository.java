package com.FreeSocial.com.Repository;

import com.FreeSocial.com.V.O.Entity.ChatEntity;
import com.FreeSocial.com.V.O.Entity.UsuarioEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface  ChatRepository extends JpaRepository<ChatEntity,Long> {



    @Query("SELECT c FROM ChatEntity c WHERE (c.usuario.id = :emisorId AND c.receptor.id = :receptorId) OR (c.usuario.id = :receptorId AND c.receptor.id = :emisorId)")
    ChatEntity findChatByUsers(Long emisorId, Long receptorId);

    @Query(value = "SELECT * FROM chats WHERE usuario_id = :userId OR receptor_id = :userId order by fecha_creacion desc", nativeQuery = true)
    public List<ChatEntity> findAllChatsByUserId(@Param("userId") Long userId);
}
