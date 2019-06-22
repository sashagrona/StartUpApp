package net.bigmir.repositories;

import net.bigmir.model.SimpleUser;
import net.bigmir.model.StartUp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface StartUpRepository extends JpaRepository<StartUp, Long> {

    @Query("SELECT s FROM StartUp s WHERE s.name=:name")
    StartUp findByName(@Param(value = "name") String name);

    boolean existsByName(String name);

    @Query("SELECT s FROM StartUp s JOIN s.users u WHERE u=:user")
    Set<StartUp> findAllByUser(@Param(value = "user") SimpleUser user);

    @Modifying
    @Query("DELETE FROM StartUp s WHERE s=:startup")
    void deleteStartup(@Param(value = "startup") StartUp startUp);


    @Modifying
    @Query(nativeQuery = true, value = "DELETE FROM users_startups WHERE user_id=? AND startup_id=?")
    void deleteStartUpFromUser(Long user, Long startup);

}
