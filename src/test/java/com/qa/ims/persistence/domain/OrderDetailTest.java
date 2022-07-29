package com.qa.ims.persistence.domain;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;


public class OrderDetailTest {

	@Test
	public void testEquals() {
		EqualsVerifier.simple().forClass(OrderDetail.class).verify();
	}

}
