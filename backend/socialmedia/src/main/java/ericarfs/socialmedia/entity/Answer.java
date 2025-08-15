package ericarfs.socialmedia.entity;

import java.time.Duration;
import java.time.Instant;
import java.util.HashSet;
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
public class Answer {
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

    @ManyToMany
    private Set<User> shares = new HashSet<>();

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
        return this.shares.contains(user);
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

    public void toggleShare(User userThatShared) {
        Set<User> sharesCopy = new HashSet<>(this.shares);

        if (sharesCopy.contains(userThatShared)) {
            sharesCopy.remove(userThatShared);
        } else {
            sharesCopy.add(userThatShared);
        }

        this.shares = sharesCopy;
    }

    public String getFormattedCreationDate() {
        Instant timeNow = Instant.now();
        Instant timeCreation = this.createdAt;

        Duration timeDifference = Duration.between(timeCreation, timeNow);
        Long timeDifferenceDays = timeDifference.toDays();
        Long timeDifferenceSeconds = timeDifference.toSeconds();
        Long timeDifferenceMinutes = timeDifference.toMinutes();
        Long timeDifferenceHours = timeDifference.toHours();

        if (timeDifferenceDays >= 365) {
            int years = (int) Math.floor(timeDifferenceDays / 365);
            if (years == 1)
                return "1 year ago.";
            return years + " years ago.";
        }

        if (timeDifferenceDays >= 30) {
            int months = (int) Math.floor(timeDifferenceDays / 30);
            if (months == 1)
                return "1 month ago.";
            return months + " months ago.";
        }

        if (timeDifferenceDays > 0) {
            if (timeDifferenceDays == 1)
                return "1 day ago.";
            return timeDifferenceDays + " days ago.";
        }

        if (timeDifferenceHours > 0) {
            if (timeDifferenceHours == 1)
                return "1 hour ago.";
            return timeDifferenceHours + " hours ago.";
        }

        if (timeDifferenceMinutes > 0) {
            if (timeDifferenceMinutes == 1)
                return "1 minute ago.";
            return timeDifferenceMinutes + " minutes ago.";
        }

        if (timeDifferenceSeconds == 1) {
            return "1 second ago.";
        }

        return timeDifferenceSeconds + " seconds ago";
    }
}
