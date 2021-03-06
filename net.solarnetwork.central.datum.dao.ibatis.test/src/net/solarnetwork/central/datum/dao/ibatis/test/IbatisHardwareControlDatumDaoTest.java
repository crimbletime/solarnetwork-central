/* ==================================================================
 * IbatisHardwareControlDatumDaoTest.java - Sep 29, 2011 3:44:20 PM
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

package net.solarnetwork.central.datum.dao.ibatis.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import net.solarnetwork.central.datum.dao.ibatis.IbatisHardwareControlDatumDao;
import net.solarnetwork.central.datum.domain.DatumFilterCommand;
import net.solarnetwork.central.datum.domain.DatumQueryCommand;
import net.solarnetwork.central.datum.domain.HardwareControlDatum;
import net.solarnetwork.central.datum.domain.HardwareControlDatumMatch;
import net.solarnetwork.central.domain.FilterResults;
import net.solarnetwork.central.domain.SortDescriptor;
import net.solarnetwork.central.support.SimpleSortDescriptor;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Test case for the {@link IbatisHardwareControlDatumDao} class.
 * 
 * @author matt
 * @version 1.1
 */
public class IbatisHardwareControlDatumDaoTest extends AbstractIbatisDaoTestSupport {

	@Autowired
	private IbatisHardwareControlDatumDao dao;

	private HardwareControlDatum lastDatum;

	@Before
	public void setUp() throws Exception {
		lastDatum = null;
		setupTestHardware();
		setupTestHardwareControl();
	}

	private HardwareControlDatum createDatum(Long nodeId, String sourceId, Integer integerValue,
			Float floatValue) {
		HardwareControlDatum datum = new HardwareControlDatum();
		datum.setCreated(new DateTime());
		datum.setIntegerValue(integerValue);
		datum.setFloatValue(floatValue);
		datum.setNodeId(nodeId);
		datum.setSourceId(sourceId);
		return datum;
	}

	@Test
	public void storeNew() {
		HardwareControlDatum datum = createDatum(TEST_NODE_ID, "/test/source", 100, 1.2F);
		Long id = dao.store(datum);
		assertNotNull(id);
		datum.setId(id);
		lastDatum = datum;
	}

	private void validate(HardwareControlDatum src, HardwareControlDatum entity) {
		assertNotNull("HardwareControlDatum should exist", entity);
		assertNotNull("Created date should be set", entity.getCreated());
		assertEquals(src.getIntegerValue(), entity.getIntegerValue());
		assertEquals(src.getFloatValue(), entity.getFloatValue());
		assertEquals(src.getNodeId(), entity.getNodeId());
		assertEquals(src.getSourceId(), entity.getSourceId());
	}

	@Test
	public void getByPrimaryKey() {
		storeNew();
		HardwareControlDatum datum = dao.get(lastDatum.getId());
		validate(lastDatum, datum);
	}

	@Test
	public void getByDate() {
		storeNew();
		HardwareControlDatum datum = dao.getDatumForDate(TEST_NODE_ID, lastDatum.getCreated());
		validate(lastDatum, datum);
	}

	@Test
	public void getMostRecent() {
		storeNew();

		DatumQueryCommand criteria = new DatumQueryCommand();
		criteria.setNodeId(TEST_NODE_ID);

		List<HardwareControlDatum> results = dao.getMostRecentDatum(criteria);
		assertNotNull(results);
		assertEquals(1, results.size());
		assertEquals(lastDatum, results.get(0));

		HardwareControlDatum datum2 = new HardwareControlDatum();
		datum2.setCreated(new DateTime().plusHours(1));
		datum2.setNodeId(TEST_NODE_ID);
		datum2.setSourceId(lastDatum.getSourceId());
		datum2.setIntegerValue(Integer.valueOf(10));
		Long id2 = dao.store(datum2);

		results = dao.getMostRecentDatum(criteria);
		assertNotNull(results);
		assertEquals(1, results.size());
		assertEquals(id2, results.get(0).getId());

		HardwareControlDatum datum3 = new HardwareControlDatum();
		datum3.setNodeId(TEST_NODE_ID);
		datum3.setSourceId("/test/source/2");
		datum3.setIntegerValue(Integer.valueOf(5));
		Long id3 = dao.store(datum3);

		results = dao.getMostRecentDatum(criteria);
		assertNotNull(results);
		assertEquals(2, results.size());
		Set<Long> ids = new LinkedHashSet<Long>();
		for ( HardwareControlDatum d : results ) {
			ids.add(d.getId());
		}
		assertTrue(ids.contains(id2));
		assertTrue(ids.contains(id3));
	}

	@Test
	public void filterIterateAll() {
		List<Long> ids = new ArrayList<Long>(10);
		// make sure created dates are different and ascending
		final int numDatum = 10;
		final long created = System.currentTimeMillis() - (1000 * numDatum);
		for ( int i = 0; i < numDatum; i++ ) {
			HardwareControlDatum d = createDatum(TEST_NODE_ID, "/test/source",
					(int) Math.round(Math.random() * 100), (float) (Math.random() * 10));
			d.setCreated(new DateTime(created + (i * 1000)));
			ids.add(dao.store(d));
		}
		DatumFilterCommand filter = new DatumFilterCommand();
		filter.setNodeId(TEST_NODE_ID);
		List<SortDescriptor> sorts = Collections
				.singletonList((SortDescriptor) new SimpleSortDescriptor("created", true));
		int offset = 0;
		final int maxResults = 2;
		while ( offset < 8 ) {
			FilterResults<HardwareControlDatumMatch> matches = dao.findFiltered(filter, sorts, offset,
					maxResults);
			assertNotNull(matches);
			assertEquals(Integer.valueOf(2), matches.getReturnedResultCount());
			Iterator<HardwareControlDatumMatch> itr = matches.getResults().iterator();
			for ( int i = 0; i < maxResults; i++, offset++ ) {
				HardwareControlDatumMatch match = itr.next();
				assertEquals(created + ((numDatum - offset - 1) * 1000), match.getCreated().getMillis());
			}
		}
	}
}
