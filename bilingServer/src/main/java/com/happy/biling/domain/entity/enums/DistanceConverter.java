package com.happy.biling.domain.entity.enums;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class DistanceConverter implements AttributeConverter<Distance, String> {

    @Override
    public String convertToDatabaseColumn(Distance distance) {
        return distance != null ? distance.getValue() : null;
    }

    @Override
    public Distance convertToEntityAttribute(String value) {
        return value != null ? Distance.fromValue(value) : null;
    }
}
