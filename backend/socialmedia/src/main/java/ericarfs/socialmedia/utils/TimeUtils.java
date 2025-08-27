package ericarfs.socialmedia.utils;

import java.time.Duration;
import java.time.Instant;

public final class TimeUtils {
    private TimeUtils() {
    }

    public static String getFormattedCreationDate(Instant timeCreation) {
        Instant timeNow = Instant.now();

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
