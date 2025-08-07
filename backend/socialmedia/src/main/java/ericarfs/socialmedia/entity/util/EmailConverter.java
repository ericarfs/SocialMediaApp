package ericarfs.socialmedia.entity.util;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class EmailConverter implements AttributeConverter<Email, String> {

    @Override
    public String convertToDatabaseColumn(Email email) {
        if (email == null) {
            return null;
        }
        return email.getEmail();
    }

    @Override
    public Email convertToEntityAttribute(String emailString) {
        if (emailString == null) {
            return null;
        }
        return new Email(emailString);
    }
}