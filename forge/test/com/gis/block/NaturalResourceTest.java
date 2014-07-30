package com.gis.block;

import static org.junit.Assert.*;

import org.junit.Assert;
import org.junit.Test;

public class NaturalResourceBlockTest {

	NaturalResourceBlock block = new NaturalResourceBlock(null);
	@Test
	public void test() {
		if(block!=null)
			Assert.assertTrue(true);
	}

}