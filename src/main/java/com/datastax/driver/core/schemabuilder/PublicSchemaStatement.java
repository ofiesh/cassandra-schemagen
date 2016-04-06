package com.datastax.driver.core.schemabuilder;

public class PublicSchemaStatement {

    public static SchemaStatement fromQueryString(String queryString) {
        return SchemaStatement.fromQueryString(queryString);
    }

    public static String buildInternal(SchemaStatement schemaStatement) {
        return schemaStatement.buildInternal();
    }
}