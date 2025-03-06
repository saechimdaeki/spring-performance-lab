package saechim.board.hotarticle.repository;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class ArticleCreatedTimeRepository {

	private final StringRedisTemplate redisTemplate;

	// hot-article::article::{articleId}::created-time
	private static final String KEY_FORMAT = "hot-article::article::%s::created-time";

	public void createOrUpdate(Long articleId, LocalDateTime createdAt, Duration ttl) {
		redisTemplate.opsForValue()
			.set(
				generateKey(articleId),
				String.valueOf(createdAt.toInstant(ZoneOffset.UTC).toEpochMilli()),
				ttl
			);
	}

	public void delete(Long articleId) {
		redisTemplate.delete(generateKey(articleId));
	}

	public LocalDateTime read(Long articleId) {
		String result = redisTemplate.opsForValue().get(generateKey(articleId));
		if (!StringUtils.hasText(result)) {
			return null;
		}
		return LocalDateTime.ofInstant(
			Instant.ofEpochMilli(Long.parseLong(result)), ZoneOffset.UTC
		);
	}

	private String generateKey(Long articleId) {
		return String.format(KEY_FORMAT, articleId);
	}
}
