package net.bigmir.dto.result;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class ResultDTO {
    protected String description = "OK";

    public ResultDTO() {
    }

    public ResultDTO(String description) {
        this.description = description;
    }

}
