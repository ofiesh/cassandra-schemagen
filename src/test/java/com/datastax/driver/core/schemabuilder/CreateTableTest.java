package com.datastax.driver.core.schemabuilder;

import com.datastax.driver.core.DataType;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CreateTableTest {

    @Test
    public void testCreateTable() {
        String create = new CreateTable("Foo").addPartitionColumn("foo", new NativeColumnType(DataType.timeuuid()))
                .addPartitionColumn("bar", new NativeColumnType(DataType.text()))
                .addClusteringColumn("cfoo", new NativeColumnType(DataType.cint()))
                .addColumn("cbar", new NativeColumnType(DataType.text()))
                .buildInternal();

        assertEquals("\n\tCREATE TABLE Foo(\n" +
                "\t\tfoo timeuuid,\n" +
                "\t\tbar text,\n" +
                "\t\tcfoo int,\n" +
                "\t\tcbar text,\n" +
                "\t\tPRIMARY KEY((foo, bar), cfoo))", create);
    }
}
