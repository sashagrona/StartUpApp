package net.bigmir.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import net.bigmir.model.Task;

@Getter
@Setter
public class TaskDTO {

    private Long id;
    private String name;
    private String description;
    private Boolean done;
    private Integer priority;

    @JsonCreator
    public TaskDTO(@JsonProperty Long id,
                   @JsonProperty(required = true) String name,
                   @JsonProperty(required = true) String description,
                   @JsonProperty Boolean done,
                   @JsonProperty(required = true) Integer priority) {
        this.name = name;
        this.description = description;
        this.done = done;
        this.priority = priority;
        this.id=id;
    }

    public static TaskDTO of(Long id, String name, String description, Boolean done, Integer priority){
        return new TaskDTO(id,name,description,done,priority);
    }

    public Task fromDTO() {
        return Task.of(name,description,done,null, priority);
    }
}
