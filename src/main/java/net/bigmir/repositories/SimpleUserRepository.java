package net.bigmir.repositories;

import net.bigmir.model.Chat;
import net.bigmir.model.Friend;
import net.bigmir.model.SimpleUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface SimpleUserRepository extends JpaRepository<SimpleUser,Long> {

    SimpleUser findByEmail(String email);

    @Query("SELECT CASE when COUNT(u)>0 THEN TRUE ELSE FALSE " + "END FROM SimpleUser u WHERE u.email= :email")
    boolean existsByEmail(@Param("email") String email);

    @Query("SELECT u.email FROM SimpleUser u")
    List<String> findAllEmails();

    @Query("SELECT f FROM Friend f JOIN f.users u WHERE u=:user")
    Set<Friend> findFriends(@Param("user") SimpleUser user);

    @Query("SELECT u FROM SimpleUser u JOIN u.chats c WHERE c=:chat")
    List<SimpleUser> getUsersFromChat(@Param("chat") Chat chat);

    @Query("SELECT s FROM SimpleUser s WHERE s.login LIKE %:word%")
    List<SimpleUser> getUsersLike(@Param("word") String word);




}
