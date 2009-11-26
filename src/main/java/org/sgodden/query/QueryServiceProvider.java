package org.sgodden.query;

import org.sgodden.query.service.QueryService;

/**
 * An interface to return configured instances of {@link QueryService}.
 * @author sgodden
 */
public interface QueryServiceProvider {
	
	/**
	 * Returns an instance of {@link QueryService}.
	 * @return the query service instance.
	 */
	public QueryService get();

}
