package ericarfs.socialmedia.entity;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import com.fasterxml.jackson.annotation.JsonIgnore;

import ericarfs.socialmedia.entity.enums.Role;
import ericarfs.socialmedia.entity.util.Email;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
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
    private List<User> following = new ArrayList<>();

    @JsonIgnore
    @ManyToMany(mappedBy = "following")
    private List<User> followers = new ArrayList<>();

    @ManyToMany
    private List<User> blockedUsers = new ArrayList<>();

    @ManyToMany
    private List<User> silencedUsers = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "sentBy")
    private List<Question> sentQuestions = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "sentTo")
    private List<Question> receivedQuestions = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "author")
    private List<Answer> answeredQuestions = new ArrayList<>();

    private String questionHelper = "Ask me anything!";

    private boolean allowAnonQuestions = true;

    private boolean isVerified = false;

    private String role;

    @CreatedDate
    private Instant createdAt;

    @LastModifiedDate
    private Instant updatedAt;

    private Instant lastLogin;

    public Role getRole() {
        return Role.valueOf(role);
    }

    public void setRole(Role role) {
        this.role = role.name();
    }
}
