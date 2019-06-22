package net.bigmir.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import net.bigmir.model.SimpleUser;

@Getter
@Setter
public class StartUpDTO {
    private String name;
    private String idea;
    private SimpleUser creator;

    @JsonCreator
    public StartUpDTO(@JsonProperty(required = true) String name,
                      @JsonProperty(required = true) String idea) {
        this.name = name;
        this.idea = idea;
    }

    public static StartUpDTO of(String name, String idea){

        return new StartUpDTO(name,idea);
    }
}
