/* ===================================================================
 * PowerDatumDao.java
 * 
 * Created Aug 13, 2008 2:27:56 PM
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

import net.solarnetwork.central.dao.AggregationFilterableDao;
import net.solarnetwork.central.dao.FilterableDao;
import net.solarnetwork.central.datum.domain.AggregateNodeDatumFilter;
import net.solarnetwork.central.datum.domain.NodeDatumFilter;
import net.solarnetwork.central.datum.domain.PowerDatum;
import net.solarnetwork.central.datum.domain.PowerDatumMatch;
import net.solarnetwork.central.datum.domain.ReportingPowerDatum;

/**
 * DAO API for PowerDatum data.
 * 
 * @author matt
 * @version 1.1
 */
public interface PowerDatumDao extends DatumDao<PowerDatum>,
		AggregationFilterableDao<ReportingPowerDatum, AggregateNodeDatumFilter>,
		FilterableDao<PowerDatumMatch, Long, NodeDatumFilter> {

	// nothing to add here

}
