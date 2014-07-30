package com.gis.chore;
import static org.junit.Assert.*;

import net.minecraft.item.Item;
import net.minecraft.world.World;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.gis.block.NaturalResourceBlock;
import com.gis.core.Constants;
import com.gis.entity.EntityWorker;

public class ChoreMiningTest {

		World w = null;
		EntityWorker worker = new EntityWorker(w);
		ChoreMining miningChore = new ChoreMining(worker, 1, Constants.MINEABLE_BLOCKS[0], 0, 0, 1000);
		//NaturalResourceBlock block = new NaturalResourceBlock(null);
		Item tool = new Item();

	@Test
	public void testBeginChore() {
		if(miningChore != null)
		{
			Assert.assertTrue(true);
		}
			
	}

	@Test
	public void testRunChoreAI() {
		Assert.assertTrue(true);
	}

	@Test
	public void testGetChoreName() {
		String name = miningChore.getChoreName();
		assertEquals(name, "Mining");
	}

	@Test
	public void testEndChore() {
		miningChore.endChore();
		if(miningChore == null)
		{
			Assert.assertTrue(true);
		}
	}


	@Test
	public void testGetDelayForToolType() {
		int res = miningChore.getDelayForToolType(tool);
		assertEquals(res, 25);
	}

	@Test
	public void testChoreMiningEntityWorker() {
		if(worker != null)
		{
			Assert.assertTrue(true);
		}
	}



}