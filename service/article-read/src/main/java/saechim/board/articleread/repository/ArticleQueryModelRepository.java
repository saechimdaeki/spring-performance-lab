package saechim.board.articleread.repository;

import java.time.Duration;
import java.util.Optional;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;
import saechim.board.common.dataserializer.DataSerializer;

@Repository
@RequiredArgsConstructor
public class ArticleQueryModelRepository {

	private final StringRedisTemplate redisTemplate;

	private static final String KEY_FORMAT = "article-read::article::%s";

	public void create(ArticleQueryModel articleQueryModel, Duration ttl) {
		redisTemplate.opsForValue()
			.set(generateKey(articleQueryModel), DataSerializer.serialize(articleQueryModel), ttl);
	}

	public void update(ArticleQueryModel articleQueryModel) {
		redisTemplate.opsForValue()
			.setIfPresent(generateKey(articleQueryModel), DataSerializer.serialize(articleQueryModel));
	}

	public void delete(Long articleId) {
		redisTemplate.delete(generateKey(articleId));
	}

	public Optional<ArticleQueryModel> read(Long articleId) {
		return Optional.ofNullable(redisTemplate.opsForValue().get(generateKey(articleId)))
			.map(json -> DataSerializer.deserialize(json, ArticleQueryModel.class));
	}

	private String generateKey(ArticleQueryModel articleQueryModel) {
		return generateKey(articleQueryModel.getArticleId());
	}

	private String generateKey(Long articleId) {
		return KEY_FORMAT.formatted(articleId);
	}
}
