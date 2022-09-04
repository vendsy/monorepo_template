package com.tray.service.consumer;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tray.api.order.request.OrderRequest;
import com.tray.api.order.response.OrderResponse;

import com.tray.service.consumer.framework.FeatureTest;
import com.tray.service.consumer.framework.Requests;

/**
 * These are working examples of tests that sometimes are better done with the BasicSeleniumTest example but are here for completeness
 * so you can test the way you would like to test
 * 
 * @author dhiller
 *
 */
public class TestExample extends FeatureTest {

	private final static Logger log = LoggerFactory.getLogger(TestExample.class);

	/**
	 * Testing a synchronous controller may be easier especially if there is no remote communication.
	 */
	@Test
	public void testSynchronousController() throws ExecutionException, InterruptedException, TimeoutException {
		//move complex request building out of the test...
		OrderRequest req = Requests.createOrderRequest();

		//always call the client api we test in the test method so developers can find what we test
		//very easily.. (do not push this down behind a method as we have found it slows others down
		//and is the whole key point of the test)
		OrderResponse resp = ordersApi.order(req).get(5, TimeUnit.SECONDS);

		validate(resp);
	}

	private void validate(OrderResponse resp) {
		//next if you want, move assert logic into a validate method to re-use amongst tests
		Assertions.assertEquals(5, resp.getOrder().getId());

	}

}
