package org.sgodden.query;

import java.io.Serializable;

/**
 * An additional set of arbitrary Restrictions.
 * @author bwoods
 *
 */
public class ArbitraryRestriction implements Serializable, Restriction {

    /**
     * Serial version UID.
     */
    private static final long serialVersionUID = 20090629L;

    private String restrictionText;
    private Object[] values;

    public ArbitraryRestriction(String restrictionText, Object[] values) {
        this.setRestrictionText(restrictionText);
        this.setValues(values);
    }

    public void setRestrictionText(String restrictionText) {
        this.restrictionText = restrictionText;
        }

    public String getRestrictionText() {
        return restrictionText;
    }

    public void setValues(Object[] values) {
        this.values = values;
    }

    public Object[] getValues() {
        return values;
    }
}
