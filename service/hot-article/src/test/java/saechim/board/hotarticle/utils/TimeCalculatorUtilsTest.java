package saechim.board.hotarticle.utils;

import java.time.Duration;

import org.junit.jupiter.api.Test;

import lombok.extern.slf4j.Slf4j;

@Slf4j
class TimeCalculatorUtilsTest {

	@Test
	void timeToMidnight() {
		Duration duration = TimeCalculatorUtils.calculateDurationToMidnight();
		log.info("duration.getSeconds() / 60 = {}", duration.getSeconds() / 60);
	}

}