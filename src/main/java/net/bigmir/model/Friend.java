package net.bigmir.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "friends")
public class Friend {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToMany(mappedBy = "friends", cascade = {CascadeType.MERGE,CascadeType.REFRESH, CascadeType.PERSIST,CascadeType.DETACH})
    private Set<SimpleUser> users = new HashSet<>();


    private String login;

    @Column(unique = true)
    private String email;
    private String pictureURL;
    private String phone;

    public Friend(SimpleUser user) {
        this.login=user.getLogin();
        this.email=user.getEmail();
        this.pictureURL=user.getPictureURL();
        this.phone=user.getPhone();
    }
}
