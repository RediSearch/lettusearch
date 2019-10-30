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
		Assert.assertEquals((Long) 2348l, info.getNumDocs());
		Assert.assertEquals((Long) 16709l, info.getNumRecords());
		Assert.assertEquals((Long) 4162l, info.getNumTerms());

	}

}
