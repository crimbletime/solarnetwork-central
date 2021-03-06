/* ==================================================================
 * DelegatingQueryBiz.java - Dec 18, 2012 4:35:14 PM
 * 
 * Copyright 2007-2012 SolarNetwork.net Dev Team
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

package net.solarnetwork.central.query.support;

import java.util.List;
import java.util.Set;
import net.solarnetwork.central.datum.domain.AggregateGeneralLocationDatumFilter;
import net.solarnetwork.central.datum.domain.AggregateGeneralNodeDatumFilter;
import net.solarnetwork.central.datum.domain.Datum;
import net.solarnetwork.central.datum.domain.DatumFilter;
import net.solarnetwork.central.datum.domain.DatumQueryCommand;
import net.solarnetwork.central.datum.domain.GeneralLocationDatumFilter;
import net.solarnetwork.central.datum.domain.GeneralLocationDatumFilterMatch;
import net.solarnetwork.central.datum.domain.GeneralNodeDatumFilter;
import net.solarnetwork.central.datum.domain.GeneralNodeDatumFilterMatch;
import net.solarnetwork.central.datum.domain.NodeDatum;
import net.solarnetwork.central.datum.domain.ReportingGeneralLocationDatumMatch;
import net.solarnetwork.central.datum.domain.ReportingGeneralNodeDatumMatch;
import net.solarnetwork.central.domain.AggregationFilter;
import net.solarnetwork.central.domain.Entity;
import net.solarnetwork.central.domain.EntityMatch;
import net.solarnetwork.central.domain.FilterResults;
import net.solarnetwork.central.domain.Location;
import net.solarnetwork.central.domain.LocationMatch;
import net.solarnetwork.central.domain.SortDescriptor;
import net.solarnetwork.central.domain.SourceLocation;
import net.solarnetwork.central.domain.SourceLocationMatch;
import net.solarnetwork.central.query.biz.QueryBiz;
import net.solarnetwork.central.query.domain.ReportableInterval;
import net.solarnetwork.central.query.domain.WeatherConditions;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;

/**
 * Delegating implementation of {@link QueryBiz}, mostly to help with AOP.
 * 
 * @author matt
 * @version 1.5
 */
public class DelegatingQueryBiz implements QueryBiz {

	private final QueryBiz delegate;

	/**
	 * Construct with a delegate.
	 * 
	 * @param delegate
	 *        the delegate
	 */
	public DelegatingQueryBiz(QueryBiz delegate) {
		super();
		this.delegate = delegate;
	}

	@Override
	public ReportableInterval getReportableInterval(Long nodeId, Class<? extends NodeDatum>[] types) {
		return delegate.getReportableInterval(nodeId, types);
	}

	@Override
	public ReportableInterval getNetworkReportableInterval(Class<? extends NodeDatum>[] types) {
		return delegate.getNetworkReportableInterval(types);
	}

	@Override
	public Set<String> getAvailableSources(Long nodeId, Class<? extends NodeDatum> type,
			LocalDate start, LocalDate end) {
		return delegate.getAvailableSources(nodeId, type, start, end);
	}

	@Override
	public List<? extends NodeDatum> getAggregatedDatum(DatumQueryCommand criteria,
			Class<? extends NodeDatum> datumClass) {
		return delegate.getAggregatedDatum(criteria, datumClass);
	}

	@Override
	public WeatherConditions getMostRecentWeatherConditions(Long nodeId) {
		return delegate.getMostRecentWeatherConditions(nodeId);
	}

	@Override
	public <F extends DatumFilter> FilterResults<? extends EntityMatch> findFilteredDatum(F filter,
			Class<? extends Datum> datumClass, List<SortDescriptor> sortDescriptors, Integer offset,
			Integer max) {
		return delegate.findFilteredDatum(filter, datumClass, sortDescriptors, offset, max);
	}

	@Override
	public FilterResults<SourceLocationMatch> findFilteredLocations(SourceLocation filter,
			Class<? extends Entity<?>> locationClass, List<SortDescriptor> sortDescriptors,
			Integer offset, Integer max) {
		return delegate.findFilteredLocations(filter, locationClass, sortDescriptors, offset, max);
	}

	@Override
	public <A extends AggregationFilter> FilterResults<?> findFilteredAggregateDatum(A filter,
			Class<? extends Datum> datumClass, List<SortDescriptor> sortDescriptors, Integer offset,
			Integer max) {
		return delegate.findFilteredAggregateDatum(filter, datumClass, sortDescriptors, offset, max);
	}

	@Override
	public ReportableInterval getReportableInterval(Long nodeId, String sourceId) {
		return delegate.getReportableInterval(nodeId, sourceId);
	}

	@Override
	public Set<String> getAvailableSources(Long nodeId, DateTime start, DateTime end) {
		return delegate.getAvailableSources(nodeId, start, end);
	}

	@Override
	public FilterResults<GeneralNodeDatumFilterMatch> findFilteredGeneralNodeDatum(
			GeneralNodeDatumFilter filter, List<SortDescriptor> sortDescriptors, Integer offset,
			Integer max) {
		return delegate.findFilteredGeneralNodeDatum(filter, sortDescriptors, offset, max);
	}

	@Override
	public FilterResults<ReportingGeneralNodeDatumMatch> findFilteredAggregateGeneralNodeDatum(
			AggregateGeneralNodeDatumFilter filter, List<SortDescriptor> sortDescriptors,
			Integer offset, Integer max) {
		return delegate.findFilteredAggregateGeneralNodeDatum(filter, sortDescriptors, offset, max);
	}

	@Override
	public FilterResults<LocationMatch> findFilteredLocations(Location filter,
			List<SortDescriptor> sortDescriptors, Integer offset, Integer max) {
		return delegate.findFilteredLocations(filter, sortDescriptors, offset, max);
	}

	@Override
	public FilterResults<GeneralLocationDatumFilterMatch> findGeneralLocationDatum(
			GeneralLocationDatumFilter filter, List<SortDescriptor> sortDescriptors, Integer offset,
			Integer max) {
		return delegate.findGeneralLocationDatum(filter, sortDescriptors, offset, max);
	}

	@Override
	public FilterResults<ReportingGeneralLocationDatumMatch> findAggregateGeneralLocationDatum(
			AggregateGeneralLocationDatumFilter filter, List<SortDescriptor> sortDescriptors,
			Integer offset, Integer max) {
		return delegate.findAggregateGeneralLocationDatum(filter, sortDescriptors, offset, max);
	}

	@Override
	public Set<String> getLocationAvailableSources(Long locationId, DateTime start, DateTime end) {
		return delegate.getLocationAvailableSources(locationId, start, end);
	}

	@Override
	public ReportableInterval getLocationReportableInterval(Long locationId, String sourceId) {
		return delegate.getLocationReportableInterval(locationId, sourceId);
	}

}
