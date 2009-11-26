package org.sgodden.query.service;

import org.hibernate.Session;

/**
 * A provider of hibernate sessions.
 * @author sgodden
 *
 */
public interface SessionProvider {
	
	/**
	 * Returns the current hibernate session.
	 * @return the current hibernate session.
	 */
	public Session get();

}
