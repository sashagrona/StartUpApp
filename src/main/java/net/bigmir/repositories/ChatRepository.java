package net.bigmir.repositories;

import net.bigmir.model.Chat;
import net.bigmir.model.SimpleUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface ChatRepository extends JpaRepository<Chat, Long> {

    @Query("SELECT c from Chat c WHERE c.chatName=:name")
    Chat getChatByName(@Param("name") String name);

    @Query("SELECT CASE when COUNT(c)>0 THEN TRUE ELSE FALSE END FROM Chat c WHERE c.chatName=:chatName")
    boolean ifChatExists(@Param("chatName") String chatName);

    @Query("SELECT c FROM Chat c JOIN c.users u WHERE u=:user")
    Set<Chat> getMyChats(@Param("user")SimpleUser user);

    @Modifying
    @Query("DELETE FROM Chat c WHERE c.chatName=:chatName")
    void deleteChatByName(@Param("chatName") String chatName);

    @Modifying
    @Query(value = "DELETE FROM users_chats WHERE user_id=? AND chat_id=?", nativeQuery = true)
    void deleteChatForUser(Long user, Long chat);

}
