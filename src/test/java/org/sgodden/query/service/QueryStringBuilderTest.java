package org.sgodden.query.service;

import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expect;
import static org.easymock.classextension.EasyMock.createMock;
import static org.easymock.classextension.EasyMock.createNiceMock;
import static org.easymock.classextension.EasyMock.replay;
import static org.easymock.classextension.EasyMock.verify;

import org.hibernate.Session;
import org.sgodden.query.AndRestriction;
import org.sgodden.query.Operator;
import org.sgodden.query.OrRestriction;
import org.sgodden.query.Query;
import org.sgodden.query.SimpleRestriction;
import org.testng.annotations.Test;

@Test
public class QueryStringBuilderTest {

    /**
     * Basic test of building a query string.
     */
    public void testBasic() {
        Query query = new Query().setObjectClassName(String.class.getName())
                // just silly example
                .addColumn("code")
                .setFilterCriterion(
                        new OrRestriction()
                                .or(
                                        new AndRestriction()
                                                .and(
                                                        new SimpleRestriction(
                                                                "code",
                                                                Operator.EQUALS,
                                                                new Object[] { "ASDASD" }))
                                                .and(
                                                        new SimpleRestriction(
                                                                "contact.code",
                                                                Operator.EQUALS,
                                                                new Object[] { "ASDASD" })))
                                .or(
                                        new AndRestriction()
                                                .and(
                                                        new SimpleRestriction(
                                                                "code",
                                                                Operator.EQUALS,
                                                                new Object[] { "ASDASD" }))
                                                .and(
                                                        new SimpleRestriction(
                                                                "code",
                                                                Operator.EQUALS,
                                                                new Object[] { "ASDASD" }))));
        
        Session s = createMock(Session.class);
        org.hibernate.Query q = createNiceMock(org.hibernate.Query.class);
        
        expect(s.createQuery(eq("SELECT obj.id, obj.code FROM java.lang.String AS obj LEFT OUTER JOIN obj.contact AS contact " +
        		"WHERE ( ( obj.code = :objcode0 AND contact.code = :contactcode1 ) OR " +
        		"( obj.code = :objcode2 AND obj.code = :objcode3 ) ) ORDER BY 2, 1"))).andReturn(q);
        
        expect(s.createQuery(eq("SELECT COUNT(distinct obj.id)  FROM java.lang.String AS obj LEFT OUTER JOIN obj.contact AS contact " +
        		"WHERE ( ( obj.code = :objcode0 AND contact.code = :contactcode1 ) OR " +
        		"( obj.code = :objcode2 AND obj.code = :objcode3 ) )"))).andReturn(q);
        
        replay(s);
        replay(q);

        new QueryStringBuilder().buildQuery(s, query);
        new QueryStringBuilder().buildCountQuery(s, query);
        
        verify(s);
        verify(q);
    }

    public void testStartsWithIgnoreCase() {
        Query query = new Query().setObjectClassName(String.class.getName())
                .addColumn("code").setFilterCriterion(
                        new SimpleRestriction("code",
                                Operator.STARTS_WITH, "AsdAsd").setIgnoreCase(true));
        
        Session s = createMock(Session.class);
        org.hibernate.Query q = createNiceMock(org.hibernate.Query.class);
        
        expect(s.createQuery(eq("SELECT obj.id, obj.code FROM java.lang.String AS obj WHERE UPPER(obj.code) LIKE :objcode0 ORDER BY 2, 1"))).andReturn(q);
        
        replay(s);
        replay(q);

        new QueryStringBuilder().buildQuery(s, query);
        
        verify(s);
        verify(q);

    }

    /**
     * Ensures that a null pointer does not occur when a null filter criterion
     * is passed.
     */
    public void testNullFilterCriterion() {
        Query query = new Query().setObjectClassName(String.class.getName())
                .addColumn("code");
        
        Session s = createMock(Session.class);
        org.hibernate.Query q = createMock(org.hibernate.Query.class);
        
        expect(s.createQuery(eq("SELECT obj.id, obj.code FROM java.lang.String AS obj ORDER BY 2, 1"))).andReturn(q);
        
        replay(s);
        replay(q);
        
        new QueryStringBuilder().buildQuery(s, query);
        
        verify(s);
        verify(q);
    }

    /**
     * Tests the inclusion of tables and child tables
     */
    public void testMultiDepthSelection() {
        Query query = new Query().setObjectClassName(String.class.getName())
                .addColumn("blah.foo");
        
        Session s = createMock(Session.class);
        org.hibernate.Query q = createMock(org.hibernate.Query.class);
        
        expect(s.createQuery(eq("SELECT obj.id, blah.foo FROM java.lang.String AS obj LEFT OUTER JOIN obj.blah AS blah ORDER BY 2, 1"))).andReturn(q);
        
        replay(s);
        replay(q);
        
        new QueryStringBuilder().buildQuery(s, query);
        
        verify(s);
        verify(q);
    }

    /**
     * Tests the inclusion of tables, child tables and grandchild tables.
     */
    public void testMultiDepthSelection2() {
        Query query = new Query().setObjectClassName(String.class.getName())
                .addColumn("blah.foo.bar");
        
        Session s = createMock(Session.class);
        org.hibernate.Query q = createMock(org.hibernate.Query.class);
        
        expect(s.createQuery(eq("SELECT obj.id, blahfoo.bar FROM java.lang.String AS obj LEFT OUTER JOIN obj.blah AS blah LEFT OUTER JOIN blah.foo AS blahfoo ORDER BY 2, 1"))).andReturn(q);
        
        replay(s);
        replay(q);
        new QueryStringBuilder().buildQuery(s, query);
        
        verify(s);
        verify(q);
    }

}