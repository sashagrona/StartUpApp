package net.bigmir.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "chats")
public class Chat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String chatName;

    @ManyToMany(mappedBy = "chats", cascade = {CascadeType.MERGE,CascadeType.REFRESH, CascadeType.PERSIST,CascadeType.DETACH})
    private Set<SimpleUser> users = new HashSet<>();


    @OneToMany(mappedBy = "chat", cascade = {CascadeType.MERGE,CascadeType.REFRESH, CascadeType.PERSIST,CascadeType.DETACH})
    private List<ChatMessage> messages = new ArrayList<>();

    public Chat(String chatName) {
        this.chatName = chatName;
    }
}
