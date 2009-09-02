package org.sgodden.query.service;

import java.lang.reflect.InvocationTargetException;
import java.util.Locale;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.impl.AbstractQueryImpl;
import org.sgodden.query.Operator;

/**
 * Utility package for the query service.
 * @author sgodden
 *
 */
public class QueryUtil {
    

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

    static Object valueToParameter(
    		String attributePath, 
    		Object object, 
    		Operator operator, 
    		Locale locale,
    		boolean ignoreCase) {
        Object ret = null;
        
        if (object instanceof String
                && !("id"
                        .equals(getUnQualifiedAttributeIdentifier(attributePath))) // the id is always numeric
        ) {
        	StringBuffer retBuf = new StringBuffer();
            if (operator == Operator.CONTAINS || operator == Operator.ENDS_WITH) {
                retBuf.append('%');
            }
            String value = object.toString();
            if (ignoreCase) {
                value = value.toUpperCase(locale);
            }
            retBuf.append(value);
            if (operator == Operator.CONTAINS || operator == Operator.STARTS_WITH) {
                retBuf.append('%');
            }
            ret = retBuf.toString();
        }
        else {
        	ret = object;
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
    static int getRelationDepth(String attributePath) {
        return attributePath.split("\\.").length - 1;
    }

    @SuppressWarnings("unchecked")
	public static Map getParameterMap(Query q) {
		if (q instanceof AbstractQueryImpl) {
			try {
				return (Map)q.getClass().getMethod("getNamedParams", (Class[])null).invoke(q, (Object[])null);
			} catch (IllegalAccessException e) {
				throw new RuntimeException("Failed to retrieve named parameters from query", e);
			} catch (InvocationTargetException e) {
				throw new RuntimeException("Failed to retrieve named parameters from query", e);
			} catch (NoSuchMethodException e) {
				throw new RuntimeException("Failed to retrieve named parameters from query", e);
			}
		} else {
			throw new IllegalArgumentException("don't know how to extract the named parameters from queries of type " + q);
		}
	}
}
