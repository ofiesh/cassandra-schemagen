package com.datastax.driver.core.schemabuilder;

import java.lang.reflect.Field;

public class SchemaGenHelpers {
    public static String getFieldName(Field field) {
        com.datastax.driver.mapping.annotations.Field fieldName =
                field.getDeclaredAnnotation(com.datastax.driver.mapping.annotations.Field.class);
        return fieldName != null && fieldName.name().length() > 0
                ? fieldName.name() : field.getName();
    }
}
