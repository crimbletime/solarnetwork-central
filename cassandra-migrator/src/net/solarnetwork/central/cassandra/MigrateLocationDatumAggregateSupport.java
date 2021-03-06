/* ==================================================================
 * MigrateLocationDatumAggregateSupport.java - Nov 25, 2013 4:48:59 PM
 * 
 * Copyright 2007-2013 SolarNetwork.net Dev Team
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

package net.solarnetwork.central.cassandra;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Calendar;
import com.datastax.driver.core.BoundStatement;

/**
 * Support for populating aggregate location datum tables.
 * 
 * @author matt
 * @version 1.0
 */
public abstract class MigrateLocationDatumAggregateSupport extends MigrateLocationDatumSupport {

	public static final String DEFAULT_CQL = "INSERT INTO solardata.loc_datum_agg (loc_id, ltype, atype, year, ts, data_num) "
			+ "VALUES (?, ?, ?, ?, ?, ?)";

	/**
	 * Default constructor.
	 */
	public MigrateLocationDatumAggregateSupport() {
		super();
		setCql(DEFAULT_CQL);
	}

	/**
	 * Copy constructor.
	 * 
	 * @param other
	 */
	public MigrateLocationDatumAggregateSupport(MigrateDatumSupport other) {
		super(other);
	}

	protected abstract int getAggregateType();

	/**
	 * Get a BoundStatement with the primary key values bound.
	 * 
	 * <p>
	 * The ResultSet must provide the following result columns, in this order:
	 * </p>
	 * 
	 * <ol>
	 * <li>loc_id</li>
	 * <li>created timestamp</li>
	 * </ol>
	 * 
	 * @param rs
	 *        the ResultSet
	 * @param cStmt
	 *        the PreparedStatement
	 * @return a new BoundStatement
	 * @throws SQLException
	 */
	@Override
	protected BoundStatement getBoundStatementForResultRowMapping(ResultSet rs,
			com.datastax.driver.core.PreparedStatement cStmt) throws SQLException {
		BoundStatement bs = new BoundStatement(cStmt);
		bs.setString(0, rs.getObject(1).toString());
		bs.setInt(1, getDatumType());
		bs.setInt(2, getAggregateType());

		Timestamp created = rs.getTimestamp(2);
		gmtCalendar.setTimeInMillis(created.getTime());
		bs.setInt(3, gmtCalendar.get(Calendar.YEAR));

		bs.setDate(4, created);
		return bs;
	}

	@Override
	protected int getBoundStatementMapParameterIndex() {
		return 5;
	}

}
