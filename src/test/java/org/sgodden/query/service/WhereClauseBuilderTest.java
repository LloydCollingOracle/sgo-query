package org.sgodden.query.service;

import static org.testng.Assert.assertEquals;

import java.util.HashMap;

import org.sgodden.query.AndRestriction;
import org.sgodden.query.Operator;
import org.sgodden.query.OrRestriction;
import org.sgodden.query.Query;
import org.sgodden.query.SimpleRestriction;
import org.testng.annotations.Test;

@Test
public class WhereClauseBuilderTest {

    /**
     * Basic test of building a where clause containing an or and a couple of
     * ands.
     */
    public void testWhereClause() {
        Query query = new Query();
        query.setFilterCriterion(new OrRestriction().or(
                new AndRestriction().and(
                        new SimpleRestriction("code", Operator.EQUALS,
                                new Object[] { "ASDASD" })).and(
                        new SimpleRestriction("contact.code",
                                Operator.EQUALS, new Object[] { "ASDASD" })))
                .or(
                        new AndRestriction().and(
                                new SimpleRestriction("code",
                                        Operator.EQUALS,
                                        new Object[] { "ASDASD" })).and(
                                new SimpleRestriction("code",
                                        Operator.EQUALS,
                                        new Object[] { "ASDASD" }))));

        StringBuffer sb = new WhereClauseBuilder().buildWhereClause(query, new HashMap<String, Object>());

        assertEquals(
                sb.toString(),
                " WHERE ( ( obj.code = :objcode0 AND contact.code = :contactcode1 ) OR ( obj.code = :objcode2 AND obj.code = :objcode3 ) )");
    }

}
