package com.jt.tours.domain;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

/**
 * Difficulty Converter converts from enum to string and vice versa.
 *
 * Created by Jason Tao on 5/31/2020
 */
@Converter(autoApply = true)
public class DifficultyConverter implements AttributeConverter<DifficultyEnum, String> {
    @Override
    public String convertToDatabaseColumn(DifficultyEnum difficulty) {
        return difficulty.getLabel();
    }

    @Override
    public DifficultyEnum convertToEntityAttribute(String s) {
        return DifficultyEnum.findByLabel(s);
    }
}