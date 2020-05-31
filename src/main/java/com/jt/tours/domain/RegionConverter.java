package com.jt.tours.domain;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

/**
 * Region converter converts from enum to string and vice versa.
 *
 * Created by Jason Tao on 5/31/2020
 */
@Converter(autoApply = true)
public class RegionConverter implements AttributeConverter<RegionEnum, String> {
    @Override
    public String convertToDatabaseColumn(RegionEnum region) {
        return region.getLabel();
    }

    @Override
    public RegionEnum convertToEntityAttribute(String s) {
        return RegionEnum.findByLabel(s);
    }
}