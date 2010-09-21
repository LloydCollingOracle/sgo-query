package org.sgodden.query.service;

import org.hibernate.Session;
import org.sgodden.query.Query;

/**
 * A provider of hibernate sessions.
 * @author Mike Whittaker
 */
public interface QueryBasedSessionProvider extends SessionProvider {

	/**
	 * Returns the current hibernate session which may be altered, eg filtered
	 * on the basis of the query.
	 * @return the current hibernate session.
	 */
	Session get(Query query);
	
}
