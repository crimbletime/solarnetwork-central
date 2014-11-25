/* ==================================================================
 * BaseMyBatisFilterableDatumDao.java - Nov 13, 2014 6:16:02 AM
 * 
 * Copyright 2007-2014 SolarNetwork.net Dev Team
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

package net.solarnetwork.central.datum.dao.mybatis;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.solarnetwork.central.dao.FilterableDao;
import net.solarnetwork.central.datum.domain.Datum;
import net.solarnetwork.central.domain.Filter;
import net.solarnetwork.central.domain.FilterMatch;
import net.solarnetwork.central.domain.FilterResults;
import net.solarnetwork.central.domain.SortDescriptor;
import net.solarnetwork.central.support.BasicFilterResults;
import org.apache.ibatis.session.RowBounds;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Base class for MyBatis {@link FilterableDao} implementations.
 * 
 * @author matt
 * @version 1.0
 */
public abstract class BaseMyBatisFilterableDatumDao<T extends Datum, M extends FilterMatch<Long>, F extends Filter>
		extends BaseMyBatisDatumDao<T> implements FilterableDao<M, Long, F> {

	/** A query property for a general Filter object value. */
	public static final String FILTER_PROPERTY = "filter";

	private final Class<? extends M> filterResultClass;

	/**
	 * Constructor.
	 * 
	 * @param domainClass
	 *        the domain class
	 * @param filterResultClass
	 *        the filter result class
	 */
	public BaseMyBatisFilterableDatumDao(Class<? extends T> domainClass,
			Class<? extends M> filterResultClass) {
		super(domainClass);
		this.filterResultClass = filterResultClass;
	}

	/**
	 * Get the filter query name for a given domain.
	 * 
	 * @param filterDomain
	 *        the domain
	 * @return query name
	 */
	protected String getFilteredQuery(String filterDomain, F filter) {
		return getQueryForAll() + "-" + filterDomain;
	}

	/**
	 * Callback to alter the default SQL properties set up by
	 * {@link #findFiltered(Filter, List, Integer, Integer)}.
	 * 
	 * @param filter
	 *        the current filter
	 * @param sqlProps
	 *        the properties
	 */
	protected void postProcessFilterProperties(F filter, Map<String, Object> sqlProps) {
		// nothing here, extending classes can implement
	}

	@Override
	// Propagation.REQUIRED for server-side cursors
	@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
	public FilterResults<M> findFiltered(F filter, List<SortDescriptor> sortDescriptors, Integer offset,
			Integer max) {
		final String filterDomain = getMemberDomainKey(filterResultClass);
		final String query = getFilteredQuery(filterDomain, filter);
		Map<String, Object> sqlProps = new HashMap<String, Object>(1);
		sqlProps.put(FILTER_PROPERTY, filter);
		if ( sortDescriptors != null && sortDescriptors.size() > 0 ) {
			sqlProps.put(SORT_DESCRIPTORS_PROPERTY, sortDescriptors);
		}
		postProcessFilterProperties(filter, sqlProps);

		// attempt count first, if max NOT specified as -1
		Long totalCount = null;
		if ( max != null && max.intValue() != -1 ) {
			final String countQuery = query + "-count";
			Number n = getSqlSession().selectOne(countQuery, sqlProps);
			if ( n != null ) {
				totalCount = n.longValue();
			}
		}

		List<M> rows = null;
		if ( offset != null && offset >= 0 && max != null && max > 0 ) {
			rows = getSqlSession().selectList(query, sqlProps, new RowBounds(offset, max));
		} else {
			rows = getSqlSession().selectList(query, sqlProps);
		}

		rows = postProcessFilterQuery(filter, rows);

		BasicFilterResults<M> results = new BasicFilterResults<M>(rows, (totalCount != null ? totalCount
				: Long.valueOf(rows.size())), offset, rows.size());

		return results;
	}

	/**
	 * Post-process filter query results.
	 * 
	 * <p>
	 * This implementation simply returns the passed on {@code rows}; subclasses
	 * can override this to process the filter results as needed.
	 * </p>
	 * 
	 * @param filter
	 *        the query filter
	 * @param rows
	 *        the result rows (never <em>null</em>)
	 * @return the processed rows
	 */
	protected List<M> postProcessFilterQuery(F filter, List<M> rows) {
		// subclasses can override
		return rows;
	}

}
