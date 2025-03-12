package saechim.board.common.outboxmessagerelay;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.connection.StringRedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class MessageRelayCoordinator {
	private final StringRedisTemplate stringRedisTemplate;

	@Value("${spring.applicaiton.name}")
	private String applicationName;

	private final String APP_ID = UUID.randomUUID().toString();

	private final int PING_INTERNAL_SECONDS = 3;
	private final int PING_FAILURE_THRESHOLD = 3;

	public AssignedShard assigned() {
		return AssignedShard.of(APP_ID, findAppIds(), MessageRelayConstants.SHARD_COUNT);
	}

	private List<String> findAppIds() {
		return stringRedisTemplate.opsForZSet().reverseRange(generateKey(), 0, -1).stream().sorted().toList();
	}

	@Scheduled(fixedRate = PING_INTERNAL_SECONDS, timeUnit = TimeUnit.SECONDS)
	public void ping() {
		stringRedisTemplate.executePipelined((RedisCallback<?>)action -> {
			StringRedisConnection conn = (StringRedisConnection)action;
			String key = generateKey();
			conn.zAdd(key, Instant.now().toEpochMilli(), APP_ID);
			conn.zRemRangeByScore(
				key,
				Double.NEGATIVE_INFINITY,
				Instant.now().minusSeconds(PING_INTERNAL_SECONDS * PING_FAILURE_THRESHOLD).toEpochMilli()
			);
			return null;
		});
	}

	@PreDestroy
	public void leave() {
		stringRedisTemplate.opsForZSet().remove(generateKey(), APP_ID);
	}

	private String generateKey() {
		return "message-relay-coordinator::app-list::%s".formatted(applicationName);
	}
}
