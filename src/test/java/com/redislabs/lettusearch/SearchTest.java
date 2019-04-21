package com.redislabs.lettusearch;

import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import com.redislabs.lettusearch.search.Limit;
import com.redislabs.lettusearch.search.SearchOptions;
import com.redislabs.lettusearch.search.SearchResults;

public class SearchTest extends AbstractBaseTest {

	@Test
	public void testPhoneticFields() {
		SearchResults<String, String> results = connection.sync().search(INDEX, "@style:pail",
				SearchOptions.builder().build());
		Assert.assertEquals(445, results.getCount());
	}

	@Test
	public void testSearchNoContent() {
		SearchResults<String, String> results = connection.sync().search(INDEX, "Hefeweizen", SearchOptions.builder()
				.withScores(true).noContent(true).limit(Limit.builder().num(100).build()).build());
		Assert.assertEquals(42, results.getCount());
		Assert.assertEquals(42, results.getResults().size());
		Assert.assertEquals("1836", results.getResults().get(0).getDocumentId());
		Assert.assertEquals(7.5, results.getResults().get(0).getScore(), 0.000001);
	}

	@Test
	public void testGet() {
		Map<String, String> map = connection.sync().get(INDEX, "1836");
		Assert.assertEquals("Widmer Brothers Hefeweizen", map.get("name"));
	}

	@Test
	public void testDel() {
		boolean deleted = connection.sync().del(INDEX, "1836", true);
		Assert.assertTrue(deleted);
		Map<String, String> map = connection.sync().get(INDEX, "1836");
		Assert.assertNull(map);
	}

}
