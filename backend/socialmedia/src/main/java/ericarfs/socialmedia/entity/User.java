package ericarfs.socialmedia.entity;

import java.io.Serializable;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonIgnore;

import ericarfs.socialmedia.entity.enums.Role;
import ericarfs.socialmedia.entity.util.Email;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;
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
@EntityListeners(AuditingEntityListener.class)
@Table(name = "users")
public class User implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 48)
    private String displayName;

    @Column(unique = true, nullable = false, length = 20)
    @Size(min = 4, message = "Username must have at least 4 characters.")
    @Size(max = 20, message = "Username must have up to 20 characters.")
    private String username;

    @Column(unique = true, nullable = false)
    private Email email;

    @Column(nullable = false)
    private String password;

    private String icon;

    @Column(length = 200)
    private String bio;

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

    @JsonIgnore
    @OneToOne(mappedBy = "user")
    private ResetToken resetToken;

    public int getFollowingCount() {
        return this.getFollowing().size();
    }

    public int getFollowersCount() {
        return this.getFollowers().size();
    }

    public int getAnswersCount() {
        return this.getAnsweredQuestions().size();
    }

    public Role getRole() {
        return Role.valueOf(role);
    }

    public void setRole(Role role) {
        this.role = role.name();
    }

    public void follow(User userToFollow) {
        if (!new HashSet<>(this.following).contains(userToFollow)) {
            this.following.add(userToFollow);
        }
    }

    public void unfollow(User userToUnfollow) {
        if (new HashSet<>(this.following).contains(userToUnfollow)) {
            this.following.remove(userToUnfollow);
        }
    }

    public void block(User userToBlock) {
        if (!new HashSet<>(this.blockedUsers).contains(userToBlock)) {
            this.blockedUsers.add(userToBlock);
            this.following.remove(userToBlock);
            this.followers.remove(userToBlock);
        }
    }

    public void unblock(User userToBlock) {
        if (new HashSet<>(this.blockedUsers).contains(userToBlock)) {
            this.blockedUsers.remove(userToBlock);
        }
    }

    public void silence(User userToSilence) {
        if (!new HashSet<>(this.silencedUsers).contains(userToSilence)) {
            this.silencedUsers.add(userToSilence);
        }
    }

    public void unsilence(User userToSilence) {
        if (new HashSet<>(this.silencedUsers).contains(userToSilence)) {
            this.silencedUsers.remove(userToSilence);
        }
    }
}
