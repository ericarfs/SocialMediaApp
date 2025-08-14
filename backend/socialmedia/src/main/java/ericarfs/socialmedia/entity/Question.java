package ericarfs.socialmedia.entity;

import java.time.Duration;
import java.time.Instant;

import org.springframework.data.annotation.CreatedDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
@Table(name = "questions")
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sent_by_id", nullable = false)
    private User sentBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sent_to_id", nullable = false)
    private User sentTo;

    @Column(nullable = false, length = 1024)
    private String body = "Ask me anything!";

    private boolean isAnon = true;

    private boolean isAnswered = false;

    @CreatedDate
    private Instant createdAt = Instant.now();

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
