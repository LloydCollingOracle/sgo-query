package org.sgodden.query;
/**
 * An additional Boolean restriction.
 * @author bwoods
 *
 */
public class BooleanRestriction extends ArbitraryRestriction{
    public BooleanRestriction(Boolean value) {
        super("true = ? ", new Object[]{value});
    }
}
