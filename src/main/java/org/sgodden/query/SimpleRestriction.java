/*
 * ================================================================= # This
 * library is free software; you can redistribute it and/or # modify it under
 * the terms of the GNU Lesser General Public # License as published by the Free
 * Software Foundation; either # version 2.1 of the License, or (at your option)
 * any later version. # # This library is distributed in the hope that it will
 * be useful, # but WITHOUT ANY WARRANTY; without even the implied warranty of #
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU # Lesser
 * General Public License for more details. # # You should have received a copy
 * of the GNU Lesser General Public # License along with this library; if not,
 * write to the Free Software # Foundation, Inc., 51 Franklin Street, Fifth
 * Floor, Boston, MA 02110-1301 USA # #
 * =================================================================
 */
package org.sgodden.query;

import java.io.Serializable;

/**
 * A single criterion in the filter of a query.
 * @author Simon Godden
 */
public class SimpleRestriction implements Serializable, Restriction {

    /**
     * Serial version UID.
     */
    private static final long serialVersionUID = 20080909L;

    /**
     * The path of the attribute whose value is to be checked.
     */
    private String attributePath;
    /**
     * The operator to apply in the test.
     */
    private Operator operator;
    /**
     * The value(s) to compare against.
     */
    private Object[] values;
    /**
     * Whether to ignore case when comparing values.
     */
    private boolean ignoreCase = false;

    /**
     * Constructs a new empty filter criterion.
     */
    public SimpleRestriction() {
    }

    /**
     * Constructs a new filter criterion, initialised with the passed values.
     * @param attributePath the path of the attribute on which filtering will
     *            occur.
     * @param operator the operator to apply.
     * @param values the value(s) by which to filter.
     */
    public SimpleRestriction(String attributePath, Operator operator,
            Object[] values) {
        this.attributePath = attributePath;
        this.operator = operator;
        this.values = values;
    }

    /**
     * Constructs a new filter criterion, initialised with the passed values.
     * @param attributePath the path of the attribute on which filtering will
     *            occur.
     * @param operator the operator to apply.
     * @param value the single value.
     */
    public SimpleRestriction(String attributePath, Operator operator,
            Object value) {
        this(attributePath, operator, new Object[]{value});
    }

    /**
     * Returns the path of the attribute on which filtering will occur.
     * @return
     */
    public String getAttributePath() {
        return attributePath;
    }

    /**
     * @see #getAttributePath()
     * @param attributePath the attribute path on which to filter.
     */
    public void setAttributePath(String attributePath) {
        this.attributePath = attributePath;
    }

    /**
     * Returns the operator of the filter criterion.
     * @return the operator.
     */
    public Operator getOperator() {
        return operator;
    }

    /**
     * @see #getOperator()
     * @param operator the operator to apply.
     */
    public void setOperator(Operator operator) {
        this.operator = operator;
    }

    /**
     * Returns the value(s) by which to filter.
     * @return the values.
     */
    public Object[] getValues() {
        return values;
    }

    /**
     * @see #getValues()
     * @param values the values by which to filter.
     */
    public void setValues(Object[] values) {
        this.values = values;
    }

    /**
     * Convenience method when filtering by a single value.
     * @param value the value by which to filter.
     * @see #setValues(Object[])
     */
    public void setValue(Object value) {
        setValues(new Object[] { value });
    }

    /**
     * Returns whether case is to be ignored when comparing values.
     * @return whether case is to be ignored.
     */
	public boolean getIgnoreCase() {
		return ignoreCase;
	}

    /**
     * Sets whether case is to be ignored when comparing values.
     * @param ignoreCase whether case is to be ignored.
     * @return this object.
     */
	public SimpleRestriction setIgnoreCase(boolean ignoreCase) {
		this.ignoreCase = ignoreCase;
		return this;
	}

	public String toString() {
		StringBuffer ret = new StringBuffer();
		ret.append(getAttributePath());
		ret.append(" ");
		ret.append(getOperator().name());
		ret.append(" ");
		if (getValues() == null) {
			ret.append("NULL");
		} else if (getValues().length == 1) {
			ret.append(getValues()[0]);
		} else {
			for (int i = 0; i < getValues().length; i++) {
				ret.append(getValues()[i]);
				if (i < getValues().length - 1)
					ret.append(",");
			}
		}
		return ret.toString();
	}
}
