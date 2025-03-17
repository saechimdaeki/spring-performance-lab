CommentClientpackage saechim.board.articleread.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@RequiredArgsConstructor
public class LikeClient {
	private final RestClient restClient;

	@Value("${endpoints.saechim-board-like-service.url}")
	private String likeServiceUrl;

	@PostConstruct
	public void initRestClient() {
		restClient = RestClient.create(likeServiceUrl);
	}

	public long count(Long articleId) {
		try {
			return restClient.get()
				.uri("/v1/article-likes/articles/{articleId}/count", articleId)
				.retrieve()
				.body(Long.class);
		} catch (Exception e) {
			log.error("[LikeClient.read] articleId = {}", articleId, e);
			return 0;
		}
	}

}
