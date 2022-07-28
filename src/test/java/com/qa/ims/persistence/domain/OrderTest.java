package com.qa.ims.persistence.domain;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;


public class OrderTest {

	@Test
	public void testEquals() {
		EqualsVerifier.simple().forClass(Order.class).withIgnoredFields("customerDao").verify();
	}

}
