package net.bigmir.repositories;

import net.bigmir.model.BusinessPlan;
import net.bigmir.model.StartUp;
import net.bigmir.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task,Long> {

    @Query("SELECT t FROM Task t WHERE t.plan=:plan ORDER BY t.priority ASC")
    List<Task> getTasksFromPlan(@Param("plan") BusinessPlan plan);

    @Modifying
    @Query("DELETE FROM Task t WHERE t.plan=:plan")
    void deleteTasksByStartUp(@Param("plan") BusinessPlan plan);

    @Modifying
    @Query("DELETE FROM Task t where t.id IN(:ids)")
    void deleteTasksById(@Param("ids") List<Long> ids);

    @Modifying
    @Query("UPDATE Task t SET t.done=true WHERE t.id in (:ids)")
    void matchAsDone(@Param("ids") List<Long> ids);
}
