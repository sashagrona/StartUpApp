package net.bigmir.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.bigmir.model.Chat;
import net.bigmir.model.ChatMessage;
import net.bigmir.model.TypeMessage;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
public class ChatMessageDTO {
    private TypeMessage type;
    private String content;
    private SimpleUserDTO sender;
    private String chat;

    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="dd/MM/yyyy HH:mm:ss")
    private Date date;

    private String dateFormat;

    public ChatMessageDTO(TypeMessage type, String content, SimpleUserDTO sender, String chat, Date date, String dateFormat) {
        this.type = type;
        this.content = content;
        this.sender = sender;
        this.chat = chat;
        this.date=date;
        this.dateFormat=dateFormat;
    }

    public static ChatMessageDTO of(TypeMessage type, String content, SimpleUserDTO sender, String chat, Date date, String dateFormat){
        return new ChatMessageDTO(type,content,sender, chat, date, dateFormat);
    }


}