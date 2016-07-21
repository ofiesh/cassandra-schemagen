package com.ofiesh.cassandra.schemagen;

import com.datastax.driver.mapping.annotations.Column;
import com.datastax.driver.mapping.annotations.PartitionKey;
import com.datastax.driver.mapping.annotations.Table;

import java.util.UUID;

@Table(name = "class_one")
public class Class1 {
    @PartitionKey
    @Column(name = "some_column")
    private UUID uuid;
}
