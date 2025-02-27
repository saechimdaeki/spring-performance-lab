package saechim.board.view.repository;

import java.time.Duration;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class ArticleViewDistributedLockRepository {
	private final StringRedisTemplate redisTemplate;

	// view::article::{articleId}::user::{user_id}::lock
	private static final String KEY_FORMAT = "view::article::%s::user::%s::lock";

	public boolean lock(Long articleId, Long userId, Duration timeout) {
		String key = generateKey(articleId, userId);
		return redisTemplate.opsForValue().setIfAbsent(key, "", timeout);
	}

	private String generateKey(Long articleId, Long userId) {
		return String.format(KEY_FORMAT, articleId, userId);
	}
}
