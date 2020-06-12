package com.jt.tours.security.domain;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

/**
 * AuthUserGroupEnum Converter converts from enum to string and vice versa.
 *
 * Created by Jason Tao on 6/4/2020
 */
@Converter(autoApply = true)
public class AuthUserGroupConverter implements AttributeConverter<AuthUserGroupEnum, String> {
    
    @Override
    public String convertToDatabaseColumn(AuthUserGroupEnum attribute) {
        return attribute.getLabel();
    }

    @Override
    public AuthUserGroupEnum convertToEntityAttribute(String label) {
        return AuthUserGroupEnum.findByLabel(label);
    }
}
