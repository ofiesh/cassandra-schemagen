package com.datastax.driver.core.schemabuilder;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import com.datastax.driver.core.DataType;
import org.junit.Test;

public class ColumnTypeResolverTest {

    private void testField(String expected, String field) {
        try {
            DataType type = DataTypeResolver.type(ColumnTypes.class.getDeclaredField(field));
            if(type != null) {
                assertEquals(expected, type.toString());
            }  else {
                assertEquals(expected, null);
            }
        } catch (NoSuchFieldException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testSetTextField() throws NoSuchFieldException {
        testField("set<text>", "setText");
    }

    @Test
    public void testImmutableSetTextField() throws NoSuchFieldException {
        testField("set<text>", "immutableSet");
    }

    @Test
    public void testCollectionText() throws NoSuchFieldException {
        testField("list<text>", "collectionText");
    }

    @Test
    public void testListTextField() throws NoSuchFieldException {
        testField("list<text>", "listText");
    }

    @Test
    public void testMapField() throws NoSuchFieldException {
        testField("map<text, int>", "map");
    }

    @Test
    public void testBigDecimalType() throws NoSuchFieldException {
        testField("decimal", "total");
    }

    @Test
    public void testUdtType() throws NoSuchFieldException {
        testField("frozen<some_udt>", "udt");
    }

    @Test
    public void testSetUdtType() {
        testField("set<frozen<some_udt>>", "set_udt");
    }

    @Test
    public void testListUdtType() {
        testField("list<frozen<some_udt>>", "list_udt");
    }

    @Test
    public void testCollectionUdtType() {
        testField("list<frozen<some_udt>>", "collection_udt");
    }

    @Test
    public void testMapUdt() {
        testField("map<timeuuid, frozen<some_udt>>", "map_udt");
    }
}
