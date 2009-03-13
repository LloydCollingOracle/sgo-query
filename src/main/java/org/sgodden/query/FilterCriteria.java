package org.sgodden.query;

/**
 * Provides a DSL-like API for creating filter criteria.
 * @author sgodden
 */
public class FilterCriteria {
    
    public SimpleRestriction criterion(String attributePath, Operator operator,
            Object[] values) {
        return new SimpleRestriction(attributePath, operator, values);
    }
    
    public OrRestriction or() {
        return new OrRestriction();
    }

}
