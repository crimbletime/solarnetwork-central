/* ===================================================================
 * DataCollectorBiz.java
 * 
 * Created Aug 31, 2008 3:34:46 PM
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
 * $Id$
 * ===================================================================
 */

package net.solarnetwork.central.in.biz;

import java.util.List;

import net.solarnetwork.central.datum.domain.Datum;
import net.solarnetwork.central.domain.PriceLocation;
import net.solarnetwork.central.domain.SourceLocation;
import net.solarnetwork.central.domain.SourceLocationMatch;

/**
 * API for collecting data from solar nodes.
 * 
 * <p>Serves as a transactional facade to posting data into central system.</p>
 *
 * @author matt.magoffin
 * @version $Revision$ $Date$
 */
public interface DataCollectorBiz {

	/**
	 * Post a new {@link Datum}.
	 * 
	 * @param <D> the Datum type
	 * @param datum the data to persist
	 * @return the persisted entity
	 */
	<D extends Datum> D postDatum(D datum);
	
	/**
	 * Post a collection of datum of {@link Datum} in a single transaction.
	 * 
	 * @param datums the collection of datum
	 * @return the persisted entities, ordered in iterator order of {@code datums}
	 */
	List<Datum> postDatum(Iterable<Datum> datums);
	
	/**
	 * Look up a PriceLocation based on a source name and location name.
	 * 
	 * @param criteria the the search criteria
	 * @return the matching location, or <em>null</em> if not found
	 */
	PriceLocation findPriceLocation(PriceLocationCriteria criteria);
	
	/**
	 * Look up WeatherLocation based on a source name and location.
	 * 
	 * <p>At a minimum the {@link SourceLocation#getSource()} must be provided in the 
	 * supplied criteria.</p>
	 * 
	 * @param criteria the search criteria
	 * @return the matching locations, or an empty list if none found
	 */
	List<SourceLocationMatch> findWeatherLocation(SourceLocation criteria);
	
}