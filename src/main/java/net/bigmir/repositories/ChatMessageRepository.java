package net.bigmir.repositories;

import net.bigmir.model.Chat;
import net.bigmir.model.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {

    @Query("SELECT m FROM ChatMessage m WHERE m.chat=:chat")
    List<ChatMessage> getMessagesFromChat(@Param("chat") Chat chat);

    @Modifying
    @Query("DELETE FROM ChatMessage c WHERE c.chat=:chat")
    void deleteChatMessageByChat(@Param("chat") Chat chat);


}
