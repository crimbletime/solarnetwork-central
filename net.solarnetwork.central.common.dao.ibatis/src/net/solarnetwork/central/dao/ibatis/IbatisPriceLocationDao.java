/* ==================================================================
 * IbatisPriceLocationDao.java - Feb 20, 2011 3:05:31 PM
 * 
 * Copyright 2007-2011 SolarNetwork.net Dev Team
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

package net.solarnetwork.central.dao.ibatis;

import java.util.HashMap;
import java.util.Map;

import net.solarnetwork.central.dao.PriceLocationDao;
import net.solarnetwork.central.domain.PriceLocation;
import net.solarnetwork.central.domain.SourceLocation;
import net.solarnetwork.central.domain.SourceLocationMatch;

/**
 * iBATIS implementation of {@link PriceLocationDao}.
 * 
 * @author matt
 * @version $Revision$
 */
public class IbatisPriceLocationDao 
extends IbatisFilterableDaoSupport<PriceLocation, SourceLocationMatch, SourceLocation>
implements PriceLocationDao {

	/** The query name used for {@link #getPriceLocationForName(String,String)}. */
	public static final String QUERY_FOR_NAME = "get-PriceLocation-for-name";
	
	/**
	 * Default constructor.
	 */
	public IbatisPriceLocationDao() {
		super(PriceLocation.class, SourceLocationMatch.class);
	}

	@Override
	public PriceLocation getPriceLocationForName(String sourceName,
			String locationName) {
		Map<String, Object> params = new HashMap<String, Object>(2);
		params.put("locationName", locationName);
		params.put("sourceName", sourceName);
		return (PriceLocation)getSqlMapClientTemplate().queryForObject(
				QUERY_FOR_NAME, params);
	}

}
