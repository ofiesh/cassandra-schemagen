package com.ofiesh.cassandra.schemagen;

import com.datastax.driver.core.ResultSet;
import org.junit.BeforeClass;
import org.junit.Test;

public class DescribeAccessorTest {

    @Test
    public void testUdt() {
        try(DescribeAccessorTester t = new DescribeAccessorTester()) {
            DescribeAccessor describeAccessor = t.getDescribeAccessor();
            ResultSet udtFields = describeAccessor.getUDTFields("test", "some_udt");
        }
    }
}
