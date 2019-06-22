package net.bigmir.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import net.bigmir.dto.SimpleUserDTO;

import javax.persistence.*;
import java.util.*;

@Entity
@Data
@NoArgsConstructor
@Table(name = "users")
public class SimpleUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String login;

    @Column(unique = true)
    private String email;

    private String phone;
    private String password;
    private String pictureURL;

    private Date expiredTime;

    @Enumerated(EnumType.STRING)
    private UserRole role;

    @OneToMany(mappedBy = "creator")
    private List<StartUp> starts = new ArrayList<>();

    @OneToMany(mappedBy = "sender", cascade = {CascadeType.MERGE,CascadeType.REFRESH, CascadeType.PERSIST,CascadeType.DETACH})
    private List<ChatMessage> messages = new ArrayList<>();

    @ManyToMany()
    @JoinTable(name = "users_startups",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "startup_id"))
    private Set<StartUp> startUps = new HashSet<>();

    @ManyToMany(cascade = {CascadeType.MERGE,CascadeType.REFRESH, CascadeType.PERSIST,CascadeType.DETACH})
    @JoinTable(name = "users_friends",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "friend_id"))
    private Set<Friend> friends = new HashSet<>();


    @ManyToMany()
    @JoinTable(name = "users_chats",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "chat_id"))
    private Set<Chat> chats = new HashSet<>();

    public void addStartUpToUser(StartUp startUp) {
        startUps.add(startUp);
    }

    public void addFriend(Friend friend) {
        friends.add(friend);
    }

    public void addChat(Chat chat) {
        chats.add(chat);
    }

    public void addStartUpToCreator(StartUp startUp){
        starts.add(startUp);
    }



    public SimpleUser(String login, String email, String phone, String password, String pictureURL, UserRole role) {
        this.login = login;
        this.email = email;
        this.phone = phone;
        this.password = password;
        this.pictureURL = pictureURL;
        this.role = role;
    }

    public static SimpleUser of(String login, String email, String phone, String pictureURL, UserRole role) {
        return new SimpleUser(login, email, phone, "none", pictureURL, role);
    }

    public SimpleUserDTO toDTO() {
        return SimpleUserDTO.of(login, email, phone, pictureURL, role);
    }

    public static SimpleUser fromDTO(SimpleUserDTO simpleUserDTO) {
        return SimpleUser.of(simpleUserDTO.getLogin(), simpleUserDTO.getEmail(), simpleUserDTO.getPhone(), simpleUserDTO.getPictureURL(), simpleUserDTO.getRole());
    }
}
