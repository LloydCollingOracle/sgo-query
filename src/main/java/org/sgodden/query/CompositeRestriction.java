package org.sgodden.query;

import java.util.List;

/**
 * A restriction composed of multiple sub-restrictions.
 * @author sgodden
 */
public interface CompositeRestriction extends Restriction {
    
    /**
     * Returns the list of sub-restrictions.
     * @return the list of sub-restrictions.
     */
    public List < Restriction > getRestrictions();

}
