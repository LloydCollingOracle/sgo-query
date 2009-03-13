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
package org.sgodden.query.models;

/**
 * Generic base interface for a model.
 * 
 * @author sgodden
 *
 */
public interface Model {
	
	/**
	 * Adds a listener to be notified when this model changes.
	 * @param listener the listener to be added.
	 */
	public void addModelListener(ModelListener listener);
	
	/**
	 * Removes the specified model listener.
	 * @param listener the listener to be removed.
	 */
	public void removeModelListener(ModelListener listener);

}
