/* ==================================================================
 * DatumFilterCommand.java - Dec 2, 2013 5:39:51 PM
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

package net.solarnetwork.central.datum.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import net.solarnetwork.central.domain.Aggregation;
import net.solarnetwork.central.domain.Location;
import net.solarnetwork.central.domain.SolarLocation;
import net.solarnetwork.central.domain.SortDescriptor;
import net.solarnetwork.central.support.MutableSortDescriptor;
import org.joda.time.DateTime;

/**
 * Implementation of {@link LocationDatumFilter}, {@link NodeDatumFilter}, and
 * {@link AggregateNodeDatumFilter}, and {@link GeneralNodeDatumFilter}.
 * 
 * @author matt
 * @version 1.5
 */
public class DatumFilterCommand implements LocationDatumFilter, NodeDatumFilter,
		AggregateNodeDatumFilter, GeneralLocationDatumFilter, AggregateGeneralLocationDatumFilter,
		GeneralNodeDatumFilter, AggregateGeneralNodeDatumFilter, GeneralLocationDatumMetadataFilter,
		GeneralNodeDatumMetadataFilter {

	private final SolarLocation location;
	private DateTime startDate;
	private DateTime endDate;
	private boolean mostRecent = false;
	private String type; // e.g. Power, Consumption, etc.
	private List<MutableSortDescriptor> sorts;
	private Integer offset = 0;
	private Integer max;
	private String dataPath; // bean path expression to a data value, e.g. "i.watts"

	private Long[] locationIds;
	private Long[] nodeIds;
	private String[] sourceIds;
	private String[] tags;
	private Aggregation aggregation;

	/**
	 * Default constructor.
	 */
	public DatumFilterCommand() {
		super();
		location = new SolarLocation();
	}

	/**
	 * Construct from a Location filter.
	 * 
	 * @param loc
	 *        the location
	 */
	public DatumFilterCommand(Location loc) {
		super();
		if ( loc instanceof SolarLocation ) {
			location = (SolarLocation) loc;
		} else {
			location = new SolarLocation(loc);
		}
	}

	@Override
	public Map<String, ?> getFilter() {
		Map<String, Object> filter = new LinkedHashMap<String, Object>();
		if ( location.getId() != null ) {
			filter.put("locationId", location.getId());
		}
		if ( startDate != null ) {
			filter.put("start", startDate);
		}
		if ( endDate != null ) {
			filter.put("end", endDate);
		}
		if ( location != null ) {
			filter.putAll(location.getFilter());
		}
		if ( nodeIds != null ) {
			filter.put("nodeIds", nodeIds);
		}
		if ( sourceIds != null ) {
			filter.put("sourceIds", sourceIds);
		}
		if ( startDate != null ) {
			filter.put("start", startDate);
		}
		if ( endDate != null ) {
			filter.put("end", endDate);
		}
		if ( aggregation != null ) {
			filter.put("aggregation", aggregation.toString());
		}
		return filter;
	}

	public boolean isHasLocationCriteria() {
		return (location != null && location.getFilter().size() > 0);
	}

	public void setLocationId(Long id) {
		location.setId(id);
	}

	@Override
	public Long getLocationId() {
		if ( location.getId() != null ) {
			return location.getId();
		}
		if ( locationIds != null && locationIds.length > 0 ) {
			return locationIds[0];
		}
		return null;
	}

	@Override
	public Location getLocation() {
		return location;
	}

	@Override
	public DateTime getStartDate() {
		return startDate;
	}

	public void setStartDate(DateTime startDate) {
		this.startDate = startDate;
	}

	@Override
	public DateTime getEndDate() {
		return endDate;
	}

	public void setEndDate(DateTime endDate) {
		this.endDate = endDate;
	}

	@Override
	public String getType() {
		return type;
	}

	public void setType(String datumType) {
		this.type = datumType;
	}

	public List<MutableSortDescriptor> getSorts() {
		return sorts;
	}

	public void setSorts(List<MutableSortDescriptor> sorts) {
		this.sorts = sorts;
	}

	public List<SortDescriptor> getSortDescriptors() {
		if ( sorts == null ) {
			return Collections.emptyList();
		}
		return new ArrayList<SortDescriptor>(sorts);
	}

	public Integer getOffset() {
		return offset;
	}

	public void setOffset(Integer offset) {
		this.offset = offset;
	}

	public Integer getMax() {
		return max;
	}

	public void setMax(Integer max) {
		this.max = max;
	}

	/**
	 * Set a single node ID.
	 * 
	 * <p>
	 * This is a convenience method for requests that use a single node ID at a
	 * time. The node ID is still stored on the {@code nodeIds} array, just as
	 * the first value. Calling this method replaces any existing
	 * {@code nodeIds} value with a new array containing just the ID passed into
	 * this method.
	 * </p>
	 * 
	 * @param nodeId
	 *        the ID of the node
	 */
	public void setNodeId(Long nodeId) {
		this.nodeIds = new Long[] { nodeId };
	}

	/**
	 * Get the first node ID.
	 * 
	 * <p>
	 * This returns the first available node ID from the {@code nodeIds} array,
	 * or <em>null</em> if not available.
	 * </p>
	 * 
	 * @return the first node ID
	 */
	@Override
	public Long getNodeId() {
		return this.nodeIds == null || this.nodeIds.length < 1 ? null : this.nodeIds[0];
	}

	/**
	 * Set a single source ID.
	 * 
	 * <p>
	 * This is a convenience method for requests that use a single source ID at
	 * a time. The source ID is still stored on the {@code sourceIds} array,
	 * just as the first value. Calling this method replaces any existing
	 * {@code sourceIds} value with a new array containing just the ID passed
	 * into this method.
	 * </p>
	 * 
	 * @param nodeId
	 *        the ID of the node
	 */
	public void setSourceId(String sourceId) {
		if ( sourceId == null ) {
			this.sourceIds = null;
		} else {
			this.sourceIds = new String[] { sourceId };
		}
	}

	/**
	 * Get the first source ID.
	 * 
	 * <p>
	 * This returns the first available source ID from the {@code sourceIds}
	 * array, or <em>null</em> if not available.
	 * </p>
	 * 
	 * @return the first node ID
	 */
	@Override
	public String getSourceId() {
		return this.sourceIds == null || this.sourceIds.length < 1 ? null : this.sourceIds[0];
	}

	@Override
	public Long[] getNodeIds() {
		return nodeIds;
	}

	public void setNodeIds(Long[] nodeIds) {
		this.nodeIds = nodeIds;
	}

	@Override
	public String[] getSourceIds() {
		return sourceIds;
	}

	public void setSourceIds(String[] sourceIds) {
		this.sourceIds = sourceIds;
	}

	@Override
	public String getTag() {
		return this.tags == null || this.tags.length < 1 ? null : this.tags[0];
	}

	@Override
	public String[] getTags() {
		return tags;
	}

	public void setTags(String[] tags) {
		this.tags = tags;
	}

	@Override
	public Aggregation getAggregation() {
		return aggregation;
	}

	public void setAggregation(Aggregation aggregation) {
		this.aggregation = aggregation;
	}

	/**
	 * Calls {@link #setAggregation(Aggregation)} for backwards API
	 * compatibility.
	 * 
	 * @param aggregate
	 *        the aggregation to set
	 */
	public void setAggregate(Aggregation aggregate) {
		setAggregation(aggregate);
	}

	@Override
	public boolean isMostRecent() {
		return mostRecent;
	}

	public void setMostRecent(boolean mostRecent) {
		this.mostRecent = mostRecent;
	}

	@Override
	public String getDataPath() {
		return dataPath;
	}

	public void setDataPath(String dataPath) {
		this.dataPath = dataPath;
	}

	@Override
	public String[] getDataPathElements() {
		String path = this.dataPath;
		if ( path == null ) {
			return null;
		}
		return path.split("\\.");
	}

	@Override
	public Long[] getLocationIds() {
		if ( locationIds != null ) {
			return locationIds;
		}
		if ( location != null && location.getId() != null ) {
			return new Long[] { location.getId() };
		}
		return null;
	}

	public void setLocationIds(Long[] locationIds) {
		this.locationIds = locationIds;
	}

}
