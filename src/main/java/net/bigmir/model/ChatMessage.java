package net.bigmir.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.bigmir.dto.ChatMessageDTO;

import javax.persistence.*;
import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "messages")
public class ChatMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private TypeMessage type;

    private String content;

    @ManyToOne(cascade = { CascadeType.MERGE,
            CascadeType.REFRESH})
    @JoinColumn(name = "user_id")
    private SimpleUser sender;


    @ManyToOne(cascade = { CascadeType.MERGE,
            CascadeType.REFRESH})
    @JoinColumn(name = "chat_id")
    private Chat chat;


    @Temporal(value = TemporalType.TIMESTAMP)
    private Date date;

    @Transient
    private String dateFormat;

    public ChatMessage(TypeMessage type, String content, SimpleUser sender, Chat chat, Date date) {
        this.type = type;
        this.content = content;
        this.sender = sender;
        this.chat = chat;
        this.date=date;
    }

    public ChatMessageDTO toDTO() {
        return ChatMessageDTO.of(type,content,sender.toDTO(), chat.getChatName(), date, dateFormat);
    }

    public static ChatMessage of(TypeMessage type, String content, SimpleUser sender, Chat chat, Date date) {
        return new ChatMessage(type,content,sender, chat, date);
    }

}
