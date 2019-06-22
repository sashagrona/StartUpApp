package net.bigmir.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@Table(name = "password_tokens")
public class PasswordResetToken {
    private static final int EXPIRATION = 60 * 24;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String passToken;

    @OneToOne(targetEntity = SimpleUser.class, fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, name = "user_id")
    private SimpleUser simpleUser;

    @Temporal(value = TemporalType.TIMESTAMP)
    private Date expireDate;

    public PasswordResetToken(String passToken, SimpleUser simpleUser) {
        this.passToken = passToken;
        this.simpleUser = simpleUser;
    }

    public long getExpireTime() {
        return expireDate.getTime() + EXPIRATION * 60*1000;
    }
}
