package com.datastax.driver.core.schemabuilder;

import com.datastax.driver.mapping.annotations.Field;
import com.datastax.driver.mapping.annotations.UDT;

import java.util.UUID;

@UDT(name = "some_udt")
public class Udt {
    private UUID id;
    @Field(name = "some_text")
    private String someText;

}
