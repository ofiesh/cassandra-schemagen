package com.ofiesh.cassandra.schemagen;

import static org.junit.Assert.assertEquals;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.DataType;
import com.datastax.driver.core.UdtDataType;
import com.datastax.driver.core.schemabuilder.SchemaBuilder;
import com.datastax.driver.core.schemabuilder.UDTType;
import com.datastax.driver.core.schemabuilder.Udt;
import org.junit.Test;

public class SchemaGenTest {

    @Test
    public void testSchemaGenCreateComplexEntity() {
        String create = SchemaBuilder.createTable("table_name")
                .addPartitionKey("id", DataType.timeuuid())
                .addPartitionKey("update_id", DataType.timeuuid())
                .addClusteringColumn("name", DataType.text())
                .addClusteringColumn("ordering", DataType.cint())
                .addColumn("total", DataType.decimal())
                .addColumn("set_text", DataType.set(DataType.text()))
                .addColumn("map", DataType.map(DataType.text(), DataType.decimal()))
                .addColumn("fooooo", new UdtDataType("some_udt"))
                .buildInternal();

        assertEquals(create, SchemaGen.create(EntityComplexKey.class).buildInternal());
    }

    @Test
    public void testSchemaGenCreateUDT() {
        String create = SchemaBuilder.createType("some_udt")
                .addColumn("id", DataType.timeuuid())
                .addColumn("some_text", DataType.text()).buildInternal();

        assertEquals(create, SchemaGen.createUDT(Udt.class).buildInternal());
    }

    @Test
    public void test() {
        try(Cluster localhost = Cluster.builder().addContactPoint("localhost").build()) {
            SchemaGen schemaGen = new SchemaGen(localhost.newSession());

            schemaGen.generate("testa", Udt.class, EntityComplexKey.class);
        }
    }
}
