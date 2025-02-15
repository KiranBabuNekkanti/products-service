package com.amcart.product.config;

import com.amcart.product.model.ImageModel;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.List;

@Converter
public class ImageModelAttributeConverter implements AttributeConverter<List<ImageModel>, String> {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(List<ImageModel> images) {
        try {
            return objectMapper.writeValueAsString(images);
        } catch (JsonProcessingException jpe) {
            return null;
        }
    }

    @Override
    public List<ImageModel> convertToEntityAttribute(String value) {
        try {
            return objectMapper.readValue(value, new TypeReference<>() {
            });
        } catch (JsonProcessingException e) {
            return null;
        }
    }
}