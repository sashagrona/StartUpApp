package net.bigmir.repositories;

import net.bigmir.model.Friend;
import net.bigmir.model.SimpleUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface FriendRepository extends JpaRepository<Friend, Long> {

    @Modifying
    @Query("DELETE FROM Friend f WHERE f.email=:email AND f in (SELECT f FROM Friend f JOIN f.users u WHERE u=:user)")
    void deleteFriendByUser(@Param("email") String email, @Param("user") SimpleUser user);

    @Query("SELECT CASE when COUNT(f)>0 THEN TRUE ELSE FALSE END FROM Friend f WHERE f.email=:email")
    boolean findByEmail(@Param("email") String email);

    Friend findFriendByEmail(String email);


}
