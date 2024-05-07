package com.FreeSocial.com.Repository;

import com.FreeSocial.com.V.O.Entity.Mensaje;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MensajeRepository extends JpaRepository<Mensaje,Long> {

    List<Mensaje> findAllByChatId(Long chatId);

    @Modifying
    @Query("UPDATE Mensaje m SET m.leido = true WHERE m.id = :mensajeId")
    int marcarComoLeido(Long mensajeId);
}
