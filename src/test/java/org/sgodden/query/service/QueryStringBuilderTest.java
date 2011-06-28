package org.sgodden.query.service;

import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.*;
import static org.easymock.classextension.EasyMock.createMock;
import static org.easymock.classextension.EasyMock.createNiceMock;
import static org.easymock.classextension.EasyMock.replay;
import static org.easymock.classextension.EasyMock.verify;

import org.easymock.Capture;
import org.hibernate.Session;
import org.sgodden.query.AggregateFunction;
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
    
    /**
     * Tests the correct dealing with collections.
     */
    public void testCollectionQuery() {
        Query query = new Query().setObjectClassName(String.class.getName())
        .addColumn("code").setFilterCriterion(
                new SimpleRestriction("contact",
                        Operator.EMPTY, null));

        Session s = createMock(Session.class);
        org.hibernate.Query q = createNiceMock(org.hibernate.Query.class);

        expect(s.createQuery(eq("SELECT obj.id, obj.code FROM java.lang.String AS obj WHERE obj.contact IS EMPTY ORDER BY 2, 1"))).andReturn(q);

        replay(s);
        replay(q);

        new QueryStringBuilder().buildQuery(s, query);

        verify(s);
        verify(q);
    }
    
    /**
     * Tests the correct dealing with collections on 2nd & 3rd level attributes
     */
    public void testMultiLevelCollectionQuery() {
        Query query = new Query().setObjectClassName(String.class.getName())
        .addColumn("code").setFilterCriterion(new AndRestriction(new SimpleRestriction("contact", Operator.NOT_EMPTY, null))
                                            .and(new SimpleRestriction("contact.roles", Operator.NOT_EMPTY, null))
                                            .and(new SimpleRestriction("contact.roles.users", Operator.EMPTY, null)));
        
        Session s = createMock(Session.class);
        org.hibernate.Query q = createNiceMock(org.hibernate.Query.class);

        expect(s.createQuery(eq("SELECT obj.id, obj.code FROM java.lang.String AS obj LEFT OUTER JOIN obj.contact AS contact LEFT OUTER JOIN obj.contact AS contact LEFT OUTER JOIN contact.roles AS contactroles WHERE ( obj.contact IS NOT EMPTY  AND contact.roles IS NOT EMPTY  AND contactroles.users IS EMPTY ) ORDER BY 2, 1"))).andReturn(q);

        replay(s);
        replay(q);

        new QueryStringBuilder().buildQuery(s, query);

        verify(s);
        verify(q);
    }

    public void testCountQueryWithLocaleFunctions() {
        Query query = new Query().setObjectClassName("org.sgodden.example.Site");
        query.addColumn("name");
        query.addColumn("code");
        query.addColumn("supplier.name");
        query.addColumn("mainAddress.country.localeData.description", AggregateFunction.LOCALE);
        query.addColumn("leadTechnicalManager.person.name");
        query.addColumn("status.localeData.description", AggregateFunction.LOCALE);
        query.addColumn("lastAudit.toDate");
        query.addColumn("lastVisit.toData");
        query.addColumn("lastAudit.score.localeData.description", AggregateFunction.LOCALE);
        query.addColumn("lastVisit.score.localeData.description", AggregateFunction.LOCALE);
        OrRestriction orRestriction = new OrRestriction();
        query.setFilterCriterion(orRestriction);
        orRestriction.or(new SimpleRestriction("name", Operator.CONTAINS, "ABC"));
        orRestriction.or(new SimpleRestriction("code", Operator.CONTAINS, "ABC"));
        orRestriction.or(new SimpleRestriction("supplier.name", Operator.CONTAINS, "ABC"));
        orRestriction.or(new SimpleRestriction("mainAddress.country.localeData.description", Operator.CONTAINS, "ABC"));
        orRestriction.or(new SimpleRestriction("leadTechnicalManager.person.name", Operator.CONTAINS, "ABC"));
        orRestriction.or(new SimpleRestriction("status.localeData.description", Operator.CONTAINS, "ABC"));
        orRestriction.or(new SimpleRestriction("lastAudit.score.localeData.description", Operator.CONTAINS, "ABC"));
        orRestriction.or(new SimpleRestriction("lastVisit.score.localeData.description", Operator.CONTAINS, "ABC"));
        
        Session s = createMock(Session.class);
        org.hibernate.Query q = createNiceMock(org.hibernate.Query.class);

        expect(s.createQuery(eq("SELECT COUNT(distinct obj.id)  FROM org.sgodden.example.Site AS obj LEFT OUTER JOIN obj.supplier AS supplier LEFT OUTER JOIN obj.mainAddress AS mainAddress LEFT OUTER JOIN mainAddress.country AS mainAddresscountry LEFT OUTER JOIN mainAddresscountry.localeData AS mainAddresscountrylocaleData LEFT OUTER JOIN obj.leadTechnicalManager AS leadTechnicalManager LEFT OUTER JOIN leadTechnicalManager.person AS leadTechnicalManagerperson LEFT OUTER JOIN obj.status AS status LEFT OUTER JOIN status.localeData AS statuslocaleData LEFT OUTER JOIN obj.lastAudit AS lastAudit LEFT OUTER JOIN obj.lastVisit AS lastVisit LEFT OUTER JOIN lastAudit.score AS lastAuditscore LEFT OUTER JOIN lastAuditscore.localeData AS lastAuditscorelocaleData LEFT OUTER JOIN lastVisit.score AS lastVisitscore LEFT OUTER JOIN lastVisitscore.localeData AS lastVisitscorelocaleData WHERE ( obj.name LIKE :objname0 OR obj.code LIKE :objcode1 OR supplier.name LIKE :suppliername2 OR mainAddresscountrylocaleData.description LIKE :mainAddresscountrylocaleDatadescription3 OR leadTechnicalManagerperson.name LIKE :leadTechnicalManagerpersonname4 OR statuslocaleData.description LIKE :statuslocaleDatadescription5 OR lastAuditscorelocaleData.description LIKE :lastAuditscorelocaleDatadescription6 OR lastVisitscorelocaleData.description LIKE :lastVisitscorelocaleDatadescription7 ) AND (mainAddresscountrylocaleData.locale IN( :mainAddresscountrylocaleDatalocale ) OR mainAddresscountrylocaleData.locale IS NULL)  AND (statuslocaleData.locale IN( :statuslocaleDatalocale ) OR statuslocaleData.locale IS NULL)  AND (lastAuditscorelocaleData.locale IN( :lastAuditscorelocaleDatalocale ) OR lastAuditscorelocaleData.locale IS NULL)  AND (lastVisitscorelocaleData.locale IN( :lastVisitscorelocaleDatalocale ) OR lastVisitscorelocaleData.locale IS NULL) "))).andReturn(q);

        replay(s);
        replay(q);

        new QueryStringBuilder().buildCountQuery(s, query);

        verify(s);
        verify(q);
    }

}