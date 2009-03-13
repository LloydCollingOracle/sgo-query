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

import java.util.ArrayList;
import java.util.Locale;
import java.util.Set;

import org.apache.commons.beanutils.PropertyUtils;

/**
 * Utility methods related to locale processing.
 * 
 * @author goddens
 *
 */
public class LocaleUtils {
	
	public static String getDescription(Object object, Locale locale){
		Object localeData = getLocaleData(object, locale);
		String ret;
		
		try {
			ret = (String) PropertyUtils.getProperty(localeData, "description");
		} catch (Exception e) {
			throw new Error(e);
		} 
		
		return ret;
	}
	
	/**
	 * Returns the description for the passed object, having the passed
	 * locale.
	 * @param object the object for which a description is required.
	 * @param locale the locale of the description to return.
	 * @return
	 */
    @SuppressWarnings("unchecked")
	public static Object getLocaleData(Object object, Locale locale){
		try {
			Set l = (Set) PropertyUtils.getProperty(object, "localeData");
			Locale[] locales = getLocaleHierarchy(locale);
			for (Locale loc : locales){
				for(Object o : l){
					String localeString = (String) PropertyUtils.getProperty(o, "locale");
					if (	(loc == null && localeString == null) 
							|| (loc != null && loc.toString().equals(localeString))
						){
						return o;
					}
				}				
			}
		} catch (Exception e) {
			throw new Error(e);
		}
		
		throw new Error("Did not find locale");
	}
	
	/**
	 * Given the passed most specific locale, returns an array of {@link Locale} instances 
	 * in preferential order, most specific to least specific, which should be used to search 
	 * for locale-dependent data.
	 * <p/>
	 * For instance, given the locale 'en_GB', this returns the following locales:
	 * <ul>
	 * <li>'en_GB'</li>
	 * <li>'en'</li>
	 * <li><code>null</code></li>
	 * </ul>
	 * 
	 * @param locale the most specific locale.
	 * @return an array of locales, starting from the passed locale, and
	 * ending with the <code>null</code> (default) locale.
	 */
	public static Locale[] getLocaleHierarchy(Locale locale){
		ArrayList<Locale> ret = new ArrayList<Locale>();
		
		Locale currentLocale = locale;
		while (currentLocale != null){
			ret.add(currentLocale);
			String variant = currentLocale.getVariant();
			
			if ("".equals(variant)){
				variant = null;
			}
			
			String country = currentLocale.getCountry();
			if ("".equals(country)){
				country = null;
			}
			
			if (variant != null){
				currentLocale = new Locale(currentLocale.getLanguage(), currentLocale.getCountry());
			} else if (country != null){
				currentLocale = new Locale(currentLocale.getLanguage());
			} else {
				currentLocale = null;
				ret.add(null);
			}
		}
		
		return (Locale[])ret.toArray(new Locale[ret.size()]);
	}
	
}
