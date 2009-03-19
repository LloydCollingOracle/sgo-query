package org.sgodden.query.service;

import java.util.Locale;

import org.sgodden.query.Operator;

/**
 * Utility package for the query service.
 * @author sgodden
 *
 */
class QueryUtil {
    

    static String getUnQualifiedAttributeIdentifier(String attributePath) {
        return attributePath.substring(attributePath.lastIndexOf('.') + 1);
    }

    static String getClassAlias(String attributePath) {
        String ret;
        if (isRelatedColumn(attributePath)) {
            ret = getRelationName(attributePath).replaceAll("\\.", "");
        }
        else {
            ret = "obj";
        }
        return ret;
    }

    /**
     * Returns the final attribute name in a potentially nested attribute path.
     * @param attributePath the attribute path.
     * @return the name of the final attribute in the path.
     */
    static String getFinalAttributeName(String attributePath) {
        String ret;
        if (isRelatedColumn(attributePath)) {
            ret = attributePath.substring(attributePath.lastIndexOf('.') + 1,
                    attributePath.length());
        }
        else {
            ret = attributePath;
        }
        return ret;
    }

    /**
     * Returns the part of a compound attribute path up to but not including the
     * last dot.
     * @param col
     * @return
     */
    static String getRelationName(String attributePath) {
        return attributePath.substring(0, attributePath.lastIndexOf('.'));
    }

    static String getQualifiedAttributeIdentifier(String attributePath) {
        if ("*".equals(attributePath))
            return "*";
        return getClassAlias(attributePath) + '.'
                + getFinalAttributeName(attributePath);
    }

    static StringBuffer valueToString(
    		String attributePath, 
    		Object object, 
    		Operator operator, 
    		Locale locale,
    		boolean ignoreCase) {
        StringBuffer ret = new StringBuffer();
        
        if (object instanceof String
                && !("id"
                        .equals(getUnQualifiedAttributeIdentifier(attributePath))) // the id is always numeric
        ) {
            ret.append("'");
            if (operator == Operator.CONTAINS || operator == Operator.ENDS_WITH) {
                ret.append('%');
            }
            String value = object.toString();
            if (ignoreCase) {
                value = value.toUpperCase(locale);
            }
            ret.append(value);
            if (operator == Operator.CONTAINS || operator == Operator.STARTS_WITH) {
                ret.append('%');
            }
            ret.append("'");
        }
        else {
        	if(object!=null){
        		ret.append(object.toString());
        	}
        }
        return ret;
    }

    /**
     * Determines if the requested column comes from a related entity.
     * @param col
     * @return
     */
    static boolean isRelatedColumn(String attributePath) {
        return attributePath.indexOf('.') != -1;
    }

    /**
     * Returns how many related tables the attribute path goes through
     * @param attributePath
     * @return
     */
    public static int getRelationDepth(String attributePath) {
        return attributePath.split("\\.").length - 1;
    }
}
