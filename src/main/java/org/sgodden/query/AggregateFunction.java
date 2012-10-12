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

import org.hibernate.dialect.function.StandardSQLFunction;
import org.hibernate.type.StringType;

/**
 * Aggregate functions that may be applied to query columns.
 * 
 * @author goddens
 *
 */
public enum AggregateFunction {

	/**
	 * No aggregate function.
	 */
	NONE,
	/**
	 * Selects the sum.
	 */
	SUM,
	/**
	 * Selects the minimum value.
	 */
	MINIMUM,
	/**
	 * Selects the maximum value.
	 */
	MAXIMUM,
	/**
	 * Selects the average value.
	 */
	AVERAGE,
	/**
	 * Selects the value on the locale-dependent instance having the locale nearest to the user locale supplied in the query.
	 * <p/>
	 * For instance, if 'localeData.description' is selected, and if the user has locale 'fr_FR', and there are locale-dependent instances
	 * with locales 'fr' and <code>null</code>, then the description on the instance having the locale 'fr' will be returned.
	 * <p/>
	 * If on the other hand, the user's locale is 'de_AT', then the description on the instance having the <code>null</code>
	 * locale will be returned, as there are no instances for either 'de_AT' or 'de'. 
	 */
	LOCALE,
	
	/**
	 * Selects the count of the value
	 */
	COUNT,

	/**
	 * Selects the count of distinct values
	 */
	COUNT_DISTINCT,
	
	/**
	 * <p>Concatenates the column values using a delimiter set on the query service.</p>
	 * <p>In order to use this, an sql function must be registered in the hibernate configuration:</p>
	 * <b>MySQL:</b><br/>
	 * <pre>hibernateConfig.addSqlFunction("group_concat",new StandardSQLFunction("group_concat", new StringType()));</pre>
	 */
	GROUP_CONCAT

}
