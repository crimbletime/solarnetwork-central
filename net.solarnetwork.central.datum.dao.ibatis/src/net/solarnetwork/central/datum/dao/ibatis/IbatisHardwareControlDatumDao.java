/* ==================================================================
 * IbatisHardwareControlDatumDao.java - Sep 29, 2011 3:32:46 PM
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
 */

package net.solarnetwork.central.datum.dao.ibatis;

import java.util.Map;
import net.solarnetwork.central.datum.dao.HardwareControlDatumDao;
import net.solarnetwork.central.datum.domain.DatumQueryCommand;
import net.solarnetwork.central.datum.domain.HardwareControlDatum;
import net.solarnetwork.central.datum.domain.HardwareControlDatumMatch;
import net.solarnetwork.central.datum.domain.NodeDatumFilter;

/**
 * Ibatis implementation of {@link HardwareControlDatumDao}.
 * 
 * @author matt
 * @version 1.1
 */
public class IbatisHardwareControlDatumDao
		extends
		IbatisFilterableDatumDatoSupport<HardwareControlDatum, HardwareControlDatumMatch, NodeDatumFilter>
		implements HardwareControlDatumDao {

	/**
	 * Default constructor.
	 */
	public IbatisHardwareControlDatumDao() {
		super(HardwareControlDatum.class, HardwareControlDatumMatch.class);
	}

	@Override
	protected String setupAggregatedDatumQuery(DatumQueryCommand criteria, Map<String, Object> params) {
		throw new UnsupportedOperationException();
	}

}
