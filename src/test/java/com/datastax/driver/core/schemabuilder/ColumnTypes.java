package com.datastax.driver.core.schemabuilder;

import com.datastax.driver.core.schemabuilder.Udt;
import com.datastax.driver.mapping.annotations.Column;
import com.google.common.collect.ImmutableSet;

import java.math.BigDecimal;
import java.util.*;

public class ColumnTypes {

    @Column(name = "set_text")
    private Set<String> setText;

    @Column(name = "collection_text")
    private Collection<String> collectionText;

    @Column(name = "list_text")
    private List<String> listText;

    @Column(name = "immutable_set_text")
    private ImmutableSet<String> immutableSet;

    private Map<String, Integer> map;

    private Udt udt;

    private Set<Udt> set_udt;

    private List<Udt> list_udt;

    private Collection<Udt> collection_udt;

    private Map<UUID, Udt> map_udt;

    private BigDecimal total;
}
