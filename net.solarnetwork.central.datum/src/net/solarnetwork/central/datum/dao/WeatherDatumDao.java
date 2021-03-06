/* ===================================================================
 * WeatherDatumDao.java
 * 
 * Created Aug 29, 2008 7:43:23 PM
 * 
 * Copyright (c) 2008 Solarnetwork.net Dev Team.
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
 * ===================================================================
 */

package net.solarnetwork.central.datum.dao;

import net.solarnetwork.central.dao.FilterableDao;
import net.solarnetwork.central.datum.domain.LocationDatumFilter;
import net.solarnetwork.central.datum.domain.WeatherDatum;
import net.solarnetwork.central.datum.domain.WeatherDatumMatch;
import org.joda.time.ReadableDateTime;

/**
 * DAO API for {@link WeatherDatum} data.
 * 
 * @author matt.magoffin
 * @version 1.1
 */
public interface WeatherDatumDao extends DatumDao<WeatherDatum>,
		FilterableDao<WeatherDatumMatch, Long, LocationDatumFilter> {

	/**
	 * Get the most-recently created WeatherDatum up to (and including) a
	 * specific {@link WeatherDatum#getInfoDate()} value.
	 * 
	 * @param nodeId
	 *        the node ID
	 * @param upToDate
	 *        the maximum date (inclusive) to search for
	 * @return the WeatherDatum, or <em>null</em> if not found
	 */
	WeatherDatum getMostRecentWeatherDatum(Long nodeId, ReadableDateTime upToDate);
}
