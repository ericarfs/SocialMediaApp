package ericarfs.socialmedia;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@SpringBootTest
@Import(TestCacheConfig.class)
class SocialmediaApplicationTests {

	@Test
	void contextLoads() {
	}
}
