package com.anusha.spendsense_backend;
import java.util.TimeZone;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class SpendsenseBackendApplicationTests {

	static {
		java.util.TimeZone.setDefault(java.util.TimeZone.getTimeZone("Asia/Kolkata"));
	}

	@Test
	void contextLoads() {
	}

}
