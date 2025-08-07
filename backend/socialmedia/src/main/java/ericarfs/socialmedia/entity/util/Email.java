package ericarfs.socialmedia.entity.util;

import java.util.regex.Pattern;

public class Email {
    private String email;

    private static final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
            + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

    private static final Pattern pattern = Pattern.compile(EMAIL_PATTERN, Pattern.CASE_INSENSITIVE);

    public Email(String email) {
        validateEmail(email);
        this.setEmail(email);
    }

    private void validateEmail(String email) {
        if (pattern.matcher(email).matches() == false)
            throw new IllegalArgumentException("Email is not valid!");
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
