/* ==================================================================
 * AbstractIbatisUserDaoTestSupport.java - Jan 29, 2010 12:18:43 PM
 * 
 * Copyright 2007-2010 SolarNetwork.net Dev Team
 * 
 * This program is free software; you can redistribute it and/or 
 * modify it under the terms of the GNU General Public License as 
 * published by the Free Software Foundation; either version 2 of 
 * the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful, 
 * but WITHOUT ANY WARRANTY; without even the implied warranty of 
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU 
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License 
 * along with this program; if not, write to the Free Software 
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 
 * 02111-1307 USA
 * ==================================================================
 * $Id$
 * ==================================================================
 */

package net.solarnetwork.central.user.dao.ibatis.test;

import static org.junit.Assert.assertNotNull;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import net.solarnetwork.central.test.AbstractCentralTransactionalTest;
import net.solarnetwork.central.user.dao.ibatis.IbatisUserDao;
import net.solarnetwork.central.user.domain.User;

/**
 * Base test class for Ibatis User DAO tests.
 * 
 * @author matt
 * @version $Id$
 */
@ContextConfiguration
public abstract class AbstractIbatisUserDaoTestSupport extends AbstractCentralTransactionalTest {

	public static final String TEST_EMAIL = "foo@localhost.localdomain";
	public static final String TEST_NAME = "Foo Bar";
	public static final String TEST_PASSWORD = "password";

	@Autowired protected IbatisUserDao userDao;

	/**
	 * Persist a new User and return it.
	 * 
	 * @param email the email of the new user
	 * @return the User
	 */
	protected User createNewUser(String email) {
		return userDao.get(storeNewUser(email));
	}
	
	/**
	 * Persist a new User and return its primary key.
	 * 
	 * @param email the email of the new user
	 * @return the primary key
	 */
	protected Long storeNewUser(String email) {
		User newUser = new User();
		newUser.setCreated(new DateTime());
		newUser.setEmail(email);
		newUser.setName(TEST_NAME);
		newUser.setPassword(TEST_PASSWORD);
		newUser.setEnabled(Boolean.TRUE);
		Long id = userDao.store(newUser);
		logger.debug("Got new user PK: " +id);
		assertNotNull(id);
		return id;
	}

}
