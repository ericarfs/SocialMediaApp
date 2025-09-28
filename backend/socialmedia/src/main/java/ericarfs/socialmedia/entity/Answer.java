package ericarfs.socialmedia.entity;

import java.io.Serializable;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
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
@Table(name = "answers")
public class Answer implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id", nullable = false)
    private Question question;

    @Column(nullable = false, length = 2048)
    private String body;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false)
    private User author;

    @ManyToMany
    private Set<User> likes = new HashSet<>();

    @OneToMany(mappedBy = "answer")
    private List<Share> shares = new ArrayList<>();

    @OneToMany(mappedBy = "inResponseToAnswer")
    private List<Question> relatedQuestions = new ArrayList<>();

    @CreatedDate
    private Instant createdAt = Instant.now();

    @LastModifiedDate
    private Instant updatedAt;

    public int getLikesCount() {
        return this.getLikes().size();
    }

    public int getSharesCount() {
        return this.getShares().size();
    }

    public boolean hasUserLiked(User user) {
        return this.likes.contains(user);
    }

    public boolean hasUserShared(User user) {
        return this.shares.stream()
                .anyMatch(share -> share.getUser().equals(user));
    }

    public void toggleLike(User userThatLiked) {
        Set<User> likesCopy = new HashSet<>(this.likes);

        if (likesCopy.contains(userThatLiked)) {
            likesCopy.remove(userThatLiked);
        } else {
            likesCopy.add(userThatLiked);
        }

        this.likes = likesCopy;
    }
}
