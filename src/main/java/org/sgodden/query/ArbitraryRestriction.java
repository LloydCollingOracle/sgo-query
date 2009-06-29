package org.sgodden.query;
/**
 * An additional set of arbitrary Restrictions.
 * @author bwoods
 *
 */
public class ArbitraryRestriction implements Restriction {
    private String restrictionText;
    private Object[] values;

    public ArbitraryRestriction(String restrictionText, Object[] parameters) {
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
