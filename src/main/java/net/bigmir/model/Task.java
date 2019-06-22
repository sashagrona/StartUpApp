package net.bigmir.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import net.bigmir.dto.TaskDTO;
import org.hibernate.annotations.Type;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@Table(name = "tasks")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;

    @Type(type = "yes_no")
    private Boolean done;

    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.REFRESH, CascadeType.PERSIST, CascadeType.DETACH})
    @JoinColumn(name = "business_plan_id")
    private BusinessPlan plan;

    private Integer priority;

    public Task(String name, String description, Boolean done, BusinessPlan plan, Integer priority) {
        this.name = name;
        this.description = description;
        this.done = done;
        this.plan = plan;
        this.priority = priority;
    }

    public static Task of(String name, String description, Boolean done, BusinessPlan plan, Integer priority) {
        return new Task(name, description, done, plan, priority);
    }

    public TaskDTO toDTO() {
        return TaskDTO.of(id,name, description, done, priority);
    }

}
