package com.redislabs.lettusearch;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.redislabs.lettusearch.RediSearchUtils.Info;

public class TestUtils extends AbstractBaseTest {

	@Test
	public void testFtInfo() {
		List<Object> infoList = commands.ftInfo(Beers.INDEX);
		Info info = RediSearchUtils.getInfo(infoList);
		Assert.assertEquals(2348, info.getNumDocs());
		Assert.assertEquals(16709, info.getNumRecords());
		Assert.assertEquals(4162, info.getNumTerms());

	}

}
