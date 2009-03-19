/* =================================================================
# This library is free software; you can redistribute it and/or
# modify it under the terms of the GNU Lesser General Public
# License as published by the Free Software Foundation; either
# version 2.1 of the License, or (at your option) any later version.
#
# This library is distributed in the hope that it will be useful,
# but WITHOUT ANY WARRANTY; without even the implied warranty of
# MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
# Lesser General Public License for more details.
#
# You should have received a copy of the GNU Lesser General Public
# License along with this library; if not, write to the Free Software
# Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
#
# ================================================================= */
package org.sgodden.query;

import org.hibernate.SessionFactory;
import org.hibernate.engine.SessionFactoryImplementor;
import org.hibernate.type.AssociationType;
import org.hibernate.type.LongType;
import org.hibernate.type.Type;

/**
 * Provides static helper methods related to objects and classes.
 * 
 * @author goddens
 *
 */
public class ObjectUtils {
	
	//private static final transient Log log = LogFactory.getLog(ObjectUtils.class);

	/**
	 * Returns the class of the potentially nested property.
	 * 
	 * @param clazz the top-level class in the propertyName.
	 * @param propertyName the (potentially nested) property name.
	 * @param sessionFactory the hibernate session factory.
	 * @return the class of the property;
	 */
    @SuppressWarnings("unchecked")
	public static Type getPropertyClass(
			Class clazz, 
			String propertyName,
			SessionFactory sessionFactory){
		String thisPropertyName = null;
		String remainingPropertyName = null;
		
		if (propertyName.indexOf('.') != -1){
			thisPropertyName = propertyName.substring(0, propertyName.indexOf('.'));
			remainingPropertyName = propertyName.substring(propertyName.indexOf('.') + 1, propertyName.length());
		} else {
			thisPropertyName = propertyName;
		}

		Type type = sessionFactory.getClassMetadata(clazz).getPropertyType(thisPropertyName);
		
		if (remainingPropertyName != null){
			Class propertyClass = null;
			AssociationType assType = (AssociationType)type;
			try {
				propertyClass = ObjectUtils.class.getClassLoader().loadClass(
						assType.getAssociatedEntityName( (SessionFactoryImplementor)sessionFactory )
				);
			} catch (Exception e) {
				throw new IllegalArgumentException("Could not determine type for property '" + thisPropertyName + "' for class '" + clazz.getName()+ "'");
			} 
			return getPropertyClass(propertyClass, remainingPropertyName, sessionFactory);
		} else {
			return type;
		}
	}
	
	/**
	 * Returns the class of the potentially nested property.
	 * 
	 * @param clazz the top-level class in the propertyName.
	 * @param propertyName the (potentially nested) property name.
	 * @param sessionFactory the hibernate session factory.
	 * @return the class of the property;
	 */
    @SuppressWarnings("unchecked")
	public static Type getPropertyClass(String className, String propertyName, SessionFactory sessionFactory){
		Type ret = null;
		try {
			Class clazz = ObjectUtils.class.getClassLoader().loadClass(className);
			ret = getPropertyClass(clazz, propertyName, sessionFactory);
		} catch (ClassNotFoundException e) {
			throw new Error(e);
		}
		return ret;
	}

}
