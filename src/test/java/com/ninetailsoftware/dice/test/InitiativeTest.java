package com.ninetailsoftware.dice.test;

import org.junit.Assert;
import org.junit.Test;

import com.ninetailsoftware.commands.Initiative;

public class InitiativeTest {

	@Test
	public void testTurnOrder() {
		Initiative initiative = new Initiative();
		String response = initiative.buildTurnOrder();
		
		Assert.assertEquals(response, "");
	}
	
}
