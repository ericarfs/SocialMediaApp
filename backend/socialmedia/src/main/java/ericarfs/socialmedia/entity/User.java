package ericarfs.socialmedia.entity;

import java.time.Instant;
import java.util.List;

import ericarfs.socialmedia.entity.util.Email;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(unique = true, nullable = false)
    private Email email;

    @Column(nullable = false)
    private String password;

    private String icon;

    @ManyToMany
    private List<User> following;

    @ManyToMany
    private List<User> blockedUsers;

    @ManyToMany
    private List<User> silencedUsers;

    @OneToMany(mappedBy = "sentBy")
    private List<Question> sentQuestions;

    @OneToMany(mappedBy = "sentTo")
    private List<Question> receivedQuestions;

    @OneToMany(mappedBy = "author")
    private List<Answer> answeredQuestions;

    private String questionHelper = "Ask me anything!";

    private Boolean allowAnonQuestions = true;

    private Boolean isVerified = false;

    private Instant createdAt = Instant.now();

    private Instant updatedAt;

    private Instant lastLogin;

    @PrePersist
    public void prePersist() {
        createdAt = Instant.now();
        if (allowAnonQuestions == null) {
            allowAnonQuestions = true;
        }
        if (isVerified == null) {
            isVerified = false;
        }
    }
}
